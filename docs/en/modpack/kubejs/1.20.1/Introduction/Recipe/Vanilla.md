# 原版配方

## 前言

- 想知道怎么表示配方中的物品看[物品与原料表示](ItemAndIngredient.md)。

- 想知道怎么修改、删除配方看[修改与删除配方](ModifyDeleteRecipe.md)。

## 工作台

### 有序合成

- 物品需要以特定的图案顺序摆放才能合成。

- 语句：event.recipes.minecraft.crafting_shaped(输出物品 , 形状 , 输入物品)。

- 例子：用 4个水桶 和 4个熔岩桶 合成 4个黑曜石。

::: code-group

```js [KJS的有序合成]
ServerEvents.recipes(event => {
    event.recipes.kubejs.shaped('4x minecraft:obsidian', 
    [
        'LOL',
        'O O',
        'LOL'
    ],
    {
        L: 'minecraft:water_bucket',
        O: 'minecraft:lava_bucket'
    });
})
```

```js [原版的有序合成]
// 区别是minecraft下的有序合成无法使用诸如“返回原料”的操作
ServerEvents.recipes(event => {
    event.recipes.minecraft.crafting_shaped('4x minecraft:obsidian', 
    [
        'LOL',
        'O O',
        'LOL'
    ],
    {
        L: 'minecraft:water_bucket',
        O: 'minecraft:lava_bucket'
    });
})
```

:::

### 无序合成

- 物品不需要以特定顺序摆放即可合成。

- 语句：event.recipes.minecraft.crafting_shapeless(输出物品 , 输入物品)。

- 例子：用 1个海绵 和 1个带有木板标签的物品 合成2个石头。

```js
// 换行为了是避免太长看的难受，可以不换行
ServerEvents.recipes(event => {
    event.recipes.minecraft.crafting_shapeless(Item.of('minecraft:stone', 2), 
    ['minecraft:stone', 
    Ingredient.of('#minecraft:planks')]);
})
```

### 返回原料的有序合成

- 与正常的有序合成相比，该种合成可以返回参与合成的物品，如合成蛋糕时，奶桶返回桶。

- 语句event.recipes.kubejs.shaped(配方).replaceIngredient(原料动作过滤器, 物品);

- 示例：毒药水与金苹果合成苹果，毒药水变成瓶子。

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shaped('minecraft:apple',
        [
            '   ',
            ' AB',
            '   '
        ],
        {
            A: Item.of('minecraft:potion', '{Potion:"minecraft:poison"}'),
            B: 'minecraft:golden_apple'

        }).replaceIngredient(Item.of('minecraft:potion', '{Potion:"minecraft:poison"}').asIngredient(), 'minecraft:glass_bottle');
})
```

### 返回原料的无序合成

- 与正常的无序合成相比，该种合成可以返回参与合成的物品，如合成蛋糕时，奶桶返回桶。

- 语句：event.recipes.kubejs.shaped(配方).replaceIngredient(原料动作过滤器, 物品);

- 示例：水桶与熔岩桶合成黑曜石，水桶与熔岩桶将在合成后变为桶。

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shapeless(
       'minecraft:obsidian',
        [ 'minecraft:water_bucket', 'minecraft:lava_bucket']
    )
    .replaceIngredient('minecraft:water_bucket', 'minecraft:bucket')
    .replaceIngredient('minecraft:lava_bucket', 'minecraft:bucket')
})
```

### 消耗原料耐久度的有序合成

- 消耗参与合成物品的耐久度。

- 语句：event.recipes.kubejs.shaped(配方).damageIngredient(原料动作过滤器, 损伤值);

- 示例：镐与铁矿石合成生铁，镐损失1耐久度。

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shaped('minecraft:raw_iron', 
        [
            '   ',
            ' AB',
            '   '
        ],
        {
            A: '#minecraft:pickaxes',
            B: '#forge:ores/iron'
        }
    )
    .damageIngredient('#minecraft:pickaxes', 1)
})
```

### 消耗原料耐久度的无序合成

- 消耗参与合成物品的耐久度。

- 语句：event.recipes.kubejs.shapeless(配方).damageIngredient(原料动作过滤器, 损伤值);

- 示例：镐与铁矿石合成生铁，镐损失1耐久度。

```js
ServerEvents.recipes(event => {
    event.recipes.kubejs.shapeless('minecraft:raw_iron', 
        [
            '#minecraft:pickaxes',
            '#forge:ores/iron'
        ]
    )
    .damageIngredient('#minecraft:pickaxes', 1)
})
```

## 锻造台

### 升级

- 如钻石装备升级为下界合金装备。

- 语句：event.recipes.minecraft.smithing_transform（输出物品, 锻造物品, 锻造原料）。

- 例子：下界合金升级锻造模板（默认加的） + 生铁 + 木炭 -> 铁锭。

```js
// 换行为了是避免太长看的难受，可以不换行
ServerEvents.recipes(event => {
    event.recipes.minecraft.smithing_transform(
        // 输出铁锭
        'minecraft:iron_ingot',
        // 输入生铁
        'minecraft:raw_iron',
        // 添加木炭
        'minecraft:charcoal'
    )
})
```

### 替换模板的升级

- 升级，但更改默认的下界合金锻造模板。

- 语句：event.recipes.minecraft.smithing_transform(输出物品, 输入物品, 添加物品).template(替换模板的物品)

- 例子：铁锭（占据模板的位置） + 生铁 + 木炭 -> 铁锭。

```js
// 换行为了是避免太长看的难受，可以不换行
ServerEvents.recipes(event => {
    event.recipes.minecraft.smithing_transform(
        'minecraft:iron_ingot',
        'minecraft:raw_iron',
        'minecraft:charcoal'
    ).template('minecraft:iron_ingot');
    // 如果替换为'minecraft:air'会在JEI中显示不出输出物品，其他物品管理器模组未测试，但配方正常。
})
```

### 模板纹饰

- 按模板为盔甲添加纹饰。

- 语句：event.recipes.minecraft.smithing_trim(锻造模板, 锻造物品, 锻造原料)。

- 例子：暂无示例，请等待更新

```js
ServerEvents.recipes(event => {
    // event.recipes.minecraft.smithing_trim();
})

