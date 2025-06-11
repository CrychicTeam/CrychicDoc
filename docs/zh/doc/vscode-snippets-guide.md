---
title: VSCode 代码片段使用指南
description: CrychicDoc 项目中的 VSCode Markdown 代码片段完整使用指南
---

# VSCode 代码片段使用指南

本指南解释了如何使用 CrychicDoc 项目中经过优化的 VSCode 代码片段，以更高效地编写 Markdown 文档。

## 快速开始

在 VSCode 中编辑 `.md` 文件时，输入代码片段的 **前缀** (例如 `@page-template`) 并按 `Tab` 或 `Enter` 键即可将其展开。

## 代码片段分类

所有代码片段都经过了重新组织和标准化，以便于查找和使用。它们被分为以下几类：

-   **VitePress Frontmatter 与页面模板**: 用于快速搭建页面和配置元数据。
-   **VitePress 内置 Markdown 功能**: 用于 VitePress 特有的容器和代码块。
-   **自定义插件与组件**: 用于项目中开发的自定义组件，如对话框、警告框等。
-   **Markdown 通用工具**: 用于标准的 Markdown 元素，如标题和表格。
-   **LLM 内容工具**: 用于添加仅语言模型可见的上下文或指令。

---

## VitePress Frontmatter 与页面模板

快速生成页面结构并配置 Frontmatter 元数据。

### 页面与章节模板

| 前缀 | 描述 |
| :--- | :--- |
| `@page-template` | 快速搭建一个标准的 VitePress 文档页面。 |
| `@root-template` | 快速搭建一个侧边栏的新根章节。 |
| `@frontmatter` | 插入一个基础的 Frontmatter 块 (`---`)。 |
| `@frontmatter-complete` | 生成一个包含所有常用字段的完整 Frontmatter 块。 |

### 单独的 Frontmatter 字段

所有 Frontmatter 字段的代码片段现在都使用 `@fm-` 前缀以保持一致性。

| 前缀 | 描述 | 输出示例 |
| :--- | :--- | :--- |
| `@fm-title` | 页面标题。 | `title: Page Title` |
| `@fm-description` | 页面描述。 | `description: Page description` |
| `@fm-root` | 页面是否为侧边栏根节点。 | `root: true` |
| `@fm-collapsed` | 侧边栏该章节是否默认折叠。 | `collapsed: true` |
| `@fm-hidden` | 从侧边栏中隐藏此页面。 | `hidden: true` |
| `@fm-layout` | 页面布局 (`doc`, `home`, `page`)。 | `layout: doc` |
| `@fm-prevnext` | 设置上一页/下一页导航链接。 | `prev: false` `next: false` |
| `@fm-authors` | 页面作者。 | `authors:\n  - author` |
| `@fm-tags` | 页面标签。 | `tags:\n  - tag` |
| `@fm-progress` | 页面完成进度。 | `progress: 100` |
| `@fm-state` | 文档状态 (`preliminary`, `published` 等)。 | `state: preliminary` |

---

## VitePress 内置 Markdown 功能

快速使用 VitePress 提供的 Markdown 扩展。所有这些代码片段现在都使用 `@vp-` 前缀。

### 提示容器

| 前缀 | 描述 |
| :--- | :--- |
| `@vp-info` | VitePress 信息容器。 |
| `@vp-tip` | VitePress 提示容器。 |
| `@vp-warning` | VitePress 警告容器。 |
| `@vp-danger` | VitePress 危险/错误容器。 |
| `@vp-details` | VitePress 可折叠详情容器。 |

### 代码功能

| 前缀 | 描述 |
| :--- | :--- |
| `@vp-codegroup` | VitePress 代码组，用于选项卡式代码块。 |
| `@vp-codelines` | 带行号的代码块。 |
| `@vp-codehighlight` | 特定行高亮的代码块。 |

---

## 自定义插件与组件

为本项目开发的自定义 Vue 组件和 Markdown 插件提供的代码片段。

### 组件代码片段

CrychicDoc 中所有交互式组件的完整代码片段集合。

#### Mermaid 图表

