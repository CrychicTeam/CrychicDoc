# 物品标签

## 概述

- 原版的物品标签应用例：minecraft:fox_food 标签下的物品作为狐狸的食物。

- 原版物品标签列表：[minecraft-wiki/标签#物品](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E7%89%A9%E5%93%81)

## 添加物品标签

- 事件：ServerEvents.tags('minecraft:item', event=>{});

- 语句：event.add(标签, 物品);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:item', event => {
    // 标签id，物品id
    event.add('kubejs:my_item_tags', 'minecraft:stick');
})
```

```js [KubeJS]
// 使用for循环对付更多的物品id
ServerEvents.tags('minecraft:item', event => {
    /**
     * 要加标签的物品id数组
     * @type {Special.Item[]}
     */
    const tagItems = [
        'minecraft:stick'
    ]
    for (const item of tagItems) {
        event.add('kubejs:my_item_tags', item);
    }
})
```

:::

## 删除物品标签

- 从标签中删除物品。

- 事件：ServerEvents.tags('minecraft:item', event=>{});

- 语句：event.remove(标签, 物品);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:item', event => {
    // 标签id，物品id
    event.remove('kubejs:my_item_tags', 'minecraft:stick');
})
```

```js [KubeJS]
// 使用for循环对付更多的物品id
ServerEvents.tags('minecraft:item', event => {
    /**
     * 从标签删除的物品id数组
     * @type {Special.Item[]}
     */
    const tagItems = [
        'minecraft:stick'
    ]
    for (const item of tagItems) {
        event.remove('kubejs:my_item_tags', item);
    }
})
```

:::
