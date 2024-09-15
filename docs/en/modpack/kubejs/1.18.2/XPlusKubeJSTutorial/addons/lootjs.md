# 12.7 便捷战利品表修改(LootJS Forge)

## 一、概要

([LootJS Wiki](https://github.com/AlmostReliable/lootjs-forge/wiki) 已经非常详尽，本节仅做翻译 + 细微调整)

LootJS是一个KubeJS的附属mod，能够让你更加便捷地修改战利品表，掉落战利品时执行事件等

mod链接：[Github](https://github.com/AlmostReliable/lootjs-forge) [Curseforge](https://www.curseforge.com/minecraft/mc-mods/lootjs-forge)，许可：LGPL-3.0

由于战利品表是在服务端侧被处理的，所以显然你应该把它放到`kubejs\server_scripts`文件夹下

你可以通过`/reload`命令来重载LootJS的修改内容

推荐先阅读15章来了解物品的效果内容和实体，玩家修改内容

## 二、LootJS是如何工作的

下面这张图展示了游戏掉落物品的过程

![](https://m1.miaomc.cn/uploads/20220424\_31c09db2a2078.png)

## 三、基本格式

LootJS添加了一个KubeJS事件`lootjs`来修改战利品表，也就是这样：

```
onEvent("lootjs", (event) => {
    // 在此处编写代码
});
```

下面是一个基础的例子，使苦力怕有30%的概率掉落烈焰棒

```
onEvent("lootjs", (event) => {
	// 你把底下这个写成一行也行，除了可能会有点难看以外(
    event
        .addEntityLootModifier("minecraft:creeper")// 获取LootActionsBuilder
        .randomChance(0.3) // 战利品表条件：添加掉落概率
        .thenAdd("minecraft:blaze_rod");// 战利品表事件：掉落物品
});
```

每一个战利品表修改都应至少有一个战利品表事件

### 1、常用函数

函数`enableLogging()`会将修改过的战利品表打印到控制台便于排查问题

函数`.disableLootModification(战利品表)`会锁定指定战利品表以防修改

```
onEvent("lootjs", (event) => {
    // 启用战利品表输出
    event.enableLogging();
    
    // 禁用树叶的战利品表(通过正则表达式)
    event.disableLootModification(/.*:blocks\/.*_leaves/);
    
    // 禁用单个战利品表
    event.disableLootModification("minecraft:entities/bat");
});
```

LootJS提供了一些返回`LootActionsBuilder`对象的函数，这使修改掉落条件和掉落物成为可能。

| 函数                           | 功能                   |
| ---------------------------- | -------------------- |
| addBlockLootModifier(命名空间)   | 为方块添加新的战利品表修饰器       |
| addEntityLootModifier(命名空间)  | 为实体添加新的战利品表修饰器       |
| addLootTableModifier(命名空间ID) | 为给定战利品表添加新的修饰器       |
| addLootTypeModifier(战利品表类型)  | 为给定战利品表类型\[1]添加新的修饰器 |
| getGlobalModifiers()         | 返回包含所有战利品表修饰器的列表     |
| removeGlobalModifier(值)\[2]  | 移除给定的战利品表修饰器         |

\[1]以下是可用的战利品表类型(LootJS)的值

```
LootType.UNKNOWN // 未知
LootType.BLOCK // 方块
LootType.ENTITY // 实体
LootType.CHEST // 战利品箱
LootType.FISHING // 钓鱼
LootType.GIFT // 礼物(村民等)
```

\[2]：你可以使用形如`@modid`来移除指定mod的全局战利品表修改，使用形如`examplemod:example_loot_change`的战利品表ID来移除指定战利品表，此外，该函数仅对全局战利品表生效，不能阻止mod直接将其物品添加到战利品表中

### 2、掉落物修改(战利品表事件)

**战利品表事件**用于修改战利品表的掉落物或为其添加不同效果。每一个战利品表修饰器都至少包含一个战利品表事件

**thenAdd(物品)** 可以向当前战利品表修饰器中添加物品

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .thenAdd("minecraft:flint");// 返回方块的战利品表修饰器并添加单个物品
});
```

**thenRemove(物品)** 将移除战利品表在所有符合给定条件的物品

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .thenRemove('#forge:gunpowder')// 从苦力怕的战利品表中移除所有#forge:gunpowder
});
```

**thenReplace(被替换物品, 替换物品)** 将战利品表中符号条件的物品全部替换为给定物品

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .thenReplace('#forge:gunpowder', 'minecraft:diamond')// 现在这只苦力怕会掉钻石了
});
```

**thenModify(物品, 回调)** 对于当前随机池的所有符合条件的物品，一个回调将被调用。LootJS会把该物品传给回调来修改它并返回该物品。记住一定要有返回语句。

```
onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier(LootType.ENTITY) 
        .weatherCheck({
            raining: true,
        })
        .thenModify(Ingredient.getAll(), (itemStack) => {
            return itemStack.withCount(itemStack.getCount() * 2);// 返回语句
        });
});
```

在这个例子中，下雨时实体的掉落物品数量将翻倍

**thenExplode(范围, 是否损坏方块, 是否引火)** 可以在掉落战利品时生成一个爆炸，而掉落物不会被炸毁。范围可以是任何数字而后两个参数（是否损坏方块, 是否引火）应为布尔值

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .thenExplode(1, false, false);// ExplosionJS详见15.3
});
```

