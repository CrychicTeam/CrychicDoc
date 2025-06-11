---
title: 文档样式一览
---
# {{ $frontmatter.title }}

本文只列举本站中非Markdown官方提供的语法，若需要官方语法参考，请参考：[官方教程](https://markdown.com.cn/)。

## VitePress官方拓展 {#vp}

<Linkcard url="https://vitepress.dev/zh/guide/markdown" title="Markdown 扩展" description="VitePress 带有内置的 Markdown 扩展。" logo="https://vitepress.yiov.top/logo.png"/>

## 选项卡 {#tab}

由`vitepress-plugin-tabs`插件提供，用于创建一个可带标签的选项卡。

:::: demo 无标签选项卡
:::tabs
== tab a
a content
== tab b
b content
:::
::::

:::: demo 标签选项卡
:::tabs key:ab
== tab a
a content
== tab b
b content
:::

:::tabs key:ab
== tab a
a content 2
== tab b
b content 2
:::
::::

## 代办清单 {#todo}

由`markdown-it-task-lists`插件提供，用于创建github风格的代办清单。

::: demo 示例
- [ ] 未完成
- [x] 已完成
:::
<!-- 
## 定义列表 {#deflist}

由`markdown-it-deflist`插件提供，用于创建基于[Pandoc](https://pandoc.org/MANUAL.html#definition-lists)语法的定义列表

::: demo 示例

Term 1

:   Definition 1

Term 2 with *inline markup*

:   Definition 2

        { some code, part of Definition 2 }

    Third paragraph of definition 2.
::: -->

## 略缩词 {#abbr}

由`markdown-it-abbr`插件提供，用于创建略缩词。

::: demo 示例
*[HTML]: Hyper Text Markup Language
*[W3C]:  World Wide Web Consortium
The HTML specification is maintained by the W3C.
:::

## 图片尺寸 {#img-size}

由`@mdit/plugin-img-size`插件提供，支持插入图片时设置图片尺寸。

在图片替代文字后面添加 =widthxheight，并用空格分隔。

width 和 height 都应该是数字，单位为像素，并且都是可选的。

```md
![替代文字 =200x300](/example.png)
![替代文字 =200x](/example.jpg "标题")
![替代文字 =x300](/example.bmp)
```

```html
<img src="/example.png" alt="替代文字" width="200" height="300" />
<img src="/example.jpg" alt="替代文字" title="标题" width="200" />
<img src="/example.bmp" alt="替代文字" height="300" />
```

::: demo 示例
![Logo =200x200](/logo.png)
![Logo =150x](/logo.png)
:::

## 对齐 {#align}

由`@mdit/plugin-align`插件提供，能够用容器控制文字的对齐方式。

>[!INFO]注
>此插件基于[`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

:::: demo 示例
::: left
左对齐的内容
:::

::: center
居中的内容
:::

::: right
右对齐的内容
:::

::: justify
两端对齐的内容
:::
::::

## 隐藏内容 {#spoiler}

*[隐藏内容]: 又名涂黑块
由`@mdit/plugin-spoiler`插件提供，用于创建 隐藏内容 。

::: demo 示例
VuePress Theme Hope !!十分强大!!。
:::

## 角标 {#sub&sup}

由`@mdit/plugin-sub`与`@mdit/plugin-sup`插件提供，用于创建上下角标。

::: demo 示例
下角标：H~2~O
上角标：19^th^
:::

## 旁注标记{#ruby}

由`@mdit/plugin-ruby`插件提供，用于创建旁注标记。

::: demo 示例
{中国:zhōng|guó}
:::

## 示例 {#demo}

由`@mdit/plugin-demo`插件提供库支持，本站维护组成员[椰浆](https://www.mcmod.cn/author/24749.html)提供样式支持，用于创建一个可预览的Markdown示例

:::: demo 示例
::: demo 示例
这是示例！
:::
::::

## 步骤组 {#stepper}

由`@mdit/plugin-tab`插件提供库支持，本站维护组成员[椰浆](https://www.mcmod.cn/author/24749.html)提供样式支持，用于创建一个表示步骤的选项卡

:::: demo 示例
::: stepper
@tab 第一步
这是第一步
@tab 第二步
这是第二步
@tab 第三步
这是第三步
:::
::::

## 标记 {#mark&ins}

由`@mdit/plugin-mark`和`@mdit/plugin-ins`插件提供，用于标记和突出显示内容。

::: demo 示例
VuePress Theme Hope ==十分强大==。  
VuePress Theme Hope ++十分++ 强大。
:::

## 警告框 {#alert}

本站提供两套警告框系统：传统的`v-alert`格式和新的带JSON配置的`CustomAlert`。

### 传统v-alert格式

由本站维护成员[椰浆](https://www.mcmod.cn/author/24749.html)编写的`v-alert`插件提供，用于创建vuetify风格的警告容器。

>[!INFO]注
>此插件基于[`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

::::: demo 传统警告框示例
::: v-success 成功
这是成功样式
:::
::: v-info 
这是提示样式，默认标题
:::
::: v-warning 警告
这是警告样式,支持其它++插件++
:::
:::: v-error 错误
这是错误样式
::: v-success 成功
可以嵌套
:::
::::
:::::

### 带JSON配置的CustomAlert

新的`CustomAlert`插件通过JSON语法提供高级配置，支持主题、变体、图标等广泛的自定义选项。

>[!INFO]注
>此插件使用自定义容器实现，确保JSON配置的可靠解析。

#### 基础用法

::::: demo 基础警告框类型
::: alert {"type": "success", "title": "成功警告"}
这是带JSON配置的成功警告框。
:::

::: alert {"type": "info", "title": "信息提示"}
这是支持**Markdown**内容的信息警告框。
:::

::: alert {"type": "warning", "title": "警告提示"}
这是带++增强++格式的警告框。
:::

::: alert {"type": "error", "title": "错误提示"}
这是支持完整Markdown语法的错误警告框。
:::
:::::

#### 高级样式

::::: demo 高级样式选项
::: alert {"type": "success", "title": "调色变体", "variant": "tonal", "density": "comfortable"}
使用调色变体和舒适密度。
:::

::: alert {"type": "info", "title": "轮廓带边框", "variant": "outlined", "border": "start"}
轮廓变体配合起始边框位置。
:::

::: alert {"type": "warning", "title": "自定义颜色", "color": "purple", "icon": "mdi-star"}
自定义紫色配合星形图标。
:::
:::::

#### 主题颜色

::::: demo 主题感知颜色
::: alert {"type": "success", "title": "亮色主题", "lightColor": "#e8f5e8", "darkColor": "#2d4a2d"}
自适应亮色和暗色主题的自定义颜色。
:::

::: alert {"type": "info", "title": "蓝色主题", "lightColor": "#e3f2fd", "darkColor": "#1a237e", "variant": "outlined"}
蓝色主题警告框配合轮廓变体。
:::
:::::

#### 配置选项

| 属性 | 类型 | 描述 | 可选值 |
|:---|:---|:---|:---|
| `type` | `string` | 警告框类型/颜色 | `success`, `info`, `warning`, `error` |
| `title` | `string` | 警告框标题 | 任意字符串 |
| `text` | `string` | 文本内容（替代插槽） | 任意字符串 |
| `variant` | `string` | 视觉样式变体 | `flat`, `elevated`, `tonal`, `outlined`, `text`, `plain` |
| `density` | `string` | 间距密度 | `default`, `comfortable`, `compact` |
| `border` | `string|boolean` | 边框位置 | `start`, `end`, `top`, `bottom`, `true`, `false` |
| `color` | `string` | 自定义颜色 | 任意CSS颜色值 |
| `lightColor` | `string` | 亮色主题颜色 | 任意CSS颜色值 |
| `darkColor` | `string` | 暗色主题颜色 | 任意CSS颜色值 |
| `icon` | `string` | 自定义图标 | Material Design图标名称（如`mdi-star`） |

## 滚动横幅 {#carousels}

由本站维护成员[椰浆](https://www.mcmod.cn/author/24749.html)编写的`carousels`插件提供，用于创建自定义内容的滚动横幅。

>[!INFO]注
>此插件基于[`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

:::: demo 示例
::: carousels#{"cycle": true, "interval": 2800, "undelimiters": true}
@tab
![1](https://docs.mihono.cn/mods/adventure/champions-unofficial/1.png)
@tab
![2](https://docs.mihono.cn/mods/adventure/champions-unofficial/2.png)
:::
::::

## 内嵌外链 {#iframe}

:::: demo 示例
:::iframes#{"src": "https://misode.github.io/"}
:::
::::

### 配置语法 {#iframe-grammer}

`iframes` 容器的配置项由跟随在容器声明后的 `json` 提供，使用 `#` 以连接配置与容器声明。

| 配置字段   | 用途                   | 类型           | 省缺值    |
| ---------- | ---------------------- | -------------- | --------- |
| `src`      | 网页的链接，必填       | `string`       | `N/A`     |
| `height`   | 设置元素的高度。       | `length value` | `140px`   |

## 对话框 {#dialog}

由本站维护成员[客服](https://github.com/M1hono)编写的`dialog`插件提供，用于创建可从任何地方触发的复杂对话框。

### 语法

插件包含两个部分：**定义**和**触发**。

1.  **定义对话框内容 (`dialog-def`)**
    使用块级容器定义对话框的内容和属性。

    ```markdown
    @@@ dialog-def#my-dialog {"title": "My Dialog", "width": 500}
    # 这是对话框标题

    这是 **markdown** 内容，支持所有标准语法。

    - 列表
    - 代码块
    - 等等...
    @@@
    ```

2.  **触发对话框 (`dialog`)**
    使用内联语法在文本中创建一个链接来打开对话框。

    ```markdown
    点击 :::dialog#my-dialog 这里::: 打开对话框。
    ```

### 配置

配置通过 `dialog-def` 容器后的 JSON 对象提供。

| 配置字段 | 用途 | 类型 | 省缺值 |
|:---|:---|:---|:---|
| `title` | 对话框的标题 | `string` | `N/A` |
| `width` | 对话框的最大宽度 | `string` \| `number` | `800` |
| `fullscreen` | 是否以全屏模式显示 | `boolean` | `false` |
| `persistent` | 点击外部是否关闭对话框 | `boolean` | `false` |

### 示例

:::: demo 示例
@@@ dialog-def#style-guide-demo {"title": "对话框演示", "width": 600}
# 欢迎!

这是一个通过 style guide 触发的对话框。

- 你可以在这里放置任何 Markdown 内容。
- `code blocks` 也会被正确渲染。

```js
console.log("Hello from dialog!");
```
@@@

点击 :::dialog#style-guide-demo 这里::: 来查看效果。
::::

## 卡片 {#card}

由本站维护成员[椰浆](https://www.mcmod.cn/author/24749.html)编写的`card`插件提供，用于创建自定义内容的卡片。

>[!INFO]注
>此插件基于[`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

::::: demo 示例
:::text 标题#副标题
这是text样式
:::
:::flat 只有标题
这是flat样式
:::
:::elevated #只有副标题
这是elevated样式
:::
:::tonal 标题#副标题
这是tonal样式
:::
:::outlined
这是outlined样式，没有标题和副标题
:::
::::plain 标题#副标题
这是plain样式  
只能使用双空格或`\`换行
:::tonal 嵌套
支持嵌套，支持!!别的插件!!
:::
::::
:::::

## 文本均分

:::: demo 示例
::: justify
这是一个展示均分对齐的段落。通过CSS的text-align: justify属性，可以在VitePress中实现文字的两端对齐效果。
:::
::::

