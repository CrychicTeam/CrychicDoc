# 烟熏炉

## 前言

- 烟熏炉相关的配方类型。

- 需在ServerEvents.recipes事件中使用。

- 一般设置配方的燃烧时间是熔炉同种配方燃烧时间的一半。

## 配方类型

### 烟熏

- 使用熔炉烧物品。

- 语句：event.recipes.minecraft.smoking(输出物品栈，原料)。

- 例子：铁矿石 -> 铁锭

::: code-group

```js [简单]
ServerEvents.recipes(event => {
    event.recipes.minecraft.smoking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore')
    )
})
```

```js [经验]
// 给予玩家10经验，矿石熔炼一般为0.7
ServerEvents.recipes(event => {
    event.recipes.minecraft.smoking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10
    )
})
```

```js [燃烧时间]
// 要烧400个游戏刻，默认为200刻
ServerEvents.recipes(event => {
    event.recipes.minecraft.smoking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10,
        400
    )
})
```

## 配方修饰
