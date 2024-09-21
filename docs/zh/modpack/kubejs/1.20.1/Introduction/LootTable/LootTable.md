<!-- # 战利品表

## 前言

- 本条目作为战利品章节所需必备知识的页面，你可以先阅读战利品章节的其他页面，遇到不理解的名词再折返来查看解释。

- **`战利品表`** 战利品用于决定在何种情况下生成何种物品。比如自然生成的容器和可疑的方块内容物、破坏方块时的掉落物、杀死实体时的掉落物、钓鱼时可以钓上的物品、猪灵的以物易物——引用自[minecraft-wiki/战利品表](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8)，战利品触发时，将测试所持有的所有随机池的谓词，过滤出条件通过的随机池，对这些随机池中的每个战利品项测试战利品项谓词，随后应用战利品修饰，如果战利品修饰也有谓词，则也需要判断谓词是否通过。

- **`战利品谓词`** 战利品谓词是应用于随机池、战利品项、战利品修饰的条件组，当用于随机池时，谓词的通过与否决定该池是否参与抽取；当应用于战利品项时，谓词的通过与否影响该战利品项的掉落；当应用于战利品修饰时，谓词的通过与否影响该战利品修饰的执行。

- **`战利品修饰器`** 战利品修饰是应用于随机池或战利品项的函数，当用于随机池时，整个随机池的战利品项都会尝试应用战利品修饰，当用于战利品项时，该项掉落时会尝试应用战利品修饰。

## 战利品表结构

::: stepper
@tab 战利品表

- **`战利品表类型`** 一个战利品表可能是以下类型之一，如僵尸战利品表是实体类型。

- **`随机池`** 也叫随机池，抽取池，每个战利品表都包含了1个或多个随机池，每个随机池都会尝试掉落战利品，他们互不影响。

- **`战利品表物品函数`** 每个战利品表。可能存在战利品表物品函数列表，是一个[物品修饰器](ItemModifier.md)的数组，可以对整个战利品表的战利品生效。

- **`战利品表谓词`** 每个战利品表。可能存在战利品表谓词列表，是一个[谓词](Predicate.md)的数组，它的通过与否将影响着整个战利品表的所有随机池是否尝试掉落。

@tab 随机池

- **`抽取次数`** 每个随机池声明自己会尝试抽取战利品项的次数。

- **`每级幸运增加的抽取次数`** 现在未被Minecraft使用。

- **`战利品项`** 定义随机池中要尝试掉落的战利品。

- **`战利品表物品函数`** 每个随机池。可能存在战利品表物品函数列表，是一个[物品修饰器](ItemModifier.md)的数组，这里的修饰器只对该池的战利品生效。

- **`战利品表谓词`** 每个随机池。可能存在战利品表谓词列表，是一个[谓词](Predicate.md)的数组，这里的谓词只影响该池的战利品是否掉落。

@tab 战利品项

