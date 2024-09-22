---
authors: ['Wudji']
---

# 1.3 脚本基础和格式变化

***

在KubeJS中，你需要使用[JavaScript语言](https://www.w3school.com.cn/js/js\_intro.asp)来编写脚本，JavaScript的教程可以在网络上找到\~\~（肯定比我讲得好）\~\~。

## 一、监听事件

绝大多数KubeJS的脚本都需要以监听事件为基础。

相比于1.16.5中，KubeJS 6中`事件组.事件名称(event => {})`替代了先前的`onEvent('字符串.事件.id', event => {})`格式

以监听方块右键事件为例：

```js
// 1.19（KubeJS 6）

BlockEvents.rightClicked(event => {
  if(event.block.id === 'minecraft:dirt') {
    console.info('Hi!')
  }
})

// 1.16.5 - 1.18.2

onEvent('block.right_click', event => {
  if(event.block.id === 'minecraft:dirt') {
    console.info('Hi!')
  }
})
```

此外，新的事件监听格式还支持通过传入参数设置事件ID，如`事件组.事件名称(para, event => {})`

```js
// 1.19（KubeJS 6）

BlockEvents.rightClicked('minecraft:dirt', event => {// 'minecraft:dirt'为参数，该事件在右键点击泥土方块时触发
  console.info('Hi!')
})

BlockEvents.rightClicked('minecraft:stone', event => {// 'minecraft:stone'为参数，该事件在右键点击石头方块时触发
  console.info('Bye!')
})

// 1.16.5 - 1.18.2

onEvent('block.right_click', event => {
  if(event.block.id === 'minecraft:dirt') {// 手动判断方块ID
    console.info('Hi!')
  } else if(event.block.id === 'minecraft:stone') {
    console.info('Bye!')
  }
})
```

一些事件_必须_填写ID：

```js
StartupEvents.register('item', event => {
  // 注册物品代码
})

ServerEvents.tags('item', event => {
  // 物品tag修改代码
})
```

**有关事件详细教程，请见后续章节**

## 二、Wrapper类的移除

在1.19+中，EntityJS, LevelJS, ItemStackJS, IngredientJS等Wrapper类被移除，带来了显著的性能和兼容性提升。旧版本中绝大多数的方法依旧被新版本支持。详细教程详见后文。

\*\*在ProbeJS的帮助下，你可以快速编写代码而不必依赖于文档。\*\*若仍有需要，可参考[KubeJS源代码仓库](https://github.com/KubeJS-Mods/KubeJS/tree/1.19/main)、[官方列表（旧） ](https://kubejs.com/wiki/kubejs/)、 [1.16-1.18版本教程](https://www.mcbbs.net/thread-1207772-1-1.html) 、 [Fabric JavaDoc](https://fabricmc.net/develop/)或[Forge JavaDoc](https://nekoyue.github.io/ForgeJavaDocs-NG/)。