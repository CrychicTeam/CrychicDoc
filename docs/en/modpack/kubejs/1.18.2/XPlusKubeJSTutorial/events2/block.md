---
authors: ['Wudji']
---

# 15.3 方块信息获取及操作

***

## 一、BlockContainerJS

### 1、属性

| 属性              | 功能                  | 返回值类型                                                              |
| --------------- | ------------------- | ------------------------------------------------------------------ |
| world           | 返回WorldJS           | WorldJS                                                            |
| pos             | 返回BlockPos          | BlockPos                                                           |
| x               | 返回方块x坐标             | 整形                                                                 |
| y               | 返回方块y坐标             | 整形                                                                 |
| z               | 返回方块z坐标             | 整形                                                                 |
| down            | 返回该方块下方的方块          | BlockContainerJS                                                   |
| up              | 返回该方块上方的方块          | BlockContainerJS                                                   |
| north           | 返回该方块北方的方块          | BlockContainerJS                                                   |
| south           | 返回该方块南方的方块          | BlockContainerJS                                                   |
| west            | 返回该方块西方的方块          | BlockContainerJS                                                   |
| east            | 返回该方块东方的方块          | BlockContainerJS                                                   |
| blockState      | 返回包含该方块状态的对象        | BlockState                                                         |
| id              | 返回方块ID              | 字符串                                                                |
| light           | 返回光照等级              | 整形                                                                 |
| canSeeSky       | 是否露天                | 布尔值                                                                |
| item            | 返回方块对应的物品           | ItemStackJS                                                        |
| playersInRadius | 返回范围内的玩家            | [EntityArrayList](https://kubejs.com/wiki/kubejs/EntityArrayList/) |
| biomeId         | 返回所处群系的ID           | 字符串                                                                |
| properties      | 返回带有当前方块所有属性的对象\[1] | 对象                                                                 |

\[1]如`block.properties.facing`会返回当前方块朝向的字符串

### 2、方法

| 方法                                               | 功能                         | 返回值类型                |
| ------------------------------------------------ | -------------------------- | -------------------- |
| offset(Direction 方向, int 格数)                     | 返回在给定方向上指定格数以外的方块          | BlockContainerJS     |
| offset(Direction 方向)                             | 返回在给定方向上一格以外的方块            | BlockContainerJS     |
| offset(整形 x, 整形 y, 整形 z)                         | 返回以当前方块为坐标系原点，指定X,Y,Z以外的方块 | BlockContainerJS     |
| setBlockState(BlockState 状态值, int )              | 设置方块状态值                    | void                 |
| hasTag(tag)                                      | 返回是否具有指定标签                 | 布尔值                  |
| set(命名空间 方块ID, Map<字符串, 字符串> 方块属性, 整形 flags)     | 在当前位置设置方块                  | void                 |
| set(命名空间 方块ID, Map<字符串, 字符串> 方块属性)               | 在当前位置设置方块                  | void                 |
| set(命名空间 方块ID)                                   | 在当前位置设置方块                  | void                 |
| createExplosion()                                | 新建爆炸事件(见下文)                | ExplosionJS          |
| createEntity(命名空间ID)                             | 新建实体(见15.2)                | EntityJS             |
| spawnLightning(布尔值 是否只有效果, nullable EntityJS 玩家) | 生成闪电                       | void                 |
| spawnLightning(布尔值 是否只有效果)                       | 生成闪电                       | void                 |
| spawnFireworks(FireworksJS 烟花)                   | 生成烟花(见下文)                  | void                 |
| getInventory(Direction 方向)                       | 获取方块的库存                    | nullable InventoryJS |
| getPlayersInRadius(浮点型 范围)                       | 返回指定范围内的玩家列表               | EntityArrayList      |

## 二、ExplosionJS

新建爆炸和新建实体类似，你需要先新建一个爆炸，然后设置属性，再在合适的时间生成它即可

| 函数                          | 功能             | 返回值类型       |
| --------------------------- | -------------- | ----------- |
| exploder(EntityJS 实体)       | 设定是谁引起的爆炸      | ExplosionJS |
| strength(浮点型 爆炸强度)          | 设定爆炸的强度        | ExplosionJS |
| causesFire(布尔值 是否引火)        | 设定爆炸是否会生成火     | ExplosionJS |
| damagesTerrain(布尔值 是否破坏环境)  | 设定爆炸是否破坏地形\[2] | ExplosionJS |
| destroysTerrain(布尔值 是否破坏环境) | 设定爆炸是否破坏地形\[2] | ExplosionJS |
| explode()                   | 炸！             | void        |

\[2]damagesTerrain会破坏方块但是掉落方块，destroysTerrain会破坏方块且不掉落方块。若不设置默认为破坏且掉落方块。

一个简单的例子，在玩家右键方块时生成一个小爆炸

```
onEvent('block.right_click', event => {
	// 新建爆炸
	let explosion = event.block.createExplosion();
	// 设置爆炸强度，默认值为3
	explosion.strength(100.0);
	// 设置是否生成火，默认值为false
	explosion.causesFire(true);
	explosion.explode();
})
```

（应该很小吧）

![](https://m1.miaomc.cn/uploads/20220416\_f84d7444ffa57.jpg)
