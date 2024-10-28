# Modify your first loot table

:::v-info

该篇教程源自于 LootJs 官方网站，

适用于 Minecraft 1.21 及更高版本

:::

让我们开始修改你的第一个掉落表。LootJS 提供了不同的函数供你使用，你可以添加、删除，甚至只是改变掉落表中物品的权重。在开始之前，请确保你具备关于掉落表基本知识，可以查看[我的世界维基](https://zh.minecraft.wiki/w/loot_table)来了解相关知识。

我们以 `minecraft:chests/desert_pyramide` 为例子。点击[这里](https://misode.github.io/loot-table/?preset=chests/desert_pyramid)了解该战利品表的结构。

## 添加物品

我们使用`addEntry()`来向其中添加一个苹果。请注意我们通常使用`firstPool()`来获取战利品表的第一个奖池。

我们可以使用 `LootEntry API` 使得苹果具有特定的权重或者数量等。

::: code-group

```js [仅添加一个苹果]
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/desert_pyramid")
         .firstPool().addEntry("minecraft:apple")
})
```

```js [添加带有特定权重的苹果]
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/desert_pyramid")
        .firstPool()
        .addEntry(LootEntry.of("minecraft:apple").withWeight(20))
})
```

```js [添加带有数量的苹果]
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/desert_pyramid")
        .firstPool()
        .addEntry(LootEntry.of("minecraft:apple").withWeight(20).setCount([2, 5]))
})
```

:::

## 修改物品

LootJS 也同样支持修改战利品表中的一个现有的物品。在我们讨论的例子中，你可以看到钻石的权重是5，我们可以将其改为1，使得钻石的滚动概率更低。

```js
LootJS.lootTables(event => {
    event
        .getLootTable("minecraft:chests/desert_pyramid")
        .firstPool()
        .modifyItemEntry(itemEntry => {
            if (itemEntry.item.id === "minecraft:diamond") {
                itemEntry.setWeight(1)
            }

            return itemEntry
        })
})
```

你可以在上面的代码示例中看到，我们再次返回了 `itemEntry`,这是因为 modify 操作必须要有一个结果。

## 删除物品

同样的，LootJs 也支持删除战利品表中的条目。

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:chests/desert_pyramid")
         .firstPool()
         .removeItem("minecraft:bone")
})
```
