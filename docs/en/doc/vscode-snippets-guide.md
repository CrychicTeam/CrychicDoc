---
title: VSCode Snippets Guide
description: A comprehensive guide to using VSCode Markdown snippets in the CrychicDoc project.
---

# VSCode Snippets Guide

This guide explains how to use the enhanced VSCode code snippets in the CrychicDoc project to write Markdown documents more efficiently.

## Quick Start

When editing `.md` files in VSCode, type the **prefix** of a snippet (e.g., `@page-template`) and press `Tab` or `Enter` to expand it.

## Snippet Categories

All snippets have been reorganized and standardized for clarity and ease of use. They are grouped into the following categories:

-   **VitePress Frontmatter & Page Templates**: For scaffolding pages and configuring metadata.
-   **VitePress Built-in Markdown Features**: For VitePress-specific containers and code blocks.
-   **Custom Plugins & Components**: For custom-developed components like Dialogs, Alerts, and Carousels.
-   **Markdown Utilities**: For standard Markdown elements like headings and tables.
-   **LLM Content Utilities**: For adding context or instructions visible only to Language Models.

---

## VitePress Frontmatter & Page Templates

Quickly generate page structures and configure frontmatter metadata.

### Page & Section Templates

| Prefix | Description |
| :--- | :--- |
| `@page-template` | Scaffolds a standard VitePress documentation page. |
| `@root-template` | Scaffolds a new root section for the sidebar. |
| `@frontmatter` | Inserts a basic frontmatter block (`---`). |
| `@frontmatter-complete` | Generates a comprehensive frontmatter block with all common fields. |

### Individual Frontmatter Fields

All frontmatter field snippets now use the `@fm-` prefix for consistency.

| Prefix | Description | Example Output |
| :--- | :--- | :--- |
| `@fm-title` | Page title. | `title: Page Title` |
| `@fm-description` | Page description. | `description: Page description` |
| `@fm-root` | Whether the page is a sidebar root. | `root: true` |
| `@fm-collapsed` | Whether the sidebar section is collapsed. | `collapsed: true` |
| `@fm-hidden` | Hides the page from the sidebar. | `hidden: true` |
| `@fm-layout` | Page layout (`doc`, `home`, `page`). | `layout: doc` |
| `@fm-prevnext` | Sets previous/next page navigation links. | `prev: false` `next: false` |
| `@fm-authors` | Page authors. | `authors:\n  - author` |
| `@fm-tags` | Page tags. | `tags:\n  - tag` |
| `@fm-progress` | Page completion progress. | `progress: 100` |
| `@fm-state` | Document's state (`preliminary`, `published`, etc.). | `state: preliminary` |

---

## VitePress Built-in Markdown Features

Quickly use Markdown extensions provided by VitePress. All these snippets now use the `@vp-` prefix.

### Admonition Containers

| Prefix | Description |
| :--- | :--- |
| `@vp-info` | VitePress info container. |
| `@vp-tip` | VitePress tip container. |
| `@vp-warning` | VitePress warning container. |
| `@vp-danger` | VitePress danger/error container. |
| `@vp-details` | VitePress details (collapsible) container. |

### Code Features

| Prefix | Description |
| :--- | :--- |
| `@vp-codegroup` | VitePress code group for tabbed code blocks. |
| `@vp-codelines` | Code block with line numbers. |
| `@vp-codehighlight` | Code block with specific lines highlighted. |

---

## Custom Plugins & Components

Snippets for custom Vue components and Markdown plugins developed for this project.

### Component Snippets

Comprehensive snippets for all the interactive components available in CrychicDoc.

#### Mermaid Diagrams

| Prefix | Description |
| :--- | :--- |
| `@mermaid-flowchart` | Flowchart diagram with decision nodes. |
| `@mermaid-journey` | User journey diagram for process flows. |
| `@mermaid-sequence` | Sequence diagram for interactions. |
| `@mermaid-gantt` | Gantt chart for project timelines. |
| `@mermaid-class` | Class diagram for object relationships. |
| `@mermaid-state` | State diagram for state machines. |

#### Timeline Plugin

| Prefix | Description |
| :--- | :--- |
| `@timeline` | Single timeline entry with date and events. |
| `@timeline-multiple` | Multiple timeline entries for chronological content. |

#### Video Components

| Prefix | Description |
| :--- | :--- |
| `@bilibili` | Bilibili video embed component. |
| `@youtube` | YouTube video embed component. |

#### Damage Chart Component

| Prefix | Description |
| :--- | :--- |
| `@damage-chart-static` | Static Minecraft damage chart for documentation. |
| `@damage-chart-interactive` | Interactive damage chart with user controls. |
| `@damage-chart-full` | Full-featured damage chart with all options. |

#### Media & Documentation Components

| Prefix | Description |
| :--- | :--- |
| `@pdf-viewer` | PDF viewer component for document embedding. |
| `@linkcard` | Link card component for external links. |
| `@linkcard-full` | Full-featured link card with all properties. |
| `@contributors` | GitHub contributors component. |
| `@contributors-advanced` | Advanced contributors with custom title and locale. |
| `@commits-counter` | GitHub commits counter component. |
| `@responsible-editor` | Responsible editor component (uses frontmatter). |
| `@comment` | Comment section component (Giscus integration). |

#### Enhanced Plugin Components

