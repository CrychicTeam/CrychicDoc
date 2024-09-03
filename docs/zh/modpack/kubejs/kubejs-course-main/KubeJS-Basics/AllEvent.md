# 所有事件
本章中将会列举出所有事件和对应的示例，其中包括kubejs提供的和模组提供的

# kubejs提供的所有事件
下面是kubejs模组提供的所有可供调用的事件，需要注意存放的文件夹

写法为:`主事件.子事件`
## 启动时文件夹下startup_scripts
|     主事件     |      子事件       |       用处       |             示例             |
| :------------: | :---------------: | :--------------: | :--------------------------: |
| StartupEvents  |       init        |  游戏初始化事件  |              -               |
| StartupEvents  |     registry      | 注册各种游戏内容 | [链接](../KubeJS-Basics/Customs/README.md) |
| WorldgenEvents |        add        | 世界生成添加事件 |              -               |
| WorldgenEvents |      remove       | 世界生成删除事件 |              -               |
|   ItemEvents   |   modification    |   修改物品属性   |              -               |
|   ItemEvents   | toolTierRegistry  |        -         |              -               |
|   ItemEvents   | armorTierRegistry |        -         |              -               |
|   ItemEvents   |  modelProperties  |        -         |              -               |
|  BlockEvents   |   modification    |   修改方块属性   |              -               |

