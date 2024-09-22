---
authors: ['Wudji']
---

# 11.1 JEI信息修改

***

添加子类型：

```
onEvent('jei.subtypes', event => {
  event.useNBT('example:item')
  event.userNBTKey('example:item', 'type')
})
```

隐藏物品/流体

```
onEvent('jei.hide.items', event => {
  event.hide('example:ingredient')
})

onEvent('jei.hide.fluids', event => {
  event.hide('example:fluid')
})
```

添加物品/流体

```
onEvent('jei.add.items', event => {
  event.add(Item.of('example:item', {test: 123}))
})

onEvent('jei.add.fluids', event => {
  event.add('example:fluid')
})
```

为物品添加信息

```
onEvent('jei.information', event => {
  event.add('example:ingredient', ['第一行介绍', '第二行介绍'])
})
```