**thenLightningStrike(是否破坏方块)** 可以在掉落战利品时生成一个闪电。同样，物品不会被破坏。

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .thenLightningStrike(false);
});
```

**thenApply(回调)** 可以在掉落战利品时进行回调，非常有意思的一个功能。

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .thenApply((context) => {
            // 有关玩家(Player)，实体(EntityJS)，方块(BlockContainerJS)等还请查看15章。
            // 回调函数
            // if(context.getPlayer()!=null){context.getPlayer().tell("测试")}
        });
});
```

可用函数（推荐先阅读15章）见文末LootContextJS

### 3、战利品表掉落条件

除了战利品表事件，你还可以为战利品表掉落添加条件限制

**值得注意的是，这些条件仅对新增的战利品修饰器生效**

**matchLoot(物品, 可选 是否全部比对)** 可以在符合条件的物品掉落时执行修改。是否全部比对的默认值为false，当为true时，当前战利品池中所有的战利品必须符合条件才能执行修改

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:cow")
        .matchLoot("minecraft:leather")
        .thenAdd("minecraft:carrot");// 在牛掉落皮革时再多掉落一个胡萝卜
});
```

**matchMainHand(IngredientJS 物品)** 当玩家主手手持指定物品时才执行修改。

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchMainHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .thenAdd("minecraft:gravel");// 手持下界合金斧(忽略nbt)时添加掉落物砂砾
});
```

**matchOffHand(IngredientJS 物品)** 当玩家副手手持指定物品时才执行修改。

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("#forge:ores")
        .matchOffHand(Item.of("minecraft:netherite_pickaxe").ignoreNBT())
        .thenAdd("minecraft:gravel");
});
```

**survivesExplosion()** 可以防止修改的掉落物被爆炸破坏

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .survivesExplosion()
        .thenAdd("minecraft:gravel");
});
```

\*\*timeCheck(period, min, max)\*\*\*和 **timeCheck(min, max)** 使修改内容在符合指定游戏刻条件下才能生效

\*当前游戏时间除以period值所得的余数来与value匹配。(若period被设置为100，value被设置为1，则时间为1/101/201……时通过)，min和max则为数值范围（开区间）

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .timeCheck(24000, 0, 9000) // 只有白天才会掉落
        .thenAdd("minecraft:diamond");
});
```

**weatherCheck(值)** 可以为战利品表添加天气条件。其中值的格式：

```
{
    raining: true, // 雨天天气
    thundering: true // 雷雨天气
}
```

如果你只使用一个值，则另一个会被忽略

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .weatherCheck({
            raining: true, // 只使用一个值
        })
        .thenAdd("minecraft:diamond");
});
```

**randomChance(浮点型 随机值)** 可以概率执行掉落物品修改

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChance(0.3) // 30%
        .thenAdd("minecraft:diamond");
});
```

**randomChanceWithLooting(浮点型 概率, 整形 附魔等级)** 可以根据抢夺等级概率调整掉落概率

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChanceWithLooting(0.3, 2) // 30% 
        .thenAdd("minecraft:diamond");
});
```

