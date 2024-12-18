---
authors: ['Gu-meng']
---
# 工具类
在本章节中将会提供简单的工具类型的写法和可调用的方法

## 基础写法
```js
StartupEvents.registry("item", event => {
    // 斧子
    event.create("meng:my_axe", "axe")
    // 锄头
    event.create("meng:my_hoe", "hoe")
    // 镐子
    event.create("meng:my_pickaxe", "pickaxe")
    // 铲子
    event.create("meng:my_shovel", "shovel")
    // 剑
    event.create("meng:my_sword", "sword")
})
```
以上是kubejs提供的工具类型

## 可调用的方法
|                 方法名                 |                         传入参数                         |                                 用处                                 | 返回类型 |
| :------------------------------------: | :------------------------------------------------------: | :------------------------------------------------------------------: | :------: |
|          speedBaseline(float)          |                            ->                            | 设置基本攻击速度(直接设置是在4的基础上做加减【剑默认-2.4,斧头-3.1】) |   this   |
|              speed(float)              |                            ->                            |                             设置攻击速度                             |   this   |
|      attackDamageBaseline(float)       |                            ->                            | 设置工具的基本伤害(直接设置是在3的基础上做加减【剑默认3,斧头默认6】) |   this   |
|        attackDamageBonus(float)        |                            ->                            |                             设置攻击伤害                             |   this   |
| modifyTier(Consumer\<MutableToolTier>) |                            ~                             |                                  ~                                   |   this   |
|               tier(Tier)               | [mcwiki](https://zh.minecraft.wiki/w/%E5%93%81%E8%B4%A8) |                             设置工具品质                             |   this    |