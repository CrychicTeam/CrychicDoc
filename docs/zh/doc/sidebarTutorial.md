---
layout: doc
title: 侧边栏设置教程
authors:
  - M1hono
prev:
  text: 类型检查示例
  link: /zh/doc\test
next:
  text: 项目合作教程
  link: /zh/doc\index
---

# VitePress 侧边栏生成器前置配置指南

本文档介绍如何使用前置配置来配置 VitePress 侧边栏生成器。通过在 Markdown 文件的前置配置中添加特定字段，特别是 `index.md` 文件，您可以自定义侧边栏的行为和显示。

## 基本配置

在每个 Markdown 文件的开头，使用 `---` 来分隔前置配置部分：

```yaml
---
# 在这里添加您的前置配置
---
```

# 文档内容从这里开始

可用的前置配置字段

1. title

目的：设置文档的标题
类型：字符串
示例：
yamlCopytitle: 入门指南


2. sidetitle

目的：设置侧边栏中显示的标题（如果未设置，则使用标题）
类型：字符串
示例：
yamlCopysidetitle: 快速入门


3. sidebarorder

目的：自定义当前目录中文档和子目录的顺序
类型：对象
示例：
yamlCopysidebarorder:
    index: -1
    introduction: 1
    advanced: 2


4. tagDisplay

目的：启用标签分组显示
类型：布尔值
示例：
yamlCopytagDisplay: true


5. back

目的：自定义返回到父目录的路径
类型：字符串
示例：
yamlCopyback: /guide/


6. autoPN

目的：自动生成上一篇/下一篇导航
类型：布尔值
示例：
yamlCopyautoPN: true


7. tagorder

目的：自定义标签的顺序
类型：对象
示例：
yamlCopytagorder:
    Basics: 1
    Advanced: 2


8. folderBlackList

目的：指定要在侧边栏中排除的文件夹列表
类型：数组
示例：
yamlCopyfolderBlackList:
    - private
    - drafts


9. generateSidebar

目的：在侧边栏中包含当前的 index.md 文件
类型：布尔值
默认值：false
示例：
yamlCopygenerateSidebar: true


10. tag

目的：为文档指定标签（用于标签分组显示）
类型：字符串
示例：
yamlCopytag: 基础知识


完整示例
以下是一个包含所有可用配置的 index.md 文件的前置配置示例：

```yaml
---
title: VitePress 指南
sidetitle: 用户指南
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
tag: 文档
---
```

```md
# VitePress 指南

本文档将帮助您入门 VitePress...
使用这些前置配置，您可以精确控制文档在侧边栏中的显示方式，包括顺序，启用标签分组和自动生成导航等高级功能。
此 Markdown 文件重点介绍如何使用前置配置来配置侧边栏生成器。它提供了关于每个可用前置配置字段的详细信息。
```
