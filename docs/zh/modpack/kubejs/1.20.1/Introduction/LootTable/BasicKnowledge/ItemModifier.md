# 物品修饰器

## 概述

- 作用：用于对物品施加单个或多个操作，例如使空地图变为指向某个标签中结构的寻宝地图。

::: warning 注意

- 一些物品修饰器类型并没有被KubeJS提供原生支持，需写为Json文本格式作为addFunction(...Json)函数的参数传递，因此在不被KubeJS原生支持的谓词中会给出可参考链接与数据包生成器来协助使用物品修饰器。

:::

## 物品修饰器类型

### 应用奖励公式

- 作用：将预定义的奖励公式应用于物品栈的计数。

- 语句：addFunction(...Json)

::: details 应用奖励公式物品修饰器参考
[minecraft-wiki/物品修饰器#apply_bonus](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#apply_bonus)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:apply_bonus",// [!code ++]
            "enchantment": "minecraft:looting",// [!code ++]
            "formula": "minecraft:uniform_bonus_count",// [!code ++]
            "parameters": {// [!code ++]
              "bonusMultiplier": 1// [!code ++]
            }// [!code ++]
          })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:apply_bonus",
    "enchantment": "minecraft:looting",
    "formula": "minecraft:uniform_bonus_count",
    "parameters": {
        "bonusMultiplier": 1
    }
}
```

:::

### 复制实体显示名

- 作用：将实体或方块实体的显示名复制到物品栈NBT中。

- 语句：copyName(战利品表上下文实体\: [Internal.CopyNameFunction$NameSource_](../Addon/ProbeJS/ProbeJSClassFlie.md#lootcontextentitytarget_));

::: details 复制实体显示名物品修饰器参考
[minecraft-wiki/物品修饰器#copy_name](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#copy_name)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.copyName("this")// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 复制NBT

- 作用：从指定类型的数据源复制NBT到物品栈。

- 语句：addFunction(...Json);

::: details 复制NBT物品修饰器参考
[minecraft-wiki/物品修饰器#copy_nbt](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#copy_nbt)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:copy_nbt",// [!code ++]
            "source": "block_entity",// [!code ++]
            "ops": [// [!code ++]
                {// [!code ++]
                    "source": "LootTable",// [!code ++]
                    "target": "LootTable",// [!code ++]
                    "op": "merge"// [!code ++]
                }// [!code ++]
            ]// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:copy_nbt",
    "source": "block_entity",
    "ops": [
        {
            "source": "LootTable",
            "target": "LootTable",
            "op": "merge"
        }
    ]
}
```

:::

### 复制方块状态

- 作用：当物品是由方块产生时，复制方块的方块状态到物品的block_state；否则此物品修饰器不做任何处理。

- 语句：addFunction(...Json);

::: details 复制方块状态物品修饰器参考
[minecraft-wiki/物品修饰器#copy_state](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#copy_state)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:copy_state",// [!code ++]
            "block": "minecraft:chest",// [!code ++]
            "properties": [// [!code ++]
                "type"// [!code ++]
            ]// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:copy_state",
    "block": "minecraft:chest",
    "properties": [
        "type"
    ]
}
```

:::

### 随机附魔

- 作用：为物品附上一个随机的魔咒。魔咒的等级也是随机的。

- 语句：enchantRandomly(附魔id数组\: ResourceLocation\[\]);

::: details 随机附魔物品修饰器参考
[minecraft-wiki/物品修饰器#enchant_randomly](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#enchant_randomly)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.enchantRandomly([
            "minecraft:aqua_affinity",
            "minecraft:bane_of_arthropods"
        ]);
    })
})
```

```json [Json文本]

```

:::

### 给予等价于经验等级的随机魔咒

- 作用：使用指定的魔咒等级附魔物品（大约等效于使用这个等级的附魔台附魔物品）。

- 语句：enchantWithLevels(附魔等级\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md), 是否包含宝藏附魔\: Boolean);

::: details 给予等价于经验等级的随机魔咒物品修饰器参考
[minecraft-wiki/物品修饰器#enchant_with_levels](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#enchant_with_levels)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.enchantWithLevels({ min: 1, max: 5 }, true)// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置探险家地图

- 作用：将普通的地图物品变为一个指引到某个结构标签的探险家地图。如果物品不是地图，则不做任何处理。

- 语句：addFunction(...Json);

::: details 设置探险家地图物品修饰器参考
[minecraft-wiki/物品修饰器#exploration_map](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#exploration_map)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:exploration_map",// [!code ++]
            "destination": "minecraft:village",// [!code ++]
            "decoration": "target_x"// [!code ++]
          })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:exploration_map",
    "destination": "minecraft:village",
    "decoration": "target_x"
}
```

:::

### 爆炸损耗

- 作用：如果物品是因为方块被爆炸破坏而产生，执行该函数的每个物品有1/爆炸半径的概率消失，堆叠的物品会被分为多个单独的物品计算；否则此物品修饰器不做任何处理。

- 语句：addFunction(...Json);

::: details 爆炸损耗物品修饰器参考
[minecraft-wiki/物品修饰器#exploration_map](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#explosion_decay)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:explosion_decay"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:explosion_decay"
}
```

