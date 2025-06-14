---
title: 侧边栏配置
description: CrychicDoc 侧边栏配置系统完全指南，从基础到高级的全面教程
---

# 侧边栏配置教程

::: alert {"type": "success", "title": "概述", "variant": "outlined"}
通过本教程，你将掌握 CrychicDoc 强大的侧边栏配置系统，学会创建结构清晰、易于导航的文档站点。
:::

Sidebar 的配置曾经非常的臃肿、难以维护，现已通过复杂的设计将其优化，使其能够通过最少的`Front-matter`配置来达到相同的效果。

## 配置系统概览

首先该系统面向使用者有着四种配置，分别为`Global Config`、`Root Config`、`Sub Config`、`Json Config`，优先级从小到大，优先级最高且存在的配置会覆盖掉优先级低的配置。

::: alert {"type": "info", "title": "💡 配置优先级", "variant": "outlined"}
**配置优先级（从低到高）：**
1. Global Config - 全局默认设置
2. Root Config - 根目录配置  
3. Sub Config - 子目录配置
4. JSON Config - 精确控制配置

**记忆法则**：越具体的配置优先级越高！
:::

@@@ dialog-def#config-overview {"title": "📊 配置系统架构图", "width": 800}
<LiteTree>
CrychicDoc 侧边栏配置系统
    Global Config (优先级1)                         // .sidebarrc.yml
        全站默认配置，影响所有目录
    Root Config (优先级2)                           // index.md + root: true
        定义侧边栏根节点
        支持分组和外部链接  
        作用于该根及所有子目录
    Sub Config (优先级3)                            // 普通 index.md
        调整特定目录行为
        覆盖继承的配置
    JSON Config (优先级4)                          // .vitepress/config/sidebar/
        locales.json                                  // 显示名称
        order.json                                    // 排序控制  
        collapsed.json                                // 折叠状态
        hidden.json                                   // 可见性控制
</LiteTree>

**使用场景：**
- **Global**: 设置全站统一的默认行为
- **Root**: 创建主要导航结构，添加外部链接
- **Sub**: 微调特定目录的展示效果
- **JSON**: 精确控制每个文件/目录的显示
@@@

想了解完整架构？:::dialog#config-overview 点击查看详细说明:::

## 快速开始

::: stepper
@tab 步骤 1：创建全局配置
在 `docs/` 目录下创建 `.sidebarrc.yml`：

```yaml
# docs/.sidebarrc.yml
defaults:
    maxDepth: 1
    collapsed: false
    hidden: false
```

@tab 步骤 2：设置根配置
在主要目录的 `index.md` 中添加：

```yaml
---
root: true
title: "我的文档"
maxDepth: 2
---
```

@tab 步骤 3：测试效果
启动开发服务器查看侧边栏变化：

```bash
npm run docs:dev
```
:::

## 🌐 Global Config（全局配置）

**位置**：`docs/.sidebarrc.yml`  
**优先级**：最低（1 级）  
**作用域**：整个文档站点

全局配置文件定义了整个站点的默认行为。所有目录都会继承这些设置，除非被更高优先级的配置覆盖。

### 配置示例

```yaml
# docs/.sidebarrc.yml
defaults:
    maxDepth: 0 # 侧边栏展开深度（0=仅当前层级，1=包含子目录）
    collapsed: true # 默认折叠状态
    hidden: false # 默认隐藏状态
    itemOrder: {} # 全局排序配置
```

### 可配置字段

| 字段        | 类型    | 默认值 | 说明               |
| ----------- | ------- | ------ | ------------------ |
| `maxDepth`  | number  | 0      | 侧边栏展开深度     |
| `collapsed` | boolean | true   | 目录项默认折叠状态 |
| `hidden`    | boolean | false  | 目录项默认隐藏状态 |
| `itemOrder` | object  | {}     | 全局项目排序配置   |

## Root Config（根配置）

**位置**：`index.md` frontmatter（包含 `root: true`）  
**优先级**：🔹 中低（2 级）  
**作用域**：声明为根的目录及其所有子目录

根配置将一个目录标记为侧边栏的根节点，该配置会影响该目录及其所有子目录的行为。

### 配置示例

