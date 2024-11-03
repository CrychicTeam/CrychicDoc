# Create a loot table

LootJS支持我们创建自定义的战利品表。

## 创建战利品表

创建一个只有一个奖池的战利品表相当的简单

```js
LootJS.lootTables(event => {
    event.create("lootjs:rare_equipment").createPool(pool => {
        // ...
    })
})
```

## 第一个奖池

### 第一个物品

假如我们想创建一个[例子](https://misode.github.io/loot-table/?version=1.21&preset=chests/end_city_treasure)中的战利品表中的一个条目，就像下面的 `json` 一样：

::: code-group

```json [json例子]
{
    "type": "minecraft:item",
    "name": "minecraft:diamond_leggings",
    "weight": 3,
    "functions": [
        {
            "function": "minecraft:enchant_with_levels",
            "levels": {
                "type": "minecraft:uniform",
                "min": 20,
                "max": 39
            },
            "options": "#minecraft:on_random_loot"
        }
    ]
}
```

```js [参照json的js实现]
LootJS.lootTables(event => {
    event.create("lootjs:rare_equipment").createPool(pool => {
        // 默认情况下，`enchantWithLevels` 将始终为 `#minecraft:on_random_loot`
        pool.addEntry(
            LootEntry.of("minecraft:diamond_leggings")
                     .withWeight(3)
                     .enchantWithLevels([20, 39])
        )
    })
})
```

:::

### 更多的物品

我们可以向第一个奖励池添加更多的物品。

```js
LootJS.lootTables(event => {
    event.create("lootjs:rare_equipment").createPool(pool => {
        pool.addEntry(
            LootEntry.of("minecraft:diamond_leggings")
                     .withWeight(3)
                     .enchantWithLevels([20, 39])
        )

        pool.addEntry(
            LootEntry.of("minecraft:iron_pickaxe")
                     .withWeight(10)
                     .enchantWithLevels([10, 19])
        )

        pool.addEntry(
            LootEntry.of("minecraft:diamond_sword")
                     .withWeight(5)
                     .enchantWithLevels([30, 50])
                     .damage([0.3, 0.5])
        )

        pool.addEntry("minecraft:diamond_horse_armor")

        pool.addEntry(LootEntry.of("minecraft:diamond").setCount([2, 5]))
    })
})
```

## 第二个奖励池

LootJS也允许我们创建第二个奖励池。例如，我们可以在第二个奖励池中添加一把带附魔的剑。

```js
LootJS.lootTables(event => {
    event
        .create("lootjs:rare_equipment")
        .createPool(pool => {
            // 之前的第一奖励池的代码，就不重复写了
        })
        .createPool(pool => {
            pool.addEntry(
                LootEntry.of("minecraft:netherite_sword").enchant(builder => {
                    builder.withEnchantment("minecraft:sharpness", [4, 5])
                    builder.withEnchantment("minecraft:unbreaking", 3)
                    builder.withEnchantment("minecraft:knockback", 2)
                    builder.withEnchantment("minecraft:mending", 1)
                })
            )

            // Minecraft 原版通常使用一个带权重的空条目来代替 `randomChance`。
            // 在这个教程中，我们将做同样的事情。
            pool.addEntry(LootEntry.empty().withWeight(20))
        })
})
```

## 将表添加到其他表中

LootJS现在支持将整个表添加到其他表中。

```js
LootJS.lootTables(event => {
    event.getLootTable("minecraft:gameplay/fishing") 
         .firstPool(pool => {
            pool.addEntry(LootEntry.reference("lootjs:rare_equipment")
//                                                      ⬆️
//                                           这里指的是刚才我们创建的战利品表
                .randomChance(0.1))
    })
})
```
