# 方块战利品表

## 前言

## 简单条件的战利品

- 示例：挖砂砾额外获得火药。

::: code-group

```js [简单条件]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:creeper") // 战利品表：砂砾方块
        .randomChance(0.5) // 条件：0.5的概率
        .addLoot("minecraft:gunpowder") // 操作：添加战利品火药
})
```

```js [无条件]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:creeper") // 战利品表：砂砾方块
        .addLoot("minecraft:gunpowder") // 操作：添加战利品火药
})
```

:::
