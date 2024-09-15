# 15.2 实体生成 & RayTraceResultJS & ItemStackJS及其他

***

本节内容：实体生成，RayTraceResultJS ，ItemStackJS，InventoryJS，PlayerStatsJS

## 一、实体生成

实体生成较为简单，你需要先新建一个实体，然后设置属性，再在合适的时间生成它即可

以下代码会在玩家在聊天栏发送内容时在玩家位置生成一个火球

```
onEvent("player.chat", event => {
	let p = event.player;
    // 新建实体
    var entity = event.world.createEntity('minecraft:fireball');
    // 设置实体NBT
    entity.mergeFullNBT('{ExplosionPower:2}');
    // 设置坐标
    entity.setPosition(p.x, p.y, p.z);
    // 生成实体，没有这一行实体是不会生成的
    entity.spawn();
})
```

createEntity的返回值为EntityJS，所以绝大多数15.1中的东西都是可以应用于实体生成的，这里就不再赘述了。

## 二、RayTraceResultJS

RayTraceResultJS 会返回玩家所看的方向、方块、实体等内容。

| 属性         | 功能           | 返回值              |
| ---------- | ------------ | ---------------- |
| fromEntity | 返回"视线"的来源    | EntityJS         |
| type       | 返回玩家看到的内容类型  | 字符串\[1]          |
| distance   | 返回目标距离玩家距离   | 浮点型              |
| hitX       | 返回"视线"目标的X坐标 | 浮点型              |
| hitY       | 返回"视线"目标的Y坐标 | 浮点型              |
| hitZ       | 返回"视线"目标的Z坐标 | 浮点型              |
| block      | 返回视线目标方块     | BlockContainerJS |
| facing     | 返回视线的方向      | Direction        |
| entity\[2] | 返回视线目标实体     | EntityJS         |

\[1]：该值可以为"block"、"entity"和"miss"

\[2]：因为KubeJS的特性，有关实体的内容仅在客户端侧可用且距离**不得大于玩家的攻击距离**。

值得注意的是，绝大多数的属性在玩家没有面向对象时均为null，也就是说在使用RayTraceResultJS时，必须先判断其值是否为null或者利用type来判断类型(否则你得到一堆报错)

以下例子为指南针添加了一个实用功能：返回其右键点击的方块id和属性并生成对应/setblock指令供玩家复制

```
onEvent('item.right_click', event => {
	let player = event.player;
	let target = player.rayTrace(1000);
	if(target.type == "block" && player.mainHandItem.id == "minecraft:compass"){
		let prop = target.block.properties.toString();
		// 把信息告诉玩家
		player.tell(`你刚才看的方块是：${target.block.id}，距离当前位置${player.getDistance(target.hitX, target.hitY, target.hitZ).toFixed(1)}格，方块属性：${prop}`);
		// 复制指令(TextJS)
		player.tell(Text.of("[点此复制/setblock指令]").bold().click(`copy:/setblock ~ ~ ~ ${target.block}`));
		// 为物品添加冷却
		// player.addItemCooldown("minecraft:compass", 1000);
	}
})
```

## 三、ItemStackJS

### 1、属性

| 属性               | 功能               | 返回值             |
| ---------------- | ---------------- | --------------- |
| id               | 返回该物品组的id        | 字符串             |
| tags             | 返回该物品组的tags      | Collection tags |
| count            | 设置/返回该物品组中物品数量   | int             |
| block            | 返回当前物品是否为方块      | 布尔值             |
| nbt              | 返回当前物品组的nbt      | CompoundTag     |
| nbtString        | 返回字符串形式的nbt      | 字符串             |
| name             | 返回当前物品组的名称       | Text            |
| enchantments     | 返回当前物品组的附魔       | MapJS           |
| harvestSpeed     | 返回当前物品组的破坏速度     | 浮点型             |
| itemGroup        | 返回当前物品组在创造物品栏的位置 | 字符串             |
| vanillaPredicate | 获取当前物品谓词(?)      | Predicate       |
| empty            | 返回当前物品组是否为空      | 布尔值             |

### 2、函数

