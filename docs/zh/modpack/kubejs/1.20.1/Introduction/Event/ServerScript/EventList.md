# 服务端脚本事件列表

## 前言

- 放在server_script文件夹下生效。

## 事件列表

### ServerEvent 服务器事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   afterRecipes    |  配方加载完成后触发   |  -   |
|   blockLootTables  |    方块类型战利品表加载时触发，可用于增删改战利品表   |  -   |
|   chestLootTables |   箱子类型战利品表加载时触发，可用于增删改战利品表   |  -   |
|   command    |  命令执行时触发   |  -   |
|   commandRegistry    |  命令注册时触发，可用于注册命令   |  [注册命令](../ServerScript/EventExamples/CommandRegistry.md)   |
|   compostableRecipes    |  -   |  -   |
|   customCommand    |  -   |  -   |
|   entityLootTables    |  加载实体战利品表时触发   |  -   |
|   fishingLootTables    |  加载钓鱼战利品表时触发   |  -   |
|   genericLootTables    |  加载普通战利品表时触发   |  -   |
|   giftLootTables    |  加载礼物战利品表时触发   |  -   |
|   highPriorityData    |  加载高优先级数据包时触发   |  -   |
|   loaded    |  服务器加载时触发   |  -   |
|   lowPriorityData    |  加载低优先级数据包时触发   |  -   |
|   recipes    |  加载配方时触发，可用于增删改配方   |  -   |
|   specialRecipeSerializers    |  -   |  -   |
|   tags    |  加载标签时触发，可用于增删改标签   |  -   |
|   tick    |  服务器每刻触发   |  -   |
|   unloaded    |  服务器关闭时触发   |  -   |

### ItemEvent 物品事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   armorTierRegistry   |   -   |   -   |
|   canPickUp   |   -   |   -   |
|   crafted    |   -   |   -   |
|   destroyed   |   -   |   -   |
|   dropped    |   -   |   -   |
|   entityInteracted    |   -   |   -   |
|   firstLeftClicked    |   -   |   -   |
|   firstRightClicked   |   -   |   -   |
|   foodEaten   |   -   |   -   |
|   modelProperties    |   -   |   -   |
|   modification    |   -   |   -   |
|   pickedUp    |   -   |   -   |
|   rightClicked    |   -   |   -   |
|   smelted    |   -   |   -   |
|   toolTierRegistry    |   -   |   -   |
|   tooltip    |   -   |   -   |
