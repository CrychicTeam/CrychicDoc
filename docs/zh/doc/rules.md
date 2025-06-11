---
progress: 100
title: 合作规范
description: 该文章提供了本站文档编写规范！
state: preliminary
---

# 文档编写规范

本文将对该文档项目的侧边栏、文件结构与合作的规范进行一定的说明，帮助您理解该如何高效地进行合作编写文档，并尽可能减少不规范合作引发的冲突。

首先您需要通过[该文章](./cooperation.md)知晓如何开始合作。

## 项目结构 {#FileStructure}

文章当前维护`中/英`两种语言，主要语言为中文。

:::alert {"type": "info", "title": "项目结构概览"}
这里是完整的项目结构，包含文件用途和状态指示器。
:::

<LiteTree>
// 定义状态和类型样式
#config=color:white;background:#1976d2;padding:2px 6px;border-radius:3px;font-size:12px;
#content=color:white;background:#4caf50;padding:2px 6px;border-radius:3px;font-size:12px;
#script=color:white;background:#ff9800;padding:2px 6px;border-radius:3px;font-size:12px;
#ignore=color:#666;background:#f5f5f5;padding:2px 6px;border-radius:3px;font-size:12px;
.important=font-weight:bold;color:#d32f2f;
.folder=color:#1976d2;font-weight:500;
// 定义图标
folder=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTEwIDRIOGEyIDIgMCAwIDAtMiAydjEyYTIgMiAwIDAgMCAyIDJoOGEyIDIgMCAwIDAgMi0yVjhhMiAyIDAgMCAwLTItMmgtM2wtMi0yWiIvPjwvc3ZnPg==
ts=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMTUgMTUiPjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzMxNzhDNiIgZD0iTTEyLjUgOHYtLjE2N2MwLS43MzYtLjU5Ny0xLjMzMy0xLjMzMy0xLjMzM0gxMGExLjUgMS41IDAgMSAwIDAgM2gxYTEuNSAxLjUgMCAwIDEgMCAzaC0xQTEuNSAxLjUgMCAwIDEgOC41IDExTTggNi41SDNtMi41IDBWMTNNMS41LjVoMTN2MTRIOS41eiIvPjwvc3ZnPg==
js=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9IiNmN2RmMWUiIGQ9Ik0zIDNoMTh2MThIM1ptMTYuNTI1IDE0LjVjLS4zLS4zNTQtLjc5NS0uNjI5LTEuNzE3LS42MjljLS44ODEgMC0xLjQzOS4zMTgtMS40MzkuNzE4YzAgLjM5Ni4zNzMuNjM3IDEuMTU2Ljk2N2MxLjMzMi41ODYgMi4yODEgMS4wOTMgMi4yODEgMi4zOGMwIDEuMzItMS4yMDMgMi4xNDMtMi45NzQgMi4xNDNjLTEuMjEzIDAtMi4yNzEtLjQ2Mi0yLjk1LTEuMDc0bC44NzUtMS4yNzNjLjQzMy4zODkgMS4wNjQuNzI0IDEuNjY0LjcyNGMuNzA2IDAgMS4wNjQtLjMzMSAxLjA2NC0uNzMzYzAtLjQ0OS0uMzc2LS43MjQtMS4yNDUtMS4wMzNjLTEuMzI1LS40ODgtMi4xMzItMS4yNS0yLjEzMi0yLjM2M2MwLTEuMzk0IDEuMDI5LTIuMTQzIDIuODU2LTIuMTQzYzEuMDY0IDAgMS43NDUuMzI4IDIuMzc3Ljg1OWwtLjgzIDEuMjQxWm0tNS44NDUtLjMzNWMuMzY2LjgxNS4zNjYgMS41NzcuMzY2IDIuNDd2My45MDZoLTEuODc2VjE5LjZjMC0xLjUyNy0uMDYtMi4xOC0uNTUtMi40OGMtLjQxLS4yODgtMS4wNzYtLjI3NC0xLjYxOC0uMTA3Yy0uMzc4LjExNy0uNzEzLjMzNS0uNzEzIDEuMDc0djUuMDU2SDYuNDI3VjEyLjgyaDEuODc2djIuMTEzYy43NDctLjM5OSAxLjU3Ny0uNzM4IDIuNjQ1LS43MzhjLjc2NCAwIDEuNTc3LjI1MyAyLjA2OS43ODdjLjQ5OC41NTIuNjI2IDEuMTU3LjcyMyAxLjk5MVoiLz48L3N2Zz4=
md=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTIyLjI3IDEzLjU2VjE2YTIgMiAwIDAgMS0yIDJIOGExIDEgMCAwIDEtMSAxSDNhMSAxIDAgMCAxLTEtMXYtNmExIDEgMCAwIDEgMS0xaDR2LTFhMiAyIDAgMCAxIDItMmgxMi4yN2ExIDEgMCAwIDEgMSAxdi41NnptLTMuNzMtOC41NkgyYTIgMiAwIDAgMC0yIDJ2MTBhMiAyIDAgMCAwIDIgMmgxNi41NGEyIDIgMCAwIDAgMi0yVjdhMiAyIDAgMCAwLTItMlptLTcuNzQgOC4zOUwxMiAxNi4yNWwyLjI2LTEuOTFhLjc1Ljc1IDAgMCAxIC45NyAxLjE0bC0zIDIuNTNhLjc1Ljc1IDAgMCAxLS45NiAwbC0zLTIuNTNhLjc1Ljc1IDAgMCAxIC45Ny0xLjE0WiIvPjwvc3ZnPg==
---
{.important}CrychicDoc                         // {.important}主项目
    [folder] .github                            // {#script}CI/CD脚本
        workflows                               // 自动构建脚本 //+
    [folder] .vitepress                         // {#config}VitePress配置
        [folder] config                         // {.important}本地化配置
            [ts] index.ts                       // 主配置文件 //v
        [folder] plugins                        // {.important}自定义插件
            [ts] custom-alert.ts                // 警告插件 //+
            [ts] dialog.ts                      // 对话框插件 //+
        [folder] theme                          // {.important}自定义主题
            [folder] components                 // Vue组件 //v
            [folder] styles                     // CSS样式 //v
        [ts] config.mts                         // {.important}VitePress配置
        [ts] index.ts                           // {.important}侧边栏配置
    [folder] .vscode                            // {#config}VS Code设置
        [md] snippets                           // Markdown代码片段 //v
    [folder] docs                               // {#content}内容目录
        [folder] public                         // 静态资源 //v
        [folder] zh                             // {#content}中文内容
            [md] 各种文件                        // 文档文件 //+
        [folder] en                             // {#content}英文内容
            [md] 各种文件                        // 文档文件 //+
    [md] README.md                              // {.important}项目说明
    [js] ExtractClassScript.js                  // {#ignore}旧版脚本
    [md] extracted_classes.md                   // {#ignore}旧版文件
    LICENSE                                     // {#config}CC BY-SA 4.0
    .gitignore                                  // {#config}Git忽略规则
