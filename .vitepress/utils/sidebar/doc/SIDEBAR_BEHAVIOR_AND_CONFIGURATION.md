# Understanding and Configuring the Generated Sidebar

This document explains how the automated sidebar generation system works in conjunction with VitePress's native sidebar capabilities, and how you should configure your documentation to achieve the desired navigation structure.

## 1. How VitePress Sidebars Work (The Target Output)

VitePress supports two main ways to define sidebars:

-   **Single Sidebar:** A simple array of `SidebarItem` objects. This is used when your entire site has one consistent sidebar.
-   **Multiple Sidebars (`SidebarMulti`):** An object where keys are URL base paths (e.g., `/guide/`, `/config/`) and values are arrays of `SidebarItem` objects (or an object `{ items: SidebarItem[]; base: string }`). VitePress displays the sidebar corresponding to the base path of the currently viewed page.

**Our generator primarily aims to produce the `SidebarMulti` format.** The output is a single JSON file (`.vitepress/config/generated/sidebars.json`) that looks like this:

```json
{
    "/en/guide/": [
        { "text": "Guide Introduction", "link": "/en/guide/" },
        {
            "text": "Core Concepts",
            "link": "/en/guide/core-concepts/",
            "items": [
                {
                    "text": "Concept A",
                    "link": "/en/guide/core-concepts/a.html"
                }
            ]
        }
    ],
    "/en/reference/api/": [
        { "text": "API Overview", "link": "/en/reference/api/" }
        // ... more items for the API reference sidebar
    ],
    "/zh/guide/": [
        // ... Chinese guide sidebar items
    ]
}
```

When a user is on a page like `/en/guide/getting-started.html`, VitePress will use the sidebar defined for the `/en/guide/` key.

## 2. 🎯 **CRITICAL: Root Directory Content Flattening Behavior**

### **Core Architectural Principle**
**Root directories create ONE sidebar section with ALL subdirectory content flattened into it.**

#### **Example Structure:**
```
docs/zh/modpack/kubejs/1.20.1/
├── index.md (root: true)    # Creates the root section
├── Introduction/
│   ├── index.md
│   ├── getting-started.md
│   └── basic-concepts.md
├── CodeShare/
│   ├── index.md
│   ├── examples.md
│   └── snippets.md
└── Upgrade/
    ├── index.md
    └── migration-guide.md
```

#### **Expected Correct Output:**
```json
"/zh/modpack/kubejs/1.20.1/": [
  {
    "text": "KubeJS", // From root index.md or JSON override
    "items": [
      // ALL content from Introduction/, CodeShare/, AND Upgrade/ flattened here
      { "text": "介绍", "link": "/zh/modpack/kubejs/1.20.1/Introduction/" },
      { "text": "入门指南", "link": "/zh/modpack/kubejs/1.20.1/Introduction/getting-started.html" },
      { "text": "基本概念", "link": "/zh/modpack/kubejs/1.20.1/Introduction/basic-concepts.html" },
      { "text": "代码分享", "link": "/zh/modpack/kubejs/1.20.1/CodeShare/" },
      { "text": "示例", "link": "/zh/modpack/kubejs/1.20.1/CodeShare/examples.html" },
      { "text": "代码片段", "link": "/zh/modpack/kubejs/1.20.1/CodeShare/snippets.html" },
      { "text": "升级", "link": "/zh/modpack/kubejs/1.20.1/Upgrade/" },
      { "text": "迁移指南", "link": "/zh/modpack/kubejs/1.20.1/Upgrade/migration-guide.html" }
    ]
  }
]
```

#### **WRONG Current Behavior (Multiple Sections):**
```json
"/zh/modpack/kubejs/1.20.1/": [
  {
    "text": "KubeJS",
    "items": [/* Only Introduction/ content */]
  },
  {
    "text": "KubeJS", 
    "items": [/* Only CodeShare/ content */]
  },
  {
    "text": "KubeJS",
    "items": [/* Only Upgrade/ content */]
  }
]
```

### **Key Principles:**

1. **ONE Section Per Root**: Root directory (`index.md` with `root: true`) creates exactly ONE sidebar section
2. **Content Flattening**: ALL subdirectory content is flattened into that single section's `items` array
3. **Group Configuration**: Optional `groups` configuration can organize the flattened content into subsections
4. **JSON Overrides**: Apply to the final flattened structure for title customization

## 3. Defining Sidebar Roots

Two main types of content can define a new top-level entry in the `SidebarMulti` object:

-   **Normal Roots (`index.md` with `root:true`):**
    *   **Mechanism:** Create an `index.md` file in the corresponding directory and put `root: true` in its frontmatter.
    *   **Example:** `docs/en/guide/index.md` with `root: true` will instruct the generator to create a sidebar definition for the base path `/en/guide/`.
    *   **Flattening Behavior:** The root will create ONE section with ALL subdirectory content flattened into its `items` array
    *   The frontmatter of this `index.md` (e.g., `title`, `itemOrder`, `groups`, `maxDepth`, `collapsed`) will control the organization of the flattened content

-   **GitBook Roots (`SUMMARY.md`):**
    *   **Mechanism:** A directory containing a `SUMMARY.md` file is automatically treated as a distinct sidebar root.
    *   **Example:** If `docs/en/my-gitbook/SUMMARY.md` exists, the directory `docs/en/my-gitbook/` becomes a GitBook root. The generator will create a sidebar definition for the base path `/en/my-gitbook/`.
    *   **Content Source:** The structure of this sidebar view is derived entirely from the `SUMMARY.md` file, parsed by a dedicated `GitBookParserService`.

