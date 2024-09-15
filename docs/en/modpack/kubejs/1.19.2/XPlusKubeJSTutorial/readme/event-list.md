# 1.4 事件列表

## 一、事件列表

| 脚本类型         | 方法                                            | 描述                               | 是否可取消 |
| ---------------- | ----------------------------------------------- | ---------------------------------- | ---------- |
| startup\_scripts | `StartupEvents.init`                            | -                                  | ❌          |
| startup\_scripts | `StartupEvents.postInit`                        | -                                  | ❌          |
| startup\_scripts | `StartupEvents.registry`                        | 注册游戏内容(用于添加物品，附魔等) | ❌          |
| client\_scripts  | `ClientEvents.highPriorityAssets`               | -                                  | ❌          |
| client\_scripts  | `ClientEvents.init`                             | -                                  | ❌          |
| client\_scripts  | `ClientEvents.loggedIn`                         | 登入世界                           | ❌          |
| client\_scripts  | `ClientEvents.loggedOut`                        | 登出世界                           | ❌          |
| client\_scripts  | `ClientEvents.tick`                             | 客户端Tick事件                     | ❌          |
| client\_scripts  | `ClientEvents.painterUpdated`                   | Painter API                        | ❌          |
| client\_scripts  | `ClientEvents.leftDebugInfo`                    | F3界面左侧信息                     | ❌          |
| client\_scripts  | `ClientEvents.rightDebugInfo`                   | F3界面右侧信息                     | ❌          |
| client\_scripts  | `ClientEvents.paintScreen`                      | 绘制Screen                         | ❌          |
| server\_scripts  | `ServerEvents.lowPriorityData`                  | 数据包事件 - 低优先度              | ❌          |
| server\_scripts  | `ServerEvents.highPriorityData`                 | 数据包事件 - 高优先度              | ❌          |
| server\_scripts  | `SeverEvents.loaded`                            | 服务端加载                         | ❌          |
| server\_scripts  | `ServerEvents.unloaded`                         | 服务端卸载                         | ❌          |
| server\_scripts  | `ServerEvents.tick`                             | 服务端Tick事件                     | ❌          |
| server\_scripts  | `ServerEvents.tags`                             | 标签添加或修改                     | ❌          |
| server\_scripts  | `ServerEvents.commandRegistry`                  | 命令注册事件                       | ❌          |
| server\_scripts  | `ServerEvents.command`                          | 命令执行事件                       | ✔          |
| server\_scripts  | `ServerEvents.customCommand`                    | 自定义命令事件                     | ✔          |
| server\_scripts  | `ServerEvents.recipes`                          | 配方添加或修改                     | ❌          |
| server\_scripts  | `ServerEvents.afterRecipes`                     | 配方加载完成后事件                 | ❌          |
| server\_scripts  | `ServerEvents.specialRecipeSerializers`         | 配方序列化相关事件                 | ❌          |
| server\_scripts  | `ServerEvents.compostableRecipes`               | 堆肥配方添加或修改                 | ❌          |
| server\_scripts  | `ServerEvents.recipeTypeRegistry`               | 配方类型注册                       | ❌          |
| server\_scripts  | `ServerEvents.genericLootTable`等，详见后续章节 | LootTable相关事件                  | ❌          |
| server\_scripts  | `LevelEvents.loaded`                            | 世界加载事件                       | ❌          |
| server\_scripts  | `LevelEvents.unloaded`                          | 世界卸载事件                       | ❌          |
| server\_scripts  | `LevelEvents.tick`                              | 世界Tick事件                       | ❌          |
| server\_scripts  | `LevelEvents.beforeExplosion`                   | 爆炸发生前事件                     | ✔          |
| server\_scripts  | `LevelEvents.afterExplosion`                    | 爆炸发生后事件                     | ❌          |
| server\_scripts  | `WorldgenEvents.add`                            | 添加世界生成（如矿石等）           | ❌          |
| server\_scripts  | `WorldgenEvents.remove`                         | 移除世界生成（如矿石等）           | ❌          |
| server\_scripts  | `NetworkEvents.fromServer`                      | 客户端接收服务端网络包             | ✔          |
| server\_scripts  | `NetworkEvents.fromClient`                      | 服务端接收客户端网络包             | ✔          |
| server\_scripts  | `ItemEvents.modification`                       | 游戏内容修改(用于修改物品)         | ❌          |
| server\_scripts  | `ItemEvents.toolTierRegistry`                   | 工具等级注册                       | ❌          |
| server\_scripts  | `ItemEvents.armorTierRegistry`                  | 护甲等级注册                       | ❌          |
| server\_scripts  | `ItemEvents.pickedUp`                           | 捡起物品事件                       | ❌          |
| server\_scripts  | `ItemEvents.dropped`                            | 丢弃物品事件                       | ❌          |
| server\_scripts  | `ItemEvents.entityInteracted`                   | 物品与实体交互事件                 | ❌          |
| server\_scripts  | `ItemEvents.crafted`                            | 物品合成事件                       | ❌          |
| server\_scripts  | `ItemEvents.smelted`                            | 物品烧炼事件                       | ❌          |
| server\_scripts  | `ItemEvents.foodEaten`                          | 食用食物类物品事件                 | ❌          |
| client\_scripts  | `ItemEvents.clientLeftClicked`                  | 左键单击事件(客户端侧)             | ❌          |
| server\_scripts  | `ItemEvents.firstLeftClicked`                   | 左键单击事件(服务端侧)             | ❌          |
| client\_scripts  | `ItemEvents.clientLeftClicked`                  | 右键单击事件(客户端侧)             | ❌          |
| server\_scripts  | `ItemEvents.firstRightClicked`                  | 右键单击事件(服务端侧)             | ❌          |
| server\_scripts  | `ItemEvents.tooltip`                            | 物品悬浮提示修改                   | ❌          |
| server\_scripts  | `ItemEvents.modelProperties`                    | 物品模型修改                       | ❌          |
| server\_scripts  | `BlockEvents.modification`                      | 游戏内容修改(用于修改方块)         | ❌          |
| server\_scripts  | `BlockEvents.rightClicked`                      | 方块右键单击事件                   | ❌          |
| server\_scripts  | `BlockEvents.leftClicked`                       | 方块左键单击事件                   | ❌          |
| server\_scripts  | `BlockEvents.placed`                            | 方块放置事件                       | ❌          |
| server\_scripts  | `BlockEvents.broken`                            | 方块被破坏事件                     | ❌          |
| server\_scripts  | `BlockEvents.detectorChanged`                   | 检测方块状态改变事件               | ❌          |
| server\_scripts  | `BlockEvents.detectorPowered`                   | 检测方块被红石充能事件             | ❌          |
| server\_scripts  | `BlockEvents.detectorUnpowered`                 | 检测方块红石充能结束事件           | ❌          |
| server\_scripts  | `EntityEvents.death`                            | 实体死亡事件                       | ❌          |
| server\_scripts  | `EntityEvents.hurt`                             | 实体受伤事件                       | ❌          |
| server\_scripts  | `EntityEvents.checkSpawn`                       | 实体检查生成位置事件               | ❌          |
| server\_scripts  | `EntityEvents.spawned`                          | 实体生成事件                       | ❌          |
| server\_scripts  | `PlayerEvents.loggedIn`                         | 玩家登入事件                       | ❌          |
| server\_scripts  | `PlayerEvents.loggedOut`                        | 玩家登出事件                       | ❌          |
| server\_scripts  | `PlayerEvents.cloned`                           | 玩家克隆事件                       | ❌          |
| server\_scripts  | `PlayerEvents.tick`                             | 玩家Tick事件                       | ❌          |
| server\_scripts  | `PlayerEvents.chat`                             | 玩家聊天事件                       | ❌          |
| server\_scripts  | `PlayerEvents.decorateChat`                     | -                                  | ❌          |
| server\_scripts  | `PlayerEvents.advancement`                      | 玩家成就事件                       | ✔          |
| server\_scripts  | `PlayerEvents.inventoryOpened`                  | 玩家打开背包事件                   | ❌          |
| server\_scripts  | `PlayerEvents.inventoryClosed`                  | 玩家关闭背包事件                   | ❌          |
| server\_scripts  | `PlayerEvents.inventoryChanged`                 | 玩家背包物品变更事件               | ❌          |
| server\_scripts  | `PlayerEvents.chestOpened`                      | 玩家打开箱子事件                   | ❌          |
| server\_scripts  | `PlayerEvents.chestClosed`                      | 玩家关闭箱子事件                   | ❌          |

