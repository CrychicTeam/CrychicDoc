---
authors: ['Gu-meng']
---
# KubeJS Additions事件介绍
在KubeJS Additions(下面简称kubejs拓展)中额外提供了许多事件，下面为大家列举出来
## 事件列表
|      主事件       |              子事件               |   所在文件夹    |         用处          | 示例  | 测试是否有效 |
| :---------------: | :-------------------------------: | :-------------: | :-------------------: | :---: | :----------: |
|  JEIAddedEvents   |      registerRecipeCatalysts      | client_scripts  |  注册jei配方种类事件  |   ~   |      ~       |
|  JEIAddedEvents   |          registerRecipes          | client_scripts  |    注册jei配方事件    |   ~   |      ~       |
|  JEIAddedEvents   | registerVanillaCategoryExtensions | client_scripts  | 注册原版种类扩展事件? |   ~   |      ~       |
|  JEIAddedEvents   |        registerIngredients        | client_scripts  |     注册材料事件?     |   ~   |      ~       |
|  JEIAddedEvents   |        onRuntimeAvailable         | client_scripts  |     可用于运行时?     |   ~   |      ~       |
|  JEIAddedEvents   |        registerGUIHandlers        | client_scripts  |    注册GUI处理事件    |   ~   |      ~       |
|  JEIAddedEvents   |       registerItemSubtypes        | client_scripts  |    注册物品子类型     |   ~   |      ~       |
|  JEIAddedEvents   |         registerAdvanced          | client_scripts  |       高级注册?       |   ~   |      ~       |
|  JEIAddedEvents   |       registerFluidSubtypes       | client_scripts  |    注册流体子类型     |   ~   |      ~       |
|  JEIAddedEvents   |        registerCategories         | client_scripts  |       注册种类?       |   ~   |      ~       |
|  JEIAddedEvents   |  registerRecipeTransferHandlers   | client_scripts  |   注册配方传输处理?   |   ~   |      ~       |
|    JadeEvents     |       onCommonRegistration        | startup_scripts |           ~           |   ~   |      ~       |
|    JadeEvents     |       onClientRegistration        | client_scripts  |           ~           |   ~   |      ~       |
|    ArchEvents     |             registry              | startup_scripts |    Arch的注册事件     |   ~   |      ~       |
|    ArchEvents     |           handleStartup           | startup_scripts |   Arch的启动端事件    |   ~   |      ~       |
|    ArchEvents     |           handleServer            | server_scripts  |   Arch的服务端事件    |   ~   |      ~       |
|    ArchEvents     |           handleClient            | client_scripts  |  Arch的客户端端事件   |   ~   |      ~       |
| CommonAddedEvents |            entityTame             | server_scripts  |     驯服实体事件      |   ~   |      √       |
| CommonAddedEvents |            playerClone            | server_scripts  |  玩家使生物繁殖事件   |   ~   |      X       |
| CommonAddedEvents |         entityEnterChunk          | server_scripts  |   实体进去区块事件    |   ~   |      X       |
| CommonAddedEvents |           playerRespawn           | server_scripts  |     玩家重生事件      |   ~   |      √       |
| CommonAddedEvents |       playerChangeDimension       | server_scripts  |   玩家维度改变事件    |   ~   |      √       |

> 注：测试是否有效不一定准确，内容经供参考，实际情况需要大家自己去测试