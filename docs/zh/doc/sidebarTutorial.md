---
layout: doc
title: 侧边栏设置教程
noguide: true
---

# VitePress 侧边栏生成器配置指南 {#guide}

## 简述 {#info}

基于多方原因考虑，本站中所有侧边栏均由开发组自行编写的侧边栏生成器生成。并未使用Vitepress提供的侧边栏插件。下文对于侧边栏生成器配置提供了详尽的解析。

## 结构与原理 {#principle}

侧边栏生成器的入口由`index.ts`文件构成，其完整路径为：`CrychicDoc/.vitepress/index.ts`。

其代码结构如下：

::: code-group

```ts [核心代码]
const dirs = [
    "modpack/kubejs"
];
```

```ts{6-8} [完整代码]
import sidebar from "./utils/sidebarGenerator"
import md from "./utils/mdParser"
import Path from "path";
import fs from "fs";

const dirs = [// [!code focus:3]
    "modpack/kubejs"
];

export default function sidebars(lang: string): {} {
    let ISidebar = {};
    dirs.forEach(dir => {
        const generator = new sidebar(`docs/${lang}/${dir}`, true);
        ISidebar[`${lang}/${dir}/`] = [generator.sidebar]
    })
    return ISidebar;
}
function logger(string: string, name: string): void {
    fs.writeFileSync(Path.join(__dirname, name), `${string}\n`, { flag: 'w+' });
}
```

:::

当您需要增加一个新的自动扫描目录时，您应在该数组中添加新的路径字符串。您新增的路径经过扫描会自动生成侧边栏，并在对应的子域名中显示。

::: info 示例

`modpack/kubejs`所生成的侧边栏将会在`https://docs.mihono.cn/<lang>/modpack/kubejs`中出现。

:::

::: warning 注意

路径字符串不应包含`docs/<lang>`目录  
在您添加路径之前，请确保您的路径是真实存在的。

:::

### 文件扫描 {#file-scan}

