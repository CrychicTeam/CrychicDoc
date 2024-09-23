# LootJs
LootJs是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作
`KubeJS`本身自带的修改Loot的方法过于繁琐，若要修改关于:
- 方块
- 实体
- 战利品列表

内的LootTable

推荐使用`LootJs`来实现

:::v-info
该篇教程源自于Github的官方Wiki

适用于Minecraft 1.19.2/1.20.1
:::

## 简介
Action用于更改当前战利品池结果或触发效果。您可以简单地将多个Action串联在一起。对于每个Loot Modification，您至少需要一个Action。

## 操作(基础)

*1*. `addLoot(...items)`

将一个或多个添加`items`到当前战利品池中
```js
LootJS.modifiers((event) => {
    // 修改方块的战利品列表
    // 下列例子以沙砾为展开
    event
        // 使用修改器指定要修改的方块
        .addBlockLootModifier("minecraft:gravel")
        // 添加一个新的Loot(燧石)给沙砾
        .addLoot("minecraft:flint")
        // 进游戏后输入/reload
        // 尝试破坏一个沙砾，你将会看到掉落一个沙砾和一个燧石，或者掉落两个燧石
        // 这里的Loot没有指定概率，所以是100%掉落一个燧石('minecraft:flint')

    // 同样的道理，我们也可以修改这样来进行对其他战利品的修改
    // 修改生物战利品列表
    event
        .addEntityLootModifier("minecraft:pig")
        .addLoot("minecraft:diamond")
    // 修改战利品类型
    event
        // 具体的战利品类型，玩家可以使用ProbeJs模组来进行补全
        .addLootTypeModifier('chest')
        .addLoot("minecraft:bedrock")
    
})
```

官方示例
```js
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
    )
})
```

::: v-warning WARN
若要移除之前的掉落则加上↓这段代码在addLoot的上面
```js
.removeLoot("minecraft:flint")

```

:::


根据Js的代码执行顺序，若将这串代码放置在addLoot底下则不会生效

必须放在addLoot的上面

若一个方块/实体/战利品列表里面有多个Loot
则可以使用
```js
.removeloot(Ingredient.all)
```

来将该方块/实体/战利品列表的所有Loot全部移除


*2*. `addAlternativesLoot(...items)`

若要对一个方块添加多种Loot,且有概率随机,这时候就要用到`addAlternativesLoot`

根据 Minecraft Wiki：只会将第一个成功（满足条件）的物品添加到战利品池中。如果没有物品成功，则不会添加任何物品

```js
LootJS.modifiers((event) => {
    event
        // 同样的道理对煤炭矿石进行修改modify
        .addBlockLootModifier("minecraft:coal_ore")
        // 删除煤炭矿石的所有Loot
        .removeLoot(Ingredient.all)
        // 对于煤炭矿石添加一个替代的LootTable
        .addAlternativesLoot(
            // 下列列举了五个战利品条目来进行修改
            LootEntry.of("minecraft:apple").when((c) => c.randomChance(0.8)),
            LootEntry.of("minecraft:stick").when((c) => c.randomChance(0.3)),
            LootEntry.of("minecraft:diamond").when((c) => c.randomChance(0.7)),
            LootEntry.of("minecraft:coal").when((c) => c.randomChance(0.99)),
            LootEntry.of("minecraft:torch").when((c) => c.randomChance(0.2))
        )
})
```

相较于上方示例,直接在`addLoot`里面添加多种Loot有所区别

在`addLoot`里面添加多种Loot以及概率的时候是所有Loot都会根据你所设置的概率来进行掉落

而`addAlternativesLoot`是根据你设置的战利品条目，若一个条目的Loot触发后，则其他条目的Loot不会触发

相当于`addLoot`是所有Loot都会触发，`addAlternativesLoot`只会触发一个.

官方的复杂示例，值得去研究一下
```js
LootJS.modifiers((event) => {
    /**
     * First loot entry with a condition. Will drop if the player has fortune.
     */
    const stickWhenFortune = LootEntry.of("minecraft:stick")
        .applyOreBonus("minecraft:fortune")
        .when((c) => c.matchMainHand(ItemFilter.hasEnchantment("minecraft:fortune")))

    /**
     * Second loot entry with a condition. Will drop if the player has silk touch and the first entry doesn't match.
     */
    const appleWhenSilkTouch = LootEntry.of("minecraft:apple").when((c) =>
        c.matchMainHand(ItemFilter.hasEnchantment("minecraft:silk_touch"))
    )

    /**
     * No conditions just an item, so this will always drop if the other two don't.
     */
    const ironIngot = "minecraft:iron_ingot"

    event
        .addBlockLootModifier("minecraft:iron_ore")
        .removeLoot(Ingredient.all)
        .addAlternativesLoot(stickWhenFortune, appleWhenSilkTouch, ironIngot)
})
```
