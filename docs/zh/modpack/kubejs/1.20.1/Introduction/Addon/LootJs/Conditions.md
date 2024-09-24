# LootJs
LootJs是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作
`KubeJS`本身自带的修改Loot的方法过于繁琐，若要修改关于:
- 方块
- 实体
- 战利品列表

内的LootTable

推荐使用`LootJs`来实现

:::v-info
该篇教程源自于Github的官方Wiki

适用于Minecraft 1.19.2/1.20.1
:::

## 简介

除了[操作](LootJs.md)之外，`LootJs`还可以使用条件来应用过滤器。

如果某个条件不成立，则在条件不成立之后不会触发任何操作。可以串联多个条件，以将更多过滤器应用于您的战利品修改器。

### 匹配战利品
matchLoot(ItemFilter, exact)

语句`matchLoot()`放置在`addLoot()`之前生效

`ItemFilter`填入你要匹配的物品ID

传参`exact`可选择，若为`true`，则该物品战利品列表里面的所有物品要满足条件后才会进行修改

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier('minecraft:cow')
        .matchLoot('minecraft:leather')// true / false
        .addLoot('minecraft:diamond')
})
```
上述示例代码，对应`minecraft:cow`的战利品列表，若`minecraft:cow`的战利品列表满足了拥有`minecraft:leather`，则添加一个新的战利品`minecraft:diamond`给`minecraft:cow`，若不满足该条件，则不会添加新的战利品

### 匹配主手物品
matchMainHand(ItemFilter)

匹配主手物品为一个`ItemFilter`，当你要匹配的主手物品带有耐久的时候，建议使用`Item.of().ignoreNBT()`来忽略物品的耐久度
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchMainHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .addLoot("minecraft:gravel")
})
```
### 匹配副手物品
matchOffHand(ItemFilter)

匹配副手物品为一个`ItemFilter`，当你要匹配的副手物品带有耐久的时候，建议使用`Item.of().ignoreNBT()`来忽略物品的耐久度
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchOffHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .addLoot("minecraft:gravel")
})
```

### 匹配装备
matchEquip(slot, ItemFilter)

匹配玩家身上的装备，包括头部，胸部，腿部，脚以及主副手符合条件后，进行战利品列表修改


无视耐久检测，可以加上`Item.of().ignoreNBT()`来取消对耐久的匹配

`slot`对应的属性有：
- `MAINHAND` 主手
- `OFFHAND` 副手
- `FEET` 脚
- `LEGS` 腿部
- `CHEST` 胸部
- `HEAD` 头部

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchEquip("MAINHAND", Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .addLoot("minecraft:gravel")
})
```
### 爆炸检查 
survivesExplosion()

`survivesExplosion()`语句添加了对能在爆炸中不会消失的方块的检查

若TNT一类的爆炸物品对该方块不会造成消失，则会添加一个战利品给这个方块

若爆炸后方块会消失，则该方块不会被添加Loot

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .survivesExplosion()
        .addLoot("minecraft:gravel")
})
```

### tick检查
timeCheck(period, min, max)

timeCheck(min, max)

下列示例代码对砂砾添加了一个时间检测：
- `24000` 是周期`period`，对应一个完整的游戏循环，即一天
- `0` 是最小值（min），表示日出时间
- `9000` 是最大值（max），大约对应游戏的上午时分

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .timeCheck(24000, 0, 9000)
        .addLoot("minecraft:diamond")
})
```
这串代码演示了：只有在游戏内的早晨时间（大约从日出到上午）破坏沙砾时，有机会掉落钻石

若不添加`period`，检查游戏世界的总时间是在所指定的`(min,max)`刻之间

```js
LootJS.modifiers((event) => {
   event
        .addBlockLootModifier("minecraft:gravel")
        .timeCheck(0, 12000)
        .addLoot("minecraft:diamond")
})
```
添加`period`以及不添加`period`的区别:
- 使用 period 时，检查的是`period`周期内的相对时间
- 不使用 period 时，检查的是游戏世界从`min`刻到`max`刻的总时间

例如上述代码`period`传参为24000，即检查一整天的tick(24000)内对应的0-9000 tick

不使用period的时候，检测的是当天tick内对应的0-12000 tick

### 天气检查
weatherCheck(value)

`weatherCheck()`语句对应传入的参数是:
- raining 雨天
- thundering 雷雨天

