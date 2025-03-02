package com.catpuppyapp.puppygit.screen.content.homescreen.innerpage

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.catpuppyapp.puppygit.compose.AskGitUsernameAndEmailDialogWithSelection
import com.catpuppyapp.puppygit.compose.BottomBar
import com.catpuppyapp.puppygit.compose.ChangeListItem
import com.catpuppyapp.puppygit.compose.CheckBoxNoteText
import com.catpuppyapp.puppygit.compose.ClickableText
import com.catpuppyapp.puppygit.compose.ConfirmDialog
import com.catpuppyapp.puppygit.compose.ConfirmDialog2
import com.catpuppyapp.puppygit.compose.CopyableDialog
import com.catpuppyapp.puppygit.compose.CredentialSelector
import com.catpuppyapp.puppygit.compose.LoadingText
import com.catpuppyapp.puppygit.compose.LongPressAbleIconBtn
import com.catpuppyapp.puppygit.compose.MyCheckBox
import com.catpuppyapp.puppygit.compose.MyLazyColumn
import com.catpuppyapp.puppygit.compose.MySelectionContainer
import com.catpuppyapp.puppygit.compose.OpenAsDialog
import com.catpuppyapp.puppygit.compose.RequireCommitMsgDialog
import com.catpuppyapp.puppygit.compose.ScrollableColumn
import com.catpuppyapp.puppygit.compose.SelectedItemDialog
import com.catpuppyapp.puppygit.compose.SetUpstreamDialog
import com.catpuppyapp.puppygit.constants.Cons
import com.catpuppyapp.puppygit.constants.PageRequest
import com.catpuppyapp.puppygit.data.AppContainer
import com.catpuppyapp.puppygit.data.entity.CredentialEntity
import com.catpuppyapp.puppygit.data.entity.RepoEntity
import com.catpuppyapp.puppygit.dev.checkoutFilesTestPassed
import com.catpuppyapp.puppygit.dev.cherrypickTestPassed
import com.catpuppyapp.puppygit.dev.createPatchTestPassed
import com.catpuppyapp.puppygit.dev.dev_EnableUnTestedFeature
import com.catpuppyapp.puppygit.dev.ignoreWorktreeFilesTestPassed
import com.catpuppyapp.puppygit.dev.proFeatureEnabled
import com.catpuppyapp.puppygit.dev.tagsTestPassed
import com.catpuppyapp.puppygit.dev.treeToTreeBottomBarActAtLeastOneTestPassed
import com.catpuppyapp.puppygit.etc.Ret
import com.catpuppyapp.puppygit.git.ImportRepoResult
import com.catpuppyapp.puppygit.git.StatusTypeEntrySaver
import com.catpuppyapp.puppygit.git.Upstream
import com.catpuppyapp.puppygit.play.pro.R
import com.catpuppyapp.puppygit.screen.functions.ChangeListFunctions
import com.catpuppyapp.puppygit.screen.functions.maybeIsGoodKeyword
import com.catpuppyapp.puppygit.screen.functions.naviToFileHistoryByRelativePath
import com.catpuppyapp.puppygit.screen.functions.openFileWithInnerSubPageEditor
import com.catpuppyapp.puppygit.screen.shared.DiffFromScreen
import com.catpuppyapp.puppygit.screen.shared.SharedState
import com.catpuppyapp.puppygit.settings.SettingsUtil
import com.catpuppyapp.puppygit.style.MyStyleKt
import com.catpuppyapp.puppygit.user.UserUtil
import com.catpuppyapp.puppygit.utils.AppModel
import com.catpuppyapp.puppygit.utils.IgnoreMan
import com.catpuppyapp.puppygit.utils.Libgit2Helper
import com.catpuppyapp.puppygit.utils.Msg
import com.catpuppyapp.puppygit.utils.MyLog
import com.catpuppyapp.puppygit.utils.StateRequestType
import com.catpuppyapp.puppygit.utils.UIHelper
import com.catpuppyapp.puppygit.utils.cache.Cache
import com.catpuppyapp.puppygit.utils.createAndInsertError
import com.catpuppyapp.puppygit.utils.dbIntToBool
import com.catpuppyapp.puppygit.utils.doJobThenOffLoading
import com.catpuppyapp.puppygit.utils.doJobWithMainContext
import com.catpuppyapp.puppygit.utils.getRequestDataByState
import com.catpuppyapp.puppygit.utils.getSecFromTime
import com.catpuppyapp.puppygit.utils.isRepoReadyAndPathExist
import com.catpuppyapp.puppygit.utils.replaceStringResList
import com.catpuppyapp.puppygit.utils.showErrAndSaveLog
import com.catpuppyapp.puppygit.utils.showToast
import com.catpuppyapp.puppygit.utils.state.CustomStateListSaveable
import com.catpuppyapp.puppygit.utils.state.CustomStateSaveable
import com.catpuppyapp.puppygit.utils.state.mutableCustomStateListOf
import com.catpuppyapp.puppygit.utils.state.mutableCustomStateOf
import com.catpuppyapp.puppygit.utils.updateSelectedList
import com.catpuppyapp.puppygit.utils.withMainContext
import com.github.git24j.core.Repository
import com.github.git24j.core.Repository.StateT

private const val TAG = "ChangeListInnerPage"
private const val stateKeyTag = "ChangeListInnerPage"

