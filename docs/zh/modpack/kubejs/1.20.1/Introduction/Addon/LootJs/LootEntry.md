# LootJs

LootJs 是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作
`KubeJS`本身自带的修改 Loot 的方法过于繁琐，若要修改关于:

-   方块
-   实体
-   战利品列表

内的 LootTable

推荐使用`LootJs`来实现

:::v-info
该篇教程源自于 Github 的官方 Wiki

适用于 Minecraft 1.19.2/1.20.1
:::

## 简介

`LootEntry` 类用于创建战利品条目.例如`addLoot`，`replaceLoot`，`modifyLoot` 等方法将 `LootEntry` 作为参数，但仍然可以使用物品 ID 或其他 `KubeJS` 物品符号.

`LootEntry` 会让你对战利品有更多控制以及修改，你可以直接将函数链入` LootEntry` 或通过 `.when` 方法来添加条件。

具体关于`LootEntry`，可以跳转到[LootEntry](../../LootTable/BasicKnowledge/LootEntry.md)进行查看

### 创建一个`LootEntry`

官方示例代码：

```js
LootEntry.of("minecraft:apple")
LootEntry.of("minecraft:apple", 1)
LootEntry.of("minecraft:apply", { ... })
LootEntry.of("minecraft:apple", 1, { ... })
LootEntry.withChance(item, chance)
```

上述的示例代码中：

-   第一句创建了一个`LootEntry`条目存储了一个`minecraft:apple`
-   第二句创建了一个`LootEntry`条目存储了一个`minecraft:apple`，并且设置其数量为 1
-   第三句创建了一个`LootEntry`条目存储了一个`minecraft:apple`，并且设置其 NBT 为`{ ... }`
-   第四句创建了一个`LootEntry`条目存储了一个`minecraft:apple`，并且设置其数量为 1，并且设置其 NBT 为`{ ... }`
-   第五句创建了一个`LootEntry`条目，并且设置其概率为`chance`，并且设置其物品为`item`，item 可以是任何`KubeJS`物品符号

### 修改 LootEntry

官方示例代码：

```js
LootEntry.of("minecraft:diamond_sword").enchantRandomly().damage(0.3);
```

上述的示例代码中创建了一个`LootEntry`条目，并且设置其物品为`minecraft:diamond_sword`，并且设置其随机附魔，并将其耐久设置为改物品耐久的 30%

更多方法请使用`ProbeJs`模组进行补全查看

### 条件检测

你可以地向`LootEntry`添加条件用以过滤，当条件满足时战利品才会进行掉落，适用于多个 Loot

官方示例代码：

```js
LootEntry.of("minecraft:apple").when((c) => c.randomChance(0.3));
```

上述的示例代码中创建了一个`LootEntry`条目，并且设置其物品为`minecraft:apple`，并设置其掉落概率为 30%

更多方法请使用`ProbeJs`模组进行补全查看

### 官方代码示例

::: code-group

```js [使用addLoot进行操作]
LootJS.modifiers((event) => {
    event.addBlockLootModifier("minecraft:oak_log").addLoot(
        /**
         * Creates a LootEntry with 50% chance of dropping a stick.
         */
        LootEntry.of("minecraft:stick").when((c) => c.randomChance(0.5)),
        /**
         * Creates a LootEntry with 50% chance of dropping a stick.
         */
        LootEntry.of("minecraft:apple").when((c) => c.randomChance(0.5))
    );
});
```

```js [使用addAlternativesLoot进行操作]
LootJS.modifiers((event) => {
    /**
     * First loot entry with a condition. Will drop if the player has fortune.
     */
    const stickWhenFortune = LootEntry.of("minecraft:stick")
        .applyOreBonus("minecraft:fortune")
        .when((c) =>
            c.matchMainHand(ItemFilter.hasEnchantment("minecraft:fortune"))
        );

    /**
     * Second loot entry with a condition. Will drop if the player has silk touch and the first entry doesn't match.
     */
    const appleWhenSilkTouch = LootEntry.of("minecraft:apple").when((c) =>
        c.matchMainHand(ItemFilter.hasEnchantment("minecraft:silk_touch"))
    );

    /**
     * No conditions just an item, so this will always drop if the other two don't.
     */
    const ironIngot = "minecraft:iron_ingot";

    event
        .addBlockLootModifier("minecraft:iron_ore")
        .removeLoot(Ingredient.all)
        .addAlternativesLoot(stickWhenFortune, appleWhenSilkTouch, ironIngot);
});
```

```js [使用addSequenceLoot进行操作]
LootJS.modifiers((event) => {
    /**
     * First loot entry with a condition. Will drop if the player has fortune.
     */
    const stickWhenFortune = LootEntry.of("minecraft:stick").when((c) =>
        c.matchMainHand(ItemFilter.hasEnchantment("minecraft:fortune"))
    );

    /**
     * Second loot entry with a condition. Will drop if the player has silk touch.
     */
    const appleWhenEfficiency = LootEntry.of("minecraft:apple").when((c) =>
        c.matchMainHand(ItemFilter.hasEnchantment("minecraft:efficiency"))
    );

    /**
     * Simple item without conditions or anything else, will drop
     */
    const flint = "minecraft:flint";

    /**
     * Random chance is 0 so no diamond will ever drop. Just to show, that it will skip all other entries.
     */
    const diamondNoDrop = LootEntry.of("minecraft:diamond").when((c) =>
        c.randomChance(0.0)
    );

    /**
     * No conditions just an item, but this will not drop, because the previous entry failed.
     */
    const ironIngot = "minecraft:iron_ingot";

    event
        .addBlockLootModifier("minecraft:coal_ore")
        .removeLoot(Ingredient.all)
        .addSequenceLoot(
            stickWhenFortune,
            appleWhenEfficiency,
            flint,
            diamondNoDrop,
            ironIngot
        );
});
```
