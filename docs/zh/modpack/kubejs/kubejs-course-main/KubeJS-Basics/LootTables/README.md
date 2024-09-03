# 战利品
在这章中会采用原生的KubeJs实现战利品的修改和添加，并不会使用LootJs进行修改战利品，LootJs的修改在模组篇章中

在原生kubeJs中，作者一共给我们提供了以下6种战利品修改或添加的方式

虽然说是六种，但其实战利品的添加都是差不多的

建议在写之前看看[战利品表](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8?variant=zh-cn)和[战利品谓词](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8?variant=zh-cn),有空的话也可以再额外看一个[物品修饰词](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8)

|   战利品事件调用                |     用途   |   添加  |  覆盖 |  用法 |
| :----------------------------: | :-------: | :----------: | :--------: | :-----------------------------------: |
| ServerEvents.genericLootTables | 全局战利品 | modify       | addGeneric | [全局战利品](./quan-ju-zhan-li-pin)   |
| ServerEvents.blockLootTables   | 方块战利品 | modifyBlock  | addBlock   | [方块战利品](./fang-kuai-diao-luo)    |
| ServerEvents.entityLootTables  | 生物战利品 | modifyEntity | addEntity  | [生物战利品](./sheng-wu-diao-luo)     |
| ServerEvents.giftLootTables    | 礼物战利品 | modify       | addGift    | [礼物战利品](./li-wu-zhan-li-pin)     |
| ServerEvents.fishingLootTables | 钓鱼战利品 | modify       | addFishing | [钓鱼战利品](./diao-yu-zhan-li-pin)   |
| ServerEvents.chestLootTables   | 宝箱战利品 | modify       | addChest   | [宝箱战利品](./bao-xiang-zhan-li-pin) |

