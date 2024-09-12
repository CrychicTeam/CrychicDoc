# 物品标签

## 前言

- 原版的物品标签应用例：minecraft:fox_food 标签下的物品作为狐狸的食物。

- 原版物品标签列表：[minecraft-wiki/标签#物品](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E7%89%A9%E5%93%81)

## 添加物品标签

- 事件：ServerEvents.tags('minecraft:item', event=>{});

- 语句：event.add(标签, 物品);

```js
ServerEvents.tags('minecraft:item', event => {
    /**
     * 要加标签的物品
     * @type {Special.Item[]}
     */
    const tagItems = [
        
    ]
    for (const item of tagItems) {
        event.add('kubejs:my_item_tags', item);
    }
})
```

## 删除物品标签

- 从标签中删除物品。

- 事件：ServerEvents.tags('minecraft:item', event=>{});

- 语句：event.remove(标签, 物品);

```js
ServerEvents.tags('minecraft:item', event => {
    /**
     * 要删除标签的物品
     * @type {Special.Item[]}
     */
    const tagItems = [
        
    ]
    for (const item of tagItems) {
        event.add('kubejs:my_item_tags', item);
    }
})
```
