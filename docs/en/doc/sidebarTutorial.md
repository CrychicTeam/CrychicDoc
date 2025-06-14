---
title: Sidebar Configuration
description: CrychicDoc sidebar configuration system complete guide, from basic to advanced comprehensive tutorial
---

# Sidebar Configuration Tutorial

::: alert {"type": "success", "title": "Overview", "variant": "outlined"}
Through this tutorial, you will master CrychicDoc's powerful sidebar configuration system and learn to create well-structured, easy-to-navigate documentation sites.
:::

The sidebar configuration used to be very bloated and difficult to maintain. It has now been optimized through complex design to achieve the same effect with minimal `Front-matter` configuration.

## Configuration System Overview

This system provides users with four types of configurations: `Global Config`, `Root Config`, `Sub Config`, and `JSON Config`, with priority from low to high. The highest priority existing configuration will override lower priority configurations.

::: alert {"type": "info", "title": "💡 Configuration Priority", "variant": "outlined"}
**Configuration Priority (Low → High):**
1. Global Config - Global default settings
2. Root Config - Root directory configuration  
3. Sub Config - Subdirectory configuration
4. JSON Config - Precise control configuration

**Memory Rule**: The more specific the configuration, the higher the priority!
:::

@@@ dialog-def#config-overview {"title": "📊 Configuration System Architecture", "width": 800}
<LiteTree>
CrychicDoc Sidebar Configuration System
    Global Config (Priority 1)                     // .sidebarrc.yml
        Site-wide default configuration, affects all directories
    Root Config (Priority 2)                       // index.md + root: true
        Define sidebar root nodes
        Support grouping and external links  
        Apply to this root and all subdirectories
    Sub Config (Priority 3)                        // Regular index.md
        Adjust specific directory behavior
        Override inherited configuration
    JSON Config (Priority 4)                       // .vitepress/config/sidebar/
        locales.json                                // Display names
        order.json                                  // Sort control  
        collapsed.json                              // Collapse state
        hidden.json                                 // Visibility control
</LiteTree>

**Use Cases:**
- **Global**: Set site-wide uniform default behavior
- **Root**: Create main navigation structure, add external links
- **Sub**: Fine-tune specific directory display effects
- **JSON**: Precise control over file/directory display
@@@

Want to understand the complete architecture? :::dialog#config-overview Click to view detailed explanation:::

## Quick Start

::: stepper
@tab Step 1: Create Global Configuration
Create `.sidebarrc.yml` in the `docs/` directory:

```yaml
# docs/.sidebarrc.yml
defaults:
    maxDepth: 1
    collapsed: false
    hidden: false
```

@tab Step 2: Set Root Configuration
Add to `index.md` in main directories:

```yaml
---
root: true
title: "My Documentation"
maxDepth: 2
---
```

@tab Step 3: Test Effect
Start the development server to see sidebar changes:

```bash
npm run docs:dev
```
:::

## Global Config (Global Configuration)

**Location**: `docs/.sidebarrc.yml`  
**Priority**: Lowest (Level 1)  
**Scope**: Entire documentation site

The global configuration file defines the default behavior for the entire site. All directories will inherit these settings unless overridden by higher priority configurations.

### Configuration Example

```yaml
# docs/.sidebarrc.yml
defaults:
    maxDepth: 0 # Sidebar expansion depth (0=current level only, 1=include subdirectories)
    collapsed: true # Default collapse state
    hidden: false # Default hide state
    itemOrder: {} # Global sorting configuration
```

### Configurable Fields

| Field       | Type    | Default | Description                |
| ----------- | ------- | ------- | -------------------------- |
| `maxDepth`  | number  | 0       | Sidebar expansion depth    |
| `collapsed` | boolean | true    | Default collapse state of directory items |
| `hidden`    | boolean | false   | Default hide state of directory items |
| `itemOrder` | object  | {}      | Global item sorting configuration |

## Root Config (Root Configuration)

**Location**: `index.md` frontmatter (containing `root: true`)  
**Priority**: 🔹 Medium-Low (Level 2)  
**Scope**: Directory declared as root and all its subdirectories

Root configuration marks a directory as a sidebar root node. This configuration affects the behavior of this directory and all its subdirectories.

### Configuration Example

```md
---
root: true # Declare as sidebar root
title: "KubeJS Documentation" # Display title
maxDepth: 2 # Expand to second level
collapsed: false # Default expanded
priority: 100 # Sort priority
groups: # Group configuration
    - title: "Code Sharing"
      path: "CodeShare/"
externalLinks: # External links
    - text: "GitHub Repository"
      link: "https://github.com/KubeJS-Mods/KubeJS"
      priority: -1000
    - text: "Official Wiki"
      link: "https://kubejs.com/wiki"
      priority: -999
---

# KubeJS 1.20.1 Documentation

Here is the complete documentation for KubeJS 1.20.1...
```

