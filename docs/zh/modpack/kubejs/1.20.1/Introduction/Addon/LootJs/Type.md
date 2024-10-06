# LootJs

LootJs 是一个`KubeJS`附属模组，它为`KubeJS`对于原版战利品列表修改进行了更方便的操作 `KubeJS`本身自带的修改 Loot 的方法过于繁琐，若要修改关于:

-   方块
-   实体
-   战利品列表

内的 LootTable

推荐使用`LootJs`来实现

:::v-info 该篇教程源自于 Github 的官方 Wiki

适用于 Minecraft 1.19.2/1.20.1

:::

## 简介

此篇作为一个 LootJs 的基础教程，专门介绍`LootJs`之中的`Type`类型参数，具体使用可以自行尝试

此篇内容不会做过多的讲解

### 装备插槽

Equipment Slots

```js
"MAINHAND";
"OFFHAND";
"FEET";
"LEGS";
"CHEST";
"HEAD";
```

### 战利品类型

Loot Types

```js
LootType.UNKNOWN;
LootType.BLOCK;
LootType.ENTITY;
LootType.CHEST;
LootType.FISHING;
LootType.GIFT;
```

### LootJs 语法

LootContextJS

保存当前战利品掉落的信息，主要用于`.apply()`

#### `getType()`

返回`LootType`

#### `getPosition()`

获取位置

#### `getEntity()`

返回`EntityJs`，如果上下文中不存在实体，则返回 `null`

#### `getKillerEntity()`

返回击杀者的 `EntityJS`，如果上下文中不存在实体，则返回 `null`

#### `getPlayer()`

返回一个 `PlayerJS`，如果上下文中不存在玩家，则返回`null`。 `null`的例子包括骷髅击杀苦力怕

#### `getDamageSource()`

返回一个 `DamageSourceJS`，如果不存在伤害源，则返回 `null`

#### `getTool()`

返回工具的 `ItemStackJS`， 如果上下文中不存在工具，它将返回一个`null`

#### `getDestroyedBlock()`

返回用于块状战利品的 `BlockContainerJS`。 其他战利品类型将为空

#### `isExploded()`

如果战利品是通过爆炸掉落的，则返回 `true`

#### `getExplosionRadius()`

返回爆炸半径，如果 `isExploded()` 返回 false，则半径为 0

#### `getLevel()`

返回`LevelJS`

#### `getServer()`

返回`ServerJS`

#### `getLuck()`

#### `getLooting()`

#### `lootSize()`

#### `addLoot(item)`

#### `removeLoot(ItemFilter)`

#### `findLoot(ItemFilter)`

#### `hasLoot(ItemFilter)`

#### `forEachLoot(callback)`

遍历每个战利品项目并调用回调函数

```js
const callback = (item) => {
	console.log(item);
};
```

### 实体谓词生成器

EntityPredicateBuilderJS

#### anyType(...types)

使用 `#` 作为前缀时，也可以传递实体标记。 例如 `#skeletons`

#### `isOnFire(flag)`

#### `isCrouching(flag)`

#### `isSprinting(flag)`

#### `isSwimming(flag)`

#### `isBaby(flag)`

#### `isInWater(flag)`

#### `isUnderWater(flag)`

#### `isMonster(flag)`

#### `isCreature(flag)`

#### `isOnGround(flag)`

#### `isUndeadMob(flag)`

#### `isArthropodMob(flag)`

#### `isIllegarMob(flag)`

#### `isWaterMob(flag)`

#### `hasEffect(effect, amplifier)`

#### `hasEffect(effect)`

#### `nbt(json)`

#### `matchMount(callback)`

匹配当前实体的坐标，`LootJs` 为回调函数提供了 `EntityPredicateBuilderJS`

```js
LootJS.modifiers((event) => {
	event
		.addLootTypeModifier([LootType.ENTITY])
		.matchEntity((entity) => {
			entity.anyType("#skeletons");
			entity.matchMount((mount) => {
				mount.anyType("minecraft:spider");
			});
		})
		.addLoot("minecraft:magma_cream");
});
```

这个例子举例的是骷髅骑着蜘蛛，当骷髅骑着蜘蛛的时候杀死骷髅会掉落岩浆膏，如果骷髅没有骑着蜘蛛的时候，不会掉落岩浆膏

