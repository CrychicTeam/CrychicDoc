---
authors: ['Wudji']
---

# 1.4 事件列表

***

以下为KubeJS内置的部分事件，其他模组或附属还可能添加其他事件。

| **事件ID**                       | **是否可被取消** | 描述          | **类型**  |
| ------------------------------ | ---------- | ----------- | ------- |
| init                           | 否          | -           | Startup |
| postinit                       | 否          | -           | Startup |
| loaded                         | 否          | -           | Startup |
| command.registry               | 否          | 注册命令        | Server  |
| command.run                    | 可          | 运行命令        | Server  |
| client.init                    | 否          | -           | Client  |
| client.debug\_info.left        | 否          | 客户端左侧F3事件   | Client  |
| client.debug\_info.right       | 否          | 客户端右侧F3事件   | Client  |
| client.logged\_in              | 否          | 客户端登入事件     | Client  |
| client.logged\_out             | 否          | 客户端登出事件     | Client  |
| client.tick                    | 否          | 客户端Tick     | Client  |
| server.load                    | 否          | 服务器加载事件     | Server  |
| server.unload                  | 否          | 服务器卸载事件     | Server  |
| server.tick                    | 否          | 服务器Tick     | Server  |
| server.datapack.first          | 否          | 高优先度数据包加载   | Server  |
| server.datapack.last           | 否          | 低优先度数据包加载   | Server  |
| recipes                        | 否          | 配方事件        | Server  |
| world.load                     | 否          | 事件加载事件      | Server  |
| world.unload                   | 否          | 世界卸载事件      | Server  |
| world.tick                     | 否          | 世界Tick      | Server  |
| world.explosion.pre            | 可          | 爆炸前事件       | Server  |
| world.explosion.post           | 否          | 爆炸后事件       | Server  |
| player.logged\_in              | 否          | 玩家登入事件      | Server  |
| player.logged\_out             | 否          | 玩家登出事件      | Server  |
| player.tick                    | 否          | 玩家Tick      | Server  |
| player.data\_from\_server.<信道> | 可          | 从服务器接收信息    | Client  |
| player.data\_from\_client.<信道> | 可          | 从客户端接收信息    | Server  |
| player.chat                    | 可          | 玩家聊天事件      | Server  |
| player.advancement             | 否          | 玩家成就事件      | Server  |
| player.inventory.opened        | 否          | 玩家打开库存事件    | Server  |
| player.inventory.closed        | 否          | 玩家关闭库存事件    | Server  |
| player.inventory.changed       | 否          | 玩家库存改变事件    | Server  |
| player.chest.opened            | 否          | 玩家打开箱子事件    | Server  |
| player.chest.closed            | 否          | 玩家关闭箱子事件    | Server  |
| entity.death                   | 可          | 实体死亡事件      | Server  |
| entity.attack                  | 可          | 实体攻击事件      | Server  |
| entity.drops                   | 可          | 实体摔落事件      | Server  |
| entity.check\_spawn            | 可          | 实体检查生成位置事件  | Server  |
| entity.spawned                 | 可          | 实体生成事件      | Server  |
| block.registry                 | 否          | 方块注册事件      | Startup |
| block.missing\_mappings        | 否          | -           | Server  |
| block.tags                     | 否          | 方块tag事件     | Server  |
| block.right\_click             | 可          | 右击方块事件      | Server  |
| block.left\_click              | 可          | 左击方块事件      | Server  |
| block.place                    | 可          | 放置方块事件      | Server  |
| block.break                    | 可          | 破坏方块事件      | Server  |
| block.drops                    | 否          | 方块掉落事件      | Server  |
| item.food\_eaten               | 可          | 食物食用事件      | Server  |
| item.registry                  | 否          | 物品注册事件      | Startup |
| item.missing\_mappings         | 否          | -           | Server  |
| item.tags                      | 否          | 物品tag事件     | Server  |
| item.right\_click              | 可          | 物品右击事件      | Client  |
| item.right\_click\_empty       | 否          | 物品右击事件（空手）  | Client  |
| item.left\_click               | 否          | 物品左击事件      | Client  |
| item.entity\_interact          | 可          | 物品实体交互事件    | Server  |
| item.modification              | 否          | 物品修改事件      | Startup |
| item.pickup                    | 可          | 物品拾取事件      | Server  |
| item.tooltip                   | 否          | 物品tooltip事件 | Client  |
| item.toss                      | 可          | 物品丢出事件      | Server  |
| item.crafted                   | 否          | 物品合成事件      | Server  |
| item.smelted                   | 否          | 物品熔炼事件      | Server  |
| fluid.registry                 | 否          | 流体注册事件      | Startup |
| fluid.tags                     | 否          | 流体tag事件     | Server  |
| entity\_type.tags              | 否          | 实体类型tag事件   | Server  |
| worldgen.add                   | 否          | 世界生成添加      | Startup |
| worldgen.remove                | 否          | 世界生成移除      | Startup |