注意，要使用`{}`进行包裹
```json
{
    raining: true, // false
    thundering: true // false
}
```
下面的示例代码描述的是在雨天的时候挖掘砂砾会掉落钻石
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .weatherCheck({
            raining: true,
        })
        .addLoot("minecraft:diamond")
})
```
若检测为雷雨天，则将`raining`更换为`thundering`，若要检测是否为晴天的时候，将`raining`以及`thundering`两个都设置为`false`即可

### 随机概率
randomChance(value)

`randomChance()`语句对应的传入参数为`float`

用来设置战利品的掉落概率

下面的示例代码描述的是：玩家挖掘砂砾后，有30%的概率会掉落钻石
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChance(0.3) // 30%
        .addLoot("minecraft:diamond")
})
```

### 抢夺附魔随机概率
randomChanceWithLooting(value, looting)

`randomChanceWithLooting()`传入的参数为`value`即概率，`looting`即抢夺附魔的等级

下面的示例代码描述的是：玩家手持附魔了抢夺2的工具，破坏砂砾后有30%的概率掉落钻石

这个方法仅适用于方块，对生物使用`randomChanceWithLooting()`方法会出现一个奇怪的bug：不管概率以及附魔等级是否指定，都会添加战利品到生物的战利品池中.
相当于对生物使用这个方法和没使用是一样的效果
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChanceWithLooting(0.3, 2) // 30% 
        .addLoot("minecraft:diamond")
})
```
### 附魔随机概率
randomChanceWithEnchantment(enchantment, [chances])

`randomChanceWithEnchantment()`语句的用途是指定手上物品的附魔ID以及附魔等级来进行战利品列表的修改

传入的参数`enchantment`对应附魔的ID，后面的数组`[chances]`对应的是附魔的等级

值得注意的是`[chances]`的计算是从该物品没有附魔，即0等级的附魔开始计算，例如：
- 效率5 `[0, 0.05, 0.05, 0.1, 0.3, 0.5]`对应的是效率附魔等级0-1-2-3-4-5级
- 0级为0%，1级为5%，2级为5%，3级为10%，4级为30%，5级为50%
- 时运3 `[0, 0.1, 0.4, 0.5]`对应的是时运附魔等级的0-1-2-3级
- 0级为0%，1级为10%，2级为40%，3级为50%

附魔等级有多少，数组里面的数字就填多少个，如果数组里面的数字数量超出了附魔等级，游戏则会报错

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChanceWithEnchantment("minecraft:looting", [0, 0.1, 0.5, 1]) 
        .addLoot("minecraft:diamond")
    
    /*
        [0, 0.1, 0.5, 1]:
          0% for no looting
         10% for looting 1
         50% for looting 2
        100% for looting 3
    */
})
```
### 生物群系检查
biome(...biomes)

`biome()`语句传入的参数为对应的生物群系ID，或者`Tag`，即在生物群系之前加上`#`来标记为`Tag`

如果给出了多个`biome`，则所有的生物群系必须匹配，若一个不匹配则不会进行战利品添加

官方示例：
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .biome("minecraft:jungle") 
        .addLoot("minecraft:diamond")
})
```

### 生物群系检查2
anyBiome(...biomes)

`anyBiome()`语句传入的参数和`biome()`语句传入的参数大相径庭

只是功能上进行了区别：
- `biome()`语句若指定了多个生物群系，则所有的群系条件必须满足，不满足则不会添加战利品
- `anyBiome()`语句若指定了多个生物群系，则至少有有一个生物群系匹配，才会添加战利品.若一个都不匹配，则不会添加战利品
  
官方示例：
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyBiome("minecraft:jungle", "#minecraft:is_forest") 
        .addLoot("minecraft:diamond")
})
```
### 维度检查
anyDimension(...dimensions)

`anyDimension()`语句传入的参数为维度ID
- `minecraft:overworld` 主世界
- `minecraft:the_nether` 下界
- `minecraft:the_end`末地

或者传入玩家的自定义维度`modid:dimensions_name`

当玩家处于所指定的维度时，才会添加对应的战利品

官方示例：
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyDimension("minecraft:nether") 
        .addLoot("minecraft:diamond")
})
```
### 结构检查
anyStructure([structures], exact)

`anyStructure()`语句传入的参数为`structures`(结构ID)以及`exact`(boolean)

玩家只有在进入特定的结构时，才会添加对应的战利品

`structures`可以写成一个数组来进行多个结构指定.

而`exact`的作用是
- false 只会检查玩家是否在结构边界内
- true  检查玩家是否在结构内（例如村庄中的房屋）

官方示例：
```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyStructure(["minecraft:stronghold", "minecraft:village"], false) 
        .addLoot("minecraft:diamond")
})
```

