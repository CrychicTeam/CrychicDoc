---
progress: 100
description: 该文章提供了本站文档编写规范！
---

# 文档编写规范

本文将对该文档项目的侧边栏、文件结构与合作的规范进行一定的说明，帮助您理解该如何高效地进行合作编写文档，并尽可能减少不规范合作引发的冲突。

首先您需要通过[该文章](./cooperation.md)知晓如何开始合作。

## 项目结构 {#FileStructure}

文章当前维护`中/英`两种语言，主要语言为中文。

```
- crychicdoc
    - .github/    自动构建脚本
    - .vitepress/
        - config/ 本地化配置文件。
        - plugins/ mdit插件存放位置
        - theme/  自定义主题和组件
        - config.mts Vitepress的配置文件
        - index.ts 侧边栏的配置文件。
    - .vscode/ md-snippets
    - docs
        - public 存放静态文件
        - zh 简体中文内容
        - en 英文内容
    - README.md  本文件
    - .gitignore gitignore文件。
    - ExtractClassScript.js 请忽视
    - extracted_classes.md 请忽视
    - LICENSE CC BY-SA 4.0
```


## 侧边栏 {#Sidebar}

**`侧边栏`是该文档最重要的引导之一**，并写有专门的[设置教程](./sidebarTutorial)，在本篇中将着重解释其在实际使用中的用法并让您知道编写文档时在侧边栏上需要进行的设置。

首先侧边栏有着两种运作逻辑。

1. ++第一种完全基于`Index.md`++,利用`children`对侧边栏的生成进行详细地控制，这种方式的优点在于，你可以完全地自定义侧边栏的每个子栏目，确保其生成的内容符合你的预期，同时无需为每个文档都设置`frontmatter`，这有利于维护结构复杂的项目，例如`KubeJS系列`的文档。
2. 第二种则为基于Index.md与++各个文档单独的`frontmatter`设置++，这种情况只需在`index.md`中进行最简单的`root`配置，并在需要生成的文章进行`noguide: true`与`title`的设置即可即可，详情可参[此处](./sidebarTutorial.md)。

由于文档的内容基本依靠侧边栏与导航栏来进行引导，因此当您编写文档内容时请务必保证侧边栏的同步，如有疑问请尝试询问负责成员。

> [!TIP] 请注意
> 为了保证Prev与Next的自动生成，请不要在index.md中书写内容而只把它当做侧边栏的生成器。

## 文章编撰规范{#Article}

该部分将将要讲述必要规范，即便规范会大致讲清需要注意的细则但还是请以`Pull Request`的反馈为主。

规范的目的是保证文档风格和引导的一致性，来保证读者能够更好地消化内容，规范的一部分内容有参考[MCMOD编写规范](https://bbs.mcmod.cn/thread-646-1-1.html)，规范并不约束第三方文档，但不允许**过度偏离文章意图的表达**。

### 标题 {#Title}

> [!warning] 请注意
> 你必须遵守标题使用的规范，<font color=red>否则你的提交将永远不会通过</font>。

标题的层级应当采用渐进式，且`H1`级别的标题应当在最上方出现且只出现一次。

例如：
```markdown
# 一级标题

## 二级标题
### 三级标题
#### 四级标题

## 二级标题
### 三级标题
#### 四级标题
```

### 自定义锚点 {#anchor}

锚点是`Vitepress`默认支持的`Markdown`扩展，使用它能够让链接不因为中文的标题而在复制后变为冗长的字符，例如[这个链接](#文章编撰规范)就是没有自定义锚点的链接，而[这个](#Title)则是被锚点优化过后的。

一般我们鼓励使用锚点来便于分享。

## 样式与插件 {#Style&Plugin}

该部分内容非强制性。

该文档内置了不少`美化样式`与类似功能的`插件`，它们有利于帮助撰写者设计更加生动的文档内容，避免平淡的文字消磨读者的耐性，也有利于作者引导读者阅读真正重要的部分

<font size = 1>这里所说的引导更多强调的是内容的主次关系，一般来说撰写的内容都是有用的信息，但需要借助排版和样式来确保读者获取到最有用的部分。</font>

### 样式 {#Style}

文档对当前支持的样式有整理一个单独的[文章](./styleList.md)，如果你有这方面的需求，可以在撰写前先查看该部分内容。

> [!TIP] 注
> 即便你不会使用复杂的样式，也请了解基本的[Markdown格式](https://markdown.com.cn/basic-syntax/)与Vitepress的[Markdown扩展](https://vitepress.dev/zh/guide/markdown)再进行文档的编写。

### 插件 {#Plugin}

文档有一些内置的`插件/组件`，一般是为服务某种特殊场景而添加，可在[此处](./samples.md)查看，

### 文档配置 {#doc-config}

该文档文件都有以下[frontmatter](#frontmatter)配置字段。

::: tip 提示

本站同时支持Vitepress的原生frontmatter样式，详情请见[此处](https://vitepress.dev/zh/reference/frontmatter-config)

:::

| 配置字段      | 用途                             | 类型      | 省缺值     |
|-----------|--------------------------------|---------|---------|
| `title`   | 设置侧边栏中显示的标题（如未设置则使用文件名）        | string  | `N/A`   |
| `noguide`| 该文章是否显示在侧边栏|boolean |`true`|
|`backPath`|设置该界面点击BackButton后前往的位置|string|`N/A`|
| `authors`  | 设置该文章额外的作者，显示在贡献者栏，配置可参考[此处](https://nolebase-integrations.ayaka.io/pages/zh-CN/integrations/vitepress-plugin-git-changelog/configure-vite-plugins#%E9%80%89%E9%A1%B9-mapcontributors-%E4%B8%BA%E8%B4%A1%E7%8C%AE%E8%80%85%E6%B7%BB%E5%8A%A0%E6%95%B0%E6%8D%AE%E6%98%A0%E5%B0%84)| string[]  | `N/A`   |
| `showComment`  | 是否显示评论区 | boolean  | `true`   |
| `gitChangelog`  | 是否显示贡献者和页面历史 | boolean  | `true`   |
| `progress`  | 设置该文章的编撰进度 | int  | `N/A`   |
| `description`  | 设置该文章的预览内容 | string  | `N/A`   |

::: details 示例

```yaml
---
title: 示例
backPath: ../
authors: ['M1hono', 'skyraah'] # 可在common-config中额外配置 但还是请尽量使用Github ID。
showComment: false
gitChangelog: false
progress: 100
description: 该文章提供了本站文档编写规范！
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
```js twoslash
const String = "Twoslash"
console.log(String)
//              ^?        
```
:::

## 内容 {#Content}

- 该项目以内容的严谨性为首要标准，需确保自己正在编写正确的内容，为此可以多与社区与腾讯群中的众人进行交流与探讨。
- 无需担忧自己新著内容的严谨性，请先进行不完美的创作，并与合作者一起进行精改进与精进。
- 请一定要多与社群沟通，并且一定不要擅自`删改`他人的创作。

> [!CAUTION] 注意
> 请不要擅自**删改**他人的创作！！！

## 关于合作 {#Cooperation}

该文档在合作上并没有繁琐的规范，有且仅有一个，在进行修改前请**先询问原作者的意见**!!第三遍了!!。

### 第三方文档合作

如果你是第三方文档的拥有者，要在作者一栏显示你的名字，你需要至少提交一次内容修改，才能被程序正确地识别，否则无法正常生成链接与头像。