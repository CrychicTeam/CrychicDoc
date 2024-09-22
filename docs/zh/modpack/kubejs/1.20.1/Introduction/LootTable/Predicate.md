# 谓词

## 概述

- 释义：用于判断对象或参数的一组条件，或者说是对一类特征的抽象描述，例如：“他穿了衣服”，谓词：“穿了衣服”。

- 作用：常作为战利品的掉落与物品修饰器的条件。

::: warning 注意

- 一些谓词类型并没有被KubeJS提供原生支持，需写为Json文本格式作为addCondition(...Json)函数的参数传递。

- 在不被KubeJS原生支持的谓词中会给出可参考链接来协助使用谓词。

:::

## 谓词类型

### 全部

- 作用：评估一系列战利品表谓词，若它们都通过检查，则评估通过。可从任何上下文调用。

- 语句：addFunction(...Json);

::: details “全部”谓词参考
[minecraft-wiki/谓词#all_of](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#all_of)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:all_of",// [!code ++]
            "terms": []// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:all_of",
    // 数组 包含其他谓词
    "terms": []
}
```

:::

### 任何

- 作用：评估一系列战利品表谓词，若其中任意一个通过检查，则评估通过。可从任何上下文调用。

- 语句：addFunction(...Json);

::: details “任何”谓词参考
[minecraft-wiki/谓词#any_of](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#any_of)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:any_of",// [!code ++]
            "terms": []// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:any_of",
    // 数组 包含其他谓词
    "terms": []
}
```

:::

### 方块状态属性

- 作用：检查方块以及其方块状态。需要战利品上下文提供的方块状态进行检测，若未提供则总是不通过。

- 语句：addCondition(...Json);

::: details 方块状态属性谓词参考
[minecraft-wiki/谓词#block_state_property](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#block_state_property)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        // 因为properties是一个可选项，因此可以在不需要的时候不写
        loot.addCondition({// [!code ++]
            "condition": "minecraft:block_state_property",// [!code ++]
            "block": "minecraft:acacia_button",// [!code ++]
        })// [!code ++]
    })
})
```

```json
{
    "condition": "minecraft:block_state_property",
    // 检查方块id
    "block": "minecraft:acacia_button",
    // 【可选】检查方块属性 可以添加多个 务必确保该方块确实有这种属性
    "properties": {
        // 检查face属性是不是floor
        "face": "floor",
        // 以数字为值的属性可以这样表示，请注意在本示例中的方块并不包含此属性，仅用于演示
        "age":{
            "min": 1,
            "max": 7
        }
    }
}
```

:::

### 伤害来源属性

- 作用：检查伤害来源的属性。需要战利品上下文提供的来源和伤害来源进行检测，若未提供则总是不通过。

- 语句：addCondition(...Json);

::: details 害来源属性谓词参考
[minecraft-wiki/谓词#damage_source_properties](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#damage_source_properties)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:damage_source_properties",// [!code ++]
            "predicate": {// [!code ++]
                 // 【可选】检查实际伤害源标签
                "tags": [// [!code ++]
                    {// [!code ++]
                        // 伤害类型标签id
                        "id": "minecraft:always_hurts_ender_dragons",// [!code ++]
                        // 检查伤害类型是否应该含有此标签
                        "expected": true// [!code ++]
                    }// [!code ++]
                ]// [!code ++]
            }// [!code ++]
        })// [!code ++]
    })
})
```

```json
{
    "condition": "minecraft:damage_source_properties",
    "predicate": {
         // 【可选】检查实际伤害源标签
        "tags": [
            {
                // 伤害类型标签id
                "id": "minecraft:always_hurts_ender_dragons",
                // 检查伤害类型是否应该含有此标签
                "expected": false
            }
        ],
        // 【可选】检查实际伤害源实体-实体属性谓词
        "source_entity": {},
        // 【可选】检查直接伤害源实体-实体属性谓词
        "direct_entity": {}
    }
}
```

:::

### 实体属性

- 作用：检查战利品表上下文中的实体。可从任何上下文调用。

