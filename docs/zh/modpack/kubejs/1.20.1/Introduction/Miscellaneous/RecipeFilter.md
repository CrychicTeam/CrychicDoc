---
  authors:
  - Eikidona
---

# 配方过滤器

- 常常用于删除配方与修改配方中作为匹配配方的条件。

- **`原料`** 文中所提及的原料，请查看[原料](Ingredient.md)。

- 语句：\{output（输出原料）, id（配方id）, input（输入原料）, type（配方类型）, mod（模组id）, not（非条件）, or（或条件）, group（配方组）\}

## 输出/输入原料条件

::: code-group

```js [输出原料]
// 物品id
{output: Ingredient.of('minecraft:iron_ingot')};
// 标签
{output: Ingredient.of('#forge:ingots/iron')};
// 创造物品栏
{output: Ingredient.of('%minecraft:food_and_drinks')};
// 模组
{output: Ingredient.of('@minecraft')};
// 多个条件（数组）
{output: [Ingredient.of('@minecraft'), {output: Ingredient.of('%minecraft:food_and_drinks')}]};
```

```js [输入原料]
// 物品id
{input: Ingredient.of('minecraft:iron_ingot')};
// 标签
{input: Ingredient.of('#forge:ingots/iron')};
// 创造物品栏
{input: Ingredient.of('%minecraft:food_and_drinks')};
// 模组
{input: Ingredient.of('@minecraft')};
// 多个条件（数组）
{input: [Ingredient.of('@minecraft'), {output: Ingredient.of('%minecraft:food_and_drinks')}]};
```

:::

## 配方id

```js
{id: 'minecraft:iron_ingot_from_nuggets'};
```

- 快速获取配方id，JEI演示。

- 按键设置搜索‘复制配方id到剪切板’，设置一个按键，对合成输出的物品按R打开JEI合成提示界面，鼠标移动至输出物品，按刚刚设置的按键。

## 配方类型

```js
{type: 'minecraft:crafting_shaped'};
```

- 便捷获取整合包中所有配方类型id

- 在你装有Probejs且已经/probejs dump 生成了的前提下。

- 在你的vscode中复制下方这段，Ctrl点击‘CreativeModeTab’。

```js
/**
 * @type {Special.RecipeSerializer}
 */
```

## 模组id

```js
{mod: '@minecraft'};
```

## 配方组

```js
{group:'string'};
```

## 非

- 语句：\{not:\{配方过滤器\}\}

```js
{not: {mod: '@minecraft'}};
```

## 或

- 语句：\{ \{配方过滤器\}, or:\{配方过滤器\} \}

```js
{mod: '@create', or: {mod: '@minecraft'}};
```
