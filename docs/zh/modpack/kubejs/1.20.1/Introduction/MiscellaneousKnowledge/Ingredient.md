# 原料

## 前言

- **`Ingredient`** 多见于配方，对于参与合成的所有物品栈，液体等统称为原料。

## 表示一个物品栈

::: code-group

```js [字符串]
'minecraft:stone'
```

```js [对象字面量]
{item: 'minecraft:stone'}
```

```js [原料对象]
Ingredient.of('minecraft:stone')
```

```js [从物品栈获取]
Item.of('minecraft:stone').asIngredient()
```

:::

## 表示多个物品栈

::: code-group

```js [字符串]
'2x minecraft:iron_ingot'
```

```js [对象字面量]
{count: 2, item: 'minecraft:stone'}
```

```js [原料对象]
Ingredient.of('minecraft:stone', 2)
```

```js [从物品栈获取]
Item.of('minecraft:stone', 2).asIngredient()
```

:::

## 表示带有NBT的物品栈

::: code-group

```js [对象字面量]
{nbt: '{CustomNBT:"kubejs:custom"}', item: 'minecraft:stone'}
/**
 * 还可以带有数量
 * {count: 1, nbt: '{CustomNBT:"kubejs:custom"}', item: 'minecraft:stone'}
 */ 
```

```js [物品栈]
// 相当于弱检查NBT
Item.of('minecraft:stone', '{CustomNBT:"kubejs:custom"}').asIngredient()
/**
 * 还可以带有数量
 * Item.of('minecraft:stone', 1, '{CustomNBT:"kubejs:custom"}').asIngredient()
 */ 
```

```js [弱检查NBT]
// 与asIngredient()相同，检查目标只需包含该NBT即可
Item.of('minecraft:stone', '{CustomNBT:"kubejs:custom"}').weakNBT()
/**
 * 还可以带有数量
 * Item.of('minecraft:stone', 1, '{CustomNBT:"kubejs:custom"}').weakNBT()
 */
```

```js [强检查NBT]
// 与asIngredient()相同，检查目标NBT必须完全相同
Item.of('minecraft:stone', '{CustomNBT:"kubejs:custom"}').strongNBT()
/**
 * 还可以带有数量
 * Item.of('minecraft:stone', 1, '{CustomNBT:"kubejs:custom"}').strongNBT()
 */
```

:::

## 表示一个标签

- 原料中将持有该标签下所有物品。

::: code-group

```js [字符串]
'#forge:ores/iron'
```

```js [原料对象]
Ingredient.of('#forge:ores/iron')
```

:::

## 表示某个创造物品栏

- 原料中将持有该创造物品栏下所有物品。

::: code-group

```js [字符串]
'%minecraft:functional_blocks'
```

```js [原料对象]
Ingredient.of('%minecraft:functional_blocks')
```

:::

## 表示某个模组

- 原料中将持有该创造物品栏下所有物品。

::: code-group

```js [字符串]
'@minecraft'
```

```js [原料对象]
Ingredient.of('@minecraft')
```

:::

## 表示全部

::: code-group

```js [字符串]
'*'
```

```js [原料对象]
Ingredient.of('*')
```

:::
