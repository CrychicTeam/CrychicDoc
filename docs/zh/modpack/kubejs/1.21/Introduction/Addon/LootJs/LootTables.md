# Loot Table Event

:::v-info

该篇教程源自于 LootJs 官方网站，

适用于 Minecraft 1.21 及更高版本

:::

此事件允许您直接修改战利品表

该事件在其他 `Mod` 将其战利品更改后使用数据包添加到原版战利品列表后触发，允许你删除或更改它们

## 战利品表

此篇教程是基于`LootJs`新版本添加的`LootJS.lootTables`事件进行的基础教程

### 获取战利品表 ID

`getLootTableIds`

返回所有可用战利品表 ID 的数组，可以进行筛选

使用正则表达式可以对战利品列表进行筛选

使用方法：

-   getLootTableIds()
-   getLootTableIds(filter: string | regex)

使用`console.log()`打印出来后，你将会在`logs/kubejs/server.log`日志文件里面看见所有通过数据包注册的战利品列表 ID

::: code-group

```js [无筛选]
LootJS.lootTables((event) => {
	let ids = event.getLootTableIds();
	console.log(ids);
});
```

```js [使用正则表达式筛选]
LootJS.lootTables((event) => {
	let ids = event.getLootTableIds(/.*chests\/.*/);
	console.log(ids);
});
```

:::

使用正则表达式筛选后，只会打印出所有匹配`chests`这个战利品列表里面的战利品 ID

例如使用`getLootTableIds()`后日志输出为:

```js
[minecraft:chests/bastion_treasure, minecraft:chests/village/village_weaponsmith, minecraft:chests/ruined_portal, minecraft:chests/trial_chambers/reward_ominous_rare, minecraft:chests/shipwreck_supply, minecraft:chests/stronghold_corridor, minecraft:chests/bastion_other, minecraft:chests/trial_chambers/reward_unique, minecraft:chests/village/village_fletcher, minecraft:chests/ancient_city_ice_box, minecraft:chests/village/village_cartographer, minecraft:chests/simple_dungeon, minecraft:chests/village/village_savanna_house, minecraft:chests/village/village_plains_house, minecraft:chests/village/village_taiga_house, minecraft:chests/nether_bridge, minecraft:chests/trial_chambers/intersection_barrel, minecraft:chests/trial_chambers/reward_ominous_unique, minecraft:chests/underwater_ruin_small, minecraft:chests/trial_chambers/reward_ominous_common, minecraft:chests/igloo_chest, minecraft:chests/village/village_armorer, minecraft:chests/village/village_mason, minecraft:chests/trial_chambers/intersection, minecraft:chests/trial_chambers/reward, minecraft:chests/buried_treasure, minecraft:chests/bastion_hoglin_stable, minecraft:chests/ancient_city, minecraft:chests/abandoned_mineshaft, minecraft:chests/trial_chambers/entrance, minecraft:chests/stronghold_crossing, minecraft:chests/village/village_fisher, minecraft:chests/village/village_butcher, minecraft:chests/shipwreck_treasure, minecraft:chests/village/village_toolsmith, minecraft:chests/village/village_temple, minecraft:chests/jungle_temple, minecraft:chests/desert_pyramid, minecraft:chests/trial_chambers/corridor, minecraft:chests/trial_chambers/reward_rare, minecraft:chests/village/village_desert_house, minecraft:chests/pillager_outpost, minecraft:chests/stronghold_library, minecraft:chests/shipwreck_map, minecraft:chests/underwater_ruin_big, minecraft:chests/trial_chambers/supply, minecraft:chests/village/village_shepherd, minecraft:chests/jungle_temple_dispenser, minecraft:chests/woodland_mansion, minecraft:chests/village/village_snowy_house, minecraft:chests/end_city_treasure, minecraft:chests/trial_chambers/reward_ominous, minecraft:chests/village/village_tannery, minecraft:chests/spawn_bonus_chest, minecraft:chests/bastion_bridge, minecraft:chests/trial_chambers/reward_common] [java.util.ArrayList]
```

### 给定 ID 获取战利品表

`getLootTable`

按照所指定的 ID 返回一个战利品表，如果没有这个战利品表的 ID，则返回为`null`

```js
LootJS.lootTables((event) => {
	let table = event.getLootTable("minecraft:chests/simple_dungeon");
	console.log(table);
});
```

### 战利品列表检测

`hasLootTable`

返回具有给定 ID 的战利品表是否存在

如果该战利品列表存在，则返回`true`，否则返回`false`

```js
LootJS.lootTables((event) => {
	let exists = event.hasLootTable("minecraft:chests/simple_dungeon");
	console.log(exists);
});
```

### 清除战利品列表

clearLootTables

清除所有与给定过滤器匹配的战利品表

可以使用选择器来过滤战利品表

语法：

-   `clearLootTables(filter: string | regex)`

下面的示例中使用的是正则表达式来进行过滤，清除有关箱子里面所有的战利品

你也可以直接使用战利品表的 ID 来进行指定清除

```js
LootJS.lootTables((event) => {
	event.clearLootTables(/.*chests.*/);
});
```

下面的示例代码中，指定了史莱姆的战利品表 ID，清除了史莱姆的所有战利品

```js
LootJS.lootTables((event) => {
	event.clearLootTables("minecraft:entities/slime");
});
```

提示：对于战利品表 ID 的获取，游戏内可以使用`/loot`指令来进行获取

### 创建战利品列表

`create`

使用这个语法可以创建一个新的战利品表并应用

语法：

-   create(id: string, type?: LootType)

如果`LootType`未提供，那么默认为`LootType.CHEST`

示例代码：

```js
LootJS.lootTables((event) => {
	event.create("lootjs:table1", LootType.ENTITY).createPool((pool) => {
		// 编辑你的战利品表
	});

	// 使用默认的LootType(CHEST)
	event.create("lootjs:table2").createPool((pool) => {
		// 编辑你的战利品表
	});
});
```