## 二、常用模组事件

#### JEI：

| 脚本类型        | 方法                         | 描述           | 是否可被取消 |
| --------------- | ---------------------------- | -------------- | ------------ |
| client\_scripts | `JEIEvents.subtypes`         | 子类型         | ❌            |
| client\_scripts | `JEIEvents.hideItems`        | 隐藏物品       | ❌            |
| client\_scripts | `JEIEvents.hideFluids`       | 隐藏流体       | ❌            |
| client\_scripts | `JEIEvents.hideCustom`       | 隐藏自定义类型 | ❌            |
| client\_scripts | `JEIEvents.removeCategories` | 移除类型       | ❌            |
| client\_scripts | `JEIEvents.removeRecipes`    | 移除配方       | ❌            |
| client\_scripts | `JEIEvents.addItems`         | 添加物品       | ❌            |
| client\_scripts | `JEIEvents.addFluids`        | 添加流体       | ❌            |
| client\_scripts | `JEIEvents.information`      | -              | ❌            |

#### REI：

| 脚本类型        | 方法                         | 描述     | 是否可被取消 |
| --------------- | ---------------------------- | -------- | ------------ |
| client\_scripts | `REIEvents.hide`             | 隐藏类型 | No           |
| client\_scripts | `REIEvents.add`              | 添加类型 | No           |
| client\_scripts | `REIEvents.information`      | -        | No           |
| client\_scripts | `REIEvents.removeCategories` | 移除类别 | No           |
| client\_scripts | `REIEvents.groupEntries`     | 注册表   | No           |

#### GameStages：

| 脚本类型        | 方法                           | 描述     | 是否可被取消 |
| --------------- | ------------------------------ | -------- | ------------ |
| server\_scripts | `GameStageEvents.stageAdded`   | 添加阶段 | No           |
| server\_scripts | `GameStageEvents.stageRemoved` | 移除阶段 | No           |
