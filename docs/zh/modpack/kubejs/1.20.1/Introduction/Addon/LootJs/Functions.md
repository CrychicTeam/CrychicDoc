# LootJs

LootJs 是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作
`KubeJS`本身自带的修改 Loot 的方法过于繁琐，若要修改关于:

- 方块
- 实体
- 战利品列表

内的 LootTable

推荐使用`LootJs`来实现

:::v-info
该篇教程源自于 Github 的官方 Wiki

适用于 Minecraft 1.19.2/1.20.1
:::

## 简介

战利品 Functions 可以帮助你在战利品掉落之前对其进行修改

如果你曾经修改过原版的战利品表，那么你大概会熟悉到这个概念

### 随机附魔

enchantRandomly()

`enchantRandomly()`会随机给战利品掉落物附上随机的附魔

下面的示例代码中会给掉落的战利品添加一个随机附魔

注意：掉落物必须是装备或者工具，不然无法进行附魔

```js
LootJS.modifiers((event) => {
  event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
    p.addLoot("minecraft:diamond_axe");
    p.enchantRandomly();
  });
});
```

### 随机附魔特定等级

enchantWithLevels(NumberProvider)

大致相当于使用附魔台进行附魔，只是指定了随机的等级

例如下列的`enchantWithLevels([40,50])`中填入了一个数组，那么战利品掉落出来的物品随机附魔将会是对应该物品附魔的最高级

超过 40 的都是对应的最高等级的附魔

```js
LootJS.modifiers((event) => {
  event.addBlockLootModifier("minecraft:coal_block").pool((p) => {
    p.addLoot("minecraft:diamond_sword");
    p.enchantWithLevels([40, 50]);
  });
});
```

### 抢夺附魔战利品数量

applyLootingBonus(NumberProvider)

下面的示例代码中，根据抢夺附魔随机给战利品增加数量

`NumberProvider`可以指定一个范围，例如`[1,10]`，那么掉落的战利品将会有 1 到 10 个

```js
LootJS.modifiers((event) => {
  event.addEntityLootModifier("minecraft:creeper").pool((p) => {
    p.addLoot("minecraft:emerald");
    p.applyLootingBonus([1, 10]);
  });
});
```

### 应用二项分布奖励

applyLootingBonus(NumberProvider)

根据击杀者手持物品的掠夺附魔等级来设置战利品掉落物堆叠大小

```js
LootJS.modifiers((event) => {
  event.addBlockLootModifier("minecraft:emerald_block").pool((p) => {
    p.addLoot("minecraft:emerald");
    p.applyBinomialDistributionBonus("minecraft:fortune", 0.2, 3);
  });
});
```
