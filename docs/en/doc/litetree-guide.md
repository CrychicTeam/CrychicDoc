---
title: LiteTree Component Guide
description: Complete guide to using LiteTree for creating tree structures in VitePress
layout: doc
---

# LiteTree Component Guide

LiteTree is a lightweight tree component designed for React/Vue/VitePress that uses an indentation format similar to YAML instead of JSON, making it perfect for use in Markdown documentation.

**Based on**: [LiteTree Official Documentation](https://zhangfisher.github.io/lite-tree/) and [GitHub Repository](https://github.com/zhangfisher/lite-tree)

## Overview

**Key Features:**
- Small size, no third-party dependencies
- Supports React/Vue versions
- Uses `lite` format (indentation-based) perfect for Markdown
- Supports custom node styles, labels, comments, icons
- Variable system for styles and icons
- Built-in markers for different states
- Excellent fault tolerance for non-standard data

## Basic Syntax

### Simple Tree Structure

:::demo Basic Tree
<LiteTree>
Company A
    Administrative Center
        CEO Office
        Human Resources
        Finance Department
    Marketing Center
        Marketing Department
        Sales Department
    R&D Center
        Development Team
        Testing Team
</LiteTree>
:::

### Tree with Markers

LiteTree supports various built-in markers to indicate different states:

:::demo Standard Markers
<LiteTree>
Project Status
    Completed Tasks      //v    Success marker
    New Features         //+    Added marker  
    Deprecated Code      //-    Removed marker
    Errors Found         //x    Error marker
    Modified Files       //*    Modified marker
    Important Items      //!    Important marker
</LiteTree>
:::

**Available Markers:**
- `//+` - Added/New items (green plus icon)
- `//-` - Removed/Deleted items (red minus icon)
- `//x` - Error/Failed items (red X icon)  
- `//v` - Success/Completed items (green check icon)
- `//*` - Modified/Changed items (orange asterisk icon)
- `//!` - Important/Priority items (red exclamation icon)

## Variable System

LiteTree supports three types of variables that must be defined before the `---` separator:

### Style Variables (`#name=styles`)

Define reusable CSS styles:

:::demo Style Variables
<LiteTree>
// Define style variables
#important=color:red;font-weight:bold;background:#ffe6e6;padding:2px 6px;border-radius:3px;
#success=color:green;font-weight:bold;background:#e6ffe6;padding:2px 6px;border-radius:3px;
#warning=color:orange;background:#fff3e0;padding:2px 6px;border-radius:3px;
---
Project Files
    {#important}Critical File
    {#success}Completed File  
    {#warning}Needs Review
</LiteTree>
:::

### Class Variables (`.name=styles`)

Define CSS classes:

:::demo Class Variables
<LiteTree>
// Define class variables
.folder=color:#1976d2;font-weight:500;
.file=color:#666;
.important=font-weight:bold;color:#d32f2f;
---
{.folder}Source Code
    {.important}main.js
    {.file}config.json
    {.file}package.json
</LiteTree>
:::

### Icon Variables (`name=base64data`)

Define custom icons using base64-encoded SVG:

:::demo Icon Variables
<LiteTree>
// Define icon variables
folder=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTEwIDRIOGEyIDIgMCAwIDAtMiAydjEyYTIgMiAwIDAgMCAyIDJoOGEyIDIgMCAwIDAgMi0yVjhhMiAyIDAgMCAwLTItMmgtM2wtMi0yWiIvPjwvc3ZnPg==
file=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTE0IDJINmEyIDIgMCAwIDAtMiAydjE2YTIgMiAwIDAgMCAyIDJoMTJhMiAyIDAgMCAwIDItMlY4bC02LTZtNCA5VjlsNCA0aC00WiIvPjwvc3ZnPg==
js=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9IiNmN2RmMWUiIGQ9Ik0zIDNoMTh2MThIM1ptMTYuNTI1IDE0LjVjLS4zLS4zNTQtLjc5NS0uNjI5LTEuNzE3LS42MjljLS44ODEgMC0xLjQzOS4zMTgtMS40MzkuNzE4YzAgLjM5Ni4zNzMuNjM3IDEuMTU2Ljk2N2MxLjMzMi41ODYgMi4yODEgMS4wOTMgMi4yODEgMi4zOGMwIDEuMzItMS4yMDMgMi4xNDMtMi45NzQgMi4xNDNjLTEuMjEzIDAtMi4yNzEtLjQ2Mi0yLjk1LTEuMDc0bC44NzUtMS4yNzNjLjQzMy4zODkgMS4wNjQuNzI0IDEuNjY0LjcyNGMuNzA2IDAgMS4wNjQtLjMzMSAxLjA2NC0uNzMzYzAtLjQ0OS0uMzc2LS43MjQtMS4yNDUtMS4wMzNjLTEuMzI1LS40ODgtMi4xMzItMS4yNS0yLjEzMi0yLjM2M2MwLTEuMzk0IDEuMDI5LTIuMTQzIDIuODU2LTIuMTQzYzEuMDY0IDAgMS43NDUuMzI4IDIuMzc3Ljg1OWwtLjgzIDEuMjQxWm0tNS44NDUtLjMzNWMuMzY2LjgxNS4zNjYgMS41NzcuMzY2IDIuNDd2My45MDZoLTEuODc2VjE5LjZjMC0xLjUyNy0uMDYtMi4xOC0uNTUtMi40OGMtLjQxLS4yODgtMS4wNzYtLjI3NC0xLjYxOC0uMTA3Yy0uMzc4LjExNy0uNzEzLjMzNS0uNzEzIDEuMDc0djUuMDU2SDYuNDI3VjEyLjgyaDEuODc2djIuMTEzYy43NDctLjM5OSAxLjU3Ny0uNzM4IDIuNjQ1LS43MzhjLjc2NCAwIDEuNTc3LjI1MyAyLjA2OS43ODdjLjQ5OC41NTIuNjI2IDEuMTU3LjcyMyAxLjk5MVoiLz48L3N2Zz4=
---
[folder] Frontend Project  
    [folder] src
        [folder] components
            [file] Header.vue
            [file] Footer.vue
        [folder] utils
            [js] helpers.js
            [js] api.js
    [file] package.json
</LiteTree>
:::

## Inline Styling

### Direct Color Styling

Apply styles directly to text using `{color:value}` syntax:

:::demo Inline Colors
<LiteTree>
Project Status
    {color:green}Completed Features
    {color:orange}In Progress
    {color:red}Critical Issues
    {color:blue;font-weight:bold}Important Notes
</LiteTree>
:::

### Mixed Styling

Combine variables, inline styles, icons, and markers:

:::demo Complete Example
<LiteTree>
// Style definitions
#new=color:white;background:#4caf50;padding:1px 4px;border-radius:2px;font-size:12px;
#deprecated=color:white;background:#f44336;padding:1px 4px;border-radius:2px;font-size:12px;
.important=font-weight:bold;color:#1976d2;
// Icon definitions
vue=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9IiM0Y2FmNTAiIGQ9Ik0yIDIwaDIwTDEyIDR6Ii8+PC9zdmc+
ts=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMTUgMTUiPjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzMxNzhDNiIgZD0iTTEyLjUgOHYtLjE2N2MwLS43MzYtLjU5Ny0xLjMzMy0xLjMzMy0xLjMzM0gxMGExLjUgMS41IDAgMSAwIDAgM2gxYTEuNSAxLjUgMCAwIDEgMCAzaC0xQTEuNSAxLjUgMCAwIDEgOC41IDExTTggNi41SDNtMi41IDBWMTNNMS41LjVoMTN2MTRIOS41eiIvPjwvc3ZnPg==
---
{.important}CrychicDoc Project
    .vitepress                    // {.important}Configuration
        config
            [ts] index.ts         // {#new}Updated config
        plugins                   // {.important}Custom plugins  
            [ts] custom-alert.ts  // {#new}Alert plugin
            [ts] dialog.ts        // {#new}Dialog plugin
        theme
            [vue] components      // {.important}Vue components
                [vue] CustomAlert.vue  // {#new}New component
                [vue] Dialog.vue       // {#new}New component  
    docs
        zh                        // Chinese docs
            styleList.md          // {#deprecated}Needs update
        en                        // English docs  
            samples.md            // {#new}New examples
            {color:blue}litetree-guide.md   // {#new}This guide
    package.json                  //v    Project configuration
    README.md                     //!    {.important}Important documentation
</LiteTree>
:::

## Common Use Cases

### Project File Structure

:::demo Project Structure
<LiteTree>
// File type styles
.folder=color:#1976d2;font-weight:500;
.file=color:#666;
.config=color:#f57c00;font-weight:500;
.doc=color:#8bc34a;
// Status styles  
#completed=color:green;background:#e6ffe6;padding:1px 3px;border-radius:2px;font-size:11px;
#inprogress=color:orange;background:#fff3e0;padding:1px 3px;border-radius:2px;font-size:11px;
#todo=color:red;background:#ffe6e6;padding:1px 3px;border-radius:2px;font-size:11px;
---
{.folder}my-project
    {.folder}src                  //v    {#completed}Structure complete
        {.folder}components       //+    {#inprogress}Adding components
            {.file}Header.vue     //v    {#completed}Done
            {.file}Footer.vue     //+    {#todo}TODO
        {.folder}pages
            {.file}Home.vue       //v    {#completed}Done
            {.file}About.vue      //*    {#inprogress}Updating
    {.config}package.json         //v    {#completed}Configured
    {.config}vite.config.js       //*    {#inprogress}Optimizing
    {.doc}README.md               //!    {#todo}Needs documentation
</LiteTree>
:::

### Team Organization

:::demo Team Structure
<LiteTree>
// Team role styles
#lead=color:white;background:#1976d2;padding:2px 6px;border-radius:3px;font-size:12px;
#senior=color:#1976d2;background:#e3f2fd;padding:2px 6px;border-radius:3px;font-size:12px;
#junior=color:#666;background:#f5f5f5;padding:2px 6px;border-radius:3px;font-size:12px;
---
Development Team
    Frontend Team                 // {#lead}Team Lead: Alice
        React Developers          //+    Expanding team
            John Smith            // {#senior}Senior Dev
            Jane Doe              // {#junior}Junior Dev
        Vue Developers            //v    Team complete
            Bob Wilson            // {#senior}Senior Dev
            Mary Johnson          // {#junior}Junior Dev
    Backend Team                  // {#lead}Team Lead: Charlie
        API Development           //*    Restructuring
            David Brown           // {#senior}Senior Dev
            Lisa Garcia           // {#junior}Junior Dev
        Database Team             //!    Critical project
            Mike Davis            // {#senior}Senior Dev
</LiteTree>
:::

## VSCode Snippets

The project includes comprehensive VSCode snippets for LiteTree. Key snippets include:

- **`@file-tree`** - Basic tree structure
- **`@file-tree-advanced`** - Tree with variables and styling
- **`@lite-style-var`** - Style variable definition
- **`@lite-class-var`** - Class variable definition  
- **`@lite-icon-var`** - Icon variable definition
- **`@icon-folder`**, **`@icon-file`**, **`@icon-js`**, **`@icon-ts`**, **`@icon-vue`** - Common icons
- **`@lite-status-styles`** - Pre-defined status styles
- **`@lite-filetype-styles`** - Pre-defined file type styles

## Best Practices

1. **Use Variables**: Define reusable styles and icons at the top
2. **Consistent Naming**: Use clear, descriptive names for variables
3. **Logical Grouping**: Group related items together
4. **Appropriate Markers**: Use markers consistently to indicate status
5. **Color Coding**: Use colors meaningfully (red=error, green=success, etc.)
6. **Icon Usage**: Use icons sparingly to avoid clutter
7. **Indentation**: Maintain consistent indentation (4 spaces recommended)

## Troubleshooting

### Variables Not Working
- Ensure variables are defined before the `---` separator
- Check for syntax errors in variable definitions
- Verify base64 encoding for icon variables

### Styling Issues  
- Ensure CSS properties are valid
- Check for missing semicolons in style definitions
- Test inline styles to debug variable issues

### Icons Not Showing
- Verify base64 encoding is correct
- Ensure SVG is properly formatted
- Check if icon name matches usage

## Advanced Features

### Built-in Icons and Elements

LiteTree includes several built-in icons and decorative elements:

:::demo Built-in Icons
<LiteTree>
Project Tasks
    [checked] Completed Task
    [unchecked] Pending Task
    [star] Important Item
    [link] External Reference
</LiteTree>
:::

### Links and Tags

LiteTree supports clickable links and tag syntax:

:::demo Links and Tags
<LiteTree>
Documentation
    User Guide              // [Read More](https://example.com)
    API Reference           // [API:tag](https://api.example.com) 
    Tutorials               // [Tutorial](https://tutorial.example.com)[new]
</LiteTree>
:::

### Collapsible Sections

Use `+` prefix to create collapsible sections:

:::demo Collapsible Sections
<LiteTree>
Project Overview
    + Development Phase
        Frontend Development
        Backend Development
        Testing
    + Deployment Phase
        Staging Environment
        Production Deployment
</LiteTree>
:::

### Complex Official Example

Here's a comprehensive example from the official LiteTree documentation showcasing all advanced features:

:::demo Official Advanced Example
<LiteTree>
#error=color:red;border: 1px solid red;background:#ffd2d2;padding:2px;
#blue=color:blue;border: 1px solid blue;background:#e6e6ff;padding:2px;
#warning=color:orange;border: 1px solid orange;background:#fff3e0;padding:2px;
.success=color:green;font-weight:bold;
airplane=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjU2IDI1NiI+PHBhdGggZmlsbD0iY3VycmVudENvbG9yIiBkPSJNMjM1LjU4IDEyOC44NEwxNjAgOTEuMDZWNDhhMzIgMzIgMCAwIDAtNjQgMHY0My4wNmwtNzUuNTggMzcuNzhBOCA4IDAgMCAwIDE2IDEzNnYzMmE4IDggMCAwIDAgOS41NyA3Ljg0TDk2IDE2MS43NnYxOC45M2wtMTMuNjYgMTMuNjVBOCA4IDAgMCAwIDgwIDIwMHYzMmE4IDggMCAwIDAgMTEgNy40M2wzNy0xNC44MWwzNyAxNC44MWE4IDggMCAwIDAgMTEtNy40M3YtMzJhOCA4IDAgMCAwLTIuMzQtNS42NkwxNjAgMTgwLjY5di0xOC45M2w3MC40MyAxNC4wOEE4IDggMCAwIDAgMjQwIDE2OHYtMzJhOCA4IDAgMCAwLTQuNDItNy4xNk0yMjQgMTU4LjI0bC03MC40My0xNC4wOEE4IDggMCAwIDAgMTQ0IDE1MnYzMmE4IDggMCAwIDAgMi4zNCA1LjY2TDE2MCAyMDMuMzF2MTYuODdsLTI5LTExLjYxYTggOCAwIDAgMC01Ljk0IDBMOTYgMjIwLjE4di0xNi44N2wxMy42Ni0xMy42NUE4IDggMCAwIDAgMTEyIDE4NHYtMzJhOCA4IDAgMCAwLTkuNTctNy44NEwzMiAxNTguMjR2LTE3LjNsNzUuNTgtMzcuNzhBOCA4IDAgMCAwIDExMiA5NlY0OGExNiAxNiAwIDAgMSAzMiAwdjQ4YTggOCAwIDAgMCA0LjQyIDcuMTZMMjQ0IDE0MC45NFoiLz48L3N2Zz4=
ts=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMTUgMTUiPjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzMxNzhDNiIgZD0iTTEyLjUgOHYtLjE2N2MwLS43MzYtLjU5Ny0xLjMzMy0xLjMzMy0xLjMzM0gxMGExLjUgMS41IDAgMSAwIDAgM2gxYTEuNSAxLjUgMCAwIDEgMCAzaC0xQTEuNSAxLjUgMCAwIDEgOC41IDExTTggNi41SDNtMi41IDBWMTNNMS41LjVoMTN2MTRIOS41eiIvPjwvc3ZnPg==
---
[airplane] Company A ({color:red}Critical, {#blue}Urgent)    // Main Organization
    Administrative Center
        {color:red;font-weight:bold;background:#ffeaea}President's Office
        [checked] Human Resources Department
        [unchecked] Finance Department  
        Administrative Department        //+  Management operations
        Legal Department                //+  Legal affairs
        [airplane] Audit Department     //+  Financial audit
        Information Center              //v  Operational
        [star] Security Department      //{color:red} Confidential operations
    + Market Center    
        Marketing Department ({#error}Critical, {#warning}Review)
        Sales Department                //-  Restructuring
        Customer Service Department     //-  Consolidating
        {#blue} Brand Department        //   Strategic focus
        Market Planning Department      //!  Priority project
        Market Marketing Department     //   {.success}Expanding
    R&D Center
        Mobile R&D Department           //!  Innovation focus
        Platform R&D Department ({.success}Java, {#error}Legacy)
        {.success} Testing Department   //v  Quality assurance
        Operations Department           //*  Process improvement
        Product Department              //*  Feature development
        Design Department               //*  UI/UX enhancement
        Project Management Department   //*  Coordination
</LiteTree>
:::

This example demonstrates:
- **Complex styling** with multiple variables and inline styles
- **Custom icons** (airplane, TypeScript) with base64 SVG
- **Built-in elements** like `[checked]`, `[unchecked]`, `[star]`
- **Mixed content** with tags, parentheses, and markers
- **Collapsible sections** using `+` prefix
- **Advanced markup** combining all features

## References

- [LiteTree Official Documentation](https://zhangfisher.github.io/lite-tree/)
- [LiteTree GitHub Repository](https://github.com/zhangfisher/lite-tree)
- [LiteTree Variables Guide](https://zhangfisher.github.io/lite-tree/guide/vars.html)