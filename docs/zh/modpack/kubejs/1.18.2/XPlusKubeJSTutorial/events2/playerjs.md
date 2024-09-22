---
authors: ['Wudji']
---

# 15.1 玩家&实体事件，信息获取及操作

***

## 一、信息获取

以下内容会返回玩家/实体的信息

| 格式                      | 功能                            | 返回值类型                     |
| ----------------------- | ----------------------------- | ------------------------- |
| inventory               | 返回玩家的库存(仅适用于玩家)               | InventoryJS\[1]           |
| block                   | 返回涉及到的方块                      | BlockContainerJS          |
| selectedSlot            | 返回当前玩家选中的格子(仅适用于玩家)           | 整形                        |
| mouseItem               | 返回当前玩家鼠标选取的物品(仅适用于玩家)         | ItemStackJS\[2]           |
| creativeMode            | 返回当前玩家是否为创造模式(仅适用于玩家)         | 布尔值                       |
| spectator               | 返回当前玩家是否为观察者模式(仅适用于玩家)        | 布尔值                       |
| stats                   | 返回玩家统计数据(仅适用于玩家)              | PlayerStatsJS\[3]         |
| foodLevel               | 返回玩家饱食度等级(仅适用于玩家)             | 整形                        |
| xp                      | 返回玩家经验值(仅适用于玩家)               | 整形                        |
| xplevel                 | 返回玩家经验等级(仅适用于玩家)              | 整形                        |
| miningBlock             | 返回玩家是否正在破坏方块(仅适用于玩家)          | 布尔值                       |
| airSupply               | 返回玩家当前氧气供应值(仅适用于玩家)           | 整形                        |
| maxAirSupply            | 返回玩家最大氧气供应值(仅适用于玩家)           | 整形                        |
| stages                  | 返回玩家游戏阶段(仅适用于玩家)              | Stages\[4]                |
| name                    | 返回玩家名称                        | Text                      |
| displayName             | 返回玩家显示名称                      | Text                      |
| alive                   | 返回游戏中玩家是否存活                   | 布尔值                       |
| living                  | 返回是否处于活跃状态                    | 布尔值                       |
| crouching               | 返回玩家/实体是否正在潜行                 | 布尔值                       |
| sprinting               | 返回玩家/实体是否正在疾跑                 | 布尔值                       |
| swimming                | 返回玩家/实体是否正在游泳                 | 布尔值                       |
| glowing                 | 返回玩家/实体是否正在发光                 | 布尔值                       |
| invisible               | 返回玩家/实体是否隐形                   | 布尔值                       |
| invulnerable            | 返回玩家/实体是否不受伤害                 | 布尔值                       |
| onGround                | 返回玩家/实体是否站在地上                 | 布尔值                       |
| fallDistance            | 返回玩家/实体目前掉落距离(落地后该值归0)        | 浮点型                       |
| noClip                  | 返回玩家/实体是否没有碰撞箱(观察者模式下该值为true) | 布尔值                       |
| x y z                   | 返回玩家/实体的x/y/z坐标               | 浮点型                       |
| yaw pitch               | 返回玩家/实体的偏航角/俯仰角               | 浮点型                       |
| motionX motionY motionZ | 返回玩家/实体动量                     | 浮点型                       |
| ticksExisted            | 返回玩家/实体存在的游戏刻数                | 整形                        |
| world                   | 返回WorldJS                     | WorldJS\[5]               |
| ridingEntity            | 返回玩家/实体正在骑的实体                 | EntityJS                  |
| teamId                  | 返回玩家/实体当前所在队伍名称               | 字符串                       |
| customName              | 返回玩家/实体自定义名称                  | Text                      |
| hasCustomName           | 返回玩家/实体是否有自定义名称               | 布尔值                       |
| customNameAlwaysVisible | 返回玩家/实体自定义名称是否总是可见            | 布尔值                       |
| eyeHeight               | 返回玩家/实体视线高度                   | 浮点型                       |
| fullNBT                 | 返回玩家/实体NBT                    | CompoundTag               |
| inWater                 | 返回玩家/实体是否在水里                  | 布尔值                       |
| underWater              | 返回玩家/实体是否在水下                  | 布尔值                       |
| child                   | 返回玩家/实体是否为幼年个体                | 布尔值                       |
| potionEffects           | 返回玩家/实体具有的效果                  | EntityPotionEffectsJS\[6] |
| lastDamageSource        | 返回玩家/实体最后收到的伤害来源              | DamageSourceJS\[7]        |
| health                  | 返回玩家/实体的血量                    | 浮点型                       |
| maxHealth               | 返回玩家/实体的最大血量                  | 浮点型                       |
| lastAttackedEntity      | 返回最后攻击的实体                     | LivingEntityJS            |
| idleTime                | 返回玩家/实体无动作时间                  | 整形                        |
| attackingEntity         | 返回玩家/实体攻击对象                   | LivingEntityJS            |
| mainHandItem            | 返回玩家/实体主手物品                   | ItemStackJS               |
| offHandItem             | 返回玩家/实体副手物品                   | ItemStackJS               |
| headArmorItem           | 返回玩家/实体头盔物品                   | ItemStackJS               |
| chestArmorItem          | 返回玩家/实体胸甲物品                   | ItemStackJS               |
| legsArmorItem           | 返回玩家/实体腿甲物品                   | ItemStackJS               |
| feetArmorItem           | 返回玩家/实体鞋子物品                   | ItemStackJS               |
| movementSpeed           | 返回玩家/实体移动速度                   | 浮点型                       |
| reachDistance           | 返回玩家/实体攻击距离                   | 浮点型                       |
| onFire                  | 返回玩家/实体着火状态（支持通过a = b方式修改）    | 整形                        |
| statusMessage           | 返回玩家状态栏消息（支持通过a = b方式修改）      | Text\[8]                  |
| horizontalFacing        | 返回玩家水平方向                      | Direction\[9]             |
| facing                  | 返回玩家方向                        | Direction\[9]             |