@Composable
fun ChangeListInnerPage(
    contentPadding: PaddingValues,
    fromTo: String,
    curRepoFromParentPage: CustomStateSaveable<RepoEntity>,
    isFileSelectionMode:MutableState<Boolean>,
    refreshRequiredByParentPage: String,
    changeListRequireRefreshFromParentPage:(whichRepoRequestRefresh:RepoEntity) -> Unit,
    changeListPageHasIndexItem: MutableState<Boolean>,
//    requirePullFromParentPage:MutableState<Boolean>,
//    requirePushFromParentPage:MutableState<Boolean>,
    requireDoActFromParent:MutableState<Boolean>,
    requireDoActFromParentShowTextWhenDoingAct:MutableState<String>,
    enableActionFromParent:MutableState<Boolean>,
    repoState: MutableIntState,
    naviUp: () -> Unit,
    itemList: CustomStateListSaveable<StatusTypeEntrySaver>,
    itemListState: LazyListState,
    selectedItemList:CustomStateListSaveable<StatusTypeEntrySaver>,
    //只有fromTo是tree to tree时才有这些条目，开始
    commit1OidStr:String="",
    commit2OidStr:String="",
    commitParentList:CustomStateListSaveable<String> = mutableCustomStateListOf(
        keyTag = stateKeyTag,
        keyName = "commitParentList",
        initValue = listOf<String>()
    ),

    repoId:String="",
    //只有fromTo是tree to tree时才有这些条目，结束

    changeListPageNoRepo:MutableState<Boolean>,
    hasNoConflictItems:MutableState<Boolean>,
    goToFilesPage:(path:String) -> Unit = {},  //跳转到Files页面以浏览仓库文件，只有主页需要传这个参数，Index和TreeToTree页面不需要
    changelistPageScrolled:MutableState<Boolean>,
    changeListPageFilterModeOn:MutableState<Boolean>,
    changeListPageFilterKeyWord:CustomStateSaveable<TextFieldValue>,
    filterListState:LazyListState,
    swap:Boolean,
    commitForQueryParents:String,
    rebaseCurOfAll:MutableState<String>? = null,  //ChangeList页面和Index页面需要此参数，treeToTree不需要

    openDrawer:()->Unit,
    goToRepoPage:(targetRepoId:String)->Unit = {},  // only show workdir changes at ChangeList need this
    changeListRepoList:CustomStateListSaveable<RepoEntity>? =null,
    goToChangeListPage:(goToThisRepo:RepoEntity)->Unit ={},
    needReQueryRepoList:MutableState<String>? =null,
    newestPageId:MutableState<String>,  // for check if switched page （用来检测页面是否切换过，有时候有的仓库查询慢，切换仓库，切换后查出来了，会覆盖条目列表，出现仓库b显示了仓库a的条目的问题，更新列表前检测下此值是否有变化能避免此bug）
    //这组件再多一个参数就崩溃了，不要再加了，会报verifyError错误，升级gradle或许可以解决，具体原因不明（缓存问题，删除项目根目录下的.gradle目录重新构建即可），后来发现是compose变异器本身的bug，编译太复杂的组件就可能报错
//    isDiffToHead:MutableState<Boolean> = mutableStateOf(false),  //仅 treeTotree页面需要此参数，用来判断是否在和headdiff
    naviTarget:MutableState<String>,
    enableFilterState:MutableState<Boolean>,
    filterList:CustomStateListSaveable<StatusTypeEntrySaver>,
    lastClickedItemKey:MutableState<String>

) {

    // for detect page changed, avoid loading repo1, switched repo2, repo2 loaded, then repo1 loaded, page show wrong items of repo1
//    val pageId = remember {
//        val newId = getShortUUID()
//        newestPageId.value = newId
//        newId
//    }

    //避免导航的时候出现 “//” 导致导航失败
    val commit1OidStr = commit1OidStr.ifBlank { Cons.allZeroOid.toString() }
    val commit2OidStr = commit2OidStr.ifBlank { Cons.allZeroOid.toString() }
    val repoId = remember(repoId, curRepoFromParentPage.value.id) { derivedStateOf { if(repoId.isBlank()) curRepoFromParentPage.value.id else repoId } }.value  // must call .value, else derived block may not executing


    val isDiffToLocal = fromTo == Cons.gitDiffFromIndexToWorktree || commit1OidStr==Cons.gitLocalWorktreeCommitHash || commit2OidStr==Cons.gitLocalWorktreeCommitHash
    val isWorktreePage = fromTo == Cons.gitDiffFromIndexToWorktree

    // xxx diff to local, not local diff to xxx
    val localAtDiffRight = remember(fromTo, commit1OidStr, commit2OidStr, swap) { derivedStateOf {
        (fromTo == Cons.gitDiffFromIndexToWorktree
                || (if(swap) commit1OidStr==Cons.gitLocalWorktreeCommitHash else commit2OidStr==Cons.gitLocalWorktreeCommitHash)
        )
    } }

    val haptic = LocalHapticFeedback.current
    val clipboardManager = LocalClipboardManager.current

//    val allRepoParentDir = AppModel.allRepoParentDir;
    val activityContext = LocalContext.current
    val exitApp = AppModel.exitApp
    val dbContainer = AppModel.dbContainer
    val navController = AppModel.navController

    val settings = remember {
        val s = SettingsUtil.getSettingsSnapshot()
        changelistPageScrolled.value = s.showNaviButtons
        s
    }


    // username and email start
    val setUserAndEmailForGlobal = stringResource(R.string.set_for_global)
    val setUserAndEmailForCurRepo = stringResource(R.string.set_for_current_repo)
    val pleaseSetUsernameAndEmailBeforeCommit = stringResource(R.string.please_set_username_email_before_commit)

    val optNumSetUserAndEmailForGlobal = 0  //为全局设置用户名和密码，值是对应选项在选项列表中的索引，这个变量其实相当于是索引的别名。关联列表：usernameAndEmailDialogOptionList
    val optNumSetUserAndEmailForCurRepo = 1  //为当前仓库设置用户名和密码
    //num: optText，例如 "1: 选项1文本"
//    val usernameAndEmailDialogOptionList = listOf(RadioOptionsUtil.formatOptionKeyAndText(optNumSetUserAndEmailForGlobal,setUserAndEmailForGlobal), RadioOptionsUtil.formatOptionKeyAndText(optNumSetUserAndEmailForCurRepo,setUserAndEmailForCurRepo))
    val usernameAndEmailDialogOptionList = listOf(  // idx关联选项：optNumSetUserAndEmailForGlobal, optNumSetUserAndEmailForCurRepo
        setUserAndEmailForGlobal,  //值的存放顺序要和选项值匹配 (这个值对应的是 optNumSetUserAndEmailForGlobal )
        setUserAndEmailForCurRepo
    )

    val usernameAndEmailDialogSelectedOption = rememberSaveable{mutableIntStateOf(optNumSetUserAndEmailForGlobal)}

    val username = rememberSaveable { mutableStateOf("") }
    val email = rememberSaveable { mutableStateOf("") }
    val showUsernameAndEmailDialog = rememberSaveable { mutableStateOf(false) }
    // username and email end



    val changeListPageHasWorktreeItem = rememberSaveable { mutableStateOf(false) }

    val showRebaseSkipDialog = rememberSaveable { mutableStateOf(false) }
    val showRebaseAbortDialog = rememberSaveable { mutableStateOf(false) }
    val showCherrypickAbortDialog = rememberSaveable { mutableStateOf(false) }
    val showMergeAbortDialog = rememberSaveable { mutableStateOf(false) }

    val initRebaseSkipDialog = { curRepo:RepoEntity ->
        try {
            val repoFullPath = curRepo.fullSavePath
            Repository.open(repoFullPath).use { repo ->
                val (usernameFromConfig, emailFromConfig) = Libgit2Helper.getGitUsernameAndEmail(repo)

                if (usernameFromConfig.isBlank() || emailFromConfig.isBlank()) {
                    Msg.requireShowLongDuration(activityContext.getString(R.string.plz_set_email_and_username_then_try_again))
                    showUsernameAndEmailDialog.value = true
                } else {
                    showRebaseSkipDialog.value = true
                }
            }
        }catch (e:Exception) {
            Msg.requireShow("init skip err")
            MyLog.e(TAG, "#initRebaseSkipDialog err: ${e.stackTraceToString()}")
        }
    }

    val initMergeAbortDialog = {
        showCherrypickAbortDialog.value = false
        showRebaseAbortDialog.value = false
        showMergeAbortDialog.value = true
    }

    val initRebaseAbortDialog = {
        showMergeAbortDialog.value = false
        showCherrypickAbortDialog.value = false
        showRebaseAbortDialog.value = true
    }

    val initCherrypickAbortDialog = {
        showMergeAbortDialog.value = false
        showRebaseAbortDialog.value = false
        showCherrypickAbortDialog.value = true
    }


    val showMergeAcceptTheirsOrOursDialog = rememberSaveable { mutableStateOf(false)}
    val mergeAcceptTheirs = rememberSaveable { mutableStateOf(false)}
    //this state used for make navi back to this page do LaunchedEffect, it must not rememberSaveable
    //这个变量用来在导航返回到这个页面时确保执行LaunchedEffect，不能用rememberSaveable
//    val needRefreshChangeListPage = remember { mutableStateOf("justForTriggerLaunchedEffectAfterNaviBack") }
    // must make sure the init value 100% difference with `refreshRequiredByParentPage`, so dont use empty string as init value
//    val lastRequireRefreshValue = rememberSaveable { mutableStateOf("TheValueNever=RequireRefresh")}
//    val changeListPageWorktreeItemList = remember { mutableStateListOf<StatusTypeEntrySaver>() }
//    val itemList = mutableCustomStateOf(value = mutableListOf<StatusTypeEntrySaver>())  //这个反正旋转屏幕都会清空，没必要用我写的自定义状态存储器
//    val changeListPageConflictItemList = mutableCustomStateOf(value = mutableListOf<StatusTypeEntrySaver>())
//    val openRepoFailedErrStrRes = stringResource(R.string.open_repo_failed)
    val curRepoUpstream = mutableCustomStateOf(keyTag = stateKeyTag, keyName = "curRepoUpstream", initValue = Upstream())
    //for error shown when coroutine has error
//    val hasErr = rememberSaveable { mutableStateOf(false) }
//    val errMsg = rememberSaveable { mutableStateOf("") }
//    show error compose
//    ShowErrorIfNeed(hasErr = hasErr, errMsg = errMsg)
//    if(hasErr.value) {
//        showToast(appContext,openRepoFailedErrStrRes+":"+errMsg.value)
////        return
//    }

//    val showToast = rememberSaveable { mutableStateOf(false) }
//    val toastMsg = rememberSaveable { mutableStateOf("") }
//    val requireShowToast = { msg:String->
//        showToast.value = true;
//        toastMsg.value = msg
//    }
//    ShowToast(showToast, toastMsg)

    val requireShowToast:(String)->Unit = {msg:String->
        Msg.requireShowLongDuration(msg)
    }


    val noItemSelectedStrRes = stringResource(R.string.no_item_selected)

    //TODO 列表条目三个点的菜单项，open(原地，不要跳转到HomeScreen的Editor)/open with
//    val menuKeyRename = "rename"
//    val menuValueRename = stringResource(R.string.rename)
//    val menuKeyInfo = "info"
//    val menuValueInfo = stringResource(R.string.info)
//    val fileMenuMap = remember{ mutableMapOf<String, String>(
//        menuKeyRename to menuValueRename,
//        menuKeyInfo to menuValueInfo,
//    ) }

//    val selectedItemList = mutableCustomStateOf(value = mutableListOf<StatusTypeEntrySaver>())
    //remember在屏幕改变后确实会丢东西
//    val selFilePathListJsonObjStr = remember{ mutableStateOf("{}") }  //key是文件名，所以这个列表只能存储相同目录下的文件，不同目录有可能名称冲突，但由于选择模式只能在当前目录选择，所以这个缺陷可以接受。json格式:{fileName:canonicalPath}

    val quitSelectionMode = {
        isFileSelectionMode.value=false  //关闭选择模式
        selectedItemList.value.clear()  //清空选中文件列表
    }

    val filesPageAddFileToSelectedListIfAbsentElseRemove:(StatusTypeEntrySaver)->Unit = { item:StatusTypeEntrySaver ->
//        val fpath = item.canonicalPath
//        val fname = item.fileName
        val fileList = selectedItemList.value

        if (fileList.contains(item)) {
            fileList.remove(item)
        } else {
            fileList.add(item)
        }

//        selectedItemList.requireRefreshView()
    }

//    val addItemToSelectedList = { item:StatusTypeEntrySaver ->
//        val fpath = item.canonicalPath
//        val fname = item.fileName
//        val fileMap = selectedItemList.value
//        fileMap.put(fpath, fname)
//
//        selectedItemList.value = fileMap
//    }
    val selectedListIsEmpty:()->Boolean = {
        // or getSelectedCount == 0
        selectedItemList.value.isEmpty()
    }
    val selectedListIsNotEmpty:()->Boolean = {
        selectedItemList.value.isNotEmpty()
    }

    //这种常量型的state用remember就行，反正也不会改，更提不上恢复
    val iconList:List<ImageVector> = if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(  //changelist page
//            Icons.Filled.CloudDone,  //提交选中文件并推送
            Icons.Filled.DoneAll,  //提交选中文件
            Icons.Filled.SelectAll,  //全选
        )else if(fromTo==Cons.gitDiffFromHeadToIndex) listOf(  //index page
//            Icons.Outlined.CloudDone,  //提交index所有条目并推送
            Icons.Outlined.Check,  //提交index所有条目
            Icons.Outlined.SelectAll
        )else if(fromTo == Cons.gitDiffFromTreeToTree) listOf(
            Icons.Filled.Download,  // checkout
            ImageVector.vectorResource(R.drawable.outline_nutrition_24),  //cherrypick
            Icons.Filled.LibraryAdd,  // patch
            Icons.Outlined.SelectAll

        ) else listOf()

    val iconTextList:List<String> = if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(  //changelist page
//        stringResource(id = R.string.commit_selected_and_index_items_then_sync),
        stringResource(id = R.string.commit_selected_and_index_items),
        stringResource(id = R.string.select_all),
        )else if(fromTo == Cons.gitDiffFromHeadToIndex) listOf(  //index page
//            stringResource(id = R.string.commit_all_index_items_then_sync),
            stringResource(id = R.string.commit_all_index_items),
            stringResource(id = R.string.select_all),
        ) else if(fromTo == Cons.gitDiffFromTreeToTree) listOf(
        stringResource(R.string.checkout),
        stringResource(R.string.cherrypick),
        stringResource(R.string.create_patch),
        stringResource(id = R.string.select_all),
        )else listOf(

        )

    //是否显示底栏按钮
    val iconVisibleList = if(fromTo == Cons.gitDiffFromTreeToTree) listOf(
        checkoutFileVisible@{ proFeatureEnabled(checkoutFilesTestPassed) },
        cherrypickFilesVisible@{ proFeatureEnabled(cherrypickTestPassed) },
        createPatchVisible@{ proFeatureEnabled(createPatchTestPassed) },
        selectedAll@{true}
    ) else listOf()

    //一般用用iconTextList就行，除非有不一样的描述
//    val iconDescTextList:SnapshotStateList<String> = remember {
//        mutableStateListOf(
//            "Commit Selected Then Sync With Remote",
//            "Commit Selected",
//            "Select All",
//        )
//    }

    val iconEnableList:List<()->Boolean> = if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf( //worktree页面
            //只有仓库非detached HEAD状态时，commitThenSync才可用
//            {selectedListIsNotEmpty() && !dbIntToBool(curRepoFromParentPage.value.isDetached) },  //提交并同步

            selectedListIsNotEmpty,  //提交
            {true},  //select all 总是启用
        ) else if(fromTo==Cons.gitDiffFromHeadToIndex) listOf( //index页面
            //只有仓库非detached HEAD状态时，commitThenSync才可用
//            {changeListPageHasIndexItem.value && !dbIntToBool(curRepoFromParentPage.value.isDetached) },  //提交并同步，只有当存在index条目时才启用，changeListPageHasIndexItem 在index和worktree页面都会更新，所以用这个值来判断是否存在index条目是可靠的，或者改用itemList也行，因为在index页面，itemList代表的就是index页面的所有条目，再或者直接填true也行，因为底栏只有选择模式才打开，而只有存在条目才能进入选择模式，所以，如果你能看到底栏，那就肯定能执行提交和推送，反之你看不到底栏，就算提交和推送按钮启用，你也执行不了

            {changeListPageHasIndexItem.value},  //提交，只有当存在index条目时才启用
            {true} // select all
        )else if(fromTo == Cons.gitDiffFromTreeToTree) listOf(
            selectedListIsNotEmpty,  // checkout
//            {isDiffToHead?.value==true && selectedListIsNotEmpty()},  // cherrypick，之前理解错了，cherrypick和HEAD无关，只要是和parents对比，就能pick
            {commitParentList.value.isNotEmpty() && selectedListIsNotEmpty()},  // cherrypick，只有存在parents的情况下才可cherrypick
            selectedListIsNotEmpty,  // create patch
            {true}
        )else listOf()

    val hasConflictItemsSelected:()->Boolean = {
        var ret= false
        for(i in selectedItemList.value.toList()) {
            if(i.changeType==Cons.gitStatusConflict) {
                ret=true
                break
            }
        }

        ret
    }
    val moreItemEnableList:List<()->Boolean> = (if(fromTo == Cons.gitDiffFromIndexToWorktree)  listOf(
            hasConflictItemsSelected,  //accept ours
            hasConflictItemsSelected,  //accept theirs
            selectedListIsNotEmpty,  //stage
            selectedListIsNotEmpty,  //revert
            selectedListIsNotEmpty, // create patch
            selectedListIsNotEmpty, // ignore
            selectedListIsNotEmpty, // import as repo
        ) else listOf( // index page actually
            selectedListIsNotEmpty,  // unstage
            selectedListIsNotEmpty, // create patch
            selectedListIsNotEmpty, // import as repo
        )
    ).asReversed()

//    val isAllConflictItemsSelected:()->Boolean = isAllConflictItemsSelectedLabel@{
//        //如果不存在冲突条目，返回true
//        var allConflictCount = 0;
//        itemList.forEach{
//            if(it.changeType == Cons.gitStatusConflict) {
//                allConflictCount++
//            }
//        }
//
//        //如果不存在冲突条目，返回true
//        if(allConflictCount==0){
//            return@isAllConflictItemsSelectedLabel true
//        }
//
//        //如果存在冲突条目，检查选中的冲突条目是否和所有冲突条目数相等
//        var selectedConflictCount = 0
//        selectedItemList.forEach{
//            if(it.changeType == Cons.gitStatusConflict) {
//                selectedConflictCount++
//            }
//        }
//
//        //返回结果
//        selectedConflictCount == allConflictCount  //present isAllConflictItemsSelected
//
//    }

    val errWhenQuerySettingsFromDbStrRes = stringResource(R.string.err_when_querying_settings_from_db)

    val isLoading = rememberSaveable { mutableStateOf(true)}
    val loadingText = rememberSaveable { mutableStateOf(activityContext.getString(R.string.loading))}
    val loadingOn = {text:String->
        //Loading的时候禁用顶栏按钮
        enableActionFromParent.value=false

        loadingText.value = text
        isLoading.value=true
//        changeStateTriggerRefreshPage(needRefreshChangeListPage)  这里不能刷新，页面会闪烁，会看不到中间状态，忘了之前怎么写的了，总之这里不用刷新
    }
    val loadingOff = {
        enableActionFromParent.value=true

        loadingText.value = ""
        isLoading.value=false
//        changeStateTriggerRefreshPage(needRefreshChangeListPage)
    }

    val mustSelectAllConflictBeforeCommitStrRes = stringResource(R.string.must_resolved_conflict_and_select_them_before_commit)

    val canceledStrRes = stringResource(R.string.canceled)
    val nFilesStagedStrRes = stringResource(R.string.n_files_staged)

//    val bottomBarCloseInitValueNobody = "0"  //谁也不关，初始值
//    val bottomBarCloseByStage = "1"
//    val bottomBarCloseByCommit = "2"
//    val bottomBarCloseBySync = "3"
//    val whoCloseBottomBar = rememberSaveable { mutableStateOf(bottomBarCloseInitValueNobody) }

    val bottomBarActDoneCallback= {msg:String, curRepo:RepoEntity ->
        //显示通知
        if(msg.isNotBlank()) {
            requireShowToast(msg)
        }

        //退出选择模式，执行关闭底栏等操作
        quitSelectionMode()

        //刷新页面
        changeListRequireRefreshFromParentPage(curRepo)
    }

    // commit msg
//    val commitMsgNeedAsk = "1"
//    val commitMsgShowAskDialog = "2"
//    val commitMsgAlreadyGet = "3"
    val showCommitMsgDialog = rememberSaveable { mutableStateOf(false)}
    val amendCommit = rememberSaveable { mutableStateOf(false)}
    val overwriteAuthor = rememberSaveable { mutableStateOf(false)}
    val commitMsg = rememberSaveable { mutableStateOf("")}
    val indexIsEmptyForCommitDialog = rememberSaveable { mutableStateOf(false)}
    val commitBtnTextForCommitDialog = rememberSaveable { mutableStateOf("") }
//    val showPushForCommitDialog = rememberSaveable { mutableStateOf(false)}

    //upstream
//    val upstreamRemoteOptionsList = mutableCustomStateOf(value = mutableListOf<String>())
    val upstreamRemoteOptionsList = mutableCustomStateListOf(keyTag = stateKeyTag, keyName = "upstreamRemoteOptionsList", initValue = listOf<String>() )
    val upstreamSelectedRemote = rememberSaveable{mutableIntStateOf(0)} //默认选中第一个remote，每个仓库至少有一个origin remote，应该不会出错
    //默认选中为上游设置和本地分支相同名
    val upstreamBranchSameWithLocal =rememberSaveable { mutableStateOf(true)}
    //把远程分支名设成当前分支的短名？
    val upstreamBranchShortRefSpec = rememberSaveable { mutableStateOf("")}
    //设置当前分支，用来让用户知道自己在为哪个分支设置上游，这是当前分支，不是上游的分支，他妈的这个变量名当初怎么他妈想的？
    val upstreamCurBranchShortName = rememberSaveable { mutableStateOf("")}
    val upstreamCurBranchFullName = rememberSaveable { mutableStateOf("")}

    //给commit msg弹窗的回调用的，如果是true，代表需要执行sync，否则只是commit就行了
//    val requireDoSync = rememberSaveable { mutableStateOf(false) }

    //修改状态，显示弹窗
    val showSetUpstreamDialog  =rememberSaveable { mutableStateOf(false)}
    val upstreamDialogOnOkText  =rememberSaveable { mutableStateOf("")}
    val initSetUpstreamDialog:(List<String>, String, String, String) -> Unit = { remoteList, curBranchShortName, curBranchFullName, onOkText ->
        //设置远程名
        upstreamRemoteOptionsList.value.clear()
        upstreamRemoteOptionsList.value.addAll(remoteList)
//                upstreamRemoteOptionsList.requireRefreshView()

        upstreamSelectedRemote.intValue = 0  //默认选中第一个remote，每个仓库至少有一个origin remote，应该不会出错

        //默认选中为上游设置和本地分支相同名
        upstreamBranchSameWithLocal.value = true
        //把远程分支初始化为当前分支的短名
        upstreamBranchShortRefSpec.value = curBranchShortName

        //设置当前分支，用来让用户知道自己在为哪个分支设置上游
        upstreamCurBranchShortName.value = curBranchShortName
        upstreamCurBranchFullName.value = curBranchFullName

        upstreamDialogOnOkText.value = onOkText
        //修改状态，显示弹窗，在弹窗设置完后，应该就不会进入这个判断了
        showSetUpstreamDialog.value = true
    }


//    val stageFailedStrRes = stringResource(R.string.stage_failed)
    val successCommitStrRes = stringResource(R.string.commit_success)


    val plzSetUpStreamForCurBranch = stringResource(R.string.please_set_upstream_for_current_branch)

    val doStageAll = { curRepo:RepoEntity ->
        //第2个参数代表使用参数提供的列表，第3个参数是当前仓库所有的changelist列表，这样就实现了stage all
        ChangeListFunctions.doStage(
            curRepo = curRepo,
            requireCloseBottomBar = true,
            userParamList = true,
            paramList = itemList.value.toList(),
            fromTo = fromTo,
            selectedListIsEmpty = selectedListIsEmpty,
            requireShowToast = requireShowToast,
            noItemSelectedStrRes = noItemSelectedStrRes,
            activityContext = activityContext,
            selectedItemList = selectedItemList.value.toList(),
            loadingText = loadingText,
            nFilesStagedStrRes = nFilesStagedStrRes,
            bottomBarActDoneCallback = bottomBarActDoneCallback
        )
    }


    val doStageAll_2 = { paramList:List<StatusTypeEntrySaver>, curRepo:RepoEntity, requireCloseBottomBar:Boolean->
        //第2个参数代表使用参数提供的列表，第3个参数是当前仓库所有的changelist列表，这样就实现了stage all
        ChangeListFunctions.doStage(
            curRepo = curRepo,
            requireCloseBottomBar = requireCloseBottomBar,
            userParamList = true,
            paramList = paramList,
            fromTo = fromTo,
            selectedListIsEmpty = selectedListIsEmpty,
            requireShowToast = requireShowToast,
            noItemSelectedStrRes = noItemSelectedStrRes,
            activityContext = activityContext,
            selectedItemList = selectedItemList.value.toList(),
            loadingText = loadingText,
            nFilesStagedStrRes = nFilesStagedStrRes,
            bottomBarActDoneCallback = bottomBarActDoneCallback
        )
    }

    //调用者记得刷新页面
    val doAbortMerge:suspend (RepoEntity)->Unit = { curRepo:RepoEntity ->
        loadingText.value = activityContext.getString(R.string.aborting_merge)

        // Abort Merge
        Repository.open(curRepo.fullSavePath).use { repo ->
            val ret = Libgit2Helper.resetHardToHead(repo)
            if(ret.hasError()) {
                requireShowToast(ret.msg)
                createAndInsertError(curRepo.id, ret.msg)
            }else {
                requireShowToast(activityContext.getString(R.string.success))
            }
//            changeStateTriggerRefreshPage(needRefreshChangeListPage)  //改成由调用者负责刷新页面了
        }
    }


    val showPushForceDialog = rememberSaveable { mutableStateOf(false)}
    if(showPushForceDialog.value) {
        ConfirmDialog2(
            title = stringResource(id = R.string.push_force),
            requireShowTextCompose = true,
            textCompose = {
                ScrollableColumn {
                    Text(stringResource(id = R.string.will_force_overwrite_remote_branch_even_it_is_ahead_to_local),
                        color = MyStyleKt.TextColor.danger()
                    )
                }
            },
            okTextColor = MyStyleKt.TextColor.danger(),
            onCancel = { showPushForceDialog.value = false }
        ) {
            showPushForceDialog.value = false

            doJobThenOffLoading(
                loadingOn,  //注：这函数内会自动禁用顶栏按钮，无需手动 `enableActionFromParent.value=false`
                loadingOff,
                activityContext.getString(R.string.force_pushing)
            ) {
                val curRepo = curRepoFromParentPage.value
                try {
//                    val success = doPush(true, null, force=true)
                    val success = ChangeListFunctions.doPush(
                        requireCloseBottomBar = true,
                        upstreamParam = null,
                        force = true,
                        curRepoFromParentPage = curRepo,
                        requireShowToast = requireShowToast,
                        appContext = activityContext,
                        loadingText = loadingText,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        dbContainer = dbContainer
                    )

                    if(!success) {
                        requireShowToast(activityContext.getString(R.string.push_force_failed))
                    }else {
                        requireShowToast(activityContext.getString(R.string.push_force_success))
                    }

                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require Push(Force) error:"+e.stackTraceToString(), activityContext.getString(R.string.push_force_failed)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }
        }
    }



    val openFileWithInnerEditor = { filePath:String, initMergeMode:Boolean ->
        openFileWithInnerSubPageEditor(
            filePath = filePath,
            mergeMode = initMergeMode,

            //cl页面不可能打开app内置目录下的文件，所以read only初始化为关闭即可
            readOnly = false,
        )
    }

    val goParentChangeList = { curRepo:RepoEntity ->
        val parentId = curRepo.parentRepoId
        if(parentId.isBlank()) {
            Msg.requireShow(activityContext.getString(R.string.not_found))
        }else {
            val target = changeListRepoList?.value?.find { it.id == parentId }
            if(target == null) {
                Msg.requireShow(activityContext.getString(R.string.not_found))
            }else {
                goToChangeListPage(target)
            }
        }

        Unit
    }



    //有两层防止重复执行的保险：一层是立刻改状态；二层是用加锁的缓存取出请求执行操作的key后就清掉，这样即使第一层破防，第二层也能阻止重复执行。
    //不过第二层保险的必要性可能不是很大，有点带安全帽撞棉花的感觉，有意义，但不大，不过对性能开销影响也不大，所以就不删了。
    //顶栏action请求执行pull/push/sync
    if(requireDoActFromParent.value) {  // pull
        //先把开关关了，避免重复执行，当然，重复执行其实也没事，因为我做了处理
        requireDoActFromParent.value = false  //防止重复执行的第一重保险，立刻把状态变量改成假
        MyLog.d(TAG, "requireDoActFromParent, act is:"+Cache.get(Cache.Key.changeListInnerPage_requireDoActFromParent))
        //执行操作
        doJobThenOffLoading(loadingOn,
            loadingOff={
                      loadingOff() //解除loading
                      enableActionFromParent.value=true  //重新启用顶栏的按钮
        }, requireDoActFromParentShowTextWhenDoingAct.value) {
            val requireAct = Cache.syncGetThenDel(Cache.Key.changeListInnerPage_requireDoActFromParent)
            //防止重复执行的第二层保险，取出请求执行操作的key后就删除，这样即使状态变量没及时更新导致重复执行，执行到这时也会取出null而终止操作

            val curRepo = curRepoFromParentPage.value
            if(requireAct==PageRequest.editIgnoreFile) {
                try {
                    Repository.open(curRepo.fullSavePath).use { repo->
                        val ignoreFilePath = IgnoreMan.getFileFullPath(Libgit2Helper.getRepoGitDirPathNoEndsWithSlash(repo))

                        withMainContext {
                            val initMergeMode = false
                            openFileWithInnerEditor(ignoreFilePath, initMergeMode)
                        }
                    }
                }catch (e:Exception) {
                    Msg.requireShowLongDuration(e.localizedMessage ?: "err")
                    MyLog.e(TAG, "get ignore file for repo '${curRepo.repoName}' err: ${e.stackTraceToString()}")
                }


            }else if(requireAct==PageRequest.showInRepos) {
                goToRepoPage(repoId)
            }else if(requireAct==PageRequest.goParent) {
                goParentChangeList(curRepo)
            }else if(requireAct==PageRequest.pull) { // pull(fetch+merge)
                ChangeListFunctions.doPull(
                    curRepo = curRepo,
                    activityContext = activityContext,
                    dbContainer = dbContainer,
                    requireShowToast = requireShowToast,
                    loadingText = loadingText,
                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                    changeListRequireRefreshFromParentPage = changeListRequireRefreshFromParentPage,
                )

            }else if(requireAct==PageRequest.fetch) {
                try {
                    //设置仓库临时状态(把临时状态设置到缓存里，不退出app都有效，目的是为了使重新查列表后临时状态亦可见)，这样重新加载页面时依然能看到临时状态
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.pulling))

                    //执行操作
//                    val fetchSuccess = doFetch(null)
                    val fetchSuccess = ChangeListFunctions.doFetch(
                        remoteNameParam = null,
                        curRepoFromParentPage = curRepo,
                        requireShowToast = requireShowToast,
                        appContext = activityContext,
                        loadingText = loadingText,
                        dbContainer = dbContainer
                    )
                    if(!fetchSuccess) {
                        requireShowToast(activityContext.getString(R.string.fetch_failed))
                    }else {
                        requireShowToast(activityContext.getString(R.string.fetch_success))
                    }
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require fetch error:"+e.stackTraceToString(), activityContext.getString(R.string.fetch_failed)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                }finally {
                    //清除缓存中的仓库状态
//                    RepoStatusUtil.clearRepoStatus(repoId)
                    //刷新页面
                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()
                }

            }else if(requireAct==PageRequest.pullRebase) {
                try {
                    //设置仓库临时状态(把临时状态设置到缓存里，不退出app都有效，目的是为了使重新查列表后临时状态亦可见)，这样重新加载页面时依然能看到临时状态
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.pulling))

                    //执行操作
//                    val fetchSuccess = doFetch(null)
                    val fetchSuccess = ChangeListFunctions.doFetch(
                        remoteNameParam = null,
                        curRepoFromParentPage = curRepo,
                        requireShowToast = requireShowToast,
                        appContext = activityContext,
                        loadingText = loadingText,
                        dbContainer = dbContainer
                    )
                    if(!fetchSuccess) {
                        //刷新页面
//                        changeStateTriggerRefreshPage(needRefreshChangeListPage)

                        requireShowToast(activityContext.getString(R.string.fetch_failed))
                    }else {
//                        val mergeSuccess = doMerge(true, null, true, trueMergeFalseRebase = false)
                        val mergeSuccess = ChangeListFunctions.doMerge(
                            requireCloseBottomBar = true,
                            upstreamParam = null,
                            showMsgIfHasConflicts = true,
                            trueMergeFalseRebase = false,
                            curRepoFromParentPage = curRepo,
                            requireShowToast = requireShowToast,
                            appContext = activityContext,
                            loadingText = loadingText,
                            bottomBarActDoneCallback = bottomBarActDoneCallback
                        )
                        if(!mergeSuccess){
                            requireShowToast(activityContext.getString(R.string.rebase_failed))
                        }else {
                            requireShowToast(activityContext.getString(R.string.pull_rebase_success))
                        }
                    }
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require Pull(Rebase) error:"+e.stackTraceToString(), activityContext.getString(R.string.pull_rebase_failed)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                }finally {
                    //清除缓存中的仓库状态
//                    RepoStatusUtil.clearRepoStatus(repoId)
                    //刷新页面
                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()
                }

                //push
            }else if(requireAct ==PageRequest.push) {
//                RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.pushing))

//                MyLog.d(TAG,"require doPush from top bar action")
                try {
//                    val success = doPush(true, null)
                    val success = ChangeListFunctions.doPush(
                        requireCloseBottomBar = true,
                        upstreamParam = null,
                        force = false,
                        curRepoFromParentPage = curRepo,
                        requireShowToast = requireShowToast,
                        appContext = activityContext,
                        loadingText = loadingText,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        dbContainer = dbContainer
                    )
                    if(!success) {
                        requireShowToast(activityContext.getString(R.string.push_failed))
                    }else {
                        requireShowToast(activityContext.getString(R.string.push_success))
                    }

                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require push error:"+e.stackTraceToString(), activityContext.getString(R.string.push_failed)+":"+e.localizedMessage, requireShowToast,curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }

            //sync
            }else if(requireAct ==PageRequest.pushForce) {
//                RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.pushing))

                MyLog.d(TAG,"require doPushForce from top bar action")

                showPushForceDialog.value = true

            //sync
            }else if(requireAct == PageRequest.sync) {
                try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.syncing))
//                    doSync(true)
                    ChangeListFunctions.doSync(
                        requireCloseBottomBar = true,
                        trueMergeFalseRebase = true,
                        curRepoFromParentPage = curRepo,
                        requireShowToast = requireShowToast,
                        appContext = activityContext,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        plzSetUpStreamForCurBranch = plzSetUpStreamForCurBranch,
                        initSetUpstreamDialog = initSetUpstreamDialog,
                        loadingText = loadingText,
                        dbContainer = dbContainer
                    )
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require sync error:"+e.stackTraceToString(), activityContext.getString(R.string.sync_failed)+":"+e.localizedMessage, requireShowToast,curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.syncRebase) {
                try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.syncing))