#### `matchTargetedEntity(callback)`

为当前实体匹配目标实体，`LootJS` 为回调函数提供了 `EntityPredicateBuilderJS`

#### `matchSlot(slot, ItemFilter)`

如果物品槽中包含指定的 `ItemFilter`，则返回 `true`

### 伤害来源谓词生成器

DamageSourcePredicateBuilderJS

#### `anyType(...types)`

如果至少有一种类型匹配，则返回 true

类型有:

```js
"inFire",
"lightningBolt",
"onFire",
"lava",
"hotFloor",
"inWall",
"cramming",
"drown",
"starve",
"cactus",
"fall",
"flyIntoWall",
"outOfWorld",
"generic",
"magic",
"wither",
"anvil",
"fallingBlock",
"dragonBreath",
"dryout",
"sweetBerryBush";
```

其他模组可能会添加更多的伤害类型，请自行查看

#### `isProjectile(flag)`

#### `isExplosion(flag)`

#### `doesBypassArmor(flag)`

#### `doesBypassInvulnerability(flag)`

#### `doesBypassMagic(flag)`

#### `isFire(flag)`

#### `isMagic(flag)`

#### `isLightning(flag)`

#### `matchDirectEntity(callback)`

匹配直接实体，`LootJS` 为回调提供了 `EntityPredicateBuilderJS`

#### `matchSourceEntity(callback)`

匹配来源实体，`LootJS` 为回调提供了 `EntityPredicateBuilderJS`

### 物品选择器

ItemFilter

`ItemFilters` 可用作 `LootJS` 中不同功能的物品过滤器。 在需要使用 `ItemFilter` 的地方，你也可以使用 `KubeJS` 中的 `Item.of()` 和 `Ingredient.of()`

`ItemFilter` 还支持以下过滤器:

```js
temFilter.ALWAYS_FALSE;
ItemFilter.ALWAYS_TRUE;
ItemFilter.SWORD;
ItemFilter.PICKAXE;
ItemFilter.AXE;
ItemFilter.SHOVEL;
ItemFilter.HOE;
ItemFilter.TOOL;
ItemFilter.POTION;
ItemFilter.HAS_TIER;
ItemFilter.PROJECTILE_WEAPON;
ItemFilter.ARMOR;
ItemFilter.WEAPON;
// 适用于所有刀剑、工具、三叉戟和射弹武器
ItemFilter.HEAD_ARMOR;
ItemFilter.CHEST_ARMOR;
ItemFilter.LEGS_ARMOR;
ItemFilter.FEET_ARMOR;
ItemFilter.FOOD;
ItemFilter.DAMAGEABLE;
ItemFilter.DAMAGED;
ItemFilter.ENCHANTABLE;
ItemFilter.ENCHANTED;
ItemFilter.BLOCK;
// 匹配项目是否可以作为方块放置
ItemFilter.hasEnchantment(enchantment, minLevel, maxLevel);
ItemFilter.hasEnchantment(enchantment);
// 检查物品是否附有附魔
```

仅在 Forge 上也可以使用：

-   ForgeItemFilter.canPerformAnyAction(...action)
-   ForgeItemFilter.canPerformAction(...action)

Forge 上的现有的默认操作:

```js
"axe_dig",
"pickaxe_dig",
"shovel_dig",
"hoe_dig",
"sword_dig",
"shears_dig",
"axe_strip",
"axe_scrape",
"axe_wax_off",
"shovel_flatten",
"sword_sweep",
"shears_harvest",	
"shears_carve",
"shears_disarm",
"till",
"shield_block",
"fishing_rod_cast";
```

其他模组可能会添加更多的操作，请自行查看

### 数字提供者

NumberProvider

`NumberProvider` 是一个原始对象，其类型由 `KubeJS` 包装，如果函数需要一个`数字提供者`，您可以传递一个简单的数字来使用常量值，也可以传递一个具有`最小值`和`最大值`的数组 `[min, max] -> [3, 10]`，或者如果您想使用`二项分布`，可以使用 `{ n: a, p: b} -> { n: 3, p: 0.5 }`。
