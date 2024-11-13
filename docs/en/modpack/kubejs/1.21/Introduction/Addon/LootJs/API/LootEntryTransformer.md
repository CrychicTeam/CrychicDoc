# LootEntryTransformer

The `LootEntryTransformer` allows you to modify, replace, or remove loot entries within a loot table. It adds multiple functionalities to cater to different use cases.

Each class that implements the `LootEntryTransformer` interface can use all the functions listed in this chapter.

- [`LootTable`](LootTable.md)
- [`LootPool`](LootPool.md)
- [`CompositeLootEntry`](LootEntry.md)

Since `CompositeLootEntry` supports nesting of `CompositeLootEntry`, the functions in this chapter have a `deep` parameter, which determines whether to traverse all entries in the loot table. The default value is true.

## removeItem

Removes all `LootEntry` of type `LootItemEntry`.

Syntax:

- `.removeItem(filter: ItemFilter)`, see [Item Filter](ItemFilter.md)
- `.removeItem(filter: ItemFilter, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/simple_dungeon")
        .removeItem(ItemFilter.hasEnchantment("minecraft:fortune"))

        // We can also directly use the item ID. LootJS will automatically convert it to an ItemFilter
        // .removeItem("minecraft:diamond")
})
```

## removeTag

Removes all `LootEntry` of type `LootTagEntry`.

Syntax:

- `.removeTag(tag: string)`
- `.removeTag(tag: string, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    event.modifyLootTypeTables("chest").removeTag("#c:ores")
})
```

## removeReference

Removes all `LootEntry` of type `TableReferenceLootEntry`.

Syntax:

- `.removeReference(filter: string | regex)`
- `.removeReference(filter: string | regex, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:gameplay/fishing")
         .removeReference("minecraft:gameplay/fishing/junk")
})
```

## removeEntry

Removes an entry through a specified filter. The filter must return true or false. When it returns true, the entry will be removed.

Syntax:

- `.removeEntry((entry) => { ... })`
- `.removeEntry((entry) => { ... }, deepRemove: boolean)`

```js
LootJS.lootTables(event => {
    // Let's remove all reference entries from the loot tables
    event.modifyLootTables(/.*/)
         .removeEntry(entry => entry.isReference())
})
```

## replaceItem

Replaces all items matching the filter criteria with a specified item. By replacing items, all loot predicates and loot item functions will be preserved. If you do not wish to do this, use `modifyItem` and create your own `LootEntry`.

Syntax:

- `.replaceItem(filter: ItemFilter, item: Item)`, see [Item Filter](ItemFilter.md)
- `.replaceItem(filter: ItemFilter, item: Item, deepReplace: boolean)`

```js
LootJS.lootTables((event) => {
    event.modifyLootTypeTables("chest").replaceItem(ItemFilter.)
})
```

## modifyEntry

Modifies all loot entries. A loot entry must always be returned again.

Syntax:

- `.modifyEntry((entry: LootEntry) => { ... })`
- `.modifyEntry((entry: LootEntry) => { ... }, deepModify: boolean)`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon").modifyEntry(entry => {
        if (entry.isItem() && entry.item.id === "minecraft:string") {
            entry.setCount([5, 12])
        }

        // Always remember to return an entry!
        return entry
    })
})
```

## modifyItem

Similar to `modifyEntry`, but only traverses `LootItemEntry`.

Syntax:

- `.modifyItem((entry: LootItemEntry) => { ... })`

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/simple_dungeon").modifyItem(itemEntry => {
        if (itemEntry.item.id === "minecraft:string") {
            itemEntry.setCount([5, 12])
        }

        // Always remember to return an entry!
        return itemEntry
    })
})
```
