# 物品类型

## 基础物品

- 创建子物品。

- subtypes(fn: Internal.Function_\<Internal.ItemStack, Internal.Collection\<Internal.ItemStack\>\>): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 伤害实体时调用，判断是否伤害实体。

- hurtEnemy(context: Internal.Predicate_\<Internal.ItemBuilder$HurtEnemyContext\>): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 使物品不可堆叠（堆叠上限为1）。

- unstackable(): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 对给定的纹理进行着色，例如矿石。

- color(index: number, color: Internal.ItemTintFunction_): Internal.ItemBuilder;

- color(callback: Internal.ItemTintFunction_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 物品完成使用后调用。

- finishUsing(finishUsing: Internal.ItemBuilder$FinishUsingCallback_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 添加属性修饰符。

- modifyAttribute(attribute: ResourceLocation_, identifier: string, d: number, operation: Internal.AttributeModifier$Operation_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品模型。

- parentModel(m: string): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置最大堆叠数量，默认为64。

- maxStackSize(v: number): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置工具说明栏。

- tooltip(text: Internal.Component_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置耐久条长度，默认原版行为。

- barWidth(barWidth: Internal.ToIntFunction_\<Internal.ItemStack\>): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- formattedDisplayName().displayName(name)函数的组合方法。

- formattedDisplayName(name: Internal.Component_): Internal.BuilderBase\<Internal.Item\>;

::: code-group

```js [KubeJS]

```

:::

- 设置物品稀有度。

- rarity(v: Internal.Rarity_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品不会被焚毁

- fireResistant(): Internal.ItemBuilder;

- fireResistant(isFireResistant: boolean): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品显示名。

- displayName(name: Internal.Component_): Internal.BuilderBase\<Internal.Item\>;

::: code-group

```js [KubeJS]

```

:::

- 设置物品使用动画。

- useAnimation(animation: Internal.UseAnim_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 当玩家开始使用物品时调用，例如玩家食用食物时返回true玩家将开始食用。

- use(use: Internal.ItemBuilder$UseCallback_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 玩家未使用完物品松开右键，例如弓，为了防止弓使用完，Minecraft将其设置为了一个很高的值。

- releaseUsing(releaseUsing: Internal.ItemBuilder$ReleaseUsingCallback_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品的容器，例如奶桶的容器是桶。

- containerItem(id: ResourceLocation_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品在熔炉中作为燃料的燃烧的时间。

- burnTime(v: number): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 直接设置物品纹理Json。

- textureJson(json: Internal.JsonObject_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品被使用的持续时间，例如这将决定一个食物要花多久吃掉。

- useDuration(useDuration: Internal.ToIntFunction_\<Internal.ItemStack\>): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 根据给定的键设置项目的纹理。

- texture(key: string, tex: string): Internal.ItemBuilder;

- texture(tex: string): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品最大损伤值（耐久度）。

- maxDamage(v: number): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品动态名字。

- name(name: Internal.ItemBuilder$NameCallback_): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 创建物品属性（Properties）

- createItemProperties(): Internal.Item$Properties;

::: code-group

```js [KubeJS]

```

:::

- 设置物品附魔光效。

- glow(v: boolean): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置食物属性。

- food(b: Internal.Consumer_\<Internal.FoodBuilder\>): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 设置物品格式化显示名，覆盖语言文件。

- formattedDisplayName(): Internal.BuilderBase\<Internal.Item\>;

::: code-group

```js [KubeJS]

```

:::

- 设置耐久条颜色，默认原版行为。

- barColor(barColor: Internal.Function_\<Internal.ItemStack, Internal.Color\>): Internal.ItemBuilder;

::: code-group

```js [KubeJS]

```

:::

- 为物品添加标签。

- tag(tag: ResourceLocation_): Internal.BuilderBase\<Internal.Item\>;

## 剑类物品

## 镐类物品

## 斧类物品

## 锹类物品

## 锄类物品

## 头盔类物品

## 胸甲类物品

## 护腿类物品

## 靴子类物品

## 唱片类物品
