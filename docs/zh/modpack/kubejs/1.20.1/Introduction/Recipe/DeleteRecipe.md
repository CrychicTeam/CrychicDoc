---
  authors:
  - Eikidona
---

# 修改与删除配方

- **`配方过滤器`** 删除配方需根据条件判断是否删除，这些专门用于判断配方的条件统称配方过滤器，为表示配方过滤器需要了解[配方过滤器](../MiscellaneousKnowledge/RecipeFilter.md)。

- **`原料`** 把参与配方合成的物品、物品标签、流体等统称为原料，为表示原料，需要了解[原料](../MiscellaneousKnowledge/Ingredient.md)。

- 语句：event.remove(配方过滤器);

## 按配方id删除

- 删除了配方id为'minecraft:iron_ingot_from_nuggets'（铁粒合成铁锭）的配方。

```js
ServerEvents.recipes(event => {
    event.remove({id: 'minecraft:iron_ingot_from_nuggets'})
})
```

- 快速获取配方id，JEI演示。

- 按键设置搜索‘复制配方id到剪切板’，设置一个按键，对合成输出的物品按R打开JEI合成提示界面，鼠标移动至输出物品，按刚刚设置的按键。

## 按输出物品删除

### 输出物品id

- 删除所有输出物品id是'minecraft:iron_ingot'（铁锭）的配方。

```js
ServerEvents.recipes(event => {
    event.remove({output: 'minecraft:iron_ingot'})
})
```

### 输出物品标签

- 删除所有输出物品标签在中'minecraft:iron_ingot'的配方。

```js
ServerEvents.recipes(event => {
    event.remove({output: '#forge:ingots/iron'})
})
```

### 输出物品来源模组

- 删除所有输出物品是来自minecraft的物品的配方。

```js
ServerEvents.recipes(event => {
    event.remove({output: '@minecraft'})
})
```

- 便捷获取整合包中所有模组id

- 在你装有Probejs且已经/probejs dump 生成了的前提下。

- 在你的vscode中复制下方这段，Ctrl点击‘Mod’。

```js
/**
 * @type {Special.Mod}
 */
```

### 输出物品所在创造物品栏

- 删除所有输出物品是在'%minecraft:food_and_drinks'（食物与饮品）创造模式物品栏的配方。

```js
ServerEvents.recipes(event => {
    event.remove({output: '%minecraft:food_and_drinks'})
})
```

- 便捷获取整合包中所有创造物品栏id

- 在你装有Probejs且已经/probejs dump 生成了的前提下。

- 在你的vscode中复制下方这段，Ctrl点击‘CreativeModeTab’。

```js
/**
 * @type {Special.CreativeModeTab}
 */
```

## 按输入物品删除

### 输入物品id

- 删除所有输入物品id是'minecraft:iron_ingot'（铁锭）的配方。

```js
ServerEvents.recipes(event => {
    event.remove({input: 'minecraft:iron_ingot'})
})
```

### 输入物品标签

- 删除所有输入物品标签在中'minecraft:iron_ingot'的配方。

```js
ServerEvents.recipes(event => {
    event.remove({input: '#forge:ingots/iron'})
})
```

### 输入物品来源模组

- 删除所有输入物品是来自minecraft的物品的配方。

```js
ServerEvents.recipes(event => {
    event.remove({input: '@minecraft'})
})
```

### 输入物品所在创造物品栏

- 删除所有输入物品是在'%minecraft:food_and_drinks'（食物与饮品）创造模式物品栏的配方。

```js
ServerEvents.recipes(event => {
    event.remove({input: '%minecraft:food_and_drinks'})
})
```

- 便捷获取整合包中所有创造物品栏id

- 在你装有Probejs且已经/probejs dump 生成了的前提下。

- 在你的vscode中复制下方这段，Ctrl点击‘CreativeModeTab’。

```js
/**
 * @type {Special.CreativeModeTab}
 */
```

## 按配方类型删除

- 删除所有'minecraft:crafting_shaped'（有序合成）类型的配方。

```js
ServerEvents.recipes(event => {
    event.remove({type: 'minecraft:crafting_shaped'})
})
```

- 便捷获取整合包中所有配方类型id

- 在你装有Probejs且已经/probejs dump 生成了的前提下。

- 在你的vscode中复制下方这段，Ctrl点击‘CreativeModeTab’。

```js
/**
 * @type {Special.RecipeSerializer}
 */
```

## 按模组删除

- 删除minecraft的所有配方。

```js
ServerEvents.recipes(event => {
    event.remove({mod: '@minecraft'})
})
```

## 按非逻辑删除

- 上述配方删除方式都是以符合全部条件来删除，此处相反，不符合全部条件的删除。

- 不是minecraft的配方的删除。

```js
ServerEvents.recipes(event => {
    event.remove({not: {mod: '@minecraft'}})
})
```

## 按或逻辑删除

- 上述配方删除方式除了反转逻辑都是以符合全部条件来删除，此处符合or前后的任一条件删除。

- 是来自minecraft的或者vinery的配方则删除。

```js
ServerEvents.recipes(event => {
    event.remove({mod: '@minecraft', or: {mod: '@vinery'}})
})
```

## 按配方组删除

- 几乎用不到，如有兴趣可查看[minecraft-wiki/配方#Json格式](https://zh.minecraft.wiki/w/%E9%85%8D%E6%96%B9#JSON%E6%A0%BC%E5%BC%8F)。

- group是一个字符串，用于为配方分组，默认为空。

```js
ServerEvents.recipes(event => {
    event.remove({group: 'string'})
})
```
