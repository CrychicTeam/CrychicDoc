---
  authors:
  - Eikidona
---
# 物品与原料表示

- 用于配方等各种需要物品或原料出现的地方。

- 如用于[原版配方添加](Vanilla.md)。

## 物品

### 使用字符串表示

- 一个石头

```js
'minecraft:stone'
```

- 两个石头

```js
'2x minecraft:stone'
```

### 使用对象表示

- 1个石头

```js
Item.of('minecraft:stone')
```

- 2个石头

```js
Item.of('minecraft:stone', 2)
```

- 带nbt的石头

```js
Item.of('minecraft:stone', '{test:666}')
```

- 2个带nbt的石头

```js
Item.of('minecraft:stone', 2, '{test:666}')
```

## 原料

### 表示一个物品

```js
Ingredient.of('minecraft:stone')
```

### 表示多个物品

```js
Ingredient.of('minecraft:stone', 2)
```

### 表示一个标签

```js
Ingredient.of('#forge:ores/iron')
```
