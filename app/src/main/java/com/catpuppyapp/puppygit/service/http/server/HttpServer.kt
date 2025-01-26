package com.catpuppyapp.puppygit.service.http.server

import com.catpuppyapp.puppygit.data.entity.RepoEntity
import com.catpuppyapp.puppygit.etc.Ret
import com.catpuppyapp.puppygit.settings.AppSettings
import com.catpuppyapp.puppygit.settings.SettingsUtil
import com.catpuppyapp.puppygit.utils.AppModel
import com.catpuppyapp.puppygit.utils.Libgit2Helper
import com.catpuppyapp.puppygit.utils.MyLog
import com.catpuppyapp.puppygit.utils.createAndInsertError
import com.catpuppyapp.puppygit.utils.dbIntToBool
import com.catpuppyapp.puppygit.utils.doJobThenOffLoading
import com.catpuppyapp.puppygit.utils.getSecFromTime
import com.github.git24j.core.Repository
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.host
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

private const val TAG = "HttpServer"

object HttpServer {
    private val lock = Mutex()
    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine. Configuration>? = null

    /**
     * expect do start/stop/restart server in `act`
     */
    suspend fun doActWithLock(act:suspend HttpServer.() -> Unit) {
        lock.withLock {
            this.act()
        }
    }

    suspend fun startServer(settings: AppSettings):Exception? {
        if(isServerRunning()) return null

        try {

            server = embeddedServer(Netty, host = settings.httpService.listenHost, port = settings.httpService.listenPort) {
                install(ContentNegotiation) {
                    //忽略对象里没有的key；编码默认值；紧凑格式
                    json(Json{ ignoreUnknownKeys = true; encodeDefaults=true; prettyPrint = false})
                }
                routing {
                    get("/ping") {
                        call.respond(createSuccessResult("pong"))
                    }

                    // for test
                    get("/echo/{msg}") {
                        call.respond(createSuccessResult(call.parameters.get("msg") ?: ""))
                    }

                    /**
                     * query params:
                     *  repoNameOrId: repo name or id，优先查name，若无匹配，查id
                     *  gitUsername: using for create commit, if null, will use PuppyGit settings
                     *  gitEmail: using for create commit, if nul, will use PuppyGit settings
                     *  masterPass: your master password, if have
                     *  forceUseIdMatchRepo: 1 or 0, if true, will force match repo by repo id, else will match by name first, if no match, then match by id
                     *  token: if caller ip not in white list, token is required

                     * e.g.
                     * request: http://127.0.0.1/pull?repoNameOrId=abc
                     */
                    get("/pull") {
                        val routeName = "'/pull'"

                        val callerIp = call.request.host()
                        val token = call.request.queryParameters.get("token")
                        val settings = SettingsUtil.getSettingsSnapshot()
                        val tokenCheckRet = tokenCheck(token, callerIp, settings)
                        if(tokenCheckRet.hasError()) {
                            // log the query params maybe better?
                            MyLog.e(TAG, "request rejected: route=$routeName, ip=$callerIp, token=$token, reason=${tokenCheckRet.msg}")
                            call.respond(createErrResult(tokenCheckRet.msg))
                            return@get
                        }

                        val repoNameOrId = call.request.queryParameters.get("repoNameOrId")
                        if(repoNameOrId == null || repoNameOrId.isBlank()) {
                            call.respond(createErrResult("invalid repo name or id"))
                            return@get
                        }

                        val forceUseIdMatchRepo = call.request.queryParameters.get("forceUseIdMatchRepo") == "1"

                        val masterPasswordFromUrl = call.request.queryParameters.get("masterPass") ?: ""

                        val masterPassword = masterPasswordFromUrl.ifEmpty { AppModel.masterPassword.value }

                        var repoForLog:RepoEntity? = null
                        // 查询仓库是否存在
                        // 尝试获取仓库锁，若获取失败，返回仓库正在执行其他操作
                        doJobThenOffLoading {
                            try {
                                val db = AppModel.dbContainer
                                val repoRet = db.repoRepository.getByNameOrId(repoNameOrId, forceUseIdMatchRepo)
                                if(repoRet.hasError()) {
                                    throw RuntimeException(repoRet.msg)
                                }

                                val repoFromDb = repoRet.data!!
                                repoForLog = repoFromDb

                                Libgit2Helper.doActWithRepoLock(repoFromDb) {
                                    if(dbIntToBool(repoFromDb.isDetached)) {
                                        throw RuntimeException("repo is detached")
                                    }

                                    if(!Libgit2Helper.isValidGitRepo(repoFromDb.fullSavePath)) {
                                        throw RuntimeException("invalid git repo")
                                    }

                                    Repository.open(repoFromDb.fullSavePath).use { gitRepo ->
                                        val upstream = Libgit2Helper.getUpstreamOfBranch(gitRepo, repoFromDb.branch)
                                        if(upstream.remote.isBlank() || upstream.branchRefsHeadsFullRefSpec.isBlank()) {
                                            throw RuntimeException("invalid upstream")
                                        }

                                        // get fetch credential
                                        val credential = Libgit2Helper.getRemoteCredential(
                                            db.remoteRepository,
                                            db.credentialRepository,
                                            repoFromDb.id,
                                            upstream.remote,
                                            trueFetchFalsePush = true,
                                            masterPassword = masterPassword
                                        )

                                        // fetch
                                        Libgit2Helper.fetchRemoteForRepo(gitRepo, upstream.remote, credential, repoFromDb)


                                        // 更新修改workstatus的时间，只更新时间就行，状态会在查询repo时更新
                                        val repoDb = AppModel.dbContainer.repoRepository
                                        repoDb.updateLastUpdateTime(repoFromDb.id, getSecFromTime())

                                        // get git username and email for merge
                                        val usernameFromUrl = call.request.queryParameters.get("gitUsername") ?:""
                                        val emailFromUrl = call.request.queryParameters.get("gitEmail") ?:""
                                        val (username, email) = if(usernameFromUrl.isNotBlank() && emailFromUrl.isNotBlank()) {
                                            Pair(usernameFromUrl, emailFromUrl)
                                        }else{
                                            val (gitUsernameFromConfig, gitEmailFromConfig) = Libgit2Helper.getGitUsernameAndEmail(gitRepo)
                                            val finallyUsername = usernameFromUrl.ifBlank { gitUsernameFromConfig }

                                            val finallyEmail = emailFromUrl.ifBlank { gitEmailFromConfig }

                                            Pair(finallyUsername, finallyEmail)
                                        }

                                        if(username == null || username.isBlank()) {
                                            throw RuntimeException("git username invalid")
                                        }

                                        if(email == null || email.isBlank()) {
                                            throw RuntimeException("git email invalid")
                                        }

                                        val remoteRefSpec = Libgit2Helper.getUpstreamRemoteBranchShortNameByRemoteAndBranchRefsHeadsRefSpec(
                                            upstream!!.remote,
                                            upstream.branchRefsHeadsFullRefSpec
                                        )

                                        //merge
                                        val mergeRet = Libgit2Helper.mergeOneHead(
                                            gitRepo,
                                            remoteRefSpec,
                                            username,
                                            email,
                                            settings = settings
                                        )

                                        if(mergeRet.hasError()) {
                                            throw RuntimeException(mergeRet.msg)
                                        }

                                    }

                                    call.respond(createSuccessResult())
                                }
                            }catch (e:Exception) {
                                val errMsg = e.localizedMessage ?: "unknown err"
                                call.respond(createErrResult(errMsg))

                                if(repoForLog!=null) {
                                    createAndInsertError(repoForLog!!.id, "$routeName by api err: $errMsg")
                                }

                                MyLog.e(TAG, "method:GET, route:$routeName, repoNameOrId=$repoNameOrId, err=${e.stackTraceToString()}")
                            }

                        }
                    }


                    /**
                     * query params:
                     *  repoNameOrId: repo name or id，优先查name，若无匹配，查id
                     *  masterPass: your master password, if have
                     *  force: force push, 1 enable , 0 disable, if null, will disable (as 0)
                     *  forceUseIdMatchRepo: 1 or 0, if true, will force match repo by repo id, else will match by name first, if no match, then match by id
                     *  token: if caller ip not in white list, token is required

                     * e.g.
                     * request: http://127.0.0.1/push?repoNameOrId=abc&masterPass=your_master_pass_if_have
                     */
                    get("/push") {
                        val routeName = "'/push'"

                        val callerIp = call.request.host()
                        val token = call.request.queryParameters.get("token")
                        val settings = SettingsUtil.getSettingsSnapshot()
                        val tokenCheckRet = tokenCheck(token, callerIp, settings)
                        if(tokenCheckRet.hasError()) {
                            // log the query params maybe better?
                            MyLog.e(TAG, "request rejected: route=$routeName, ip=$callerIp, token=$token, reason=${tokenCheckRet.msg}")
                            call.respond(createErrResult(tokenCheckRet.msg))
                            return@get
                        }

                        val repoNameOrId = call.request.queryParameters.get("repoNameOrId")
                        if(repoNameOrId == null || repoNameOrId.isBlank()) {
                            call.respond(createErrResult("invalid repo name or id"))
                            return@get
                        }

                        val forceUseIdMatchRepo = call.request.queryParameters.get("forceUseIdMatchRepo") == "1"

                        val masterPasswordFromUrl = call.request.queryParameters.get("masterPass") ?: ""

                        val masterPassword = masterPasswordFromUrl.ifEmpty { AppModel.masterPassword.value }

                        // force push or no
                        val force = call.request.queryParameters.get("force") == "1"

                        var repoForLog:RepoEntity? = null
                        // 查询仓库是否存在
                        // 尝试获取仓库锁，若获取失败，返回仓库正在执行其他操作
                        doJobThenOffLoading {
                            try {
                                val db = AppModel.dbContainer
                                val repoRet = db.repoRepository.getByNameOrId(repoNameOrId, forceUseIdMatchRepo)
                                if(repoRet.hasError()) {
                                    throw RuntimeException(repoRet.msg)
                                }

                                val repoFromDb = repoRet.data!!
                                repoForLog = repoFromDb

                                Libgit2Helper.doActWithRepoLock(repoFromDb) {
                                    if(dbIntToBool(repoFromDb.isDetached)) {
                                        throw RuntimeException("repo is detached")
                                    }

                                    if(!Libgit2Helper.isValidGitRepo(repoFromDb.fullSavePath)) {
                                        throw RuntimeException("invalid git repo")
                                    }

                                    Repository.open(repoFromDb.fullSavePath).use { gitRepo ->
                                        val upstream = Libgit2Helper.getUpstreamOfBranch(gitRepo, repoFromDb.branch)
                                        if(upstream.remote.isBlank() || upstream.branchRefsHeadsFullRefSpec.isBlank()) {
                                            throw RuntimeException("invalid upstream")
                                        }

                                        // get fetch credential
                                        val credential = Libgit2Helper.getRemoteCredential(
                                            db.remoteRepository,
                                            db.credentialRepository,
                                            repoFromDb.id,
                                            upstream.remote,
                                            trueFetchFalsePush = false,
                                            masterPassword = masterPassword
                                        )

                                        val ret = Libgit2Helper.push(gitRepo, upstream.remote, upstream.pushRefSpec, credential, force)
                                        if(ret.hasError()) {
                                            throw RuntimeException(ret.msg)
                                        }

                                        // 更新修改workstatus的时间，只更新时间就行，状态会在查询repo时更新
                                        val repoDb = AppModel.dbContainer.repoRepository
                                        repoDb.updateLastUpdateTime(repoFromDb.id, getSecFromTime())
                                    }

                                    call.respond(createSuccessResult())
                                }
                            }catch (e:Exception) {
                                val errMsg = e.localizedMessage ?: "unknown err"
                                call.respond(createErrResult(errMsg))

                                if(repoForLog!=null) {
                                    createAndInsertError(repoForLog!!.id, "$routeName by api err: $errMsg")
                                }

                                MyLog.e(TAG, "method:GET, route:$routeName, repoNameOrId=$repoNameOrId, err=${e.stackTraceToString()}")
                            }

                        }
                    }

                    /**
                     * query params:
                     *  repoNameOrId: repo name or id，优先查name，若无匹配，查id
                     *  username: using for create commit, if null, will use PuppyGit settings
                     *  email: using for create commit, if nul, will use PuppyGit settings
                     *  masterPass: your master password, if have
                     *  forceUseIdMatchRepo: 1 or 0, if true, will force match repo by repo id, else will match by name first, if no match, then match by id
                     *  token: if caller ip not in white list, token is required

                     * e.g.
                     * request: http://127.0.0.1/pullAll?repoNameOrId=abc&username=username&email=email&masterPass=your_master_pass_if_have
                     */
                    get("pullAll") {
                        call.respond(createErrResult("not yet implemented"))
                    }

                    /**
                     * query params:
                     *  masterPass: your master password, if have
                     *  force: 1 enable , 0 disable, if null, will disable (as 0)
                     *  token: if caller ip not in white list, token is required
                     *
                     * e.g.
                     * request: http://127.0.0.1/pushAll?masterPass=your_master_pass_if_have
                     */
                    get("pushAll") {
                        call.respond(createErrResult("not yet implemented"))
                    }
                }
            }.start(wait = false) // 不能传true，会block整个程序

            MyLog.w(TAG, "Http Server started on '${settings.httpService.listenHost}:${settings.httpService.listenPort}'")
            return null
        }catch (e:Exception) {
            MyLog.e(TAG, "Http Server start failed, err=${e.stackTraceToString()}")
            return e
        }
    }

