---
title: Cooperation Guide
progress: 100
description: This article provides documentation writing standards for this site!
---

# Documentation Writing Standards

This document explains the standards for sidebar, file structure, and collaboration in this documentation project, helping you understand how to efficiently collaborate on writing documentation and minimize conflicts caused by non-standard cooperation.

First, you need to understand how to start collaborating through [this article](./cooperation.md).

## Project Structure {#FileStructure}

The documentation currently maintains `Chinese/English` bilingual content, with Chinese as the primary language.

:::alert {"type": "info", "title": "Project Structure Overview"}
Here's the complete project structure with file purposes and status indicators.
:::

<LiteTree>
// Define status and type styles
#config=color:white;background:#1976d2;padding:2px 6px;border-radius:3px;font-size:12px;
#content=color:white;background:#4caf50;padding:2px 6px;border-radius:3px;font-size:12px;
#script=color:white;background:#ff9800;padding:2px 6px;border-radius:3px;font-size:12px;
#ignore=color:#666;background:#f5f5f5;padding:2px 6px;border-radius:3px;font-size:12px;
.important=font-weight:bold;color:#d32f2f;
.folder=color:#1976d2;font-weight:500;
// Define icons
folder=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTEwIDRIOGEyIDIgMCAwIDAtMiAydjEyYTIgMiAwIDAgMCAyIDJoOGEyIDIgMCAwIDAgMi0yVjhhMiAyIDAgMCAwLTItMmgtM2wtMi0yWiIvPjwvc3ZnPg==
ts=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMTUgMTUiPjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzMxNzhDNiIgZD0iTTEyLjUgOHYtLjE2N2MwLS43MzYtLjU5Ny0xLjMzMy0xLjMzMy0xLjMzM0gxMGExLjUgMS41IDAgMSAwIDAgM2gxYTEuNSAxLjUgMCAwIDEgMCAzaC0xQTEuNSAxLjUgMCAwIDEgOC41IDExTTggNi41SDNtMi41IDBWMTNNMS41LjVoMTN2MTRIOS41eiIvPjwvc3ZnPg==
js=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9IiNmN2RmMWUiIGQ9Ik0zIDNoMTh2MThIM1ptMTYuNTI1IDE0LjVjLS4zLS4zNTQtLjc5NS0uNjI5LTEuNzE3LS42MjljLS44ODEgMC0xLjQzOS4zMTgtMS40MzkuNzE4YzAgLjM5Ni4zNzMuNjM3IDEuMTU2Ljk2N2MxLjMzMi41ODYgMi4yODEgMS4wOTMgMi4yODEgMi4zOGMwIDEuMzItMS4yMDMgMi4xNDMtMi45NzQgMi4xNDNjLTEuMjEzIDAtMi4yNzEtLjQ2Mi0yLjk1LTEuMDc0bC44NzUtMS4yNzNjLjQzMy4zODkgMS4wNjQuNzI0IDEuNjY0LjcyNGMuNzA2IDAgMS4wNjQtLjMzMSAxLjA2NC0uNzMzYzAtLjQ0OS0uMzc2LS43MjQtMS4yNDUtMS4wMzNjLTEuMzI1LS40ODgtMi4xMzItMS4yNS0yLjEzMi0yLjM2M2MwLTEuMzk0IDEuMDI5LTIuMTQzIDIuODU2LTIuMTQzYzEuMDY0IDAgMS43NDUuMzI4IDIuMzc3Ljg1OWwtLjgzIDEuMjQxWm0tNS44NDUtLjMzNWMuMzY2LjgxNS4zNjYgMS41NzcuMzY2IDIuNDd2My45MDZoLTEuODc2VjE5LjZjMC0xLjUyNy0uMDYtMi4xOC0uNTUtMi40OGMtLjQxLS4yODgtMS4wNzYtLjI3NC0xLjYxOC0uMTA3Yy0uMzc4LjExNy0uNzEzLjMzNS0uNzEzIDEuMDc0djUuMDU2SDYuNDI3VjEyLjgyaDEuODc2djIuMTEzYy43NDctLjM5OSAxLjU3Ny0uNzM4IDIuNjQ1LS43MzhjLjc2NCAwIDEuNTc3LjI1MyAyLjA2OS43ODdjLjQ5OC41NTIuNjI2IDEuMTU3LjcyMyAxLjk5MVoiLz48L3N2Zz4=
md=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTIyLjI3IDEzLjU2VjE2YTIgMiAwIDAgMS0yIDJIOGExIDEgMCAwIDEtMSAxSDNhMSAxIDAgMCAxLTEtMXYtNmExIDEgMCAwIDEgMS0xaDR2LTFhMiAyIDAgMCAxIDItMmgxMi4yN2ExIDEgMCAwIDEgMSAxdi41NnptLTMuNzMtOC41NkgyYTIgMiAwIDAgMC0yIDJ2MTBhMiAyIDAgMCAwIDIgMmgxNi41NGEyIDIgMCAwIDAgMi0yVjdhMiAyIDAgMCAwLTItMlptLTcuNzQgOC4zOUwxMiAxNi4yNWwyLjI2LTEuOTFhLjc1Ljc1IDAgMCAxIC45NyAxLjE0bC0zIDIuNTNhLjc1Ljc1IDAgMCAxLS45NiAwbC0zLTIuNTNhLjc1Ljc1IDAgMCAxIC45Ny0xLjE0WiIvPjwvc3ZnPg==
---
{.important}CrychicDoc                         // {.important}Main Project
    [folder] .github                            // {#script}CI/CD Scripts  
        workflows                               // Automated build scripts //+
    [folder] .vitepress                         // {#config}VitePress Configuration
        [folder] config                         // {.important}Localization configs
            [ts] index.ts                       // Main config file //v
        [folder] plugins                        // {.important}Custom plugins
            [ts] custom-alert.ts                // Alert plugin //+
            [ts] dialog.ts                      // Dialog plugin //+
        [folder] theme                          // {.important}Custom theme
            [folder] components                 // Vue components //v
            [folder] styles                     // CSS styles //v
        [ts] config.mts                         // {.important}VitePress config
        [ts] index.ts                           // {.important}Sidebar config
    [folder] .vscode                            // {#config}VS Code settings
        [md] snippets                           // Markdown snippets //v
    [folder] docs                               // {#content}Content Directory
        [folder] public                         // Static assets //v
        [folder] zh                             // {#content}Chinese content
            [md] various files                  // Documentation files //+
        [folder] en                             // {#content}English content  
            [md] various files                  // Documentation files //+
    [md] README.md                              // {.important}Project description
    [js] ExtractClassScript.js                  // {#ignore}Legacy script
    [md] extracted_classes.md                   // {#ignore}Legacy file
    LICENSE                                     // {#config}CC BY-SA 4.0
    .gitignore                                  // {#config}Git ignore rules