//                    doSync(true, trueMergeFalseRebase = false)
                    ChangeListFunctions.doSync(
                        requireCloseBottomBar = true,
                        trueMergeFalseRebase = false,
                        curRepoFromParentPage = curRepo,
                        requireShowToast = requireShowToast,
                        appContext = activityContext,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        plzSetUpStreamForCurBranch = plzSetUpStreamForCurBranch,
                        initSetUpstreamDialog = initSetUpstreamDialog,

                        loadingText = loadingText,
                        dbContainer = dbContainer
                    )
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require Sync(Rebase) error:"+e.stackTraceToString(), activityContext.getString(R.string.sync_rebase_failed)+":"+e.localizedMessage, requireShowToast,curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.mergeAbort) {
                initMergeAbortDialog()
            }else if(requireAct == PageRequest.stageAll) {
                try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.staging))

                    doStageAll(curRepo)
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require stage_all error:"+e.stackTraceToString(), activityContext.getString(R.string.stage_all_failed)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.indexToWorkTree_CommitAll) {
                // workTreeToIndex的提交所有和Index页面的提交的区别在于：workTreeToIndex在提交前会先执行stage all，并且itemList传null以触发强制重查indexList


                try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.staging))
                    val listForStage = itemList.value.toList()

                    if(AppModel.devModeOn) {
                        MyLog.d(TAG, "listForStage.isEmpty() = ${listForStage.isEmpty()}")
                    }

                    // stage if need，不管是否stage，都一定会执行commit，因为就算index为空，也可amend commit
                    if(listForStage.isNotEmpty()) {
                        val requireCloseBottombar = false
                        doStageAll_2(listForStage, curRepo, requireCloseBottombar)
                    }

                    //执行commit
                    ChangeListFunctions.doCommit(
                        requireShowCommitMsgDialog = true,
                        cmtMsg = "",
                        requireCloseBottomBar = true,
//                            requireDoSync = false,
                        curRepoFromParentPage = curRepo,
                        refreshChangeList = changeListRequireRefreshFromParentPage,
                        username = username,
                        email = email,
                        requireShowToast = requireShowToast,
                        pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                        showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                        amendCommit = amendCommit,
                        overwriteAuthor = overwriteAuthor,
                        showCommitMsgDialog = showCommitMsgDialog,
                        repoState = repoState,
                        activityContext = activityContext,
                        loadingText = loadingText,
                        repoId = repoId,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        fromTo = fromTo,
                        itemList = null,  //直接传null，创建提交前会查index list
                        successCommitStrRes = successCommitStrRes,
                        indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                        commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                            showPushForCommitDialog=showPushForCommitDialog
                    )

                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require commit all error:"+e.stackTraceToString(), activityContext.getString(R.string.commit_err)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    //提交会弹窗，不要在这里刷新页面，在弹窗回调里刷新
//                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.commit) {
                try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.committing))
//                    doCommit(true, "", true, false)
                    ChangeListFunctions.doCommit(
                        requireShowCommitMsgDialog = true,
                        cmtMsg = "",
                        requireCloseBottomBar = true,
//                        requireDoSync = false,
                        curRepoFromParentPage = curRepo,
                        refreshChangeList = changeListRequireRefreshFromParentPage,
                        username = username,
                        email = email,
                        requireShowToast = requireShowToast,
                        pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                        showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                        amendCommit = amendCommit,
                        overwriteAuthor = overwriteAuthor,
                        showCommitMsgDialog = showCommitMsgDialog,
                        repoState = repoState,
                        activityContext = activityContext,
                        loadingText = loadingText,
                        repoId = repoId,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        fromTo = fromTo,
                        itemList = itemList.value,
                        successCommitStrRes = successCommitStrRes,
                        indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                        commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                        showPushForCommitDialog=showPushForCommitDialog
                    )
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require commit error:"+e.stackTraceToString(), activityContext.getString(R.string.commit_failed)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

//                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.mergeContinue) {
                try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.continue_merging))

                    val repoFullPath = curRepo.fullSavePath
                    Repository.open(repoFullPath).use {repo ->
                        val readyRet = Libgit2Helper.readyForContinueMerge(repo, activityContext)
                        if(readyRet.hasError()) {
                            Msg.requireShowLongDuration(readyRet.msg)

                            //显示的时候只显示简短错误信息，例如"请先解决冲突！"，存的时候存详细点，存上什么操作导致的错误，例如：“merge continue err:请先解决冲突”
                            val errPrefix= activityContext.getString(R.string.merge_continue_err)
                            createAndInsertError(repoId, "$errPrefix:${readyRet.msg}")
                        }else {
//                            doCommit(true, "", true, false)
                            ChangeListFunctions.doCommit(
                                requireShowCommitMsgDialog = true,
                                cmtMsg = "",
                                requireCloseBottomBar = true,
//                                requireDoSync = false,
                                curRepoFromParentPage = curRepo,
                                refreshChangeList = changeListRequireRefreshFromParentPage,
                                username = username,
                                email = email,
                                requireShowToast = requireShowToast,
                                pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                                showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                                amendCommit = amendCommit,
                                overwriteAuthor = overwriteAuthor,
                                showCommitMsgDialog = showCommitMsgDialog,
                                repoState = repoState,
                                activityContext = activityContext,
                                loadingText = loadingText,
                                repoId = repoId,
                                bottomBarActDoneCallback = bottomBarActDoneCallback,
                                fromTo = fromTo,
                                itemList = itemList.value,
                                successCommitStrRes = successCommitStrRes,
                                indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                                commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                                showPushForCommitDialog=showPushForCommitDialog
                            )
                        }

                    }
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require Continue Merge error:"+e.stackTraceToString(), activityContext.getString(R.string.continue_merge_err)+":"+e.localizedMessage, requireShowToast,repoId)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

//                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.rebaseContinue) {
                try {
//                    doCommit(true, "", true, false)
                    ChangeListFunctions.doCommit(
                        requireShowCommitMsgDialog = true,
                        cmtMsg = "",
                        requireCloseBottomBar = true,
//                        requireDoSync = false,
                        curRepoFromParentPage = curRepo,
                        refreshChangeList = changeListRequireRefreshFromParentPage,
                        username = username,
                        email = email,
                        requireShowToast = requireShowToast,
                        pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                        showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                        amendCommit = amendCommit,
                        overwriteAuthor = overwriteAuthor,
                        showCommitMsgDialog = showCommitMsgDialog,
                        repoState = repoState,
                        activityContext = activityContext,
                        loadingText = loadingText,
                        repoId = repoId,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        fromTo = fromTo,
                        itemList = itemList.value,
                        successCommitStrRes = successCommitStrRes,
                        indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                        commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                        showPushForCommitDialog=showPushForCommitDialog
                    )
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require Rebase Continue error:"+e.stackTraceToString(), activityContext.getString(R.string.rebase_continue_err)+":"+e.localizedMessage, requireShowToast,repoId)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

//                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.rebaseSkip) {
                initRebaseSkipDialog(curRepo)

            }else if(requireAct == PageRequest.rebaseAbort) {
                initRebaseAbortDialog()

            }else if(requireAct == PageRequest.cherrypickContinue) {
                try {
//                    doCommit(true, "", true, false)
                    ChangeListFunctions.doCommit(
                        requireShowCommitMsgDialog = true,
                        cmtMsg = "",
                        requireCloseBottomBar = true,
//                        requireDoSync = false,
                        curRepoFromParentPage = curRepo,
                        refreshChangeList = changeListRequireRefreshFromParentPage,
                        username = username,
                        email = email,
                        requireShowToast = requireShowToast,
                        pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                        showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                        amendCommit = amendCommit,
                        overwriteAuthor = overwriteAuthor,
                        showCommitMsgDialog = showCommitMsgDialog,
                        repoState = repoState,
                        activityContext = activityContext,
                        loadingText = loadingText,
                        repoId = repoId,
                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                        fromTo = fromTo,
                        itemList = itemList.value,
                        successCommitStrRes = successCommitStrRes,
                        indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                        commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                        showPushForCommitDialog=showPushForCommitDialog
                    )
                }catch (e:Exception){
                    showErrAndSaveLog(TAG,"require Cherrypick Continue error:"+e.stackTraceToString(), activityContext.getString(R.string.cherrypick_continue_err)+":"+e.localizedMessage, requireShowToast,repoId)
                }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

//                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }else if(requireAct == PageRequest.cherrypickAbort) {
                initCherrypickAbortDialog()
            }

        }
    }

    //获取 left..right 中右边的
    val getCommitRight = {
        if(swap) {
            commit1OidStr
        }else {
            commit2OidStr
        }
    }

