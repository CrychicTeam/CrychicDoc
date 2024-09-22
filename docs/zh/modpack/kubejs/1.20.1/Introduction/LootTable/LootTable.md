# 战利品表

## 概述

- **`战利品表`** 战利品用于决定在何种情况下生成何种物品。比如自然生成的容器和可疑的方块内容物、破坏方块时的掉落物、杀死实体时的掉落物、钓鱼时可以钓上的物品、猪灵的以物易物——引用自[minecraft-wiki/战利品表](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8)

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
|   通用    |   ServerEvents.genericLootTables   |   [通用类型战利品表](./Generic.md)   |

## 随机池

- 内含战利品，每个随机池都有自己的抽取次数设置，触发战利品表时独立抽取战利品。

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

::: warning 注意

- 一些抽取项类型并没有被KubeJS提供原生支持，需写为Json文本格式作为addEntry(...Json)函数的参数传递，但绝大多数情况物品类型已足够使用，其他类型可参考如下链接。
- 可参考：[minecraft-wiki/战利品表#抽取项](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8#%E6%8A%BD%E5%8F%96%E9%A1%B9)
- 可参考：[数据包生成器/战利品表](https://misode.github.io/loot-table/)

:::

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

## 谓词

### 全部谓词类型一览

|   谓词类型    |   作用    |   语句    |   KubeJS原生支持    |   示例    |
|:------------:|:---------:|:---------:|:---------:|:---------:|
|   全部   |   评估一系列战利品表谓词，若它们都通过检查，则评估通过。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#全部)   |
|   任何   |   评估一系列战利品表谓词，若其中任意一个通过检查，则评估通过。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#任何)   |
|   方块状态属性   |   检查方块以及其方块状态。需要战利品上下文提供的方块状态进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#方块状态属性)   |
|   伤害来源属性   |   检查伤害来源的属性。需要战利品上下文提供的来源和伤害来源进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#伤害来源属性)   |
|   实体属性   |   检查战利品表上下文中的实体。可从任何上下文调用。   |   entityProperties(..args)   |   [√]   |   [示例](./Predicate.md#实体属性)   |
|   实体分数   |   检查实体的记分板分数。   |   entityScores(..args)   |   [√]   |   [示例](./Predicate.md#实体分数)   |
|   取反（非）   |   定义一个谓词列表，当内含谓词不通过时该谓词通过。   |   -   |   [×]   |   [示例](./Predicate.md#取反)   |
|   被玩家击杀   |   检查实体是否死于玩家击杀(死时被玩家攻击过)。   |   killedByPlayer()   |   [√]   |   [示例](./Predicate.md#被玩家击杀)   |
|   检查位置   |   检查当前位置。需要战利品上下文提供的来源进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#检查位置)   |
|   匹配工具   |   检查工具。需要战利品上下文提供的工具进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#匹配工具)   |
|   随机概率   |   生成一个取值范围为0.0–1.0之间的随机数，并检查其是否小于指定值。可从任何上下文调用。   |   randomChance(..args)   |   [√]   |   [示例](./Predicate.md#随机概率)   |
|   受抢夺附魔影响的随机概率   |   检查随机概率，这个概率会受到抢夺魔咒的等级影响。   |   randomChanceWithLooting(..args)   |   [√]   |   [示例](./Predicate.md#受抢夺附魔影响的随机概率)   |
|   引用谓词文件   |   调用谓词文件并返回其结果。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#引用谓词文件)   |
|   未被爆炸破坏   |   返回成功概率为1/爆炸半径，如果上下文未传递爆炸则始终通过。   |   survivesExplosion()   |   [√]   |   [示例](./Predicate.md#未被爆炸破坏)   |
|   附魔奖励   |   以魔咒等级为索引，从列表中挑选概率通过。需要战利品上下文提供的工具进行检测，如果未提供，则附魔等级被视为 0。   |   -   |   [×]   |   [示例](./Predicate.md#附魔奖励)   |
|   检查时间   |   将当前的游戏时间（更确切地来说，为24000 * 天数 + 当天时间）和给定值进行比较。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#检查时间)   |
|   检查值   |   将一个数与另一个数或范围进行比较。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#检查值)   |
|   检查天气   |   检查当前游戏的天气状态。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#检查天气)   |

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

## 全部物品修饰器类型一览

### 物品修饰器类型

|   物品修饰器类型    |   作用    |   语句    |   KubeJS原生支持    |   示例    |
|:------------:|:---------:|:---------:|:---------:|:---------:|
|   应用奖励公式   |   将预定义的奖励公式应用于物品栈的计数。   |   -   |   [×]   |   [示例](./ItemModifier.md#应用奖励公式)   |
|   复制显示名   |   将实体或方块实体的显示名复制到物品栈NBT中。   |   copyName("block_entity")   |   [×]   |   [示例](./ItemModifier.md#复制实体显示名)   |
|   复制NBT   |   将NBT从指定源复制到项目上。唯一允许的值是"block_entity"   |   -   |   [×]   |   [示例](./ItemModifier.md#复制nbt)   |
|   复制方块状态   |   当物品是由方块产生时，复制方块的方块状态到物品的block_state；否则此物品修饰器不做任何处理。   |   -   |   [×]   |   [示例](./ItemModifier.md#复制方块状态)   |
|   随机附魔   |   为物品附上一个随机的魔咒。魔咒的等级也是随机的。   |   enchantRandomly(..args)   |   [√]   |   [示例](./ItemModifier.md#随机附魔)   |
|   给予等价于经验等级的随机魔咒   |   使用指定的魔咒等级附魔物品（大约等效于使用这个等级的附魔台附魔物品）。   |   enchantWithLevels(..args)   |   [√]   |   [示例](./ItemModifier.md#给予等价于经验等级的随机魔咒)   |
|   设置探险家地图   |   将普通的地图物品变为一个指引到某个结构标签的探险家地图。如果物品不是地图，则不做任何处理。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置探险家地图)   |
|   爆炸损耗   |   如果物品栈是因为方块被爆炸破坏而产生，执行该函数的每个物品有1/爆炸半径的概率消失，物品栈会被分为多个单独的物品计算；否则此物品修饰器不做任何处理。   |   -   |   [×]   |   [示例](./ItemModifier.md#爆炸损耗)   |
|   填充玩家头颅   |   将玩家头颅设置为指定玩家的头颅。如果物品不是玩家头颅则不做任何处理。   |   -   |   [×]   |   [示例](./ItemModifier.md#填充玩家头颅)   |
|   熔炉熔炼   |   将物品转变为用熔炉烧炼后的对应物品。如果物品不可烧炼，则不做任何处理。   |   furnaceSmelt()   |   [√]   |   [示例](./ItemModifier.md#熔炉熔炼)   |
|   限制物品栈数量   |   限制物品数量。   |   -   |   [×]   |   [示例](./ItemModifier.md#限制物品栈数量)   |
|   根据抢夺魔咒调整物品数量   |   决定了抢夺魔咒对该物品数量的影响。如果未使用，抢夺魔咒将对该物品没有效果。   |   lootingEnchant(..args)   |   [√]   |   [示例](./ItemModifier.md#根据抢夺魔咒调整物品数量)   |
|   引用物品修饰器   |   引用另一个物品修饰器。   |   -   |   [×]   |   [示例](./ItemModifier.md#引用物品修饰器)   |
|   设置属性   |   为物品加上属性修饰符。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置属性)   |
|   设置旗帜图案   |   设置旗帜物品的图案。如果物品不是旗帜，则此修饰器不做任何处理。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置旗帜图案)   |
|   设置内容物   |   设置物品的内容物。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置内容物)   |
|   设置物品数量   |   设置该物品的数量。   |   count(..args)   |   [√]   |   [示例](./ItemModifier.md#设置物品数量)   |
|   设置损伤值   |   设置工具的损坏值。   |   damage(..args)   |   [√]   |   [示例](./ItemModifier.md#设置损伤值)   |
|   设置魔咒   |   设置物品的魔咒。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置魔咒)   |
|   设置乐器   |   设置山羊角的种类。如果物品不是山羊角则不做任何处理。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置乐器)   |
|   设置战利品表   |   为一个容器方块物品设定战利品表。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置战利品表)   |
|   设置物品描述   |   为物品添加描述信息。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置物品描述)   |
|   设置物品名   |   添加或修改物品的自定义名称。   |   name(..args)   |   [√]   |   [示例](./ItemModifier.md#设置物品名)   |
|   设置NBT   |   设置物品栈NBT数据。   |   nbt(..args)   |   [×]   |   [示例](./ItemModifier.md#设置nbt)   |
|   设置药水   |   设置物品包含的药水效果标签。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置药水)   |
|   设置迷之炖菜效果   |   为谜之炖菜添加状态效果。   |   -   |   [×]   |   [示例](./ItemModifier.md#设置迷之炖菜状态效果)   |

### 添加物品修饰器

::: code-group

```js [应用战利品表]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            pool.addItem('minecraft:diamond')
        })
        // 为战利品表直接应用
        loot.name(Component.red('测试钻石'))// [!code ++]
        // 有条件的物品修饰器
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
            // 为战利品池直接应用
            pool.name(Component.red('测试钻石'))// [!code ++]
            // 有条件的物品修饰器
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
            // 为战利品项直接应用
            pool.addItem('minecraft:diamond').name(Component.red('测试钻石'))// [!code ++]
            // 有条件的物品修饰器
            pool.addItem('minecraft:diamond').addConditionalFunction(c => {// [!code ++]
                c.name(Component.aqua('测试钻石'))// [!code ++]
            })// [!code ++]
        })
    })
})
```

:::
