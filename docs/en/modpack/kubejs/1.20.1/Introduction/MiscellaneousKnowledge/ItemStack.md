# 物品栈

## 前言

- **`ItemStack`** 游戏中最常见的对象，玩家背包内每一个“物品”都是物品栈，物品栈描述其代表什么物品，数量，NBT等。

## 表示

### 表示一个物品

- 示例：一个石头

::: code-group

```js [字符串]
'minecraft:stone'
```

```js [对象]
Item.of('minecraft:stone')
```

:::

### 表示多个物品

- 示例：2个石头

::: code-group

```js [字符串]
'2x minecraft:stone'
```

```js [对象]
Item.of('minecraft:stone', 2)
```

:::

### 表示带NBT的物品

- 示例：带nbt的石头

```js
Item.of('minecraft:stone', '{test:666}')
```

- 示例：2个带nbt的石头

```js
Item.of('minecraft:stone', 2, '{test:666}')
```

### 表示带附魔的物品

- 示例：带1级抢夺的石头

```js
Item.of('minecraft:stone').enchant('minecraft:looting', 1)
```

### 表示带有自定义名称的物品

- 名称为可变组件，需要了解[可变组件](../MiscellaneousKnowledge/MutableComponent.md)

- 示例：名称是金色的石头。

```js
Item.of('minecraft:stone').withName(Component.gold('石头'))
```

### 表示带有自定义工具栏提示的物品

- 工具栏提示为可变组件，需要了解[可变组件](../MiscellaneousKnowledge/MutableComponent.md)

- 示例：带有金色的说明的石头。

```js
Item.of('minecraft:stone').withLore(Component.gold('说明'))
```
