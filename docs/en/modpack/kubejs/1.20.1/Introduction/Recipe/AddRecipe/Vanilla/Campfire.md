# 营火

## 前言

## 配方类型

### 营火烹饪

- 语句：event.recipes.minecraft.campfire_cooking(输出物品栈，输入原料)

- 例子：铁矿石 => 铁锭

::: code-group

```js [简单]
ServerEvents.recipes(event => {
    event.recipes.minecraft.campfire_cooking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore')
    )
})
```

```js [经验]
// 这个配方将提供10点经验
ServerEvents.recipes(event => {
    event.recipes.minecraft.campfire_cooking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10
    )
})
```

```js   [烹饪时间]
// 这个配方将需要400刻的时间
ServerEvents.recipes(event => {
    event.recipes.minecraft.campfire_cooking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10,
        400
    )
})
```

:::

## 配方修饰