目标目录的文件将会自动被生成器扫描，并生成相应的侧边栏，每个文件都有以下[frontmatter](#frontmatter)配置字段。

::: tip 提示

本站同时支持Vitepress的原生frontmatter样式，详情请见[此处](https://vitepress.dev/zh/reference/frontmatter-config)

:::

| 配置字段      | 用途                             | 类型      | 省缺值     |
|-----------|--------------------------------|---------|---------|
| `title`   | 设置侧边栏中显示的标题（如未设置则使用文件名）        | string  | `N/A`   |
|`backPath`|设置该界面点击BackButton后前往的位置|string|`N/A`|
| `noguide` | 设置该文件是否添加到侧边栏中                 | boolean | `false` |
| `authors`  | 设置该文章额外的作者，显示在贡献者栏，配置可参考[此处](https://nolebase-integrations.ayaka.io/pages/zh-CN/integrations/vitepress-plugin-git-changelog/configure-vite-plugins#%E9%80%89%E9%A1%B9-mapcontributors-%E4%B8%BA%E8%B4%A1%E7%8C%AE%E8%80%85%E6%B7%BB%E5%8A%A0%E6%95%B0%E6%8D%AE%E6%98%A0%E5%B0%84)| string[]  | `N/A`   |
| `showComment`  | 是否显示评论区 | boolean  | `true`   |
| `gitChangelog`  | 是否显示贡献者和页面历史 | boolean  | `true`   |

::: details 示例

```yaml
---
title: 示例
backPath: ../
noguide: true
authors: ['M1hono', 'skyraah'] # 可在common-config中额外配置 但还是请尽量使用Github ID。
showComment: false
gitChangelog: false
---
```

:::

### 目录扫描 {#dir-scan}

在本站生成器中，目录不会被自动递归扫描，您需要在目录目录上级创建`index.md`并对其[frontmatter](#frontmatter)进行配置，手动指定需要进入并扫描的目录。

#### 解析 {#index}

`index.md`中的frontmatter除了支持常规的文件配置格式，还会额外读取一项名为`root`的`<Object>`对象，其配置字段及代码声明如下。

:::: details 代码声明

::: code-group

```ts [root对象]
interface Index {
    root: {
        title: string,
        collapsed?: boolean; 
        children: SubDir[]
    }
}
```

```ts [SubDir对象]
interface SubDir {
    title: string;
    path: string;
    noScan?: boolean;
    collapsed?: boolean;
    file?: string;
    children?: SubDir[];
}
```

:::

::::

::: tabs

== root

| 配置字段        | 用途                          | 类型      | 省缺值   |
|-------------|-----------------------------|---------|-------|
| `title`     | 设置该侧边栏的名称，非必填字段             | string  | `N/A` |
| `collapsed` | 设置该侧边栏默认展开/收缩，非必填字段，留空以禁用展开 | boolean | `N/A` |
| `children`  | 设置该目录下哪些子目录应被递归，在root为必填字段        | SubDir  | `N/A` |
  
== SubDir

| 配置字段        | 用途                              | 类型      | 省缺值     |
|-------------|---------------------------------|---------|---------|
| `title`     | 设置该子侧边栏的名称，必填字段                 | string  | `N/A`   |
| `collapsed` | 设置该子侧边栏默认展开/收缩，非必填字段，留空以禁用展开    | boolean | `N/A`   |
| `path`      | 设置该子侧边栏的目录路径，必填字段               | string  | `N/A`   |
| `noScan`    | 设置是否应自动扫描该目录内的所有文件              | boolean | `false` |
| `file`      | 设置该子侧边栏名称连接的文件，非必填字段，文件需处于该子目录内 | string  | `N/A`   |
| `children`  | 设置该目录下哪些子目录应被递归                   | SubDir  | `N/A` | 

:::

::: details 示例

```yaml
---
noguide: true
root:
  title: example
  collapsed: true
  children:
      - title: subDir a
        path: test
        collapsed: true  
        children:
            - title: subDir back
              path: test
              children:
                  - title: subDir back
                    path: test
                    file: README
      - title: subDir back
        path: test
        noScan: true
        file: README
---
```

:::

::: tip 提示

如果您使用VSCode编辑器参与本站的编撰工作，您可以调用我们预设的代码片段，对于类型为非对象的配置字段，您可以直接输入其名称唤起补全。对于`root`对象以及`SubDir`对象，您分别可以使用`@root`以及`@subdir`来唤出相应的片段。

:::

## frontmatter声明 {#frontmatter}

在每个 Markdown 文件的开头，使用 `---` 来创建frontmatter配置

```yaml
---
# 在这里添加您的frontmatter
---
```

<!-- ## 基本配置

在每个 Markdown 文件的开头，使用 `---` 来分隔前置配置部分：

```yaml
---
# 在这里添加您的前置配置
---
```

# 文档内容从这里开始

可用的前置配置字段

| 配置字段       | 目的                                  | 类型     | 示例 |
| ------------- | ----------------------------------- | ------ | ---- |
| `title`       | 设置文档的标题                          | 字符串    | `title: 入门指南` |
| `sidetitle`   | 设置侧边栏中显示的标题（如果未设置，则使用标题） | 字符串    | `sidetitle: 快速入门` |
| `sidebarorder`| 自定义当前目录中文档和子目录的顺序       | 对象      | `sidebarorder: \n    index: -1 \n    introduction: 1 \n    advanced: 2` |
| `tagDisplay`  | 启用标签分组显示                        | 布尔值    | `tagDisplay: true` |
| `back`        | 自定义返回到父目录的路径                 | 字符串    | `back: /guide/` |
| `autoPN`      | 自动生成上一篇/下一篇导航                | 布尔值    | `autoPN: true` |
| `tagorder`    | 自定义标签的顺序                        | 对象      | `tagorder: \n    Basics: 1 \n    Advanced: 2` |
| `folderBlackList` | 指定要在侧边栏中排除的文件夹列表        | 数组      | `folderBlackList: \n    - private \n    - drafts` |
| `generateSidebar` | 在侧边栏中包含当前的 `index.md` 文件    | 布尔值    | `generateSidebar: true` |
| `tag`         | 为文档指定标签（用于标签分组显示）         | 字符串    | `tag: 基础知识` |

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
``` -->
