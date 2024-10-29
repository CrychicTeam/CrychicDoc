# LootEntryTransformer

`LootEntryTransformer` 允许您修改、替换或删除战利品表中的战利品抽取项。它添加了多个功能以满足不同用例的需求。

每个实现了 `LootEntryTransformer` 接口的类都可以使用本章内容中列出的所有函数。

- `LootTable`
- `LootPool`
- `CompositeLootEntry`

因为 `CompositeLootEntry` 支持进行嵌套 `CompositeLootEntry`，因此本章内容中的函数都有一个 `deep` 参数，该参数用于判断是否需要遍历战利品表的所有抽取项，默认值为 true。

## removeItem

删除所有类型为 `LootItemEntry` 的 `LootEntry`。

语法：

- `.removeItem(filter: ItemFilter)` ，查看[物品过滤器](ItemFilter.md)
- `.removeItem(filter: ItemFilter, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/simple_dungeon")
        .removeItem(ItemFilter.hasEnchantment("minecraft:fortune"))

        // 我们也可以直接使用物品 ID。LootJS将会自动将其转换为 ItemFilter
        // .removeItem("minecraft:diamond")
})
```

## removeTag

删除所有类型为 `LootTagEntry` 的 `LootEntry`。

语法：

- `.removeTag(tag: string)`
- `.removeTag(tag: string, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    event.modifyLootTypeTables("chest").removeTag("#c:ores")
})
```

## removeReference

删除所有类型为 `TableReferenceLootEntry` 的 `LootEntry`。

语法：

- `.removeReference(filter: string | regex)`
- `.removeReference(filter: string | regex, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:gameplay/fishing")
         .removeReference("minecraft:gameplay/fishing/junk")
})
```

## removeEntry

删除通过某个指定过滤器的一个抽取项。过滤器必须返回 true 或 false 。当返回 true 时，抽取项将被删除。

语法：

- `.removeEntry((entry) => { ... })`
- `.removeEntry((entry) => { ... }, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    // 让我们删除所有战利品表中的引用抽取项
    event.modifyLootTables(/.*/)
         .removeEntry(entry => entry.isReference())
})
```