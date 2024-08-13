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

1. title

Purpose: Sets the title of the document
Type: String
Example:
yamlCopytitle: Getting Started Guide


2. sidetitle

Purpose: Sets the title displayed in the sidebar (if not set, the title is used)
Type: String
Example:
yamlCopysidetitle: Quick Start


3. sidebarorder

Purpose: Customizes the order of documents and subdirectories in the current directory
Type: Object
Example:
yamlCopysidebarorder:
        index: -1
        introduction: 1
        advanced: 2


4. tagDisplay

Purpose: Enables tag grouping display
Type: Boolean
Example:
yamlCopytagDisplay: true


5. back

Purpose: Customizes the path to return to the parent directory
Type: String
Example:
yamlCopyback: /guide/


6. autoPN

Purpose: Automatically generates previous/next navigation
Type: Boolean
Example:
yamlCopyautoPN: true


7. tagorder

Purpose: Customizes the order of tags
Type: Object
Example:
yamlCopytagorder:
        Basics: 1
        Advanced: 2


8. folderBlackList

Purpose: Specifies the list of folders to exclude from the sidebar
Type: Array
Example:
yamlCopyfolderBlackList:
        - private
        - drafts


9. generateSidebar

Purpose: Includes the current index.md file in the sidebar
Type: Boolean
Default: false
Example:
yamlCopygenerateSidebar: true


10. tag/s

Purpose: Specifies tags for the document (used for tag grouping display). When used in a index.md, It should be tags for taging this folder.
Type: String
Example:
yamlCopytag: Basics


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
