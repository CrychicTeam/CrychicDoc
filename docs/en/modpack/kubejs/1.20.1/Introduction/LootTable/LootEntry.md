# 战利品

- 本章节弃用xxx

- 用于在本章中需要表示战利品的任何地方。

## 前言

:::: warning **注意**
::: justify
本章需要安装Kubejs附属模组[lootJs](https://www.mcmod.cn/class/6327.html)
:::
::::

## 战利品表示

### 字符串表示法

- 字符串表示法，用于简单的只涉及物品的表示。

```js
// 代表一个火药战利品
'minecraft:gunpowder'
```

### 对象表示法

- 推荐阅读[物品表示法](../Recipe/ItemAndIngredient.md)，一切涉及“物品”参数均可使用物品表示法。

- 作为LootEntry对象而存在，支持调用该类函数以实现复杂功能。

- 语句：LootEntry.of(args); 拥有4个方法重载。

- 语句：LootEntry.withChance(物品, 几率数字); 创建一个带有掉落几率（取值范围\(0, 1\]）的战利品。

::: code-group

```js [使用物品表示法]
// 表示一个火药战利品
LootEntry.of(Item.of('minecraft:gunpowder'));
// 或更简便的使用字符串
LootEntry.of('minecraft:gunpowder');
```

```js [指定数量]
// 表示一个指定数量的火药战利品
LootEntry.of('minecraft:gunpowder', 1);
```

```js [指定NBT]
// 表示一个指定NBT的火药战利品
LootEntry.of('minecraft:gunpowder', '{test:test}');
```

```js [指定数量与NBT]
// 表示一个指定数量与NBT的火药战利品
LootEntry.of('minecraft:gunpowder', 1, '{test:test}');
```

```js [概率战利品]
// 表示一个具有0.5掉落几率的战利品
LootEntry.withChance(Item.of('minecraft:gunpowder'), 0.5);
```

:::

## 为战利品添加修饰

- **`物品修饰器`** 为战利品中的物品应用物品修饰器，引用文献[minecraft-wiki/物品修饰器](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8)，在这里你可以使用kjs代码的方式应用这些修饰器。

### 应用奖励

- 语句：.applyBonus(附魔, 数字);

### 添加权重

- 用于权重战利品池中。

- 语句：withWeight(数字);

```js
LootEntry.of('minecraft:gunpowder').withWeight(50);
```

### 添加战利品函数

- 语句：addFunction(战利品函数回调);

- 回调内返回1个ItemStack对象，。

```js
LootEntry.of(Item.of('minecraft:gunpowder')).addFunction((lootItem) => {});
```

### 从json添加战利品表函数

- 语句：customFunction(Json);

- 这是一种数据包的方式添加战利品表函数回调。

```js
LootEntry.of(Item.of('minecraft:gunpowder')).customFunction(Json);
```

## 为战利品添加条件
