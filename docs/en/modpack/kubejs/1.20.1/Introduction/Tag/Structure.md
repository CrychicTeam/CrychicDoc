# 结构标签

## 概述

- 原版的结构标签应用例：eye_of_ender_located 末影之眼指向最近的拥有此标签的结构。

- 原版结构标签列表：[minecraft-wiki/标签#结构](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E7%BB%93%E6%9E%84)

## 添加结构标签

- 事件：ServerEvents.tags('minecraft:structure', event=>{});

- 语句：event.add(标签, 结构);

::: code-group

```js
ServerEvents.tags('minecraft:structure', event => {
    // 标签id，结构id
    event.add('kubejs:my_structure_tags', 'minecraft:village_plains');
})
```

```js
// 使用for循环对付更多的结构id
ServerEvents.tags('minecraft:structure', event => {
    /**
     * 要删除标签的结构id数组
     * @type {Special.Structure[]}
     */
    const tagStructures = [
        'minecraft:village_plains'
    ]
    for (const structure of tagStructures) {
        event.add('kubejs:my_structure_tags', structure);
    }
})
```

:::

## 删除结构标签

- 事件：ServerEvents.tags('minecraft:structure', event=>{});

- 语句：event.remove(标签, 结构);

::: code-group

```js
ServerEvents.tags('minecraft:structure', event => {
    // 标签id，结构id
    event.remove('kubejs:my_structure_tags', 'minecraft:village_plains');
})
```

```js
// 使用for循环对付更多的结构id
ServerEvents.tags('minecraft:structure', event => {
    /**
     * 要删除标签的结构id数组
     * @type {Special.Structure[]}
     */
    const tagStructures = [
        'minecraft:village_plains'
    ]
    for (const structure of tagStructures) {
        event.remove('kubejs:my_structure_tags', structure);
    }
})
```

:::
