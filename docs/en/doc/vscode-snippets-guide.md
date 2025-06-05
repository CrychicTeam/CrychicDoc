---
title: VSCode Snippets Guide
description: A comprehensive guide to using VSCode Markdown snippets in the CrychicDoc project.
---

# VSCode Snippets Guide

This guide explains how to use the enhanced VSCode code snippets in the CrychicDoc project to write Markdown documents more efficiently.

## Quick Start

When editing `.md` files in VSCode, type the **prefix** of a snippet and press `Tab` (or `Enter`, depending on your settings) to expand it.

## Snippet Categories

All snippets are categorized by their functionality for easier discovery and use.

---

## Sidebar, Page, & Frontmatter Configuration

These snippets help you quickly generate and configure page frontmatter, as well as the structure and behavior of your sidebars.

### Page & Section Templates

Use these to rapidly scaffold standard page structures or root sections for your sidebar.

| Prefix           | Description                                      |
| ---------------- | ------------------------------------------------ |
| `front`          | Basic Frontmatter block                          |
| `page`           | Complete page template                           |
| `section`        | Complete root section template for sidebar       |
| `sidebar-root`   | (Same as above) Complete root section template |
| `sidebar-page`   | Complete page template within a sidebar        |
| `front-complete` | Complete frontmatter with all common fields    |

### Core Frontmatter Fields

For adding or modifying common frontmatter fields.

| Prefix            | Description                       | Example Output (Illustrative) |
| ----------------- | --------------------------------- | ----------------------------- |
| `title`           | Document title (field generation) | `title: Your Title`           |
| `description`     | Document description (field gen.) | `description: Your Desc`      |
| `layout`          | Document layout (field gen.)      | `layout: doc`                 |
| `tags`            | Document tags (field gen.)        | `tags:\n  - tag1`            |
| `authors`         | Document authors (field gen.)     | `authors:\n  - author1`       |
| `progress`        | Document progress (field gen.)    | `progress: 100`               |
| `noguide`         | No guide flag (field gen.)        | `noguide: true`               |
| `title-val`       | Set document/section title        | `title: Your Title`           |
| `description-val` | Set page description              | `description: Your Desc`      |
| `authors-val`     | Set page authors (array)          | `authors:\n  - user1`         |
| `tags-val`        | Set page tags (array)             | `tags:\n  - tag1`            |
| `progress-val`    | Set completion progress           | `progress: 100`               |

### Sidebar-specific Configuration

Define specific behaviors and attributes for sidebar items.

| Prefix            | Description                               | Example Output                 |
| ----------------- | ----------------------------------------- | ------------------------------ |
| `root`            | Root sidebar config (creates new section) | `root: true...`                |
| `root-layout`     | Root sidebar config with VitePress layout | `root: true...layout: doc`     |
| `dir`             | Basic directory config (for `index.md`)   | `title: Dir Title...`          |
| `root-field`      | Add `root` field                          | `root: true`                   |
| `root-true`       | Set as sidebar root section               | `root: true`                   |
| `root-false`      | Not a sidebar root section                | `root: false`                  |
| `collapsed`       | Add `collapsed` field                     | `collapsed: true`              |
| `collapsed-true`  | Collapsed by default                      | `collapsed: true`              |
| `collapsed-false` | Expanded by default                       | `collapsed: false`             |
| `hidden`          | Add `hidden` field                        | `hidden: true`                 |
| `hidden-true`     | Hide from sidebar                         | `hidden: true`                 |
| `hidden-false`    | Show in sidebar                           | `hidden: false`                |
| `priority-val`    | Set ordering priority                     | `priority: 1`                  |
| `maxdepth-val`    | Maximum nesting depth                     | `maxDepth: 3`                  |
| `layout-doc`      | Document layout (VitePress)               | `layout: doc`                  |
| `layout-home`     | Home page layout (VitePress)              | `layout: home`                 |
| `layout-page`     | Custom page layout (VitePress)            | `layout: page`                 |
| `prev`            | Add `prev` navigation field               | `prev: false`                  |
| `prev-true`       | Enable previous navigation                | `prev: true`                   |
| `prev-false`      | Disable previous navigation               | `prev: false`                  |
| `next`            | Add `next` navigation field               | `next: false`                  |
| `next-true`       | Enable next navigation                    | `next: true`                   |
| `next-false`      | Disable next navigation                   | `next: false`                  |
| `comment-true`    | Enable comments                           | `showComment: true`            |
| `comment-false`   | Disable comments                          | `showComment: false`           |
| `changelog-true`  | Show git changelog                        | `gitChangelog: true`           |
| `changelog-false` | Hide git changelog                        | `gitChangelog: false`          |

### Document States

Mark the current status of a document.

