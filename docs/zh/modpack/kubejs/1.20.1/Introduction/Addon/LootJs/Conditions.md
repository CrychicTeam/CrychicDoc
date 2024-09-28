# LootJs

LootJs 是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作
`KubeJS`本身自带的修改 Loot 的方法过于繁琐，若要修改关于:

-   方块
-   实体
-   战利品列表

内的 LootTable

推荐使用`LootJs`来实现

:::v-info
该篇教程源自于 Github 的官方 Wiki

适用于 Minecraft 1.19.2/1.20.1
:::

## 简介

除了[操作](LootJs.md)之外，`LootJs`还可以使用条件来应用过滤器。

如果某个条件不成立，则在条件不成立之后不会触发任何操作。可以串联多个条件，以将更多过滤器应用于您的战利品修改器。

开始此篇之前，你需要记住这几个网址

[EntityPredicateBuilderJS](https://github.com/AlmostReliable/lootjs/wiki/1.19.2-Types#EntityPredicateBuilderJS)

[DamageSourcePredicateBuilderJS](https://github.com/AlmostReliable/lootjs/wiki/1.19.2-Types#DamageSourcePredicateBuilderJS)

此篇内容中会讲述到对应的 BuilderJS，可以翻阅此链接进行查看

### 匹配战利品

matchLoot(ItemFilter, exact){font-small}

语句`matchLoot()`放置在`addLoot()`之前生效

`ItemFilter`填入你要匹配的物品 ID

传参`exact`可选择，若为`true`，则该物品战利品列表里面的所有物品要满足条件后才会进行修改

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:cow")
        .matchLoot("minecraft:leather") // true / false
        .addLoot("minecraft:diamond");
});
```

上述示例代码，对应`minecraft:cow`的战利品列表，若`minecraft:cow`的战利品列表满足了拥有`minecraft:leather`，则添加一个新的战利品`minecraft:diamond`给`minecraft:cow`，若不满足该条件，则不会添加新的战利品

### 匹配主手物品

matchMainHand(ItemFilter){font-small}

匹配主手物品为一个`ItemFilter`，当你要匹配的主手物品带有耐久的时候，建议使用`Item.of().ignoreNBT()`来忽略物品的耐久度

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchMainHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .addLoot("minecraft:gravel");
});
```

### 匹配副手物品

matchOffHand(ItemFilter){font-small}

匹配副手物品为一个`ItemFilter`，当你要匹配的副手物品带有耐久的时候，建议使用`Item.of().ignoreNBT()`来忽略物品的耐久度

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchOffHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .addLoot("minecraft:gravel");
});
```

### 匹配装备

matchEquip(slot, ItemFilter){font-small}

匹配玩家身上的装备，包括头部，胸部，腿部，脚以及主副手符合条件后，进行战利品列表修改

无视耐久检测，可以加上`Item.of().ignoreNBT()`来取消对耐久的匹配

`slot`对应的属性有：

-   `MAINHAND` 主手
-   `OFFHAND` 副手
-   `FEET` 脚
-   `LEGS` 腿部
-   `CHEST` 胸部
-   `HEAD` 头部

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchEquip(
            "MAINHAND",
            Item.of("minecraft:netherite_pickaxe").ignoreNBT()
        )
        .addLoot("minecraft:gravel");
});
```

### 匹配实体

matchEntity(callback)

`matchEntity()`语句通过回调函数来进行实体的匹配，例如当实体拥有对应的 NBT 或者状态后，进行战利品修改，反之则不进行修改

与死亡、打开箱子或破坏方块的实体匹配

