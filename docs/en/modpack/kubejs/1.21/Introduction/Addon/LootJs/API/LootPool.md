# LootPool

随机池用于决定在何种情况下生成何种物品。主要定义了若干待选的抽取项。它通过使用多个不同的池来帮助在战利品表中分组物品。

`LootPool` 扩展自 [`LootEntriesTransformer`](LootEntryTransformer.md) ，其中的每个功能都可以应用于掉落池。

## getName

获取随机池的名称。如果未设置名称则可能返回 null。一些模组会为其自己添加的随机池命名，因此您可以使用此方法来识别它们。

语法：

- `.getName()`

```js
LootJS.lootTables(event => {
    let name = event.getLootTable("minecraft:chests/simple_dungeon")
                    .firstPool().getName()
})
```

## name

设置随机池的名称。

语法：

- `.name(name: string)`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon")
         .firstPool().name("example_name")
})
```

## rolls

指定该随机池的抽取次数。

语法：

- `.rolls(rolls: NumberProvider)`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon")
         .firstPool()
         .rolls([1, 5]) // 将会随机抽取1到5次
})
```

## bonusRolls

设置随机池中额外的抽取次数。默认值为0。

语法：

- `.bonusRolls(rolls: NumberProvider)`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon")
         .firstPool()
         .bonusRolls(1)
})
```

## when

设置随机池的谓词列表。如果没有满足谓词，则跳过池。见 [`LootCondition`](LootConditions.md) 获取更多信息。

语法：

- `.when((conditions) => {})`

```js
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/simple_dungeon")
        .firstPool()
        .when(conditions => {
            conditions.randomChance(0.5)
        })
})
```

## getConditions

返回所有附加在随机池中的谓词列表。更多信息请见 [`LootCondition`](LootConditions.md)。

语法：

- `.getConditions()`
- `.conditions`

```js
LootJS.lootTables(event => {
    let conditions = event.getLootTable("minecraft:chests/simple_dungeon")
                          .firstPool().getConditions()
    conditions.add(LootCondition.randomChance(0.5))
})
```

## apply

设置随机池的物品修饰器。见 [`LootFunction`](LootFunctions.md) 获取更多信息。

语法：

- `.apply((functions) => {})`

```js
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/simple_dungeon")
        .firstPool()
        .apply(functions => {
            functions.setCount([1, 25])
        })
})
```

## getFunctions

返回所有附加在随机池中的物品修饰器列表。更多信息请见 [`LootFunction`](LootFunctions.md)。

语法：

- `.getFunctions()`
- `.functions`

```js
LootJS.lootTables(event => {
    let functions = event.getLootTable("minecraft:chests/simple_dungeon")     
                         .firstPool().getFunctions()

    functions.add(LootFunction.setCount([1, 25]))
})
```

## getEntries

返回随机池中所有 `LootEntry` 的列表。

语法：

- `.getEntries()`
- `.entries`

```js
LootJS.lootTables(event => {
    let entries = event.getLootTable("minecraft:chests/simple_dungeon")
                       .firstPool().getEntries()

    entries.addEntry("minecraft:apple")
})
```

## addEntry

添加新的 `LootEntry` 到池中。查看 [`LootEntry`](LootEntry.md) 获取更多信息。

语法：

- `.addEntry(entry: LootEntry)`

```js
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/simple_dungeon")
        .firstPool()
        .addEntry(LootEntry.of("minecraft:apple"))
})
```
