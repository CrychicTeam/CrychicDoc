# 随机池

- 存在于每个战利品表的随机池列表中，内含抽取项（战利品），每个随机池都有自己的抽取次数设置，触发战利品表时每个随机池进行有放回的独立抽取。

## 添加随机池

- 创建一个新随机池。

- 语句：addPool(pool=>{});

```js
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {// [!code ++]
        })// [!code ++]
    })
})
```

## 抽取次数

- **`默认值`** 默认值为1。

- pool.rolls 是一个[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- pool.setUniformRolls(min, max) 设置取值范围，接受最小值与最大值

- pool.setBinomialRolls(n, p) 设置二项分布，接受n尝试次数，p每次尝试成功概率，期望次数np

::: code-group

```js [绝对值]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.rolls = 1// [!code ++]
        })
    })
})
```

```js [取值范围]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.setUniformRolls(1, 1)// [!code ++]
        })
    })
})
```

```js [二项分布]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.setBinomialRolls(5, 0.5)// [!code ++]
        })
    })
})
```

:::