```

## 熔炉

### 熔炉普通配方

- 使用熔炉烧物品。

- 语句：event.recipes.minecraft.smelting(输出物品，输入原料)。

- 例子：铁矿石 -> 铁锭

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smelting('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore')
    )
})
```

- 也可以把输入物品换成标签

- 例子：forge铁矿石标签 -> 铁锭

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smelting('minecraft:iron_ingot',
        Ingredient.of('#forge:ores/iron');
    )
})
```

### 熔炉配方经验

- 可以定义烧完物品给多少经验。

- 语句：event.recipes.minecraft.smelting(输出物品，输入原料，经验值)。

- 例子：铁矿石 -> 铁锭 给10经验（烧矿默认0.7）

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smelting('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10
    )
})
```

### 熔炉燃烧时间

- 可以定义烧完物品需要多少时间（游戏刻）。

- 语句：event.recipes.minecraft.smelting(输出物品，输入原料，经验值，游戏刻)。

- 例子：铁矿石 -> 铁锭 给10经验 需要10秒（原版默认5秒，200游戏刻）。

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smelting('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10,
        400
    )
})
```

## 高炉

- 使用高炉烧物品，与熔炉一模一样。

### 高炉普通配方

- 语句：event.recipes.minecraft.blasting(输出物品，输入原料)

- 例子：铁矿石 -> 铁锭

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.blasting('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore')
    )
})
```

### 高炉配方经验

- 可以定义烧完物品给多少经验。

- 语句：event.recipes.minecraft.blasting(输出物品，输入原料，经验值)。

- 例子：铁矿石 -> 铁锭 给10经验（烧矿默认0.7）

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.blasting('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10
    )
})
```

### 高炉燃烧时间

- 可以定义烧完物品需要多少时间（游戏刻）。

- 语句：event.recipes.minecraft.blasting(输出物品，输入原料，经验值，游戏刻)。

- 例子：铁矿石 -> 铁锭 给10经验 需要10秒（原版默认5秒，200游戏刻）。

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.blasting('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10,
        400
    )
})
```

## 烟熏炉

- 使用高炉烧物品，与熔炉一模一样。

### 烟熏炉普通配方

- 语句：event.recipes.minecraft.smoking(输出物品，输入原料)

- 例子：铁矿石 -> 铁锭

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smoking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore')
    )
})
```

### 烟熏炉配方经验

- 可以定义烧完物品给多少经验。

- 语句：event.recipes.minecraft.smoking(输出物品，输入原料，经验值)。

- 例子：铁矿石 -> 铁锭 给10经验（烧矿默认0.7）

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smoking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10
    )
})
```

### 烟熏炉燃烧时间

- 可以定义烧完物品需要多少时间（游戏刻）。

- 语句：event.recipes.minecraft.smoking(输出物品，输入原料，经验值，游戏刻)。

- 例子：铁矿石 -> 铁锭 给10经验 需要10秒（原版默认5秒，200游戏刻）。

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.smoking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10,
        400
    )
})
```

## 营火

- 使用营火烧物品，与熔炉一模一样。

### 营火普通配方

- 语句：event.recipes.minecraft.campfire_cooking(输出物品，输入原料)

- 例子：铁矿石 -> 铁锭

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.campfire_cooking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore')
    )
})
```

### 营火配方经验

- 可以定义烧完物品给多少经验。

- 语句：event.recipes.minecraft.campfire_cooking(输出物品，输入原料，经验值)。

- 例子：铁矿石 -> 铁锭 给10经验（烧矿默认0.7）

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.campfire_cooking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10
    )
})
```

### 营火燃烧时间

- 可以定义烧完物品需要多少时间（游戏刻）。

- 语句：event.recipes.minecraft.campfire_cooking(输出物品，输入原料，经验值，游戏刻)。

- 例子：铁矿石 -> 铁锭 给10经验 需要10秒（原版默认5秒，200游戏刻）。

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.campfire_cooking('minecraft:iron_ingot',
        Ingredient.of('minecraft:iron_ore'),
        10,
        400
    )
})
```

## 切石机

- 使用切石机合成。

- 语句：event.recipes.minecraft.stonecutting(输出物品，输入原料)

- 例子：铁矿石 -> 生铁

```js
ServerEvents.recipes(event => {
    event.recipes.minecraft.stonecutting('minecraft:raw_iron', Ingredient.of('minecraft:iron_ore'))
})
```

## 参考文献

> wudi的配方教程：
[wudi-gitbook](https://wudji.gitbook.io/xplus-kubejs-tutorial-v2-zh_cn/recipe/modify-recipes#er-pei-fang-tian-jia)