**randomChanceWithEnchantment(字符串 附魔ID, \[概率])** 可以根据给定附魔的等级调整概率

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .randomChanceWithEnchantment("minecraft:looting", [0, 0.1, 0.5, 1]) 
        .thenAdd("minecraft:diamond");
    
    /*
        [0, 0.1, 0.5, 1]:
          没有抢夺附魔时掉落概率为 0%
          抢夺 I 时掉落概率为    10%
          抢夺 II 时掉落概率为   50%
          抢夺 III 时掉落概率为  100%
          
    */
});
```

**biome(...字符串 群系ID)** 当_所有_群系均符合时执行修改。你可以传入群系标签(形如`#cold`)来限定群系必须为寒冷群系（见6 自定义世界生成）

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .biome("minecraft:jungle") 
        .thenAdd("minecraft:diamond");// 丛林群系才会掉落物品
});
```

**anyBiome(...字符串 群系ID)** 当_任意_群系符合时即执行修改。你可以传入群系标签(形如`#cold`)来限定群系必须为寒冷群系

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyBiome("minecraft:jungle", "minecraft:ocean") 
        .thenAdd("minecraft:diamond");// 在丛林或海洋时掉落
});
```

**anyDimension(...字符串 维度ID)** 当_任意_维度符合时即执行修改。

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyDimension("minecraft:nether") 
        .thenAdd("minecraft:diamond");
});
```

**anyStructure(\[字符串 结构ID], 布尔值 是否精确匹配)** 可以判断战利品掉落于指定结构中。当精确匹配的值为true时，玩家在结构的建筑内才生效；为false时则在结构范围内即可

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .anyStructure(["minecraft:stronghold", "minecraft:village"], false) 
        .thenAdd("minecraft:diamond");// 在给定结构范围内即掉落
});
```

**lightLevel(min, max)** 在给定光照强度范围内才执行修改（开区间）

```
onEvent("lootjs", (event) => {
    event
        .addBlockLootModifier("minecraft:gravel")
        .lightLevel(0, 15) 
        .thenAdd("minecraft:diamond");
});
```

**killedByPlayer()** 只有被玩家杀死时才执行修改

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:iron_golem")
        .thenRemove("#forge:ingots/iron");// 先移除再添加
    event
        .addEntityLootModifier("minecraft:iron_golem")
        .killedByPlayer() 
        .thenAdd(Item.of("minecraft:iron_ingot").withCount(2));// 只有玩家杀死铁傀儡时才掉落铁锭，干掉刷铁机（
});
```

**matchEntity(callback)** 可以匹配触发战利品表的实体（如死亡的 / 打开箱子 / 破坏方块的），当条件符合时执行修改

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchEntity((entity) => {
            // EntityPredicateBuilderJS（见文末）
        }) 
        .thenAdd("minecraft:diamond");
});
```

**matchDirectKiller(callback)** 可以匹配直接击杀目标的实体（例如弓箭实体而不是发射的实体），当条件符合时执行修改

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchKiller((entity) => {
            // EntityPredicateBuilderJS（见文末）
        }) 
        .thenAdd("minecraft:diamond");
});
```

**matchPlayer(callback)** 可以匹配玩家，当条件符合时执行修改。当玩家杀死了另一个玩家时，将以击杀者作为匹配对象

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchPlayer((player) => {
            // EntityPredicateBuilderJS（见文末）
        }) 
        .thenAdd("minecraft:diamond");
});
```

**matchDamageSource(callback)** 可以匹配伤害类型，当条件符合时执行修改

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .matchDamageSource((source) => {
            // DamageSourcePredicateBuilderJS（见文末）
        }) 
        .thenAdd("minecraft:diamond");
});
```

**distanceToKiller(IntervalJS 距离)** 可以检测实体掉落与击杀者之间的距离

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .distanceToKiller(Interval.min(25))/*IntervalJS (见文末)*/ 
        .thenAdd("minecraft:diamond");
});
```

**hasAnyStage(...字符串 Gamestage)** 可以检测玩家是否具有指定Gamestage，当具有_任意一个_时执行修改

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .hasAnyStage("stoneage")
        .thenAdd("minecraft:coal");// 玩家具有stoneage时掉落一个煤炭
});
```

