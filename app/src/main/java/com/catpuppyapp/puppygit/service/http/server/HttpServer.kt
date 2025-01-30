package com.catpuppyapp.puppygit.service.http.server

import com.catpuppyapp.puppygit.constants.Cons
import com.catpuppyapp.puppygit.data.entity.RepoEntity
import com.catpuppyapp.puppygit.etc.Ret
import com.catpuppyapp.puppygit.settings.AppSettings
import com.catpuppyapp.puppygit.settings.SettingsUtil
import com.catpuppyapp.puppygit.utils.AppModel
import com.catpuppyapp.puppygit.utils.MyLog
import com.catpuppyapp.puppygit.utils.RepoActUtil
import com.catpuppyapp.puppygit.utils.createAndInsertError
import com.catpuppyapp.puppygit.utils.doJobThenOffLoading
import com.catpuppyapp.puppygit.utils.genHttpHostPortStr
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.host
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

private const val TAG = "HttpServer"

private val sendSuccessNotification:(title:String?, msg:String?, startPage:Int?, startRepoId:String?)->Unit= {title, msg, startPage, startRepoId ->
    HttpService.sendSuccessNotification(title, msg, startPage, startRepoId)
}

private val sendNotification:(title:String, msg:String, startPage:Int, startRepoId:String)->Unit={title, msg, startPage, startRepoId ->
    HttpService.sendNotification(title, msg, startPage, startRepoId)
}

private val sendProgressNotification:(repoNameOrId:String, progress:String)->Unit={repoNameOrId, progress ->
    HttpService.sendProgressNotification(repoNameOrId, progress)
}


/**
 * 如果ip同时在白名单和黑名单，将允许连接
 * 如果设置项中的token为空字符串，将允许所有连接
 */
private fun tokenPassedOrThrowException(token:String?, ip:String, settings: AppSettings): Ret<Unit?> {
    //这个错误信息不要写太具体哪个出错，不然别人可以探测你的ip和token名单
    val errMsg = "invalid token or ip blocked"
    val errRet:Ret<Unit?> = Ret.createError(null, errMsg)

    // check token
    val tokenList = settings.httpService.tokenList
    //全空格token不被允许，不过"a b c"这种可以允许，首尾空格将被去除，但中间的会保留
    if(token == null || tokenList.isEmpty() || token.isBlank() || tokenList.contains(token).not()) {
        // token empty will reject all requests
        return errRet
    }

    val whiteList = settings.httpService.ipWhiteList
    //匹配到通配符 或者 匹配到请求者的ip 则 放行
    if(whiteList.isEmpty() || ip.isBlank() || whiteList.find { it == "*" || it == ip } == null) {
        return errRet
    }

    return Ret.createSuccess(null)
}

private suspend fun pullRepoList(
    repoList:List<RepoEntity>,
    routeName: String,
    gitUsernameFromUrl:String,
    gitEmailFromUrl:String,
) {
    RepoActUtil.pullRepoList(
        repoList,
        routeName,
        gitUsernameFromUrl,
        gitEmailFromUrl,
        sendSuccessNotification,
        sendNotification,
        sendProgressNotification,
    )
}


suspend fun pushRepoList(
    repoList:List<RepoEntity>,
    routeName: String,
    gitUsernameFromUrl:String,
    gitEmailFromUrl:String,
    autoCommit:Boolean,
    force:Boolean,
) {
    RepoActUtil.pushRepoList(
        repoList,
        routeName,
        gitUsernameFromUrl,
        gitEmailFromUrl,
        autoCommit,
        force,
        sendSuccessNotification,
        sendNotification,
        sendProgressNotification
    )
}


internal class HttpServer {
    private val lock = Mutex()
    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine. Configuration>? = null

    /**
     * expect do start/stop/restart server in `act`
     */
    suspend fun <T> doActWithLock(act:suspend HttpServer.() -> T):T {
        lock.withLock {
            return this.act()
        }
    }

