# 方块战利品表

## 前言

- 事件：LootJS.modifiers( (event) =>{ event.addBlockLootModifier(方块id) } );

## 简单条件的战利品

- 语句：

- 语句：addLoot("minecraft:gunpowder")

- 示例：挖砂砾额外获得火药。

::: code-group

```js [简单条件]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:gravel") // 战利品表：砂砾方块
        .randomChance(0.5) // 条件：0.5的概率
        .addLoot("minecraft:gunpowder") // 操作：添加战利品火药
})
```

```js [无条件]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:gravel") // 战利品表：砂砾方块
        .addLoot("minecraft:gunpowder") // 操作：添加战利品火药
})
```

:::

## 添加条件的战利品列表

- 语句：.addAlternativesLoot(一个或多个有条件的战利品);

- 例：砂砾会从苹果，木棍，钻石，煤炭，火把中依次尝试掉落，掉落成功则终止尝试。

```js
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:gravel") // 战利品表：砂砾方块
        .addAlternativesLoot( // 会掉落第一个符合条件的战利品
            LootEntry.of("minecraft:apple").when((c) => c.randomChance(0.8)),
            LootEntry.of("minecraft:stick").when((c) => c.randomChance(0.3)),
            LootEntry.of("minecraft:diamond").when((c) => c.randomChance(0.7)),
            LootEntry.of("minecraft:coal").when((c) => c.randomChance(0.99)),
            LootEntry.of("minecraft:torch").when((c) => c.randomChance(0.2))
        );
})
```

## 添加权重战利品列表

- 需要设置投掷次数与战利品权重，投掷次数可以指定区间，单个数字，或者不写以使用默认值（为1）。

- 例：砂砾方块按权重在每次投掷时掉落战利品，火药或下界之星。

::: code-group

```js [投掷次数区间]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:gravel") // 战利品表：砂砾方块
        .addWeightedLoot( 
            [3, 10], // 会投掷3到10次，每次按权重在下方列表中选择战利品。
            [
            Item.of("minecraft:gunpowder").withChance(50), 
            Item.of("minecraft:nether_star").withChance(5)
            ]
        );
})
```

```js [指定投掷次数]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:gravel") // 战利品表：砂砾方块
        .addWeightedLoot( 
            5, // 会投掷5次，每次按权重在下方列表中选择战利品。
            [
            Item.of("minecraft:gunpowder").withChance(50), 
            Item.of("minecraft:nether_star").withChance(5)
            ]
        );
})
```

```js [默认投掷次数]
LootJS.modifiers((event) => {
    // 添加方块战利品表修饰
    event
        .addBlockLootModifier("minecraft:gravel") // 战利品表：砂砾方块
        .addWeightedLoot(  // 会投掷1次，每次按权重在下方列表中选择战利品。
            [
            Item.of("minecraft:gunpowder").withChance(50), 
            Item.of("minecraft:nether_star").withChance(5)
            ]
        );
})
```

:::

## 添加序列战利品列表

- 被添加在序列战利品列表中的战利品会在触发战利品掉落时挨个尝试掉落，直到有一个不能掉落为止。

- 例：煤矿石将依次尝试掉落苹果、木棍、钻石、煤炭、火把，掉落成功会进行下一个战利品的掉落尝试，掉落失败则终止尝试。

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:coal_ore")
        .removeLoot(Ingredient.all)
        .addSequenceLoot(
            LootEntry.of("minecraft:apple").when((c) => c.randomChance(0.8)),
            LootEntry.of("minecraft:stick").when((c) => c.randomChance(0.3)),
            LootEntry.of("minecraft:diamond").when((c) => c.randomChance(0.7)),
            LootEntry.of("minecraft:coal").when((c) => c.randomChance(0.99)),
            LootEntry.of("minecraft:torch").when((c) => c.randomChance(0.2))
        );
});
```
