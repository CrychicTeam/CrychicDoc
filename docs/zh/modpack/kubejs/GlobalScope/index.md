---
layout: doc
title: 前言
prev:
  text: 全局类
  link: /zh/modpack/kubejs/GlobalScope/index
next:
  text: Item类
  link: /zh/modpack/kubejs/GlobalScope/item
  authors:
  - Eikidona
---

# 全局类

全局类是在任何作用域都可使用的类，是开发中实用的工具。

## 约定

为了缩短篇幅，便于书写与理解，文中使用了一些符号，单词等代表某些含义。\
**`args`** 有参数的函数——假设现有函数demo(args); 这里的“args”既不代表参数名，也不代表参数类型，参数个数等，仅代表这是一个有参数的函数。

## 常见的全局类

例：当获取一个随机数时。

```js
// 它会返回一个[0, 1)区间的数
let random = Math.random();
```

事实上就在调用Math这个全局类中的random函数。

对于Kubejs，它提供了一些全局类帮助我们表示Minecraft中的一些对象，例如物品类（Item）

```js
// 这是一个草方块物品
let grass_block = Item.of('minecraft:grass_block');
```

因此，了解Kubejs提供的全局类，对于使用kubejs大有裨益。

::: warning 注意
本文涉及类型信息均依据Probejs-6.0.1提供的自动补全。推荐安装Probejs以获取代码补全与类型提示功能。

- [Probejs](https://www.mcmod.cn/class/6486.html)
:::
