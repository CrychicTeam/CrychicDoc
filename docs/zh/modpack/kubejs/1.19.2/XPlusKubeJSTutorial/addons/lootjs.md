# 11.2 LootJS —— 便捷战利品表修改

{% hint style="info" %}
This page is currently under construction and does not yet cover all aspects or information. We are working on it. Thank you for your understanding.
{% endhint %}

KubeJS 自带的`LootBuilder`并不便于简单的战利品表修改，如添加额外掉落物。其修改掉落条件和函数也较为繁琐，而LootJS便解决了这个问题，它能够让你更加便捷地修改战利品表，掉落战利品时执行事件等。

## 一、附属信息

(本节部分内容参考自 [LootJS Wiki](https://github.com/AlmostReliable/lootjs-forge/wiki))

mod链接：[Github](https://github.com/AlmostReliable/lootjs-forge) [Curseforge](https://www.curseforge.com/minecraft/mc-mods/lootjs-forge)，许可：LGPL-3.0。1.19.2+版本支持 Forge 和 Fabric 模组加载器。

你可以通过/reload命令来重载LootJS的修改内容

## 二、工作原理

LootJS的工作原理如下：

![](https://m1.miaomc.cn/uploads/20220424\_31c09db2a2078.png)

## 三、LootJS事件

### 1、事件监听

_注：本节代码应置于`kubejs\server_scripts`文件夹下_

要使用LootJS修改战利品表，你需要使用`LootJS.modifiers`事件。

```js
LootJS.modifiers(event => {
    // code here
});
```

### 2、修改逻辑

![](https://m1.miaomc.cn/uploads/20230708\_64a960a74e4a5.png)

值得注意的是，修改战利品表时应至少存在一个战利品表事件！

例如，以下为两个修改示例：

```js
LootJS.modifiers((event) => {
    // 玩家使用忽略nbt的下界合金镐破坏带有#forge:ores标签的方块时，额外掉落一个砂砾。
    event
        .addBlockLootModifier("#forge:ores") // 战利品表修饰器
        .matchMainHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT()) // 战利品表条件
        .addLoot("minecraft:gravel");// 战利品表事件
    // 玩家破坏绿宝石块时，添加基于二项分布的绿宝石掉落
    event
        .addBlockLootModifier("minecraft:emerald_block") // 战利品表修饰器
        .pool((p) => {// 战利品表事件（新建随机池）
            p.addLoot("minecraft:emerald");// 战利品表事件
            p.applyBinomialDistributionBonus("minecraft:fortune", 0.2, 3);// 战利品表函数
        });
});
```

## 四、战利品修饰器（Loot Modifier）

| **函数**                            | **功能**                             |
| ----------------------------------- | ------------------------------------ |
| addBlockLootModifier(...方块ID)     | 为方块添加新的战利品表修饰器         |
| addEntityLootModifier(...实体ID)    | 为实体添加新的战利品表修饰器         |
| addLootTableModifier(...命名空间ID) | 为给定战利品表添加新的修饰器         |
| addLootTypeModifier(...战利品表ID)  | 为给定战利品表类型\[1]添加新的修饰器 |