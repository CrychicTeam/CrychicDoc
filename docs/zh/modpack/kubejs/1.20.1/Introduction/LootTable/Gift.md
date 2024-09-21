# 礼物类型战利品表

## 战利品表

::: details 赠送村庄英雄的礼物

- armorer_gift —— 盔甲商

- butcher_gift —— 屠夫

- cartographer_gift —— 制图师

- cleric_gift —— 牧师

- farmer_gift —— 农民

- fisherman_gift —— 渔夫

- fletcher_gift —— 制箭师

- leatherworker_gift —— 皮匠

- librarian_gift —— 图书管理员

- mason_gift —— 石匠

- shepherd_gift —— 牧羊人

- toolsmith_gift —— 工具商

- weaponsmith_gift —— 武器商

:::

::: details 猫的礼物

- 很可惜 暂无

:::

::: code-group

```js [修改原战利品表]
ServerEvents.blockLootTables(event => {
    event.modify('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [覆盖原战利品表]
ServerEvents.blockLootTables(event => {
    event.addGift('armorer_gift', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++] 添加战利品
        }) 
    })
})
```

```js [带有谓词&修饰器的]
ServerEvents.blockLootTables(event => {
    event.addGift('armorer_gift', loot => {// [!code ++] 覆盖原战利品表
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
