package com.catpuppyapp.puppygit.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.catpuppyapp.puppygit.play.pro.R
import com.catpuppyapp.puppygit.utils.state.CustomStateSaveable

/**
 * 关于此组件的备忘：
 * 文件管理器 一类
 * 提交历史 和 文件历史 一类
 * 自动化页面 一类
 * 自动化页面弹窗选择仓库那个 一类
 * 其他所有页面 一类
 *
 * 其中自动化页面首页和弹窗数据量不会太大，没做在渲染时避免重复执行过滤操作的处理；文件管理器由于执行了递归搜索，需要单独处理；提交历史和文件历史由于有个在普通列表显示过滤列表条目的功能，需要处理下索引；其他所有页面几乎是相同的过滤策略，只有匹配条件不同
 */
@Composable
fun FilterTextField(
    filterKeyWord: CustomStateSaveable<TextFieldValue>,
    loading:Boolean = false,
    placeholderText:String = stringResource(R.string.input_keyword),
    singleLine:Boolean = true,
    trailingIconTooltipText: String="",
    trailingIcon: ImageVector?=null,
    trailingIconColor: Color?=null,
    trailingIconDesc: String?=null,
    trailingIconOnClick:(()->Unit)?=null,
    onValueChange:(newValue:TextFieldValue)->Unit = { filterKeyWord.value = it },
) {
    val trailIcon:@Composable (() -> Unit)? = if(trailingIcon!=null){
        {
            LongPressAbleIconBtn(
                tooltipText = trailingIconTooltipText,
                icon = trailingIcon,
                iconContentDesc = trailingIconDesc,
                iconColor = trailingIconColor
            ) {
                trailingIconOnClick?.invoke()
            }

        }
    }else {
        null
    }

    Column(
        modifier = Modifier.fillMaxWidth().fillMaxHeight().padding(top = 1.dp),
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(.95f),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),  //字整小点，不然若高度不占满，可能竖着显示不全字
            value = filterKeyWord.value,
            onValueChange = { onValueChange(it) },
            placeholder = { Text(placeholderText) },
            singleLine = singleLine,
            trailingIcon = trailIcon,
            // label = {Text(title)}

            //软键盘换行按钮替换成搜索图标且按搜索图标后执行搜索
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//        keyboardActions = KeyboardActions(onSearch = {
//            doFilter(filterKeyWord.value.text)
//        })
        )

        //用来指示是否仍在查找
        if(loading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