\[1]、\[2]、\[3]、\[6]、\[7]、\[8]见后文

\[4]见11.4 KubeJs内置游戏阶段 (类似于GameStage)

\[5]见6 自定义世界生成

\[9]返回值可为DOWN, UP, NORTH, SOUTH, WEST, EAST

## 二、函数

| 格式                                                             | 功能                  | 返回值              |
| -------------------------------------------------------------- | ------------------- | ---------------- |
| sendInventoryUpdate()（仅适用于玩家）                                  | 向客户端(?)发送背包数据更新     | void             |
| give(ItemStackJS 物品) （仅适用于玩家）                                  | 给予玩家物品              | void             |
| giveInHand(ItemStackJS 物品)（仅适用于玩家）                             | 给予玩家物品(快捷栏而不是背包)    | void             |
| sendData(字符串 频道, 数据)（仅适用于玩家）                                   | 发送网络包（详见后文）         | void             |
| addFood(整形 食物水平, 浮点型 饱和度)（仅适用于玩家）                              | 为玩家添加饥饿值            | void             |
| addExhaustion(浮点型 饥饿等级)（仅适用于玩家）                                | 为玩家添加饥饿等级           | void             |
| addXP(整形 经验值)（仅适用于玩家）                                          | 为玩家添加经验值            | void             |
| addXPLevels(整形 经验等级)（仅适用于玩家）                                   | 为玩家添加经验等级           | void             |
| paint(CompoundTag 渲染器)（仅适用于玩家）                                 | 调用Painter API(详见后文) | void             |
| boostElytraFlight()（仅适用于玩家）                                    | 加速玩家鞘翅飞行(类似于烟花)     | void             |
| closeInventory()（仅适用于玩家）                                       | 关闭玩家背包              | void             |
| addItemCooldown(Item 物品, 整形 冷却tick)（仅适用于玩家）                    | 添加物品冷却              | voi              |
| runCommand(字符串 命令内容)                                           | 执行命令\[10]           | int              |
| runCommandSilent(字符串 命令内容)                                     | 静默执行命令\[10]         | int              |
| setMotion(浮点型 x, 浮点型 y, 浮点型 z)                                 | 设置实体动量              | void             |
| setRotation(浮点型 偏航角,浮点型 俯仰角)                                   | 设置玩家旋转角度            | void             |
| setPosition(浮点型 x, 浮点型 y, 浮点型 z)                               | 设置玩家坐标              | void             |
| setPositionAndRotation(浮点型 x, 浮点型 y, 浮点型 z, 浮点型 偏航角,浮点型 俯仰角)   | 上边两个函数的合并           | void             |
| addMotion(浮点型 x, 浮点型 y, 浮点型 z)                                 | 添加实体动量              | void             |
| kill()                                                         | 我给你毙了               | void             |
| startRiding(EntityJS 实体, 布尔值 是否强制)                             | 使玩家骑某个实体            | boolean          |
| removePassengers()                                             | 移除当前实体乘客            | void             |
| dismountRidingEntity()                                         | 取消当前实体骑乘状态          | void             |
| isPassenger(EntityJS 实体)                                       | 返回实体是否为骑乘状态         | 布尔值              |
| isOnSameTeam(EntityJS 实体)                                      | 返回是否与某实体为同一队        | 布尔值              |
| isOnScoreboardTeam(字符串 队伍名称)                                   | 返回是否在计分板队伍上         | 布尔值              |
| extinguish()                                                   | 灭火                  | void             |
| playSound(SoundEvent 音乐名, 浮点型 音量, 浮点型 角度)                      | 播放声音                | void             |
| playSound(SoundEvent 音乐名)                                      | 同上                  | void             |
| spawn()                                                        | 立即重生                | void             |
| attack(字符串 来源, 浮点型 血量)                                         | 使对象受伤               | void             |
| attack(浮点型 血量)                                                 | 同上                  | void             |
| rayTrace(浮点型 距离)                                               | rayTraceEvent(详见后文) | RayTraceResultJS |
| getDistanceSq(浮点型 x, 浮点型 y, 浮点型 z)                             | 返回与坐标之间距离的平方\[11]   | 浮点型              |
| getDistanceSq(BlockPos 坐标)                                     | 返回与坐标之间距离的平方\[11]   | 浮点型              |
| getDistance(浮点型 x, 浮点型 y, 浮点型 z)                               | 返回与坐标之间距离\[11]      | 浮点型              |
| getDistance(BlockPos 坐标)                                       | 返回与坐标之间距离\[11]      | 浮点型              |
| heal(浮点型 血量)                                                   | 为对象添加血量             | void             |
| swingArm(InteractionHand 手)                                    | 晃动手臂\[12]           | void             |
| getEquipment(EquipmentSlot 栏位)                                 | 返回指定栏位的装备\[13]      | ItemStackJS      |
| setEquipment(EquipmentSlot 栏位, ItemStackJS 物品)                 | 设置指定栏位的装备\[13]      | void             |
| getHeldItem(InteractionHand 手)                                 | 返回指定手的物品\[12]       | ItemStackJS      |
| setHeldItem(InteractionHand 手, ItemStackJS 手)                  | 设置指定手的物品\[12]       | void             |
| damageEquipment(EquipmentSlot 栏位, 整形 扣除耐久值)                    | 损坏物品\[13]           | void             |
| damageEquipment(EquipmentSlot 栏位, 整形 扣除耐久值, Consumer onBroken) | 损坏物品\[13]           | void             |
| damageEquipment(EquipmentSlot 栏位)                              | 损坏物品\[13]           | void             |
| damageHeldItem(InteractionHand 手, 整形 扣除耐久值)                    | 损坏物品\[13]           | void             |
| damageHeldItem(InteractionHand 手, 整形 扣除耐久值, Consumer onBroken) | 损坏物品\[13]           | void             |
| damageHeldItem()                                               | 损坏物品                | void             |
| isHoldingInAnyHand(内容)                                         | 指定内容是否拿在手中          | boolean          |
| canEntityBeSeen(EntityJS 实体)                                   | 是否能看到指定实体           | boolean          |
| rayTrace(整形 距离)                                                | rayTraceEvent(详见后文) | RayTraceResultJS |
| mergeFullNBT(字符串 NBT)                                          | 将给定的NBT与原实体的合并\[14] | void             |

\[10]：该函数与ServerJS下的同名方法有两点不同 ① 此处执行命令需要加斜杠( / )，而后者不用 ② 此处执行命令会以玩家的权限执行，而前者为服务器控制台权限。另外成功执行命令时该函数返回1，失败返回0

\[11]：getDistance与getDistanceSq的区别见下：

![](https://m1.miaomc.cn/uploads/20220326\_6988c88d4bdc4.png)

\[12]：InteractionHand 接受值为 MAIN\_HAND或OFF\_HAND

\[13]：EquipmentSlot 接受值为 MAINHAND、OFFHAND、HEAD、CHEST、LEGS、FEET

\[14]：相同的会被新值覆盖

注1：实体与玩家的操作方法大体相同（也就是适用于玩家的很多函数同时适用于实体）

注2：实际上PlayerJS继承于LivingEntityJS，而后者又继承于EntityJS。