```md
---
root: true # 声明为侧边栏根
title: "KubeJS 文档" # 显示标题
maxDepth: 2 # 展开到第二层
collapsed: false # 默认展开
priority: 100 # 排序优先级
groups: # 分组配置
    - title: "代码分享"
      path: "CodeShare/"
externalLinks: # 外部链接
    - text: "GitHub 地址"
      link: "https://github.com/KubeJS-Mods/KubeJS"
      priority: -1000
    - text: "官方 Wiki"
      link: "https://kubejs.com/wiki"
      priority: -999
---

# KubeJS 1.20.1 文档

这里是 KubeJS 1.20.1 版本的完整文档...
```

### 可配置字段

| 字段            | 类型         | 说明                            |
| --------------- | ------------ | ------------------------------- |
| `root`          | boolean      | **必须为 true**，标记为侧边栏根 |
| `title`         | string       | 侧边栏中显示的标题              |
| `hidden`        | boolean      | 是否隐藏此根节点                |
| `priority`      | number       | 排序优先级（数字越小越靠前）    |
| `maxDepth`      | number       | 展开深度                        |
| `collapsed`     | boolean      | 是否默认折叠                    |
| `groups`        | array        | 分组配置                        |
| `externalLinks` | array        | 外部链接配置                    |
| `itemOrder`     | array/object | 子项排序配置                    |

### 分组配置（Groups）

分组功能允许将子目录的内容提升为独立的顶级项目：

```yaml
groups:
    - title: "代码分享" # 分组显示名称
      path: "CodeShare/" # 子目录路径
      priority: 10 # 分组排序优先级
      maxDepth: 3 # 分组内容的展开深度
```

### 外部链接配置（External Links）

```yaml
externalLinks:
    - text: "GitHub 仓库" # 显示文本
      link: "https://github.com/example/repo" # 链接地址
      priority: -1000 # 排序优先级
      hidden: false # 是否隐藏
```

## Sub Config（子配置）

**位置**：任意目录的 `index.md` frontmatter（不包含 `root: true`）  
**优先级**：🔸 中高（3 级）  
**作用域**：当前目录及其子目录

子配置用于调整非根目录的行为，可以覆盖从上级目录或全局配置继承的设置。

在代码层面，它会覆盖`Directory Config`以修改该目录的相关配置。

### 配置示例

```md
---
title: "高级功能" # 自定义显示标题
hidden: false # 显示此目录
priority: 50 # 调整排序
maxDepth: 1 # 限制展开深度
collapsed: true # 默认折叠
itemOrder: # 子项排序
    - "setup.md"
    - "advanced.md"
    - "troubleshooting.md"
---

# 高级功能指南

这个目录包含了高级功能的详细说明...
```

### 可配置字段

| 字段        | 类型         | 说明               |
| ----------- | ------------ | ------------------ |
| `title`     | string       | 侧边栏中显示的标题 |
| `hidden`    | boolean      | 是否隐藏此目录     |
| `priority`  | number       | 排序优先级         |
| `maxDepth`  | number       | 展开深度           |
| `collapsed` | boolean      | 是否默认折叠       |
| `itemOrder` | array/object | 子项排序配置       |

## JSON Config（JSON 配置）

**位置**：`.vitepress/config/sidebar/{lang}/{path}/`  
**优先级**：🔹 最高（4 级）  
**作用域**：特定目录

JSON 配置提供了最细粒度的控制，可以覆盖所有其他配置。每个目录可以有多个 JSON 文件来控制不同的方面。

### 文件类型

#### 1. 本地化配置 - `locales.json`

控制目录和文件的显示名称：

```json
{
    "_self_": "整合包",
    "kubejs/": "KubeJS",
    "recommendation/": "推荐",
    "setup.md": "环境搭建",
    "advanced.md": "高级配置"
}
```

-   `_self_`：当前目录的显示名称
-   `path/`：子目录的显示名称
-   `file.md`：文件的显示名称

#### 2. 排序配置 - `order.json`

控制`SidebarItem`的`Priority`：

```json
{
    "setup.md": 1,
    "basic/": 2,
    "advanced/": 3,
    "kubejs/": 9007199254740991,
    "recommendation/": 9007199254740992
}
```

-   Priority越小，数字越靠前。
-   这并不符合常识过已没有修改的可能，
-   默认值：`9007199254740991`（JavaScript 最大安全整数）

#### 3. 折叠配置 - `collapsed.json`

控制目录项的折叠状态：

```json
{
  "basic/": false,     # 默认展开
  "advanced/": true,   # 默认折叠
  "api/": true
}
```

#### 4. 隐藏配置 - `hidden.json`

