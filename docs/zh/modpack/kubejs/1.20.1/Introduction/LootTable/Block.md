# 方块类型战利品表

## 战利品表

::: code-group

```js [修改原战利品表]
ServerEvents.blockLootTables(event => {
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [覆盖原战利品表]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [带有谓词&修饰器的]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {// [!code ++]
        loot.addPool(pool => {// [!code ++]
            pool.addItem('minecraft:diamond')// [!code ++]
// [!code ++]
            pool.addItem('minecraft:diamond').addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++]
// [!code ++]
            pool.addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++]
        })
// [!code ++]
        loot.addPool(pool => {// [!code ++]
            pool.addItem('minecraft:diamond')// [!code ++]
            pool.addItem('minecraft:diamond').survivesExplosion()// [!code ++]
            pool.survivesExplosion()// [!code ++]
        })
// [!code ++]
        loot.addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))//

    })
})

```

:::

## 谓词

- 引用页面。

## 物品修饰器

- 引用页面。

## 示例