| 前缀 | 描述 |
| :--- | :--- |
| `@mermaid-flowchart` | 带决策节点的流程图。 |
| `@mermaid-journey` | 用于流程的用户旅程图。 |
| `@mermaid-sequence` | 用于交互的时序图。 |
| `@mermaid-gantt` | 项目时间线的甘特图。 |
| `@mermaid-class` | 对象关系的类图。 |
| `@mermaid-state` | 状态机的状态图。 |

#### 时间线插件

| 前缀 | 描述 |
| :--- | :--- |
| `@timeline` | 带日期和事件的单个时间线条目。 |
| `@timeline-multiple` | 用于时间顺序内容的多个时间线条目。 |

#### 视频组件

| 前缀 | 描述 |
| :--- | :--- |
| `@bilibili` | B站视频嵌入组件。 |
| `@youtube` | YouTube 视频嵌入组件。 |

#### 伤害图表组件

| 前缀 | 描述 |
| :--- | :--- |
| `@damage-chart-static` | 用于文档的静态 Minecraft 伤害图表。 |
| `@damage-chart-interactive` | 带用户控制的交互式伤害图表。 |
| `@damage-chart-full` | 包含所有选项的全功能伤害图表。 |

#### 媒体与文档组件

| 前缀 | 描述 |
| :--- | :--- |
| `@pdf-viewer` | 用于文档嵌入的 PDF 查看器组件。 |
| `@linkcard` | 外部链接的链接卡片组件。 |
| `@linkcard-full` | 包含所有属性的全功能链接卡片。 |
| `@contributors` | GitHub 贡献者组件。 |
| `@contributors-advanced` | 带自定义标题和语言的高级贡献者组件。 |
| `@commits-counter` | GitHub 提交计数器组件。 |
| `@responsible-editor` | 责任编辑组件 (使用 frontmatter)。 |
| `@comment` | 评论区组件 (Giscus 集成)。 |

#### 增强插件组件

| 前缀 | 描述 |
| :--- | :--- |
| `@carousel-simple` | 使用默认设置的简单图片轮播。 |
| `@carousel-advanced` | 包含完整配置选项的高级轮播。 |
| `@stepper` | 基础步骤指南组件。 |
| `@stepper-advanced` | 包含详细内容和代码块的高级步骤器。 |
| `@iframe` | 基础内嵌 iframe 组件。 |
| `@iframe-advanced` | 带宽度和高度配置的高级 iframe。 |

#### Demo 块组合

| 前缀 | 描述 |
| :--- | :--- |
| `@demo-mermaid` | 包含 Mermaid 图表的 Demo 块。 |
| `@demo-timeline` | 包含时间线组件的 Demo 块。 |
| `@demo-video` | 包含视频组件的 Demo 块。 |
| `@demo-chart` | 包含伤害图表组件的 Demo 块。 |

### 对话框插件 (Dialog Plugin)

| 前缀 | 描述 |
| :--- | :--- |
| `@dialog-def` | 创建一个对话框 **定义** 块。内部内容会作为 Markdown 渲染。 |
| `@dialog-trigger` | 创建一个用于触发对话框的内联 **触发器** 链接。 |
| `@dialog-full` | 创建一个完整的定义块和一个在 demo 块中的触发器，用于快速测试。 |

### 警告框插件 (Alert Plugin)

项目支持传统v-alert格式和新的带JSON配置的CustomAlert。

#### 传统v-alert格式

| 前缀 | 描述 |
| :--- | :--- |
| `@alert` | 通用警告框容器，可通过下拉菜单选择类型 (`success`, `info` 等)。 |
| `@alert-success` | 成功警告框。 |
| `@alert-info` | 信息警告框。 |
| `@alert-warning` | 警告警告框。 |
| `@alert-error` | 错误警告框。 |

#### 带JSON配置的CustomAlert

**完整警告框模板：**

