# 服务端脚本事件列表

## 前言

- 放在server_script文件夹下生效。

## 事件列表

### ServerEvents 服务器事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   afterRecipes    |  配方加载完成后触发   |  -   |
|   blockLootTables  |    方块类型战利品表加载时触发，可用于增删改战利品表。   |  -   |
|   chestLootTables |   箱子类型战利品表加载时触发，可用于增删改战利品表。   |  -   |
|   command    |  命令执行时触发。   |  -   |
|   commandRegistry    |  命令注册时触发，可用于注册命令。   |  [注册命令](../ServerScript/EventExamples/CommandRegistry.md)   |
|   compostableRecipes    |  -   |  -   |
|   customCommand    |  -   |  -   |
|   entityLootTables    |  加载实体战利品表时触发。   |  [实体类型战利品表](../../LootTable/Entity.md)   |
|   fishingLootTables    |  加载钓鱼战利品表时触发。   |  [钓鱼类型战利品表](../../LootTable/Fish.md)   |
|   genericLootTables    |  加载普通战利品表时触发。   |  [通用类型战利品表](../../LootTable/Generic.md)   |
|   giftLootTables    |  加载礼物战利品表时触发。   |  [礼物类型战利品表](../../LootTable/Gift.md)   |
|   highPriorityData    |  加载高优先级数据包时触发。   |  -   |
|   loaded    |  服务器加载时触发。   |  -   |
|   lowPriorityData    |  加载低优先级数据包时触发。   |  -   |
|   recipes    |  加载配方时触发，可用于增删改配方。   |  -   |
|   specialRecipeSerializers    |  -   |  -   |
|   tags    |  加载标签时触发，可用于增删改标签。   |  -   |
|   tick    |  服务器每刻触发。   |  -   |
|   unloaded    |  服务器关闭时触发。   |  -   |

### ItemEvents 物品事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   canPickUp   |   -   |   -   |
|   crafted    |   物品被在工作台上合成。   |   -   |
|   destroyed   |   物品被摧毁。   |   -   |
|   dropped    |   物品掉落。   |   -   |
|   entityInteracted    |   实体交互。   |   -   |
|   firstLeftClicked    |   首次左键点击。   |   -   |
|   firstRightClicked   |   首次右键点击。   |   -   |
|   foodEaten   |   进食。   |   -   |
|   pickedUp    |   捡起。   |   -   |
|   rightClicked    |   右键点击。   |   -   |
|   smelted    |   被火焰灼烧。   |   -   |
|   tooltip    |   工具栏提示。   |   -   |

### BlockEvents 方块事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   broken   |   方块被玩家破坏时触发   |   -   |
|   detectorChanged   |   当在KubeJS中注册的检测器方块接受块更新充能/未充能时触发。   |   -   |
|   detectorPowered   |   当在KubeJS中注册的检测器方块接受块更新充能时调用。   |   -   |
|   detectorUnpowered   |   当在KubeJS中注册的检测器方块接受块更新未充能时调用。   |   -   |
|   farmlandTrampled   |   当一个实体试图践踏农田时调用。   |   -   |
|   leftClicked   |   当玩家左键点击方块时调用。   |   -   |
|   placed   |   -   |   当方块被放置时调用。   |
|   rightClicked   |   当玩家右键点击方块时调用。   |   -   |

### EntityEvents 实体事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   checkSpawn   |   由BaseSpawner或Chunk generation(待证实)生成的实体才会触发。   |   -   |
|   death   |   实体即将死亡（死亡之前）触发。   |   -   |
|   drops   |   -   |   -   |
|   hurt   |   实体即将受伤（受伤之前）触发。   |   -   |
|   spawned   |   实体即将被添加到世界时触发(包括从磁盘加载)。   |   -   |

### PlayerEvents 玩家事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   advancement   |   玩家获得进度时触发。   |   -   |
|   chat   |   玩家发送聊天消息时触发。   |   -   |
|   chestClosed   |   关闭箱子时触发。   |   -   |
|   chestOpened   |   打开箱子时触发。   |   -   |
|   decorateChat   |   装饰聊天消息时触发。   |   -   |
|   inventoryChanged   |   玩家背包物品发生改变时触发。   |   -   |
|   inventoryClosed   |   玩家关闭背包时触发。   |   -   |
|   inventoryOpened   |   玩家打开背包时触发。   |   -   |
|   loggedIn   |   玩家登录时触发。   |   -   |
|   loggedOut   |   玩家登出时触发。   |   -   |
|   respawned   |   玩家重生时触发。   |   -   |
|   tick   |   玩家每刻更新时触发。   |   -   |
