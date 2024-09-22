---
authors: ['Gu-meng']
---
# 添加和删除tag

[关于tag介绍](../Digression/Tag.md)

在游戏内你将**橡木原木**拿在手中输入指令`/kjs hand`的时候会发现，除了物品id外还有其他的前面带 `#` 的id，这些就是tagId，我们可以将tag浅理解为“组”，比如**橡木原木**下面有一个tagid为`#minecraft:logs`，这个就是所有原木的“组”

常用的tag类型有：方块tag(block tag)，流体tag(fluid tag)和物品tag(item tag)

在游戏里我们可以这样修改已有物品的tag
```js
ServerEvents.tags("block",event=>{})

ServerEvents.tags("fluid",event=>{})

ServerEvents.tags("item",event=>{})
```
上面是常用的tag类型，在游戏里根据情况选择

## 示例
```js
ServerEvents.tags("item",event=>{
    // 将橡木原木从原木的tag中去除
    event.remove("minecraft:logs",['minecraft:oak_log']);
    // 删除所有木板的tag
    event.removeAll('minecraft:planks');
    // 将基岩加入到原木的tag中
    event.add("minecraft:logs",['minecraft:bedrock']);
})
```
上方只演示了物品的tag的添加和删除

在使用其他tag类型也是一样的，都是使用`remove` `removeAll` `add`这三个方法