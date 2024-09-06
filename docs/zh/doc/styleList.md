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

## 警告框

由本站维护成员[椰浆](https://www.mcmod.cn/author/24749.html)编写的`v-alert`插件提供，用于创建vuetify风格的警告容器。

:::: demo 示例
::: v-success 成功
这是成功样式
:::
::: v-info 提示
这是提示样式
:::
::: v-warning 警告
这是警告样式
:::
::: v-error 错误
这是错误样式
:::
::::