- **`战利品项类型`** 现在无需关注特别它，但你可以在[数据包生成器/战利品表](https://misode.github.io/loot-table/)找到直观的参考。

- **`权重`** 在随机池占据的权重。

- **`品质`** 根据幸运程度，包括属性与状态效果影响权重max(floor(weight + (quality × generic.luck)),0)由于bug，有时不起作用[bug](https://minecraft.wiki/w/Loot_table#cite_note-luck-2)。

- **`战利品表物品函数`** 每个战利品项，可能存在战利品表物品函数列表，是一个[物品修饰器](ItemModifier.md)的数组，这里的修饰器只对该战利品项生效。

- **`战利品表谓词`** 每个战利品项。可能存在战利品表谓词列表，是一个[谓词](Predicate.md)的数组，这里的谓词只影响该战利品项是否掉落。
:::

## 战利品表类型

### 方块类型

::: code-group

```js

```

:::

### 实体类型

::: code-group

```js

```

:::

### 钓鱼类型

::: code-group

```js

```

:::

### 礼物类型

::: code-group

```js

```

:::

### 箱子类型

::: code-group

```js

```

:::

## 随机池

- 存放许多战利品项的数组，当触发战利品时将从随机池按抽取次数抽取若干次战利品项。

### 抽取次数

- 决定尝试抽取多少次战利品项，使用1个语句设置即可。

- 语句：pool.rolls = 数字提供器; 请查看[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- 语句：pool.setBinomialRolls(重复次数, 成功概率); 设置符合二项分布的抽取次数

- 语句：pool.setUniformRolls(最小值, 最大值); 设置抽取次数区间

### 战利品项

- 作为随机池的元素而存在。

#### 添加物品类型战利品项

- 向随机池中添加物品类型的战利品项。

- 语句：pool.addItem(args); 具有3个方法重载。

::: code-group

```js [简单]
// 向随机池中添加一个物品类型战利品
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple')
        })
    })
})
```

```js [权重]
// 向随机池中添加一个物品类型战利品并设置权重
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5)
        })
    })
})
```

```js [物品个数]
// 向随机池中添加一个物品类型战利品并设置权重与个数，个数由数字提供器决定
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5)
        })
    })
})
```

:::

#### 添加空类型战利品项

- 空的战利品项，抽到它什么都不会掉。

- 语句：pool.addEmpty(权重);

- 示例：尸壳死亡时在该随机池中将有5的权重什么都不掉。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:golden_apple', 5)
            pool.addEmpty(5)
        })
    })
})
```

#### 添加标签类型战利品项

- 创建一个物品标签类型战利品项，将物品标签的全部或单个物品作为战利品。

- 语句：pool.addTag(标签, 是否从中抽取1个物品); 如果为否，则生成标签中全部物品作为战利品。

:::: warning **注意**
::: justify
从标签中抽取物品为true时无法应用战利品修饰，原因：漏洞。目前该漏洞仍在 1.21.1 和 24w33a 中，见[漏洞追踪](https://bugs.mojang.com/browse/MC-212671)
:::
::::

- 示例：尸壳死亡掉落1个猪灵喜爱的物品。

```js
ServerEvents.entityLootTables(event => {
    event.modifyEntity('minecraft:husk', loot => {
        loot.addPool(pool => {
            pool.addTag('minecraft:piglin_loved', true)
            
        })
    })
})
``` -->

# 战利品表

## 概述

- **`战利品表`** 战利品用于决定在何种情况下生成何种物品。比如自然生成的容器和可疑的方块内容物、破坏方块时的掉落物、杀死实体时的掉落物、钓鱼时可以钓上的物品、猪灵的以物易物——引用自[minecraft-wiki/战利品表](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8)

<!-- - **`战利品谓词`** 战利品谓词是应用于随机池、战利品项、战利品修饰的条件组，当用于随机池时，谓词的通过与否决定该池是否参与抽取；当应用于战利品项时，谓词的通过与否影响该战利品项的掉落；当应用于战利品修饰时，谓词的通过与否影响该战利品修饰的执行。

- **`战利品修饰器`** 战利品修饰是应用于随机池或战利品项的函数，当用于随机池时，整个随机池的战利品项都会尝试应用战利品修饰，当用于战利品项时，该项掉落时会尝试应用战利品修饰。 -->

## 战利品表数据结构

::: stepper
@tab 战利品表

- **`类型`** 一个战利品表可能是[以下类型](./LootTable.md#战利品表类型)之一，如僵尸战利品表是实体类型。

- **`随机池列表`** 别名：战利品池列表，定义该战利品表下的随机池（战利品池），每个池子包含将要抽取的战利品，每个池子独立抽取。

**`[可选]物品修饰器列表`** 在这里的物品修饰器将对整个战利品表的战利品起效，每个物品修饰器也拥有一个可选的谓词列表，用于判断这个物品修饰器是否应用。

@tab 随机池（战利品池）

- **`抽取次数`** 每个随机池声明自己会尝试抽取战利品项的次数。

- **`每级幸运增加的抽取次数`** 现在未被Minecraft使用。

- **`抽取项列表`** 别名：战利品列表，一个数组，定义该随机池中有什么抽取项（战利品）。

**`[可选]谓词列表`** 内含若干谓词，可用于判断该池是否尝试抽取。

**`[可选]物品修饰器列表`** 内含若干物品修饰器，可用于修改该池中的战利品，每个物品修饰器也拥有一个可选的谓词列表，用于判断这个物品修饰器是否应用。

@tab 抽取项（战利品）

- **`类型`** 一个战利品列表可能包含数量不定的抽取项，这是战利品表中定义的战利品，一个抽取项可能是[以下类型](./LootTable.md#抽取项类型)之一。

- **`权重`** 战利品项在随机池占据的权重。

- **`品质`** 根据幸运程度，包括属性与状态效果影响权重max(floor(weight + (quality × generic.luck)),0)由于bug，有时不起作用[bug](https://minecraft.wiki/w/Loot_table#cite_note-luck-2)。

**`[可选]谓词列表`** 内含若干谓词，可用于判断该战利品是否掉落。

**`[可选]物品修饰器列表`** 内含若干物品修饰器，可用于修改该战利品，每个物品修饰器也拥有一个可选的谓词列表，用于判断这个物品修饰器是否应用。

:::

## 战利品表类型

- 设置某个游戏事物的战利品表。

- 战利品表类型总览及其对应类型战利品表的完整示例。

|   战利品表类型    |   事件    |   示例    |
|:------------:|:---------:|:---------:|
|   方块    |   ServerEvents.blockLootTables   |   [方块类型战利品表](./Block.md)   |
|   实体    |   ServerEvents.entityLootTables   |   [实体类型战利品表](./Entity.md)   |
|   钓鱼    |   ServerEvents.fishingLootTables   |   [钓鱼类型战利品表](./Fish.md)   |
|   礼物    |   ServerEvents.fishingLootTables   |   [礼物类型战利品表](./Gift.md)   |
|   箱子    |   ServerEvents.chestLootTables   |   [箱子类型战利品表](./Chest.md)   |

## 随机池

- 设置战利品表中随机池的相关操作。

### 添加随机池

- 创建一个新随机池。

- 语句：addPool(pool=>{});

```js
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {// [!code ++]
        })// [!code ++]
    })
})
```

### 抽取次数

- **`默认值`** 默认值为1。

- pool.rolls 是一个[数字提供器](../MiscellaneousKnowledge/NumberProvider.md)

- pool.setUniformRolls(min, max) 设置取值范围，接受最小值与最大值

- pool.setBinomialRolls(n, p) 设置二项分布，接受n尝试次数，p每次尝试成功概率，期望次数np

::: code-group

```js [绝对值]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.rolls = 1// [!code ++]
        })
    })
})
```

```js [取值范围]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.setUniformRolls(1, 1)// [!code ++]
        })
    })
})
```

```js [二项分布]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.setBinomialRolls(5, 0.5)// [!code ++]
        })
    })
})
```

:::

### 抽取项

#### 抽取项类型

|   抽取项类型    |   作用    |   语句    |
|:------------:|:---------:|:---------:|
|   选择    |   从中掉落第一个满足条件的战利品   |   -   |
|   动态    |   用于潜影盒与纹饰陶罐   |   -   |
|   空    |   什么都不掉的战利品   |   addEmpty()   |
|   物品    |   掉落一个物品   |   addItem()   |
|   组    |   掉落一组物品   |   -   |
|   战利品表    |   从另一个战利品表决定掉落什么   |   -   |
|   序列    |   按依次掉落，直到某一项谓词不通过   |   -   |
|   物品标签    |   掉落标签中1个或全部物品   |   -   |

#### 添加抽取项

- 示例：

```js
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')// [!code ++]
        })
    })
})

