# 箱子战利品表

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

## 战利品

### 箱子战利品id

- 如：末地战利品箱的战利品id为：minecraft:chests/end_city_treasure，那么在函数中作为参数传递的字符串则为minecraft:end_city_treasure，在游戏中可使用`/loot give @s`命令补全查看。

### 替换原有战利品

- 语句：event.addChest(箱子战利品表id, loot=>{创建战利品池});

- 示例：替换末地城宝藏，使其只有金苹果。

```js
ServerEvents.chestLootTables(event=>{
    event.addChest("minecraft:end_city_treasure",loot=>{
        loot.addPool(pool=>{
            pool.rolls = [1, 1];
            pool.addItem('minecraft:golden_apple');
        })
    })
})
```

### 添加额外战利品

- 语句：event.modify(箱子战利品表id, loot=>{创建战利品池});

- 示例：向末地城宝藏添加金苹果。

```js
ServerEvents.chestLootTables(event=>{
    event.modify("minecraft:end_city_treasure",loot=>{
        loot.addPool(pool=>{
            pool.rolls = [1, 1];
            pool.addItem('minecraft:golden_apple');
        })
    })
})
```

## 战利品谓词

## 战利品修饰