### Configurable Fields

| Field           | Type         | Description                              |
| --------------- | ------------ | ---------------------------------------- |
| `root`          | boolean      | **Must be true**, mark as sidebar root |
| `title`         | string       | Title displayed in sidebar              |
| `hidden`        | boolean      | Whether to hide this root node          |
| `priority`      | number       | Sort priority (smaller numbers first)   |
| `maxDepth`      | number       | Expansion depth                         |
| `collapsed`     | boolean      | Whether to collapse by default          |
| `groups`        | array        | Group configuration                     |
| `externalLinks` | array        | External link configuration             |
| `itemOrder`     | array/object | Child item sorting configuration        |

### Group Configuration (Groups)

The grouping feature allows promoting subdirectory content as independent top-level items:

```yaml
groups:
    - title: "Code Sharing" # Group display name
      path: "CodeShare/" # Subdirectory path
      priority: 10 # Group sort priority
      maxDepth: 3 # Expansion depth of group content
```

### External Link Configuration

```yaml
externalLinks:
    - text: "GitHub Repository" # Display text
      link: "https://github.com/example/repo" # Link address
      priority: -1000 # Sort priority
      hidden: false # Whether to hide
```

## Sub Config (Sub Configuration)

**Location**: `index.md` frontmatter in any directory (not containing `root: true`)  
**Priority**: 🔸 Medium-High (Level 3)  
**Scope**: Current directory and its subdirectories

Sub configuration is used to adjust the behavior of non-root directories and can override settings inherited from parent directories or global configuration.

At the code level, it overrides `Directory Config` to modify the related configuration of that directory.

### Configuration Example

```md
---
title: "Advanced Features" # Custom display title
hidden: false # Show this directory
priority: 50 # Adjust sorting
maxDepth: 1 # Limit expansion depth
collapsed: true # Default collapsed
itemOrder: # Child item sorting
    - "setup.md"
    - "advanced.md"
    - "troubleshooting.md"
---

# Advanced Features Guide

This directory contains detailed explanations of advanced features...
```

### Configurable Fields

| Field       | Type         | Description                      |
| ----------- | ------------ | -------------------------------- |
| `title`     | string       | Title displayed in sidebar       |
| `hidden`    | boolean      | Whether to hide this directory   |
| `priority`  | number       | Sort priority                    |
| `maxDepth`  | number       | Expansion depth                  |
| `collapsed` | boolean      | Whether to collapse by default   |
| `itemOrder` | array/object | Child item sorting configuration |

## JSON Config (JSON Configuration)

**Location**: `.vitepress/config/sidebar/{lang}/{path}/`  
**Priority**: 🔹 Highest (Level 4)  
**Scope**: Specific directory

JSON configuration provides the finest granular control and can override all other configurations. Each directory can have multiple JSON files to control different aspects.

### File Types

#### 1. Localization Configuration - `locales.json`

Controls display names of directories and files:

```json
{
    "_self_": "Modpack",
    "kubejs/": "KubeJS",
    "recommendation/": "Recommendations",
    "setup.md": "Environment Setup",
    "advanced.md": "Advanced Configuration"
}
```

-   `_self_`: Display name of current directory
-   `path/`: Display name of subdirectory
-   `file.md`: Display name of file

#### 2. Sort Configuration - `order.json`

Controls `SidebarItem` `Priority`:

```json
{
    "setup.md": 1,
    "basic/": 2,
    "advanced/": 3,
    "kubejs/": 9007199254740991,
    "recommendation/": 9007199254740992
}
```

-   Smaller Priority values appear first
-   This doesn't follow common sense but cannot be modified
-   Default value: `9007199254740991` (JavaScript maximum safe integer)

#### 3. Collapse Configuration - `collapsed.json`

Controls collapse state of directory items:

```json
{
  "basic/": false,     # Default expanded
  "advanced/": true,   # Default collapsed
  "api/": true
}
```

#### 4. Hide Configuration - `hidden.json`

Controls item visibility:

```json
{
  "draft.md": true,        # Hide draft files
  "internal/": true,       # Hide internal directories
  "deprecated/": true      # Hide deprecated content
}
```

### JSON Configuration Directory Structure

