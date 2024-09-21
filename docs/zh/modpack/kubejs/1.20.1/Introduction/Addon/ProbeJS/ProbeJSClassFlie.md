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