## 服务端文件夹下server_scripts
|    主事件     |          子事件          |         用处          |                      示例                      |
| :-----------: | :----------------------: | :-------------------: | :--------------------------------------------: |
| ServerEvents  |     lowPriorityData      |           -           |                       -                        |
| ServerEvents  |     highPriorityData     |           -           |                       -                        |
| ServerEvents  |          loaded          |    服务器重载事件     |                       -                        |
| ServerEvents  |         unloaded         |           -           |                       -                        |
| ServerEvents  |           tick           |     游戏tick事件      |                       -                        |
| ServerEvents  |           tags           |           -           |                       -                        |
| ServerEvents  |     commandRegistry      |     指令注册事件      |                       -                        |
| ServerEvents  |         command          |    服务器指令事件     |                       -                        |
| ServerEvents  |      customCommand       |      自定义指令       |  [链接](../kjs-jin-jie/zi-ding-yi-zhi-ling)   |
| ServerEvents  |         recipes          |       配方事件        |      [链接](./ji-chu-xie-fa-tian-jia)       |
| ServerEvents  |       afterRecipes       | 配方事件后处理(有bug) |                       -                        |
| ServerEvents  | specialRecipeSerializers |           -           |                       -                        |
| ServerEvents  |    compostableRecipes    |           -           |                       -                        |
| ServerEvents  |    recipeTypeRegistry    |           -           |                       -                        |
| ServerEvents  |    genericLootTables     |    全局战利品事件     |  [链接](./LootTables/quan-ju-zhan-li-pin)  |
| ServerEvents  |     blockLootTables      |    方块战利品事件     |  [链接](./LootTables/fang-kuai-diao-luo)   |
| ServerEvents  |     entityLootTables     |    实体战利品事件     |   [链接](./LootTables/sheng-wu-diao-luo)   |
| ServerEvents  |      giftLootTables      |  村民礼物战利品事件   |   [链接](./LootTables/li-wu-zhan-li-pin)   |
| ServerEvents  |    fishingLootTables     |    钓鱼战利品事件     |  [链接](./LootTables/diao-yu-zhan-li-pin)  |
| ServerEvents  |     chestLootTables      |    宝箱战利品事件     | [链接](./LootTables/bao-xiang-zhan-li-pin) |
|  LevelEvents  |          loaded          |     世界加载事件      |                       -                        |
|  LevelEvents  |         unloaded         |           -           |                       -                        |
|  LevelEvents  |           tick           |     世界tick事件      |                       -                        |
|  LevelEvents  |     beforeExplosion      |           -           |                       -                        |
|  LevelEvents  |      afterExplosion      |           -           |                       -                        |
| NetworkEvents |        fromClient        |           -           |                       -                        |
|  ItemEvents   |       rightClicked       |     物品右键事件      |                       -                        |
|  ItemEvents   |        canPickUp         |           -           |                       -                        |
|  ItemEvents   |         pickedUp         |     物品捡起事件      |                       -                        |
|  ItemEvents   |         dropped          |     物品丢弃事件      |                       -                        |
|  ItemEvents   |     entityInteracted     |           -           |                       -                        |
|  ItemEvents   |         crafted          |           -           |                       -                        |
|  ItemEvents   |         smelted          |           -           |                       -                        |
|  ItemEvents   |        foodEaten         |   食物物品食用事件    |                       -                        |
|  ItemEvents   |    firstRightClicked     |           -           |                       -                        |
|  ItemEvents   |     firstLeftClicked     |           -           |                       -                        |
|  BlockEvents  |       rightClicked       |     方块右键事件      |                       -                        |
|  BlockEvents  |       leftClicked        |     方块左键事件      |                       -                        |
|  BlockEvents  |          placed          |     方块放置事件      |                       -                        |
|  BlockEvents  |          broken          |     方块破坏事件      |                       -                        |
|  BlockEvents  |     detectorChanged      |           -           |                       -                        |
|  BlockEvents  |     detectorPowered      |           -           |                       -                        |
|  BlockEvents  |    detectorUnpowered     |           -           |                       -                        |
|  BlockEvents  |     farmlandTrampled     |           -           |                       -                        |
| EntityEvents  |          death           |     实体死亡事件      |                       -                        |
| EntityEvents  |           hurt           |     实体受伤事件      |                       -                        |
| EntityEvents  |        checkSpawn        |           -           |                       -                        |
| EntityEvents  |         spawned          |     实体生成事件      |                       -                        |
| PlayerEvents  |         loggedIn         |   玩家登入游戏事件    |                       -                        |
| PlayerEvents  |        loggedOut         |   玩家退出游戏事件    |                       -                        |
| PlayerEvents  |        respawned         |     玩家重生事件      |                       -                        |
| PlayerEvents  |           tick           |     玩家tick事件      |                       -                        |
| PlayerEvents  |           chat           |     玩家聊天事件      |                       -                        |
| PlayerEvents  |       decorateChat       |           -           |                       -                        |
| PlayerEvents  |       advancement        |           -           |                       -                        |
| PlayerEvents  |     inventoryOpened      |   玩家打开背包事件    |                       -                        |
| PlayerEvents  |     inventoryClosed      |   玩家关闭背包事件    |                       -                        |
| PlayerEvents  |     inventoryChanged     |   玩家背包变化事件    |                       -                        |
| PlayerEvents  |       chestOpened        |   玩家打开箱子事件    |                       -                        |
| PlayerEvents  |       chestClosed        |   玩家关闭箱子事件    |                       -                        |

## 客户端文件夹下client_scripts
|    主事件     |       子事件       |      用处      |            示例            |
| :-----------: | :----------------: | :------------: | :------------------------: |
| ClientEvents  | highPriorityAssets |       -        |             -              |
| ClientEvents  |        init        |       -        |             -              |
| ClientEvents  |      loggedIn      |       -        |             -              |
| ClientEvents  |     loggedOut      |       -        |             -              |
| ClientEvents  |        tick        | 客户端tick事件 |             -              |
| ClientEvents  |   painterUpdated   |       -        |             -              |
| ClientEvents  |   leftDebugInfo    |       -        |             -              |
| ClientEvents  |   rightDebugInfo   |       -        |             -              |
| ClientEvents  |    paintScreen     |       -        |             -              |
| NetworkEvents |     fromServer     |       -        |             -              |
|  ItemEvents   |      tooltip       |    物品提示    | [链接](./wu-pin-ti-shi) |
|  ItemEvents   | clientRightClicked |       -        |             -              |
|  ItemEvents   | clientLeftClicked  |       -        |             -              |

# 模组提供的事件