控制项目的可见性：

```json
{
  "draft.md": true,        # 隐藏草稿文件
  "internal/": true,       # 隐藏内部目录
  "deprecated/": true      # 隐藏废弃内容
}
```

### JSON 配置目录结构

:::demo JSON配置目录结构
<LiteTree>
.vitepress/config/sidebar/
    zh/                                 // 中文配置
        modpack/
            locales.json                // 本地化
            order.json                  // 排序
            collapsed.json              // 折叠状态
            hidden.json                 // 隐藏配置
            kubejs/
                locales.json
                order.json
        docs/
    en/                                 // 英文配置
        ...
</LiteTree>
:::

## 配置优先级详解

配置的应用顺序和优先级：

```
📊 优先级（低 → 高）：
1️⃣ Global Config     (.sidebarrc.yml)
2️⃣ Root Config       (root: true 的 index.md)
3️⃣ Sub Config        (普通 index.md)
4️⃣ JSON Config       (.vitepress/config/sidebar/)
```

### 优先级示例

假设有以下配置：

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

**最终效果**：

-   `maxDepth`: 1（Sub Config 覆盖 Root Config）
-   `collapsed`: false（Root Config 覆盖 Global Config）
-   `basic/` 目录折叠：true（JSON Config 优先级最高）

## 实用配置技巧

### 1. 创建多级导航

```yaml
# docs/zh/modpack/kubejs/index.md
---
root: true
title: "KubeJS"
maxDepth: 3
groups:
    - title: "1.20.1 版本"
      path: "1.20.1/"
      priority: 1
    - title: "1.21 版本"
      path: "1.21/"
      priority: 2
---
```

### 2. 添加外部资源链接

```yaml
externalLinks:
    - text: "📚 官方文档"
      link: "https://kubejs.com/"
      priority: -1000
    - text: "💬 Discord 社区"
      link: "https://discord.gg/lat"
      priority: -999
    - text: "🐛 问题反馈"
      link: "https://github.com/KubeJS-Mods/KubeJS/issues"
      priority: -998
```

### 3. 灵活的排序配置

```yaml
# 使用数组（简单排序）
itemOrder:
  - "introduction.md"
  - "getting-started.md"
  - "advanced/"
  - "examples/"

# 使用对象（精确控制）
itemOrder:
  "introduction.md": 1
  "getting-started.md": 2
  "advanced/": 100
  "examples/": 200
```

### 4. 条件性隐藏内容

```json
// hidden.json - 隐藏开发中的内容
{
    "wip/": true,
    "draft-feature.md": true,
    "experimental/": true
}
```

## 🎯 最佳实践

### 1. 配置层次规划

-   **Global Config**：设置整站的基本默认值
-   **Root Config**：定义主要栏目的结构和外链
-   **Sub Config**：调整特定目录的展示方式
-   **JSON Config**：精确控制显示名称和排序

### 2. 命名规范

-   目录名使用驼峰，例如：`GettingStart` 或 `gettingStart`。
-   JSON 配置中使用友好的显示名称

### 3. 性能优化

-   合理设置 `maxDepth`，避免过深的嵌套
-   对于大型目录，使用分组功能分解内容
-   利用 `hidden` 配置隐藏不必要的文件

### 4. 维护建议

-   定期检查和清理无用的 JSON 配置文件
-   使用 Git 跟踪配置文件的变更
-   建立配置文档，便于团队协作

::: alert {"type": "info", "title": "注意"}
一般可以通过`_self_` 来控制`Root`的`Json Config`。
:::

## ❓ 常见问题

### Q: 为什么我的配置没有生效？

A: 检查配置的优先级顺序，高优先级的配置会覆盖低优先级的配置。

### Q: 如何隐藏整个目录？

A: 在该目录的 `index.md` 中设置 `hidden: true`，或在对应的 `hidden.json` 中配置。

### Q: 外部链接的排序如何控制？

A: 使用 `priority` 字段，数值越小越靠前。建议使用负数让外链显示在最前面。

### Q: 如何调试配置问题？

A:

1. 检查浏览器控制台的错误信息
2. 确认文件路径和语法正确性
3. 使用开发模式查看实时效果
4. 逐级检查配置的优先级

---

通过合理使用这四种配置方式，你可以创建出结构清晰、易于导航的文档侧边栏。记住优先级规则，善用各种配置的特点，就能打造出完美的用户体验！
