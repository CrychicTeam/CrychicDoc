# 战利品表类型

- **`战利品表类型`** 指的是战利品表触发过程中传递的参数集的类型，因此成为战利品表上下文类型，简称战利品表类型。

## 战利品表上下文

- 战利品上下文（Loot Context）是一些用于战利品表、谓词、物品修饰器以及数值提供器的参数构成的集合。

## 战利品表类型列表

- 参见[Minecraft-wiki/战利品上下文#参数集](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E4%B8%8A%E4%B8%8B%E6%96%87#%E5%8F%82%E6%95%B0%E9%9B%86)

|   战利品表类型    |   用途    |   KubeJS事件    |   KubeJS示例    |
|:------------:|:---------:|:---------:|:---------:|
|   方块    |    方块被破坏时触发。    |   ServerEvents.blockLootTables   |   [方块类型战利品表](../Vanilla/Block.md)   |
|   实体    |    当打开带有战利品表的容器时触发。    |   ServerEvents.entityLootTables   |   [实体类型战利品表](../Vanilla/Entity.md)   |
|   钓鱼    |    生物死亡时触发。    |   ServerEvents.fishingLootTables   |   [钓鱼类型战利品表](../Vanilla/Fish.md)   |
|   礼物    |    当猫或村民给予玩家礼物、嗅探兽刨挖时触发。    |   ServerEvents.fishingLootTables   |   [礼物类型战利品表](../Vanilla/Gift.md)   |
|   箱子    |    当打开带有战利品表的容器时触发。    |   ServerEvents.chestLootTables   |   [箱子类型战利品表](../Vanilla/Chest.md)   |
|   通用    |    -    |   ServerEvents.genericLootTables   |   [通用类型战利品表](../Vanilla/Generic.md)   |