| Prefix             | Description          | Output                 |
| ------------------ | -------------------- | -------------------- |
| `state-preliminary`| Preliminary state    | `state: preliminary` |
| `state-unfinished` | Unfinished state     | `state: unfinished`  |
| `state-outdated`   | Outdated state       | `state: outdated`    |
| `state-renovating` | Renovating state     | `state: renovating`  |
| `state preliminary`| (Same) Preliminary   | `state: preliminary` |
| `state unfinished` | (Same) Unfinished    | `state: unfinished`  |
| `state outdated`   | (Same) Outdated      | `state: outdated`    |
| `state renovating` | (Same) Renovating    | `state: renovating`  |

---

## VitePress Built-in Feature Enhancements

Quickly use Markdown extensions provided by VitePress.

### Admonition Containers

| Prefix           | Description                     |
| ---------------- | ------------------------------- |
| `info`           | VitePress info container        |
| `tip`            | VitePress tip container         |
| `warning`        | VitePress warning container     |
| `danger`         | VitePress danger/error container|
| `details`        | VitePress details container     |
| `custom-container`| Custom type container           |

### Code Features

| Prefix           | Description              |
| ---------------- | ------------------------ |
| `code-group`     | VitePress code group     |
| `code-lines`     | Code block with line numbers |
| `code-highlight` | Code block with line highlighting |

### Math Formulas

| Prefix        | Description       | Example Output            |
| ------------- | ----------------- | ------------------------- |
| `math-inline` | Inline math formula | `$x^2 + y^2 = z^2$`       |
| `math-block`  | Block math formula  | `$$\nx^2 + y^2 = z^2\n$$` |

---

## Extended Components & Styling

Enhance content presentation using custom Vue components or special Markdown syntax.

### Content Organization

| Prefix    | Description       |
| --------- | ----------------- |
| `tabs`    | Tab container     |
| `stepper` | Stepper/progress indicator |
| `timeline`| Timeline plugin   |

### Media & Interaction

| Prefix       | Description             |
| ------------ | ----------------------- |
| `carousel`   | Image carousel          |
| `iframe`     | Embedded iframe         |
| `img-size`   | Image with specific size|
| `linkcard`   | Link card component     |
| `bilibili`   | Bilibili video component|
| `pdf-viewer` | PDF viewer component    |

### Diagrams & Visualization

| Prefix         | Description            |
| -------------- | ---------------------- |
| `mermaid`      | Mermaid diagram        |
| `damage-chart` | Damage chart component |
| `lite-tree`    | File tree structure    |

### Card Styles

| Prefix          | Description   |
| --------------- | ------------- |
| `card-text`     | Text card     |
| `card-flat`     | Flat card     |
| `card-elevated` | Elevated card |
| `card-tonal`    | Tonal card    |
| `card-outlined` | Outlined card |

### Custom Alerts (v-style)

| Prefix          | Description    |
| --------------- | -------------- |
| `alert-success` | Success alert  |
| `alert-info`    | Info alert     |
| `alert-warning` | Warning alert  |
| `alert-error`   | Error alert    |

### Alignment & Layout

| Prefix          | Description         |
| --------------- | ------------------- |
| `align-left`    | Left-aligned content|
| `align-center`  | Center-aligned content|
| `align-right`   | Right-aligned content|
| `align-justify` | Justified content   |
| `demo`          | Demo block container|

### Special Effects

| Prefix       | Description            |
| ------------ | ---------------------- |
| `magic-move` | Magic Move code transition |

---

## Utilities & Text Formatting

### Markdown Extensions

| Prefix    | Description                      | Example Output           |
| --------- | -------------------------------- | ------------------------ |
| `spoiler` | Spoiler/hidden text              | `!!hidden content!!`     |
| `mark`    | Highlighted/marked text          | `==highlighted text==`   |
| `insert`  | Inserted text                    | `++inserted text++`      |
| `sub`     | Subscript text                   | `text~subscript~`        |
| `sup`     | Superscript text                 | `text^superscript^`      |
| `ruby`    | Ruby annotation (phonetic guide) | `{中国:zhōng\|guó}`     |

### Task Lists

| Prefix | Description      | Example Output                |
| ------ | ---------------- | ----------------------------- |
| `todo` | To-do list items | `- [ ] Task\n- [x] Completed` |

### Common Utilities

| Prefix     | Description                        | Output                     |
| ---------- | ---------------------------------- | -------------------------- |
| `#nbsp`    | Insert two non-breaking spaces     | `&nbsp;&nbsp;`             |
| `nbsp`     | Insert one non-breaking space      | `&nbsp;`                   |
| `@title`   | Frontmatter title variable reference| `{{ $frontmatter.title }}` |
| `fm-title` | (Same) Frontmatter title           | `{{ $frontmatter.title }}` |
| `@done`    | Done Unicode checkbox              | `☑`                        |
| `@pending` | Pending Unicode checkbox           | `☐`                        |

---

## Usage Tips

### 1. Quick Access

