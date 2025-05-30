---
title: VSCode 代码片段使用指南
description: CrychicDoc 项目中的 VSCode Markdown 代码片段完整使用指南
---

# VSCode 代码片段使用指南

本指南介绍如何使用 CrychicDoc 项目中的增强型 VSCode 代码片段，让您更高效地编写 Markdown 文档。

## 快速开始

在 VSCode 中编辑 `.md` 文件时，输入下面的前缀并按 `Tab` 键即可展开代码片段。

## 基础工具

| 前缀       | 描述           | 输出                       |
| ---------- | -------------- | -------------------------- |
| `#nbsp`    | 插入不间断空格 | `&nbsp;&nbsp;`             |
| `@title`   | 前端标题引用   | `{{ $frontmatter.title }}` |
| `@done`    | 完成复选框     | `☑`                        |
| `@pending` | 待办复选框     | `☐`                        |

## 文档前端配置

### 基础字段

| 前缀          | 描述       |
| ------------- | ---------- |
| `title`       | 文档标题   |
| `description` | 文档描述   |
| `progress`    | 进度百分比 |
| `authors`     | 作者列表   |
| `noguide`     | 无指南标志 |
| `noScan`      | 禁用扫描   |
| `file`        | 文件引用   |

### 文档状态

| 前缀                | 输出                 |
| ------------------- | -------------------- |
| `state preliminary` | `state: preliminary` |
| `state unfinished`  | `state: unfinished`  |
| `state outdated`    | `state: outdated`    |
| `state renovating`  | `state: renovating`  |

## 导航结构

| 前缀        | 描述       |
| ----------- | ---------- |
| `@root`     | 根导航配置 |
| `subDir`    | 子目录配置 |
| `path`      | 路径字段   |
| `collapsed` | 折叠导航   |
| `@subdir`   | 子目录项目 |

## 容器和组件

### 选项卡和步骤

| 前缀      | 描述       | 示例         |
| --------- | ---------- | ------------ |
| `tabs`    | 选项卡容器 | 支持键值绑定 |
| `stepper` | 步骤组     | 逐步教程     |

### 警告框

| 前缀      | 类型 | 样式 |
| --------- | ---- | ---- |
| `success` | 成功 | 绿色 |
| `info`    | 信息 | 蓝色 |
| `warning` | 警告 | 橙色 |
| `error`   | 错误 | 红色 |

### 卡片组件

| 前缀            | 描述     | 特点     |
| --------------- | -------- | -------- |
| `card-text`     | 文本卡片 | 标准样式 |
| `card-flat`     | 扁平卡片 | 无阴影   |
| `card-elevated` | 浮起卡片 | 有阴影   |
| `card-tonal`    | 色调卡片 | 彩色背景 |
| `card-outlined` | 轮廓卡片 | 仅边框   |
| `card-plain`    | 简单卡片 | 支持嵌套 |

## 文本对齐

| 前缀      | 对齐方式 |
| --------- | -------- |
| `left`    | 左对齐   |
| `center`  | 居中     |
| `right`   | 右对齐   |
| `justify` | 两端对齐 |

## 媒体和交互

| 前缀       | 描述         | 配置选项              |
| ---------- | ------------ | --------------------- |
| `carousel` | 图片轮播     | 循环、间隔、分隔符    |
| `iframe`   | 内嵌网页     | 链接、高度            |
| `img-size` | 指定尺寸图片 | 宽度 x 高度           |
| `linkcard` | 链接卡片     | URL、标题、描述、图标 |

## 演示容器

| 前缀         | 描述         |
| ------------ | ------------ |
| `@demo`      | 基础演示容器 |
| `demo-multi` | 多层演示容器 |

## 文本格式化

| 前缀      | 效果     | 语法                |
| --------- | -------- | ------------------- |
| `spoiler` | 隐藏文本 | `!!文本!!`          |
| `mark`    | 标记文本 | `==文本==`          |
| `ins`     | 插入文本 | `++文本++`          |
| `sub`     | 下标     | `~文本~`            |
| `sup`     | 上标     | `^文本^`            |
| `ruby`    | 注音     | `{汉字:读音}`       |
| `abbr`    | 缩写     | `*[缩写]: 完整形式` |

## 任务列表

| 前缀        | 描述         |
| ----------- | ------------ |
| `task`      | 完整任务列表 |
| `todo`      | 未完成任务   |
| `done-task` | 已完成任务   |

## 模板

| 前缀           | 描述         |
| -------------- | ------------ |
| `doc-template` | 完整文档模板 |
| `section`      | 带锚点的章节 |

## 使用技巧

### 1. 快速访问

-   按 `Ctrl+空格` 打开智能提示
-   输入前缀的部分字母，VSCode 会自动过滤

### 2. 参数填写

-   代码片段展开后，按 `Tab` 在参数间跳转
-   按 `Shift+Tab` 返回上一个参数
-   按 `Esc` 退出片段模式

### 3. 嵌套使用

```markdown
::: v-info 提示
这里可以使用 ==标记文本== 和 !!隐藏内容!!
:::
```

### 4. 自定义修改

如需修改代码片段，编辑 `.vscode/md.code-snippets` 文件。

## 键盘快捷键建议

建议在 VSCode 中设置以下快捷键以提高效率：

```json
// keybindings.json
[
    {
        "key": "ctrl+shift+d",
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && editorLangId == markdown",
        "args": { "name": "Demo Container" }
    },
    {
        "key": "ctrl+shift+t",
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && editorLangId == markdown",
        "args": { "name": "Tabs Container" }
    }
]
```

## 常见问题

### Q: 代码片段没有提示？

A: 确保在 Markdown 文件中，并检查 VSCode 设置中的 `editor.suggest.showSnippets` 为 `true`。

### Q: 如何添加自定义片段？

A: 编辑 `.vscode/md.code-snippets` 文件，按照现有格式添加新的代码片段。

### Q: 参数跳转不工作？

A: 确保使用 `Tab` 键而不是方向键进行参数间跳转。

## 示例工作流

1. **创建新文档**：输入 `doc-template` 快速生成文档框架
2. **添加内容**：使用各种容器片段组织内容
3. **格式化文本**：使用文本格式化片段突出重点
4. **插入媒体**：使用 `carousel` 或 `iframe` 添加富媒体内容
5. **完善文档**：添加任务列表、警告框等增强可读性

通过熟练使用这些代码片段，您可以显著提高 Markdown 文档的编写效率！
