# 物品修饰器

> [!WARNING] 施工警戒线
> 该页面待施工。

## 概述

- 作用：用于对物品施加单个或多个操作，例如使空地图变为指向某个标签中结构的寻宝地图。

> [!WARNING] 注意
> 由于大部分物品修饰器并不被KubeJS原生支持，只能以Json传入，本文已尝试给出示例但观感极差，因此建议在[minecraft-wiki/物品修饰器#数据格式](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8#%E6%95%B0%E6%8D%AE%E6%A0%BC%E5%BC%8F)了解某个修饰器的作用后使用[数据包生成器#物品修饰器](https://misode.github.io/item-modifier/)来快速书写物品修饰器。

## 物品修饰器类型

### 应用奖励公式

- 作用：根据指定魔咒的等级，增加物品数量。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 复制显示名

- 作用：将物品的名称设置为对应实体或方块实体的名称。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 复制NBT

- 作用：从指定类型的数据源复制NBT到物品。

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

- 作用：将对战利品项从附魔列表中随机附魔。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 给予等价于经验等级的随机魔咒

- 作用：对战利品项执行一次数字提供器返回的等级的附魔。

- 语句：

::: code-group

```js [KubeJS]

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

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 限制堆叠数量

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

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置物品数量

- 作用：设置该物品的数量。

- 语句：

::: code-group

```js [KubeJS]

```

```json [Json文本]

```

:::

### 设置损伤值

- 作用：设置工具的损坏值。

- 语句：damage(损伤值\: [数字提供器](../MiscellaneousKnowledge/NumberProvider.md))

::: code-group

```js [KubeJS]

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

- 作用：为一个容器方块物品设定战利品表。

- 语句：

::: code-group

```js [KubeJS]

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