| 前缀 | 描述 |
| :--- | :--- |
| `@custom-alert` | 通用自定义警告框，可选择类型和标题。 |
| `@custom-alert-success` | 带JSON配置的快速成功警告框。 |
| `@custom-alert-info` | 带JSON配置的快速信息警告框。 |
| `@custom-alert-warning` | 带JSON配置的快速警告警告框。 |
| `@custom-alert-error` | 带JSON配置的快速错误警告框。 |
| `@custom-alert-advanced` | 带变体和密度选项的警告框。 |
| `@custom-alert-styled` | 带边框和颜色样式的警告框。 |
| `@custom-alert-themed` | 带亮色/暗色主题颜色的警告框。 |
| `@custom-alert-icon` | 带自定义图标的警告框。 |
| `@custom-alert-full` | 全功能警告框，包含所有配置选项。 |
| `@custom-alert-minimal` | 仅指定类型的最小警告框。 |

**单一配置属性：**

| 前缀 | 描述 |
| :--- | :--- |
| `@alert-config-type` | 警告框类型属性 (`success`, `info`, `warning`, `error`)。 |
| `@alert-config-title` | 警告框标题属性。 |
| `@alert-config-text` | 警告框文本内容属性。 |
| `@alert-config-variant` | 警告框变体属性 (`flat`, `elevated`, `tonal` 等)。 |
| `@alert-config-density` | 警告框密度属性 (`default`, `comfortable`, `compact`)。 |
| `@alert-config-border` | 警告框边框属性 (`start`, `end`, `top`, `bottom`, `true`, `false`)。 |
| `@alert-config-color` | 警告框自定义颜色属性。 |
| `@alert-config-light-color` | 警告框亮色主题颜色属性。 |
| `@alert-config-dark-color` | 警告框暗色主题颜色属性。 |
| `@alert-config-theme-colors` | 亮色和暗色主题颜色。 |
| `@alert-config-icon` | 警告框自定义图标属性。 |

### 其他组件

| 前缀 | 描述 |
| :--- | :--- |
| `@carousel` | 图片轮播插件。 |
| `@iframe` | 内嵌 iframe 插件。 |
| `@stepper` | 步骤指示器组件。 |
| `@file-tree` | 文件树结构组件。 |
| `@linkcard` | 链接卡片组件。 |

---

## Markdown 通用工具

用于标准 Markdown 语法和文本格式化的代码片段。

| 前缀 | 描述 |
| :--- | :--- |
| `@h1`, `@h2`, `@h3`, `@h4` | 1 至 4 级标题。 |
| `@table` | 插入一个 2x2 的 Markdown 表格。 |
| `@toc` | 插入一个带锚点链接的目录。 |
| `@demo` | 用于展示示例的 Demo 块容器。 |
| `@spoiler` | 隐藏/剧透文本 (`!!text!!`)。 |
| `@mark` | 高亮 (标记) 文本 (`==text==`)。 |
| `@insert` | 插入的文本 (`++text++`)。 |
| `@sub` | 下标文本。 |
| `@sup` | 上标文本。 |

---

## LLM 内容工具

用于添加仅语言模型可见的上下文或指令的代码片段。

| 前缀 | 描述 |
| :--- | :--- |
| `@llm-only` | 仅 LLM 可见的内容块。 |
| `@llm-exclude` | LLM 不可见的内容块 (从上下文中排除)。 |
| `@llm-instructions` | AI 助手指令块。 |
| `@llm-context` | 为 LLM 提供上下文信息。 |

---

## 使用技巧

-   **参数导航**: 在展开的代码片段中，使用 `Tab` 和 `Shift+Tab` 在占位符 (`${1:text}`) 之间导航。按 `Esc` 退出编辑模式。
-   **自定义**: 如需修改代码片段，请编辑 `.vscode/md.code-snippets` 文件。

## 示例工作流

1.  **创建页面**: 输入 `@page-template` 快速搭建一个新页面。
2.  **添加对话框**:
    *   使用 `@dialog-def` 定义对话框的内容。
    *   在文档的其他地方使用 `@dialog-trigger` 创建触发链接。
3.  **添加内容**:
    *   使用 `@stepper` 组织教程步骤。
    *   使用 `@vp-info`、`@vp-warning` 或 `@alert-success` 强调关键点。
4.  **展示代码**: 使用 `@vp-codegroup` 来展示多语言示例。
5.  **格式化文本**: 使用 `@mark` 和 `@spoiler` 丰富文本。

通过熟练使用这些代码片段，您可以显著提高编写 Markdown 文档的效率和一致性。
