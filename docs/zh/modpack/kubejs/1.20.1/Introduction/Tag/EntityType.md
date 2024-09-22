# 实体类型标签

## 概述

- 原版的流体标签应用例：minecraft:axolotl_always_hostiles 美西螈总是与这些生物保持敌对。

- 原版实体类型标签应用列表：[minecraft-wiki/标签#实体类型](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E5%AE%9E%E4%BD%93%E7%B1%BB%E5%9E%8B%E6%A0%87%E7%AD%BE)

## 添加实体类型标签

- 事件：ServerEvents.tags('minecraft:entity_type', event => {});

- 语句：event.add(标签id, 实体类型id);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:entity_type', event => {
    // 标签id，实体类型id
    event.add('kubejs:my_entity_type', 'minecraft:zombie');
})
```

```js [KubeJS]
// 使用for循环对付更多的实体类型id
ServerEvents.tags('minecraft:entity_type', event => {
    /**
     * 要加进标签的实体id数组
     * @type {Special.EntityType[]}
     */
    const tagEntityTypes = [
        'minecraft:zombie'
    ]
    for (const entityType of tagEntityTypes) {
        event.add('kubejs:my_entity_type', entityType);
    }
})
```

:::

## 删除实体类型标签

- 事件：ServerEvents.tags('minecraft:entity_type', event => {});

- 语句：event.remove(标签id, 实体类型id);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:entity_type', event => {
    // 标签id，实体类型id
    event.remove('kubejs:my_entity_type', 'minecraft:zombie');
})
```

```js [KubeJS]
// 使用for循环对付更多的实体类型id
ServerEvents.tags('minecraft:entity_type', event => {
    /**
     * 要从标签中删除的实体类型id数组
     * @type {Special.EntityType[]}
     */
    const tagEntityTypes = [
        'minecraft:zombie'
    ]
    for (const entityType of tagEntityTypes) {
        event.remove('kubejs:my_entity_type', entityType);
    }
})
```

:::