//    获取 left..right 中左边的
    val getCommitLeft = {
        if(swap) {
            commit2OidStr
        }else {
            commit1OidStr
        }
    }

    if(showRebaseSkipDialog.value) {
        val closeDialog = {
            showRebaseSkipDialog.value = false
        }

        ConfirmDialog(
            title=stringResource(R.string.rebase_skip),
            text=stringResource(R.string.are_you_sure),
            okTextColor = MyStyleKt.TextColor.danger(),
            onCancel = {
                closeDialog()
            }
        ) {  //onOk
            closeDialog()

            val curRepo = curRepoFromParentPage.value
            doJobThenOffLoading(
                loadingOn = loadingOn,
                loadingOff = loadingOff,
                loadingText = activityContext.getString(R.string.loading)
            ) {
                try {
                    val repoFullPath = curRepo.fullSavePath
                    Repository.open(repoFullPath).use { repo ->
                        val (usernameFromConfig, emailFromConfig) = Libgit2Helper.getGitUsernameAndEmail(repo)

                        if (usernameFromConfig.isBlank() || emailFromConfig.isBlank()) {
                            Msg.requireShowLongDuration(activityContext.getString(R.string.plz_set_email_and_username_then_try_again))
                            showUsernameAndEmailDialog.value = true
                        } else {
                            val readyRet = Libgit2Helper.rebaseSkip(repo, activityContext, usernameFromConfig, emailFromConfig, settings = settings)
                            if (readyRet.hasError()) {
                                Msg.requireShowLongDuration(readyRet.msg)

                                //显示的时候只显示简短错误信息，例如"请先解决冲突！"，存的时候存详细点，存上什么操作导致的错误，例如：“merge continue err:请先解决冲突”
                                val errPrefix = activityContext.getString(R.string.rebase_skip_err)
                                createAndInsertError(repoId, "$errPrefix:${readyRet.msg}")
                            } else {
                                Msg.requireShow(activityContext.getString(R.string.rebase_success))
                            }
                        }
                    }
                } catch (e: Exception) {
                    showErrAndSaveLog(
                        TAG,
                        "require Rebase Skip error:" + e.stackTraceToString(),
                        activityContext.getString(R.string.rebase_skip_err) + ":" + e.localizedMessage,
                        requireShowToast,
                        repoId
                    )
                } finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                    changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                }
            }
        }
    }

    if(showMergeAbortDialog.value || showRebaseAbortDialog.value || showCherrypickAbortDialog.value) {
        val closeDialog = {
            showMergeAbortDialog.value = false
            showRebaseAbortDialog.value = false
            showCherrypickAbortDialog.value = false
        }

        ConfirmDialog(
            title=stringResource(R.string.abort),
            text=stringResource(R.string.abort_merge_notice_text),
            okTextColor = MyStyleKt.TextColor.danger(),
            onCancel = {
                closeDialog()
            }
        ) {  //onOk
//            val nullMergeTrueRebaseFalseCherrypick = if(showMergeAbortDialog.value) null else if(showRebaseAbortDialog.value) true else false
            val nullMergeTrueRebaseFalseCherrypick = if(showMergeAbortDialog.value) null else showRebaseAbortDialog.value

            closeDialog()

            val curRepo = curRepoFromParentPage.value
            doJobThenOffLoading(
                loadingOn = loadingOn,
                loadingOff = loadingOff,
                loadingText = activityContext.getString(R.string.aborting)
            ) {
                if(nullMergeTrueRebaseFalseCherrypick == null) {  // merge abort
                    try {
//                    RepoStatusUtil.setRepoStatus(repoId, appContext.getString(R.string.aborting_merge))

                        doAbortMerge(curRepo)

                    }catch (e:Exception){
                        showErrAndSaveLog(TAG,"require abort_merge error:"+e.stackTraceToString(), activityContext.getString(R.string.abort_merge_failed)+":"+e.localizedMessage, requireShowToast, curRepo.id)
                    }finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                        changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                    }
                }else if(nullMergeTrueRebaseFalseCherrypick) {  // rebase abort
                    try {
                        val repoFullPath = curRepo.fullSavePath
                        Repository.open(repoFullPath).use { repo ->
                            val readyRet = Libgit2Helper.rebaseAbort(repo)
                            if (readyRet.hasError()) {
                                Msg.requireShowLongDuration(readyRet.msg)

                                //显示的时候只显示简短错误信息，例如"请先解决冲突！"，存的时候存详细点，存上什么操作导致的错误，例如：“merge continue err:请先解决冲突”
                                val errPrefix = activityContext.getString(R.string.rebase_abort_err)
                                createAndInsertError(repoId, "$errPrefix:${readyRet.msg}")
                            } else {
                                Msg.requireShow(activityContext.getString(R.string.rebase_aborted))
                            }
                        }
                    } catch (e: Exception) {
                        showErrAndSaveLog(
                            TAG,
                            "require Rebase Abort error:" + e.stackTraceToString(),
                            activityContext.getString(R.string.rebase_abort_err) + ":" + e.localizedMessage,
                            requireShowToast,
                            repoId
                        )
                    } finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                        changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                    }
                }else {  // cherrypick abort
                    try {
                        val repoFullPath = curRepo.fullSavePath
                        Repository.open(repoFullPath).use { repo ->
                            val readyRet = Libgit2Helper.cherrypickAbort(repo)
                            if (readyRet.hasError()) {
                                Msg.requireShowLongDuration(readyRet.msg)

                                //显示的时候只显示简短错误信息，例如"请先解决冲突！"，存的时候存详细点，存上什么操作导致的错误，例如：“merge continue err:请先解决冲突”
                                val errPrefix = activityContext.getString(R.string.cherrypick_abort_err)
                                createAndInsertError(repoId, "$errPrefix:${readyRet.msg}")
                            } else {
                                Msg.requireShow(activityContext.getString(R.string.cherrypick_aborted))
                            }
                        }
                    } catch (e: Exception) {
                        showErrAndSaveLog(
                            TAG,
                            "require Cherrypick Abort error:" + e.stackTraceToString(),
                            activityContext.getString(R.string.cherrypick_abort_err) + ":" + e.localizedMessage,
                            requireShowToast,
                            repoId
                        )
                    } finally {
//                    RepoStatusUtil.clearRepoStatus(repoId)

                        changeListRequireRefreshFromParentPage(curRepo)
//                    refreshRepoPage()

                    }
                }
            }
        }
    }


    if(showMergeAcceptTheirsOrOursDialog.value) {
        val acceptTheirs = mergeAcceptTheirs.value
        ConfirmDialog(
            title=if(acceptTheirs) stringResource(R.string.accept_theirs) else stringResource(R.string.accept_ours),
            text=stringResource(R.string.ask_do_operation_for_selected_conflict_items),
            okTextColor = MyStyleKt.TextColor.danger(),
            onCancel = {
                showMergeAcceptTheirsOrOursDialog.value=false
            }
        ) {  //onOk
            showMergeAcceptTheirsOrOursDialog.value=false
            val curRepo = curRepoFromParentPage.value

            doJobThenOffLoading(
                loadingOn = loadingOn,
                loadingOff = loadingOff,
                loadingText = activityContext.getString(R.string.loading)
            ) {
                ChangeListFunctions.doAccept(
                    curRepo = curRepo,
                    acceptTheirs = acceptTheirs,
                    loadingText = loadingText,
                    activityContext = activityContext,
                    hasConflictItemsSelected = hasConflictItemsSelected,
                    requireShowToast = requireShowToast,
                    selectedItemList = selectedItemList.value.toList(),
                    repoState = repoState,
                    repoId = repoId,
                    fromTo = fromTo,
                    selectedListIsEmpty = selectedListIsEmpty,
                    noItemSelectedStrRes = noItemSelectedStrRes,
                    nFilesStagedStrRes = nFilesStagedStrRes,
                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                    changeListRequireRefreshFromParentPage = changeListRequireRefreshFromParentPage,
                )
            }
        }
    }

    if(showSetUpstreamDialog.value) {
        val curRepo = curRepoFromParentPage.value
        SetUpstreamDialog(
            callerTag = TAG,
            curRepo = curRepo,
            loadingOn = loadingOn,
            loadingOff = loadingOff,
            showClear = false,  //这个页面肯定是没上游才会显示设置上游的弹窗，所以不需要显示clear
            remoteList = upstreamRemoteOptionsList.value,
            curBranchShortName = upstreamCurBranchShortName.value,  //供显示的，让用户知道在为哪个分支设置上游
            curBranchFullName = upstreamCurBranchFullName.value,
            selectedOption = upstreamSelectedRemote,
            upstreamBranchShortName = upstreamBranchShortRefSpec,
            upstreamBranchShortNameSameWithLocal = upstreamBranchSameWithLocal,
            onOkText = upstreamDialogOnOkText.value,
            isCurrentBranchOfRepo = true,
            onSuccessCallback = {
                //提示用户：上游已保存
                requireShowToast(activityContext.getString(R.string.upstream_saved))
                //把loading信息改成正在同步
                loadingOn(activityContext.getString(R.string.syncing))

                //重新执行doSync()
//                            doSync(true)
                ChangeListFunctions.doSync(
                    requireCloseBottomBar = true,
                    trueMergeFalseRebase = true,
                    curRepoFromParentPage = curRepo,
                    requireShowToast = requireShowToast,
                    appContext = activityContext,
                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                    plzSetUpStreamForCurBranch = plzSetUpStreamForCurBranch,
                    initSetUpstreamDialog = initSetUpstreamDialog,

                    loadingText = loadingText,
                    dbContainer = dbContainer
                )

            },
            onErrorCallback = {
                requireShowToast(activityContext.getString(R.string.set_upstream_error))
            },
            closeDialog = {
                showSetUpstreamDialog.value = false
            },
            onCancel = {
                showSetUpstreamDialog.value = false
                //刷新页面是为了显示提交后或stage后的change list
                changeListRequireRefreshFromParentPage(curRepo)
            },
            onClearErrorCallback = {},
            onClearFinallyCallback = {},
            onClearSuccessCallback = {},
            onFinallyCallback = {},  //这里不用刷新页面，因为在这个页面设置完上游还要执行其他操作，其他操作结束后会刷新页面

        )
    }

    if(showCommitMsgDialog.value) {
        val curRepo = curRepoFromParentPage.value
        val repoIsNotDetached = !dbIntToBool(curRepo.isDetached)
        RequireCommitMsgDialog(
            curRepo = curRepo,
            repoPath = curRepo.fullSavePath,
            repoState=repoState.intValue,
            overwriteAuthor=overwriteAuthor,
            amend=amendCommit,
            commitMsg=commitMsg,
            commitBtnText = commitBtnTextForCommitDialog.value,
            showPush = repoIsNotDetached,
            showSync = repoIsNotDetached,
            onOk={curRepo, msgOrAmendMsg, requirePush, requireSync->
                //把弹窗相关的状态变量设置回初始状态，不然只能提交一次，下次就不弹窗了(ps:这个放coroutine里修改会报状态异常，提示不能并发修改状态，之前记得能修改，不明所以)
                showCommitMsgDialog.value = false  //关闭弹窗

                val cmtMsg = msgOrAmendMsg  //存上实际的提交信息，若勾选Amend实际值就是amendMsg的值否则是commitMsg的值
                commitMsg.value = "" //清空提交信息状态

                doJobThenOffLoading(loadingOn, loadingOff,activityContext.getString(R.string.committing)) {
                    try {
//                        // do stage, then do commit
//                        if(!doStage(false)) {  //如果stage失败，提示错误并返回，或许该记日志？
//                            requireShowToast(stageFailedStrRes)
//                            return@launch
//                        }
//                        val requireDoSync:Boolean = Cache.getByType<Boolean>(Cache.Key.changeListInnerPage_RequireDoSyncAfterCommit)?:false

                        //执行commit
//                        val commitSuccess = doCommit(false, cmtMsg, !requireDoSync, requireDoSync)
                        val commitSuccess = ChangeListFunctions.doCommit(
                            requireShowCommitMsgDialog = false,
                            cmtMsg = cmtMsg,
                            requireCloseBottomBar = !(requireSync || requirePush),
//                            requireDoSync = requireDoSync,
                            curRepoFromParentPage = curRepo,
                            refreshChangeList = changeListRequireRefreshFromParentPage,
                            username = username,
                            email = email,
                            requireShowToast = requireShowToast,
                            pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                            showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                            amendCommit = amendCommit,
                            overwriteAuthor = overwriteAuthor,
                            showCommitMsgDialog = showCommitMsgDialog,
                            repoState = repoState,
                            activityContext = activityContext,
                            loadingText = loadingText,
                            repoId = repoId,
                            bottomBarActDoneCallback = bottomBarActDoneCallback,
                            fromTo = fromTo,
                            itemList = itemList.value,
                            successCommitStrRes = successCommitStrRes,
                            indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                            commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                            showPushForCommitDialog=showPushForCommitDialog
                        )

                        if(requireSync) {
                            if(commitSuccess){
                                //更新loading提示文案
                                loadingText.value = activityContext.getString(R.string.syncing)

//                                doSync(true)
                                ChangeListFunctions.doSync(
                                    requireCloseBottomBar = true,
                                    trueMergeFalseRebase = true,
                                    curRepoFromParentPage = curRepo,
                                    requireShowToast = requireShowToast,
                                    appContext = activityContext,
                                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                                    plzSetUpStreamForCurBranch = plzSetUpStreamForCurBranch,
                                    initSetUpstreamDialog = initSetUpstreamDialog,

                                    loadingText = loadingText,
                                    dbContainer = dbContainer
                                )
                            }else {
                                requireShowToast(activityContext.getString(R.string.sync_canceled_by_commit_failed))
                            }
                        }else if(requirePush) {
                            if(commitSuccess) {
                                loadingText.value = activityContext.getString(R.string.pushing)

                                val success = ChangeListFunctions.doPush(
                                    requireCloseBottomBar = true,
                                    upstreamParam = null,
                                    force = false,
                                    curRepoFromParentPage = curRepo,
                                    requireShowToast = requireShowToast,
                                    appContext = activityContext,
                                    loadingText = loadingText,
                                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                                    dbContainer = dbContainer
                                )

                                if (!success) {
                                    requireShowToast(activityContext.getString(R.string.push_failed))
                                } else {
                                    requireShowToast(activityContext.getString(R.string.push_success))
                                }
                            }else {
                                requireShowToast(activityContext.getString(R.string.push_canceled_by_commit_failed))
                            }
                        }

                        //关闭底栏
//                        bottomBarActDoneCallback("")

                    }catch (e:Exception){
//                        e.printStackTrace()
                        MyLog.e(TAG, "#doCommit at showCommitMsgDialog #onOk:" + e.stackTraceToString())
                    }
                }
            },
            indexIsEmptyForCommitDialog = indexIsEmptyForCommitDialog,
            onCancel={curRepo ->
                showCommitMsgDialog.value = false

                changeListRequireRefreshFromParentPage(curRepo)
            },
        )
    }

    val savedStrRes = stringResource(R.string.saved)

    val invalidUsernameOrEmail = stringResource(R.string.invalid_username_or_email)
    val errorUnknownSelectionStrRes = stringResource(R.string.error_unknown_selection)

    if(showUsernameAndEmailDialog.value) {
        //请求用户设置用户名和邮箱的弹窗
        AskGitUsernameAndEmailDialogWithSelection(
            curRepo = curRepoFromParentPage.value,
            text = stringResource(R.string.please_set_username_email_before_commit),
            optionsList = usernameAndEmailDialogOptionList,
            selectedOption = usernameAndEmailDialogSelectedOption,
            username = username,
            email = email,
            onOk = {curRepo ->
                doJobThenOffLoading(
                    loadingOn = loadingOn,
                    loadingOff = loadingOff,
//                    loadingText = appContext.getString(R.string.saving)
                ) saveUsernameAndEmail@{
                    //关闭弹窗
                    showUsernameAndEmailDialog.value = false
                    //debug
//                println("username.value::"+username.value)
//                println("email.value::"+email.value)
                    //debug

                    //如果用户名和邮箱都不为空，保存然后执行提交，否则显示提示信息
                    if (username.value.isNotBlank() && email.value.isNotBlank()) {
                        //save username and email to global or repo, then redo commit
                        if (usernameAndEmailDialogSelectedOption.intValue == optNumSetUserAndEmailForGlobal) {  //为全局设置用户名和邮箱
                            //如果保存失败，return
                            if (!Libgit2Helper.saveGitUsernameAndEmailForGlobal(
                                    requireShowErr = requireShowToast,
                                    errText = errWhenQuerySettingsFromDbStrRes,
                                    errCode1 = "1",
                                    errCode2 = "2",   //??? wtf errcode?
                                    username = username.value,
                                    email = email.value
                                )
                            ) {
                                return@saveUsernameAndEmail
                            }

                        } else if (usernameAndEmailDialogSelectedOption.intValue == optNumSetUserAndEmailForCurRepo) {  //为当前仓库设置用户名和邮箱
                            Repository.open(curRepo.fullSavePath).use { repo ->
                                //如果保存失败，return，方法内会提示错误信息，这里就不用再提示了
                                if (!Libgit2Helper.saveGitUsernameAndEmailForRepo(
                                        repo,
                                        requireShowToast,
                                        username = username.value,
                                        email = email.value
                                    )
                                ) {
                                    return@saveUsernameAndEmail
                                }
                            }
                        } else { //理论上不可能执行到这里，除非哪里不到导致没拿到选项编号
                            requireShowToast(errorUnknownSelectionStrRes)
                            return@saveUsernameAndEmail
                        }

                        //显示"已保存"提示信息
                        requireShowToast(savedStrRes)
//                        val requireDoSync:Boolean = Cache.getByType<Boolean>(Cache.Key.changeListInnerPage_RequireDoSyncAfterCommit)?:false

                        //重新执行commit，这次会从状态里取出用户名和邮箱，不对，因为上面已经存到配置文件里了，所以其实直接从配置文件就能取到
//                        doCommit(true, "", !requireDoSync, requireDoSync)
                        ChangeListFunctions.doCommit(
                            requireShowCommitMsgDialog = true,
                            cmtMsg = "",
//                            requireCloseBottomBar = !requireDoSync,
                            requireCloseBottomBar = true,
//                            requireDoSync = requireDoSync,
                            curRepoFromParentPage = curRepo,
                            refreshChangeList = changeListRequireRefreshFromParentPage,
                            username = username,
                            email = email,
                            requireShowToast = requireShowToast,
                            pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                            showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                            amendCommit = amendCommit,
                            overwriteAuthor = overwriteAuthor,
                            showCommitMsgDialog = showCommitMsgDialog,
                            repoState = repoState,
                            activityContext = activityContext,
                            loadingText = loadingText,
                            repoId = repoId,
                            bottomBarActDoneCallback = bottomBarActDoneCallback,
                            fromTo = fromTo,
                            itemList = itemList.value,
                            successCommitStrRes = successCommitStrRes,
                            indexIsEmptyForCommitDialog = indexIsEmptyForCommitDialog,
                            commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                            showPushForCommitDialog=showPushForCommitDialog
                        )
                    } else {
                        requireShowToast(invalidUsernameOrEmail)
                    }
                }

            },
            onCancel = {curRepo ->
                showUsernameAndEmailDialog.value = false
                email.value = ""
                username.value = ""
                requireShowToast(canceledStrRes)

                changeListRequireRefreshFromParentPage(curRepo)
            },

            enableOk = {
                username.value.isNotBlank() && email.value.isNotBlank()
            }
        )
    }


    val showCherrypickDialog = rememberSaveable { mutableStateOf(false)}
    val cherrypickTargetHash = rememberSaveable { mutableStateOf("")}
    val cherrypickParentHash = rememberSaveable { mutableStateOf("")}
    val cherrypickAutoCommit = rememberSaveable { mutableStateOf(false)}

    val initCherrypickDialog = { curRepo: RepoEntity ->
        doJobThenOffLoading job@{
            //得解析下hash，避免短id或分支名之类的
            Repository.open(curRepo.fullSavePath).use { repo->
                //commit1 左边的，是parent
                val ret=Libgit2Helper.resolveCommitByHashOrRef(repo, commit1OidStr)
                if(ret.hasError() || ret.data==null) {
                    Msg.requireShowLongDuration(ret.msg)
                    return@job
                }
                cherrypickParentHash.value =ret.data!!.id().toString()

                //解析右边的commit
                val ret2=Libgit2Helper.resolveCommitByHashOrRef(repo, commit2OidStr)
                if(ret2.hasError() || ret2.data==null) {
                    Msg.requireShowLongDuration(ret2.msg)
                    return@job
                }
                cherrypickTargetHash.value = ret2.data!!.id().toString()

                cherrypickAutoCommit.value = false

                showCherrypickDialog.value = true
            }
        }
    }

    if(showCherrypickDialog.value) {
        val shortTarget = Libgit2Helper.getShortOidStrByFull(cherrypickTargetHash.value)
        val shortParent = Libgit2Helper.getShortOidStrByFull(cherrypickParentHash.value)

        ConfirmDialog(
            title = stringResource(R.string.cherrypick),
            requireShowTextCompose = true,
            textCompose = {
                Column{
                    Text(text =  buildAnnotatedString {
                        append(stringResource(R.string.target)+": ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append(shortTarget)
                        }
                    },
                        modifier = Modifier.padding(horizontal = MyStyleKt.CheckoutBox.horizontalPadding)
                    )
                    Text(text =  buildAnnotatedString {
                        append(stringResource(R.string.parent)+": ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                            append(shortParent)
                        }
                    },
                        modifier = Modifier.padding(horizontal = MyStyleKt.CheckoutBox.horizontalPadding)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = stringResource(R.string.will_cherrypick_changes_of_selected_files_are_you_sure),
                        modifier = Modifier.padding(horizontal = MyStyleKt.CheckoutBox.horizontalPadding)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    MyCheckBox(text = stringResource(R.string.auto_commit), value = cherrypickAutoCommit)
                }
            },
            onCancel = { showCherrypickDialog.value = false }
        ) {
            showCherrypickDialog.value = false
            val curRepo = curRepoFromParentPage.value
            val selectedItemList = selectedItemList.value.toList()
            doJobThenOffLoading(
                loadingOn,
                loadingOff,
                activityContext.getString(R.string.cherrypicking)
            ) {
                val pathSpecs = selectedItemList.map{it.relativePathUnderRepo}
                Repository.open(curRepo.fullSavePath).use { repo->
                    val ret = Libgit2Helper.cherrypick(
                        repo,
                        targetCommitFullHash = cherrypickTargetHash.value,
                        parentCommitFullHash = cherrypickParentHash.value,
                        pathSpecList = pathSpecs,
                        autoCommit = cherrypickAutoCommit.value,
                        settings = settings
                    )

                    if(ret.hasError()) {
                        Msg.requireShowLongDuration(ret.msg)
                        if(ret.code != Ret.ErrCode.alreadyUpToDate) {  //如果错误码不是 Already up-to-date ，就log下

                            //选提交时记日志把files改成commits用来区分
                            createAndInsertError(repoId, "cherrypick files changes of '$shortParent..$shortTarget' err:"+ret.msg)
                        }
                    }else {
                        Msg.requireShow(activityContext.getString(R.string.success))
                    }
                }
            }
        }
    }

    val showIgnoreDialog = rememberSaveable { mutableStateOf(false)}
    if(showIgnoreDialog.value) {
        ConfirmDialog(
            title = stringResource(R.string.ignore),
            text = stringResource(R.string.will_ignore_selected_files_are_you_sure),
            onCancel = { showIgnoreDialog.value = false }
        ) {
            showIgnoreDialog.value=false

            val curRepo = curRepoFromParentPage.value
            val selectedItemList = selectedItemList.value.toList()

            doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.loading)) {
                try {
                    Repository.open(curRepo.fullSavePath).use { repo ->
                        val repoDotGitPath = Libgit2Helper.getRepoGitDirPathNoEndsWithSlash(repo)
                        val linesWillIgnore = selectedItemList.map { it.relativePathUnderRepo }
                        IgnoreMan.appendLinesToIgnoreFile(repoDotGitPath, linesWillIgnore)
                    }
                    Msg.requireShow(activityContext.getString(R.string.success))
                }catch (e:Exception) {
                    val errMsg = e.localizedMessage
                    Msg.requireShowLongDuration(errMsg ?: "err")
                    createAndInsertError(curRepo.id, "ignore files err: $errMsg")
                }finally {
                    changeListRequireRefreshFromParentPage(curRepo)
                }

            }
        }
    }



    val showCreatePatchDialog = rememberSaveable { mutableStateOf(false)}
    val savePatchPath= rememberSaveable { mutableStateOf("") } //给这变量一赋值app就崩溃，原因不明，报的是"java.lang.VerifyError: Verifier rejected class"之类的错误，日，后来升级gradle解决了
    val showSavePatchSuccessDialog = rememberSaveable { mutableStateOf(false)}

    if(showSavePatchSuccessDialog.value) {
//        val path = Cache.getByType<String>(Cache.Key.changeListInnerPage_SavePatchPath) ?:""
        val path = savePatchPath.value

        CopyableDialog(
            title = stringResource(R.string.success),
            text = replaceStringResList(stringResource(R.string.export_path_ph1_you_can_go_to_files_page_found_this_file), listOf(path)),
            okBtnText = stringResource(R.string.copy_path),
            onCancel = { showSavePatchSuccessDialog.value = false }
        ) {
            showSavePatchSuccessDialog.value = false

            clipboardManager.setText(AnnotatedString(path))
            Msg.requireShow(activityContext.getString(R.string.copied))
        }
    }


    if(showCreatePatchDialog.value) {
        ConfirmDialog(
            title = stringResource(R.string.create_patch),
            requireShowTextCompose = true,
            textCompose = {
                ScrollableColumn {
                    Text(text = stringResource(R.string.will_create_patch_for_selected_files_are_you_sure))
                }
            },
            onCancel = { showCreatePatchDialog.value = false }
        ){
            showCreatePatchDialog.value = false

            val curRepo = curRepoFromParentPage.value

            doJobThenOffLoading(loadingOn,loadingOff, activityContext.getString(R.string.creating_patch)) job@{
                try {
                    val savePatchRet = ChangeListFunctions.createPath(
                        curRepo = curRepo,
                        leftCommit = getCommitLeft(),
                        rightCommit = getCommitRight(),
                        fromTo = fromTo,
                        relativePaths = selectedItemList.value.map { it.relativePathUnderRepo }
                    );

                    if(savePatchRet.success()) {
//                            savePatchPath.value = getFilePathStrBasedRepoDir(outFile.canonicalPath, returnResultStartsWithSeparator = true)
                        savePatchPath.value = savePatchRet.data?.outFileFullPath ?: ""
                        //之前app给savePatchPath赋值会崩溃，所以用了Cache规避，后来升级gradle解决了
//                            Cache.set(Cache.Key.changeListInnerPage_SavePatchPath, getFilePathStrBasedRepoDir(outFile.canonicalPath, returnResultStartsWithSeparator = true))
                        showSavePatchSuccessDialog.value = true
                    }else {
                        //抛异常，catch里会向用户显示错误信息 (btw: exception.message或.localizedMessage都不包含异常类型名，对用户展示比较友好)
                        throw (savePatchRet.exception ?: RuntimeException(savePatchRet.msg))
                    }
                }catch (e:Exception) {
                    val errPrefix = "create patch err: "
                    Msg.requireShowLongDuration(e.localizedMessage ?: errPrefix)
                    createAndInsertError(curRepo.id, errPrefix+e.localizedMessage)
                    MyLog.e(TAG, "$errPrefix${e.stackTraceToString()}")
                }
            }
        }
    }
    



    val checkoutForce = rememberSaveable { mutableStateOf(false)}
    val showCheckoutFilesDialog = rememberSaveable { mutableStateOf(false)}
    val checkoutTargetHash = rememberSaveable { mutableStateOf("")}

    val initCheckoutDialog = { curRepo:RepoEntity, targetHash:String ->
        doJobThenOffLoading job@{
            Repository.open(curRepo.fullSavePath).use { repo ->
                val ret = Libgit2Helper.resolveCommitByHashOrRef(repo, targetHash)
                if (ret.hasError() || ret.data == null) {
                    Msg.requireShowLongDuration(ret.msg)
                    return@job
                }

                checkoutTargetHash.value = ret.data!!.id().toString()
                checkoutForce.value = false
                showCheckoutFilesDialog.value = true
            }
        }

    }

    if(showCheckoutFilesDialog.value) {
        ConfirmDialog(
            title = stringResource(R.string.checkout),
            requireShowTextCompose = true,
            textCompose = {
                Column{
                    Text(text =  buildAnnotatedString {
                            append(stringResource(R.string.target)+": ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(Libgit2Helper.getShortOidStrByFull(checkoutTargetHash.value))
                            }
                        },
                        modifier = Modifier.padding(horizontal = MyStyleKt.CheckoutBox.horizontalPadding)
                        )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = stringResource(R.string.will_checkout_selected_files_are_you_sure),
                        modifier = Modifier.padding(horizontal = MyStyleKt.CheckoutBox.horizontalPadding)
                        )
                    MyCheckBox(text = stringResource(R.string.force), value = checkoutForce)
                    if(checkoutForce.value) {
                        CheckBoxNoteText(
                            text = stringResource(R.string.if_local_has_uncommitted_changes_will_overwrite),
                            color = MyStyleKt.TextColor.danger(),
                        )
                    }
                }
            },
            okTextColor = if(checkoutForce.value) MyStyleKt.TextColor.danger() else Color.Unspecified,
            onCancel = { showCheckoutFilesDialog.value = false }
        ) {
            showCheckoutFilesDialog.value = false
            val curRepo = curRepoFromParentPage.value
            val selectedItemList = selectedItemList.value.toList()

            doJobThenOffLoading(
                loadingOn,
                loadingOff,
                activityContext.getString(R.string.checking_out)
            ) {
                val pathSpecs = selectedItemList.map{it.relativePathUnderRepo}
                Repository.open(curRepo.fullSavePath).use { repo->
                    val ret = Libgit2Helper.checkoutFiles(repo, checkoutTargetHash.value, pathSpecs, force=checkoutForce.value)
                    if(ret.hasError()) {
                        Msg.requireShowLongDuration(ret.msg)
                        createAndInsertError(repoId, "checkout files err:"+ret.msg)
                    }else {
                        Msg.requireShow(activityContext.getString(R.string.success))
                    }
                }
            }
        }
    }

    // 向下滚动监听，开始
