# 物品修饰器

> [!WARNING] 施工警戒线
> 该页面待施工。

## 概述

- 作用：用于对物品施加单个或多个操作，例如使空地图变为指向某个标签中结构的寻宝地图。

::: warning 注意

- 一些物品修饰器类型并没有被KubeJS提供原生支持，需写为Json文本格式作为addFunction(...Json)函数的参数传递，因此在不被KubeJS原生支持的谓词中会给出可参考链接与数据包生成器来协助使用物品修饰器。

:::

## 物品修饰器类型

### 应用奖励公式

- 作用：将预定义的奖励公式应用于物品栈的计数。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 复制实体显示名

- 作用：将实体或方块实体的显示名复制到物品栈NBT中。

- 语句：copyName(战利品表上下文实体\: [Internal.CopyNameFunction$NameSource_](../Addon/ProbeJS/ProbeJSClassFlie.md#lootcontextentitytarget_))

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.copyName("this")// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 复制NBT

- 作用：从指定类型的数据源复制NBT到物品栈。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 复制方块状态

- 作用：当物品是由方块产生时，复制方块的方块状态到物品的block_state；否则此物品修饰器不做任何处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 随机附魔

- 作用：为物品附上一个随机的魔咒。魔咒的等级也是随机的。

- 语句：enchantRandomly(附魔id数组\: ResourceLocation\[\]);

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.enchantRandomly([
            "minecraft:aqua_affinity",
            "minecraft:bane_of_arthropods"
        ]);
    })
})
```

```json [Json文本]

```

:::

### 给予等价于经验等级的随机魔咒

- 作用：使用指定的魔咒等级附魔物品（大约等效于使用这个等级的附魔台附魔物品）。

- 语句：enchantWithLevels(附魔等级\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md), 是否包含宝藏附魔\: Boolean)

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.enchantWithLevels({ min: 1, max: 5 }, true)// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置探险家地图

- 作用：将普通的地图物品变为一个指引到某个结构的探险家地图。如果物品不是地图，则不做任何处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 爆炸损耗

- 作用：如果物品是因为方块被爆炸破坏而产生，执行该函数的每个物品有1/爆炸半径的概率消失，堆叠的物品会被分为多个单独的物品计算；否则此物品修饰器不做任何处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 填充玩家头颅

- 作用：将玩家头颅设置为指定玩家的头颅。如果物品不是玩家头颅则不做任何处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 熔炉熔炼

- 作用：将物品转变为用熔炉烧炼后的对应物品。如果物品不可烧炼，则不做任何处理。

- 语句：furnaceSmelt()

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.furnaceSmelt()// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 限制物品栈数量

作用：限制物品数量。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置内容物

- 作用：对物品的内容物进行处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 引用物品修饰器

- 作用：引用另一个物品修饰器。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置属性

- 作用：为物品加上属性修饰符。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置旗帜图案

- 作用：设置旗帜物品的图案。如果物品不是旗帜，则此修饰器不做任何处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 根据抢夺魔咒调整物品数量

- 作用：决定了抢夺魔咒对该物品数量的影响。如果未使用，抢夺魔咒将对该物品没有效果。

- 语句：lootingEnchant(每级抢夺增加的物品掉落数量\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md), 最大掉落数量\: Number)

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.lootingEnchant({ min: 1, max: 5 }, 3)// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置物品数量

- 作用：设置该物品的数量。

- 语句：count(数量\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md))

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.count({ min: 1, max: 5 })// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置损伤值

- 作用：设置工具的损坏值。

- 语句：damage(损伤值\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md))

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.damage({ min: 1, max: 5 })// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置魔咒

- 作用：设置物品的魔咒。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置乐器

- 作用：设置山羊角的种类。如果物品不是山羊角则不做任何处理。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置战利品表

- 作用：设置放置和打开容器方块时的战利品表。

- 语句：lootTable(战利品表id\: ResourceLocation, \[可选\]战利品表种子\: Number)

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.lootTable('minecraft:archaeology/desert_pyramid', 233)// [!code ++]
    })
})
```

```json [Json文本]

```

:::

### 设置物品描述

- 作用：为物品添加描述信息。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置物品名

- 作用：添加或修改物品的自定义名称。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置NBT

- 作用：设置物品NBT数据。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置药水

- 作用：设置物品包含的药水效果标签。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置迷之炖菜状态效果

- 作用：为谜之炖菜添加状态效果。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

## 有条件的物品修饰器(物品条件函数)

- 作用：使一些物品修饰器有条件的应用。

- 语句：addConditionalFunction(回调函数(物品条件函数\: [Internal.ConditionalFunction](../Addon/ProbeJS/ProbeJSClassFlie.md#conditionalfunction)));

::: code-group

```js [KubeJS]
ServerEvents.entityLootTables(event => {
    event.addEntity("minecraft:husk", loot => {
        loot.addConditionalFunction(cf => {// [!code ++]
            // 添加谓词，还可以通过链式调用继续添加
            cf.survivesExplosion()// [!code ++]
            // 添加物品修饰器，还可以通过链式调用继续添加
            cf.name(Component.red('爆炸残余物'))// [!code ++]
        })// [!code ++]
    })
})
```

:::
