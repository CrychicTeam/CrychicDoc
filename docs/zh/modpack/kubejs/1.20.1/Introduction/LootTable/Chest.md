<!-- # 箱子战利品表

## 前言

- **`数字提供器`** 返回数字的表达式，详情查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- **`战利品`** 了解战利品可用的函数以及结构，查看[战利品](./LootTable.md)

## 箱子战利品表id

- 如：末地战利品箱的战利品id为：minecraft:chests/end_city_treasure，那么在函数中作为参数传递的字符串则为minecraft:end_city_treasure，在游戏中可使用`/loot give @s`命令补全查看。

## 战利品池

### 替换原战利品池

- 语句：event.addChest(箱子战利品表id, loot=>{创建战利品池});

- 示例：替换末地城宝藏，使其只有金苹果。

```js
ServerEvents.chestLootTables(event=>{
    event.addChest('minecraft:end_city_treasure', loot=>{
        loot.addPool(pool=>{
            pool.rolls = [1, 1];
            pool.addItem('minecraft:golden_apple');
        })
    })
})
```

### 添加新战利品池

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

### 随机概率

- 通过小数表示的随机概率。

- 语句：randomChance(数字);

- 例：

::: code-group

```js [应用战利品池]
// 有0.5的概率尝试抽取该池 此时概率决定是否尝试抽取战利品池
ServerEvents.chestLootTables(event => {
    event.modify('minecraft:end_city_treasure', loot => {
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
ServerEvents.chestLootTables(event => {
    event.modify('minecraft:end_city_treasure', loot => {
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
ServerEvents.chestLootTables(event => {
    event.modify('minecraft:end_city_treasure', loot => {
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
ServerEvents.chestLootTables(event => {
    event.modify('minecraft:end_city_treasure', loot => {
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
ServerEvents.chestLootTables(event => {
    event.modify('minecraft:end_city_treasure', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:oak_wood', 5, 1).furnaceSmelt()
            
        })
    })
})
``` -->

# 箱子类型战利品表

## 战利品表

::: code-group

```js [修改原战利品表]
ServerEvents.blockLootTables(event => {
    event.modify('minecraft:end_city_treasure', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [覆盖原战利品表]
ServerEvents.blockLootTables(event => {
    event.addChest('minecraft:end_city_treasure', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [带有谓词&修饰器的]
ServerEvents.blockLootTables(event => {
    event.addChest('minecraft:end_city_treasure', loot => {// [!code ++] 覆盖原战利品表
        loot.addPool(pool => {// [!code ++] 向战利品表添加战利品池
            pool.addItem('minecraft:diamond')// [!code ++] 向战利品池添加添加战利品

            pool.addItem('minecraft:diamond').addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++] 可以向战利品的物品修饰器列表添加物品修饰器

            pool.addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++] 可以向战利品池的物品修饰器列表添加物品修饰器
        })

        loot.addPool(pool => {// [!code ++] 还可以再向战利品表添加战利品池
            pool.addItem('minecraft:diamond')// [!code ++] 向战利品池添加添加战利品
            pool.addItem('minecraft:diamond').survivesExplosion()// [!code ++] 向战利品的谓词列表添加谓词
            pool.survivesExplosion()// [!code ++] 向战利品池的谓词列表添加谓词
        })

        loot.addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++] 可以向战利品表的物品修饰器列表添加物品修饰器

    })
})

```

:::

## 谓词

- 引用页面。

## 物品修饰器

- 引用页面。

## 示例