`LootJs`将在您的回调函数中提供`EntityPredicateBuilderJS`以进行匹配。

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchEntity((entity) => {
            entity.isInWater(true);
        })
        .addLoot("minecraft:diamond");
});
```

以上的代码示例检测`minecraft:creeper`是否处于水中，若处于水中，则为该实体添加战利品`minecraft:diamond`掉落+

### 匹配直接击杀者

matchDirectKiller(callback)

直接击杀者以及间接击杀者的关系，例如玩家射箭杀死了一只僵尸：

-   僵尸是被击杀者
-   箭是直接击杀者
-   玩家却是间接击杀者

因为是箭杀死的僵尸，而不是玩家杀死的僵尸，但是箭是玩家射出的，所以玩家属于间接击杀者

相较于匹配实体，`matchDirectKiller()`是匹配直接击杀者的条件

下面的示例代码检查直接击杀者是否在水中击杀了被击杀者，若击杀者在水中，则给被击杀者添加一个战利品掉落`minecraft:diamond`

反之若击杀者未处于水中，则不会给被击杀者添加一个战利品掉落

同样的道理，`LootJs`给回调函数提供的依旧是`EntityPredicateBuilderJS`以进行匹配

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchDirectKiller((entity) => {
            entity.isInWater(true);
        })
        .addLoot("minecraft:diamond");
});
```

### 匹配间接击杀者

matchKiller(callback)

相较于匹配直接击杀者，而`matchKiller()`匹配的是间接击杀者的条件

击杀者不管是弹射物还是玩家，最终都已间接造成伤害的击杀者为准

上述检查`直接击杀者`的代码中，若使用弓箭在地面对`minecraft:creeper`进行射击，不会添加新的战利品给`minecraft:creeper`
只有在水中射击(箭矢在水中击杀生物，且未离开水面)，才会给`minecraft:creeper`添加一个新的战利品

下面的示例代码中，只会匹配间接击杀者的状态，即玩家，不会匹配箭矢的状态.当间接击杀者的状态符合条件后，对被击杀者添加新的战利品

同样的道理，`LootJs`给回调函数提供的依旧是`EntityPredicateBuilderJS`以进行匹配

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchKiller((entity) => {
            entity.isInWater(true);
        })
        .addLoot("minecraft:diamond");
});
```

### 匹配玩家

matchPlayer(callback)

相较于上述的`直接击杀者`,`间接击杀者`，而`matchPlayer`匹配玩家的条件.

`LootJs`给回调函数提供的依旧是`EntityPredicateBuilderJS`以进行匹配

下述代码中，直接检测玩家是否在水中并生成对应的战利品掉落

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchPlayer((player) => {
            player.isInWater();
        })
        .addLoot("minecraft:diamond");
});
```

### 匹配伤害来源和类型

matchDamageSource(callback)

上述三种匹配类型是根据拥有对应的条件后才会进行战利品的生成

而`matchDamageSource()`是针对于伤害的类型，以及来源来进行匹配，并生成对应的战利品

`LootJS`将提供`DamageSourcePredicateBuilderJS`给回调函数中进行匹配

下面的示例代码中，将检测对`minecraft:creeper`的伤害类型是否是铁砧掉落，若是则添加一个新的战利品给`minecraft:creeper`

将`anvil`修改为`arrow`后就是检测来自箭矢的伤害类型

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchDamageSource((source) => {
            source.anyType("anvil");
        })
        .addLoot("minecraft:diamond");
});
```

更多的伤害类型请查阅 Minecraft Wiki 进行获取

`and` ?
`is` ?
`or` ?
`isNot` ?
`invert` ?
`matchDirectEntity` ?
`matchSourceEntity` ?

### 爆炸检查

survivesExplosion(){font-small}

`survivesExplosion()`语句添加了对能在爆炸中不会消失的方块的检查

若 TNT 一类的爆炸物品对该方块不会造成消失，则会添加一个战利品给这个方块

若爆炸后方块会消失，则该方块不会被添加 Loot

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .survivesExplosion()
        .addLoot("minecraft:gravel");
});
```

### tick 检查

timeCheck(period, min, max){font-small}

timeCheck(min, max)

下列示例代码对砂砾添加了一个时间检测：

-   `24000` 是周期`period`，对应一个完整的游戏循环，即一天
-   `0` 是最小值（min），表示日出时间
-   `9000` 是最大值（max），大约对应游戏的上午时分

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .timeCheck(24000, 0, 9000)
        .addLoot("minecraft:diamond");
});
```

这串代码演示了：只有在游戏内的早晨时间（大约从日出到上午）破坏沙砾时，有机会掉落钻石

若不添加`period`，检查游戏世界的总时间是在所指定的`(min,max)`刻之间

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .timeCheck(0, 12000)
        .addLoot("minecraft:diamond");
});
```

