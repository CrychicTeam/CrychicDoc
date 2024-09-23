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

*1*.`addLoot(...items)`

将一个或多个添加`items`到当前战利品池中

以沙砾展开:
- `addBlockLootModifier`语句对沙砾进行LootTable修改
- `addLoot`对沙砾的LootTable进行添加

对于具体的LootType，请使用`ProbeJs`来进行补全提示

```js
LootJS.modifiers((event) => {
    // 修改方块
    event
        .addBlockLootModifier("minecraft:gravel")
        .addLoot("minecraft:flint")
    // 修改实体
    event
        .addEntityLootModifier("minecraft:pig")
        .addLoot("minecraft:diamond")
    // 修改原版战利品类型
    event
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

若要移除之前的掉落则加上↓这段代码在addLoot的上面
```js
.removeLoot("minecraft:flint")

```



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

- `addBlockLootModifier`语句进行对"minecraft:coal_ore"修改
- `removeLoot`语句内使用`Ingrediten.all`删除"minecraft:coal_ore"的所有Loot
- `addAlternativesLoot`进行LootTable列表修改

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:coal_ore")
        .removeLoot(Ingredient.all)
        .addAlternativesLoot(
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


*3*.`addSequenceLoot(...items)`

相较于`addAlternativesLoot`,`addSequenceLoot`的区别是
- 添加多个战利品列表，所有战利品列表都会生效
- 若一个Loot不触发，则停止该Loot往下进行的修改

这里的所有列表都会生效的意思是，当一个条目的战利品列表停止触发后，接下来往下对LootTable的修改将不会再进行
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

例如上述例子所示,若`minecraft:apple`触发并掉落,则接着触发`minecraft:stick`的Loot.

若`minecraft:stick`的Loot未触发,则该LootTable就不会进行下去，终止在掉落一个苹果，以此类推

若`minecraft:stick`触发，`minecraft:diamond`触发，`minecraft:coal`不触发则会掉落苹果，木棍，以及钻石(煤炭和火把则不会掉落)

以此类推
