---
title: VSCode 代码片段使用指南
description: CrychicDoc 项目中的 VSCode Markdown 代码片段完整使用指南
---

# VSCode 代码片段使用指南

本指南介绍如何使用 CrychicDoc 项目中的增强型 VSCode 代码片段，让您更高效地编写 Markdown 文档。

## 快速开始

在 VSCode 中编辑 `.md` 文件时，输入代码片段的**前缀**并按 `Tab` 键 (或根据您的设置按 `Enter`) 即可展开代码片段。

## 代码片段分类

为了方便查找和使用，所有代码片段已根据其功能进行了分类。

---

## 侧边栏与页面配置 (Sidebar, Page, & Frontmatter Configuration)

这些代码片段用于快速生成和配置页面的 Frontmatter、侧边栏的结构和行为。

### 页面与章节模板 (Page & Section Templates)

用于快速搭建标准页面或侧边栏根章节的结构。

| 前缀             | 描述                             |
| ---------------- | -------------------------------- |
| `front`          | 基础 Frontmatter块               |
| `page`           | 完整页面模板                     |
| `section`        | 完整侧边栏根章节模板             |
| `sidebar-root`   | (同上) 完整侧边栏根章节模板    |
| `sidebar-page`   | 完整侧边栏内页面模板             |
| `front-complete` | 包含所有常用字段的完整 Frontmatter |

### Frontmatter 核心字段 (Core Frontmatter Fields)

用于添加或修改通用的 Frontmatter 字段。

| 前缀              | 描述                       | 输出示例 (仅供参考)        |
| ----------------- | -------------------------- | ------------------------- |
| `title`           | 文档标题 (字段生成)        | `title: Your Title`       |
| `description`     | 文档描述 (字段生成)        | `description: Your Desc`  |
| `layout`          | 文档布局 (字段生成)        | `layout: doc`             |
| `tags`            | 文档标签 (字段生成)        | `tags:\n  - tag1`        |
| `authors`         | 文档作者 (字段生成)        | `authors:\n  - author1`   |
| `progress`        | 文档进度 (字段生成)        | `progress: 100`           |
| `noguide`         | 无指南标记 (字段生成)      | `noguide: true`           |
| `title-val`       | 设置文档/章节标题          | `title: Your Title`       |
| `description-val` | 设置页面描述               | `description: Your Desc`  |
| `authors-val`     | 设置页面作者 (数组)        | `authors:\n  - user1`     |
| `tags-val`        | 设置页面标签 (数组)        | `tags:\n  - tag1`        |
| `progress-val`    | 设置完成进度               | `progress: 100`           |

### 侧边栏专用配置 (Sidebar-specific Configuration)

用于定义侧边栏项目的特定行为和属性。

| 前缀              | 描述                             | 输出示例              |
| ----------------- | -------------------------------- | --------------------- |
| `root`            | 根侧边栏配置 (创建独立区域)      | `root: true...`       |
| `root-layout`     | 带 VitePress 布局的根侧边栏配置  | `root: true...layout: doc`|
| `dir`             | 基础目录配置 (用于 `index.md`)   | `title: Dir Title...` |
| `root-field`      | 添加 `root` 字段                 | `root: true`          |
| `root-true`       | 设置为侧边栏根章节               | `root: true`          |
| `root-false`      | 非侧边栏根章节                   | `root: false`         |
| `collapsed`       | 添加 `collapsed` 字段            | `collapsed: true`     |
| `collapsed-true`  | 默认折叠                         | `collapsed: true`     |
| `collapsed-false` | 默认展开                         | `collapsed: false`    |
| `hidden`          | 添加 `hidden` 字段               | `hidden: true`        |
| `hidden-true`     | 从侧边栏隐藏                     | `hidden: true`        |
| `hidden-false`    | 在侧边栏显示                     | `hidden: false`       |
| `priority-val`    | 设置排序优先级                   | `priority: 1`         |
| `maxdepth-val`    | 最大嵌套深度                     | `maxDepth: 3`         |
| `layout-doc`      | 文档布局 (VitePress)             | `layout: doc`         |
| `layout-home`     | 主页布局 (VitePress)             | `layout: home`        |
| `layout-page`     | 自定义页面布局 (VitePress)       | `layout: page`        |
| `prev`            | 添加 `prev` 导航字段             | `prev: false`         |
| `prev-true`       | 启用上一页导航                   | `prev: true`          |
| `prev-false`      | 禁用上一页导航                   | `prev: false`         |
| `next`            | 添加 `next` 导航字段             | `next: false`         |
| `next-true`       | 启用下一页导航                   | `next: true`          |
| `next-false`      | 禁用下一页导航                   | `next: false`         |
| `comment-true`    | 启用评论                         | `showComment: true`   |
| `comment-false`   | 禁用评论                         | `showComment: false`  |
| `changelog-true`  | 显示 Git 更新日志                | `gitChangelog: true`  |
| `changelog-false` | 隐藏 Git 更新日志                | `gitChangelog: false` |

