# 物品标签

## 前言

## 向标签添加物品

- 向标签中添加物品，如标签不存在会自动创建。

- 事件：ServerEvents.tags('item', event=>{});

- 语句：event.add(标签, 物品);

```js
ServerEvents.tags('item', event => {
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

## 从标签删除物品

- 从标签中删除物品。

- 事件：ServerEvents.tags('minecraft:item', event=>{});

- 语句：event.remove(标签, 物品);

```js
ServerEvents.tags('item', event => {
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
