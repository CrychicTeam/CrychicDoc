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

`LootJS` 提供了一系列的`KubeJS Events`来创建你的战利品修改或其他内容.

此事件为服务器端事件，可以通过`/reload`指令来重新加载

要了解有关 KubeJS 事件的更多信息，请查阅[KubeJS Events](https://kubejs.com/)

### 添加方块战利品列表修改

addBlockLootModifier(...blocks)

你可以在`addBlockLootModifier()`语句中填入方块的 ID 来指定进行对该方块的战利品列表修改

同时可以对指定的方块进行[Action](LootJs.md)以及[Conditions](Conditions.md)进行修改

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChance(0.3)
        .addLoot("minecraft:gunpowder");
});
```

### 添加实体战利品列表修改

addEntityLootModifier(...entities)

你可以在`addEntityLootModifier()`语句中填入实体的 ID 来指定进行对该实体的战利品列表修改

同时可以对指定的实体进行[Action](LootJs.md)以及[Conditions](Conditions.md)进行修改

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .addLoot("minecraft:gunpowder");
});
```

### 添加 LootTable 修改

addLootTableModifier(...values)

你可以在`addLootTableModifier()`语句中填入对应 LootTable 的 ID 或正则表达式进行对该 LootTable 的战利品修改

同时可以对指定的 LootTable 进行[Action](LootJs.md)以及[Conditions](Conditions.md)进行修改

下面的示例中添加了对 2 个 LootTable 的修改，一个是`minecraft:entities/creeper`的 LootTable，另一个是对正则表达式`/.*creeper.*/`的 LootTable

```js
LootJS.modifiers((event) => {
    event
        .addLootTableModifier("minecraft:entities/creeper")
        .randomChance(0.3)
        .addLoot("minecraft:gunpowder");

    event
        .addLootTableModifier(/.*creeper.*/)
        .randomChance(0.3)
        .addLoot("minecraft:gunpowder");
});
```

上述代码中两者都是对`minecraft:creeper`的 LootTable 进行修改

### 添加战利品类型修改

addLootTypeModifier(...types)

你可以在`addLootTypeModifier()`语句中填入战利品类型来指定进行对该战利品类型的战利品列表修改

同时可以对指定的战利品类型进行[Action](LootJs.md)以及[Conditions](Conditions.md)进行修改

对应的战利品类型有：

-   `LootType.ADVANCEMENT_ENTITY` (高级实体战利品)
-   `LootType.ADVANCEMENT_REWARD` (高级奖励战利品)
-   `LootType.BLOCK` (方块战利品)
-   `LootType.CHEST` (箱子战利品)
-   `LootType.ENTITY` (实体战利品)
-   `LootType.FISHING` (钓鱼战利品)
-   `LootType.GIFT` (礼物战利品，例如袭击胜利后村民给予的物品)
-   `LootType.PINGLIN_BARTER` (猪灵交易战利品)
-   `LootType.UNKNOWN` (未知战利品)

下面的示例代码中对`LootType.ENTITY`进行修改，为所有的`Entity`添加了一个 30%的概率来掉落一个`minecraft:gravel`战利品

```js
LootJS.modifiers((event) => {
    event
        .addLootTypeModifier(LootType.ENTITY)
        .randomChance(0.3)
        .addLoot("minecraft:gravel");
});
```

### 禁用部分战利品掉落

下面的示例代码中：

-   `disableWitherStarDrop()` 禁用下界之星掉落
-   `disableCreeperHeadDrop()` 禁用苦力怕掉落头颅
-   `disableSkeletonHeadDrop()` 禁用骷髅掉落头颅
-   `disableZombieHeadDrop()` 禁用僵尸掉落头颅

```js
LootJS.modifiers((event) => {
    event.disableWitherStarDrop();
    event.disableCreeperHeadDrop();
    event.disableSkeletonHeadDrop();
    event.disableZombieHeadDrop();
});
```

### 启用日志输出

enableLogging()

下面的示例代码中启用了日志输出，当战利品列表被添加时，会在控制台输出日志

具体日志可以查看`logs/kubejs/server.log`

```js
LootJS.modifiers((event) => {
    event.enableLogging();
});
```

### 返回其他模组战利品列表

getGlobalModifiers()

该方法为返回其他模组已经注册的全局战利品列表

注意该方法只适用于`Forge`

官方示例代码：

```js
LootJS.modifiers((event) => {
    const modifiers = event.getGlobalModifiers();
    modifiers.forEach((modifier) => {
        console.log(modifier);
    });
});
```

### 删除其他模组战利品

removeGlobalModifier(...values)

注意该方法只适用于`Forge`

官方示例代码：

```js
LootJS.modifiers((event) => {
    event.removeGlobalModifier("examplemod:example_loot_change"); // by location
    event.removeGlobalModifier("@examplemod"); // by mod id. Use `@` as prefix
});
```

### 禁用给定战利品列表

注意该方法只适用于`Forge`

可以直接指定战利品列表，也可以使用正则表达式来进行指定

官方示例代码：

```js
LootJS.modifiers((event) => {
    event.disableLootModification(/.*:blocks\/.*_leaves/);

    event.disableLootModification("minecraft:entities/bat");
});
```
