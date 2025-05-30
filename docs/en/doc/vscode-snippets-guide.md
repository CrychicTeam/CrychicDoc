---
title: VSCode Snippets Guide
description: Complete guide for using VSCode Markdown snippets in the CrychicDoc project
---

# VSCode Snippets Guide

This guide covers how to use the enhanced VSCode snippets in the CrychicDoc project for more efficient Markdown writing.

## Quick Start

When editing `.md` files in VSCode, type the prefixes below and press `Tab` to expand the code snippets.

## Basic Utilities

| Prefix     | Description                 | Output                     |
| ---------- | --------------------------- | -------------------------- |
| `#nbsp`    | Insert non-breaking spaces  | `&nbsp;&nbsp;`             |
| `@title`   | Frontmatter title reference | `{{ $frontmatter.title }}` |
| `@done`    | Done checkbox               | `☑`                        |
| `@pending` | Pending checkbox            | `☐`                        |

## Frontmatter Configuration

### Basic Fields

| Prefix        | Description          |
| ------------- | -------------------- |
| `title`       | Document title       |
| `description` | Document description |
| `progress`    | Progress percentage  |
| `authors`     | Authors list         |
| `noguide`     | No guide flag        |
| `noScan`      | Disable scanning     |
| `file`        | File reference       |

### Document States

| Prefix              | Output               |
| ------------------- | -------------------- |
| `state preliminary` | `state: preliminary` |
| `state unfinished`  | `state: unfinished`  |
| `state outdated`    | `state: outdated`    |
| `state renovating`  | `state: renovating`  |

## Navigation Structure

| Prefix      | Description            |
| ----------- | ---------------------- |
| `@root`     | Root navigation config |
| `subDir`    | Subdirectory config    |
| `path`      | Path field             |
| `collapsed` | Collapsed navigation   |
| `@subdir`   | Subdirectory item      |

## Containers and Components

### Tabs and Steps

| Prefix    | Description    | Example                |
| --------- | -------------- | ---------------------- |
| `tabs`    | Tab container  | Supports key binding   |
| `stepper` | Step container | Step-by-step tutorials |

### Alert Boxes

| Prefix    | Type        | Style  |
| --------- | ----------- | ------ |
| `success` | Success     | Green  |
| `info`    | Information | Blue   |
| `warning` | Warning     | Orange |
| `error`   | Error       | Red    |

### Card Components

| Prefix          | Description   | Features           |
| --------------- | ------------- | ------------------ |
| `card-text`     | Text card     | Standard style     |
| `card-flat`     | Flat card     | No shadow          |
| `card-elevated` | Elevated card | With shadow        |
| `card-tonal`    | Tonal card    | Colored background |
| `card-outlined` | Outlined card | Border only        |
| `card-plain`    | Plain card    | Supports nesting   |

## Text Alignment

| Prefix    | Alignment    |
| --------- | ------------ |
| `left`    | Left align   |
| `center`  | Center align |
| `right`   | Right align  |
| `justify` | Justify      |

## Media and Interactive

| Prefix     | Description      | Configuration Options         |
| ---------- | ---------------- | ----------------------------- |
| `carousel` | Image carousel   | Cycle, interval, delimiters   |
| `iframe`   | Embedded webpage | URL, height                   |
| `img-size` | Sized image      | Width x Height                |
| `linkcard` | Link card        | URL, title, description, icon |

## Demo Containers

| Prefix       | Description                |
| ------------ | -------------------------- |
| `@demo`      | Basic demo container       |
| `demo-multi` | Multi-level demo container |

## Text Formatting

| Prefix    | Effect          | Syntax               |
| --------- | --------------- | -------------------- |
| `spoiler` | Spoiler text    | `!!text!!`           |
| `mark`    | Marked text     | `==text==`           |
| `ins`     | Inserted text   | `++text++`           |
| `sub`     | Subscript       | `~text~`             |
| `sup`     | Superscript     | `^text^`             |
| `ruby`    | Ruby annotation | `{Chinese:pinyin}`   |
| `abbr`    | Abbreviation    | `*[ABBR]: Full Form` |

## Task Lists

| Prefix      | Description        |
| ----------- | ------------------ |
| `task`      | Complete task list |
| `todo`      | Unchecked task     |
| `done-task` | Checked task       |

## Templates

| Prefix         | Description                |
| -------------- | -------------------------- |
| `doc-template` | Complete document template |
| `section`      | Section with anchor        |

## Usage Tips

### 1. Quick Access

-   Press `Ctrl+Space` to open IntelliSense
-   Type partial letters of prefix, VSCode will auto-filter

### 2. Parameter Navigation

-   After snippet expansion, press `Tab` to jump between parameters
-   Press `Shift+Tab` to go back to previous parameter
-   Press `Esc` to exit snippet mode

### 3. Nested Usage

```markdown
::: v-info Info
You can use ==marked text== and !!spoiler content!! here
:::
```

### 4. Custom Modifications

To modify snippets, edit the `.vscode/md.code-snippets` file.

## Suggested Keyboard Shortcuts

Recommended keyboard shortcuts for improved efficiency:

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

## Troubleshooting

### Q: Snippets not showing?

A: Ensure you're in a Markdown file and check that `editor.suggest.showSnippets` is `true` in VSCode settings.

### Q: How to add custom snippets?

A: Edit the `.vscode/md.code-snippets` file and add new snippets following the existing format.

### Q: Parameter jumping not working?

A: Make sure to use the `Tab` key rather than arrow keys for parameter navigation.

## Example Workflow

1. **Create new document**: Use `doc-template` to quickly generate document framework
2. **Add content**: Use various container snippets to organize content
3. **Format text**: Use text formatting snippets to highlight key points
4. **Insert media**: Use `carousel` or `iframe` to add rich media content
5. **Enhance document**: Add task lists, alert boxes, etc. for better readability

By mastering these code snippets, you can significantly improve your Markdown writing efficiency!
