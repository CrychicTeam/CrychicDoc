---
title: Document Style Overview
---
# {{ $frontmatter.title }}

This document only lists the syntax that is not officially provided by Markdown on this site. For official syntax reference, please refer to: [Official Tutorial](https://markdown.com.cn/).

## VitePress Official Extensions {#vp}

<Linkcard url="https://vitepress.dev/zh/guide/markdown" title="Markdown Extensions" description="VitePress comes with built-in Markdown extensions." logo="https://vitepress.yiov.top/logo.png"/>

## Tabs {#tab}

Provided by the `vitepress-plugin-tabs` plugin, used to create labeled tabs.

:::: demo Unlabeled Tabs
:::tabs
== tab a
a content
== tab b
b content
:::
::::

:::: demo Labeled Tabs
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

## Task Lists {#todo}

Provided by the `markdown-it-task-lists` plugin, used to create GitHub-style task lists.

::: demo Example
- [ ] Incomplete
- [x] Completed
:::

## Abbreviations {#abbr}

Provided by the `markdown-it-abbr` plugin, used to create abbreviations.

::: demo Example
*[HTML]: Hyper Text Markup Language
*[W3C]:  World Wide Web Consortium
The HTML specification is maintained by the W3C.
:::

## Image Size {#img-size}

Provided by the `@mdit/plugin-img-size` plugin, supports setting image dimensions when inserting images.

Add =widthxheight after the image alt text, separated by a space.

Both width and height should be numbers in pixels and are optional.

```md
![Alt text =200x300](/example.png)
![Alt text =200x](/example.jpg "Title")
![Alt text =x300](/example.bmp)
```

```html
<img src="/example.png" alt="Alt text" width="200" height="300" />
<img src="/example.jpg" alt="Alt text" title="Title" width="200" />
<img src="/example.bmp" alt="Alt text" height="300" />
```

::: demo Example
![Logo =200x200](/logo.png)
![Logo =150x](/logo.png)
:::

## Alignment {#align}

Provided by the `@mdit/plugin-align` plugin, allows controlling text alignment using containers.

>[!INFO]Note
>This plugin is based on [`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

:::: demo Example
::: left
Left-aligned content
:::

::: center
Centered content
:::

::: right
Right-aligned content
:::

::: justify
Justified content
:::
::::

## Spoilers {#spoiler}

*[Spoilers]: Also known as redacted blocks
Provided by the `@mdit/plugin-spoiler` plugin, used to create spoiler content.

::: demo Example
VuePress Theme Hope !!is very powerful!!.
:::

## Subscript & Superscript {#sub&sup}

Provided by the `@mdit/plugin-sub` and `@mdit/plugin-sup` plugins, used to create subscripts and superscripts.

::: demo Example
Subscript: H~2~O
Superscript: 19^th^
:::

## Ruby Annotations {#ruby}

Provided by the `@mdit/plugin-ruby` plugin, used to create ruby annotations.

::: demo Example
{China:zhōng|guó}
:::

## Demo {#demo}

Library support provided by the `@mdit/plugin-demo` plugin, with styling support from site maintainer [Coconut Milk](https://www.mcmod.cn/author/24749.html), used to create previewable Markdown examples.

:::: demo Example
::: demo Example
This is an example!
:::
::::

## Step Groups {#stepper}

Library support provided by the `@mdit/plugin-tab` plugin, with styling support from site maintainer [Coconut Milk](https://www.mcmod.cn/author/24749.html), used to create step-based tabs.

:::: demo Example
::: stepper
@tab Step 1
This is step 1
@tab Step 2
This is step 2
@tab Step 3
This is step 3
:::
::::

## Mark & Insert {#mark&ins}

Provided by the `@mdit/plugin-mark` and `@mdit/plugin-ins` plugins, used to mark and highlight content.

::: demo Example
VuePress Theme Hope ==is very powerful==.  
VuePress Theme Hope ++is very++ powerful.
:::

## Alert Boxes {#alert}

Provided by the `v-alert` plugin written by site maintainer [Coconut Milk](https://www.mcmod.cn/author/24749.html), used to create Vuetify-style alert containers.

>[!INFO]Note
>This plugin is based on [`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

::::: demo Example
::: v-success Success
This is success style
:::
::: v-info 
This is info style, default title
:::
::: v-warning Warning
This is warning style, supports other ++plugins++
:::
:::: v-error Error
This is error style
::: v-success Success
Can be nested
:::
::::
:::::

## Carousel Banners {#carousels}

Provided by the `carousels` plugin written by site maintainer [Coconut Milk](https://www.mcmod.cn/author/24749.html), used to create scrolling banners with custom content.

>[!INFO]Note
>This plugin is based on [`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

:::: demo Example
::: carousels#{"cycle": true, "interval": 2800, "undelimiters": true}
@tab
![1](https://docs.mihono.cn/mods/adventure/champions-unofficial/1.png)
@tab
![2](https://docs.mihono.cn/mods/adventure/champions-unofficial/2.png)
:::
::::

## Embedded External Links {#iframe}

:::: demo Example
:::iframes#{"src": "https://misode.github.io/"}
:::
::::

> Try embedding a codeblock?

### Configuration Syntax {#carousels-grammer}

The configuration options for the `carousels` container are provided by JSON following the container declaration, connected with `#`.

| Configuration Field | Purpose                                 | Type           | Default Value |
|--------------------|----------------------------------------|----------------|---------------|
| `src`              | URL of the webpage, required           | `string`       | `N/A`         |
| `height`           | Sets the element height                | `length value` | `140px`       |

## Cards {#card}

Provided by the `card` plugin written by site maintainer [Coconut Milk](https://www.mcmod.cn/author/24749.html), used to create cards with custom content.

>[!INFO]Note
>This plugin is based on [`@mdit/plugin-container`](https://mdit-plugins.github.io/zh/container.html)

::::: demo Example
:::text Title#Subtitle
This is text style
:::
:::flat Title Only
This is flat style
:::
:::elevated #Subtitle Only
This is elevated style
:::
:::tonal Title#Subtitle
This is tonal style
:::
:::outlined
This is outlined style, no title or subtitle
:::
::::plain Title#Subtitle
This is plain style  
Only double spaces or `\` can be used for line breaks
:::tonal Nested
Supports nesting, supports !!other plugins!!
:::
::::
:::::

## Justified Text

:::: demo Example
::: justify
This is a paragraph demonstrating justified alignment. Through CSS's text-align: justify property, you can achieve justified text alignment in VitePress.
:::
::::