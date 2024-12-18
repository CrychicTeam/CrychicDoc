---
progress: 0
state: unfinished
---
# ProbeJS类型文件

## LootBuilder

### 常用函数

- 清空战利品池。

```js
clearPools(): void;
```

- 从Json添加物品修饰器，返回物品修饰器列表。

```js
addFunction(arg0: Internal.JsonObject_): Internal.FunctionContainer;
```

- 添加谓词，受抢夺附魔影响的随机概率。
- 返回：谓词列表[Internal.ConditionContainer](#lootbuilder)

```js
randomChanceWithLooting(chance: number, multiplier: number): Internal.ConditionContainer;
```

- 设置显示名，具有两个方法重载。
- 参数1：显示名\: [Component_](../../MiscellaneousKnowledge/Components.md)
- 参数2？：战利品表上下文实体\: [Internal.LootContext$EntityTarget_](#lootcontextentitytarget_)
- 返回：谓词列表[Internal.ConditionContainer](#lootbuilder)

::: code-group

```js [KubeJS]
name(name: net.minecraft.network.chat.Component_, entity: Internal.LootContext$EntityTarget_): Internal.FunctionContainer;
```

```js [KubeJS]
name(name: net.minecraft.network.chat.Component_): Internal.FunctionContainer;
```

:::

- 将当前战利品表作为Json对象返回。

```js
toJson(): Internal.JsonObject;
```

- 添加战利品池，接受一个回调函数。
- 参数：回调函数(战利品池构造器[Internal.LootBuilderPool](#lootbuilderpool))

```js
addPool(p: Internal.Consumer_<Internal.LootBuilderPool>): void;
```

- 对战利品耐久造成损伤。
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)。

```js
damage(damage: Internal.NumberProvider_): Internal.FunctionContainer;
```

- 设置战利品数量。
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)。

```js
count(count: Internal.NumberProvider_): Internal.FunctionContainer;
```

- 设置谓词，未被爆炸破坏。
- 返回：谓词列表[Internal.ConditionContainer](#conditioncontainer)。

```js
survivesExplosion(): Internal.ConditionContainer;
```

- 清空所有谓词。

```js
clearConditions(): void;
```

- 复制方块实体显示名。
- 参数：复制源实体类型[Internal.CopyNameFunction$NameSource_](#copynamefunctionnamesource_)
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)

```js
copyName(source: Internal.CopyNameFunction$NameSource_): Internal.FunctionContainer;
```

- 从Json添加谓词。

- 返回：自身-战利品表构造器[Internal.LootBuilder](#lootbuilder)

```js
addCondition(o: Internal.JsonObject_): this;
```

- 引用其他战利品表，需要战利品表id与作为随机种子的数字。
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)。

```js
lootTable(table: ResourceLocation_, seed: number): Internal.FunctionContainer;
```

- 为战利品给予等价于经验等级的随机魔咒。
- 参:1：等级[数字提供器](../../MiscellaneousKnowledge/NumberProvider.md)
- 参数2：false | true是否包含宝藏附魔
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)。

```js
enchantWithLevels(levels: Internal.NumberProvider_, treasure: boolean): Internal.FunctionContainer;
```

- 随机附魔，需要附魔id数组。
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)。

```js
enchantRandomly(enchantments: ResourceLocation_[]): Internal.FunctionContainer;
```

- 对战利品进行熔炉熔炼。
- 返回：物品修饰器列表[Internal.FunctionContainer](#functioncontainer)。

```js
furnaceSmelt(): Internal.FunctionContainer;
```

- 添加谓词，检查实体属性。
- 参数1：[战利品表上下文实体](#lootcontextentitytarget_)
- 参数2：实体属性Json
- 返回：谓词列表\: [Internal.ConditionContainer](#conditioncontainer)

```js
entityProperties(entity: Internal.LootContext$EntityTarget_, properties: Internal.JsonObject_): Internal.ConditionContainer;
```

- 清空所有的物品修饰器。

```js
clearFunctions(): void;
```

- 根据抢夺魔咒调整物品数量。
- 参数1：物品数量\: [数字提供器](../../MiscellaneousKnowledge/NumberProvider.md)
- 参数2：最大物品数量\: 数字
- 返回：物品修饰器列表\: [Internal.FunctionContainer](#functioncontainer)

```js
lootingEnchant(count: Internal.NumberProvider_, limit: number): Internal.FunctionContainer;
```

- 添加带有条件的物品修饰器。
- 参数：回调函数(物品条件函数\: [Internal.ConditionalFunction](#conditionalfunction))
- 返回：物品修饰器列表\: [Internal.FunctionContainer](#functioncontainer)

```js
addConditionalFunction(func: Internal.Consumer_<Internal.ConditionalFunction>): Internal.FunctionContainer;
```

- 随机概率。
- 参数：概率数字\[0, 1\]
- 返回：谓词列表\: [Internal.ConditionContainer](#conditioncontainer)

```js
randomChance(chance: number): Internal.ConditionContainer;
```

- 被玩家击杀。
- 返回：谓词列表\: [Internal.ConditionContainer](#conditioncontainer)

```js
killedByPlayer(): Internal.ConditionContainer;
```

- 设置NBT
- 返回：物品修饰器列表\: [Internal.FunctionContainer](#functioncontainer)

```js
nbt(tag: Internal.CompoundTag_): Internal.FunctionContainer;
```

- 检查实体分数。
- 参数1：战利品表上下文实体\: [Internal.LootContext$EntityTarget_](#lootcontextentitytarget_)
- 参数2：键值对\: \{记分板id, 分数\: [数字提供器](../../MiscellaneousKnowledge/NumberProvider.md)\}
- 返回：谓词列表\: [Internal.ConditionContainer](#conditioncontainer)

```js
entityScores(entity: Internal.LootContext$EntityTarget_, scores: Internal.Map_<string, any>): Internal.ConditionContainer;
```

### 常用属性

- 战利品表类型。

```js
type: string;
```

- 战利品表的谓词列表。

```js
conditions: Internal.JsonArray;
```

- 战利品表的战利品池。

```js
pools: Internal.JsonArray;
```

- 战利品表的物品修饰器列表。

```js
functions: Internal.JsonArray;
```

- 战利品表的资源位置。

```js
customId: ResourceLocation;
```  

## LootBuilderPool

### 常用函数

### 常用属性

## ConditionContainer

### 常用函数

### 常用属性

## FunctionContainer

### 常用函数

### 常用属性

## CopyNameFunction$NameSource_

- 复制方块实体显示名物品修饰器的源实体。

### 枚举值

- 实际上用于复制方块实体显示名时，这里仅应该填"block_entity"

- "killer" | "killer_player" | "this" | "block_entity"

- CopyNameFunction$NameSource.KILLER | CopyNameFunction$NameSource.KILLER_PLAYER | CopyNameFunction$NameSource.THIS | CopyNameFunction$NameSource.BLOCK_ENTITY

## LootContext$EntityTarget_

- 战利品表上下文实体

## ConditionalFunction

- 有条件的物品函数。

## ItemBuilder

### 继承关系

### 可用函数

- 设置工具等级。

```js
toToolTier(o: any): Internal.Tier
```

- 添加子类型物品。

```js
subtypes(fn: Internal.Function_<Internal.ItemStack, Internal.Collection<Internal.ItemStack>>): this
```

- 攻击实体时调用。

```js
hurtEnemy(context: Internal.Predicate_<Internal.ItemBuilder$HurtEnemyContext>): this
```

- ?

```js
getTranslationKeyGroup(): string
```

- 从目前的ItemBuilder构造一个Item。

```js
get(): Internal.Item
```

- 使ItemStack不可堆叠（即堆叠大小上限为1）。

```js
unstackable(): this
```

- 对给定索引的项的纹理进行着色。用于着色物品，如GT矿石和粉尘。

::: code-group

```js
color(index: number, color: Internal.ItemTintFunction_): this
```

```js
color(callback: Internal.ItemTintFunction_): this
```

:::

- 当玩家使用完物品时调用（例如吃东西完成后恢复饥饿值）。

```js
finishUsing(finishUsing: Internal.ItemBuilder$FinishUsingCallback_): this
```

- ?

```js
getRegistryType(): Internal.RegistryInfo<any>
```

- 设置属性修饰符。

```js
modifyAttribute(attribute: Internal.Attribute_, identifier: string, d: number, operation: Internal.AttributeModifier$Operation_): this
```

- 设置物品模型。

```js
parentModel(m: string): this
```

- 设置物品最大堆叠数，默认64。

```js
maxStackSize(v: number): this
```

- 设置物品的工具栏提示。

```js
tooltip(text: Internal.Component_): this
```

- 确定物品耐久性条的宽度。默认为原版行为。该函数应该返回0到13之间的值(条的最大宽度)。

```js
barWidth(barWidth: Internal.ToIntFunction_<Internal.ItemStack>): this
```

- formattedDisplayName().displayName(name)的组合方法。

```js
formattedDisplayName(name: Internal.Component_): Internal.BuilderBase<Internal.Item>
```

- 设置物品稀有度。

```js
rarity(v: Internal.Rarity_): this
```

- 设置物品是否可以被焚毁。

::: code-group

```js
fireResistant(): this
```

```js
fireResistant(isFireResistant: boolean): this
```

:::

- 设置物品显示名。

```js
displayName(name: Internal.Component_): Internal.BuilderBase<Internal.Item>
```

- 设置物品使用动画。

```js
useAnimation(animation: Internal.UseAnim_): this
```

- ？

```js
getBuilderTranslationKey(): string
```

- 决定玩家是否使用物品，例如玩家食用物品时返回true允许玩家开始吃食物。

```js
use(use: Internal.ItemBuilder$UseCallback_): this
```

- 物品没有使用完但松开鼠标右键时，例如弓箭松开将箭射出，为了防止弓使用完，Minecraft将弓的使用时间设置为了一个很高的值

```js
releaseUsing(releaseUsing: Internal.ItemBuilder$ReleaseUsingCallback_): this
```

- 设置项目的容器项目，例如，为牛奶桶设置一个桶。

```js
containerItem(id: ResourceLocation_): this
```

- 设置物品在熔炉的燃烧时间。默认值为0(不是燃料)。

```js
burnTime(v: number): this
```

- 直接设置项目的纹理json。

```js
textureJson(json: Internal.JsonObject_): this
```

- \<static\> ?

```js
toArmorMaterial(o: any): Internal.ArmorMaterial;
```

- ？

```js
generateLang(lang: Internal.LangEventJS_): void
```

- 物品被使用的持续时间，例如吃完一个东西需要花费的时间。

```js
useDuration(useDuration: Internal.ToIntFunction_<Internal.ItemStack>): this
```

- ？

::: code-group

```js
texture(key: string, tex: Internal.Attribute_): this
```

```js
texture(tex: Internal.Attribute_): this
```

::: 

- 设置物品的最大损伤值。默认值为0(无耐久度)。

```js
maxDamage(v: number): this
```

- 动态设置项的名称。

```js
name(name: Internal.ItemBuilder$NameCallback_): this
```

- ？

```js
generateDataJsons(generator: Internal.DataJsonGenerator_): void
```

- 设置物品属性（Properties）。

```js
createItemProperties(): Internal.Item$Properties
```

- \<abstract\> ？

```js
createObject(): Internal.Item
```

- 使物品像被附魔一样发光，即使它没有附魔。

```js
glow(v: boolean): this
```

- ?

```js
transformObject(arg0: any): any
```

- 直接设置项目的模型json。

```js
modelJson(json: Internal.JsonObject_): this
```

- 设置该项的食物属性。

```js
food(b: Internal.Consumer_<Internal.FoodBuilder>): this
```

- 使displayName()覆盖语言文件。

```js
formattedDisplayName(): Internal.BuilderBase<Internal.Item>
```

- ？

```js
transformObject(obj: Internal.Item_): Internal.Item
```

- 决定物品耐久性条的颜色。默认为香草行为。

```js
barColor(barColor: Internal.Function_<Internal.ItemStack, Internal.Color>): this
```

- 向该物品添加一个标签，例如:“minecraft:stone”。

```js
tag(tag: ResourceLocation_): Internal.BuilderBase<Internal.Item>
```

- 设置该物品的语言键，例如block.minecraft.stone

```js
translationKey(key: string): Internal.BuilderBase<Internal.Item>
```

- 判断相等性。

```js
equals(arg0: any): boolean
```

- ？

```js
newID(pre: string, post: string): ResourceLocation
```

## BasicItemJSBuilder

- 

```js

```

## SwordItemBuilder

## PickaxeItemBuilder

## AxeItemBuilder

## ShovelItemBuilder

## ShearsItemBuilder

## HoeItemBuilder

## ArmorItemBuilder$Helmet

## ArmorItemBuilder$Chestplate

## ArmorItemBuilder$Leggings

## ArmorItemBuilder$Boots

## RecordItemJS$Builder

## SmithingTemplateItemBuilder

## SequencedAssemblyItemBuilder

## SandpaperItemBuilder