## 4. "Root-within-a-Root" (Nested Independent Sidebars)

This is a powerful feature for creating distinct sidebar experiences for sub-sections of your documentation.

-   **Scenario:**
    -   `docs/en/main-section/index.md` has `root: true` (defines sidebar for `/en/main-section/`).
    -   `docs/en/main-section/sub-feature/index.md` ALSO has `root: true`.
-   **Behavior:**
    1.  **Separate Sidebar Definitions:** The generator will create two entries in `sidebars.json`:
        -   One for `/en/main-section/` with flattened content from all non-root subdirectories
        -   Another for `/en/main-section/sub-feature/` with its own flattened content
    2.  **Item in Parent Sidebar:** When generating the sidebar for `/en/main-section/`, the `sub-feature/` directory will appear as a regular, clickable item in the flattened content
    3.  **Context Switch on Navigation:** When the user navigates to `/en/main-section/sub-feature/`, VitePress switches to the dedicated sidebar for that sub-root

## 5. Role of `groups` - Organizing Flattened Content Within a Sidebar View

-   **Purpose:** The `groups` array in the frontmatter organizes the **flattened content** within the sidebar view
-   **No New Roots:** Groups **do not** create new top-level keys in `sidebars.json`
-   **Content Organization:** Groups create organized subsections within the single root section

### **Example with Groups:**
```yaml
---
root: true
title: Main Guide
groups:
  - title: "基础教程"
    children: ["Introduction/*"]
  - title: "代码示例"  
    children: ["CodeShare/*"]
  - title: "升级指南"
    children: ["Upgrade/*"]
---
```

**Results in:**
```json
"/zh/modpack/kubejs/1.20.1/": [
  {
    "text": "KubeJS教程",
    "items": [
      {
        "text": "基础教程",
        "items": [/* All Introduction/ content */]
      },
      {
        "text": "代码示例", 
        "items": [/* All CodeShare/ content */]
      },
      {
        "text": "升级指南",
        "items": [/* All Upgrade/ content */]
      }
    ]
  }
]
```

### **Without Groups (Flat List):**
```json
"/zh/modpack/kubejs/1.20.1/": [
  {
    "text": "KubeJS教程",
    "items": [
      /* All content from ALL subdirectories in a single flat list */
      /* Ordered by itemOrder configuration or alphabetically */
    ]
  }
]
```

## 6. Clickable Directories and Groups

-   **Always Clickable:** Any item in the sidebar that represents a directory or an organizational group (from the `groups` array) will be generated with a `link` property.
-   **Link Target:**
    -   **Directories:** The link will point to the directory's path (e.g., `/en/guide/concepts/`). VitePress will typically serve the `index.html` from that directory.
    -   **Groups:** The link target is determined based on the group configuration and child content
-   **`maxDepth` Interaction:** User-configured `maxDepth` controls how many levels of nested `items` are *displayed* within the flattened structure

## 7. Configuration Inheritance Summary

1.  **Global Defaults (`docs/.sidebarrc.yml`):** Provides base settings like `maxDepth`, default `collapsed` for roots.
2.  **Root `index.md` (`root:true`):** Defines a new sidebar view with flattened content. Its frontmatter controls the organization of ALL flattened content (e.g., `groups` for organizing flattened items, `itemOrder` for ordering within the flattened structure).
3.  **Sub-directory `index.md` (no `root:true`):** Provides metadata for that directory when it appears in the flattened content (e.g., custom `title` for the directory item).
4.  **File `*.md` frontmatter:** `title`, `status`, `priority` for individual page links within the flattened structure.

## 8. Role of Managed JSON Files (`.vitepress/config/sidebar/...`)

-   **Purpose:** For fine-grained overrides of text (`locales.json`), order (`order.json`), and collapsed states (`collapsed.json`) on the **flattened structure**.
-   **Scope:** JSON files are scoped per sidebar root view and apply to the flattened content within that view.
-   **Flattened Path Keys:** Keys in JSON files correspond to items in the flattened structure:
    ```json
    {
      "_self_": "KubeJS教程",           // Root section title override
      "Introduction/": "介绍",          // Directory in flattened content  
      "CodeShare/": "代码分享",         // Directory in flattened content
      "Upgrade/": "升级",              // Directory in flattened content
      "Introduction/getting-started.md": "入门指南" // File in flattened content
    }
    ```
-   **Preservation of User Edits:** The system preserves manual edits while updating the structure when content changes.

## 9. 🔄 **Current System Status**

### **Known Issues (Being Fixed)**
- ❌ **Structural Generation**: Creating multiple sections instead of flattening content
- ❌ **Content Flattening**: Logic not implemented for root directories  
- ❌ **JSON Overrides**: Not applying due to structural issues
- ❌ **Group Configuration**: Not properly organizing flattened content

### **Target Architecture**
- ✅ **One Section Per Root**: Root directories create single sidebar sections
- ✅ **Content Flattening**: All subdirectory content appears in root section's items array
- ✅ **Group Organization**: Groups organize flattened content into logical subsections
- ✅ **JSON Override Application**: Overrides apply to final flattened structure

This architecture ensures logical content organization focused on user navigation experience rather than file system structure.