```

<!-- #### 选择

- 暂无。

#### 动态

- 暂无。

#### 空

- addEmpty()

```js
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addEmpty()// [!code ++]
        }) 
    })
})
```

#### 物品

- addItem([物品栈](../MiscellaneousKnowledge/ItemStack.md))

- addItem([物品栈](../MiscellaneousKnowledge/ItemStack.md), 权重数字)

- addItem([物品栈](../MiscellaneousKnowledge/ItemStack.md), 权重数字, [数字提供器](../MiscellaneousKnowledge/NumberProvider.md))

::: code-group

```js [KubeJS]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:grass_block')// [!code ++]
        }) 
    })
})
```

```js [KubeJS]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:grass_block', 50)// [!code ++]
        }) 
    })
})
```

```js [KubeJS]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:grass_block', 50, {min: 1, max: 3})// [!code ++]
        }) 
    })
})
```

:::

#### 组

- 暂无。

#### 战利品表

- addLootTable(战利品表id);

```js
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addLootTable('minecraft:blocks/blackstone')// [!code ++]
        }) 
    })
})
```

#### 序列

- 暂无。

#### 物品标签

- 暂无。 -->

## 谓词

- 引用页面。

### 谓词类型

|   谓词类型    |   作用    |   语句    |
|:------------:|:---------:|:---------:|
|   全部   |   -   |   -   |
|   任何   |   -   |   -   |
|   方块状态属性   |   -   |   -   |
|   伤害来源属性   |   -   |   -   |
|   实体属性   |   -   |   -   |
|   实体分数   |   -   |   -   |
|   取反（非）   |   -   |   -   |
|   被玩家击杀   |   -   |   -   |
|   检查位置   |   -   |   -   |
|   匹配工具   |   -   |   -   |
|   随机概率   |   -   |   -   |
|   受抢夺附魔影响的随机概率   |   -   |   -   |
|   引用谓词文件   |   -   |   -   |
|   未被爆炸破坏   |   -   |   -   |
|   附魔奖励   |   -   |   -   |
|   检查时间   |   -   |   -   |
|   检查值   |   -   |   -   |
|   检查天气   |   -   |   -   |

### 添加谓词

::: code-group

```js [应用随机池]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')
            pool.survivesExplosion()// [!code ++]
        })
    })
})
```

```js [应用战利品项]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond').survivesExplosion()// [!code ++]
        })
    })
})
```

```js [应用物品修饰器]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')
            pool.addConditionalFunction(c => {
                c.name(Component.aqua('测试钻石'))
                c.survivesExplosion()// [!code ++]
            })
        })
    })
})
```

:::

## 物品修饰器

### 物品修饰器类型

|   物品修饰器类型    |   作用    |   语句    |
|:------------:|:---------:|:---------:|
|   应用奖励公式   |   -   |   -   |
|   复制方块实体显示名   |   -   |   -   |
|   复制NBT   |   -   |   -   |
|   复制方块状态   |   -   |   -   |
|   随机附魔   |   -   |   -   |
|   给予等价于经验等级的随机魔咒   |   -   |   -   |
|   设置探险家地图   |   -   |   -   |
|   爆炸损耗   |   -   |   -   |
|   填充玩家头颅   |   -   |   -   |
|   熔炉熔炼   |   -   |   -   |
|   限制堆叠数量   |   -   |   -   |
|   根据抢夺魔咒调整物品数量   |   -   |   -   |
|   引用物品修饰器文件   |   -   |   -   |
|   设置属性   |   -   |   -   |
|   设置旗帜图案   |   -   |   -   |
|   设置内容物   |   -   |   -   |
|   设置物品数量   |   -   |   -   |
|   设置损伤值   |   -   |   -   |
|   设置魔咒   |   -   |   -   |
|   设置乐器   |   -   |   -   |
|   设置战利品表   |   -   |   -   |
|   设置物品描述   |   -   |   -   |
|   设置物品名   |   -   |   -   |
|   设置NBT   |   -   |   -   |
|   设置药水   |   -   |   -   |
|   设置迷之炖菜效果   |   -   |   -   |

### 添加物品修饰器

::: code-group

```js [应用战利品表]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')
        })
        loot.addConditionalFunction(c => {// [!code ++]
            c.name(Component.aqua('测试钻石'))// [!code ++]
        })// [!code ++]
    })
})
```

```js [应用随机池]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')
            pool.addConditionalFunction(c => {// [!code ++]
                c.name(Component.aqua('测试钻石'))// [!code ++]
            })// [!code ++]
        })

    })
})
```

```js [应用战利品项]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond').addConditionalFunction(c => {// [!code ++]
                c.name(Component.aqua('测试钻石'))// [!code ++]
            })// [!code ++]
        })
    })
})
```

:::