| Prefix | Description |
| :--- | :--- |
| `@carousel-simple` | Simple image carousel with default settings. |
| `@carousel-advanced` | Advanced carousel with full configuration options. |
| `@stepper` | Basic step-by-step guide component. |
| `@stepper-advanced` | Advanced stepper with detailed content and code blocks. |
| `@iframe` | Basic embedded iframe component. |
| `@iframe-advanced` | Advanced iframe with width and height configuration. |

#### Demo Block Combinations

| Prefix | Description |
| :--- | :--- |
| `@demo-mermaid` | Demo block containing a Mermaid diagram. |
| `@demo-timeline` | Demo block containing timeline components. |
| `@demo-video` | Demo block containing video components. |
| `@demo-chart` | Demo block containing damage chart components. |

### Dialog Plugin

| Prefix | Description |
| :--- | :--- |
| `@dialog-def` | Creates a dialog **definition** block. Content inside is rendered as Markdown. |
| `@dialog-trigger` | Creates an inline **trigger** link for a dialog. |
| `@dialog-full` | Creates a complete definition and a trigger inside a demo block for testing. |

### Alert Plugin

The project supports both legacy v-alert format and the new CustomAlert with JSON configuration.

#### Legacy v-alert Format

| Prefix | Description |
| :--- | :--- |
| `@alert` | Generic alert container with a dropdown to select type (`success`, `info`, etc.). |
| `@alert-success` | Success alert. |
| `@alert-info` | Info alert. |
| `@alert-warning` | Warning alert. |
| `@alert-error` | Error alert. |

#### CustomAlert with JSON Configuration

**Complete Alert Templates:**

| Prefix | Description |
| :--- | :--- |
| `@custom-alert` | Generic custom alert with type and title selection. |
| `@custom-alert-success` | Quick success alert with JSON config. |
| `@custom-alert-info` | Quick info alert with JSON config. |
| `@custom-alert-warning` | Quick warning alert with JSON config. |
| `@custom-alert-error` | Quick error alert with JSON config. |
| `@custom-alert-advanced` | Alert with variant and density options. |
| `@custom-alert-styled` | Alert with border and color styling. |
| `@custom-alert-themed` | Alert with light/dark theme colors. |
| `@custom-alert-icon` | Alert with custom icon. |
| `@custom-alert-full` | Full-featured alert with all configuration options. |
| `@custom-alert-minimal` | Minimal alert with only type specified. |

**Single Configuration Properties:**

| Prefix | Description |
| :--- | :--- |
| `@alert-config-type` | Alert type property (`success`, `info`, `warning`, `error`). |
| `@alert-config-title` | Alert title property. |
| `@alert-config-text` | Alert text content property. |
| `@alert-config-variant` | Alert variant property (`flat`, `elevated`, `tonal`, etc.). |
| `@alert-config-density` | Alert density property (`default`, `comfortable`, `compact`). |
| `@alert-config-border` | Alert border property (`start`, `end`, `top`, `bottom`, `true`, `false`). |
| `@alert-config-color` | Alert custom color property. |
| `@alert-config-light-color` | Alert light theme color property. |
| `@alert-config-dark-color` | Alert dark theme color property. |
| `@alert-config-theme-colors` | Both light and dark theme colors. |
| `@alert-config-icon` | Alert custom icon property. |

### Other Components

| Prefix | Description |
| :--- | :--- |
| `@carousel` | Image carousel plugin. |
| `@iframe` | Embedded iframe plugin. |
| `@stepper` | Step-by-step guide component. |
| `@file-tree` | File tree structure component. |
| `@linkcard` | Link card component. |

---

## Markdown Utilities

Snippets for standard Markdown syntax and text formatting.

| Prefix | Description |
| :--- | :--- |
| `@h1`, `@h2`, `@h3`, `@h4` | Headings level 1 to 4. |
| `@table` | Inserts a 2x2 Markdown table. |
| `@toc` | Inserts a Table of Contents with anchor links. |
| `@demo` | Demo block container for showcasing examples. |
| `@spoiler` | Hidden/spoiler text (`!!text!!`). |
| `@mark` | Highlighted (marked) text (`==text==`). |
| `@insert` | Inserted text (`++text++`). |
| `@sub` | Subscript text. |
| `@sup` | Superscript text. |

---

## LLM Content Utilities

Snippets for adding context or instructions that are only visible to Language Models.

| Prefix | Description |
| :--- | :--- |
| `@llm-only` | Block content visible only to LLMs. |
| `@llm-exclude` | Block content invisible to LLMs. |
| `@llm-instructions` | AI assistant instructions block. |
| `@llm-context` | Provide contextual information for LLMs. |

---

## Usage Tips

-   **Parameter Navigation**: Use `Tab` and `Shift+Tab` to navigate between placeholders (`${1:text}`) in an expanded snippet. Press `Esc` to exit editing mode.
-   **Customization**: To modify snippets, edit the `.vscode/md.code-snippets` file.

## Example Workflow

1.  **Create Page**: Type `@page-template` to scaffold a new page.
2.  **Add Dialog**:
    *   Define a dialog's content using `@dialog-def`.
    *   Create a trigger link elsewhere in the text with `@dialog-trigger`.
3.  **Add Content**:
    *   Use `@stepper` to organize a tutorial.
    *   Use `@vp-info`, `@vp-warning`, or `@alert-success` to emphasize key points.
4.  **Showcase Code**: Use `@vp-codegroup` for multi-language examples.
5.  **Format Text**: Use `@mark` and `@spoiler` to enrich the text.

By mastering these snippets, you can significantly improve the efficiency and consistency of your Markdown documentation.
