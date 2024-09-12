# 方块标签

## 前言

## 向标签中添加方块

- 事件：ServerEvents.tags('minecraft:block', event=>{});

- 语句：event.add(标签, 方块);

```js
ServerEvents.tags('minecraft:block', event => {
    /**
     * 要加标签的方块
     * @type {Special.Block[]}
     */
    const tagBlocks = [

    ]
    for (const block of tagBlocks) {
        event.add('kubejs:my_block_tags', block);
    }
})
```

## 从标签中删除方块
