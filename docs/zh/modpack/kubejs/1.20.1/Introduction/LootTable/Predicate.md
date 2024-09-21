# 谓词

## 概述

- 释义：用于判断对象或参数的一组条件，或者说是对一类特征的抽象描述；例如：“他穿了衣服”，谓词：“某某穿了衣服”。

- 作用：常作为战利品的掉落与物品修饰器的应用条件，不过由于KubeJS并没有原生支持全部的谓词，因此绝大部分都需要写Json作为参数传入。

## 技巧

- 谓词并不被KubeJS完全原生支持，部分谓词需以Json对象表示作为addCondition()的参数传递，因此建议在[minecraft-wiki/谓词](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D)了解某个谓词的作用后使用[数据包生成器#谓词](https://misode.github.io/predicate/)来快速生成谓词Json文本。

## 谓词类型

### 全部

- 作用：定义谓词列表，当数组内全部谓词均通过时此谓词通过。

- 语句：addCondition(\{"condition": "minecraft:all_of", "terms": []\});

::: code-group

```json [Json文本]
{
    "condition": "minecraft:all_of",
    // 数组 包含其他谓词
    "terms": []
}
```

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

:::

### 任何

- 作用：定义谓词列表，当数组内任一谓词通过时此谓词通过。

- 语句：addCondition(\{"condition": "minecraft:any_of", "terms": []\});

::: code-group

```json [Json文本]
{
    "condition": "minecraft:any_of",
    // 数组 包含其他谓词
    "terms": []
}
```

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

:::

### 方块状态属性

- 作用：将检查战利品上下文中方块的属性。

- 语句：addCondition(...Json);

::: code-group

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

:::

### 伤害来源属性

- 作用：检查伤害来源的属性。

- 语句：addCondition(...Json);

::: code-group

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

:::

### 实体属性

- 作用：检查战利品表上下文中的实体。

- 实体属性可参考：
- [minecraft-wiki/谓词#entity_properties](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#entity_properties)
- [数据包生成器/谓词](https://misode.github.io/predicate/)

- 语句：entityProperties(战利品表上下文实体\: [Internal.LootContext$EntityTarget_](../Addon/ProbeJS/ProbeJSClassFlie.md#lootcontextentitytarget_), 实体属性Json);

::: code-group

```json [Json文本]
// 并没有包含全部可用键，请参考wiki与数据包生成器
{
    "type": "minecraft:player"
}
```

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

<!-- ```json
{
    "condition": "minecraft:entity_properties",
    // 判断的实体：this，killer，direct_killer，killer_player
    // this：表示实体自身，即死亡的实体，或是破坏方块、打开容器或获取进度的玩家
    // killer：表示进行击杀的实体
    // direct_killer：表示进行直接击杀的实体
    // killer_player：表示进行击杀的玩家
    "entity": "this",
    // 要应用于实体的战利品表谓词
    "predicate": {
        // 【可选】检查实体到执行位置的距离
        "distance":{
            // 【可选】绝对距离 也可以直接写数字
            "absolute":{
                // 【可选】最小值
                "min": 0,
                // 【可选】最大值
                "max": 16
            },
            // 【可选】水平距离 也可以直接写数字
            "horizontal":{
                // 【可选】最小值
                "min": 0,
                // 【可选】最大值
                "max": 16
            },
            // 【可选】X轴上的距离 也可以直接写数字
            "x":{
                // 【可选】最小值
                "min": 0,
                // 【可选】最大值
                "max": 16
            },
            // 【可选】y轴上的距离 也可以直接写数字
            "y":{
                // 【可选】最小值
                "min": 0,
                // 【可选】最大值
                "max": 16
            },
            // 【可选】y轴上的距离 也可以直接写数字
            "z":{
                // 【可选】最小值
                "min": 0,
                // 【可选】最大值
                "max": 16
            }
        },
        // 【可选】状态效果列表
        "effects":{
            // 状态效果id
            "minecraft:night_vision": {
                // 【可选】状态效果等级 也可以直接写数字
                "amplifier": {
                    // 【可选】最小值
                    "min": 0,
                    // 【可选】最大值
                    "max": 2
                },
                // 【可选】状态效果持续时间（刻） 也可以直接写数字
                "duration": {
                    // 【可选】最小值
                    "min": 0,
                    // 【可选】最大值
                    "max": 2
                },
                // 【可选】是否为信标添加的状态效果
                "ambient": false,
                // 【可选】是否可见
                "visible": false
              },
              // 【可选】实体身上的装备
              "equipment":{
                // 【可选】胸部
                "chest":{物品谓词},
                // 【可选】脚部
                "feet":{物品谓词},
                // 【可选】头部
                "head":{物品谓词},
                // 【可选】腿部
                "legs":{物品谓词},
                // 【可选】主手
                "mainhand":{物品谓词},
                // 【可选】副手
                "offhand":{物品谓词}
              },
              // 【可选】检查实体特质
              "flags":{
                // 【可选】检查该实体是否为幼体
                "is_baby": true,
                // 【可选】检查该实体是否正在飞行
                "is_flying": true,
                // 【可选】检查该实体是否正在着火
                "is_on_fire": true,
                // 【可选】检查该实体是否立在地面上
                "is_on_ground": true,
                // 【可选】检查该实体是否正在潜行
                "is_sneaking": true,
                // 【可选】检查该实体是否正在疾跑
                "is_sprinting": true,
                // 【可选】检查该实体是否正在游泳
                "is_swimming": true,
              },
              // 【可选】检查实体的位置
              "location":{位置信息谓词},
              // 【可选】检查实体运动状况，单位：m/s
              "movement":{
                // 【可选】检查摔落高度是否在指定区间
                "fall_distance":{浮点数界限范围},
                // 【可选】检查水平速度分量是否在指定区间
                "horizontal_speed":{浮点数界限范围},
                // 【可选】检查速度是否在指定区间内
                "speed":{浮点数界限范围},
                // 【可选】检查垂直速度分量是否在指定区间
                "vertical_speed":{浮点数界限范围},
                // 【可选】检查X轴速度分量是否在指定区间
                "x":{浮点数界限范围},
                // 【可选】检查Y轴速度分量是否在指定区间
                "y":{浮点数界限范围},
                // 【可选】检查Z轴速度分量是否在指定区间
                "z":{浮点数界限范围},
              },
              // 【可选】检查影响实体移动速度的方块位置。此位置最低不超过实体位置0.5格以下。
              "movement_affected_by":{位置信息谓词},
              // 【可选】检查实体是否具有指定的NBT。
              "nbt":"",
              // 【可选】检查正在骑乘此实体的实体。
              "passenger":{实体谓词},
              // 【可选】(大于0）根据实体已经加载的时间，按照指定的周期，一个周期内只可能检查成功一次。
            "periodic_tick": 1,
            // 【可选】检查实体某些槽位内的物品
            "slots":{

            }
        }
    }
}
``` -->

:::

### 实体分数

- 作用：检查实体的记分板分数。

- 语句：entityScores(战利品表上下文实体\: [Internal.LootContext$EntityTarget_](../Addon/ProbeJS/ProbeJSClassFlie.md#lootcontextentitytarget_), 键值对{记分板id, 分数\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md)})

::: code-group

<!-- ```json
{
  "condition": "minecraft:entity_scores",
  // 要检查的实体。从战利品上下文指定实体。
  //设置成this表示实体自身，即死亡的实体，或是破坏方块、打开容器或获取进度的玩家
  // killer表示进行伤害的实体
  // direct_killer表示进行直接伤害的实体
  // killer_player表示进行伤害的玩家。
  "entity": "this",
  "scores": {
    // 记分板id
    "minecraft:test": { 
      "min": 0,
      "max": 2
    }
  }
}
``` -->

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

::: details 取反谓词参考
[minecraft-wiki/谓词#inverted](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#inverted)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

::: code-group

```json
{
    "condition": "minecraft:inverted",
    "term": {} // 其他谓词
}
```

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

:::

### 被玩家击杀

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：检查实体是否死于玩家击杀。

- 语句：killedByPlayer();

<!-- - 参考：[minecraft-wiki/谓词#killed_by_player](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#killed_by_player)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/) -->

<!-- - 类型： -->

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.killedByPlayer()// [!code ++]
    })
})
```

<!-- ```json

``` -->

:::

### 检查位置

- 作用：检查执行位置。

::: details 位置信息谓词参考
[minecraft-wiki/谓词#location_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#location_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

<!-- - 类型： -->

<!-- - 参考：[minecraft-wiki/谓词#location_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#location_check)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/) -->

::: code-group

```json [Json文本]
// 并未列出全部可用键，请查看参考
{
    "condition": "minecraft:location_check",
    "predicate": {
        "biome": "minecraft:badlands"
    }
}

```

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

:::

### 匹配工具

- 作用：检查战利品上下文工具。

::: details 检查工具谓词参考
[minecraft-wiki/谓词#match_tool](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#match_tool)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

::: code-group

```json [Json文本]
{
    "condition": "minecraft:match_tool",
    "predicate": {
        "tag": "minecraft:axes"
    }
}
```

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

:::

### 随机概率

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：检查随机概率，使用[0, 1]的数字表示。

- 语句：randomChance(基础概率\: number);

<!-- - 参考：[minecraft-wiki/谓词#random_chance](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#random_chance)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/) -->

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.randomChance(0.5)// [!code ++]
    })
})
```

<!-- ```json

``` -->

:::

### 受抢夺附魔影响的随机概率

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：检查随机概率，这个概率会受到抢夺魔咒的等级影响。

- 语句：randomChanceWithLooting(基础概率\: number, 每级增加\: number);

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

- 引用一个谓词文件，将其作为谓词。

- 语句：addCondition(...Json);

::: details 引用谓词谓词参考
[minecraft-wiki/谓词#reference](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#reference)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

::: code-group

```json [Json文本]
{
  "condition": "minecraft:reference",
  "name": "minecraft:test" // 引用谓词ResourceLocation
}

```

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

:::

### 未被爆炸破坏

- 作用：返回成功概率为1 / 爆炸半径，如果上下文未传递爆炸则始终通过。

<!-- ::: details 未被爆炸破坏参考
[minecraft-wiki/谓词#survives_explosion](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#survives_explosion)

[数据包生成器/谓词](https://misode.github.io/predicate/)
::: -->

- 语句：survivesExplosion()

<!-- - 语句：survivesExplosion()

- 示例：如果燧石被爆炸摧毁不会掉落火药。 -->

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

- 作用：定义一个包含概率（小数）的数组，检查魔咒id，根据该id魔咒等级确定使用哪一个数字进行概率判断。

::: details 附魔奖励谓词参考
[minecraft-wiki/谓词#reference](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#reference)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

::: code-group

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

:::

### 检查时间

- 作用：将当前的游戏时间（更确切地来说，为24000 * 天数 + 当天时间）和给定值进行比较。

::: details 检查时间谓词参考
[minecraft-wiki/谓词#time_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#time_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

::: code-group

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

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addCondition({// [!code ++]
            "condition": "minecraft:time_check",// [!code ++]
            "value": {// [!code ++]
                "min": 0,// [!code ++]
                "max": 12000
            },// [!code ++]
            "period": 24000
        })// [!code ++]
    })
})
```

:::

### 检查值

- 作用：将一个数与另一个数或范围进行比较。

::: details 检查值谓词参考
[minecraft-wiki/谓词#value_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#value_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

::: code-group

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

:::

### 检查天气

- 检查所在维度天气晴雨。

::: details 检查天气谓词参考
[minecraft-wiki/谓词#weather_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#weather_check)

[数据包生成器/谓词](https://misode.github.io/predicate/)
:::

- 语句：addCondition(...Json);

::: code-group

```json [Json文本]
{
    "condition": "minecraft:weather_check",
    "raining": true,
    "thundering": true
}
```

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

:::
