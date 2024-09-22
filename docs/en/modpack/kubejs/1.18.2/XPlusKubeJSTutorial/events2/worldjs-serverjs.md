---
authors: ['Wudji']
---

# 15.4 WorldJS 和 ServerJS

***

## 一、WorldJS

WorldJS允许你获取、修改当前世界的一些信息，而ServerJS允许你操作当前服务器中的一些内容

## 1、属性

| 属性           | 描述             | 返回值             |
| ------------ | -------------- | --------------- |
| side         | 返回当前运行环境\[1]   | ScriptType      |
| gameRules    | 返回游戏规则效果内容\[2] | GameRulesJS     |
| server       | 返回ServerJS     | ServerJS        |
| time         | 获取游戏总时间        | 长整型             |
| localTime    | 获取游戏日时间        | 长整型             |
| dimension    | 返回游戏维度名        | 字符串             |
| overworld    | 返回当前世界是否为主世界   | 布尔型             |
| daytime      | 返回当前世界是否为白天    | 布尔型             |
| raining      | 返回当前世界天气是否为雨天  | 布尔型             |
| thundering   | 返回当前世界天气是否为雷暴  | 布尔型             |
| rainStrength | 返回当前世界下雨强度     | 浮点型             |
| players      | 返回当前世界玩家列表     | EntityArrayList |
| entities     | 返回当前世界实体列表     | EntityArrayList |

\[1]：值可以为client/server

\[2]：见本节末GameRulesJS

## 2、函数

| 函数                                                                      | 描述       | 返回值              |
| ----------------------------------------------------------------------- | -------- | ---------------- |
| getBlock(整形 x, 整形 y, 整形 z)                                              | 返回指定坐标方块 | BlockContainerJS |
| getBlock(BlockPos pos)                                                  | 返回指定坐标方块 | BlockContainerJS |
| getBlock(BlockEntity blockEntity)                                       | 返回指定坐标方块 | BlockContainerJS |
| createExplosion(浮点型 x, 浮点型 y, 浮点型 z)                                    | 新建爆炸     | ExplosionJS\[3]  |
| createEntity(命名空间id)                                                    | 新建实体     | EntityJS\[4]     |
| spawnLightning(浮点型 x, 浮点型 y, 浮点型 z, 布尔值 是否只有效果, nullable EntityJS 玩家对象) | 生成闪电     | void             |
| spawnLightning(浮点型 x, 浮点型 y, 浮点型 z, 布尔值 是否只有效果)                         | 生成闪电     | void             |
| spawnFireworks(浮点型 x, 浮点型 y, 浮点型 z, FireworksJS 烟花)                     | 生成烟花     | void             |

\[3]：见15.3 ExplosionJS 部分

\[4]：见15.2 EntityJS部分

## 二、ServerJS

## 1、属性

| 属性           | 描述                | 返回值               |
| ------------ | ----------------- | ----------------- |
| overworld    | 返回ServerWorldJS   | ServerWorldJS\[5] |
| worlds       | 返回ServerWorldJS列表 | List              |
| running      | 返回服务器是否正在运行       | 布尔值               |
| hardcore     | 返回服务器是否为极限模式      | 布尔值               |
| singlePlayer | 返回是否为单人模式         | 布尔值               |
| dedicated    | 返回是否为"纯"服务器端(?)   | 布尔值               |
| motd         | 返回服务器的motd        | 字符串               |
| players      | 返回当前服务器玩家列表       | EntityArrayList   |
| entities     | 返回当前服务器实体列表       | EntityArrayList   |

## 2、函数

| 函数                                     | 描述              | 返回值                     |
| -------------------------------------- | --------------- | ----------------------- |
| stop()                                 | 停止当前服务器         | void                    |
| getLevel(字符串 维度名称)                     | 返回给定维度          | WorldJS                 |
| getPlayer(UUID uuid)                   | 返回给定UUID的玩家     | nullable ServerPlayerJS |
| getPlayer(字符串 玩家名称)                    | 返回给定名称的玩家       | nullable ServerPlayerJS |
| getEntities(字符串 过滤器\[6])               | 返回符合给定过滤器的实体列表  | EntityArrayList         |
| getAdvancement(命名空间id)                 | 返回给定成就          | AdvancementJS\[7]       |
| sendDataToAll(字符串 通道, nullable any 数据) | 向所有玩家发送数据\[8]   | void                    |
| setMotd(Component motd)                | 设置服务器motd       | void                    |
| getName()                              | 返回服务器名称         | Text                    |
| getDisplayName()                       | 返回服务器显示名称\[9]   | Text                    |
| tell(Component 消息)                     | 向服务器所有玩家发送消息    | void                    |
| setStatusMessage(Component 消息)         | 设置服务器所有玩家的状态栏消息 | void                    |

\[5]：见本节末ServerWorldJS

\[6]：形如"@e\[type=minecraft:clicken]"等

\[7]：见本节末AdvancementJS

\[8]：见16 网络包

\[9]：这里指从服务器后台执行命令时括号中显示的“名称”

## 三、GameRulesJS

| 函数                      | 描述         | 返回值  |
| ----------------------- | ---------- | ---- |
| getString(字符串 游戏规则)     | 返回指定游戏规则的值 | 字符串  |
| getBoolean(字符串 游戏规则)    | 返回指定游戏规则的值 | 布尔型  |
| getInt(字符串 游戏规则)        | 返回指定游戏规则的值 | 整形   |
| set(字符串 游戏规则, Object 值) | 设置指定游戏规则的值 | void |

## 四、AdvancementJS

| 属性          | 描述     | 返回值  |
| ----------- | ------ | ---- |
| displayText | 返回进度文本 | Text |
| title       | 返回进度标题 | Text |
| description | 返回进度描述 | Text |

| 函数                        | 描述       | 返回值  |
| ------------------------- | -------- | ---- |
| addChild(AdvancementJS a) | 添加子进度    | void |
| hasDisplay()              | 该进度是否会显示 | 布尔值  |
