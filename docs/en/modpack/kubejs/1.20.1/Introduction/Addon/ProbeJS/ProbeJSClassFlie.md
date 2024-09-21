# ProbeJS类型文件

## Internal.LootBuilder

### 常用函数

- 清空战利品池。

```js
clearPools(): void;
```

- 从Json添加物品修饰器，返回物品修饰器列表。

```js
addFunction(arg0: Internal.JsonObject_): Internal.FunctionContainer;
```

- 添加谓词，受抢夺附魔影响的随机概率，返回谓词列表[Internal.ConditionContainer](ProbeJSClassFlie.md#internalconditioncontainer)。

```js
randomChanceWithLooting(chance: number, multiplier: number): Internal.ConditionContainer;
```

- ？

```js
name(name: net.minecraft.network.chat.Component_, entity: Internal.LootContext$EntityTarget_): Internal.FunctionContainer;
```

- 将当前战利品表作为Json对象返回。

```js
toJson(): Internal.JsonObject;
```

- 添加战利品池，接受一个回调函数，回调函数参数为[Internal.LootBuilderPool](ProbeJSClassFlie.md#internallootbuilderpool)。

```js
addPool(p: Internal.Consumer_<Internal.LootBuilderPool>): void;
```

- 对战利品耐久造成损伤，返回物品修饰器列表[Internal.FunctionContainer](ProbeJSClassFlie.md#internalfunctioncontainer)。

```js
damage(damage: Internal.NumberProvider_): Internal.FunctionContainer;
```

- 设置战利品数量，返回物品修饰器列表[Internal.FunctionContainer](ProbeJSClassFlie.md#internalfunctioncontainer)。

```js
count(count: Internal.NumberProvider_): Internal.FunctionContainer;
```

- 设置谓词，未被爆炸破坏，返回谓词列表[Internal.ConditionContainer](ProbeJSClassFlie.md#internalconditioncontainer)。

```js
survivesExplosion(): Internal.ConditionContainer;
```

- 清空所有谓词。

```js
clearConditions(): void;
```

- 复制方块实体显示名，需要复制源实体类型

```js
copyName(source: Internal.CopyNameFunction$NameSource_): Internal.FunctionContainer;
```

- 从Json添加谓词，返回LootBuilder对象本身。

```js
addCondition(o: Internal.JsonObject_): this;
```

- 引用其他战利品表，需要战利品表id与作为随机种子的数字，返回物品修饰器列表[Internal.FunctionContainer](ProbeJSClassFlie.md#internalfunctioncontainer)。

```js
lootTable(table: ResourceLocation_, seed: number): Internal.FunctionContainer;
```

- 为战利品给予等价于经验等级的随机魔咒，需要[数字提供器](../../MiscellaneousKnowledge/NumberProvider.md)表示等级，是否包含宝藏附魔，返回物品修饰器列表[Internal.FunctionContainer](ProbeJSClassFlie.md#internalfunctioncontainer)。

```js
enchantWithLevels(levels: Internal.NumberProvider_, treasure: boolean): Internal.FunctionContainer;
```

- 随机附魔，需要附魔id数组，返回物品修饰器列表[Internal.FunctionContainer](ProbeJSClassFlie.md#internalfunctioncontainer)。

```js
enchantRandomly(enchantments: ResourceLocation_[]): Internal.FunctionContainer;
```

- 对战利品进行熔炉熔炼，返回物品修饰器列表[Internal.FunctionContainer](ProbeJSClassFlie.md#internalfunctioncontainer)。

```js
furnaceSmelt(): Internal.FunctionContainer;
```

- 添加谓词，检查实体属性

```js
entityProperties(entity: Internal.LootContext$EntityTarget_, properties: Internal.JsonObject_): Internal.ConditionContainer;
```

- 清空所有的物品修饰器。

```js
clearFunctions(): void;
```

- 

```js
lootingEnchant(count: Internal.NumberProvider_, limit: number): Internal.FunctionContainer;
```

- 

```js
addConditionalFunction(func: Internal.Consumer_<Internal.ConditionalFunction>): Internal.FunctionContainer;
```

- 

```js
randomChance(chance: number): Internal.ConditionContainer;
```

- 

```js
killedByPlayer(): Internal.ConditionContainer;
```

- 

```js
nbt(tag: Internal.CompoundTag_): Internal.FunctionContainer;
```

- 

```js
entityScores(entity: Internal.LootContext$EntityTarget_, scores: Internal.Map_<string, any>): Internal.ConditionContainer;
```

- 

```js
name(name: net.minecraft.network.chat.Component_): Internal.FunctionContainer;
```

### 常用属性

-

```js
type: string;
```

-

```js
conditions: Internal.JsonArray;
```

-

```js
pools: Internal.JsonArray;
```

-

```js
functions: Internal.JsonArray;
```

-

```js
customId: ResourceLocation;
```  

## Internal.LootBuilderPool

### 常用函数

### 常用属性

## Internal.ConditionContainer

### 常用函数

### 常用属性

## Internal.FunctionContainer

### 常用函数

### 常用属性

## Internal.CopyNameFunction$NameSource_

- 复制方块实体显示名物品修饰器的源实体。

### 枚举值

- 可用值："killer" | "killer_player" | "this" | "block_entity"; 实际上这里仅应该填"block_entity"
