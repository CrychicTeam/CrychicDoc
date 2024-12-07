# 注册物品

- 在`startup_script`类型脚本中监听事件: StartupEvents.registry('minecraft:item', event=>{})
- 选择要注册的物品类型，调用事件的create函数
- 注册完成后，这个物品还不具有任何实际作用，我们需要为他设置[物品属性](ItemProperty.md)

::: details 物品类型

|   描述    |   值    |
|:---------:|:---------:|
|   基础物品    |    'basic'    |
|   剑类物品    |    'sword'    |
|   镐类物品    |    'pickaxe'    |
|   斧类物品    |    'axe'    |
|   锹类物品    |    'shovel'    |
|   锄类物品    |    'hoe'    |
|   头盔类物品    |    'helmet'    |
|   胸甲类物品    |    'chestplate'    |
|   护腿类物品    |    'leggings'    |
|   靴子类物品    |    'boots'    |
|   唱片类物品    |    'music_disc'    |

:::

## 基础物品

```js [KubeJS]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'basic')
})
```

## 剑类物品

```js [剑类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'sword')
})
```

## 镐类物品

```js [镐类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'pickaxe')
})
```

## 斧类物品

```js [斧类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'axe')
})
```

## 锹类物品

```js [锹类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'shovel')
})
```

## 锄类物品

```js [锄类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'hoe')
})
```

## 头盔类物品

```js [头盔类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'helmet')
})
```

## 胸甲类物品

```js [胸甲类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'chestplate')
})
```

## 护腿类物品

```js [护腿类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'leggings')
})
```

## 靴子类物品

```js [靴子类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'boots' )
})
```

## 唱片类物品

```js [唱片类物品]
StartupEvents.registry('minecraft:item', event => {
    event.create('kubejs:demo', 'music_disc')
})
```

<!-- ## 常用函数

::: details 基础物品

|   函数    |   描述    |   默认    |
|:---------:|:---------:|:--------:|
|   displayName(Component 物品显示名)   |   设置物品显示名。  |   -   |
|   fireResistant(boolean)    |    是否抗火。    |   false   |
|   maxDamage(number)    |    设置最大损伤值（耐久度）。    |   0   |
|   unstackable()    |    设置物品无法堆叠（堆叠上限为1）。    |   64   |
|   maxStackSize(number)    |    设置最大堆叠数。    |   64   |
|   modifyAttribute(ResourceLocation_ 属性id, string 属性标识符, number 属性值, Internal.AttributeModifier$Operation_ 属性操作符)    |    设置物品属性修饰符。    |   -   |
|   tooltip(Component 工具栏提示)    |    设置物品工具栏提示。    |   0   |
|   glow(boolean)    |    设置物品是否具有附魔光效。    |   0   |
|   food(Internal.Consumer_\<Internal.FoodBuilder\>)    |    设置食物属性。    |   -   |
|   rarity(Internal.Rarity_)    |    设置物品稀有度。    |   0   |
|   useAnimation(Internal.UseAnim_)    |    设置物品使用动画。    |   -   |
|   containerItem(ResourceLocation 物品id)    |    设置物品的容器，例如奶桶的容器是桶。    |   0   |
|   rarity(Internal.Rarity_)    |    设置物品稀有度。    |   "common"   |
|   burnTime(number 游戏刻)    |    设置物品在熔炉作为燃料的燃烧时间    |   0   |

::: -->