</LiteTree>

## Sidebar {#Sidebar}

:::alert {"type": "warning", "title": "Sidebar Importance"}
The **`sidebar`** is one of the most important guides in this documentation and has a dedicated [setup tutorial](./sidebarTutorial). This section focuses on explaining its practical usage and what sidebar settings you need to configure when writing documentation.
:::

The sidebar operates with two different logics:

### Sidebar Operation Modes

<LiteTree>
// Define workflow styles
#method1=color:white;background:#2196f3;padding:2px 6px;border-radius:3px;font-size:12px;
#method2=color:white;background:#9c27b0;padding:2px 6px;border-radius:3px;font-size:12px;
.pros=color:green;font-weight:500;
.step=color:#1976d2;
---
Sidebar Configuration Methods
    {#method1}Method 1: Index.md Based Control          //v    {.pros}Full Control
        {.step}Configure children in Index.md          // Complete sidebar customization
        {.step}No individual frontmatter needed        // Maintenance-friendly  
        {.pros}Perfect for complex projects             // Like KubeJS series
        {.pros}Predictable structure generation         // Full control over sub-categories
    {#method2}Method 2: Frontmatter Based              //+    {.pros}Simple Setup
        {.step}Basic root config in Index.md           // Minimal setup required
        {.step}Set noguide: true in articles           // Individual article control
        {.step}Configure title in frontmatter          // Article-specific titles
        {.pros}Simple maintenance                       // Easy for individual articles
        {.pros}Flexible article management              // Per-document control
</LiteTree>

:::alert {"type": "info", "title": "Navigation Guidance"}
Since document content mainly relies on sidebar and navigation bar for guidance, please ensure sidebar synchronization when writing documentation content. If you have questions, try consulting responsible members.
:::

:::alert {"type": "tip", "title": "Important Note"}
To ensure automatic generation of Prev and Next, please do not write content in index.md but only use it as a sidebar generator.
:::

## Article Writing Standards {#Article}

This section explains necessary standards. Although the standards will generally clarify the details to pay attention to, please still focus on `Pull Request` feedback.

:::alert {"type": "success", "title": "Standards Purpose"}
The purpose of standards is to ensure consistency in documentation style and guidance, helping readers better digest content. Part of the standards reference [MCMOD Writing Standards](https://bbs.mcmod.cn/thread-646-1-1.html). Standards do not constrain third-party documentation but do not allow **excessive deviation from article intent expression**.
:::

### Headings {#Title}

:::alert {"type": "error", "title": "Critical Requirement"}
You **must** follow heading usage standards, <font color=red>**otherwise your submission will never pass**</font>.
:::

Heading hierarchy should be progressive, and `H1` level headings should appear at the top and only once.

**Example Structure:**

<LiteTree>
// Define heading styles
#h1=color:white;background:#d32f2f;padding:3px 8px;border-radius:4px;font-weight:bold;
#h2=color:white;background:#1976d2;padding:2px 6px;border-radius:3px;
#h3=color:white;background:#388e3c;padding:2px 6px;border-radius:3px;
#h4=color:white;background:#f57c00;padding:2px 6px;border-radius:3px;
---
Document Structure
    {#h1}# Primary Heading                      // Only one per document //!
        {#h2}## Secondary Heading              // Multiple allowed //v
            {#h3}### Tertiary Heading          // Nested under H2 //v
                {#h4}#### Quaternary Heading   // Nested under H3 //v
        {#h2}## Another Secondary Heading      // Same level as above //v
            {#h3}### Another Tertiary Heading  // Nested structure //v
                {#h4}#### Another Quaternary   // Proper nesting //v
</LiteTree>

### Custom Anchors {#anchor}

Anchors `{#custom-anchor}` are a `VitePress` supported `Markdown` extension. Using them prevents links from becoming lengthy characters due to Chinese titles when copied. For example, [this link](#Article) is a link without custom anchors, while [this one](#Title) is optimized with anchors.

We generally encourage using anchors for easier sharing.

## Styles and Plugins {#Style&Plugin}

:::alert {"type": "info", "title": "Optional Content"}
This section content is **non-mandatory**.
:::

This documentation has built-in `beautification styles` and similar `plugins` that help writers design more vivid documentation content, avoid plain text wearing down reader patience, and help authors guide readers to truly important sections.

<font size = 1>The guidance mentioned here emphasizes the primary and secondary relationship of content. Generally, all written content is useful information, but typography and styles are needed to ensure readers get the most useful parts.</font>

### Styles {#Style}

The documentation has organized current supported styles in a separate [article](./styleList.md). If you have needs in this area, you can check this content before writing.

:::alert {"type": "tip", "title": "Basic Requirements"}
Even if you won't use complex styles, please understand basic [Markdown formatting](https://markdown.com.cn/basic-syntax/) and VitePress [Markdown extensions](https://vitepress.dev/zh/guide/markdown) before writing documentation.
:::

### Plugins {#Plugin}

The documentation has some built-in `plugins/components`, generally added to serve specific scenarios. You can view them [here](./samples.md).

### Documentation Configuration {#doc-config}

All documentation files have the following [frontmatter](#frontmatter) configuration fields:

:::alert {"type": "info", "title": "VitePress Compatibility"}
This site also supports VitePress native frontmatter styles. For details, see [here](https://vitepress.dev/zh/reference/frontmatter-config).
:::

| Configuration Field | Purpose | Type | Default Value |
|-----------|--------------------------------|---------|---------|
| `title` | Set title displayed in sidebar (uses filename if not set) | string | `N/A` |
| `noguide` | Whether this article appears in sidebar (false = show, true = hide) | boolean | `false` |
| `backPath` | Set destination when clicking BackButton | string | `N/A` |
| `authors` | Set additional authors for this article, displayed in contributors section | string[] | `N/A` |
| `showComment` | Whether to show comment section | boolean | `true` |
| `gitChangelog` | Whether to show contributors and page history | boolean | `true` |
| `progress` | Set article writing progress | int | `N/A` |
| `description` | Set article preview content | string | `N/A` |

:::details Configuration Example

```yaml
---
title: Example
backPath: ../
authors: ['M1hono', 'skyraah'] # You must submit at least one contribution to properly display your avatar and link
showComment: false
gitChangelog: false
progress: 100
description: This article provides documentation writing standards for this site!
---
```

:::

#### frontmatter Declaration {#frontmatter}

At the beginning of each Markdown file, use `---` to create frontmatter configuration:

```yaml
---
# Add your frontmatter here
---
```

### Type Completion {#TwoSlash}

:::alert {"type": "warning", "title": "Feature Status"}
**TwoSlash type completion feature is currently unavailable**. We are undergoing technical upgrades and this feature will be re-enabled in future versions.
:::

This section actually belongs to the Plugin category, but it's quite special.

If you've seen the two links mentioned in [Styles](#Style), you probably already know about `Codeblock`, a convenient feature for sharing code and displaying `syntax highlighting`.

The documentation plans to include plugins for displaying `type completion`, enabling more beneficial code display for related tutorials.

**Planned Type Completion Effect:**

```js
const String = "TypeScript"
console.log(String)
//              ^? (will show: const String: "TypeScript")
```

**Alternative Solutions:**
- Use standard code block syntax highlighting
- Add type explanations in comments manually
- Utilize JSDoc-style type annotations

## Content {#Content}

<LiteTree>
// Define priority styles
#critical=color:white;background:#d32f2f;padding:2px 6px;border-radius:3px;font-size:12px;
#important=color:white;background:#ff9800;padding:2px 6px;border-radius:3px;font-size:12px;
#guideline=color:white;background:#4caf50;padding:2px 6px;border-radius:3px;font-size:12px;
---
Content Guidelines
    {#critical}Content Accuracy                         //!    Primary standard
        Ensure correct content                          // Verify information accuracy
        Discuss with community and QQ groups           // Collaborate for verification
    {#guideline}Content Creation Process               //+    Collaborative approach  
        Create imperfect initial content               // Don't worry about perfection
        Collaborate for refinement and improvement      // Work together for quality
    {#important}Community Communication                //v    Essential practice
        Communicate frequently with community           // Stay connected
        Never arbitrarily delete/modify others' work   // Respect others' contributions
</LiteTree>

:::alert {"type": "error", "title": "Critical Warning"}
Please **DO NOT** arbitrarily **delete or modify** others' creations!!!
:::

## About Collaboration {#Cooperation}

This documentation has no complicated collaboration standards, only one: **ask the original author's opinion first** before making modifications!! For the third time!!

### Third-party Documentation Collaboration

If you are the owner of third-party documentation and want your name to appear in the author column, you need to submit at least one content modification to be properly recognized by the program, otherwise links and avatars cannot be generated normally.

:::alert {"type": "success", "title": "Collaboration Summary"}
- **Respect**: Always ask before modifying others' work
- **Communicate**: Stay in touch with the community
- **Contribute**: Submit at least one change for proper recognition
- **Quality**: Focus on content accuracy and consistency
:::