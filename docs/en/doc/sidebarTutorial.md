---
layout: doc
title: Sidebar Configuration Tutorial
authors:
  - M1hono
prev:
  text: ts-check Sample
  link: /en/doc\test
next:
  text: Project Collaboration Tutorial
  link: /en/doc\index
---

# VitePress Sidebar Generator Pre-Configuration Guide

This document explains how to configure the VitePress sidebar generator using pre-configuration. By adding specific fields in the front matter of your Markdown files, especially the `index.md` file, you can customize the behavior and display of the sidebar.

## Basic Configuration

At the beginning of each Markdown file, use `---` to separate the front matter section:

```yaml
---
# Add your front matter configuration here
---
```

# Document content starts here

Available front matter fields:

| Field          | Purpose                                      | Type    | Example                                      |
| -------------- | -------------------------------------------- | ------- | -------------------------------------------- |
| `title`        | Set the title of the document                 | String  | `title: Getting Started`                     |
| `sidetitle`    | Set the title displayed in the sidebar        | String  | `sidetitle: Quick Start`                      |
| `sidebarorder` | Customize the order of documents and subfolders in the current directory | Object  | `sidebarorder: \n    index: -1 \n    introduction: 1 \n    advanced: 2` |
| `tagDisplay`   | Enable tag grouping display                   | Boolean | `tagDisplay: true`                           |
| `back`         | Customize the path to navigate back to the parent directory | String  | `back: /guide/`                              |
| `autoPN`       | Automatically generate previous/next navigation | Boolean | `autoPN: true`                               |
| `tagorder`     | Customize the order of tags                   | Object  | `tagorder: \n    Basics: 1 \n    Advanced: 2` |
| `folderBlackList` | Specify a list of folders to exclude from the sidebar | Array   | `folderBlackList: \n    - private \n    - drafts` |
| `generateSidebar` | Include the current `index.md` file in the sidebar | Boolean | `generateSidebar: true`                      |
| `tag`          | Specify a tag for the document (used for tag grouping display) | String  | `tag: Basics`                                 |

Complete example
Here is an example of the front matter configuration for an index.md file that includes all available configurations:

```yaml
---
title: VitePress Guide
sidetitle: User Guide
sidebarorder:
        index: -1
        quickstart: 1
        configuration: 2
        advanced: 3
tagDisplay: true
back: /documentation/
autoPN: true
tagorder:
        Basics: 1
        Configuration: 2
        Advanced: 3
folderBlackList:
        - private
        - drafts
generateSidebar: true
tags: Documentation
---
```

```md
# VitePress Guide

This document will help you get started with VitePress...
Using these front matter configurations, you can have precise control over how your document is displayed in the sidebar, including order, enabling tag grouping, and advanced features like automatic navigation generation.
This Markdown file focuses on how to configure the sidebar generator using pre-configuration. It provides detailed information about each available front matter field.
```