### 文档状态 (Document States)

用于标记文档的当前状态。

| 前缀               | 描述         | 输出                 |
| ------------------ | ------------ | -------------------- |
| `state-preliminary`| 初稿状态     | `state: preliminary` |
| `state-unfinished` | 未完成状态   | `state: unfinished`  |
| `state-outdated`   | 过期状态     | `state: outdated`    |
| `state-renovating` | 修订中状态   | `state: renovating`  |
| `state preliminary`| (同上)初稿   | `state: preliminary` |
| `state unfinished` | (同上)未完成 | `state: unfinished`  |
| `state outdated`   | (同上)过期   | `state: outdated`    |
| `state renovating` | (同上)修订中 | `state: renovating`  |

---

## VitePress 内置功能增强 (VitePress Built-in Feature Enhancements)

用于快速使用 VitePress 提供的 Markdown 扩展功能。

### 提示容器 (Admonition Containers)

| 前缀             | 描述                       |
| ---------------- | -------------------------- |
| `info`           | VitePress 信息容器         |
| `tip`            | VitePress 提示容器         |
| `warning`        | VitePress 警告容器         |
| `danger`         | VitePress 危险/错误容器    |
| `details`        | VitePress 可折叠详情容器   |
| `custom-container`| 自定义类型容器             |

### 代码功能 (Code Features)

| 前缀             | 描述                 |
| ---------------- | -------------------- |
| `code-group`     | VitePress 代码组     |
| `code-lines`     | 带行号的代码块       |
| `code-highlight` | 带行高亮的代码块     |

### 数学公式 (Math Formulas)

| 前缀          | 描述         | 输出示例                  |
| ------------- | ------------ | ------------------------- |
| `math-inline` | 内联数学公式 | `$x^2 + y^2 = z^2$`       |
| `math-block`  | 块级数学公式 | `$$\nx^2 + y^2 = z^2\n$$` |

---

## 扩展组件与样式 (Extended Components & Styling)

利用项目自定义的 Vue 组件或特殊 Markdown 语法来增强内容表现力。

### 内容组织 (Content Organization)

| 前缀      | 描述       |
| --------- | ---------- |
| `tabs`    | 选项卡容器 |
| `stepper` | 步骤指示器 |
| `timeline`| 时间轴插件 |

### 媒体与交互 (Media & Interaction)

| 前缀         | 描述                 |
| ------------ | -------------------- |
| `carousel`   | 图片轮播             |
| `iframe`     | 内嵌 iframe          |
| `img-size`   | 指定尺寸的图片       |
| `linkcard`   | 链接卡片组件         |
| `bilibili`   | Bilibili 视频组件    |
| `pdf-viewer` | PDF 查看器组件       |

### 图表与可视化 (Diagrams & Visualization)

| 前缀           | 描述               |
| -------------- | ------------------ |
| `mermaid`      | Mermaid 图表       |
| `damage-chart` | 伤害计算图表组件   |
| `lite-tree`    | 文件树结构         |

### 卡片样式 (Card Styles)

