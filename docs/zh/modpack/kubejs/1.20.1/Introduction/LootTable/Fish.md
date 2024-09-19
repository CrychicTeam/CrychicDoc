# 钓鱼类型战利品表

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

- 事件：ServerEvents.fishingLootTables(event=>{});

## 钓鱼战利品表id

fish —— 表示钓鱼战利品表中的鱼。

junk —— 表示钓鱼战利品表中的垃圾。

treasure —— 表示钓鱼战利品表中的宝藏。

## 战利品池

### 替换原战利品池

- 战利品池抽取次数与战利品项物品个数是一个数字提供器

- 语句：event.addFishing(钓鱼战利品表id, loot => { loot.addPool(pool => { }) });

- 示例：钓鱼只会掉落火药。

::: code-group

```js [单个物品]
ServerEvents.fishingLootTables(event => {
    // addFishing将替换该id钓鱼战利品表
    event.addFishing('fish', loot => {
        // addPool创建一个战利品池
        loot.addPool(pool => {
            // 战利品池抽取次数
            pool.rolls = [1, 1];
            // 添加物品
            pool.addItem('minecraft:gunpowder')
        })
    })
})
```

```js [权重列表]
ServerEvents.fishingLootTables(event => {
    // addFishing将替换该id钓鱼战利品表
    event.addFishing('fish', loot => {
        // addPool创建一个战利品池
        loot.addPool(pool => {
            // 战利品池抽取次数
            pool.rolls = [1, 1];
            // 添加物品
            pool.addItem('minecraft:gunpowder', 50)
            pool.addItem('minecraft:iron_ingot', 50)
        })
    })
})
```

```js [具有战利品项个数提供器的权重列表]
ServerEvents.fishingLootTables(event => {
    // addFishing将替换该id钓鱼战利品表
    event.addFishing('fish', loot => {
        // addPool创建一个战利品池
        loot.addPool(pool => {
            // 战利品池抽取次数
            pool.rolls = [1, 1];
            // 添加物品
            pool.addItem('minecraft:gunpowder', 50, {min: 1, max: 3})
            pool.addItem('minecraft:iron_ingot', 50, {min: 1, max: 3})
        })
    })
})
```

:::

### 添加新战利品池

- 战利品池抽取次数与战利品项物品个数是一个数字提供器

- 语句：event.modify(方块id, loot => { loot.addPool(pool => { }) });

- 语句：pool.rolls 设置抽取次数。

- 语句：pool.addItem(args)，添加战利品项，具有3个方法重载。

- 示例：尸壳还会额外掉落火药。

::: code-group

```js [单个物品]
ServerEvents.fishingLootTables(event => {
    // modify将添加新战利品池
    event.modify('fish', loot => {
        // addPool创建一个战利品池
        loot.addPool(pool => {
            // 战利品池抽取次数
            pool.rolls = [1, 1];
            // 添加物品
            pool.addItem('minecraft:gunpowder')
        })
    })
})
```

```js [权重列表]
ServerEvents.fishingLootTables(event => {
    // modify将添加新战利品池
    event.modify('fish', loot => {
        // addPool创建一个战利品池
        loot.addPool(pool => {
            // 战利品池抽取次数
            pool.rolls = [1, 1];
            // 添加物品
            pool.addItem('minecraft:gunpowder', 50)
            pool.addItem('minecraft:iron_ingot', 50)
        })
    })
})
```

```js [具有战利品项个数提供器的权重列表]
ServerEvents.fishingLootTables(event => {
    // modify将添加新战利品池
    event.modify('fish', loot => {
        // addPool创建一个战利品池
        loot.addPool(pool => {
            // 战利品池抽取次数
            pool.rolls = [1, 1];
            // 添加物品
            pool.addItem('minecraft:gunpowder', 50, {min: 1, max: 3})
            pool.addItem('minecraft:iron_ingot', 50, {min: 1, max: 3})
        })
    })
})
```

:::

### 修改原有战利品

- 等待后续...

## 战利品谓词

### 随机概率

- 通过小数表示的随机概率。

- 语句：randomChance(数字);

- 例：

::: code-group

```js [应用战利品池]
// 有0.5的概率尝试抽取该池 此时概率决定是否尝试抽取战利品池
ServerEvents.fishingLootTables(event => {
    event.modify('fish', loot => {
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
ServerEvents.fishingLootTables(event => {
    event.modify('fish', loot => {
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
ServerEvents.fishingLootTables(event => {
    event.modify('fish', loot => {
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
ServerEvents.fishingLootTables(event => {
    event.modify('fish', loot => {
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
ServerEvents.fishingLootTables(event => {
    event.modify('fish', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:oak_wood', 5, 1).furnaceSmelt()
            
        })
    })
})
```