- 语句：entityProperties(战利品表上下文实体\: [Internal.LootContext$EntityTarget_](../Addon/ProbeJS/ProbeJSClassFlie.md#lootcontextentitytarget_), 实体属性Json);

::: details 实体属性谓词参考
[minecraft-wiki/谓词#entity_properties](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#entity_properties)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
// 并没有包含全部可用键，请参考wiki与数据包生成器
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.entityProperties('this', {// [!code ++]
            "type": "minecraft:player"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
// 并没有包含全部可用键，请参考wiki与数据包生成器
{
    "type": "minecraft:player"
}
```

:::

### 实体分数

- 作用：检查实体的记分板分数。

- 语句：entityScores(战利品表上下文实体\: [Internal.LootContext$EntityTarget_](../Addon/ProbeJS/ProbeJSClassFlie.md#lootcontextentitytarget_), 键值对{记分板id, 分数\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md)});

::: details 实体分数谓词参考
[minecraft-wiki/谓词#entity_scores](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#entity_scores)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.entityScores('this', { 'test': 1 })// [!code ++]
    })
})
```

:::

### 取反

- 作用：定义一个谓词列表，当内含谓词不通过时该谓词通过。

- 语句：addCondition(...Json);

::: details 取反谓词参考
[minecraft-wiki/谓词#inverted](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#inverted)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:inverted",// [!code ++]
            "term": {}// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:inverted",
    "term": {} // 其他谓词
}
```

:::

### 被玩家击杀

- 作用：检查实体是否死于玩家击杀(死时被玩家攻击过)。

- 语句：killedByPlayer();

::: details 被玩家击杀谓词参考
[minecraft-wiki/谓词#killed_by_player](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#killed_by_player)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.killedByPlayer()// [!code ++]
    })
})
```

:::

### 检查位置

- 作用：检查当前位置。需要战利品上下文提供的来源进行检测，若未提供则总是不通过。

- 语句：addCondition(...Json);

::: details 位置信息谓词参考
[minecraft-wiki/谓词#location_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#location_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:location_check",// [!code ++]
            "predicate": {// [!code ++]
                "biome": "minecraft:badlands"// [!code ++]
            }// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
// 并未列出全部可用键，请查看参考
{
    "condition": "minecraft:location_check",
    "predicate": {
        "biome": "minecraft:badlands"
    }
}

```

:::

### 匹配工具

- 作用：检查工具。需要战利品上下文提供的工具进行检测，若未提供则总是不通过。

- 语句：addCondition(...Json);

::: details 检查工具谓词参考
[minecraft-wiki/谓词#match_tool](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#match_tool)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:match_tool",// [!code ++]
            "predicate": {// [!code ++]
                "tag": "minecraft:axes"// [!code ++]
            }// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:match_tool",
    "predicate": {
        "tag": "minecraft:axes"
    }
}
```

:::

### 随机概率

- 作用：生成一个取值范围为0.0–1.0之间的随机数，并检查其是否小于指定值。可从任何上下文调用。

- 语句：randomChance(概率\[0, 1\]);

::: details 随机概率谓词参考
[minecraft-wiki/谓词#random_chance](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#random_chance)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.randomChance(0.5)// [!code ++]
    })
})
```

:::

### 受抢夺附魔影响的随机概率

- 作用：检查随机概率，这个概率会受到抢夺魔咒的等级影响。

- 语句：randomChanceWithLooting(概率\[0, 1\], 每级抢夺增加概率\[0, 1\]);

::: details 受抢夺附魔影响的随机概率谓词参考
[minecraft-wiki/谓词#random_chance_with_enchanted_bonus](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#random_chance_with_enchanted_bonus)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
// 基础概率，每一级抢夺增加的概率
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.randomChanceWithLooting(0.1, 0.2)// [!code ++]
    })
})
```

:::

### 引用谓词文件

- 调用谓词文件并返回其结果。可从任何上下文调用。

- 语句：addCondition(...Json);

::: details 引用谓词谓词参考
[minecraft-wiki/谓词#reference](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#reference)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:reference",// [!code ++]
            "name": "minecraft:test"// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
  "condition": "minecraft:reference",
  "name": "minecraft:test" // 引用谓词ResourceLocation
}

```

:::

### 未被爆炸破坏

- 作用：返回成功概率为1 / 爆炸半径，如果上下文未传递爆炸则始终通过。

- 语句：survivesExplosion();

::: details 未被爆炸破坏谓词参考
[minecraft-wiki/谓词#survives_explosion](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#survives_explosion)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.survivesExplosion()// [!code ++]
    })
})
```

:::

### 附魔奖励

- 作用：以魔咒等级为索引，从列表中挑选概率通过。需要战利品上下文提供的工具进行检测，如果未提供，则附魔等级被视为 0。

- 语句：addCondition(...Json);

::: details 附魔奖励谓词参考
[minecraft-wiki/谓词#reference](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#reference)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:table_bonus",// [!code ++]
            "enchantment": "minecraft:aqua_affinity", // [!code ++]
            "chances": [ // [!code ++]
                0.1,// [!code ++]
                0.2,// [!code ++]
                0.3,// [!code ++]
                0.4// [!code ++]
            ]// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:table_bonus",
    "enchantment": "minecraft:aqua_affinity", //附魔id
    "chances": [ //每一级对应的概率 从0开始
        0.1,
        0.2,
        0.3,
        0.4
    ]
}

```

:::

### 检查时间

- 作用：将当前的游戏时间（更确切地来说，为24000 * 天数 + 当天时间）和给定值进行比较。可从任何上下文调用。

- 语句：addCondition(...Json);

::: details 检查时间谓词参考
[minecraft-wiki/谓词#time_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#time_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:time_check",// [!code ++]
            "value": {// [!code ++]
                "min": 0,// [!code ++]
                "max": 12000// [!code ++]
            },// [!code ++]
            "period": 24000// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:time_check",
    "value": {
        "min": 0,
        "max": 12000
    },
    "period": 24000
}
```

:::

### 检查值

- 作用：将一个数与另一个数或范围进行比较。可从任何上下文调用。

- 语句：addCondition(...Json);

::: details 检查值谓词参考
[minecraft-wiki/谓词#value_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#value_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:value_check",// [!code ++]
            "value": {// [!code ++]
                "min": 0,// [!code ++]
                "max": 12000// [!code ++]
            },// [!code ++]
            "range": {// [!code ++]
                "min": 0,// [!code ++]
                "max": 6000// [!code ++]
            }// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:value_check",
    "value": {
        "min": 0,
        "max": 12000
    },
    "range": {
        "min": 0,
        "max": 6000
    }
}
```

:::

### 检查天气

- 检查当前游戏的天气状态。可从任何上下文调用。

- 语句：addCondition(...Json);

::: details 检查天气谓词参考
[minecraft-wiki/谓词#weather_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#weather_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:weather_check",// [!code ++]
            "raining": true,// [!code ++]
            "thundering": true// [!code ++]
        })// [!code ++]
    })
})
```

```json [Json文本]
{
    "condition": "minecraft:weather_check",
    "raining": true,
    "thundering": true
}
```

:::