:::demo JSON Configuration Directory Structure
<LiteTree>
.vitepress/config/sidebar/
    zh/                                 // Chinese configuration
        modpack/
            locales.json                // Localization
            order.json                  // Sorting
            collapsed.json              // Collapse state
            hidden.json                 // Hide configuration
            kubejs/
                locales.json
                order.json
        docs/
    en/                                 // English configuration
        ...
</LiteTree>
:::

## Configuration Priority Details

Configuration application order and priority:

```
📊 Priority (Low → High):
1️⃣ Global Config     (.sidebarrc.yml)
2️⃣ Root Config       (index.md with root: true)
3️⃣ Sub Config        (regular index.md)
4️⃣ JSON Config       (.vitepress/config/sidebar/)
```

### Priority Example

Assume the following configurations:

```yaml
# 1️⃣ docs/.sidebarrc.yml
defaults:
    maxDepth: 0
    collapsed: true
```

```yaml
# 2️⃣ docs/zh/modpack/kubejs/index.md
---
root: true
maxDepth: 2
collapsed: false
---
```

```yaml
# 3️⃣ docs/zh/modpack/kubejs/1.20.1/index.md
---
maxDepth: 1
---
```

```json
// 4️⃣ .vitepress/config/sidebar/zh/modpack/kubejs/1.20.1/collapsed.json
{
    "basic/": true
}
```

**Final Result**:

-   `maxDepth`: 1 (Sub Config overrides Root Config)
-   `collapsed`: false (Root Config overrides Global Config)
-   `basic/` directory collapse: true (JSON Config has highest priority)

## Practical Configuration Tips

### 1. Creating Multi-level Navigation

```yaml
# docs/zh/modpack/kubejs/index.md
---
root: true
title: "KubeJS"
maxDepth: 3
groups:
    - title: "Version 1.20.1"
      path: "1.20.1/"
      priority: 1
    - title: "Version 1.21"
      path: "1.21/"
      priority: 2
---
```

### 2. Adding External Resource Links

```yaml
externalLinks:
    - text: "📚 Official Documentation"
      link: "https://kubejs.com/"
      priority: -1000
    - text: "💬 Discord Community"
      link: "https://discord.gg/lat"
      priority: -999
    - text: "🐛 Issue Reports"
      link: "https://github.com/KubeJS-Mods/KubeJS/issues"
      priority: -998
```

### 3. Flexible Sorting Configuration

```yaml
# Using array (simple sorting)
itemOrder:
  - "introduction.md"
  - "getting-started.md"
  - "advanced/"
  - "examples/"

# Using object (precise control)
itemOrder:
  "introduction.md": 1
  "getting-started.md": 2
  "advanced/": 100
  "examples/": 200
```

### 4. Conditional Content Hiding

```json
// hidden.json - Hide content under development
{
    "wip/": true,
    "draft-feature.md": true,
    "experimental/": true
}
```

## 🎯 Best Practices

### 1. Configuration Hierarchy Planning

-   **Global Config**: Set basic default values for the entire site
-   **Root Config**: Define main section structure and external links
-   **Sub Config**: Adjust display methods of specific directories
-   **JSON Config**: Precise control of display names and sorting

### 2. Naming Conventions

-   Use camelCase for directory names, e.g., `GettingStart` or `gettingStart`
-   Use friendly display names in JSON configuration

### 3. Performance Optimization

-   Set `maxDepth` reasonably to avoid overly deep nesting
-   For large directories, use grouping features to break down content
-   Use `hidden` configuration to hide unnecessary files

### 4. Maintenance Recommendations

-   Regularly check and clean up unused JSON configuration files
-   Use Git to track configuration file changes
-   Establish configuration documentation for team collaboration

::: alert {"type": "info", "title": "Note"}
You can generally use `_self_` to control the `JSON Config` of `Root`.
:::

## ❓ Frequently Asked Questions

### Q: Why isn't my configuration taking effect?

A: Check the configuration priority order. Higher priority configurations will override lower priority configurations.

### Q: How do I hide an entire directory?

A: Set `hidden: true` in that directory's `index.md`, or configure it in the corresponding `hidden.json`.

### Q: How do I control the sorting of external links?

A: Use the `priority` field. Smaller values appear first. It's recommended to use negative numbers to make external links appear at the top.

### Q: How do I debug configuration issues?

A:

1. Check browser console for error messages
2. Confirm file paths and syntax correctness
3. Use development mode to view real-time effects
4. Check configuration priority level by level

---

By properly using these four configuration methods, you can create a well-structured, easy-to-navigate documentation sidebar. Remember the priority rules and make good use of the characteristics of various configurations to create the perfect user experience! 