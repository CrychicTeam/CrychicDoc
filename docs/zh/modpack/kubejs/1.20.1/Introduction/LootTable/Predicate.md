# 谓词

> [!WARNING] 施工警戒线
> 该页面待重置，写太烂。

## 前言

- 释义：用于判断对象或参数的一组条件，或者说是对一类特征的抽象描述；例如：“他穿了衣服”，谓词：“某某穿了衣服”。

- 作用：常作为战利品的掉落与物品修饰器的应用条件，不过由于KubeJS并没有原生支持全部的谓词，因此绝大部分都需要写Json作为参数传入。

> [!WARNING] 注意
> 由于大部分谓词并不被KubeJS原生支持，只能以Json传入，本文已尝试给出示例但观感极差，因此建议在[minecraft-wiki/谓词](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D)了解某个谓词的作用后使用[数据包生成器#谓词](https://misode.github.io/predicate/)来快速书写谓词。

## 谓词类型

<!-- - 技巧：可以使用该网站快速生成谓词Json文本，选定战利品表类型后它将只提供该类型可用的谓词，十分具有帮助[数据包生成器#谓词](https://misode.github.io/predicate/)

- 注意：本文指向的wiki页面均已更新至最新minecraft1.21版本的内容，存在些微差异，建议使用上述的数据包生成器选定1.20后生成。 -->

### 全部

- 作用：定义谓词数组，当数组内全部谓词均通过时此谓词通过。

<!-- - 类型："minecraft:all_of" -->

- 参考：[minecraft-wiki/谓词#all_of](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#all_of)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json
{
    "condition": "minecraft:all_of",
    // 数组 包含其他谓词
    "terms": []
}
```

::: -->

### 任何

- 作用：定义谓词数组，当数组内任一谓词通过时此谓词通过。

<!-- - 类型："minecraft:any_of" -->

- 参考：[minecraft-wiki/谓词#any_of](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#any_of)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json
{
    "condition": "minecraft:any_of",
    // 数组 包含其他谓词
    "terms": []
}
```

::: -->

### 方块状态属性

- 作用：将检查战利品上下文中方块的属性。

<!-- - 类型："minecraft:block_state_property" -->

- 参考：[minecraft-wiki/谓词#block_state_property](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#block_state_property)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

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

::: -->

### 伤害来源属性

- 作用：检查伤害来源的属性。

<!-- - 类型："minecraft:damage_source_properties" -->

- 参考：[minecraft-wiki/谓词#damage_source_properties](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#damage_source_properties)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

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

::: -->

### 实体属性

- 作用：检查战利品表上下文中的实体。

<!-- - 类型："minecraft:entity_properties" -->

- 参考：[minecraft-wiki/谓词#entity_properties](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#entity_properties)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json
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
                // 【可选】检查垂直速度分量是否在指定区间
                // 【可选】检查X轴速度分量是否在指定区间
                // 【可选】检查Y轴速度分量是否在指定区间
                // 【可选】检查Z轴速度分量是否在指定区间
              }
        }
    }
}
```

::: -->

### 实体分数

- 作用：检查实体的记分板分数。

<!-- - 类型："entity_scores" -->

- 参考：[minecraft-wiki/谓词#entity_scores](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#entity_scores)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json
{
  "condition": "minecraft:entity_scores",
  // 要检查的实体。从战利品上下文指定实体。
  //设置成this表示实体自身，即死亡的实体，或是破坏方块、打开容器或获取进度的玩家
  // killer表示进行伤害的实体
  // direct_killer表示进行直接伤害的实体
  // killer_player表示进行伤害的玩家。
  "entity": "this",
  "scores": {
    "minecraft:test": {
      "min": 0,
      "max": 2
    }
  }
}
```

::: -->

### 取反

- 作用：定义一个谓词数组，当内含谓词不通过时该谓词通过。

- 参考：[minecraft-wiki/谓词#entity_inverted](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#inverted)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json

```

::: -->

### 被玩家击杀

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：检查实体是否死于玩家击杀。

- 参考：[minecraft-wiki/谓词#killed_by_player](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#killed_by_player)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- - 类型： -->

::: code-group

```js
killedByPlayer()
```

<!-- ```json

``` -->

:::

### 检查位置

- 作用：检查执行位置。

<!-- - 类型： -->

- 参考：[minecraft-wiki/谓词#location_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#location_check)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json

```

::: -->

### 匹配工具

- 作用：检查战利品上下文工具。

<!-- - 类型： -->

- 参考：[minecraft-wiki/谓词#match_tool](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#match_tool)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- ::: code-group

```json

```

::: -->

### 随机概率

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：检查随机概率，概论使用[0, 1]的数字表示。

- 参考：[minecraft-wiki/谓词#random_chance](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#random_chance)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

::: code-group

```js
randomChance(0.5);
```

<!-- ```json

``` -->

:::

### 受抢夺附魔影响的随机概率

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：检查随机概率，这个概率会受到抢夺魔咒的等级影响。

- 参考：[minecraft-wiki/谓词#random_chance_with_enchanted_bonus](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#random_chance_with_enchanted_bonus)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

::: code-group

```js [KubeJS]
// 基础概率，每一级抢夺增加的概率
randomChanceWithLooting(0.2, 0.2)
```

<!-- ```json
// 基础概率，每一级抢夺增加的概率
randomChanceWithLooting(0.2, 0.2)
``` -->

:::

### 引用谓词文件

- 参考：[minecraft-wiki/谓词#reference](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#reference)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

### 未被爆炸破坏

<!-- - 注意：该类型是被KubeJS原生支持的，但作为其他不受原生支持的谓词的内容时仍然需要写成Json文本。 -->

- 作用：返回成功概率为1 / 爆炸半径，如果上下文未传递爆炸则始终通过。

- 参考：[minecraft-wiki/谓词#survives_explosion](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#survives_explosion)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

<!-- - 语句：survivesExplosion()

- 示例：如果燧石被爆炸摧毁不会掉落火药。 -->

::: code-group

```js
survivesExplosion()
```

:::

### 附魔奖励

- 作用：定义一个包含概率（小数）的数组，检查魔咒id，根据该id魔咒等级确定使用哪一个数字进行概率判断。

- 参考：[minecraft-wiki/谓词#table_bonus](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#table_bonus)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

### 检查时间

- 作用：将当前的游戏时间（更确切地来说，为24000 * 天数 + 当天时间）和给定值进行比较。

- 参考：[minecraft-wiki/谓词#time_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#time_check)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

### 检查值

- 作用：将一个数与另一个数或范围进行比较。

- 参考：[minecraft-wiki/谓词#value_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#value_check)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)

### 检查天气

- 参考：[minecraft-wiki/谓词#weather_check](https://zh.minecraft.wiki/w/%E8%B0%93%E8%AF%8D#weather_check)

- 参考：[数据包生成器/谓词](https://misode.github.io/predicate/)