| 前缀            | 描述     |
| --------------- | -------- |
| `card-text`     | 文本卡片 |
| `card-flat`     | 扁平卡片 |
| `card-elevated` | 浮起卡片 |
| `card-tonal`    | 色调卡片 |
| `card-outlined` | 轮廓卡片 |

### 自定义警告框 (Custom Alerts - v-style)

| 前缀            | 描述       |
| --------------- | ---------- |
| `alert-success` | 成功警告框 |
| `alert-info`    | 信息警告框 |
| `alert-warning` | 警告警告框 |
| `alert-error`   | 错误警告框 |

### 对齐与布局 (Alignment & Layout)

| 前缀            | 描述             |
| --------------- | ---------------- |
| `align-left`    | 左对齐内容       |
| `align-center`  | 居中对齐内容     |
| `align-right`   | 右对齐内容       |
| `align-justify` | 两端对齐内容     |
| `demo`          | Demo 演示块容器  |

### 特殊效果 (Special Effects)

| 前缀         | 描述               |
| ------------ | ------------------ |
| `magic-move` | Magic Move 代码过渡 |

---

## 常用工具与文本格式化 (Utilities & Text Formatting)

### 文本标记 (Markdown Extensions)

| 前缀      | 描述                             | 输出示例                 |
| --------- | -------------------------------- | ------------------------ |
| `spoiler` | 隐藏/剧透文本                    | `!!hidden content!!`     |
| `mark`    | 高亮标记文本                     | `==highlighted text==`   |
| `insert`  | 插入文本                         | `++inserted text++`      |
| `sub`     | 下标文本                         | `text~subscript~`        |
| `sup`     | 上标文本                         | `text^superscript^`      |
| `ruby`    | Ruby 注解 (拼音指南)             | `{中国:zhōng\|guó}`     |

### 任务列表 (Task Lists)

| 前缀   | 描述         | 输出示例                      |
| ------ | ------------ | ----------------------------- |
| `todo` | 待办事项列表 | `- [ ] Task\n- [x] Completed` |

### 常用工具 (Common Utilities)

| 前缀       | 描述                     | 输出                       |
| ---------- | ------------------------ | -------------------------- |
| `#nbsp`    | 插入两个不间断空格       | `&nbsp;&nbsp;`             |
| `nbsp`     | 插入一个不间断空格       | `&nbsp;`                   |
| `@title`   | Frontmatter 标题变量引用 | `{{ $frontmatter.title }}` |
| `fm-title` | (同上) Frontmatter 标题  | `{{ $frontmatter.title }}` |
| `@done`    | 完成状态 Unicode 复选框  | `☑`                        |
| `@pending` | 待办状态 Unicode 复选框  | `☐`                        |

---

## 使用技巧

### 1. 快速访问

-   按 `Ctrl+空格` (Windows/Linux) 或 `Cmd+空格` (macOS) 打开智能提示，代码片段通常会优先显示。
-   输入前缀的部分字母，VSCode 会自动过滤并显示匹配的代码片段。

### 2. 参数填写

-   许多代码片段包含占位符 (例如 `${1:Placeholder}`)。代码片段展开后，光标会自动定位到第一个占位符。
-   填写完毕后，按 `Tab` 键跳转到下一个占位符。
-   按 `Shift+Tab` 返回上一个占位符。
-   所有占位符填写完毕或希望提前结束时，按 `Esc` 键退出片段编辑模式。

### 3. 嵌套使用

大部分容器类代码片段支持嵌套使用，例如在提示容器中标记文本：

```markdown
::: v-info 提示
这里可以使用 ==标记文本== 和 !!隐藏内容!!
:::
```

### 4. 自定义修改

如需修改或添加您自己的代码片段，可以直接编辑项目根目录下的 `.vscode/md.code-snippets` 文件。文件格式为 JSON。

## 键盘快捷键建议

为了进一步提高效率，您可以为常用的代码片段设置自定义键盘快捷键。编辑您的 `keybindings.json` 文件 (通过 `File > Preferences > Keyboard Shortcuts > User > keybindings.json` 打开)：

