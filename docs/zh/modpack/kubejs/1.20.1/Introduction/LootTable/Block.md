# 方块类型战利品表

## 战利品表

::: code-group

```js [修改原战利品表]
ServerEvents.blockLootTables(event => {
    // 修改原战利品表
    event.modifyBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            // 添加战利品
            pool.addItem('minecraft:diamond')// [!code ++]
        }) 
    })
})
```

```js [覆盖原战利品表]
ServerEvents.blockLootTables(event => {
    // 创建战利品表，但因为id(ResourceLocation)和原战利品表一模一样，因此覆盖了原战利品表
    event.addBlock('minecraft:gravel', loot => {
        loot.addPool(pool => {
            // 添加战利品
            pool.addItem('minecraft:diamond')// [!code ++]
        }) 
    })
})
```

```js [带有谓词&修饰器的]
ServerEvents.blockLootTables(event => {
    event.addBlock('minecraft:gravel', loot => {// [!code ++]
        loot.addPool(pool => {// [!code ++]
            pool.addItem('minecraft:diamond')// [!code ++]
            // 为战利品添加有条件的物品修饰器
            pool.addItem('minecraft:diamond').addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++]
            // 为战利品池添加有条件的物品修饰器
            pool.addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++]
        })

        loot.addPool(pool => {// [!code ++]
            pool.addItem('minecraft:diamond')// [!code ++]
            pool.addItem('minecraft:diamond').survivesExplosion()// [!code ++]
            // 为战利品池添加谓词
            pool.survivesExplosion()// [!code ++]
        })// [!code ++]
        // 为战利品表添加有条件的物品修饰器
        loot.addConditionalFunction(c=>c.name(Component.aqua('测试钻石')))// [!code ++]

    })
})

```

:::

## 谓词

- 方块类型战利品表上下文可用的谓词。

|   谓词类型    |   作用    |   语句    |   KubeJS原生支持    |   示例    |
|:------------:|:---------:|:---------:|:---------:|:---------:|
|   全部   |   评估一系列战利品表谓词，若它们都通过检查，则评估通过。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#全部)   |
|   任何   |   评估一系列战利品表谓词，若其中任意一个通过检查，则评估通过。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#任何)   |
|   方块状态属性   |   检查方块以及其方块状态。需要战利品上下文提供的方块状态进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#方块状态属性)   |
|   实体属性   |   检查战利品表上下文中的实体。可从任何上下文调用。   |   entityProperties(..args)   |   [√]   |   [示例](./Predicate.md#实体属性)   |
|   实体分数   |   检查实体的记分板分数。   |   entityScores(..args)   |   [√]   |   [示例](./Predicate.md#实体分数)   |
|   取反（非）   |   定义一个谓词列表，当内含谓词不通过时该谓词通过。   |   -   |   [×]   |   [示例](./Predicate.md#取反)   |
|   检查位置   |   检查当前位置。需要战利品上下文提供的来源进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#检查位置)   |
|   匹配工具   |   检查工具。需要战利品上下文提供的工具进行检测，若未提供则总是不通过。   |   -   |   [×]   |   [示例](./Predicate.md#匹配工具)   |
|   随机概率   |   生成一个取值范围为0.0–1.0之间的随机数，并检查其是否小于指定值。可从任何上下文调用。   |   randomChance(..args)   |   [√]   |   [示例](./Predicate.md#随机概率)   |
|   引用谓词文件   |   调用谓词文件并返回其结果。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#引用谓词文件)   |
|   未被爆炸破坏   |   返回成功概率为1/爆炸半径，如果上下文未传递爆炸则始终通过。   |   survivesExplosion()   |   [√]   |   [示例](./Predicate.md#未被爆炸破坏)   |
|   附魔奖励   |   以魔咒等级为索引，从列表中挑选概率通过。需要战利品上下文提供的工具进行检测，如果未提供，则附魔等级被视为 0。   |   -   |   [×]   |   [示例](./Predicate.md#附魔奖励)   |
|   检查时间   |   将当前的游戏时间（更确切地来说，为24000 * 天数 + 当天时间）和给定值进行比较。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#检查时间)   |
|   检查值   |   将一个数与另一个数或范围进行比较。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#检查值)   |
|   检查天气   |   检查当前游戏的天气状态。可从任何上下文调用。   |   -   |   [×]   |   [示例](./Predicate.md#检查天气)   |

## 物品修饰器

- 方块类型战利品表上下文可用的物品修饰器。

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
