---
  authors:
  - Eikidona
---

# 修改配方

- **`配方过滤器`** 修改配方需根据条件判断是否修改，这些专门用于判断配方的条件统称配方过滤器。

- **`原料`** 把参与配方合成的物品、物品标签、流体等统称为原料，表示原料请查看[传送门](ItemAndIngredient.md)。

## 替换输入/输出物品

- 替换输入语句：event.replaceInput(配方过滤器，要替换的原料，替换原料);

- 替换输出语句：event.replaceOutput(配方过滤器，要替换的原料，替换原料);

### 按输出物品

::: code-group

```js [物品id]
// 如果输出物品id是铁锭，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({output: 'minecraft:iron_ingot'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输出物品id是铁锭，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({output: 'minecraft:iron_ingot'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

```js [物品标签]
// 如果输出物品标签有铁锭，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({output: '#forge:ingots/iron'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输出物品标签有铁锭，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({output: '#forge:ingots/iron'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

```js [物品模组]
// 如果输出物品来自minecraft，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({output: '@minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输出物品来自minecraft，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({output: '@minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

```js [创造物品栏]
// 如果输出物品来自'%minecraft:food_and_drinks'（食物与饮品）
// 替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({output: '%minecraft:food_and_drinks'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输出物品来自'%minecraft:food_and_drinks'（食物与饮品）
// 替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({output: '%minecraft:food_and_drinks'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
/**
 * 便捷获取整合包中所有创造物品栏id
 * 在你装有Probejs且已经/probejs dump 生成了的前提下。
 * 在你的vscode中复制本段，Ctrl点击‘CreativeModeTab’。
 * @type {Special.CreativeModeTab}
 */ 
```

:::

```js
{output?: Internal.ReplacementMatch_, id?: Special.RecipeId, input?: Internal.ReplacementMatch_, type?: Special.RecipeSerializer, mod?: Special.Mod, not?: Internal.RecipeFilter_, or?: Internal.RecipeFilter_, group?: string}
```

### 按输入物品

::: code-group

```js [物品id]
// 如果输入物品id是铁锭，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({input: 'minecraft:iron_ingot'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输入物品id是铁锭，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({input: 'minecraft:iron_ingot'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

```js [物品标签]
// 如果输入物品标签有铁锭，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({input: '#forge:ingots/iron'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输入物品标签有铁锭，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({input: '#forge:ingots/iron'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

```js [物品模组]
// 如果输入物品来自minecraft，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({input: '@minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输入物品来自minecraft，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({input: '@minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

```js [创造物品栏]
// 如果输入物品来自'%minecraft:food_and_drinks'（食物与饮品）
// 替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({input: '%minecraft:food_and_drinks'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果输入物品来自'%minecraft:food_and_drinks'（食物与饮品）
// 替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({input: '%minecraft:food_and_drinks'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
/**
 * 便捷获取整合包中所有创造物品栏id
 * 在你装有Probejs且已经/probejs dump 生成了的前提下。
 * 在你的vscode中复制本段，Ctrl点击‘CreativeModeTab’。
 * @type {Special.CreativeModeTab}
 */ 
```

:::

### 按配方id

```js
// 如果配方来自minecraft，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({id: 'minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果配方来自minecraft，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({id: 'minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
```

- 快速获取配方id，JEI演示。

- 按键设置搜索‘复制配方id到剪切板’，设置一个按键，对合成输出的物品按R打开JEI合成提示界面，鼠标移动至输出物品，按刚刚设置的按键。

### 按来源模组

```js
// 如果配方来自minecraft，替换输入的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceInput({mod: 'minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})

// 如果配方来自minecraft，替换输出的“生铁”为“生铁标签”
ServerEvents.recipes(event => {
    event.replaceOutput({mod: 'minecraft'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
/**
 * 便捷获取整合包中所有模组id
 * 在你装有Probejs且已经/probejs dump 生成了的前提下。
 * 在你的vscode中输入下方这串，Ctrl点击‘Mod’。
 * @type {Special.Mod}
 */ 
```

### 按配方类型

- 将所有'minecraft:crafting_shaped'（有序合成）类型的配方，中的生铁替换为生铁标签。

```js
// 替换输入
ServerEvents.recipes(event => {
    event.replaceInput({type: 'minecraft:crafting_shaped'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
// 替换输出
ServerEvents.recipes(event => {
    event.replaceOutput({type: 'minecraft:crafting_shaped'}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
/**
 * 便捷获取整合包中所有配方类型id
 * 在你装有Probejs且已经/probejs dump 生成了的前提下。
 * 在你的vscode中复制本段，Ctrl点击‘CreativeModeTab’。
 * @type {Special.RecipeSerializer}
 */ 
```

### 按或条件

- ‘或’意味着不必满足所有条件组，而是满足其中一个条件组即可通过。

- 将所有'minecraft:crafting_shaped'（有序合成）类型或'minecraft:crafting_shapeless'（无序合成）类型的配方，中的生铁替换为生铁标签。

```js
// 替换输入
ServerEvents.recipes(event => {
    event.replaceInput({type: 'minecraft:crafting_shaped', or:{type: 'minecraft:crafting_shapeless'}}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
// 替换输出
ServerEvents.recipes(event => {
    event.replaceOutput({type: 'minecraft:crafting_shaped', or:{type: 'minecraft:crafting_shapeless'}}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
/**
 * 便捷获取整合包中所有配方类型id
 * 在你装有Probejs且已经/probejs dump 生成了的前提下。
 * 在你的vscode中复制本段，Ctrl点击‘CreativeModeTab’。
 * @type {Special.RecipeSerializer}
 */ 
```

### 按非条件

- ‘非’意味着不满足所有条件组才可通过。

- 将所有不是'minecraft:crafting_shapeless'（无序合成）类型的配方中的生铁替换为生铁标签。

```js
// 替换输入
ServerEvents.recipes(event => {
    event.replaceInput({not:{type: 'minecraft:crafting_shapeless'}}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
// 替换输出
ServerEvents.recipes(event => {
    event.replaceInput({not:{type: 'minecraft:crafting_shapeless'}}, 
    Ingredient.of('minecraft:raw_iron'), 
    Ingredient.of('#forge:raw_materials/iron'))
})
/**
 * 便捷获取整合包中所有配方类型id
 * 在你装有Probejs且已经/probejs dump 生成了的前提下。
 * 在你的vscode中复制本段，Ctrl点击‘CreativeModeTab’。
 * @type {Special.RecipeSerializer}
 */ 
```
