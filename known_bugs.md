

---
软键盘状态在切换行之后仍会丢失 20250726：
提交 '7ff0f013' 在一定程度上缓解了编辑器内容修改后软键盘状态丢失的bug，但并没有完全修复，目前如果在同一行输入能保持状态，但如果添加或删除行，例如按回车、在行开头按backspace，等切换行的操作，软键盘状态仍会重置，不过影响不大，而且目前的实现方式，不太好处理，所以不处理了。

---
UI bug 20250720:
bug：编辑器，开软键盘，点顶栏菜单，不展开，可能和那个获取configure的代码失效然后我改用状态变量有关？如果是的话，需要更新compose，然后禁用我的状态变量，改用默认的`LocalConfiguration.current`，看看还有没有此bug。

此bug影响范围：跟屏幕尺寸相关的都有影响，目前是如果尺寸变化，比如横屏切竖屏，宽高会变化，这时点下屏幕就能获取最新的尺寸信息，但如果修改成使用`LocalConfiguration.current`，点屏幕也获取不到最新尺寸，我记得之前行，但现在不行了，可能因为改了manifest里activity属性？总之现在不改能凑合用，改了连凑合都不行，所以暂时不改了。

---
app崩溃时有可能丢失用户正在编辑且未保存的内容 20250715：
场景1：如果是compose线程崩溃，一般不会丢内容，会触发自动保存，但我忘记自动保存是compose生命周期的on pause触发的还是在外部写了某种机制了，粗略看了下代码，应该是依靠的editor inner page 的on pause生命周期，但是，如果是依靠生命周期，应该也有可能丢内容，尽管我以前测试的结果是没丢，但更新依赖库可能会有影响。

场景2：如果是app进程崩溃，比如OOM，很大概率会丢内容，经我测试，未保存的内容，崩溃前会触发保存，但由于进程被杀，可能文件写入一半，进程就没了，所以这种保存还不如不保存，不保存没准丢的数据反而更少。


总的来说，应该极力避免类似场景2的情况发生，并且尽量避免类似场景1的情况发生。

---
语法高亮对于单行大文件，会很卡 20250709：
editor语法高亮开启时，如果打开单行，大量代码的文件，可能会很卡。

原因：当前语法高亮分析为了方便实现，并没针对删除和新增的内容做差异比较，完全是基于行的删除和新增，因此，单行内容就等于每次修改内容后都执行全量解析，所以性能就差了。

---
editor删除内容时停顿 20250518：
editor，按住软键盘删内容，会停顿，和输入法有点关系，如果输入法会关联上下文来显示候选词就会导致删除停住，可能和目前editor实现所用的组件有关，那个BasicTextField可能没很好的处理这个问题，不过问题不大，不管了，以后再说。
---
x 解决了) diff页面共用条目列表导致的问题 20250427：
最终怎么解决的：
首先尝试了：导航时参数扔导航专用缓存，随机返回key，navi up时清掉，就行了，每个页面的状态都独立了，互不影响了，要说缺点，没什么明显缺点
然后发现还是有下面复现方法1的bug，发现是因为CustomSaveableState共享了状态导致的，于是让所有子页面都使用随机key，然后在返回顶级页面后清掉子页面的状态变量缓存，最终解决了这个问题，同时一定程度上减少了内容占用（之前每个页面的状态变量都常驻内存，现在只有首页的常驻内存）。


优先级：低

注：发现此bug后给不同来源的条目列表做了区分，各用各的，所以此bug有所缓解，但理论上只有每个diff页面实例各用各的列表才能根除。


由于所有diff页面共用一个条目列表，所以如果从diff页面跳转到diff页面，列表会错乱。

复现：
方法1. cl页面->diff某文件(bug点)->编辑->文件历史->diff，再返回到bug点，bug就出现了
方法2. 查看某文件历史 -> diff -> 顶部在内部editor打开 -> 关闭文件 -> 从最近文件列表选个别的文件打开 -> 查看其文件历史 -> diff ...这个步骤可以无限循环下去，而之前的diff列表条目已经被后面的覆盖

解决方案：
导航到页面时给每个diff页面实例创建各自独立的条目列表，并在返回时清掉这个列表就行了

---
x 已解决）无法克隆ssh子模块 20250424：

已解决：原因：把`凭据.name`当作private key传过去了，晕！

报错：failed to authenticate SSH session: Unable to extract public key from private key file: Unsupported private key file format

但如果不是子模块的ssh仓库则可正常克隆，感觉应该不是很严重的bug，否则普通ssh url的仓库应该也无法克隆，改天看下libgit2源代码，看下是否好解决

复现：添加ssh url的submodule，然后选择对应的sshkey凭据并克隆

---
偶发bug，难以复现，备忘 20250402：
x 无法复现，检查逻辑，未发现问题，以后遇到再说吧) 换行bug：在行开头，回车，下次光标会在下一行第一个字后面，应在前面

---
（已解决）文本编辑器bug（不严重） 20250325：
解决方案：仅让TextField的onFocus负责聚焦，其会调用selectField定位到某行，update时更新状态使用上次定位的位置即可。


复现：选中有文本的行，点击其他行的行号，启动多选模式，指示当前行的三道横线图标依然在之前编辑的行而不是点击行号启动选择模式的那行。

原因：旧状态调用updateField()的时机有问题，比如旧状态先调用selectField()，触发onChanged()，创建了新状态，接着，旧状态的updateField()被调用，于是，state中的已选中行又变回了旧状态中的数据。


实际影响：问题不大，只有当按复现方法触发bug后，初次光标定位不准，其他没影响。

---
文本编辑器切换预览和编辑时自动定位不够准确 20250319：
功能：在特定切换预览和编辑的时候换算行号和像素，然后定位到滚动位置。


触发场景：
从预览到编辑，需要定位行号的场景： 
1. 在预览页面点击顶部编辑图标

从编辑到预览，需要定位行号的场景：
1. 预览页面导航栈首次打开root条目时
2. 非初次预览，但当前预览页面和编辑页面不匹配：正在编辑文件a，然后预览文件a，然后从文件a通过点击相对路径链接跳转到了文件b，在预览文件b的时候点顶部左上角关闭按钮退出预览返回编辑，这时预览的b和编辑的a，正在预览和正在编辑不匹配，若再进入预览页面，就会在当前导航栈“加塞”，这时相当于预览页面重新打开一次当前编辑的文件，所以需要重新初始化下预览的滚动位置，而初始化操作就是尽量定位到当前正在编辑的位置


缺陷：不准，因为需要换算像素和行号，很难做到精准，如果基本都是纯文字，准确度尚可，若有图片等文字和预览大小差异较大的情况，偏差会比较大。
