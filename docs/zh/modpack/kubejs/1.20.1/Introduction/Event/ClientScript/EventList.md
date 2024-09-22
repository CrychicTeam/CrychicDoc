# 客户端脚本事件列表

## 前言

- 放在client_script文件夹下生效。

## 事件列表

### ClientEvents 客户端事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   atlasSpriteRegistry   |   -   |   -   |
|   highPriorityAssets   |   高优先级资源包加载时触发。   |   -   |
|   lang   |   语言文件加载时触发。   |   -   |
|   leftDebugInfo   |   在呈现左侧调试栏时调用。   |   -   |
|   loggedIn   |   登录时触发。   |   -   |
|   loggedOut   |   登出时触发。   |   -   |
|   paintScreen   |   渲染paint时触发。   |   -   |
|   painterUpdated   |   paint更新时触发。   |   -   |
|   rightDebugInfo   |   在呈现右侧调试栏时调用。   |   -   |
|   tick   |   每刻触发   |   -   |

### ItemEvents 物品事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   rightClicked   |   当玩家右键点击物品而没有瞄准任何东西时调用。   |   -   |
|   crafted   |   当玩家在工作台制作物品时调用。   |   -   |
|   dropped   |   当玩家掉落物品时调用。   |   -   |
|   tooltip   |   在为物品工具提示注册处理程序时调用。   |   -   |
|   firstRightClicked   |   当玩家右键点击物品而没有瞄准任何东西时调用。   |   -   |
|   pickedUp    |   当玩家拾取道具时（拾取之后）调用。   |   -   |
|   destroyed   |   物品被摧毁时调用。   |   -   |
|   entityInteracted    |   当玩家右键点击实体时调用。   |   -   |
|   foodEaten   |   当实体吃食物时调用。   |   -   |
|   canPickUp   |   当玩家拾取物品时（拾取之前）调用。   |   -   |
|   smelted    |   当物品被玩家熔炼时调用。。   |   -   |

### EntityEvents 实体事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   spawned   |   实体即将被添加到世界时触发(包括从磁盘加载)。   |   -   |
|   drops   |   -   |   -   |
|   checkSpawn   |   由BaseSpawner或Chunk generation(待证实)生成的实体才会触发。   |   -   |
|   death   |   实体即将死亡（死亡之前）触发。   |   -   |
|   hurt   |   实体即将受伤（受伤之前）触发。   |   -   |

### LevelEvents 维度事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   beforeExplosion   |   在爆炸发生前调用。   |   -   |
|   tick   |   每刻调用。   |   -   |
|   afterExplosion   |   爆炸发生后调用。   |   -   |

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

### PlayerEvents 玩家事件

|   事件名称    |   用途    |   示例    |
|:------------:|:---------:|:---------:|
|   chestClosed   |   当玩家关闭箱子时调用。   |   -   |
|   chestOpened   |   当玩家打开箱子时调用。   |   -   |
|   inventoryChanged   |   当玩家的背包物品改变时调用。   |   -   |
|   inventoryClosed   |   当玩家关闭容器时调用。   |   -   |
|   inventoryOpened   |   当玩家打开容器时调用。   |   -   |
|   tick   |   玩家每刻更新时触发。   |   -   |
