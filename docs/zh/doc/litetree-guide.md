---
title: LiteTree 组件使用指南
description: 在 VitePress 中使用 LiteTree 创建树形结构的完整指南
layout: doc
---

# LiteTree 组件使用指南

LiteTree 是一个为 React/Vue/VitePress 设计的轻量级树组件，使用类似 YAML 的缩进格式而非 JSON，非常适合在 Markdown 文档中使用。

**基于**: [LiteTree 官方文档](https://zhangfisher.github.io/lite-tree/) 和 [GitHub 仓库](https://github.com/zhangfisher/lite-tree)

## 概述

**主要特性:**
- 体积小，无第三方依赖
- 支持 React/Vue 版本
- 使用 `lite` 格式（基于缩进）完美适配 Markdown
- 支持自定义节点样式、标签、注释、图标
- 变量系统支持样式和图标定义
- 内置标记符表示不同状态
- 对非标准数据具有良好的容错性

## 基础语法

### 简单树形结构

:::demo 基础树形结构
<LiteTree>
A公司
    行政中心
        总裁办
        人力资源部
        财务部
    市场中心
        市场部
        销售部
    研发中心
        开发团队
        测试团队
</LiteTree>
:::

### 带标记符的树

LiteTree 支持各种内置标记符来表示不同状态:

:::demo 标准标记符
<LiteTree>
项目状态
    已完成任务      //v    成功标记
    新增功能        //+    添加标记
    废弃代码        //-    删除标记
    发现错误        //x    错误标记
    修改文件        //*    修改标记
    重要项目        //!    重要标记
</LiteTree>
:::

**可用标记符:**
- `//+` - 添加/新增项目 (绿色加号图标)
- `//-` - 删除/移除项目 (红色减号图标)
- `//x` - 错误/失败项目 (红色X图标)
- `//v` - 成功/完成项目 (绿色对勾图标)
- `//*` - 修改/变更项目 (橙色星号图标)
- `//!` - 重要/优先项目 (红色感叹号图标)

## 变量系统

LiteTree 支持三种类型的变量，必须在 `---` 分隔符之前定义:

### 样式变量 (`#name=styles`)

定义可重用的 CSS 样式:

:::demo 样式变量
<LiteTree>
// 定义样式变量
#important=color:red;font-weight:bold;background:#ffe6e6;padding:2px 6px;border-radius:3px;
#success=color:green;font-weight:bold;background:#e6ffe6;padding:2px 6px;border-radius:3px;
#warning=color:orange;background:#fff3e0;padding:2px 6px;border-radius:3px;
---
项目文件
    {#important}关键文件
    {#success}已完成文件
    {#warning}需要审查
</LiteTree>
:::

### 类变量 (`.name=styles`)

定义 CSS 类:

:::demo 类变量
<LiteTree>
// 定义类变量
.folder=color:#1976d2;font-weight:500;
.file=color:#666;
.important=font-weight:bold;color:#d32f2f;
---
{.folder}源代码
    {.important}main.js
    {.file}config.json
    {.file}package.json
</LiteTree>
:::

### 图标变量 (`name=base64data`)

使用 base64 编码的 SVG 定义自定义图标:

:::demo 图标变量
<LiteTree>
// 定义图标变量
folder=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTEwIDRIOGEyIDIgMCAwIDAtMiAydjEyYTIgMiAwIDAgMCAyIDJoOGEyIDIgMCAwIDAgMi0yVjhhMiAyIDAgMCAwLTItMmgtM2wtMi0yWiIvPjwvc3ZnPg==
file=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9ImN1cnJlbnRDb2xvciIgZD0iTTE0IDJINmEyIDIgMCAwIDAtMiAydjE2YTIgMiAwIDAgMCAyIDJoMTJhMiAyIDAgMCAwIDItMlY4bC02LTZtNCA5VjlsNCA0aC00WiIvPjwvc3ZnPg==
js=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9IiNmN2RmMWUiIGQ9Ik0zIDNoMTh2MThIM1ptMTYuNTI1IDE0LjVjLS4zLS4zNTQtLjc5NS0uNjI5LTEuNzE3LS42MjljLS44ODEgMC0xLjQzOS4zMTgtMS40MzkuNzE4YzAgLjM5Ni4zNzMuNjM3IDEuMTU2Ljk2N2MxLjMzMi41ODYgMi4yODEgMS4wOTMgMi4yODEgMi4zOGMwIDEuMzItMS4yMDMgMi4xNDMtMi45NzQgMi4xNDNjLTEuMjEzIDAtMi4yNzEtLjQ2Mi0yLjk1LTEuMDc0bC44NzUtMS4yNzNjLjQzMy4zODkgMS4wNjQuNzI0IDEuNjY0LjcyNGMuNzA2IDAgMS4wNjQtLjMzMSAxLjA2NC0uNzMzYzAtLjQ0OS0uMzc2LS43MjQtMS4yNDUtMS4wMzNjLTEuMzI1LS40ODgtMi4xMzItMS4yNS0yLjEzMi0yLjM2M2MwLTEuMzk0IDEuMDI5LTIuMTQzIDIuODU2LTIuMTQzYzEuMDY0IDAgMS43NDUuMzI4IDIuMzc3Ljg1OWwtLjgzIDEuMjQxWm0tNS44NDUtLjMzNWMuMzY2LjgxNS4zNjYgMS41NzcuMzY2IDIuNDd2My45MDZoLTEuODc2VjE5LjZjMC0xLjUyNy0uMDYtMi4xOC0uNTUtMi40OGMtLjQxLS4yODgtMS4wNzYtLjI3NC0xLjYxOC0uMTA3Yy0uMzc4LjExNy0uNzEzLjMzNS0uNzEzIDEuMDc0djUuMDU2SDYuNDI3VjEyLjgyaDEuODc2djIuMTEzYy43NDctLjM5OSAxLjU3Ny0uNzM4IDIuNjQ1LS43MzhjLjc2NCAwIDEuNTc3LjI1MyAyLjA2OS43ODdjLjQ5OC41NTIuNjI2IDEuMTU3LjcyMyAxLjk5MVoiLz48L3N2Zz4=
---
[folder] 前端项目
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

## 内联样式

### 直接颜色样式

使用 `{color:value}` 语法直接对文本应用样式:

:::demo 内联颜色
<LiteTree>
项目状态
    {color:green}已完成功能
    {color:orange}进行中
    {color:red}严重问题
    {color:blue;font-weight:bold}重要说明
</LiteTree>
:::

### 混合样式

结合变量、内联样式、图标和标记符:

:::demo 完整示例
<LiteTree>
// 样式定义
#new=color:white;background:#4caf50;padding:1px 4px;border-radius:2px;font-size:12px;
#deprecated=color:white;background:#f44336;padding:1px 4px;border-radius:2px;font-size:12px;
.important=font-weight:bold;color:#1976d2;
// 图标定义
vue=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMjQgMjQiPjxwYXRoIGZpbGw9IiM0Y2FmNTAiIGQ9Ik0yIDIwaDIwTDEyIDR6Ii8+PC9zdmc+
ts=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxZW0iIGhlaWdodD0iMWVtIiB2aWV3Qm94PSIwIDAgMTUgMTUiPjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iIzMxNzhDNiIgZD0iTTEyLjUgOHYtLjE2N2MwLS43MzYtLjU5Ny0xLjMzMy0xLjMzMy0xLjMzM0gxMGExLjUgMS41IDAgMSAwIDAgM2gxYTEuNSAxLjUgMCAwIDEgMCAzaC0xQTEuNSAxLjUgMCAwIDEgOC41IDExTTggNi41SDNtMi41IDBWMTNNMS41LjVoMTN2MTRIOS41eiIvPjwvc3ZnPg==
---
{.important}CrychicDoc 项目
    .vitepress                    // {.important}配置目录
        config
            [ts] index.ts         // {#new}更新配置
        plugins                   // {.important}自定义插件
            [ts] custom-alert.ts  // {#new}警告插件
            [ts] dialog.ts        // {#new}对话框插件
        theme
            [vue] components      // {.important}Vue 组件
                [vue] CustomAlert.vue  // {#new}新组件
                [vue] Dialog.vue       // {#new}新组件
    docs
        zh                        // 中文文档
            styleList.md          // {#deprecated}需要更新
        en                        // 英文文档
            samples.md            // {#new}新示例
            {color:blue}litetree-guide.md   // {#new}本指南
    package.json                  //v    项目配置
    README.md                     //!    {.important}重要文档
</LiteTree>
:::

## 常见用例

### 项目文件结构

:::demo 项目结构
<LiteTree>
// 文件类型样式
.folder=color:#1976d2;font-weight:500;
.file=color:#666;
.config=color:#f57c00;font-weight:500;
.doc=color:#8bc34a;
// 状态样式
#completed=color:green;background:#e6ffe6;padding:1px 3px;border-radius:2px;font-size:11px;
#inprogress=color:orange;background:#fff3e0;padding:1px 3px;border-radius:2px;font-size:11px;
#todo=color:red;background:#ffe6e6;padding:1px 3px;border-radius:2px;font-size:11px;
---
{.folder}我的项目
    {.folder}src                  //v    {#completed}结构完成
        {.folder}components       //+    {#inprogress}添加组件中
            {.file}Header.vue     //v    {#completed}已完成
            {.file}Footer.vue     //+    {#todo}待办
        {.folder}pages
            {.file}Home.vue       //v    {#completed}已完成
            {.file}About.vue      //*    {#inprogress}更新中
    {.config}package.json         //v    {#completed}已配置
    {.config}vite.config.js       //*    {#inprogress}优化中
    {.doc}README.md               //!    {#todo}需要文档
</LiteTree>
:::

### 团队组织

:::demo 团队结构
<LiteTree>
// 团队角色样式
#lead=color:white;background:#1976d2;padding:2px 6px;border-radius:3px;font-size:12px;
#senior=color:#1976d2;background:#e3f2fd;padding:2px 6px;border-radius:3px;font-size:12px;
#junior=color:#666;background:#f5f5f5;padding:2px 6px;border-radius:3px;font-size:12px;
---
开发团队
    前端团队                      // {#lead}团队负责人: 张三
        React 开发者              //+    团队扩充中
            李四                  // {#senior}高级开发
            王五                  // {#junior}初级开发
        Vue 开发者                //v    团队完整
            赵六                  // {#senior}高级开发
            钱七                  // {#junior}初级开发
    后端团队                      // {#lead}团队负责人: 孙八
        API 开发                  //*    重构中
            周九                  // {#senior}高级开发
            吴十                  // {#junior}初级开发
        数据库团队                //!    关键项目
            郑一                  // {#senior}高级开发
</LiteTree>
:::

## VSCode 代码片段

项目包含 LiteTree 的完整 VSCode 代码片段。主要片段包括:

- **`@file-tree`** - 基础树结构
- **`@file-tree-advanced`** - 带变量和样式的树
- **`@lite-style-var`** - 样式变量定义
- **`@lite-class-var`** - 类变量定义
- **`@lite-icon-var`** - 图标变量定义
- **`@icon-folder`**, **`@icon-file`**, **`@icon-js`**, **`@icon-ts`**, **`@icon-vue`** - 常用图标
- **`@lite-status-styles`** - 预定义状态样式
- **`@lite-filetype-styles`** - 预定义文件类型样式

## 最佳实践

1. **使用变量**: 在顶部定义可重用的样式和图标
2. **一致命名**: 为变量使用清晰、描述性的名称
3. **逻辑分组**: 将相关项目分组在一起
4. **恰当标记**: 一致使用标记符表示状态
5. **颜色编码**: 有意义地使用颜色 (红色=错误, 绿色=成功等)
6. **图标使用**: 适度使用图标以避免杂乱
7. **缩进**: 保持一致的缩进 (推荐4个空格)

## 故障排除

### 变量不工作
- 确保变量在 `---` 分隔符之前定义
- 检查变量定义中的语法错误
- 验证图标变量的 base64 编码

### 样式问题
- 确保 CSS 属性有效
- 检查样式定义中是否缺少分号
- 测试内联样式以调试变量问题

### 图标不显示
- 验证 base64 编码正确
- 确保 SVG 格式正确
- 检查图标名称是否与使用匹配

## 参考资料

- [LiteTree 官方文档](https://zhangfisher.github.io/lite-tree/)
- [LiteTree GitHub 仓库](https://github.com/zhangfisher/lite-tree)
- [LiteTree 变量指南](https://zhangfisher.github.io/lite-tree/guide/vars.html) 