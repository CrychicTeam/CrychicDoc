# ItemFilter

物品过滤器是 LootJS 中的一个重要使用工具。主要用于根据给定的条件过滤物品。

在任何可以使用物品过滤器作为参数的地方，我们都可以简单的使用物品的id作为字符串或者标签传递参数，LootJS将会自动为其创建物品过滤器。

## armor

如果该物品是armor类物品则会被匹配。

::: v-info

当一些模组的物品不是使用原版armor系统，而是使用自己创建的armor类物品时，这些物品不会被匹配到。

:::

语法：

- `ItemFilter.ARMOR`

## blockItem

如果该物品是 block item 时可以被匹配。

语法：

- `ItemFilter.BLOCK_ITEM`

## custom

你可以创建自定义的物品过滤器。

语法：

- `ItemFilter.custom(filter: (item: ItemStack) => boolean)`

```js
// 当物品id为 minecraft:apple 时会被匹配。
ItemFilter.custom(item => item.id === "minecraft:apple")

// 当物品拥有 #c:ores 标签并且数量大于 16 时会被匹配。
ItemFilter.custom(item => {
    if (item.hasTag("#c:ores")) {
        return true
    }

    return item.count > 16
})
```

## damageable

如果物品可以被损坏则会被匹配。

语法：

- `ItemFilter.DAMAGEABLE`

## damaged

如果物品已经损坏则会被匹配。

语法：

- `ItemFilter.DAMAGED`

## edible

如果物品可以被使用则会被匹配。

语法：

- `ItemFilter.EDIBLE`

## enchanted

如果该物品已经被附魔则会被匹配。

语法：

- `temFilter.ENCHANTED`

## empty

检查该物品是否为空。

语法：

- `ItemFilter.EMPTY`

## equipmentSlot

如果物品在特定的装备槽位则匹配。目前支持检测的槽位有： `mainhand` 、 `offhand` 、 `head` 、 `chest` 、 `legs` 、 `fee"`。

语法：

- `ItemFilter.equipmentSlot(slot: string | EquipmentSlot)`

```js
ItemFilter.equipmentSlot("mainhand")
```

## equipmentSlotGroup

如果物品位于特定装备组中则匹配。现有组： `any` 、 `mainhand` 、 `offhand` 、 `hand` 、 `feet` 、 `legs` 、 `chest` 、 `head` 、 `armor`。

语法：

- `ItemFilter.equipmentSlotGroup(slot: string | EquipmentSlotGroup)`

```js
ItemFilter.equipmentSlotGroup("armor")
```

## hasEnchantment

用于检查给定的物品是否与附魔相匹配。

语法：

- `ItemFilter.hasEnchantment(filter)`
- `ItemFilter.hasEnchantment(filter, levelRange: Range)` ,关于 Range

::: code-group

```js [仅匹配附魔]
ItemFilter.hasEnchantment("minecraft:fortune")

```

```js [通过模组匹配]
// 匹配所有来自 `minecraft` 的附魔
ItemFilter.hasEnchantment("@minecraft")
```

```js [通过一个数组中有的附魔匹配]
// 匹配数组中有的所有附魔
ItemFilter.hasEnchantment(["minecraft:fortune", "minecraft:mending"])
```

```js [通过附魔等级匹配]
// 匹配2-3级的耐久附魔
ItemFilter.hasEnchantment("minecraft:unbreaking", [2, 3])
```

:::

## hasStoredEnchantment

用于检查给定的附魔书具有的附魔相匹配。

语法：

- `ItemFilter.hasStoredEnchantment(filter)`
- `ItemFilter.hasStoredEnchantment(filter, levelBound: Range)` 关于 Range

::: code-group

```js [仅匹配附魔]
ItemFilter.hasStoredEnchantment("minecraft:fortune")

```

```js [通过模组匹配]
// 匹配所有来自 `minecraft` 的附魔
ItemFilter.hasStoredEnchantment("@minecraft")
```

```js [通过一个数组中有的附魔匹配]
// 匹配数组中有的所有附魔
ItemFilter.hasStoredEnchantment(["minecraft:fortune", "minecraft:mending"])
```

```js [通过附魔等级匹配]
// 匹配2-3级的耐久附魔
ItemFilter.hasStoredEnchantment("minecraft:unbreaking", [2, 3])
```

:::

## item

如果物品匹配则匹配。这不能检测物品的数量，但可以同时检测组件。

- `ItemFilter.item(item: ItemStack | string)`
- `ItemFilter.item(item: ItemStack | string, matchComponents: boolean)`

## not/negate

反转过滤器。如果过滤器不匹配则匹配。

语法：

- `ItemFilter.not(filter: ItemFilter)`
- `anyFilter.negate()` 这里的 `anyFilter` 不是指有该过滤器，而是本教程提到的过滤器。

```js
ItemFilter.not(ItemFilter.hasEnchantment("minecraft:fortune"))

ItemFilter.hasEnchantment("minecraft:fortune").negate()
```

## tag

如果物品具有特定标签，则匹配。

语法：

- `ItemFilter.tag(tag: string)`

## toolAction

NeoForge 添加了一个名为 ToolAction 东西，它可以用来确定一个物品是否可以执行特定的动作。一些模组将其用于多功能工具。

如果所有动作都存在，则匹配。

语法：

- `ItemFilter.toolAction(...action)`
- `ItemFilter.anyToolAction(...action)`

::: code-group

```js [仅匹配一个动作]
ItemFilter.toolAction("pickaxe_dig")

ItemFilter.anyToolAction("pickaxe_dig")
```

```js [匹配多个动作]
ItemFilter.toolAction("pickaxe_dig", "shovel_dig")

ItemFilter.anyToolAction("pickaxe_dig", "shovel_dig")
```

:::

## 组合匹配

### allOf

将多个物品过滤器合并为一个。如果所有过滤器都匹配则匹配。

语法：

- `ItemFilter.allOf(...filters: ItemFilter[])`

```js
ItemFilter.allOf(
    ItemFilter.hasEnchantment("minecraft:fortune"), 
    ItemFilter.equipmentSlotGroup("hand")
)
```

### anyOf

将多个物品过滤器合并为一个。如果至少有一个过滤器匹配则匹配。

语法：

- `ItemFilter.anyOf(...filters: ItemFilter[])`

```js
ItemFilter.anyOf(
    ItemFilter.hasEnchantment("minecraft:silk_touch"),
    ItemFilter.equipmentSlotGroup("armor")
)
```

### all

一个返回匹配所有物品的过滤器。

语法：

- `ItemFilter.ALL`

### none

一个返回不匹配所有物品的过滤器。

语法：

- `ItemFilter.NONE`