    suspend fun startServer(host:String, port:Int):Exception? {
        if(isServerRunning()) return null

        try {
            server = embeddedServer(Netty, host = host, port = port) {
                install(ContentNegotiation) {
                    //忽略对象里没有的key；编码默认值；紧凑格式
                    json(Json{ ignoreUnknownKeys = true; encodeDefaults=true; prettyPrint = false})
                }
                routing {
                    get("/status") {
                        call.respond(createSuccessResult("online"))
                    }

                    // for test
                    get("/echo/{msg}") {
                        call.respond(createSuccessResult(call.parameters.get("msg") ?: ""))
                    }

                    // not work, service launch app may require float window permission
//                    get("/launchApp") {
//                        HttpService.launchApp()
//                    }

                    /**
                     * query params:
                     *  repoNameOrId: repo name or id, match by name first, if none, will match by id
                     *  gitUsername: using for create commit, if null, will use PuppyGit settings
                     *  gitEmail: using for create commit, if null, will use PuppyGit settings
                     *  forceUseIdMatchRepo: 1 enable or 0 disable, default 0, if enable, will force match repo by repo id, else will match by name first, if no match, then match by id
                     *  token: token is required

                     * e.g.
                     * request: http://127.0.0.1/pull?repoNameOrId=abc
                     */
                    get("/pull") {
                        var repoNameOrIdForLog:String? = null
                        var repoForLog:RepoEntity? = null
                        val routeName = "'/pull'"
                        val settings = SettingsUtil.getSettingsSnapshot()

                        try {
                            tokenPassedOrThrowException(call, routeName, settings)

                            val repoNameOrId = call.request.queryParameters.get("repoNameOrId")
                            if(repoNameOrId == null || repoNameOrId.isBlank()) {
                                throw RuntimeException("invalid repo name or id")
                            }

                            repoNameOrIdForLog = repoNameOrId



                            val forceUseIdMatchRepo = call.request.queryParameters.get("forceUseIdMatchRepo") == "1"
                            // get git username and email for merge, if request doesn't contains them, will use PuppyGit app settings
                            val gitUsernameFromUrl = call.request.queryParameters.get("gitUsername") ?:""
                            val gitEmailFromUrl = call.request.queryParameters.get("gitEmail") ?:""


                            val db = AppModel.dbContainer
                            val repoRet = db.repoRepository.getByNameOrId(repoNameOrId, forceUseIdMatchRepo)
                            if(repoRet.hasError()) {
                                throw RuntimeException(repoRet.msg)
                            }

                            val repoFromDb = repoRet.data!!
                            repoForLog = repoFromDb

                            //执行请求，可能时间很长，所以开个协程，直接返回响应即可
                            doJobThenOffLoading {
                                pullRepoList(listOf(repoFromDb), routeName, gitUsernameFromUrl, gitEmailFromUrl)
                            }

                            call.respond(createSuccessResult())
                        }catch (e:Exception) {
                            val errMsg = e.localizedMessage ?: "unknown err"
                            call.respond(createErrResult(errMsg))

                            if(repoForLog!=null) {
                                createAndInsertError(repoForLog.id, "pull by api $routeName err: $errMsg")
                            }

                            if(settings.httpService.showNotifyWhenErr) {
                                sendNotification("$routeName err", errMsg, Cons.selectedItem_ChangeList, repoForLog?.id ?: "")
                            }

                            MyLog.e(TAG, "method:GET, route:$routeName, repoNameOrId=$repoNameOrIdForLog, err=${e.stackTraceToString()}")
                        }

                    }


                    /**
                     * query params:
                     *  repoNameOrId: repo name or id，优先查name，若无匹配，查id
                     *  gitUsername: using for create commit, if null, will use PuppyGit settings
                     *  gitEmail: using for create commit, if null, will use PuppyGit settings
                     *  force: force push, 1 enable , 0 disable, if null, will disable (as 0)
                     *  forceUseIdMatchRepo: 1 enable or 0 disable, default 0, if enable, will force match repo by repo id, else will match by name first, if no match, then match by id
                     *  token: token is required
                     *  autoCommit: 1 enable or 0 disable, default 1: if enable and no conflict items exists, will auto commit all changes,
                     *    and will check index, if index empty, will not pushing;
                     *    if disable, will only do push, no commit changes,
                     *    no index empty check, no conflict items check.
                     *
                     * e.g.
                     * request: http://127.0.0.1/push?repoNameOrId=abc
                     */
                    get("/push") {
                        var repoNameOrIdForLog:String? = null
                        var repoForLog:RepoEntity? = null
                        val routeName = "'/push'"
                        val settings = SettingsUtil.getSettingsSnapshot()

                        try {
                            tokenPassedOrThrowException(call, routeName, settings)

                            val repoNameOrId = call.request.queryParameters.get("repoNameOrId")
                            if(repoNameOrId == null || repoNameOrId.isBlank()) {
                                throw RuntimeException("invalid repo name or id")
                            }

                            repoNameOrIdForLog = repoNameOrId


                            if(settings.httpService.showNotifyWhenProgress) {
                                sendProgressNotification(repoNameOrId, "pushing...")
                            }


                            val forceUseIdMatchRepo = call.request.queryParameters.get("forceUseIdMatchRepo") == "1"


                            //这个只要不明确传0，就是启用
                            val autoCommit = call.request.queryParameters.get("autoCommit") != "0"

                            // force push or no
                            val force = call.request.queryParameters.get("force") == "1"


                            // get git username and email for merge
                            val gitUsernameFromUrl = call.request.queryParameters.get("gitUsername") ?:""
                            val gitEmailFromUrl = call.request.queryParameters.get("gitEmail") ?:""

                            // 查询仓库是否存在
                            // 尝试获取仓库锁，若获取失败，返回仓库正在执行其他操作

                            val db = AppModel.dbContainer
                            val repoRet = db.repoRepository.getByNameOrId(repoNameOrId, forceUseIdMatchRepo)
                            if(repoRet.hasError()) {
                                throw RuntimeException(repoRet.msg)
                            }

                            val repoFromDb = repoRet.data!!
                            repoForLog = repoFromDb

                            doJobThenOffLoading {
                                pushRepoList(listOf(repoFromDb),routeName, gitUsernameFromUrl, gitEmailFromUrl, autoCommit, force)
                            }

                            call.respond(createSuccessResult())

                        }catch (e:Exception) {
                            val errMsg = e.localizedMessage ?: "unknown err"
                            call.respond(createErrResult(errMsg))

                            if(repoForLog!=null) {
                                createAndInsertError(repoForLog!!.id, "push by api $routeName err: $errMsg")
                            }

                            if(settings.httpService.showNotifyWhenErr) {
                                sendNotification("$routeName err", errMsg, Cons.selectedItem_ChangeList, repoForLog?.id ?: "")
                            }

                            MyLog.e(TAG, "method:GET, route:$routeName, repoNameOrId=$repoNameOrIdForLog, err=${e.stackTraceToString()}")
                        }

                    }

                    /**
                     * query params:
                     *  gitUsername: using for create commit, if null, will use PuppyGit settings
                     *  gitEmail: using for create commit, if null, will use PuppyGit settings
                     *  token: token is required

                     * e.g.
                     * request: http://127.0.0.1/pullAll?gitUsername=username&gitEmail=email&token=your_token
                     */
                    get("/pullAll") {
                        val routeName = "'/pullAll'"
                        val settings = SettingsUtil.getSettingsSnapshot()

                        try {
                            tokenPassedOrThrowException(call, routeName, settings)


                            if(settings.httpService.showNotifyWhenProgress) {
                                sendProgressNotification("PuppyGit", "pulling all...")
                            }


                            // get git username and email for merge, if request doesn't contains them, will use PuppyGit app settings
                            val gitUsernameFromUrl = call.request.queryParameters.get("gitUsername") ?:""
                            val gitEmailFromUrl = call.request.queryParameters.get("gitEmail") ?:""


                            //执行请求，可能时间很长，所以开个协程，直接返回响应即可
                            doJobThenOffLoading {
                                pullRepoList(AppModel.dbContainer.repoRepository.getAll(), routeName, gitUsernameFromUrl, gitEmailFromUrl)
                            }

                            call.respond(createSuccessResult())
                        }catch (e:Exception) {
                            val errMsg = e.localizedMessage ?: "unknown err"
                            call.respond(createErrResult(errMsg))

                            if(settings.httpService.showNotifyWhenErr) {
                                sendNotification("$routeName err", errMsg, Cons.selectedItem_Repos ,"")
                            }

                            MyLog.e(TAG, "method:GET, route:$routeName, err=${e.stackTraceToString()}")
                        }
                    }

                    /**
                     * query params:
                     *  gitUsername: using for create commit, if null, will use PuppyGit settings
                     *  gitEmail: using for create commit, if null, will use PuppyGit settings
                     *  autoCommit: same as '/push'
                     *  force: 1 enable , 0 disable, default 0
                     *  token: token is required
                     *
                     * e.g.
                     * request: http://127.0.0.1/pushAll?token=your_token
                     */
                    get("/pushAll") {
                        val routeName = "'/pushAll'"
                        val settings = SettingsUtil.getSettingsSnapshot()

                        try {
                            tokenPassedOrThrowException(call, routeName, settings)

                            if(settings.httpService.showNotifyWhenProgress) {
                                sendProgressNotification("PuppyGit", "pushing all...")
                            }

                            //这个只要不明确传0，就是启用
                            val autoCommit = call.request.queryParameters.get("autoCommit") != "0"

                            // force push or no
                            val force = call.request.queryParameters.get("force") == "1"


                            // get git username and email for merge
                            val gitUsernameFromUrl = call.request.queryParameters.get("gitUsername") ?:""
                            val gitEmailFromUrl = call.request.queryParameters.get("gitEmail") ?:""

                            // 查询仓库是否存在
                            // 尝试获取仓库锁，若获取失败，返回仓库正在执行其他操作
                            doJobThenOffLoading {
                                pushRepoList(AppModel.dbContainer.repoRepository.getAll(),routeName, gitUsernameFromUrl, gitEmailFromUrl, autoCommit, force)
                            }

                            call.respond(createSuccessResult())

                        }catch (e:Exception) {
                            val errMsg = e.localizedMessage ?: "unknown err"
                            call.respond(createErrResult(errMsg))

                            if(settings.httpService.showNotifyWhenErr) {
                                sendNotification("$routeName err", errMsg, Cons.selectedItem_Repos, "")
                            }

                            MyLog.e(TAG, "method:GET, route:$routeName, err=${e.stackTraceToString()}")
                        }


                    }
                }
            }.start(wait = false) // 不能传true，会block整个程序

            MyLog.w(TAG, "Http Server started on '${genHttpHostPortStr(host, port.toString())}'")
            return null
        }catch (e:Exception) {
            //端口占用之类的能捕获到错误，看来即使非阻塞也并非一启动就立即返回，应该是成功绑定端口ip后才返回
            MyLog.e(TAG, "Http Server start failed, err=${e.stackTraceToString()}")
            return e
        }
    }

