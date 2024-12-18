# 切石机

## 前言

## 配方类型

### 切石

- 使用切石机合成。

- 语句：event.recipes.minecraft.stonecutting(输出物品栈，原料);

- 例子：铁矿石 -> 生铁

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.stonecutting('minecraft:raw_iron', Ingredient.of('minecraft:iron_ore'))
})
```

## 配方修饰

暂无。