</LiteTree>


## 侧边栏 {#Sidebar}

:::alert {"type": "warning", "title": "侧边栏重要性"}
**`侧边栏`是该文档最重要的引导之一**，并写有专门的[设置教程](./sidebarTutorial)，在本篇中将着重解释其在实际使用中的用法并让您知道编写文档时在侧边栏上需要进行的设置。
:::

侧边栏有着两种不同的运作逻辑：

### 侧边栏操作模式

<LiteTree>
// 定义工作流样式
#method1=color:white;background:#2196f3;padding:2px 6px;border-radius:3px;font-size:12px;
#method2=color:white;background:#9c27b0;padding:2px 6px;border-radius:3px;font-size:12px;
.pros=color:green;font-weight:500;
.step=color:#1976d2;
---
侧边栏配置方法
    {#method1}方法一：基于Index.md控制                //v    {.pros}完全控制
        {.step}在Index.md中配置children             // 完全自定义侧边栏
        {.step}无需单独的frontmatter设置           // 维护友好
        {.pros}适合复杂项目                       // 如KubeJS系列
        {.pros}可预测的结构生成                   // 完全控制子类别
    {#method2}方法二：基于Frontmatter              //+    {.pros}简单设置
        {.step}Index.md中基本root配置             // 最小化设置要求
        {.step}文章中设置noguide: true            // 单独文章控制
        {.step}在frontmatter中配置title           // 文章特定标题
        {.pros}简单维护                          // 单个文章易管理
        {.pros}灵活的文章管理                    // 每个文档控制
</LiteTree>

:::alert {"type": "info", "title": "导航引导"}
由于文档的内容基本依靠侧边栏与导航栏来进行引导，因此当您编写文档内容时请务必保证侧边栏的同步，如有疑问请尝试询问负责成员。
:::

:::alert {"type": "tip", "title": "重要提示"}
为了保证Prev与Next的自动生成，请不要在index.md中书写内容而只把它当做侧边栏的生成器。
:::

## 文章编撰规范{#Article}

该部分将将要讲述必要规范，即便规范会大致讲清需要注意的细则但还是请以`Pull Request`的反馈为主。

:::alert {"type": "success", "title": "规范目的"}
规范的目的是保证文档风格和引导的一致性，来保证读者能够更好地消化内容。规范的一部分内容有参考[MCMOD编写规范](https://bbs.mcmod.cn/thread-646-1-1.html)，规范并不约束第三方文档，但不允许**过度偏离文章意图的表达**。
:::

### 标题 {#Title}

:::alert {"type": "error", "title": "关键要求"}
你**必须**遵守标题使用的规范，<font color=red>**否则你的提交将永远不会通过**</font>。
:::

标题的层级应当采用渐进式，且`H1`级别的标题应当在最上方出现且只出现一次。

**结构示例：**

<LiteTree>
// 定义标题样式
#h1=color:white;background:#d32f2f;padding:3px 8px;border-radius:4px;font-weight:bold;
#h2=color:white;background:#1976d2;padding:2px 6px;border-radius:3px;
#h3=color:white;background:#388e3c;padding:2px 6px;border-radius:3px;
#h4=color:white;background:#f57c00;padding:2px 6px;border-radius:3px;
---
文档结构
    {#h1}# 一级标题                          // 每个文档只有一个 //!
        {#h2}## 二级标题                    // 允许多个 //v
            {#h3}### 三级标题               // 嵌套在H2下 //v
                {#h4}#### 四级标题          // 嵌套在H3下 //v
        {#h2}## 另一个二级标题              // 与上面同级 //v
            {#h3}### 另一个三级标题         // 嵌套结构 //v
                {#h4}#### 另一个四级标题    // 正确嵌套 //v
</LiteTree>

### 自定义锚点 {#anchor}

锚点`{#custom-anchor}`是`Vitepress`默认支持的`Markdown`扩展，使用它能够让链接不因为中文的标题而在复制后变为冗长的字符，例如[这个链接](#文章编撰规范)就是没有自定义锚点的链接，而[这个](#Title)则是被锚点优化过后的。

一般我们鼓励使用锚点来便于分享。

## 样式与插件 {#Style&Plugin}

:::alert {"type": "info", "title": "可选内容"}
该部分内容是**非强制性**的。
:::

该文档内置了不少`美化样式`与类似功能的`插件`，它们有利于帮助撰写者设计更加生动的文档内容，避免平淡的文字消磨读者的耐性，也有利于作者引导读者阅读真正重要的部分。

<font size = 1>这里所说的引导更多强调的是内容的主次关系，一般来说撰写的内容都是有用的信息，但需要借助排版和样式来确保读者获取到最有用的部分。</font>

### 样式 {#Style}

文档对当前支持的样式有整理一个单独的[文章](./styleList.md)，如果你有这方面的需求，可以在撰写前先查看该部分内容。

:::alert {"type": "tip", "title": "基础要求"}
即便你不会使用复杂的样式，也请了解基本的[Markdown格式](https://markdown.com.cn/basic-syntax/)与Vitepress的[Markdown扩展](https://vitepress.dev/zh/guide/markdown)再进行文档的编写。
:::

### 插件 {#Plugin}

文档有一些内置的`插件/组件`，一般是为服务某种特殊场景而添加，可在[此处](./samples.md)查看。

### 文档配置 {#doc-config}

该文档文件都有以下[frontmatter](#frontmatter)配置字段：

:::alert {"type": "info", "title": "VitePress兼容性"}
本站同时支持Vitepress的原生frontmatter样式，详情请见[此处](https://vitepress.dev/zh/reference/frontmatter-config)。
:::

| 配置字段      | 用途                             | 类型      | 省缺值     |
|-----------|--------------------------------|---------|---------|
| `title`   | 设置侧边栏中显示的标题（如未设置则使用文件名）        | string  | `N/A`   |
| `noguide`| 该文章是否显示在侧边栏 (false=显示, true=隐藏)|boolean |`false`|
|`backPath`|设置该界面点击BackButton后前往的位置|string|`N/A`|
| `authors`  | 设置该文章额外的作者，显示在贡献者栏| string[]  | `N/A`   |
| `showComment`  | 是否显示评论区 | boolean  | `true`   |
| `gitChangelog`  | 是否显示贡献者和页面历史 | boolean  | `true`   |
| `progress`  | 设置该文章的编撰进度 | int  | `N/A`   |
| `description`  | 设置该文章的预览内容 | string  | `N/A` |
| `state`  | 设置该文章的编写状态 | string | `N/A` |

::: details 示例

```yaml
---
title: 示例
backPath: ../
authors: ['M1hono', 'skyraah'] # 你必须提交过一次贡献才能正常地显示自己的头像与链接。
showComment: false
gitChangelog: false
progress: 100
description: 该文章提供了本站文档编写规范！
state: preliminary #仅允许preliminary unfinished outdated renovating四种输入
---
```

:::

#### frontmatter声明 {#frontmatter}

在每个 Markdown 文件的开头，使用 `---` 来创建frontmatter配置

```yaml
---
# 在这里添加您的frontmatter
---
```

### 类型补全 {#TwoSlash}

:::alert {"type": "warning", "title": "Feature Status"}
**TwoSlash当前无法使用**. 
:::

该部分实际算Plugin的范畴，但其过于特殊。

如果你看过了[样式](#Style)所提及的两个链接，那么你想必已经知道了`Codeblock`这个方便分享代码并显示`代码高亮`的功能。

文档在这个基础上内置了显示`类型补全`的插件，使得其可以提供更有利于相关教程的代码展示。

例如，同样的代码块：

::: demo
```js
const String = "No Twoslash"
console.log(String)
//              ^?        
```
:::

但如果有了类型补全的话：

::: demo
```js
const String = "Twoslash"
console.log(String)
//              ^?        
```
:::

## 内容 {#Content}

<LiteTree>
// 定义优先级样式
#critical=color:white;background:#d32f2f;padding:2px 6px;border-radius:3px;font-size:12px;
#important=color:white;background:#ff9800;padding:2px 6px;border-radius:3px;font-size:12px;
#guideline=color:white;background:#4caf50;padding:2px 6px;border-radius:3px;font-size:12px;
---
内容指导原则
    {#critical}内容准确性                           //!    首要标准
        确保编写正确的内容                         // 验证信息准确性
        与社区和QQ群众人交流探讨                  // 协作验证
    {#guideline}内容创作流程                       //+    协作方式
        进行不完美的初始创作                       // 不要担心完美
        与合作者一起精改进与精进                   // 团队合作提升质量
    {#important}社区沟通                          //v    重要实践
        多与社群沟通                             // 保持连接
        绝不擅自删改他人的创作                     // 尊重他人贡献
</LiteTree>

:::alert {"type": "error", "title": "严重警告"}
请不要擅自**删改**他人的创作！！！
:::

## 关于合作 {#Cooperation}

该文档在合作上并没有繁琐的规范，有且仅有一个：在进行修改前请**先询问原作者的意见**！！第三遍了！！

### 第三方文档合作

如果你是第三方文档的拥有者，要在作者一栏显示你的名字，你需要至少提交一次内容修改，才能被程序正确地识别，否则无法正常生成链接与头像。

:::alert {"type": "success", "title": "合作总结"}
- **尊重**：修改他人作品前务必先询问
- **沟通**：与社区保持联系
- **贡献**：至少提交一次修改以获得正确识别
- **质量**：专注于内容准确性和一致性
:::