//    val enableFilterState = rememberSaveable { mutableStateOf(false)}
//    val firstVisible = remember {derivedStateOf { if(enableFilterState.value) filterListState.value.firstVisibleItemIndex else itemListState.firstVisibleItemIndex }}
//    ScrollListener(
//        nowAt = firstVisible.value,
//        onScrollUp = {changelistPageScrollingDown.value = false}
//    ) { // onScrollDown
//        changelistPageScrollingDown.value = true
//    }
//
//    val lastAt = remember { mutableIntStateOf(0) }
//    val lastIsScrollDown = remember { mutableStateOf(false) }
//    val forUpdateScrollState = remember {
//        derivedStateOf {
//            val nowAt = if(enableFilterState.value) {
//                filterListState.firstVisibleItemIndex
//            } else {
//                itemListState.firstVisibleItemIndex
//            }
//
//            val scrolledDown = nowAt > lastAt.intValue  // scroll down
////            val scrolledUp = nowAt < lastAt.intValue
//
//            val scrolled = nowAt != lastAt.intValue  // scrolled
//            lastAt.intValue = nowAt
//
//            // only update state when this scroll down and last is not scroll down, or this is scroll up and last is not scroll up
//            if(scrolled && ((lastIsScrollDown.value && !scrolledDown) || (!lastIsScrollDown.value && scrolledDown))) {
//                changelistPageScrolled.value = true
//            }
//
//            lastIsScrollDown.value = scrolledDown
//        }
//    }.value
    // 向下滚动监听，结束

    val selectItem = { item: StatusTypeEntrySaver ->
        isFileSelectionMode.value = true
        UIHelper.selectIfNotInSelectedListElseNoop(item, selectedItemList.value)

        //只刷新选中列表即可
//        selectedItemList.requireRefreshView()

        //这个刷新太彻底，画面会闪，不要用
//        changeStateTriggerRefreshPage(needRefreshChangeListPage)
    }

    val selectAll = {
        val list = if(enableFilterState.value) filterList.value else itemList.value

        list.toList().forEach {
            selectItem(it)
        }

        Unit
//        list.forEach {
//            UIHelper.selectIfNotInSelectedListElseNoop(it, selectedItemList.value)
//        }
    }

    //BottomBar选项
    val iconOnClickList:List<()->Unit> = if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(  // ChangeList页面的底栏选项 ( worktree页面 )
//            commitThenSync@{
//                // commit then sync(pull then push)
//                doJobThenOffLoading(loadingOn=loadingOn,loadingOff=loadingOff, loadingText=activityContext.getString(R.string.executing_commit_then_sync)) {
//                    val stageSuccess = doStage(false, false, null)
//                    if(!stageSuccess){
//                        bottomBarActDoneCallback(activityContext.getString(R.string.stage_failed))
//                    }else {
//                        //显示commit弹窗，之后在其确认回调函数里执行后续操作
//                        //最后一个参数代表执行完提交后是否执行sync
////                        doCommit(true, "", true, true)
//                        ChangeListFunctions.doCommit(
//                            requireShowCommitMsgDialog = true,
//                            cmtMsg = "",
//                            requireCloseBottomBar = true,
////                            requireDoSync = true,
//                            curRepoFromParentPage = curRepoFromParentPage,
//                            refreshRequiredByParentPage = refreshRequiredByParentPage,
//                            username = username,
//                            email = email,
//                            requireShowToast = requireShowToast,
//                            pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
//                            showUserAndEmailDialog = showUserAndEmailDialog,
//                            amendCommit = amendCommit,
//                            overwriteAuthor = overwriteAuthor,
//                            showCommitMsgDialog = showCommitMsgDialog,
//                            repoState = repoState,
//                            appContext = activityContext,
//                            loadingText = loadingText,
//                            repoId = repoId,
//                            bottomBarActDoneCallback = bottomBarActDoneCallback,
//                            fromTo = fromTo,
//                            itemList = itemList,
//                            successCommitStrRes = successCommitStrRes,
//                            indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
//                            commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                            showPushForCommitDialog=showPushForCommitDialog
//                        )
//                    }
//                }
//            },

            commit@{
                val curRepo = curRepoFromParentPage.value
                doJobThenOffLoading(loadingOn=loadingOn,loadingOff=loadingOff, loadingText=activityContext.getString(R.string.committing)) {
                    val stageSuccess = ChangeListFunctions.doStage(
                        curRepo = curRepo,
                        requireCloseBottomBar = false,
                        userParamList = false,
                        paramList = null,

                        fromTo = fromTo,
                        selectedListIsEmpty = selectedListIsEmpty,
                        requireShowToast = requireShowToast,
                        noItemSelectedStrRes = noItemSelectedStrRes,
                        activityContext = activityContext,
                        selectedItemList = selectedItemList.value.toList(),
                        loadingText = loadingText,
                        nFilesStagedStrRes = nFilesStagedStrRes,
                        bottomBarActDoneCallback = bottomBarActDoneCallback
                    )

                    if(!stageSuccess){
                        bottomBarActDoneCallback(activityContext.getString(R.string.stage_failed), curRepo)
                    }else {
                        //显示commit弹窗，之后在其确认回调函数里执行后续操作
//                        doCommit(true,"", true, false)
                        ChangeListFunctions.doCommit(
                            requireShowCommitMsgDialog = true,
                            cmtMsg = "",
                            requireCloseBottomBar = true,
//                            requireDoSync = false,
                            curRepoFromParentPage = curRepo,
                            refreshChangeList = changeListRequireRefreshFromParentPage,
                            username = username,
                            email = email,
                            requireShowToast = requireShowToast,
                            pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                            showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                            amendCommit = amendCommit,
                            overwriteAuthor = overwriteAuthor,
                            showCommitMsgDialog = showCommitMsgDialog,
                            repoState = repoState,
                            activityContext = activityContext,
                            loadingText = loadingText,
                            repoId = repoId,
                            bottomBarActDoneCallback = bottomBarActDoneCallback,
                            fromTo = fromTo,
                            itemList = itemList.value,
                            successCommitStrRes = successCommitStrRes,
                            indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                            commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                            showPushForCommitDialog=showPushForCommitDialog
                        )
                    }
                }

            },

            selectAll@{
                //impl select all
                selectAll()
            },
        ) else if(fromTo == Cons.gitDiffFromHeadToIndex) listOf(  //index页面的底栏选项
//        commitThenSync@{
//            // commit then sync(pull then push)
//            doJobThenOffLoading(loadingOn=loadingOn,loadingOff=loadingOff, loadingText=activityContext.getString(R.string.executing_commit_then_sync)) {
//                //显示commit弹窗，之后在其确认回调函数里执行后续操作
//                //最后一个参数代表执行完提交后是否执行sync
////                doCommit(true, "", true, true)
//                ChangeListFunctions.doCommit(
//                    requireShowCommitMsgDialog = true,
//                    cmtMsg = "",
//                    requireCloseBottomBar = true,
////                    requireDoSync = true,
//                    curRepoFromParentPage = curRepoFromParentPage,
//                    refreshRequiredByParentPage = refreshRequiredByParentPage,
//                    username = username,
//                    email = email,
//                    requireShowToast = requireShowToast,
//                    pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
//                    showUserAndEmailDialog = showUserAndEmailDialog,
//                    amendCommit = amendCommit,
//                    overwriteAuthor = overwriteAuthor,
//                    showCommitMsgDialog = showCommitMsgDialog,
//                    repoState = repoState,
//                    appContext = activityContext,
//                    loadingText = loadingText,
//                    repoId = repoId,
//                    bottomBarActDoneCallback = bottomBarActDoneCallback,
//                    fromTo = fromTo,
//                    itemList = itemList,
//                    successCommitStrRes = successCommitStrRes,
//                    indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
//                    commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                    showPushForCommitDialog=showPushForCommitDialog
//                )
//            }
//        },

        commit@{
            val curRepo = curRepoFromParentPage.value
            doJobThenOffLoading(loadingOn=loadingOn,loadingOff=loadingOff, loadingText=activityContext.getString(R.string.committing)) {
                //显示commit弹窗，之后在其确认回调函数里执行后续操作
//                doCommit(true,"", true, false)
                ChangeListFunctions.doCommit(
                    requireShowCommitMsgDialog = true,
                    cmtMsg = "",
                    requireCloseBottomBar = true,
//                    requireDoSync = false,
                    curRepoFromParentPage = curRepo,
                    refreshChangeList = changeListRequireRefreshFromParentPage,
                    username = username,
                    email = email,
                    requireShowToast = requireShowToast,
                    pleaseSetUsernameAndEmailBeforeCommit = pleaseSetUsernameAndEmailBeforeCommit,
                    showUsernameAndEmailDialog = showUsernameAndEmailDialog,
                    amendCommit = amendCommit,
                    overwriteAuthor = overwriteAuthor,
                    showCommitMsgDialog = showCommitMsgDialog,
                    repoState = repoState,
                    activityContext = activityContext,
                    loadingText = loadingText,
                    repoId = repoId,
                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                    fromTo = fromTo,
                    itemList = itemList.value,
                    successCommitStrRes = successCommitStrRes,
                    indexIsEmptyForCommitDialog=indexIsEmptyForCommitDialog,
                    commitBtnTextForCommitDialog=commitBtnTextForCommitDialog,
//                    showPushForCommitDialog=showPushForCommitDialog
                )
            }

        },
        selectAll@{
            //impl select all
            selectAll()
        }
    )else if(fromTo == Cons.gitDiffFromTreeToTree) {
        listOf(  //tree to tree页面的底栏选项
            checkout@{
                val curRepo = curRepoFromParentPage.value
                // when diff a commit to local (commit..local), if I clicked the checkout, actually I expect checkout the left version,
                //  in other cases, I expect checkout the right version
                val target = if(localAtDiffRight.value) getCommitLeft() else getCommitRight()

                //x 废弃) 如果target是local，不需要checkout
//                if(target == Cons.gitLocalWorktreeCommitHash) {
//                    Msg.requireShow("canceled: target is local")
//                    return@checkout
//                }

                initCheckoutDialog(curRepo, target)
            },

            cherrypick@{
                val curRepo = curRepoFromParentPage.value
                //必须是和parents diff才能cherrypick
                if(commitParentList.value.isNotEmpty()) {
                    initCherrypickDialog(curRepo)
                }else {
                    Msg.requireShowLongDuration(activityContext.getString(R.string.cherrypick_only_work_for_diff_to_parents))
                }

            },
            createPatch@{
                showCreatePatchDialog.value = true
            },
            selectAll@{
                //impl select all
                selectAll()
            }
        )

    }else {
        listOf()
    }

    val revertStrRes = stringResource(R.string.revert_n_and_del_m_files)

    val enableMoreIcon = fromTo != Cons.gitDiffFromTreeToTree
    //话说用这个diffFromTo变量来判断其实并非本意，但也勉强说得过去，因为worktree的changelist和index页面所需要diff的不同，所以，也可以这么写，其实本该还有一个FromHeadToWorktree的diff变量的，但在这里用不上
    val moreItemTextList = (if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(
        if(UserUtil.isPro()) stringResource(id = R.string.accept_ours) else stringResource(id = R.string.accept_ours_pro_only),
        if(UserUtil.isPro()) stringResource(id = R.string.accept_theirs) else stringResource(id = R.string.accept_theirs_pro_only),
        stringResource(R.string.stage),
        stringResource(R.string.revert),
        stringResource(R.string.create_patch),
        if(proFeatureEnabled(ignoreWorktreeFilesTestPassed)) stringResource(R.string.ignore) else "",
        stringResource(R.string.import_as_repo),

    )  //按元素添加顺序在列表中会呈现为从上到下
    else if(fromTo == Cons.gitDiffFromHeadToIndex) listOf(stringResource(R.string.unstage), stringResource(R.string.create_patch), stringResource(R.string.import_as_repo),)
    else listOf()  // tree to tree，无选项
    ).asReversed()

    val showAcceptOursTheirs = (repoState.intValue == Repository.StateT.MERGE.bit || repoState.intValue == Repository.StateT.REBASE_MERGE.bit || repoState.intValue == Repository.StateT.CHERRYPICK.bit)

    val moreItemVisibleList = (if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(
        {showAcceptOursTheirs},  // visible for "accept ours"
        {showAcceptOursTheirs},  // visible for "accept theirs"
        {true},  // stage
        {true},  // revert
        {true},  // create patch
        {true},  // ignore
        {true}, // import as repo
    ) else listOf()  // empty list, always visible
    ).asReversed()

    val showRevertAlert = rememberSaveable { mutableStateOf(false)}
    val doRevert = { curRepo:RepoEntity, selectedItemList:List<StatusTypeEntrySaver> ->
            // impl Revert selected files
            //  需要弹窗确认，确认后loading，执行完操作后toast提示
            //  revert文件为index中的数据，注意是index，不是head
            //  revert时提示红字提示会删除状态为"new"的文件，因为这种文件是untracked，如果revert，就只能删除，vscode就是这么做的，合理。
                if(selectedListIsEmpty()) {  //因为无选中项时按钮禁用，所以一般不会执行这块，只是以防万一
                    requireShowToast(noItemSelectedStrRes)
                }else{
                    loadingText.value = activityContext.getString(R.string.reverting)

                    //取出数据库路径
                    Repository.open(curRepo.fullSavePath).use { repo ->
                        val untrakcedFileList = mutableListOf<String>()  // untracked list，在我的app里这种修改类型显示为 "New"
                        val pathspecList = mutableListOf<String>()  // modified、deleted 列表
                        selectedItemList.forEach {
                            //新文件(Untracked)在index里不存在，若revert，只能删除文件，所以单独加到另一个列表
                            if(it.changeType == Cons.gitStatusNew) {
                                untrakcedFileList.add(it.canonicalPath)  //删除文件，添加全路径（但其实用仓库内相对路径也行，只是需要把仓库路径和仓库下相对路径拼接一下，而这个全路径是我在查询status list的时候拼好的，所以直接用就行）
                            }else if(it.changeType != Cons.gitStatusConflict){  //冲突条目不可revert！其余index中有的文件，也就是git tracked的文件，删除/修改 之类的，都可恢复为index中的状态
                                pathspecList.add(it.relativePathUnderRepo)
                            }
                        }
                        //如果列表不为空，恢复文件
                        if(pathspecList.isNotEmpty()) {
                            Libgit2Helper.revertFilesToIndexVersion(repo, pathspecList)
                        }
                        //如果untracked列表不为空，删除文件
                        if(untrakcedFileList.isNotEmpty()) {
                            Libgit2Helper.rmUntrackedFiles(untrakcedFileList)
                        }

                        //操作完成，显示提示
                        // "reverted n and deleted m files"，其中 n 和 m 将替换为恢复和删除的文件数
                        val msg = replaceStringResList(revertStrRes,
                            listOf(pathspecList.size.toString(),untrakcedFileList.size.toString()))
                        //关闭底栏，revert是个独立操作，不会和其他操作组合，所以一定需要关底栏
                        bottomBarActDoneCallback(msg, curRepo)
                    }
                }
        }

    val doUnstage = doUnstage@{ curRepo:RepoEntity, selectedItemList:List<StatusTypeEntrySaver> ->
        loadingText.value = activityContext.getString(R.string.unstaging)

        //menu action: unstage
        Repository.open(curRepo.fullSavePath).use {repo ->
            val refspecList = mutableListOf<String>()
//                准备refspecList
            selectedItemList.forEach {
                //不要用index.removeByPath()，那个是停止追踪(make it untracked)，不是unstage！！！
                refspecList.add(it.relativePathUnderRepo)
            }

            //do unstage
            Libgit2Helper.unStageItems(repo, refspecList)
        }

        //关闭底栏，刷新页面
        bottomBarActDoneCallback(activityContext.getString(R.string.unstage_success), curRepo)
    }
    val showUnstageConfirmDialog = rememberSaveable { mutableStateOf(false)}
    if(showUnstageConfirmDialog.value) {
        ConfirmDialog(
            title = stringResource(R.string.unstage),
            text = stringResource(R.string.will_unstage_are_u_sure),
            onCancel = { showUnstageConfirmDialog.value = false }
        ) {
            showUnstageConfirmDialog.value = false
            val curRepo = curRepoFromParentPage.value
            doJobThenOffLoading(loadingOn, loadingOff, loadingText=activityContext.getString(R.string.unstaging)) {
                doUnstage(curRepo, selectedItemList.value.toList())
            }
        }
    }



    val credentialList = mutableCustomStateListOf(stateKeyTag, "credentialList", listOf<CredentialEntity>())
    val selectedCredentialIdx = rememberSaveable{mutableIntStateOf(0)}

//    val fullPathForImport = StateUtil.getRememberSaveableState("")
    val importList = mutableCustomStateListOf(stateKeyTag, "importList", listOf<StatusTypeEntrySaver>())
    val showImportToReposDialog = rememberSaveable { mutableStateOf(false)}
    if(showImportToReposDialog.value){
        ConfirmDialog2(
            title = activityContext.getString(R.string.import_as_repo),
            requireShowTextCompose = true,
            textCompose = {
                ScrollableColumn {
                    Text(stringResource(R.string.will_try_import_selected_dirs_as_repos))
                    Spacer(Modifier.height(15.dp))

//                    Text(stringResource(R.string.will_import_selected_submodules_to_repos))
                    CredentialSelector(credentialList.value.toList(), selectedCredentialIdx)

                    Spacer(Modifier.height(10.dp))
                    Text(stringResource(R.string.import_repos_link_credential_note), fontWeight = FontWeight.Light)
                }
            },
            onCancel = { showImportToReposDialog.value = false },

        ) {
            showImportToReposDialog.value = false

            val curRepo = curRepoFromParentPage.value
            doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.importing)) {
                val repoNameSuffix = "_of_${curRepo.repoName}"
                val parentRepoId = curRepo.id
//                val importList = selectedItemList.value.toList().filter { it.cloned }

                val credentialList = credentialList.value.toList()
                val selectedCredentialId = credentialList[selectedCredentialIdx.intValue].id

                val repoDb = AppModel.dbContainer.repoRepository
                val importRepoResult = ImportRepoResult()

                try {
                    importList.value.forEach {
                        val result = repoDb.importRepos(dir=it.canonicalPath, isReposParent=false, repoNameSuffix = repoNameSuffix, parentRepoId = parentRepoId, credentialId = selectedCredentialId)
                        importRepoResult.all += result.all
                        importRepoResult.success += result.success
                        importRepoResult.failed += result.failed
                        importRepoResult.existed += result.existed
                    }

                    Msg.requireShowLongDuration(replaceStringResList(activityContext.getString(R.string.n_imported), listOf(""+importRepoResult.success)))
                }catch (e:Exception) {
                    //出错的时候，importRepoResult的计数不一定准，有可能比实际成功和失败的少，不过不可能多
                    val errMsg = e.localizedMessage
                    Msg.requireShowLongDuration(errMsg ?: "import err")
                    createAndInsertError(curRepo.id, "import repo(s) err: $errMsg")
                    MyLog.e(TAG, "import repo(s) from ChangeList err: importRepoResult=$importRepoResult, err="+e.stackTraceToString())
                }finally {
                    //require refresh repo list for go to submodule after import
                    // since only worktree(ChangeList) can go to sub, so only need pass this param to ChangeList page, index and treeToTree page no need yet
                    if(needReQueryRepoList != null) {
                        changeListRequireRefreshFromParentPage(curRepo)
                    }

                    // refresh ChangeList page is unnecessary yet
//                    changeStateTriggerRefreshPage(needRefreshChangeListPage)
                }
            }

        }
    }


    /*
    if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(stringResource(R.string.revert), stringResource(R.string.stage),)
                            else if(fromTo == Cons.gitDiffFromHeadToIndex) listOf(stringResource(R.string.unstage))
                            else listOf()  //这个应该不会执行到
     */

    val initImportAsRepo = {
        val selectedItemList = selectedItemList.value.toList()
        val tmplist = selectedItemList.filter { it.toFile().isDirectory }
        if(tmplist.isEmpty()) {
            Msg.requireShow(activityContext.getString(R.string.no_dir_selected))
        }else {
            importList.value.clear()
            importList.value.addAll(tmplist)
            showImportToReposDialog.value=true
        }
    }

    val moreItemOnClickList:List<()->Unit> = (if(fromTo == Cons.gitDiffFromIndexToWorktree) listOf(
        acceptOurs@{
            if(!UserUtil.isPro()) {
                Msg.requireShowLongDuration(activityContext.getString(R.string.this_feature_is_pro_only))
                return@acceptOurs
            }

            mergeAcceptTheirs.value=false
            showMergeAcceptTheirsOrOursDialog.value=true
        },
        acceptTheirs@{
            if(!UserUtil.isPro()) {
                Msg.requireShowLongDuration(activityContext.getString(R.string.this_feature_is_pro_only))
                return@acceptTheirs
            }

            mergeAcceptTheirs.value=true
            showMergeAcceptTheirsOrOursDialog.value=true
        },
        // menu action: Stage
        stage@{
            doJobThenOffLoading(
                loadingOn = loadingOn,
                loadingOff = loadingOff,
                loadingText=activityContext.getString(R.string.staging)
            ) {
                val curRepo = curRepoFromParentPage.value
                ChangeListFunctions.doStage(
                    curRepo = curRepo,
                    requireCloseBottomBar = true,
                    userParamList = false,
                    paramList = null,

                    fromTo = fromTo,
                    selectedListIsEmpty = selectedListIsEmpty,
                    requireShowToast = requireShowToast,
                    noItemSelectedStrRes = noItemSelectedStrRes,
                    activityContext = activityContext,
                    selectedItemList = selectedItemList.value.toList(),
                    loadingText = loadingText,
                    nFilesStagedStrRes = nFilesStagedStrRes,
                    bottomBarActDoneCallback = bottomBarActDoneCallback
                )
            }

            Unit
        },
        //menu action: Revert
        revert@{
            //显示弹窗，确认后才会执行操作
            showRevertAlert.value = true
        },
        createPatch@{
            showCreatePatchDialog.value = true
        },
        ignore@{
            showIgnoreDialog.value = true
        },
        importAsRepo@{
            initImportAsRepo()
        }
    ) else if(fromTo == Cons.gitDiffFromHeadToIndex) listOf(
        unstage@{
            showUnstageConfirmDialog.value = true
        },
        createPatch@{
            showCreatePatchDialog.value = true
        },
        importAsRepo@{
            initImportAsRepo()
        }
    ) else listOf()  // fromTo == Cons.gitDiffFromTreeToTree

    ).asReversed()

    val switchItemSelected = { item:StatusTypeEntrySaver ->
        if(isFileSelectionMode.value.not()) {
            //清空选中文件列表
            selectedItemList.value.clear()
            //开启选择模式
            isFileSelectionMode.value = true
        }

        //如果元素不在已选择条目列表则添加
        filesPageAddFileToSelectedListIfAbsentElseRemove(item)
    }

    val getSelectedFilesCount = {
        selectedItemList.value.size
    }

    val isItemInSelected:(StatusTypeEntrySaver)->Boolean = { item:StatusTypeEntrySaver ->
//        val fpath = item.canonicalPath
//        val fileMap = selectedItemList.value
//
//        fileMap.contains(fpath)

        selectedItemList.value.contains(item)
    }



    val showOpenAsDialog = rememberSaveable { mutableStateOf(false)}
    val readOnlyForOpenAsDialog = rememberSaveable { mutableStateOf(false)}
    val openAsDialogFilePath = rememberSaveable { mutableStateOf("")}
    val openAsDialogFileName = rememberSaveable { mutableStateOf("")}