    suspend fun stopServer():Exception? {
        try {
            server?.stop(1000, 1500)
            delay(1500)
            MyLog.w(TAG, "Http Server stopped")
            return null
        }catch (e:Exception) {
            MyLog.e(TAG, "Http Server stop failed: ${e.stackTraceToString()}")
            return e
        }
    }

    suspend fun restartServer(settings: AppSettings):Exception? {
        stopServer()
        server = null
        return startServer(settings)
    }

    fun isServerRunning():Boolean {
        return server != null
    }

}

/**
 * 如果ip同时在白名单和黑名单，将允许连接
 * 如果设置项中的token为空字符串，将允许所有连接
 */
private fun tokenCheck(token:String?,ip:String, settings: AppSettings): Ret<Unit?> {
    val whiteList = settings.httpService.ipWhiteList
    if(whiteList.contains(ip)) {
        return Ret.createSuccess(null)
    }

    val blackList = settings.httpService.ipBlackList
    if(blackList.contains(ip)) {
        return Ret.createError(null, "ip blocked")
    }

    // check token
    val expectedToken = settings.httpService.token
    if(expectedToken.isEmpty()) {
        // token empty will allow all requests
        return Ret.createSuccess(null)
    }

    if(token!=null && token == expectedToken) {
        return Ret.createSuccess(null)
    }

    return Ret.createError(null, "invalid token")
}
