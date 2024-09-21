# 钓鱼类型战利品表

## 战利品表

- fish —— 表示钓鱼战利品表中的鱼。

- junk —— 表示钓鱼战利品表中的垃圾。

- treasure —— 表示钓鱼战利品表中的宝藏。

::: code-group

```js [修改原战利品表]
ServerEvents.blockLootTables(event => {
    event.modify('fish', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [覆盖原战利品表]
ServerEvents.blockLootTables(event => {
    event.addFishing('fish', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [带有谓词&修饰器的]
ServerEvents.blockLootTables(event => {
    event.addFishing('fish', loot => {// [!code ++] 覆盖原战利品表
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