| 函数                                                                                 | 功能                     | 返回值          |
| ---------------------------------------------------------------------------------- | ---------------------- | ------------ |
| hasTag(tag)                                                                        | 判断物品是否有指定tag           | 布尔值          |
| withCount(整形 数量)                                                                   | 返回一个指定数量的物品组           | ItemStackJS  |
| hasNBT()                                                                           | 判断是否具有NBT              | 布尔值          |
| removeNBT()                                                                        | 移除物品的NBT               | void         |
| withNBT(CompoundTag nbt)                                                           | 返回具有指定NBT的物品组          | ItemStackJS  |
| withName(Text 名称)                                                                  | 返回一个具有指定名称的物品组         | ItemStackJS  |
| strongEquals(any)                                                                  | 将当前物品组与给定内容对比(同时比较数量等) | 布尔值          |
| hasEnchantment(字符串 附魔ID, 整形 等级)                                                    | 判断当前物品组是否有给定的附魔        | 布尔值          |
| enchant(字符串 附魔ID, 整形 等级)                                                           | 返回一个添加了指定附魔的物品组        | ItemStackJS  |
| enchant(MapJS 附魔内容)                                                                | 返回一个添加了指定附魔的物品组        | ItemStackJS  |
| ignoreNBT()                                                                        | 返回忽略的NBT的原料(配方相关)      | IngredientJS |
| weakNBT()                                                                          | 返回部分忽略的NBT的原料(配方相关)    | IngredientJS |
| areItemsEqual(ItemStackJS 对比对象)                                                    | 将当前物品组与给定物品组对比         | 布尔值          |
| isNBTEqual(ItemStackJS 对比对象)                                                       | 将当前物品组与给定物品组的NBT对比     | 布尔值          |
| getHarvestLevel(ToolType 工具类型, nullable PlayerJS 玩家, nullable BlockContainerJS 方块) | 返回其挖掘等级                | 整形           |
| getHarvestSpeed(nullable BlockContainerJS 方块)                                      | 返回其挖掘速度                | 浮点型          |

## 四、InventoryJS

你可以使用InventoryJS来快捷地操作玩家物品栏

| 函数                           | 功能                    | 返回值         |
| ---------------------------- | --------------------- | ----------- |
| get(int 格子编号)                | 返回指定格子的物品             | ItemStackJS |
| set(int 格子编号,ItemStackJS 物品) | 设置指定格子的物品             | void        |
| clear()                      | 清空玩家背包                | void        |
| clear(ItemStackJS 物品)        | 清空玩家背包中指定物品           | void        |
| find()                       | 返回玩家背包中有物品的格子编号\[3]   | 整形          |
| find(ItemStackJS 物品)         | 返回玩家背包中有指定物品的格子编号\[3] | 整形          |
| count()                      | 返回物品背包中物品总数\[4]       | 整形          |
| count(ItemStackJS 物品)        | 返回玩家背包中指定物品数\[4]      | 整形          |

\[3] 当无该物品时会返回 -1，有则为最小编号

\[4] 当无物品时会返回 0

下图是玩家物品栏格子编号(图源：Minecraft Wiki)

![](https://patchwiki.biligame.com/images/mc/c/c6/g1edcd1iybb1x4tw6ntsvmvo5auixhl.png)

## 五、PlayerStatsJS

PlayerStatsJS 允许你操作玩家的统计信息中的数据

| 函数                     | 功能                 | 返回值  |
| ---------------------- | ------------------ | ---- |
| get(命名空间)              | 返回玩家指定项的统计信息       | 整形   |
| set(命名空间, 整形 值)        | 设置玩家指定项的统计信息       | void |
| add(命名空间, 整形 值)        | 为玩家指定项的统计信息添加值     | void |
| getBlocksMined(命名空间)   | 返回玩家破坏指定方块的统计信息的值  | 整形   |
| getItemsCrafted(命名空间)  | 返回玩家合成指定物品的统计信息的值  | 整形   |
| getItemsUsed(命名空间)     | 返回玩家使用指定物品的统计信息的值  | 整形   |
| getItemsBroken(命名空间)   | 返回玩家用坏指定物品的统计信息的值  | 整形   |
| getItemsPickedUp(命名空间) | 返回玩家捡起指定物品的统计信息的值  | 整形   |
| getItemsDropped(命名空间)  | 返回玩家丢弃指定物品的统计信息的值  | 整形   |
| getKilled(命名空间)        | 返回玩家杀死指定生物的统计信息的值  | 整形   |
| getKilledBy(命名空间)      | 返回玩家被指定生物杀死的统计信息的值 | 整形   |