添加`period`以及不添加`period`的区别:

-   使用 period 时，检查的是`period`周期内的相对时间
-   不使用 period 时，检查的是游戏世界从`min`刻到`max`刻的总时间

例如上述代码`period`传参为 24000，即检查一整天的 tick(24000)内对应的 0-9000 tick

不使用 period 的时候，检测的是当天 tick 内对应的 0-12000 tick

### 天气检查

weatherCheck(value){font-small}

`weatherCheck()`语句对应传入的参数是:

-   raining 雨天
-   thundering 雷雨天

注意，要使用`{}`进行包裹

```json
{
    "raining": true, // false
    "thundering": true // false
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
        .addLoot("minecraft:diamond");
});
```

若检测为雷雨天，则将`raining`更换为`thundering`，若要检测是否为晴天的时候，将`raining`以及`thundering`两个都设置为`false`即可

### 随机概率

randomChance(value){font-small}

`randomChance()`语句对应的传入参数为`float`

用来设置战利品的掉落概率

下面的示例代码描述的是：玩家挖掘砂砾后，有 30%的概率会掉落钻石

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChance(0.3) // 30%
        .addLoot("minecraft:diamond");
});
```

### 抢夺附魔随机概率

randomChanceWithLooting(value, looting){font-small}

`randomChanceWithLooting()`传入的参数为`value`即概率，`looting`即抢夺附魔的等级

下面的示例代码描述的是：玩家手持附魔了抢夺 2 的工具，破坏砂砾后有 30%的概率掉落钻石

这个方法仅适用于方块，对生物使用`randomChanceWithLooting()`方法会出现一个奇怪的 bug：不管概率以及附魔等级是否指定，都会添加战利品到生物的战利品池中.
相当于对生物使用这个方法和没使用是一样的效果

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChanceWithLooting(0.3, 2) // 30%
        .addLoot("minecraft:diamond");
});
```

### 附魔随机概率

randomChanceWithEnchantment(enchantment, [chances]){font-small}

`randomChanceWithEnchantment()`语句的用途是指定手上物品的附魔 ID 以及附魔等级来进行战利品列表的修改

传入的参数`enchantment`对应附魔的 ID，后面的数组`[chances]`对应的是附魔的等级

值得注意的是`[chances]`的计算是从该物品没有附魔，即 0 等级的附魔开始计算，例如：

-   效率 5 `[0, 0.05, 0.05, 0.1, 0.3, 0.5]`对应的是效率附魔等级 0-1-2-3-4-5 级
-   0 级为 0%，1 级为 5%，2 级为 5%，3 级为 10%，4 级为 30%，5 级为 50%
-   时运 3 `[0, 0.1, 0.4, 0.5]`对应的是时运附魔等级的 0-1-2-3 级
-   0 级为 0%，1 级为 10%，2 级为 40%，3 级为 50%

附魔等级有多少，数组里面的数字就填多少个，如果数组里面的数字数量超出了附魔等级，游戏则会报错

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChanceWithEnchantment("minecraft:looting", [0, 0.1, 0.5, 1])
        .addLoot("minecraft:diamond");

    /*
        [0, 0.1, 0.5, 1]:
          0% for no looting
         10% for looting 1
         50% for looting 2
        100% for looting 3
    */
});
```

### 生物群系检查

biome(...biomes){font-small}

`biome()`语句传入的参数为对应的生物群系 ID，或者`Tag`，即在生物群系之前加上`#`来标记为`Tag`

如果给出了多个`biome`，则所有的生物群系必须匹配，若一个不匹配则不会进行战利品添加

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .biome("minecraft:jungle")
        .addLoot("minecraft:diamond");
});
```

### 生物群系检查 2

anyBiome(...biomes){font-small}

`anyBiome()`语句传入的参数和`biome()`语句传入的参数大相径庭

只是功能上进行了区别：

-   `biome()`语句若指定了多个生物群系，则所有的群系条件必须满足，不满足则不会添加战利品
-   `anyBiome()`语句若指定了多个生物群系，则至少有有一个生物群系匹配，才会添加战利品.若一个都不匹配，则不会添加战利品

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyBiome("minecraft:jungle", "#minecraft:is_forest")
        .addLoot("minecraft:diamond");
});
```