-   Press `Ctrl+Space` (Windows/Linux) or `Cmd+Space` (macOS) to open IntelliSense; snippets are usually prioritized.
-   Type partial letters of the prefix; VSCode will filter and show matching snippets.

### 2. Parameter Navigation

-   Many snippets include placeholders (e.g., `${1:Placeholder}`). When a snippet expands, the cursor automatically focuses on the first placeholder.
-   Press `Tab` to navigate to the next placeholder.
-   Press `Shift+Tab` to go back to the previous placeholder.
-   Press `Esc` to exit snippet editing mode when all placeholders are filled or if you want to finish early.

### 3. Nested Usage

Most container-type snippets support nesting. For example, you can mark text within an info container:

```markdown
::: info Tip
This is ==marked text== and !!spoiler content!!.
:::
```

### 4. Custom Modifications

To modify or add your own snippets, edit the `.vscode/md.code-snippets` file in the project root. The file format is JSON.

## Suggested Keyboard Shortcuts

For even greater efficiency, you can assign custom keyboard shortcuts to frequently used snippets. Edit your `keybindings.json` file (accessible via `File > Preferences > Keyboard Shortcuts > User > keybindings.json`):

```json
// keybindings.json
[
    {
        "key": "ctrl+shift+d", // Example shortcut
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && editorLangId == markdown",
        "args": { 
            // "name": "Demo Block" // Use the snippet's name (key from JSON)
            "snippet": "::: demo ${1:Demo Title}\n${2:Demo content}\n:::" // Or provide the body directly
        }
    },
    {
        "key": "ctrl+shift+t", // Example shortcut
        "command": "editor.action.insertSnippet",
        "when": "editorTextFocus && editorLangId == markdown",
        "args": { 
            // "name": "Tabs" 
            "snippet": ":::tabs${1: key:example}\n== ${2:Tab 1}\n${3:Content 1}\n== ${4:Tab 2}\n${5:Content 2}\n:::"
        }
    }
    // You can add more shortcuts for other frequently used snippets
]
```
**Note**: When using `"name": "Snippet Name"`, ensure the name matches exactly with the snippet name defined in `.vscode/md.code-snippets` (the key in the JSON object, e.g., "Demo Block", "Tabs"). If providing the snippet body directly via the `snippet` argument, the `name` is not needed.

## Troubleshooting

### Q: Snippets are not showing up or expanding?

A:
1.  Ensure you are editing a Markdown (`.md`) file.
2.  Check your VSCode settings (`File > Preferences > Settings`), search for `editor.suggest.showSnippets`, and ensure it is enabled (`true`).
3.  Confirm you are typing the correct snippet prefix and pressing the correct expansion key (usually `Tab` or `Enter`, depending on your `editor.tabCompletion` and `editor.suggest.insertMode` settings).
4.  Verify that the `.vscode/md.code-snippets` file exists in your project root and that its JSON format is valid.

### Q: How do I add new custom snippets?

A:
1.  Open the `.vscode/md.code-snippets` file in your project.
2.  Add a new entry following the JSON format of existing snippets. Each snippet is a key-value pair: the key is the snippet's name (displayed in VSCode command palette), and the value is an object containing `prefix`, `body`, and `description`.
    ```json
    "My New Snippet": {
        "prefix": "mynew",
        "body": [
            "This is my new snippet with a ${1:placeholder}.",
            "$0" // $0 indicates the final cursor position after snippet expansion
        ],
        "description": "A brief description of my new snippet."
    }
    ```
3.  Save the file. The new snippet should be available immediately.

### Q: What do parameter placeholders (`${1:text}`) and final cursor position (`$0`) mean?

A:
-   `${1:text}`: This is a parameter placeholder. `1` is the order of the parameter (navigate with `Tab`), and `text` is the default placeholder text displayed. You can have `${2:another}`, `${3:more}`, etc.
-   `$0`: This marks the final position of the cursor after the user has filled in all parameters and exited snippet editing mode.

## Example Workflow

1.  **Create New Document**: Type `sidebar-page` (or `page`) to quickly generate a document framework with basic frontmatter and a title.
2.  **Define Section**: If it's a new sidebar root, use `sidebar-root` to initialize its `index.md`.
3.  **Add Content**:
    *   Use `tabs` or `stepper` to organize step-by-step content.
    *   Use `info`, `warning`, `alert-success`, etc., containers to emphasize key points.
    *   Use `card-text` and other card components to enhance links or small sections.
4.  **Format Text**: Use `mark`, `spoiler`, `ruby`, etc., snippets to enrich text presentation.
5.  **Insert Media & Diagrams**: Use `carousel`, `mermaid`, `bilibili`, etc., to add rich media content.
6.  **Showcase Code**: Use `code-group`, `code-lines`, `magic-move` for clear code presentations.
7.  **Refine Document**: Add `todo` task lists, use `@title` to dynamically reference the page title.

By mastering these code snippets, you can significantly improve the efficiency and consistency of your Markdown document writing!
