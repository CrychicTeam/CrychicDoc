# 工作台

## 前言

- 工作台相关的配方类型。

- 需在ServerEvents.recipes事件中使用。

## 配方类型

### 有序合成

- 原料需以确定顺序、排列的合成称为有序合成。

- 表示输出物品栈需要了解[物品栈](../../../MiscellaneousKnowledge/ItemStack.md)

- 表示原料需要了解[原料](../../../MiscellaneousKnowledge/Ingredient.md)

- 语句：event.recipes.kubejs.shapeless(输出物品栈, 图案, 图案字符代表的原料);

- 示例：木剑 + 铁锭 + 铁锭 = 铁剑

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shaped(
        Item.of('minecraft:iron_sword', '{Damage:0}'),
        // 图案是一个包含三个字符串的数组，每个字符串有3个字符，空格代表该槽位没有物品
        // 相同的物品使用同一个字母
        [
            '  A',
            ' A ',
            'B  '
        ],
        // 这里将声明字母代表的原料
        {
            A: 'minecraft:iron_ingot',
            B: Item.of('minecraft:wooden_sword', '{Damage:0}')
        }
    )
})
```

### 无序合成

- 原料无需以确定顺序、排列的合成称为无序合成。

- 表示输出物品栈需要了解[物品栈](../../../MiscellaneousKnowledge/ItemStack.md)

- 表示原料需要了解[原料](../../../MiscellaneousKnowledge/Ingredient.md)

- 语句：event.recipes.kubejs.shapeless(输出物品栈, 原料数组)

- 示例：木剑 + 铁锭 + 铁锭 => 铁剑

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shapeless(
        Item.of('minecraft:iron_sword', '{Damage:0}'),
        [
            Item.of('minecraft:wooden_sword', '{Damage:0}'),
            'minecraft:iron_ingot',
            'minecraft:iron_ingot'
        ]
    )
})
```

## 配方修饰

> [!WARNING] 注意
> 添加无序/有序合成时建议使用event.recipes.kubejs而不是event.recipes.minecraft，因为后者不支持配方修饰。

### 替换原料

- 合成后会将原料替换为给定的物品栈。

- 需了解[原料动作过滤器](../../../MiscellaneousKnowledge/IngredientActionFilter.md)

- 语句：replaceIngredient(原料动作过滤器, 物品栈);

- 示例：木剑 + 2x铁锭 => 铁剑，铁锭将变为橡木木板。

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shaped(
        Item.of('minecraft:iron_sword', '{Damage:0}'),
        [
            '  A',
            ' A ',
            'B  '
        ],
        {
            A: 'minecraft:iron_ingot',
            B: Item.of('minecraft:wooden_sword', '{Damage:0}')
        }
    ).replaceIngredient('minecraft:iron_ingot', 'minecraft:oak_planks')
})
```

### 损伤原料

- 合成后将对原料造成耐久度损耗。

- 需了解[原料动作过滤器](../../../MiscellaneousKnowledge/IngredientActionFilter.md)

- 语句：

- 示例：木剑 + 铁锭 => 10铁粒，扣除木剑1点耐久

::: code-group

```js [不指定扣除耐久]
ServerEvents.recipes(event => {
    event.recipes.kubejs.shapeless(
        '10x minecraft:iron_nugget',
        [
            Item.of('minecraft:wooden_sword', '{Damage:0}'),
            'minecraft:iron_ingot'
        ]
    ).damageIngredient(Item.of('minecraft:wooden_sword', '{Damage:0}'))
})
```

```js [指定扣除耐久]
ServerEvents.recipes(event => {
    event.recipes.kubejs.shapeless(
        '10x minecraft:iron_nugget',
        [
            Item.of('minecraft:wooden_sword', '{Damage:0}'),
            'minecraft:iron_ingot'
        ]
    ).damageIngredient(Item.of('minecraft:wooden_sword', '{Damage:0}'), 1)
})
```

:::

### 保持原料

- 合成后将保持原料不会消失。

- 需了解[原料动作过滤器](../../../MiscellaneousKnowledge/IngredientActionFilter.md)

- 语句：keepIngredient(配方动作过滤器);

- 示例：铁镐 + 铁矿石 => 2x粗铁，铁镐不会消失。

```js

ServerEvents.recipes(event => {
    event.recipes.kubejs.shapeless(
        '2x minecraft:raw_iron',
        [
            Item.of('minecraft:iron_pickaxe', '{Damage:0}'),
            'minecraft:iron_ore'
        ]
    ).keepIngredient(Item.of('minecraft:iron_pickaxe', '{Damage:0}'))
})
```
