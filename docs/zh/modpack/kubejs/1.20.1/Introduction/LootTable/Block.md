# 方块战利品

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

- 事件：ServerEvents.blockLootTables(event=>{});

## 战利品

### 替换原有战利品

- 战利品池抽取次数与战利品项物品个数是一个[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- 语句：event.addBlock(方块id, loot => { loot.addPool(pool => { }) });

- 示例：砂砾只会掉落火药。

::: code-group

```js [单个物品]
ServerEvents.blockLootTables(event => {
    // addBlock将替换该方块战利品
    event.addBlock('minecraft:gravel', loot => {
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
ServerEvents.blockLootTables(event => {
    // addBlock将替换该方块战利品
    event.addBlock('minecraft:gravel', loot => {
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
ServerEvents.blockLootTables(event => {
    // addBlock将替换该方块战利品
    event.addBlock('minecraft:gravel', loot => {
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

### 添加额外战利品

- 战利品池抽取次数与战利品项物品个数是一个[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- 语句：event.modifyBlock(方块id, loot => { loot.addPool(pool => { }) });

- 示例：砂砾还会额外掉落火药。

::: code-group

```js [单个物品]
ServerEvents.blockLootTables(event => {
    // modifyBlock将为该方块添加新战利品池
    event.modifyBlock('minecraft:gravel', loot => {
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
ServerEvents.blockLootTables(event => {
    // modifyBlock将为该方块添加新战利品池
    event.modifyBlock('minecraft:gravel', loot => {
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
ServerEvents.blockLootTables(event => {
    // modifyBlock将为该方块添加新战利品池
    event.modifyBlock('minecraft:gravel', loot => {
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

### 全部

- [全部](LootTable.md#全部)

### 任何

- [任何](LootTable.md#任何)

### 方块状态属性

- [方块状态属性](LootTable.md#方块状态属性)

### 实体属性

- [实体属性](LootTable.md#实体属性)

### 实体分数

- [实体分数](LootTable.md#实体分数)

### 取反

- [取反](LootTable.md#取反)

### 检查位置

- [检查位置](LootTable.md#检查位置)

### 匹配工具

- [匹配工具](LootTable.md#匹配工具)

### 随机概率

- [随机概率](LootTable.md#随机概率)

::: code-group

```js [应用战利品池]
// 有0.5的概率尝试抽取该池 此时概率决定是否尝试抽取战利品池
ServerEvents.blockLootTables(event => {
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
ServerEvents.blockLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.rolls = [1, 1];
            pool.addItem('minecraft:gunpowder').randomChance(0.5);
        })
    })
})
```

:::

- 引用谓词文件

- 未被爆炸破坏

- 返回成功概率为1 / 爆炸半径，如果上下文未传递爆炸则始终通过。

- 语句：survivesExplosion()

- 示例：如果燧石被爆炸摧毁不会掉落火药。

```js
ServerEvents.blockLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.rolls = [1, 1];
            pool.addItem('minecraft:gunpowder').survivesExplosion()
        })
    })
})
```

- 附魔奖励

- 检查时间

- 检查值

- 检查天气

## 战利品修饰

### 对战利品项随机附魔

- 将对战利品项从附魔列表中随机附魔。

- 语句：enchantRandomly(附魔id数组);

- 示例：掉落了保护1的金苹果。

```js
ServerEvents.blockLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5, 1)
            .enchantRandomly(['minecraft:protection'])
        })
    })
})
```

### 对战利品项按等级附魔

- 对战利品项执行一次数字提供器返回的等级的附魔。

- 语句：enchantWithLevels(数字提供器, 是否包含宝藏附魔);

- 示例：砂砾掉落一把30级附魔的铁剑。

```js
ServerEvents.blockLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
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

- 示例：砂砾掉落橡木的熔炉冶炼产物。

```js
ServerEvents.blockLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:oak_wood', 5, 1).furnaceSmelt()
            
        })
    })
})
```
