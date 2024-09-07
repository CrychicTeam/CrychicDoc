---
title: 文档样式一览
authors: 
  - skyraah
noguide: true
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

你可以在图片链接末尾使用`=widthxheight`来指定图片尺寸。

`width`和`height`都应该为数字并意味着像素单位的尺寸，并且它们两者都是可选的。整个标记应该通过空格与图片链接相分割。

::: demo 示例
![Logo](/logo.png =200x200)
![Logo](/logo.png =150x)
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

由本站维护成员[椰浆](https://www.mcmod.cn/author/24749.html)编写的`v-alert`插件提供，用于创建vuetify风格的警告容器。

>[!INFO]注
>此插件基于[`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

::::: demo 示例
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

### 配置语法 {#carousels-grammer}

`carousels`容器的配置项由跟随在容器声明后的`json`提供，使用`#`以连接配置与容器声明。

| 配置字段        | 用途                                    | 类型    | 省缺值   |
|----------------|----------------------------------------|---------|---------|
| `ratio`        | 设置横幅的比例，默认为16:9。                 | `number` | `16/9`  |
| `cycle`        | 设置横幅是否自动循环，默认为关闭。            | `boolean` | `false` |
| `interval`     | 设置横幅循环时停留的时间，默认为6秒。        | `number` | `6000`  |
| `arrows`       | 设置横幅导航控制按钮是否显示，hover为<br/>鼠标悬停在横幅上时显示，默认显示。 | `boolean` \| "`hover`" | `true`  |
| `undelimiters` | 设置横幅底部的控制条是否显示，默认为显示。      | `boolean` | `false` |

### 图片缩放兼容 {#img-zoom}

在横幅中插入图片有两种形式，分别为
::: code-group

```md [可缩放]
@tab
![img](你的图片路径)
```

```md [不可缩放]
@tab 你的图片路径
<!-- 该空行不可缺少，否则组件不会正确识别@tab标识 -->
```

:::

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

::::: demo 示例
::: justify
这是一个展示均分对齐的段落。通过CSS的text-align: justify属性，可以在VitePress中实现文字的两端对齐效果。
:::

:::::