**playerPredicate(callback)，entityPredicate(callback)，killerPredicate(callback)，directKillerPredicate(callback)** 可以自定义回调函数来匹配玩家，实体，击杀者，直接击杀者，当条件符合时执行修改。_注意：该回调必须返回true或false_

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .playerPredicate((player) => player.stages.has("stoneage"))
        .thenAdd("minecraft:emerald");
});

onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .entityPredicate((entity) => entity.type == "minecraft:pig")
        .thenAdd("minecraft:diamond");
});

onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .killerPredicate((entity) => entity.type == "minecraft:pig")
        .thenAdd("minecraft:feather");
});

onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:pig")
        .directKillerPredicate((entity) => entity.type == "minecraft:pig")
        .thenAdd("minecraft:stone");
});
```

**not(callback)** 可以反转战利品表条件

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .not((n) => {
            n.biome("minecraft:jungle")
        })
        .thenAdd("minecraft:diamond");// 当群系不是丛林时掉落钻石
});
```

**or(callback)** 可以同时添加多个条件，当_一个条件为真_时即通过

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .or((or) => {
            or.biome("minecraft:jungle").anyDimension("minecraft:nether");
        })
        .thenAdd("minecraft:diamond");// 群系为丛林**或**维度为下界时掉落钻石
});
```

**and(callback)** 可以同时添加多个条件，当_所有条件为真_时即通过

```
onEvent("lootjs", (event) => {
    event
        .addEntityLootModifier("minecraft:creeper")
        .and((and) => {
            and.biome("minecraft:jungle").distanceToKiller(Interval.min(25));
        })
        .thenAdd("minecraft:diamond");// 群系为丛林**且**击杀者距离大于25时掉落钻石
});
```

### 4、IntervalJS，EntityPredicateBuilderJS，DamageSourcePredicateBuilderJS和LootContextJS

**IntervalJS**可以用来表示两个数字之间的范围。注意：以下所有范围均为**开区间**

| 格式                         | 描述                        | 返回值        |
| -------------------------- | ------------------------- | ---------- |
| Interval.between(min, max) | 返回最小最大值之间的范围 `(min, max)` | IntervalJS |
| Interval.min(min)          | 返回只限制了最小值的范围 `(min,+∞)`   | IntervalJS |
| Interval.max(max)          | 返回只限制了最大值的范围 `(-∞,max)`   | IntervalJS |
| **函数**                     | **描述**                    | **返回值**    |
| matches(值)                 | 检测值是否在范围内                 | 布尔值        |
| matchesSqr(值)              | 检测值是否在范围内                 | 布尔值        |

**EntityPredicateBuilderJS** 可以用条件限制实体以判断是否执行修改，以下返回值均为EntityPredicateBuilderJS

| 函数                                               | 描述                             |
| ------------------------------------------------ | ------------------------------ |
| anyType(...类型)                                   | 判断实体是否有给定标签(输入示例:`#skeletons`) |
| isOnFire(布尔值 条件真假)                               | 判断实体是否着火                       |
| isCrouching(布尔值 条件真假)                            | 判断实体是否正在潜行                     |
| isSprinting(布尔值 条件真假)                            | 判断实体是否正在疾跑                     |
| isSwimming(布尔值 条件真假)                             | 判断实体是否正在游泳                     |
| isBaby(布尔值 条件真假)                                 | 判断实体是否为幼年状态                    |
| isInWater(布尔值 条件真假)                              | 判断实体是否在水里                      |
| isMonster(布尔值 条件真假)                              | 判断实体是否为敌对生物                    |
| isCreature(布尔值 条件真假)                             | 判断实体是否为友好生物                    |
| isOnGround(布尔值 条件真假)                             | 判断实体是否在地上                      |
| isUndeadMob(布尔值 条件真假)                            | 判断实体是否为亡灵生物                    |
| isArthropodMob(布尔值 条件真假)                         | 判断实体是否为节肢动物                    |
| isWaterMob(布尔值 条件真假)                             | 判断实体是否为水生动物                    |
| isIllegarMob(布尔值 条件真假)                           | 判断实体是否为掠夺生物                    |
| hasEffect(字符串 效果ID, 整形 等级) 和 hasEffect(字符串 效果ID) | 判断实体是否具有给定效果                   |
| nbt(json)                                        | 判断实体是否具有指定nbt                  |
| matchMount(回调)                                   | 判断实体所骑乘的实体\[1]                 |
| matchTargetedEntity(回调)                          | 判断实体的目标实体\[1]                  |
| matchSlot(整形 栏位编号, IngredientJS 物品)              | 判断实体给定物品栏栏位的物品                 |