//    val showOpenInEditor = rememberSaveable { mutableStateOf(false)}

    if(showOpenAsDialog.value) {
        OpenAsDialog(readOnly = readOnlyForOpenAsDialog,fileName = openAsDialogFileName.value, filePath = openAsDialogFilePath.value) {
            //onClose
            showOpenAsDialog.value=false
        }
    }




    val goToSub = { item:StatusTypeEntrySaver ->
        val target = changeListRepoList?.value?.find { item.toFile().canonicalPath == it.fullSavePath }
        if(target==null) {
            Msg.requireShow(activityContext.getString(R.string.dir_not_imported))
        }else {
            goToChangeListPage(target)
        }

        Unit
    }

    val menuKeyTextList = listOf(
        stringResource(R.string.open),
        stringResource(R.string.open_as),
        stringResource(R.string.show_in_files),
        stringResource(R.string.file_history),
        stringResource(R.string.copy_path),
        stringResource(R.string.copy_full_path),
        stringResource(R.string.import_as_repo),
        stringResource(R.string.go_sub),
    )

    val menuKeyActList = listOf(
        open@{ item:StatusTypeEntrySaver ->
            if(!item.toFile().exists()) {
                requireShowToast(activityContext.getString(R.string.file_doesnt_exist))
                return@open
            }

            naviTarget.value = Cons.ChangeListNaviTarget_NoNeedReload

            //open file，在子页面打开，不要跳转到主页的editor页面
            openFileWithInnerEditor(item.canonicalPath, item.changeType == Cons.gitStatusConflict)

        },
        openAs@{item:StatusTypeEntrySaver ->
            if(!item.toFile().exists()) {
                requireShowToast(activityContext.getString(R.string.file_doesnt_exist))
                return@openAs
            }

//            readOnlyForOpenAsDialog.value = FsUtils.isReadOnlyDir(item.canonicalPath)
            openAsDialogFilePath.value = item.canonicalPath
            openAsDialogFileName.value=item.fileName
            showOpenAsDialog.value=true
        },
        showInFiles@{item:StatusTypeEntrySaver ->
            if(!item.toFile().exists()) {
                requireShowToast(activityContext.getString(R.string.file_doesnt_exist))
                return@showInFiles
            }

            goToFilesPage(item.canonicalPath)
        },
        fileHistory@{item:StatusTypeEntrySaver ->
            naviToFileHistoryByRelativePath(repoId, item.relativePathUnderRepo)
        },
        copyPath@{item:StatusTypeEntrySaver ->
            clipboardManager.setText(AnnotatedString(item.relativePathUnderRepo))
            Msg.requireShow(activityContext.getString(R.string.copied))
        },
        copyRealPath@{item:StatusTypeEntrySaver ->
            clipboardManager.setText(AnnotatedString(item.canonicalPath))
            Msg.requireShow(activityContext.getString(R.string.copied))
        },
        importAsRepo@{
            importList.value.clear()
            importList.value.add(it)
            showImportToReposDialog.value = true
        },
        goToSub@{
            goToSub(it)
        }
    )
    val menuKeyEnableList:List<(StatusTypeEntrySaver)->Boolean> = listOf(
        openEnabled@{true },  //改成点击后动态检测文件是否存在，要不然在index页面，即使文件状态为删除，但worktree实际可能存在文件，这时文件应该仍可编辑，但却点不了按钮
        openAsEnabled@{true },

        //只有worktree的cl页面支持在Files页面显示文件，index页面由于是二级页面，跳转不了，干脆禁用了
        showInFilesEnabled@{fromTo == Cons.gitDiffFromIndexToWorktree},  //对所有条目都启用showInFiles，不过会在点击后检查文件是否存在，若不存在不会跳转
        fileHistoryEnabled@{it.maybeIsFileAndExist()},
        copyPath@{true},
        copyRealPath@{true},
        importAsRepo@{ (fromTo == Cons.gitDiffFromIndexToWorktree || fromTo == Cons.gitDiffFromHeadToIndex) && it.toFile().isDirectory }, //only dir can be import as repo
        goToSub@{fromTo == Cons.gitDiffFromIndexToWorktree && it.toFile().isDirectory},  // only dir maybe import as cur repo's sub repo, then maybe can go to sub
    )

    //这个页面，显示就是启用，禁用就不需要显示，所以直接把enableList作为visibleList即可
    val menuKeyVisibleList:List<(StatusTypeEntrySaver)->Boolean> = menuKeyEnableList

    val hasError = rememberSaveable { mutableStateOf(false)}
    val errMsg = rememberSaveable { mutableStateOf("")}
    val setErrMsg = {msg:String ->
        errMsg.value = msg
        hasError.value = true
    }
    val clearErrMsg = {
        hasError.value=false
        errMsg.value="";
    }


//    if (needRefreshChangeListPage.value) {
//        initChangeListPage()
//        needRefreshChangeListPage.value = false
//    }

    //back handler block start
    //如果是从主页创建的此组件，按返回键应双击退出，需要注册个BackHandler，作为2级页面时则不用
    //换句话说：ChangeList页面需要注册双击返回；Index页面不需要
    val isBackHandlerEnable = rememberSaveable { mutableStateOf(true)}
    val backHandlerOnBack = getBackHandler(
        activityContext,
        exitApp,
        isFileSelectionMode,
        quitSelectionMode,
        fromTo,
        naviUp,
        changeListPageFilterModeOn,
        openDrawer

    )
    //注册BackHandler，拦截返回键，实现双击返回和返回上级目录
    BackHandler(enabled = isBackHandlerEnable.value, onBack = {backHandlerOnBack()})
    //back handler block end

    val showSelectedItemsShortDetailsDialog = rememberSaveable { mutableStateOf(false)}
