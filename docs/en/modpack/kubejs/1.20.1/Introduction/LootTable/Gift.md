# 礼物战利品表

:::: warning **注意**
::: justify
Gift类型的战利品表目前仅能对村民赠与村庄英雄玩家的礼物起效，目前暂未得知对猫咪早晨赠与玩家的礼物如何操作。
:::
::::

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

## 村庄英雄礼物战利品id

armorer_gift —— 盔甲商

butcher_gift —— 屠夫

cartographer_gift —— 制图师

cleric_gift —— 牧师

farmer_gift —— 农民

fisherman_gift —— 渔夫

fletcher_gift —— 制箭师

leatherworker_gift —— 皮匠

librarian_gift —— 图书管理员

mason_gift —— 石匠

shepherd_gift —— 牧羊人

toolsmith_gift —— 工具商

weaponsmith_gift —— 武器商

## 替换原有战利品

- 语句：event.addGift(村庄英雄礼物战利品id, loot=>{创建战利品池});

- 示例：替换了盔甲商的村庄英雄礼物战利品，现在他只会送金合欢木船。

```js
ServerEvents.giftLootTables(event => {
    event.addGift('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:acacia_boat')
        })
    })
})
```

## 添加额外战利品

- 语句：event.modify(村庄英雄礼物战利品id, loot=>{创建战利品池});

- 示例：替换了盔甲商的村庄英雄礼物战利品，现在他额外赠送一条金合欢木船。

```js
ServerEvents.giftLootTables(event => {
    event.modify('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:acacia_boat')
        })
    })
})
```
