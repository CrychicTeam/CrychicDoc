# LootTable

战利品表用于决定在何种情况下生成何种物品。

`LootTables` 在 LootJS 中继承自 `LootEntriesTransformer` 。更多信息请见 [`LootEntriesTransformer`](LootEntryTransformer.md)。

## firstPool

返回战利品表中第一个随机池。如果没有随机池存在，则创建一个并返回。

语法：

- `.firstPool()`
- `.firstPool((pool) => {})`

```js
LootJS.lootTables(event => {
    let pool = event.getLootTable("minecraft:chests/simple_dungeon").firstPool()
})
```

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon")
         .firstPool(pool => {
            // modify the pool here
    })
})
```

## createPool

创建一个新的随机池并返回它。

语法：

- `.createPool()`
- `.createPool((pool) => {})`

```js
LootJS.lootTables(event => {
    let pool = event.getLootTable("minecraft:chests/simple_dungeon").createPool()
})
```

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon")
         .createPool(pool => {
            // 在这里自定义你的随机池
    })
})
```

## getFunctions

返回所有附加到战利品表的物品修饰器列表。

语法：

- `.getFunctions()`

```js
LootJS.lootTables(event => {
    let functions = event.getLootTable("minecraft:chests/simple_dungeon").getFunctions()
})
```

## onDrop

添加一个自定义的回调函数，该回调函数会在战利品表抽取出物品后触发。

语法：

- `.onDrop((context, loot) => {})`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon").onDrop((context, loot) => {
        for (let item of loot) {
            console.log(item)
        }
    })
})
```

## getLocation

获取战利品表的id。

语法：

- `.getLocation()`

```js
LootJS.lootTables(event => {
    let table = event.getLootTable("minecraft:chests/simple_dungeon")
    let location = table.getLocation() 
    // 将会返回 `"minecraft:chests/simple_dungeon"`
})
```

## getLootType

获取战利品表的类型。

语法：

- `.getLootType()`

```js
LootJS.lootTables(event => {
    let table = event.getLootTable("minecraft:chests/simple_dungeon")
    let type = table.getLootType() // 将会返回 `LootType.CHEST`
})
```

## clear

清空战利品表。这就会移除战利品表中所有的物品修饰器和随机池。

语法：

- `.clear()`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon").clear()
})
```

## print

打印出战利品表。可以用于调试。

语法：

- `.print()`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon").print()
})
```