//    val selectedItemsShortDetailsStr = rememberSaveable { mutableStateOf("")}
    if(showSelectedItemsShortDetailsDialog.value) {
        SelectedItemDialog(
//            detailStr = selectedItemsShortDetailsStr.value,
            selectedItems = selectedItemList.value,
            formatter = {"${it.fileName}, ${it.relativePathUnderRepo.removeSuffix(it.fileName)}"},
            switchItemSelected = switchItemSelected,
            clearAll = {selectedItemList.value.clear()},
            closeDialog = {showSelectedItemsShortDetailsDialog.value = false}
        )
    }

    val countNumOnClickForBottomBar = {
//        val sb = StringBuilder()
//        selectedItemList.value.forEach {
//            sb.appendLine("${it.fileName}, ${it.relativePathUnderRepo.removeSuffix(it.fileName)}").appendLine()
//        }
//        selectedItemsShortDetailsStr.value = sb.removeSuffix("\n").toString()
        showSelectedItemsShortDetailsDialog.value = true
    }

    if(showRevertAlert.value) {
        ConfirmDialog(
            title=stringResource(R.string.revert),
            text=stringResource(R.string.will_revert_modified_or_deleted_and_rm_new_files_are_you_sure),
            okTextColor = MyStyleKt.TextColor.danger(),
            onCancel = {showRevertAlert.value=false}
        ) {  //onOk
            showRevertAlert.value=false
            val curRepo = curRepoFromParentPage.value

            doJobThenOffLoading(
                loadingOn = loadingOn,
                loadingOff = loadingOff,
                loadingText = activityContext.getString(R.string.reverting)
            ) {
                doRevert(curRepo, selectedItemList.value.toList())
            }
        }
    }

    val iconModifier = MyStyleKt.Icon.modifier


    if(isLoading.value) {
        //LoadingText默认开启滚动，所以无需处理(ps 滚动是为了显隐顶栏
        LoadingText(text = loadingText.value, contentPadding = contentPadding)
    }else {
        if(hasError.value){  //有错误则显示错误（例如无仓库、无效树id都会导致出错）
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(bottom = 80.dp)
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState())

                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                MySelectionContainer {
                    Text(errMsg.value, color = MyStyleKt.TextColor.error())
                }
            }
        }else {  //有仓库，但条目列表为空，可能没修改的东西，这时显示仓库是否clean是否和远程同步等信息
            // onUi是为了和在callback里区分，callback函数应该通过状态变量获取最新值
            val curRepoOnUi = curRepoFromParentPage.value
            val curRepoUpstreamOnUi = curRepoUpstream.value

            if(itemList.value.isEmpty()) {  //列表为空
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
//                        .padding(bottom = 80.dp)  //不要在这加padding，如果想加，应在底部加个padding row
                        .verticalScroll(rememberScrollState())
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    if(fromTo == Cons.gitDiffFromIndexToWorktree) {  //worktree
                        Text(text = stringResource(id = R.string.work_tree_clean))
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp)
                            ,
                        ) {
                            if (changeListPageHasIndexItem.value){  //index不为空
                                ClickableText(
                                    text =  stringResource(R.string.index_dirty),
                                    modifier = MyStyleKt.ClickableText.modifierNoPadding
                                        .clickable {  //导航到Index页面
                                            navController.navigate(Cons.nav_IndexScreen)
                                        }
                                    ,
                                )
                            } else{  //index为空
                                Text(text =  stringResource(R.string.index_clean))
                            }
                        }

                        //如果仓库状态不是detached HEAD，检查是否和上游同步
                        if (!dbIntToBool(curRepoOnUi.isDetached)) {
                            var upstreamNotSet = false

                            val fontSizeOfPullPushSync = 16.sp

                            val splitSign = " | "
                            val splitHorizonPadding = 10.dp

                            Column(
                                modifier = Modifier
                                    .padding(top = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                //领先或落后或上游无效，显示 "click here to sync" (ps: 上游无效也显示是因为worktree页面的sync可创建上游)
                                if (curRepoOnUi.ahead != 0 || curRepoOnUi.behind != 0
                                    //上游为空，显示sync
                                    || curRepoUpstreamOnUi.branchRefsHeadsFullRefSpec.isBlank()
                                    //上游不为空，但未发布，显示sync
                                    || (curRepoUpstreamOnUi.branchRefsHeadsFullRefSpec.isNotBlank() && !curRepoUpstreamOnUi.isPublished)
                                ) {
                                    //查询下仓库是否领先或落后于上游
                                    if (curRepoOnUi.ahead != 0) {
                                        Row {
                                            Text(text = stringResource(R.string.local_ahead) + ": " + curRepoOnUi.ahead)

                                        }
                                    }
                                    if (curRepoOnUi.behind != 0) {
                                        Row {  //换行
                                            Text(text = stringResource(R.string.local_behind) + ": " + curRepoOnUi.behind)
                                        }
                                    }

                                    //没设置上游
                                    if(curRepoUpstreamOnUi.branchRefsHeadsFullRefSpec.isBlank()) {
                                        upstreamNotSet = true
                                        Row {  //换行
                                            Text(text = stringResource(R.string.no_upstream))
                                        }
                                    }

                                    //设置了上游但没推送到远程
                                    if(curRepoUpstreamOnUi.branchRefsHeadsFullRefSpec.isNotBlank() && !curRepoUpstreamOnUi.isPublished) {
                                        Row {  //换行
                                            Text(text = stringResource(R.string.upstream_not_published))
                                        }
                                    }

                                    if(curRepoOnUi.behind != 0) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            ClickableText(
                                                text = stringResource(R.string.merge),
                                                fontSize = fontSizeOfPullPushSync,
                                                modifier = MyStyleKt.ClickableText.modifierNoPadding.clickable {
                                                    val curRepo = curRepoFromParentPage.value
                                                    doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.merging)) {
                                                        ChangeListFunctions.doMerge(
                                                            requireCloseBottomBar = true,
                                                            upstreamParam = null,
                                                            showMsgIfHasConflicts = true,
                                                            trueMergeFalseRebase = true,
                                                            curRepoFromParentPage = curRepo,
                                                            requireShowToast = requireShowToast,
                                                            appContext = activityContext,
                                                            loadingText = loadingText,
                                                            bottomBarActDoneCallback = bottomBarActDoneCallback
                                                        )
                                                    }
                                                }
                                            )

                                            Text(text = splitSign, modifier = Modifier.padding(horizontal = splitHorizonPadding))

                                            ClickableText(
                                                text = stringResource(R.string.rebase),
                                                fontSize = fontSizeOfPullPushSync,
                                                modifier = MyStyleKt.ClickableText.modifierNoPadding.clickable {
                                                    val curRepo = curRepoFromParentPage.value
                                                    doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.rebasing)) {
                                                        ChangeListFunctions.doMerge(
                                                            requireCloseBottomBar = true,
                                                            upstreamParam = null,
                                                            showMsgIfHasConflicts = true,
                                                            trueMergeFalseRebase = false,
                                                            curRepoFromParentPage = curRepo,
                                                            requireShowToast = requireShowToast,
                                                            appContext = activityContext,
                                                            loadingText = loadingText,
                                                            bottomBarActDoneCallback = bottomBarActDoneCallback
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    }


                                } else {
                                    Text(text = stringResource(id = R.string.already_up_to_date))
                                }


                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    val syncText = stringResource(if(upstreamNotSet) R.string.set_upstream_and_sync else R.string.sync)
//                                    val syncIcon = if(upstreamNotSet) Icons.Filled.CloudSync else Icons.Filled.Sync
//                                    val syncIcon = if(upstreamNotSet) Icons.Filled.CloudSync else ImageVector.vectorResource(R.drawable.two_way_sync)
                                    val syncIcon = ImageVector.vectorResource(R.drawable.two_way_sync)
                                    LongPressAbleIconBtn(
                                        iconModifier = iconModifier,
                                        tooltipText = syncText,
                                        icon = syncIcon ,
                                        iconContentDesc = syncText,

                                        ) {
                                        val curRepo = curRepoFromParentPage.value
                                        doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.syncing)) {
                                            try {
//                                                   //     doSync(true)
                                                ChangeListFunctions.doSync(
                                                    requireCloseBottomBar = true,
                                                    trueMergeFalseRebase = true,
                                                    curRepoFromParentPage = curRepo,
                                                    requireShowToast = requireShowToast,
                                                    appContext = activityContext,
                                                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                                                    plzSetUpStreamForCurBranch = plzSetUpStreamForCurBranch,
                                                    initSetUpstreamDialog = initSetUpstreamDialog,

                                                    loadingText = loadingText,
                                                    dbContainer = dbContainer
                                                )
                                            } catch (e: Exception) {
                                                showErrAndSaveLog(
                                                    TAG,
                                                    "sync error:" + e.stackTraceToString(),
                                                    activityContext.getString(
                                                        R.string.sync_failed
                                                    ) + ":" + e.localizedMessage,
                                                    requireShowToast,
                                                    curRepo.id
                                                )
                                            } finally {
                                                changeListRequireRefreshFromParentPage(curRepo)
                                            }

                                        }

                                    }

                                    //如果设置了上游，显示pull/push
                                    if(!upstreamNotSet) {
                                        LongPressAbleIconBtn(
                                            iconModifier = iconModifier,
                                            tooltipText = stringResource(R.string.push),
                                            icon =  Icons.Filled.Upload,
                                            iconContentDesc = stringResource(id = R.string.push),

                                        ) {
                                            val curRepo = curRepoFromParentPage.value
                                            doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.pushing)) {
                                                try {
//                                                            val success = doPush(true, null)
                                                    val success = ChangeListFunctions.doPush(
                                                        requireCloseBottomBar = true,
                                                        upstreamParam = null,
                                                        force = false,
                                                        curRepoFromParentPage = curRepo,
                                                        requireShowToast = requireShowToast,
                                                        appContext = activityContext,
                                                        loadingText = loadingText,
                                                        bottomBarActDoneCallback = bottomBarActDoneCallback,
                                                        dbContainer = dbContainer
                                                    )
                                                    if (!success) {
                                                        requireShowToast(activityContext.getString(R.string.push_failed))
                                                    } else {
                                                        requireShowToast(activityContext.getString(R.string.push_success))
                                                    }
                                                } catch (e: Exception) {
                                                    showErrAndSaveLog(
                                                        TAG,
                                                        "require push error:" + e.stackTraceToString(),
                                                        activityContext.getString(R.string.push_failed) + ":" + e.localizedMessage,
                                                        requireShowToast,
                                                        curRepo.id
                                                    )
                                                } finally {
                                                    changeListRequireRefreshFromParentPage(curRepo)
                                                }

                                            }
                                        }


                                        LongPressAbleIconBtn(
                                            iconModifier = iconModifier,
                                            tooltipText = stringResource(R.string.pull),
                                            icon =  Icons.Filled.Download,
                                            iconContentDesc = stringResource(id = R.string.pull),

                                            ) {
                                            doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.pulling)) {
                                                val curRepo = curRepoFromParentPage.value

                                                ChangeListFunctions.doPull(
                                                    curRepo = curRepo,
                                                    activityContext = activityContext,
                                                    dbContainer = dbContainer,
                                                    requireShowToast = requireShowToast,
                                                    loadingText = loadingText,
                                                    bottomBarActDoneCallback = bottomBarActDoneCallback,
                                                    changeListRequireRefreshFromParentPage = changeListRequireRefreshFromParentPage,
                                                )
                                            }

                                        }

                                    }

                                }
                            }
                        }

                        //只有非detached HEAD 且 设置了上游（没发布也行） 才显示检查更新
                        if(!dbIntToBool(curRepoOnUi.isDetached) && curRepoUpstreamOnUi.branchRefsHeadsFullRefSpec.isNotBlank()) {
                            Row (
//                                modifier=Modifier.padding(top=18.dp)

                            ){
//                                LongPressAbleIconBtn(
//                                    iconModifier = iconModifier,
//                                    tooltipText = stringResource(id = R.string.check_update),
//                                    icon =  Icons.Filled.Downloading,
//                                    iconContentDesc = stringResource(id = R.string.check_update),
//
//                                ) {}
                                ClickableText(
                                    text = stringResource(id = R.string.check_update),
                                    modifier = MyStyleKt.ClickableText.modifierNoPadding.clickable {
                                        val curRepo = curRepoFromParentPage.value
                                        // fetch
                                        doJobThenOffLoading(
                                            loadingOn,
                                            loadingOff,
                                            activityContext.getString(R.string.fetching)
                                        ) {
                                            try {
//                                                    val fetchSuccess = doFetch(null)
                                                val fetchSuccess = ChangeListFunctions.doFetch(
                                                    remoteNameParam = null,
                                                    curRepoFromParentPage = curRepo,
                                                    requireShowToast = requireShowToast,
                                                    appContext = activityContext,
                                                    loadingText = loadingText,
                                                    dbContainer = dbContainer
                                                )
                                                if (fetchSuccess) {
                                                    requireShowToast(
                                                        activityContext.getString(
                                                            R.string.fetch_success
                                                        )
                                                    )
                                                } else {
                                                    requireShowToast(
                                                        activityContext.getString(
                                                            R.string.fetch_failed
                                                        )
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                showErrAndSaveLog(
                                                    TAG,
                                                    "fetch error:" + e.stackTraceToString(),
                                                    activityContext.getString(
                                                        R.string.fetch_failed
                                                    ) + ":" + e.localizedMessage,
                                                    requireShowToast,
                                                    curRepo.id
                                                )
                                            } finally {
                                                changeListRequireRefreshFromParentPage(curRepo)
                                            }
                                        }

                                    },
                                )
                            }
                        }

                        //如果是pro，显示 文件、分支、提交历史 功能入口
                        if(UserUtil.isPro()) {
                            Row(modifier=Modifier.padding(top=18.dp)
                                ,

                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                if(dev_EnableUnTestedFeature || tagsTestPassed) {
                                    LongPressAbleIconBtn(
                                        enabled = true,
                                        iconModifier = iconModifier,
                                        tooltipText = stringResource(R.string.tags),
                                        icon = Icons.AutoMirrored.Filled.Label,
                                        iconContentDesc = stringResource(R.string.tags),

                                        ) {  //go to tags page
                                        navController.navigate(Cons.nav_TagListScreen + "/" + repoId)
                                    }

                                }

                                LongPressAbleIconBtn(
                                    enabled = true,
                                    iconModifier = iconModifier,
                                    tooltipText = stringResource(R.string.commit_history),
                                    icon =  Icons.Filled.History,
                                    iconContentDesc = stringResource(id = R.string.commit_history),

                                    ) {  // go to commit history (git log) page
                                    val curRepo = curRepoFromParentPage.value

                                    //打开当前仓库的提交记录页面，话说，那个树形怎么搞？可以先不搞树形，以后再弄
                                    Cache.set(Cache.Key.commitList_fullOidKey, "")  //这里不需要传分支名，会通过HEAD解析当前分支
                                    Cache.set(Cache.Key.commitList_shortBranchNameKey, "")  //这里用不到这个值，所以没必要创建那么多key，都用一个关联无效value的key就行了
                                    val useFullOid = "0"
                                    val isHEAD = "1"
                                    //注：如果fullOidKey传null，会变成字符串 "null"，然后查不出东西，返回空字符串，与其在导航组件取值时做处理，不如直接传空字符串，不做处理其实也行，只要“null“作为cache key取不出东西就行，但要是不做处理万一字符串"null"作为cache key能查出东西，就歇菜了，总之，走正常流程取得有效cache key，cache value传空字符串，即可
                                    navController.navigate(Cons.nav_CommitListScreen + "/" + curRepo.id + "/" + useFullOid +"/" +isHEAD)

                                }

                                LongPressAbleIconBtn(
                                    enabled = true,
                                    iconModifier = iconModifier,
                                    tooltipText = stringResource(R.string.branches),
                                    icon = ImageVector.vectorResource(id = R.drawable.branch),
                                    iconContentDesc = stringResource(id = R.string.branches),

                                ) { // go to branches page
                                    navController.navigate(Cons.nav_BranchListScreen + "/" + curRepoFromParentPage.value.id)
                                }


                                LongPressAbleIconBtn(
                                    enabled = true,
                                    iconModifier = iconModifier,
                                    tooltipText = stringResource(R.string.files),
                                    icon =  Icons.Filled.Folder,
                                    iconContentDesc = stringResource(id = R.string.files),

                                    ) { // go to Files page
                                    goToFilesPage(curRepoFromParentPage.value.fullSavePath)
                                }

                            }
                        }

                    }else if(fromTo == Cons.gitDiffFromHeadToIndex) {  //index
                        Text(text = stringResource(id = R.string.index_clean))

                    }else {  //fromTo == Cons.gitDiffFromTreeToTree and other
                        Text(text = stringResource(id = R.string.no_difference_found))
                    }
                }


            }else {  //列表不为空，显示条目
                //根据关键字过滤条目
                val k = changeListPageFilterKeyWord.value.text.lowercase()  //关键字
                val enableFilter = changeListPageFilterModeOn.value && maybeIsGoodKeyword(k)
                val itemListOrFilterList = if(enableFilter){
                    val fl = itemList.value.filter {
                        it.fileName.lowercase().contains(k)
                                || it.relativePathUnderRepo.lowercase().contains(k)
                                || it.getSizeStr().lowercase().contains(k)
                                || it.getChangeTypeAndSuffix(isDiffToLocal).lowercase().contains(k)
                                || it.getItemTypeString().lowercase().contains(k)
                                || it.changeType?.lowercase()?.contains(k) == true
                    }

                    filterList.value.clear()
                    filterList.value.addAll(fl)

                    fl
                }else {
                    itemList.value
                }

                val listState = if(enableFilter) filterListState else itemListState
//                if(enableFilter) {  //更新filter列表state
//                    filterListState.value = listState
//                }

                //更新是否启用filter
                enableFilterState.value = enableFilter

                MyLazyColumn(
                    contentPadding = contentPadding,
                    list = itemListOrFilterList,
                    listState = listState,
                    requireForEachWithIndex = true,
                    requirePaddingAtBottom = true,
                    forEachCb = {}
                ) { index, it:StatusTypeEntrySaver ->
                    ChangeListItem(
                        item = it,
                        //                            selectedItemList = selectedItemList,
                        isFileSelectionMode = isFileSelectionMode,
                        //                            filesPageAddFileToSelectedListIfAbsentElseRemove = filesPageAddFileToSelectedListIfAbsentElseRemove,
                        menuKeyTextList=menuKeyTextList,
                        menuKeyActList=menuKeyActList,
                        menuKeyEnableList=menuKeyEnableList,
                        menuKeyVisibleList=menuKeyVisibleList,
                        fromTo=fromTo,
                        isDiffToLocal = isDiffToLocal,
                        lastClickedItemKey = lastClickedItemKey,
                        switchItemSelected=switchItemSelected,
                        isItemInSelected=isItemInSelected,
                        onLongClick= lc@{
//                            if (fromTo != Cons.gitDiffFromTreeToTree) {  // 比较两个提交树时禁用长按选择条目
//
//                            }
                            //如果是tree to tree页面且底栏功能（cherrypick checkout patch）没测试通过，不启用选择模式，直接返回
                            if(fromTo == Cons.gitDiffFromTreeToTree && !proFeatureEnabled(treeToTreeBottomBarActAtLeastOneTestPassed())) {
                                return@lc
                            }

                            //震动反馈
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                            //非选择模式 长按启用选择模式；选择模式下长按进行区域选择
                            if (!isFileSelectionMode.value) {
                                switchItemSelected(it)
                            }else {
                                //在选择模式下长按条目，执行区域选择（连续选择一个范围）
                                UIHelper.doSelectSpan(index, it,
                                    selectedItemList.value, itemListOrFilterList,
                                    switchItemSelected,
                                    selectItem
                                )
                            }
                        }
                        //                            treeOid1Str = commit1OidStr,
                        //                            treeOid2Str = commit2OidStr
                    ){  //onClick
                        if (isFileSelectionMode.value) {
                            switchItemSelected(it)
                        } else {
                            //x已实现）判断如果点击的是冲突条目不要跳转到diff列表，而是改用二级页面的编辑器打开
                            //添加路径到缓存，然后在diffscreen取出来
                            //                        Cache.Map.set(Cache.Map.Key.diffScreen_UnderRepoPath, item.relativePathUnderRepo)
                            if(it.changeType == Cons.gitStatusConflict) {  //如果是冲突条目，直接用编辑器打开（冲突条目没法预览diff）
                                naviTarget.value = Cons.ChangeListNaviTarget_NoNeedReload

                                val initMergeMode = true  //因为changeType == conflict，所以这里直接传true即可
                                openFileWithInnerEditor(it.canonicalPath, initMergeMode)
                            }
                                // on worktree page, if new file(untracked), open with editor instead view difference
//                            else if(isWorktreePage && it.changeType == Cons.gitStatusNew){
//                                val initMergeMode = false
//                                openFileWithInnerEditor(it.canonicalPath, initMergeMode)
//                            }
                            else {  //非冲突条目，预览diff

                                //用来实现在diff页面stage条目后将其从cl页面移除。ps 由于filterList是由itemList产生的，所以，这里这里直接操作itemList才对，否则，无法显示最新的修改
//                                SharedState.homeChangeList_itemList = if(enableFilter) filterList.value else itemList.value
                                SharedState.homeChangeList_itemList = itemList.value
                                SharedState.homeChangeList_indexHasItem = changeListPageHasIndexItem

                                //准备导航到diff页面的参数
                                Cache.set(Cache.Key.diffScreen_underRepoPathKey, it.relativePathUnderRepo)
//                                val diffableList = itemList.filter {item -> item.changeType == Cons.gitStatusModified || item.changeType == Cons.gitStatusNew || item.changeType == Cons.gitStatusDeleted}
                                var indexAtDiffableList = -1

                                val diffableList = mutableListOf<StatusTypeEntrySaver>()
                                // if filter mode on, this item list is filter list, logically, no changed required
                                // 如果开启过滤模式，这个列表会是过滤后的列表，符合逻辑，不用修改
                                val itemCopy = itemListOrFilterList.toList()
                                for(idx in itemCopy.indices) {
                                    val item = itemCopy[idx]
                                    //非冲突类型的条目一律添加，包括submodules
                                    if(item.changeType != Cons.gitStatusConflict) {
                                        diffableList.add(item)

                                        if(item == it) {
                                            indexAtDiffableList = diffableList.lastIndex
                                        }
                                    }
                                }

                                Cache.set(Cache.Key.diffScreen_diffableItemListKey, diffableList)

                                naviTarget.value = Cons.ChangeListNaviTarget_NoNeedReload

                                //导航到diffScreen
                                navController.navigate(
                                    Cons.nav_DiffScreen +
                                            "/" + it.repoIdFromDb +
                                            //    "/" + encodeStrUri(item.relativePathUnderRepo) +
                                            "/" + fromTo +
                                            "/" + it.changeType +
                                            "/" + it.fileSizeInBytes +
                                            "/" + (if(swap) commit2OidStr else commit1OidStr) +
                                            "/" + (if(swap) commit1OidStr else commit2OidStr) +
                                            "/" + (if(it.itemType==Cons.gitItemTypeSubmodule) 1 else 0) +
                                            "/" + (if(isDiffToLocal) 1 else 0)
                                            + "/" + indexAtDiffableList
                                            +"/" + (if(localAtDiffRight.value) 1 else 0)

                                            +"/" + (if(fromTo == Cons.gitDiffFromIndexToWorktree) DiffFromScreen.HOME_CHANGELIST.code
                                            else if(fromTo == Cons.gitDiffFromHeadToIndex) DiffFromScreen.INDEX.code
                                            else DiffFromScreen.TREE_TO_TREE.code)
                                )

                            }
                        }
                    }
                    HorizontalDivider()

                }

                //放到这里只有存在条目时才显示BottomBar，要不要把这块代码挪外边？不过要是没条目也没必要显示BottomBar，也算合理。
                //Bottom bar
                if (isFileSelectionMode.value) {
                    BottomBar(
                        quitSelectionMode=quitSelectionMode,
                        iconList=iconList,
                        iconTextList=iconTextList,
                        iconDescTextList=iconTextList,
                        iconOnClickList=iconOnClickList,
                        iconEnableList=iconEnableList,
                        enableMoreIcon=enableMoreIcon,
                        moreItemTextList=moreItemTextList,
                        moreItemOnClickList=moreItemOnClickList,
                        moreItemEnableList = moreItemEnableList,
                        moreItemVisibleList = moreItemVisibleList,
                        iconVisibleList = iconVisibleList,
                        countNumOnClickEnabled = true,
                        getSelectedFilesCount = getSelectedFilesCount,
                        countNumOnClick = countNumOnClickForBottomBar
                    )
                }
            }
        }

    }


//    if(lastRequireRefreshValue.value != refreshRequiredByParentPage.value) {
//        lastRequireRefreshValue.value = refreshRequiredByParentPage.value

    LaunchedEffect(refreshRequiredByParentPage) {
        // do reload
        try {
            if(AppModel.devModeOn) {
                MyLog.d(TAG, "#LaunchedEffect: naviTarget.value=${naviTarget.value}")
            }

            val (requestType, payloadRepoId) = getRequestDataByState<String?>(
                refreshRequiredByParentPage,
                getThenDel = true
            )


            if(AppModel.devModeOn) {
                MyLog.d(TAG, "#LaunchedEffect: requestType=$requestType, payloadRepoId=$payloadRepoId")
            }


            //if navi to Difference page or internal Editor then navi back, actually most time no need reload page
            if(naviTarget.value == Cons.ChangeListNaviTarget_NoNeedReload){
                //不管是否返回，反正消费过naviTarget一次后，就重置为初始值
                // if navi back from Index, need refresh
                naviTarget.value = Cons.ChangeListNaviTarget_InitValue


                //执行到这个代码块一般是从Diff页面或者子页面文本编辑器(SubPageEditor)返回来的，
                // 有可能携带了希望执行的操作，这里检查下(可理解成子页面向父页面传参)
                //检查是否有需要执行的操作
                // 从diff页面返回可能会请求执行此操作，可针对 homeChangeList和indexChangeList执行此操作
                if(requestType == StateRequestType.indexToWorkTree_CommitAll) {
                    Cache.set(Cache.Key.changeListInnerPage_requireDoActFromParent, PageRequest.indexToWorkTree_CommitAll)
                    requireDoActFromParentShowTextWhenDoingAct.value = activityContext.getString(R.string.committing)
                    requireDoActFromParent.value = true

                    enableActionFromParent.value=false  //禁用顶栏按钮避免冲突

                }else if(requestType == StateRequestType.headToIndex_CommitAll) {
                    Cache.set(Cache.Key.changeListInnerPage_requireDoActFromParent, PageRequest.commit)
                    requireDoActFromParentShowTextWhenDoingAct.value = activityContext.getString(R.string.committing)
                    requireDoActFromParent.value = true

                    enableActionFromParent.value=false
                }


                //无需重载的情况下，可直接返回了，上面请求执行的操作直接使用现有数据即可，无需重载
                return@LaunchedEffect
            }


            if(requestType == StateRequestType.withRepoId && payloadRepoId != null) {
                // 请求刷新页面的仓库的repoId
                val curRepo = curRepoFromParentPage.value
                val repoMaybeValid = curRepo.fullSavePath.isNotBlank() && curRepo.id.isNotBlank()
                if(repoMaybeValid && payloadRepoId != curRepo.id) {
                    //请求刷新页面的仓库和当前仓库不一致，可能某个仓库执行任务的过程中切换了仓库，直接返回，不刷新
                    return@LaunchedEffect
                }
            }


            // this assigned should not be necessary,
            // because remember haven't hold a mutableState value,
            // so it just a simple variable, should catchable as constant by lambda
            // on the other hand, assigned is fine though
//            val newPageId = generateRandomString()
            //直接用refresh变量的值即可，省得重新生成了
            val currentPageId = refreshRequiredByParentPage
            //这个最新pageId似乎可省略啊？虽然不省略逻辑上更清晰
            newestPageId.value = currentPageId

            //初始化
            doJobThenOffLoading(loadingOn, loadingOff, activityContext.getString(R.string.loading)) {
                changeListInit(
                    dbContainer = dbContainer,
                    activityContext = activityContext,
//        needRefresh = needRefreshChangeListPage,
//        needRefreshParent = refreshRequiredByParentPage,
                    curRepoUpstream=curRepoUpstream,
                    isFileSelectionMode = isFileSelectionMode,
                    changeListPageNoRepo = changeListPageNoRepo,
                    changeListPageHasIndexItem = changeListPageHasIndexItem,
                    changeListPageHasWorktreeItem = changeListPageHasWorktreeItem,
                    itemList = itemList,
                    requireShowToast=requireShowToast,
                    curRepoFromParentPage = curRepoFromParentPage,
                    selectedItemList = selectedItemList,
                    fromTo = fromTo,
                    repoState=repoState,
                    commit1OidStr = commit1OidStr,
                    commit2OidStr=commit2OidStr,
                    commitParentList=commitParentList,
                    repoId = repoId,
                    setErrMsg=setErrMsg,
                    clearErrMsg=clearErrMsg,
                    loadingOn=loadingOn,
                    loadingOff=loadingOff,
                    hasNoConflictItems=hasNoConflictItems,
                    swap=swap,
                    commitForQueryParents=commitForQueryParents,
                    rebaseCurOfAll=rebaseCurOfAll,
                    credentialList=credentialList,
                    quitSelectionMode = quitSelectionMode,
                    repoChanged = {
                        // 检测原理是currentPageId捕获的是常量(或值拷贝)，state变量每次调用都重新查最新的值，因此若变化，可检测到
                        val repoChanged = currentPageId != newestPageId.value
                        if(repoChanged) {
                            MyLog.d(TAG, "Repo Changed!")
                        }

                        // if true, page changed
                        repoChanged
                    }

//        isDiffToHead=isDiffToHead,
//        headCommitHash=headCommitHash
//        scope
                )

                //下面执行需要等初始化完成后才能执行的操作



            }
        } catch (e: Exception) {
            MyLog.e(TAG, "#LaunchedEffect err: ${e.stackTraceToString()}")
        }
    }
//    }

//    DisposableEffect(Unit) {
//        onDispose {
//            //退出时，把父页面传来的变量重置一下，不然有可能发生editor页面没刷新那样的bug
//            changeListPageHasIndexItem.value = false
//        }
//    }
}



