# 原料

## 前言

- **`Ingredient`** 多见于配方，对于参与合成的所有物品栈，液体等统称为原料。

## 表示一个物品

```js
Ingredient.of('minecraft:stone')
```

## 表示多个物品

```js
Ingredient.of('minecraft:stone', 2)
```

## 表示一个标签

```js
Ingredient.of('#forge:ores/iron')
```
