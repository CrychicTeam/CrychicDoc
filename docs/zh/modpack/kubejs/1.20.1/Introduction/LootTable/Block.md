# 方块战利品

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

- 事件：ServerEvents.blockLootTables(event=>{});

## 替换原有战利品

- 战利品池抽取次数与战利品项物品个数是一个数字提供器

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

## 添加额外战利品

- 战利品池抽取次数与战利品项物品个数是一个数字提供器

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

## 修改原有战利品

- 等待后续...