### 维度检查

anyDimension(...dimensions){font-small}

`anyDimension()`语句传入的参数为维度 ID

-   `minecraft:overworld` 主世界
-   `minecraft:the_nether` 下界
-   `minecraft:the_end`末地

或者传入玩家的自定义维度`modid:dimensions_name`

当玩家处于所指定的维度时，才会添加对应的战利品

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyDimension("minecraft:nether")
        .addLoot("minecraft:diamond");
});
```

### 结构检查

anyStructure([structures], exact){font-small}

`anyStructure()`语句传入的参数为`structures`(结构 ID)以及`exact`(boolean)

玩家只有在进入特定的结构时，才会添加对应的战利品

`structures`可以写成一个数组来进行多个结构指定.

而`exact`的作用是

-   false 只会检查玩家是否在结构边界内
-   true 检查玩家是否在结构内（例如村庄中的房屋）

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyStructure(["minecraft:stronghold", "minecraft:village"], false)
        .addLoot("minecraft:diamond");
});
```

### 亮度检查

lightLevel(min, max){font-small}

`lightLevel()`语句传入的参数为两个整数，即 0-15 之间

通过检测方块的亮度来进行战利品的添加

官方示例:

```js
LootJS.modifiers((event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .lightLevel(0, 15)
        .addLoot("minecraft:diamond");
});
```

### 玩家击杀检查

killedByPlayer(){font-small}

该语句不需要传入任何参数，单纯是一个检测的方法

该方法只适用于对生物的战利品修改

检测生物是否被玩家击杀，如果是玩家击杀，则添加对应的战利品

官方示例：

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .killedByPlayer()
        .addLoot("minecraft:diamond");
});
```

### 击杀者距离检测

distanceToKiller(interval){font-small}

`distanceToKiller()`传入的参数为`$MinMaxBounds$Doubles$Type`，只需要填入数字即可

检测被击杀者与击杀者的距离，若击杀者位于所制定的范围之外，则会生成对应的战利品.若在指定的范围内，则不会生成战利品

下面的示例代码中，若玩家在`minecraft:villager`的 3 格范围内击杀了`minecraft:villager`，则不会生成战利品.若在 3 格范围之外击杀
`minecraft:villager`，则会生成相应的战利品

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:villager")
        .distanceToKiller(3)
        .addLoot("minecraft:diamond");
});
```

### 任意阶段检查

hasAnyStage(...stages){font-small}

`hasAnyStage()`语句传入的参数为玩家的阶段

若玩家持有指定的该阶段，则会对对应的生物/方块/战利品列表添加对应的战利品掉落

该方法建议搭配[Game Stage](https://www.mcmod.cn/class/1360)[游戏阶段]模组进行搭配使用

下面的示例代码中，当玩家持有`diamondSpawn`阶段的时候，击杀`minecraft:pig`会生成钻石战利品掉落物

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .hasAnyStage("diamondsSpawn")
        .addLoot("minecraft:diamond");
});
```

### 玩家谓词

playerPredicate(callback){font-small}

谓词跳转[Predicate.md](../../LootTable/BasicKnowledge/Predicate.md)进行详情

下面的示例代码中，使用回调函数来检查`player`的血量是否大于 5，若大于 5 则生成对应的战利品，反之不生成战利品

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .playerPredicate((player) => player.getHealth() > 5)
        .addLoot("minecraft:emerald");
});
```

### 实体谓词

entityPredicate(callback){font-small}

谓词跳转[Predicate.md](../../LootTable/BasicKnowledge/Predicate.md)进行详情