:::

### 填充玩家头颅

- 作用：将玩家头颅设置为指定玩家的头颅。如果物品不是玩家头颅则不做任何处理。

- 语句：addFunction(...Json);

::: details 填充玩家头颅物品修饰器参考
[minecraft-wiki/物品修饰器#fill_player_head](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#fill_player_head)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:fill_player_head",// [!code ++]
            "entity": "this"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:fill_player_head",
    "entity": "this"
}

```

:::

### 熔炉熔炼

- 作用：将物品转变为用熔炉烧炼后的对应物品。如果物品不可烧炼，则不做任何处理。

- 语句：furnaceSmelt();

::: details 熔炉熔炼物品修饰器参考
[minecraft-wiki/物品修饰器#exploration_map](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#furnace_smelt)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.furnaceSmelt()// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 限制物品栈数量

作用：限制物品数量。

- 语句：addFunction(...Json);

::: details 限制物品栈数量物品修饰器参考
[minecraft-wiki/物品修饰器#limit_count](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#limit_count)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:limit_count",// [!code ++]
            "limit": {// [!code ++]
                "min": 1,// [!code ++]
                "max": 4// [!code ++]
            }// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:limit_count",
    "limit": {
        "min": 1,
        "max": 4
    }
}
```

:::

### 设置内容物

- 作用：对物品的内容物进行处理。

- 语句：addFunction(...Json);

::: details 设置内容物物品修饰器参考
[minecraft-wiki/物品修饰器#set_contents](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_contents)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_contents",// [!code ++]
            "type": "minecraft:chest",// [!code ++]
            "entries": [// [!code ++]
                {// [!code ++]
                    "type": "minecraft:item",// [!code ++]
                    "name": "minecraft:stone"// [!code ++]
                }// [!code ++]
            ]// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_contents",
    "type": "minecraft:chest",
    "entries": [
        {
            "type": "minecraft:item",
            "name": "minecraft:stone"
        }
    ]
}
```

:::

### 引用物品修饰器文件

- 作用：引用另一个物品修饰器。

- 语句：addFunction(...Json);

::: details 引用物品修饰器文件物品修饰器参考
[minecraft-wiki/物品修饰器#reference](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#reference)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:reference",// [!code ++]
            "name": "minecraft:demo"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:reference",
    "name": "minecraft:demo"
}
```

:::

### 设置属性

- 作用：为物品加上属性修饰符。

- 语句：addFunction(...Json);

::: details 设置属性物品修饰器参考
[minecraft-wiki/物品修饰器#set_attributes](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_attributes)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_attributes",// [!code ++]
            "modifiers": [// [!code ++]
                {// [!code ++]
                    "attribute": "minecraft:generic.max_health",// [!code ++]
                    "name": "kubejs",// [!code ++]
                    "amount": 1,// [!code ++]
                    "operation": "addition",// [!code ++]
                    "slot": "mainhand"// [!code ++]
                }// [!code ++]
            ]// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_attributes",
    "modifiers": [
        {
            "attribute": "minecraft:generic.max_health",
            "name": "kubejs",
            "amount": 1,
            "operation": "addition",
            "slot": "mainhand"
        }
    ]
}

```

:::

### 设置旗帜图案

- 作用：设置旗帜物品的图案。如果物品不是旗帜，则此修饰器不做任何处理。

- 语句：addFunction(...Json);

::: details 设置属性物品修饰器参考
[minecraft-wiki/物品修饰器#set_banner_pattern](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_banner_pattern)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_banner_pattern",// [!code ++]
            "patterns": [// [!code ++]
                {// [!code ++]
                    "pattern": "minecraft:base",// [!code ++]
                    "color": "white"// [!code ++]
                }// [!code ++]
            ],// [!code ++]
            "append": true// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_banner_pattern",
    "patterns": [
        {
            "pattern": "minecraft:base",
            "color": "white"
        }
    ],
    "append": true
}
```

:::

### 根据抢夺魔咒调整物品数量

- 作用：决定了抢夺魔咒对该物品数量的影响。如果未使用，抢夺魔咒将对该物品没有效果。

- 语句：lootingEnchant(每级抢夺增加的物品掉落数量\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md), 最大掉落数量\: Number);

::: details 根据抢夺魔咒调整物品数量物品修饰器参考
[minecraft-wiki/物品修饰器#looting_enchant](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#looting_enchant)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.lootingEnchant({ min: 1, max: 5 }, 3)// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置物品数量

- 作用：设置该物品的数量。

- 语句：count(数量\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md));

::: details 设置物品数量物品修饰器参考
[minecraft-wiki/物品修饰器#set_count](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_count)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.count({ min: 1, max: 5 })// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置损伤值

- 作用：设置工具的损坏值。

- 语句：damage(损伤值\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md));