\[1]：提供EntityPredicateBuilderJS

例子：

```
onEvent("lootjs", (event) => {
    event
        .addLootTypeModifier([LootType.ENTITY])
        .matchEntity((entity) => {
            entity.anyType("#skeletons");
            entity.matchMount((mount) => {
                mount.anyType("minecraft:spider");
            });
        })
        .thenAdd("minecraft:magma_cream");// 当骷髅类实体骑乘蜘蛛时掉落一个演讲稿
});
```

**DamageSourcePredicateBuilderJS** 用条件来限制伤害类型以判断是否执行修改

| 函数                                  | 描述           |
| ----------------------------------- | ------------ |
| anyType(...类型)                      | 判断伤害是否符合类型   |
| isProjectile(布尔值 条件真假)              | 判断伤害是否为投掷物   |
| isExplosion(布尔值 条件真假)               | 判断伤害是否为爆炸    |
| doesBypassArmor(布尔值 条件真假)           | 判断伤害是否无视盔甲   |
| doesBypassInvulnerability(布尔值 条件真假) | 判断伤害是否无视隐身效果 |
| doesBypassMagic(布尔值 条件真假)           | 判断伤害是否为饥饿引起  |
| isFire(布尔值 条件真假)                    | 判断伤害是否为火     |
| isMagic(布尔值 条件真假)                   | 判断伤害是否为饥饿    |
| isLightning(布尔值 条件真假)               | 判断伤害是否为闪电    |
| matchDirectEntity(回调)               | 判断直接造成伤害的实体  |
| matchSourceEntity(回调)               | 判断造成伤害的实体源   |

\[2]：提供DamageSourcePredicateBuilderJS

| 函数                           | 描述                     | 返回值              |
| ---------------------------- | ---------------------- | ---------------- |
| getType()                    | 返回战利品类型                | LootTypes\[5]    |
| getPosition()                | 返回掉落位置                 | Vector3d         |
| getEntity()                  | 返回具有当前战利品表的实体(可能为null) | EntityJS         |
| getKillerEntity()            | 返回杀死对象的实体(可能为null)     | EntityJS         |
| getPlayer()                  | 返回杀死对象的玩家(可能为null\[6]) | PlayerJS         |
| getDamageSource()            | 返回伤害类型(可能为null\[6])    | DamageSourceJS   |
| getTool()                    | 返回触发战利品表使用的工具(可能为null) | ItemStackJS      |
| getDestroyedBlock()          | 返回被破坏的方块(可能为null)      | BlockContainerJS |
| isExploded()                 | 返回战利品表是否被爆炸触发          | 布尔值              |
| getExplosionRadius()         | 返回触发的爆炸的半径\[7]         | 整形               |
| getLevel()                   | 返回WorldJS              | WorldJS          |
| getServer()                  | 返回ServerJS             | ServerJS         |
| getLuck()                    | 返回玩家幸运值                | 整形               |
| getLooting()                 | 返回LootingModifier      | 整形               |
| getRandom()                  | 返回范围(?)                | Random           |
| addLoot(ItemStackJS 物品)      | 添加掉落物                  | void             |
| removeLoot(IngredientJS 物品)  | 移除掉落物                  | void             |
| findLoot(IngredientJS 物品)    | 返回掉落物列表                | List             |
| hasLoot(IngredientJS 物品)     | 返回是否掉落指定物品             | 布尔值              |
| forEachLoot(Consumer action) | 遍历每个物品并给予回调\[8]        | void             |

\[5]：见 本章 常用函数 部分

\[6]：为null的例子：骷髅射杀苦力怕

\[7]：如果不是则为0

\[8]：例如

```
const callback = (item) => {
    console.log(item);
};
```