    /**
     * will throw exception if token bad
     */
    private fun tokenPassedOrThrowException(call: RoutingCall, routeName: String, settings: AppSettings) {
        val callerIp = call.request.host()
        val token = call.request.queryParameters.get("token")
        val tokenCheckRet = tokenPassedOrThrowException(token, callerIp, settings)
        if (tokenCheckRet.hasError()) {
            // log the query params maybe better?
            MyLog.e(TAG, "request rejected: route=$routeName, ip=$callerIp, token=$token, reason=${tokenCheckRet.msg}")
            throw RuntimeException(tokenCheckRet.msg)
        }
    }

    suspend fun stopServer():Exception? {
        if(server == null) {
            MyLog.w(TAG, "server is null, stop canceled")
            return null
        }

        try {
            server?.stop(0, 0)  //立即停止服务器
            server = null

            MyLog.w(TAG, "Http Server stopped")
            return null
        }catch (e:Exception) {
            MyLog.e(TAG, "Http Server stop failed: ${e.stackTraceToString()}")
            return e
        }
    }

    suspend fun restartServer(host: String, port: Int):Exception? {
        stopServer()
        return startServer(host, port)
    }

    fun isServerRunning():Boolean {
        //这检查的是协程是否Active，协程还在运行，服务器就在运行，大概是这个逻辑吧？
        return server?.application?.isActive == true

        //这个不行，服务器正在启动，连接不通，但不久就上线了，用这个会误认为服务器不在线，误启动
//        val settings = SettingsUtil.getSettingsSnapshot()
//        return checkApiRunning("${genHttpHostPortStr(settings.httpService.listenHost, settings.httpService.listenPort)}/ping", 2)
    }


}
