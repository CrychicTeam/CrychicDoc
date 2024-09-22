# 方块标签

## 概述

- 原版的方块标签应用例：minecraft:air 标签用于冰霜行者魔咒检测水上方的空气。

- 原版方块标签应用列表：[minecraft-wiki/标签#方块](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E6%96%B9%E5%9D%97)

## 添加方块标签

- 事件：ServerEvents.tags('minecraft:block', event=>{});

- 语句：event.add(标签, 方块);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:block', event => {
     // 标签id，方块id
    event.add('kubejs:my_block_tags', 'minecraft:grass_block');
})
```

```js [KubeJS]
// 你可以使用for循环对付更多的方块id
ServerEvents.tags('minecraft:block', event => {
    /**
     * 要加标签的方块id数组
     * @type {Special.Block[]}
     */
    const tagBlocks = [
        'minecraft:grass_block'
    ]
    for (const block of tagBlocks) {
        event.add('kubejs:my_block_tags', block);
    }
})
```

:::

## 删除物品标签

- 事件：ServerEvents.tags('minecraft:block', event=>{});

- 语句：event.remove(标签, 方块);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:block', event => {
    // 标签id，方块id
    event.remove('kubejs:my_block_tags', 'minecraft:grass_block');
})
```

```js [KubeJS]
// 使用for循环对付更多的方块id
ServerEvents.tags('minecraft:block', event => {
    /**
     * 从标签删除的方块id数组
     * @type {Special.Block[]}
     */
    const tagBlocks = [
        'minecraft:grass_block'
    ]
    for (const block of tagBlocks) {
        event.remove('kubejs:my_block_tags', block);
    }
})
```

:::