private suspend fun changeListInit(
    dbContainer: AppContainer,
    activityContext: Context,
//    needRefresh:MutableState<String>,
//    needRefreshParent:MutableState<String>,
    curRepoUpstream: CustomStateSaveable<Upstream>,
    isFileSelectionMode: MutableState<Boolean>,
    changeListPageNoRepo: MutableState<Boolean>,
    changeListPageHasIndexItem: MutableState<Boolean>,
    changeListPageHasWorktreeItem: MutableState<Boolean>,
    itemList: CustomStateListSaveable<StatusTypeEntrySaver>,
    requireShowToast:(String)->Unit,
    curRepoFromParentPage: CustomStateSaveable<RepoEntity>,
    selectedItemList: CustomStateListSaveable<StatusTypeEntrySaver>,
    fromTo: String,
    repoState: MutableIntState,
    commit1OidStr:String,  //只有fromTo是tree to tree时才用到这两个tree oid
    commit2OidStr:String,
    commitParentList: CustomStateListSaveable<String>,
    repoId:String,
    setErrMsg:(String)->Unit,
    clearErrMsg:()->Unit,
    loadingOn:(String)->Unit,
    loadingOff:()->Unit,
    hasNoConflictItems: MutableState<Boolean>,
    swap:Boolean,
    commitForQueryParents:String,
    rebaseCurOfAll: MutableState<String>?,
    credentialList: CustomStateListSaveable<CredentialEntity>,
    quitSelectionMode: () -> Unit,
    repoChanged:()->Boolean,
//    isDiffToHead:MutableState<Boolean>?,
//    headCommitHash:MutableState<String>
//    scope:CoroutineScope
){
    val funName = "changeListInit"
    try {
        val tmpCommit1 = commit1OidStr
        val commit1OidStr = if(swap) commit2OidStr else commit1OidStr
        val commit2OidStr = if(swap) tmpCommit1 else commit2OidStr

        //先清空错误信息，后面若有错会设置上
        clearErrMsg()
        //先设置无仓库为假，后面如果查了发现确实没会改成真
        changeListPageNoRepo.value=false
        changeListPageHasIndexItem.value=false
        //设置仓库状态为初始值
        repoState.intValue = StateT.NONE.bit

        //设置仓库为无效仓库（后面会查询然后更新为有效仓库，如果存在有效仓库的话）
        //仓库名之所以保留是为了在标题栏尽可能快的让用户看到切换后的仓库标题；id和fullSavePath是为了确保后面的检测能判断出来这是个无效仓库以执行更新
        //没必要给整个变量重新赋值
//            curRepoFromParentPage.value = RepoEntity(id = "", fullSavePath = "", repoName = curRepoFromParentPage.value.repoName)

        //不能清id或fullSavePath，会导致误判仓库无效，然后重新查一个
//            tmpCurRepo.id=""
//            tmpCurRepo.fullSavePath=""
        //重置下信息，后面会重新从db查询
        val tmpCurRepo = curRepoFromParentPage.value
        tmpCurRepo.lastCommitHash=""
        tmpCurRepo.branch=""
        tmpCurRepo.upstreamBranch=""


        //先清空列表
        //废弃，因为恢复的有可能不是最新状态：这里实现恢复的逻辑，如果列表不为空，就直接恢复数据，不清空列表，也不重新查询，如果刷新，加个flag，强制重新查询
        itemList.value.clear()  //这里必须清下这个列表，不然切换仓库后可能有残留，可能在此函数外修改了此列表？懒得排查了直接清下，后续查了会更新
        credentialList.value.clear()  //这个也在添加之前清了，避免出“残留”的问题，这里也清下

        //这个列表不用全清，最后会更新，若在这清，可能会出现刷新后归0，卡一下，然后出现数字的bug，因为清空和重新添加之间可能会有一段时间间隔
//            selectedItemList.value.clear()


        if(fromTo == Cons.gitDiffFromTreeToTree) {
            val repoDb = dbContainer.repoRepository
            val repoFromDb = repoDb.getById(repoId)
            if(repoFromDb==null) {
                MyLog.w(TAG, "#$funName, tree to tree diff, query repo err!")
                changeListPageNoRepo.value=true
                setErrMsg(activityContext.getString(R.string.err_when_querying_repo_info))
                return
            }

            curRepoFromParentPage.value = repoFromDb
            //避免后续从状态变量获取值导致仓库不一致
            val curRepoFromParentPage = curRepoFromParentPage.value

            //想启动个定时检查仓库临时状态是否已清空的协程，意义不大，问题很多，后来放弃了
//                if(repoFromDb.tmpStatus.isNotBlank()) {
//                    scope.launch {
//                        while (true) {
//                            if(curRepoFromParentPage.value.id != repoId) {
//                                //换仓库了，循环终止
//                                MyLog.d(TAG, "repo changed, stop query status")
//                                break
//                            }
//
//                            val status = RepoStatusUtil.getRepoStatus(repoId)
//                            //状态清空了，且仓库没变，则更新仓库状态
//                            if(status.isBlank() && curRepoFromParentPage.value.id == repoId) {
//                                val repoWithNewStatus = repoDb.getById(repoId)
//                                if(repoWithNewStatus!=null) {
//                                    curRepoFromParentPage.value = repoWithNewStatus
//                                }
//                                break
//                            }
//                            delay(1000)
//                        }
//                    }
//                }

            Repository.open(repoFromDb.fullSavePath).use { repo ->
                //查询head然后检查是否diff to head
//                    val headCommitRet = Libgit2Helper.getHeadCommit(repo)
//                    if(headCommitRet.success()) {
//                        val headCommitId = headCommitRet.data?.id()?.toString()
//                        if(headCommitId!=null && isDiffToHead!=null) {
//                            headCommitHash.value = headCommitId
//                            isDiffToHead.value = commit1OidStr == headCommitId || commit2OidStr == headCommitId
//                        }
//                    }


                //如果1或2是worktree ( local )，则用 treeToWorkTree 函数
                if(Libgit2Helper.CommitUtil.isLocalCommitHash(commit1OidStr)
                    || Libgit2Helper.CommitUtil.isLocalCommitHash(commit2OidStr)
                ) {  // tree to worktree
                    //如果1是 "local" 则需要反转比较的两个提交对象
                    val reverse = Libgit2Helper.CommitUtil.isLocalCommitHash(commit1OidStr)

                    val cl = if(reverse) {  //commit1 是local，解析commit2为tree2，把tree2作为参数1然后传reverse为true，这时就可比较 workTreeToTree 了
                        val tree2 = Libgit2Helper.resolveTree(repo, commit2OidStr)
                        if(tree2==null) {
                            MyLog.w(TAG, "#$funName, tree to tree diff, query tree2 err!")
                            setErrMsg(activityContext.getString(R.string.error_invalid_commit_hash)+", 3")
                            return
                        }
                        Libgit2Helper.getTreeToTreeChangeList(repo, repoId, tree2, null, reverse=true, treeToWorkTree = true)
                    }else {  //commit2 是local，解析commit1为tree1，tree2传null，反转传false
                        val tree1 = Libgit2Helper.resolveTree(repo, commit1OidStr)
                        if(tree1==null) {
                            MyLog.w(TAG, "#$funName, tree to tree diff, query tree1 err!")
                            setErrMsg(activityContext.getString(R.string.error_invalid_commit_hash)+", 4")
                            return
                        }
                        Libgit2Helper.getTreeToTreeChangeList(repo, repoId, tree1, null, reverse=false, treeToWorkTree = true)
                    }

                    if(repoChanged()) {
                        return
                    }

                    itemList.value.clear()
                    itemList.value.addAll(cl)

                    //和local比较不需要parents list
                    commitParentList.value.clear()
                }else {  // tree to tree，两个tree都不是local(worktree)
                    val tree1 = Libgit2Helper.resolveTree(repo, commit1OidStr)
                    if(tree1==null) {
                        MyLog.w(TAG, "#$funName, tree to tree diff, query tree1 err!")
                        setErrMsg(activityContext.getString(R.string.error_invalid_commit_hash)+", 1")
                        return
                    }
                    val tree2 = Libgit2Helper.resolveTree(repo, commit2OidStr)
                    if(tree2==null) {
                        MyLog.w(TAG, "#$funName, tree to tree diff, query tree2 err!")
                        setErrMsg(activityContext.getString(R.string.error_invalid_commit_hash)+", 2")
                        return
                    }
                    val treeToTreeChangeList = Libgit2Helper.getTreeToTreeChangeList(repo, repoId, tree1, tree2);

                    if(repoChanged()) {
                        return
                    }

                    itemList.value.clear()
                    itemList.value.addAll(treeToTreeChangeList)

                    //只有和parents比较时才需要查询parents（目前20240807只通过点击commit item触发）；其他情况（手动输入两个commit、长按commit条目出现的diff to local）都不需要查询commit，这时把commitForQueryParents直接传空字符串就行
                    if(Libgit2Helper.CommitUtil.mayGoodCommitHash(commitForQueryParents)) {
                        //get commit parents list
                        val parentList = Libgit2Helper.getCommitParentsOidStrList(repo, commitForQueryParents)
//                    if(debugModeOn) {
//                        println("parentList="+parentList)
//                    }

                        if(repoChanged()) {
                            return
                        }

                        commitParentList.value.clear()
                        commitParentList.value.addAll(parentList)
                    }
                }



            }
        }else {  // IndexToWorkTree or HeadToIndex
            //再查询数据
            //从changelist相关设置项读取上次在这个页面最后使用的仓库
//                val settingsRepository = dbContainer.settingsRepository
            //除非给getOrInsertByUsedFor()传的参数有误，否则这里绝对不会为null，所以永远不会在这return
//                val settings = settingsRepository.getOrInsertByUsedFor(Cons.dbSettingsUsedForChangeList)
//                    ?: return@launch
            //这个?:不是三元表达式，这里的作用是如果?:左边的内容为null，返回其右边的内容
//                val changeListSettings = MoshiUtil.changeListSettingsJsonAdapter.fromJson(settings.jsonVal) ?: ChangeListSettings()
            val changeListSettings = SettingsUtil.getSettingsSnapshot().changeList
            val lastUsedRepoId = changeListSettings.lastUsedRepoId
            var needQueryRepoFromDb = false

            //如果当前仓库无效，则检查是否需要查询(20240404:修改逻辑，第一次进入这个页面必查(如果仓库状态就绪且路径有效，就查下当前仓库信息，相当于更新下页面的仓库变量；否则重选个仓库)，要不然在其他页面更新了仓库信息，这页面显示的还是旧信息！)
            if(!isRepoReadyAndPathExist(curRepoFromParentPage.value)) {
                //如果有上次使用的仓库id，根据id查询，如果无，设置flag，之后会从数据库查询一个ready的仓库
                if (lastUsedRepoId.isBlank()) {  //没有之前使用的repoid
                    needQueryRepoFromDb = true  //没有上次使用的仓库信息，从数据库挑一个能用的
                } else {  //有之前使用的repoid，查询一下仓库信息，但不一定能查出来
                    //如果设置项记录的id无效（比如repo被删除），则无法查询出有效的仓库
                    val repoFromDb = dbContainer.repoRepository.getById(lastUsedRepoId)
                    //检查仓库是否为null，是否就绪且路径有效
                    if (repoFromDb == null || !isRepoReadyAndPathExist(repoFromDb)) {  //进入这个分支可能是之前changelist页面使用的仓库被删除了，所以id无效了
                        needQueryRepoFromDb = true
                    } else {  //正常来说，如果是非第一次打开这个页面，并且之前选过有效仓库，会进入到这里查询出上次使用的仓库
                        curRepoFromParentPage.value = repoFromDb  //执行到这，repoFromDb必然非null，否则会在上面的isRepoReadyAndPathExist()判断返回假而进入上面的if语句
                    }
                }
            }else { //如果上次使用仓库状态ok，路径存在，从查一下，更新下它的信息，以免有误，例如我在其他页面修改了仓库信息，但这个页面没销毁，也不知道，那就会依然显示旧信息
                val repoFromDb = dbContainer.repoRepository.getById(curRepoFromParentPage.value.id)
                if(repoFromDb == null || !isRepoReadyAndPathExist(repoFromDb)) {  //等于null，需要重查一个就绪的仓库，不过一般进入到这查出的仓库就不会等于null，这个判断只是以防万一
                    needQueryRepoFromDb = true
                }else {  //在其他页面更新过仓库信息后，会进入到这里，例如在分支页面修改了仓库分支，或者在changelist页面标题栏切换了仓库
                    curRepoFromParentPage.value = repoFromDb  //更新下仓库信息。(执行到这，repoFromDb必然非null，否则会在上面的isRepoReadyAndPathExist()判断返回假而进入上面的if语句)
                }
            }

            if(repoChanged()) {
                return
            }

            //如果上面的id查出的仓库无效，从数据库重新获取一个就绪且路径存在的仓库
            if (needQueryRepoFromDb) {  //没有效仓库，从数据库随便挑一个能用的
                val repoFromDb = dbContainer.repoRepository.getAReadyRepo()

                if(repoChanged()) {
                    return
                }

                if (repoFromDb == null) {  //没就绪的仓库，直接结束
                    changeListPageNoRepo.value = true
                    setErrMsg(activityContext.getString(R.string.no_repo_for_shown))
                    return
                } else {
                    //更新页面state
                    curRepoFromParentPage.value = repoFromDb
                }
            }


            if(repoChanged()) {
                return
            }

            val curRepoFromParentPage = curRepoFromParentPage.value

            //检测是否查询出了有效仓库
            if (!isRepoReadyAndPathExist(curRepoFromParentPage)) {
                //            setErrMsgForTriggerNotify(hasErr, errMsg, queryRepoForChangeListErrStrRes)
                //如果执行到这，还没查询到仓库也没出任何错误，那很可能是因为数据库里根本没有仓库
                changeListPageNoRepo.value=true
                setErrMsg(activityContext.getString(R.string.no_repo_for_shown))
                return
            }

            if(repoChanged()) {
                return
            }

            //通过上面的检测，执行到这里，一定查询到了有效的仓库且赋值给了 curRepoFromParentPage
            //如果选择的仓库改变了，则更新最后选择的仓库并保存到数据库
            if(changeListSettings.lastUsedRepoId != curRepoFromParentPage.id) {
                //更新changeListSettings，下次就不用查询repo表了(若仓库状态后续变成无效或者仓库被删除，其实还是需要查表)
                changeListSettings.lastUsedRepoId = curRepoFromParentPage.id
//                    settings.jsonVal=MoshiUtil.changeListSettingsJsonAdapter.toJson(changeListSettings)
//                    settingsRepository.update(settings)
                val settingsWillSave = SettingsUtil.getSettingsSnapshot()
                settingsWillSave.changeList = changeListSettings
                //更新配置文件
                SettingsUtil.updateSettings(settingsWillSave)
            }



            //查凭据列表(20250123：忘了为什么要在这查凭据列表了)
            val credentialListFromDb = AppModel.dbContainer.credentialRepository.getAll(includeNone = true, includeMatchByDomain = true)

            if(repoChanged()) {
                return
            }

            credentialList.value.clear()
            credentialList.value.addAll(credentialListFromDb)
            //查凭据列表结束



//            MyLog.d(TAG, "ChangeListInnerPage#Init: queryed Repo id:"+curRepoFromParentPage.value.id)

            Repository.open(curRepoFromParentPage.fullSavePath).use { gitRepository ->
                //废弃，新的逻辑是把冲突条目列在其他条目上面就行了：确认实现这的逻辑： 如果有冲突，会提示先去解决冲突
//                changeListPageHasConflictItem.value = Libgit2Helper.hasConflictItemInRepo(gitRepository)
//                //如果有冲突，后面就先不用检测了，优先解决冲突
//                if (changeListPageHasConflictItem.value) {
//                    return@launch
//                }

                //更新下仓库状态的状态变量，这个最快，所以先更新它
                //这个值应该不会是null，除非libgit2添加了新状态，git24j没跟着添加
                repoState.intValue = gitRepository.state()?.bit?: Cons.gitRepoStateInvalid
                //如果状态是rebase，更新计数，仅在worktree页面和index页面需要查询此值，tree to tree不需要
                //TODO 日后如果实现multi commits cherrypick，也需要添加一个cherrypick计数的变量。(另外，merge因为总是只有一个合并对象，所以不需要显示计数)
                if(repoState.intValue == Repository.StateT.REBASE_MERGE.bit
                    && (fromTo== Cons.gitDiffFromIndexToWorktree || fromTo== Cons.gitDiffFromHeadToIndex)
                ) {
                    rebaseCurOfAll?.value = Libgit2Helper.rebaseCurOfAllFormatted(gitRepository)
                }

                hasNoConflictItems.value = !gitRepository.index().hasConflicts()
                MyLog.d(TAG, "hasNoConflictItems="+hasNoConflictItems.value)






                //先检查index是否为空，因为这个性能比检查worktree快。
                //注意：冲突条目实际在index和worktree都有，但是我这里只有在worktree的时候才添加冲突条目，在index是隐藏的，因为冲突条目实际上是没有stage的，所以理应出现在worktree而不是index
                //检查是否存在staged条目，用来在worktree条目为空时，决定是否显示index有条目的提示
                //这个列表可以考虑传给index页面，不过要在index页面设置成如果没传参就查询，有参则用参的形式，但即使有参，也可通过index的刷新按钮刷新页面状态
                val (indexIsEmpty, indexList) = Libgit2Helper.checkIndexIsEmptyAndGetIndexList(gitRepository, curRepoFromParentPage.id, onlyCheckEmpty = false)

                if(repoChanged()) {
                    return
                }

                changeListPageHasIndexItem.value = !indexIsEmpty
                MyLog.d(TAG,"#$funName(): changeListPageHasIndexItem = "+changeListPageHasIndexItem.value)
                //只有在index页面，才需要更新条目列表，否则这个列表由worktree页面来更新
                if(fromTo == Cons.gitDiffFromHeadToIndex) {
                    itemList.value.clear()
                    indexList?.let {
                        itemList.value.addAll(it)
                    }
                }






                //最后查worktree是否为空，这个查的最慢，放最后。
                //20240504: 先查worktree，再查index是否为空，因为worktree有可能更改index。20250123：worktree修改index？用户不操作会自动修改？不可能啊，总之又改成先查index和仓库状态了，最后再查worktree列表，因为查worktree列表最慢
                // 检测index是否为空，如果不为空，会在图标有红点提示(最好红点，高亮图标也行)，如果worktree status为空(包含conflict条目) 且 index不为空，则会提示用户可去index区查看status

                if(fromTo == Cons.gitDiffFromIndexToWorktree) {  //查询worktree页面条目，就是从首页抽屉打开的changelist
                    //查询status页面的条目
                    val wtStatusList = Libgit2Helper.getWorkdirStatusList(gitRepository)

                    // 这个可以说是最重要的一处检测，因为重新执行git status最费时间的
                    if(repoChanged()) {
                        return
                    }

                    val hasWorktreeItem = wtStatusList.entryCount() > 0
                    changeListPageHasWorktreeItem.value = hasWorktreeItem
                    if (hasWorktreeItem) {
                        val worktreeItems = Libgit2Helper.getWorktreeChangeList(gitRepository, wtStatusList, curRepoFromParentPage.id)

                        if(repoChanged()) {
                            return
                        }

                        itemList.value.clear()
                        itemList.value.addAll(worktreeItems)
                    }

                    curRepoUpstream.value = Libgit2Helper.getUpstreamOfBranch(gitRepository, curRepoFromParentPage.branch)
                }


            }




        }


        if(repoChanged()) {
            return
        }

        val pageChangedNeedAbort = updateSelectedList(
            selectedItemList = selectedItemList.value,
            itemList = itemList.value,
            quitSelectionMode = quitSelectionMode,
            match = { oldSelected, item-> oldSelected.relativePathUnderRepo == item.relativePathUnderRepo },
            pageChanged = repoChanged
        )

        // 这里本来就在最后，所以是否return没差别，但避免以后往下面加代码忘了return，这里还是return下吧
        if (pageChangedNeedAbort) return



    }catch (e:Exception) {
        val curRepo = curRepoFromParentPage.value
//            setErrMsgForTriggerNotify(hasErr, errMsg, e.localizedMessage?:"")

        setErrMsg(e.localizedMessage ?: "ChangeList init err, code=182u45c945")

        showErrAndSaveLog(TAG,
            "#$funName() err, params are: fromTo=${fromTo}, commit1OidStr=${commit1OidStr}, commit2OidStr=${commit2OidStr}, curRepo=${curRepo}\nerr is: "+e.stackTraceToString(),
            "ChangeList init err:"+e.localizedMessage,
            requireShowToast,
            curRepo.id
        )
    }
}


@Composable
private fun getBackHandler(
    appContext: Context,
    exitApp: () -> Unit,
    isFileSelectionMode:MutableState<Boolean>,
    quitSelectionMode:()->Unit,
    fromTo: String,
    naviUp:()->Unit,
    changeListPageFilterModeOn:MutableState<Boolean>,
    openDrawer:()->Unit

): () -> Unit {
    val backStartSec = rememberSaveable { mutableLongStateOf(0) }
    val pressBackAgainForExitText = stringResource(R.string.press_back_again_to_exit);
    val showTextAndUpdateTimeForPressBackBtn = {
        openDrawer()
        showToast(appContext, pressBackAgainForExitText, Toast.LENGTH_SHORT)
        backStartSec.longValue = getSecFromTime() + Cons.pressBackDoubleTimesInThisSecWillExit
    }

    val backHandlerOnBack:()->Unit = {
        if(isFileSelectionMode.value) {
            quitSelectionMode()
        }else if(changeListPageFilterModeOn.value){
            changeListPageFilterModeOn.value = false
        } else {
            if(fromTo != Cons.gitDiffFromIndexToWorktree) { // TreeToTree or Index，非WorkTree页面，非顶级页面，点击返回上级页面
                doJobWithMainContext{
                    naviUp()
                }
            }else {  //WorkTree，顶级页面，双击退出app
                //如果在两秒内按返回键，就会退出，否则会提示再按一次可退出程序
                if (backStartSec.longValue > 0 && getSecFromTime() <= backStartSec.longValue) {  //大于0说明不是第一次执行此方法，那检测是上次获取的秒数，否则直接显示“再按一次退出app”的提示
                    exitApp()
                } else {
                    showTextAndUpdateTimeForPressBackBtn()
                }
            }

        }
    }


    return backHandlerOnBack
}

