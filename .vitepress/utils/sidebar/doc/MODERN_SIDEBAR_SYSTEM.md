# Modern Sidebar System Documentation ✅

**Status:** Production Ready  
**Phase 2:** ✅ COMPLETED  
**Last Updated:** January 2025

## 🎉 System Overview

The CrychicDoc sidebar system has been successfully modernized and simplified. The system now correctly implements content flattening with robust JSON override capabilities and non-destructive hidden functionality.

---

## 📋 **Current Configuration Format**

### **Root Directory Configuration**
```yaml
---
root: true                    # Creates separate sidebar section
title: "Section Title"        # Display title (can be overridden by JSON)
collapsed: true               # Default collapsed state (can be overridden by JSON)
hidden: false                 # Hide from sidebar (can be overridden by JSON)
layout: doc                   # VitePress layout
prev: false                   # Navigation controls
next: false                   # Navigation controls
---

# Section Title

Content here...
```

### **Regular Directory Configuration**
```yaml
---
title: "Directory Title"      # Display title (can be overridden by JSON)
collapsed: true               # Default collapsed state (can be overridden by JSON)
hidden: false                 # Hide from sidebar (can be overridden by JSON)
---

# Directory Title

Content here...
```

### **Minimal Configuration**
```yaml
---
title: "Title"                # Only title needed
---

# Title

Content here...
```

---

## 🔧 **JSON Override System**

### **Override File Locations**
```
.vitepress/config/sidebar/{lang}/{path}/
├── locales.json     # Custom display titles
├── order.json       # Item ordering (lower = higher priority)
├── collapsed.json   # Collapse states
└── hidden.json      # Hide/show items
```

### **locales.json** - Custom Display Titles
```json
{
  "FileName": "显示名称",
  "DirectoryName": "目录名称", 
  "_self_": "自定义根标题"
}
```

### **order.json** - Item Ordering
```json
{
  "FirstItem": 1,
  "SecondItem": 2,
  "LastItem": 10
}
```

### **collapsed.json** - Collapse States
```json
{
  "DirectoryName": true,
  "_self_": false
}
```

### **hidden.json** - Hide/Show Items
```json
{
  "ItemToHide": true,
  "ItemToShow": false
}
```

---

## 🏗️ **How It Works**

### **Content Flattening**
1. **Root Detection**: Directories with `root: true` create separate sidebar sections
2. **Content Flattening**: All subdirectory content is flattened into the root section
3. **Single Navigation**: Results in ONE sidebar section with ALL content accessible

### **Example Structure**
```
docs/zh/modpack/kubejs/1.20.1/
├── index.md (root: true)       # Creates the main section
├── Introduction/               # Content flattened into main section
├── CodeShare/                  # Content flattened into main section
└── Upgrade/                    # Content flattened into main section
```

**Results in:**
```json
"/zh/modpack/kubejs/1.20.1/": [
  {
    "text": "KubeJS", 
    "items": [
      // ALL content from Introduction/, CodeShare/, Upgrade/ here
      // With JSON overrides applied
      // With proper ordering
      // With hidden items filtered
    ]
  }
]
```

### **Hidden Item System**
- Items marked as `hidden: true` are tagged with `_hidden` flag
- Hidden items are filtered from final output but preserved in structure
- Can be toggled back to visible without breaking the system
- **Non-destructive** - no permanent removal

---

## 🚀 **Features**

### ✅ **Working Features**
- **Content Flattening**: Root directories create single sections with all content
- **JSON Overrides**: locales, order, collapsed, hidden all functional
- **Hidden System**: Non-destructive hiding/showing of items
- **Language-Specific**: Separate `sidebar_en.json` and `sidebar_zh.json` files
- **Performance**: Intelligent change detection and caching
- **Hot Reload**: Automatic regeneration on file changes in development

### ❌ **Removed Legacy Features**
- `status` field - Removed for simplification
- `priority` field - Replaced by JSON order system
- `maxDepth` field - Replaced by content flattening
- `sub` field - Replaced by automatic detection
- `locale` frontmatter - Replaced by JSON locales system
- `itemOrder` frontmatter - Replaced by JSON order system
- `groups` configuration - Replaced by content flattening

---

## 📝 **VS Code Snippets**

### **Modern Root Configuration**
**Prefix:** `modern-sidebar-root`
```yaml
---
root: true
title: Section Title
collapsed: true
layout: doc
prev: false
next: false
---

# Section Title
```

### **Modern Directory Configuration**
**Prefix:** `modern-sidebar-dir`
```yaml
---
title: Directory Title
collapsed: true
---

# Directory Title
```

### **Hidden Configuration**
**Prefix:** `modern-sidebar-hidden`
```yaml
---
title: Hidden Section
hidden: true
---

# Hidden Section
```

### **JSON Override Templates**

**Locales JSON** - Prefix: `json-locales`
```json
{
  "FileName": "显示名称",
  "DirectoryName": "目录名称",
  "_self_": "自定义根标题"
}
```

**Order JSON** - Prefix: `json-order`
```json
{
  "FirstItem": 1,
  "SecondItem": 2,
  "LastItem": 10
}
```

**Collapsed JSON** - Prefix: `json-collapsed`
```json
{
  "DirectoryName": true,
  "_self_": false
}
```

**Hidden JSON** - Prefix: `json-hidden`
```json
{
  "ItemToHide": true,
  "AnotherItem": false
}
```

---

## 🔍 **Troubleshooting**

### **Common Issues**

**Issue: Sidebar not generating**
- Check if `root: true` is set correctly
- Verify file paths are correct
- Check console for generation errors

**Issue: JSON overrides not working**
- Verify JSON file location: `.vitepress/config/sidebar/{lang}/{path}/`
- Check JSON syntax is valid
- Ensure file names match exactly: `locales.json`, `order.json`, etc.

**Issue: Hidden items not working**
- Set `hidden: true` in frontmatter OR
- Add to `hidden.json` with `"itemName": true`
- Check that generation completed successfully

### **Debug Helpers**

**Check Generated Files:**
```bash
# Check if sidebar files exist
ls .vitepress/config/generated/
# Should see: sidebar_en.json, sidebar_zh.json

# Check JSON override files
ls .vitepress/config/sidebar/zh/modpack/kubejs/1.20.1/
# Should see: locales.json, order.json, collapsed.json, hidden.json
```

**Debug Path Template:**
```
.vitepress/config/sidebar/{lang}/{path}/
```

---

## 🎯 **Migration Guide**

### **From Legacy to Modern**

**Before (Legacy):**
```yaml
---
title: Section
priority: 1
maxDepth: 6
locale:
  item1: 项目1
itemOrder:
  item1: 1
---
```

**After (Modern):**
```yaml
---
title: Section
root: true
collapsed: true
---
```

**Plus JSON Files:**
```json
// .vitepress/config/sidebar/zh/section/locales.json
{
  "item1": "项目1"
}

// .vitepress/config/sidebar/zh/section/order.json
{
  "item1": 1
}
```

---

## 📊 **Performance**

### **Generation Performance**
- ✅ **Language-Specific Files**: Separate generation for faster loading
- ✅ **Intelligent Change Detection**: Only regenerates when needed
- ✅ **Caching**: Memory-based caching for repeated requests
- ✅ **Debouncing**: Prevents excessive regeneration during development

### **File Sizes**
- English sidebar: ~145KB (4,194 lines)
- Chinese sidebar: ~217KB (5,872 lines)
- Total paths: 54 sections across both languages

---

**Modern Sidebar System: ✅ Production Ready**

The system is now fully functional with all Phase 2 objectives completed successfully. 