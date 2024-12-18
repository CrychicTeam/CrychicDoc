# 生物群系标签

## 概述

- 原版的生物群系标签应用例：has_closer_water_fog 当玩家在此标签中的生物群系中时，迷雾效果距离玩家更近。

- 原版生物群系标签列表：[minecraft-wiki/标签#生物群系](https://zh.minecraft.wiki/w/%E6%A0%87%E7%AD%BE#%E7%94%9F%E7%89%A9%E7%BE%A4%E7%B3%BB)

## 添加生物群系标签

- 事件：ServerEvents.tags('minecraft:block', event=>{});

- 语句：event.add(标签, 生物群系);

::: code-group

```js
ServerEvents.tags('minecraft:biome', event => {
    // 标签id，生物群系id
    event.add('kubejs:my_biome_tags', 'minecraft:plains');
})
```

```js [KubeJS]
ServerEvents.tags('minecraft:biome', event => {
    /**
     * 要加标签的生物群系id数组
     * @type {Special.Biome[]}
     */
    const tagBiomes = [
        'minecraft:plains'
    ]
    for (const biome of tagBiomes) {
        event.add('kubejs:my_biome_tags', biome);
    }
})
```

:::

## 删除生物群系标签

- 事件：ServerEvents.tags('minecraft:biome', event=>{});

- 语句：event.remove(标签, 生物群系);

::: code-group

```js [KubeJS]
ServerEvents.tags('minecraft:biome', event => {
    // 标签id，生物群系id
    event.remove('kubejs:my_biome_tags', 'minecraft:plains');
})
```

```js
// 使用for循环对付更多的生物群系id
ServerEvents.tags('minecraft:biome', event => {
    /**
     * 要加标签的生物群系id数组
     * @type {Special.Biome[]}
     */
    const tagBiomes = [

    ]
    for (const biome of tagBiomes) {
        event.add('kubejs:my_biome_tags', biome);
    }
})
```

:::
