---
authors: ['Wudji']
---

# 11.2 REI信息修改

***

隐藏物品

```
onEvent('rei.hide.items', event => {
  event.hide('example:ingredient')
})
```

添加物品/流体

```
onEvent('rei.add.items', event => {
  event.add(item.of('example:item').nbt({test: 123}))
})
```

为物品添加信息

```
onEvent('rei.information', event => {
  event.add('example:ingredient', '标题', ['第一行', '第二行'])
})
```
