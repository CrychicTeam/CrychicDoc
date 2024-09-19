# 战利品
在这章中会采用原生的KubeJs实现战利品的修改和添加，并不会使用LootJs进行修改战利品，LootJs的修改在模组篇章中

在原生kubeJs中，作者一共给我们提供了以下6种战利品修改或添加的方式

虽然说是六种，但其实战利品的添加都是差不多的

建议在写之前看看[战利品表](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8?variant=zh-cn)和[战利品谓词](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8?variant=zh-cn),有空的话也可以再额外看一个[物品修饰词](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8)

|   战利品事件调用                |     用途   |   添加  |  覆盖 |  用法 |
| :----------------------------: | :-------: | :----------: | :--------: | :-----------------------------------: |
| ServerEvents.genericLootTables | 全局战利品 | modify       | addGeneric | [全局战利品](./GlobalLootTable)   |
| ServerEvents.blockLootTables   | 方块战利品 | modifyBlock  | addBlock   | [方块战利品](./BlockLootTable)    |
| ServerEvents.entityLootTables  | 生物战利品 | modifyEntity | addEntity  | [生物战利品](./EntityLootTable)     |
| ServerEvents.giftLootTables    | 礼物战利品 | modify       | addGift    | [礼物战利品](./GiftLootTable)     |
| ServerEvents.fishingLootTables | 钓鱼战利品 | modify       | addFishing | [钓鱼战利品](./FishingLootTable)   |
| ServerEvents.chestLootTables   | 宝箱战利品 | modify       | addChest   | [宝箱战利品](./ChestLootTable) |

## LootBuilderPool通用方法
|                         方法名                          |                  参数                   |           用途           |      返回类型      |
| :-----------------------------------------------------: | :-------------------------------------: | :----------------------: | :----------------: |
|              `setUniformRolls(int1,int2)`               |  int1->最小抽取次数 int2->最大抽取次数  |    随机从奖池抽取次数    |        void        |
|               `addCondition(JsonObject)`                |                    ~                    |            ~             | ConditionContainer |
| `addConditionalFunction(Consumer<ConditionalFunction>)` |                    ~                    |            ~             | FunctionContainer  |
|                     `addEmpty(int)`                     |            int->空值占比权重            |     设置抽到空的权重     |   LootTableEntry   |
|                 `addEntry(JsonObject)`                  |                    ~                    |            ~             |   LootTableEntry   |
|                  `addItem(ItemStack)`                   |                   ->                    |         添加物品         |   LootTableEntry   |
|                `addItem(ItemStack,int)`                 |             int-> 权重占比              |         添加物品         |   LootTableEntry   |
|         `addItem(ItemStack,int,NumberProvider)`         |        NumberProvider-> 数量范围        |         添加物品         |   LootTableEntry   |
|            `addLootTable(ResourceLocation)`             |                   ->                    |  添加到其他的战利品表里  |   LootTableEntry   |
|                  `addTag(string,bool)`                  | string->tagId bool-> 是否从其中抽取一个 |    添加tag作为战利品     |   LootTableEntry   |
|          `randomChanceWithLooting(int1,int2)`           |      int1->chance int2->multiplier      |            ~             | ConditionContainer |
|                 `count(NumberProvider)`                 |                   ->                    |       设置数量范围       | FunctionContainer  |
|                `damage(NumberProvider)`                 |                   ->                    |      设置损坏值范围      | FunctionContainer  |
|          `enchantRandomly(ResourceLocation[])`          |                   ->                    |         随机附魔         | FunctionContainer  |
|        `enchantWithLevels(NumberProvider,bool)`         |                    ~                    |            ~             | FunctionContainer  |
|       `entityProperties(EntityTarget,JsonObject)`       |                    ~                    |            ~             | ConditionContainer |
|      `entityScores(EntityTarget,Map<string, any>)`      |                    ~                    |            ~             | ConditionContainer |
|                       `entries()`                       |                    -                    |     获取entries列表      |     JsonArray      |
|                   `killedByPlayer()`                    |                    -                    |     设置需要玩家击杀     | ConditionContainer |
|                   `randomChance(int)`                   |                   ->                    |            ~             | ConditionContainer |
|          `randomChanceWithLooting(int1,int2)`           |     int1-> chance int2-> multiplier     |            ~             | ConditionContainer |
|              `setBinomialRolls(int1,int2)`              |                    ~                    |            ~             |        void        |
|                  `survivesExplosion()`                  |                    -                    |            ~             | ConditionContainer |
|                    `furnaceSmelt()`                     |                    -                    | 掉落物品可被火焰附加熔炼 | FunctionContainer  |