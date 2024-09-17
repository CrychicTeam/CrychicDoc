# 礼物战利品表

:::: warning **注意**
::: justify
Gift类型的战利品表目前仅能对村民赠与村庄英雄玩家的礼物起效，目前暂未得知对猫咪早晨赠与玩家的礼物如何操作。
:::
::::

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

## 战利品

### 村庄英雄礼物战利品id

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

### 替换原有战利品

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

### 添加额外战利品

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

## 战利品谓词

### 随机概率

- 通过小数表示的随机概率。

- 语句：randomChance(数字);

- 例：

::: code-group

```js [应用战利品池]
// 有0.5的概率尝试抽取该池 此时概率决定是否尝试抽取战利品池
ServerEvents.giftLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.rolls = [1, 1];
            pool.addItem('minecraft:gunpowder');
            pool.randomChance(0.5);
        })
    })
})
```

```js [应用战利品项]
// 有0.5的概率尝试掉落火药 此时概率决定抽取战利品池后是否掉落火药
ServerEvents.giftLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.rolls = [1, 1];
            pool.addItem('minecraft:gunpowder').randomChance(0.5);
        })
    })
})
```

:::

## 战利品修饰

### 对战利品项随机附魔

- 将对战利品项从附魔列表中随机附魔。

- 语句：enchantRandomly(附魔id数组);

- 示例：掉落了保护1的金苹果。

```js
ServerEvents.entityLootTables(event => {
    event.modify('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5, 1)
            .enchantRandomly(['minecraft:protection'])
        })
    })
})
```

### 对战利品项按等级附魔

- 对战利品项执行一次数字提供器返回的等级的附魔。

- 语句：.enchantWithLevels(数字提供器, 是否包含宝藏附魔);

- 示例：尸壳掉落一把30级附魔的铁剑。

```js
ServerEvents.entityLootTables(event => {
    event.modify('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem(Item.of('minecraft:iron_sword', '{Damage:0}'), 5, 1)
            .enchantWithLevels(30, true)
        })
    })
})
```

### 熔炉熔炼

- 得到物品放入熔炉冶炼后的产物。

- 语句：furnaceSmelt()

- 示例：尸壳死亡掉落橡木的熔炉冶炼产物。

```js
ServerEvents.entityLootTables(event => {
    event.modify('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:oak_wood', 5, 1).furnaceSmelt()
            
        })
    })
})
```