下面的示例中，用回调函数来检查被击杀的`minecraft:pig`(Entity)是否处于水中，若处于水中则生成对应的战利品，反之则不生成战利品

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .entityPredicate((entity) => entity.isInWater())
        .addLoot("minecraft:emerald");
});
```

### 击杀者谓词

killerPredicate(callback){font-small}

谓词跳转[Predicate.md](../../LootTable/BasicKnowledge/Predicate.md)进行详情

上面已经科普过直接击杀者与间接击杀者的关系，下面的示例代码中，回调函数检测的是间接击杀者的谓词

当玩家手持钻石剑在水中杀死`minecraft:pig`的时候，会掉落一根羽毛，若玩家使用弓箭来杀死`minecraft:pig`，箭矢射到了水中，而玩家没处于水中，则不会生成战利品掉落，只有玩家在水中用弓箭(箭矢可以离开水面)射死`minecraft:pig`后才会掉落羽毛

这里可以很明显的看出，`killerPredicate()`语句是用于检测间接击杀者的谓词

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .killerPredicate((entity) => entity.isInWater())
        .addLoot("minecraft:feather");
});
```

### 直接击杀者谓词

directKillerPredicate(callback){font-small}

`directKillerPredicate()`的回调函数，是检测直接击杀者的谓词

例如：玩家射箭击杀了僵尸，检测的是箭而不是玩家，因为是箭对僵尸造成的击杀，并不是玩家对僵尸造成了击杀

下面的示例代码中，只有直接击杀者在水中击杀了`minecraft:zombie`后才会生成对应的战利品掉落物，例如：

-   玩家在水中使用剑击杀了`minecraft:zombie`
-   玩家射出的箭在水中击杀了`minecraft:zombie`
-   玩家抛出的治疗药水在水中击杀了`minecraft:zombie`

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:zombie")
        .killerPredicate((entity) => entity.isInWater())
        .addLoot("minecraft:feather");
});
```

### not 语句

not(callback){font-small}

`not()`语句为 LootTable 的修改添加了一个否定，当不满足条件的时候生成对应的战利品

下面的示例代码中：`not()`语句回调函数检测玩家手上的物品是否为`minecraft:diamond_sword`，若为该物品，则不会生成战利品，反之则生成战利品

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .not((n) => {
            n.matchMainHand("minecraft:diamond_sword");
        })
        .addLoot("minecraft:diamond");
});
```

### or 语句

or(callback){font-small}

`or()`语句为 LootTable 的修改添加多个条件，只要其中一个条件为真，则会对战利品进行修改，若一个条件都不为真，则不会进行战利品修改

下面的示例代码中，添加了两个条件，一个是检测玩家的主手是否为`minecraft:diamond_sword`，一个是检测玩家的副手是否为`minecraft:coal`，当玩家只要满足其中一个条件的时候，都会进行对战利品的修改

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .or((or) => {
            or.matchMainHand("minecraft:diamond_sword");
            or.matchOffHand("minecraft:coal");
        })
        .addLoot("minecraft:diamond");
});
```

### and 语句

and(callback){font-small}

相较于上述的`or()`语句，`and()`语句则是必须满足所有条件，才会对战利品进行修改

下面的示例代码中，只有当玩家的主手为`minecraft:diamond_sword`以及副手为`minecraft:coal`的时候，战利品的修改才会生效.若缺少其中之一一个条件，战利品修改都不会生效

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .and((and) => {
            and.matchMainHand("minecraft:diamond_sword");
            and.matchOffHand("minecraft:coal");
        })
        .addLoot("minecraft:diamond");
});
```

### 自定义条件

customCondition(json){font-small}

`customCondition()`语句里要填入的参数为 json，当满足这个自定义条件的时候，对战利品进行修改，反之不会进行修改

下面的示例代码中，只有当对应修改的方块/实体在满足了`minecraft:weather_check`且`raining`为`true`的时候才会进行对其战利品的修改，也就是检查天气为雨天的时候才会对`minecraft:creeper`的战利品进行修改

具体更多的原版 JSON 可以查看[Predicate.md](../../LootTable/BasicKnowledge/Predicate.md)进行详情

```js
LootJS.modifiers((event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .customCondition({
            condition: "minecraft:weather_check",
            raining: true,
        })
        .addLoot("minecraft:diamond");
});
```