```json
// keybindings.json
[
    {
        "key": "ctrl+shift+d", // 示例快捷键
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && editorLangId == markdown",
        "args": { 
            // "name": "Demo Block" // 使用代码片段的名称 (JSON 中的键)
            "snippet": "::: demo ${1:Demo Title}\n${2:Demo content}\n:::" // 或者直接提供 body
        }
    },
    {
        "key": "ctrl+shift+t", // 示例快捷键
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && editorLangId == markdown",
        "args": { 
            // "name": "Tabs" 
            "snippet": ":::tabs${1: key:example}\n== ${2:Tab 1}\n${3:Content 1}\n== ${4:Tab 2}\n${5:Content 2}\n:::"
        }
    }
    // 您可以为其他常用片段添加更多快捷键
]
```
**注意**: 使用 `"name": "Snippet Name"` 时，应确保该名称与 `.vscode/md.code-snippets` 文件中定义的片段名称 (JSON对象中的键，如 "Demo Block", "Tabs") 完全一致。如果通过 `snippet` 参数直接提供代码体，则无需 `name`。

## 常见问题

### Q: 代码片段没有提示或无法展开？

A:
1.  确保您正在编辑的是 Markdown (`.md`) 文件。
2.  检查 VSCode 设置 (`File > Preferences > Settings`)，搜索 `editor.suggest.showSnippets` 并确保其已启用 (`true`)。
3.  确认代码片段的前缀输入正确，并且按下了正确的展开键 (通常是 `Tab` 或 `Enter`，取决于您的 `editor.tabCompletion` 和 `editor.suggest.insertMode` 设置)。
4.  确认 `.vscode/md.code-snippets` 文件存在于项目根目录，并且其 JSON 格式正确无误。

### Q: 如何添加新的自定义代码片段？

A:
1.  打开项目中的 `.vscode/md.code-snippets` 文件。
2.  仿照现有代码片段的 JSON 格式添加新的条目。每个代码片段是一个键值对，键是片段的名称 (在 VSCode 命令面板中显示)，值是一个包含 `prefix`、`body` 和 `description` 的对象。
    ```json
    "My New Snippet": {
        "prefix": "mynew",
        "body": [
            "This is my new snippet with a ${1:placeholder}.",
            "$0" // $0 表示片段展开后的最终光标位置
        ],
        "description": "A brief description of my new snippet."
    }
    ```
3.  保存文件后，新的代码片段应立即生效。

### Q: 参数占位符 (`${1:text}`) 和光标最终位置 (`$0`) 是什么意思？

A:
-   `${1:text}`: 这是一个参数占位符。`1` 是参数的顺序 (按 `Tab` 跳转)，`text` 是默认显示的提示文本。您可以有 `${2:another}`、`${3:more}` 等。
-   `$0`: 这标记了当用户填写完所有参数并退出片段编辑模式后，光标最终停留的位置。

## 示例工作流

1.  **创建新文档**: 输入 `sidebar-page` (或 `page`) 快速生成包含基本 Frontmatter 和标题的文档框架。
2.  **定义章节**: 若为新的侧边栏根，使用 `sidebar-root` 初始化 `index.md`。
3.  **添加内容**:
    *   使用 `tabs` 或 `stepper` 组织分步内容。
    *   使用 `info`, `warning`, `alert-success` 等提示容器强调重点。
    *   使用 `card-text` 等卡片组件美化链接或小节。
4.  **格式化文本**: 使用 `mark`, `spoiler`, `ruby` 等片段丰富文本表现。
5.  **插入媒体与图表**: 使用 `carousel`, `mermaid`, `bilibili` 等添加富媒体内容。
6.  **代码展示**: 使用 `code-group`, `code-lines`, `magic-move` 清晰展示代码。
7.  **完善文档**: 添加 `todo` 任务列表，使用 `@title` 动态引用标题。

通过熟练使用这些代码片段，您可以显著提高 Markdown 文档的编写效率和规范性！