::: details 设置损伤值物品修饰器参考
[minecraft-wiki/物品修饰器#set_damage](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_damage)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.damage({ min: 1, max: 5 })// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置魔咒

- 作用：设置物品的魔咒。

- 语句：addFunction(...Json);

::: details 设置魔咒物品修饰器参考
[minecraft-wiki/物品修饰器#set_enchantments](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_enchantments)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_enchantments",// [!code ++]
            "enchantments": {// [!code ++]
                "minecraft:aqua_affinity": 0// [!code ++]
            },// [!code ++]
            "add": true// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_enchantments",
    "enchantments": {
        "minecraft:aqua_affinity": 0
    },
    "add": true
}
```

:::

### 设置乐器

- 作用：设置山羊角的种类。如果物品不是山羊角则不做任何处理。

- 语句：addFunction(...Json);

::: details 设置乐器物品修饰器参考
[minecraft-wiki/物品修饰器#set_instrument](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_instrument)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_instrument",// [!code ++]
            "options": "#minecraft:screaming_goat_horns"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_instrument",
    "options": "#minecraft:screaming_goat_horns"
}
```

:::

### 设置战利品表

- 作用：设置放置和打开容器方块时的战利品表。

- 语句：lootTable(战利品表id\: ResourceLocation, \[可选\]战利品表种子\: Number);

::: details 设置战利品表物品修饰器参考
[minecraft-wiki/物品修饰器#set_instrument](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_loot_table)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.lootTable('minecraft:archaeology/desert_pyramid', 233)// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置物品描述

- 作用：为物品添加描述信息。

- 语句：addFunction(...Json);

::: details 设置物品描述物品修饰器参考
[minecraft-wiki/物品修饰器#set_lore](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_lore)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_lore",// [!code ++]
            "entity": "this",// [!code ++]
            "lore": [// [!code ++]
                {// [!code ++]
                    "text": "物品说明"// [!code ++]
                }// [!code ++]
            ],// [!code ++]
            "replace": false// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_lore",
    "entity": "this",
    "lore": [
        {
            "text": "物品说明"
        }
    ],
    "replace": false
}
```

:::

### 设置物品名

- 作用：添加或修改物品的自定义名称。

- 语句：addFunction(...Json);

::: details 设置物品名物品修饰器参考
[minecraft-wiki/物品修饰器#set_name](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_name)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_name",// [!code ++]
            "entity": "this",// [!code ++]
            "name": "物品名"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_name",
    "entity": "this",
    "name": "物品名"
}
```

:::

### 设置NBT

- 作用：设置物品NBT数据。

- 语句：addFunction(...Json);

::: details 设置乐器物品修饰器参考
注意：wiki只有最新版本Minecraft的资料，因此现在关于nbt一律成为了组件components，不要完全照搬wiki的内容，仅供参考。

[minecraft-wiki/物品修饰器#set_components](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_components)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_nbt",// [!code ++]
            "tag": "{KubeJS:\"test\"}"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_nbt",
    "tag": "{KubeJS:\"test\"}"
}
```

:::

### 设置药水

- 作用：设置物品包含的药水效果标签。

- 语句：addFunction(...Json);

::: details 设置药水物品修饰器参考
[minecraft-wiki/物品修饰器#set_potion](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_potion)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_potion",// [!code ++]
            "id": "minecraft:strong_strength"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_potion",
    "id": "minecraft:strong_strength"
}

```

:::

### 设置迷之炖菜状态效果

- 作用：为谜之炖菜添加状态效果，如果不是迷之炖菜则失败。

- 语句：addFunction(...Json);

::: details 设置迷之炖菜状态效果物品修饰器参考
[minecraft-wiki/物品修饰器#set_stew_effect](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#set_stew_effect)

[数据包生成器/物品修饰器](https://misode.github.io/item-modifier/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addFunction({// [!code ++]
            "function": "minecraft:set_stew_effect",// [!code ++]
            "effects": [// [!code ++]
                {// [!code ++]
                    "type": "minecraft:absorption",// [!code ++]
                    "duration": 200// [!code ++]
                }// [!code ++]
            ]// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "function": "minecraft:set_stew_effect",
    "effects": [
        {
            "type": "minecraft:absorption",
            "duration": 200
        }
    ]
}
```

:::

## 有条件的物品修饰器(物品条件函数)

- 作用：使一些物品修饰器有条件的应用。

- 语句：addConditionalFunction(回调函数(物品条件函数\: [Internal.ConditionalFunction](../Addon/ProbeJS/ProbeJSClassFlie.md#conditionalfunction)));

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addConditionalFunction(cf => {// [!code ++]
            // 添加谓词，还可以通过链式调用继续添加
            cf.survivesExplosion()// [!code ++]
            // 添加物品修饰器，还可以通过链式调用继续添加
            cf.name(Component.red('爆炸残余物'))// [!code ++]
        })// [!code ++]
    })
})
```

:::
