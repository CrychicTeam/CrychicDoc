# Item Filter

The Item Filter is an important tool in LootJS, primarily used for filtering items based on specified conditions.

Wherever an item filter can be used as a parameter, we can simply pass the item's ID as a string or a tag, and LootJS will automatically create an item filter for it.

## armor

Matches if the item is an armor item.

::: v-info

Items from mods that do not use the vanilla armor system but create their own armor items will not be matched.

:::

Syntax:

- `ItemFilter.ARMOR`

## blockItem

Matches if the item is a block item.

Syntax:

- `ItemFilter.BLOCK_ITEM`

## custom

You can create a custom item filter.

Syntax:

- `ItemFilter.custom(filter: (item: ItemStack) => boolean)`

```js
// Matches when the item ID is 'minecraft:apple'.
ItemFilter.custom(item => item.id === "minecraft:apple")

// Matches when the item has the #c:ores tag and the quantity is greater than 16.
ItemFilter.custom(item => {
    if (item.hasTag("#c:ores")) {
        return true
    }

    return item.count > 16
})
```

## damageable

Matches if the item can be damaged.

Syntax:

- `ItemFilter.DAMAGEABLE`

## damaged

Matches if the item is damaged.

Syntax:

- `ItemFilter.DAMAGED`

## edible

Matches if the item can be used.

Syntax:

- `ItemFilter.EDIBLE`

## enchanted

Matches if the item is enchanted.

Syntax:

- `ItemFilter.ENCHANTED`

## empty

Checks if the item is empty.

Syntax:

- `ItemFilter.EMPTY`

## equipmentSlot

Matches if the item is in a specific equipment slot. Currently supported slots are: `mainhand`, `offhand`, `head`, `chest`, `legs`, `feet`.

Syntax:

- `ItemFilter.equipmentSlot(slot: string | EquipmentSlot)`

```js
ItemFilter.equipmentSlot("mainhand")
```

## equipmentSlotGroup

Matches if the item is in a specific equipment group. Currently supported groups are: `any`, `mainhand`, `offhand`, `hand`, `feet`, `legs`, `chest`, `head`, `armor`.

Syntax:

- `ItemFilter.equipmentSlotGroup(slot: string | EquipmentSlotGroup)`

```js
ItemFilter.equipmentSlotGroup("armor")
```

## hasEnchantment

Used to check if the given item matches an enchantment.

Syntax:

- `ItemFilter.hasEnchantment(filter)`
- `ItemFilter.hasEnchantment(filter, levelRange: Range)`, about Range

::: code-group

```js [Only matching enchantment]
ItemFilter.hasEnchantment("minecraft:fortune")
```

```js [Matching by mod]
// Matches all enchantments from 'minecraft'
ItemFilter.hasEnchantment("@minecraft")
```

```js [Matching by an array of enchantments]
// Matches all enchantments in the array
ItemFilter.hasEnchantment(["minecraft:fortune", "minecraft:mending"])
```

```js [Matching by enchantment level]
// Matches Unbreaking enchantments of levels 2-3
ItemFilter.hasEnchantment("minecraft:unbreaking", [2, 3])
```

:::

## hasStoredEnchantment

Used to check if the given enchantment book matches an enchantment.

Syntax:

- `ItemFilter.hasStoredEnchantment(filter)`
- `ItemFilter.hasStoredEnchantment(filter, levelBound: Range)` about Range

::: code-group

```js [Only matching enchantment]
ItemFilter.hasStoredEnchantment("minecraft:fortune")
```

```js [Matching by mod]
// Matches all enchantments from 'minecraft'
ItemFilter.hasStoredEnchantment("@minecraft")
```

```js [Matching by an array of enchantments]
// Matches all enchantments in the array
ItemFilter.hasStoredEnchantment(["minecraft:fortune", "minecraft:mending"])
```

```js [Matching by enchantment level]
// Matches Unbreaking enchantments of levels 2-3
ItemFilter.hasStoredEnchantment("minecraft:unbreaking", [2, 3])
```

:::

## item

Matches if the item matches. This cannot detect the quantity of the item, but it can detect components.

- `ItemFilter.item(item: ItemStack | string)`
- `ItemFilter.item(item: ItemStack | string, matchComponents: boolean)`

## not/negate

Reverses the filter. If the filter does not match, it matches.

Syntax:

- `ItemFilter.not(filter: ItemFilter)`
- `anyFilter.negate()`, where `anyFilter` is not referring to a filter but the filters mentioned in this tutorial.

```js
ItemFilter.not(ItemFilter.hasEnchantment("minecraft:fortune"))

ItemFilter.hasEnchantment("minecraft:fortune").negate()
```

## tag

Matches if the item has a specific tag.

Syntax:

- `ItemFilter.tag(tag: string)`

## toolAction

NeoForge added a ToolAction, which can be used to determine if an item can perform a specific action. Some mods use it for multi-tool functionality.

Matches if all actions exist.

Syntax:

- `ItemFilter.toolAction(...action)`
- `ItemFilter.anyToolAction(...action)`

::: code-group

```js [Only matching one action]
ItemFilter.toolAction("pickaxe_dig")

ItemFilter.anyToolAction("pickaxe_dig")
```

```js [Matching multiple actions]
ItemFilter.toolAction("pickaxe_dig", "shovel_dig")

ItemFilter.anyToolAction("pickaxe_dig", "shovel_dig")
```

:::

## Combined Matching

### allOf

Merges multiple item filters into one. Matches if all filters match.

Syntax:

- `ItemFilter.allOf(...filters: ItemFilter[])`

```js
ItemFilter.allOf(
    ItemFilter.hasEnchantment("minecraft:fortune"), 
    ItemFilter.equipmentSlotGroup("hand")
)
```

### anyOf

Merges multiple item filters into one. Matches if at least one filter matches.

Syntax:

- `ItemFilter.anyOf(...filters: ItemFilter[])`

```js
ItemFilter.anyOf(
    ItemFilter.hasEnchantment("minecraft:silk_touch"),
    ItemFilter.equipmentSlotGroup("armor")
)
```

### all

Returns a filter that matches all items.

Syntax:

- `ItemFilter.ALL`

### none

Returns a filter that matches no items.

Syntax:

- `ItemFilter.NONE`
