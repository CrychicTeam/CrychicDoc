# Loot Modification Event

该事件用于创建战利品修饰器，这些修饰器用于修改滚动战利品表后直接生成的物品。也就是说他不直接修改战利品表，而是修改战利品表最后生成的物品。

## getGlobalModifiers

返回所有由模组注册的全局战利品修饰器列表。

语法：

- `.getGlobalModifiers()`

## removeGlobalModifiers

删除所有由指定过滤器指定的模组注册的全局战利品修饰器。

语法：

- `.removeGlobalModifiers(filter: string | regex)`

## addTableModifier

为所有与给指定过滤器匹配的战利品表创建战利品修饰器。

语法：

- `.addTableModifier(filter: string | string[] | regex)` ，返回一个LootModifier

::: code-group

```js [使用过滤器]
LootJS.modifiers(event => {
    event
        .addTableModifier("minecraft:chests/simple_dungeon")
        .randomChance(0.5)
        .addLoot("minecraft:gunpowder")
})
```

```js [支持正则表达式]
LootJS.modifiers(event => {
    event
        .addTableModifier(/minecraft:chests:.*/)
        .randomChance(0.5)
        .addLoot("minecraft:gunpowder")
})
```

:::

## addTypeModifier

为给定的战利品类型添加新的类型修饰器。有效的战利品表类型有 `chest` 、 `block` 、 `entity` 、 `fishing` 、 `archaeology` 、 `gift` 、 `vault` 、 `shearing` 、 `piglin_barter`

语法:

- `.addTypeModifier(type: LootType)` ，返回一个LootModifier

::: code-group

```js [支持添加一个]
LootJS.modifiers(event => {
    event.addTypeModifier("chest")
         .randomChance(0.5)
         .addLoot("minecraft:gunpowder")
})
```

```js [也支持添加多个]
LootJS.modifiers(event => {
    event.addTypeModifier("block", "entity")
         .randomChance(0.5)
         .addLoot("minecraft:gunpowder")
})
```

:::

## addEntityModifier

为与指定过滤器匹配的所有实体添加新的战利品修饰器。

语法：

- `.addEntityModifier(filter: string | string[] | tag)` ，返回一个LootModifier

::: code-group

```js [支持直接使用过滤器]
LootJS.modifiers(event => {
    event.addEntityModifier("minecraft:creeper")
         .randomChance(0.5)
         .addLoot("minecraft:gunpowder")
})
```

```js [也支持使用过滤器数组]
LootJS.modifiers(event => {
    event
        .addEntityModifier(["minecraft:cow", "minecraft:pig"])
        .randomChance(0.5)
        .addLoot("minecraft:gold_nugget")
})
```

```js [也支持使用标签过滤器]
LootJS.modifiers(event => {
    event.addEntityModifier("#minecraft:skeletons")
         .randomChance(0.5)
         .addLoot("minecraft:stick")
})
```

:::

## addBlockModifier

为与指定过滤器匹配的所有方块添加新的战利品修饰器。

语法：

- `.addBlockModifier(filter: string | string[] | regex | tag)` ，返回一个LootModifier

::: code-group

```js [支持直接使用过滤器]
LootJS.modifiers(event => {
    event.addBlockModifier("minecraft:iron_ore")
         .randomChance(0.5)
         .addLoot("minecraft:iron_nugget")
})
```

```js [也支持使用过滤器数组]
LootJS.modifiers(event => {
    event
        .addBlockModifier(["minecraft:gravel", "minecraft:dirt"])
        .randomChance(0.5)
        .addLoot("minecraft:gold_nugget")
})
```

```js [也支持使用标签]
LootJS.modifiers(event => {
    event.addBlockModifier("#c:ores")
         .randomChance(0.5)
         .addLoot("minecraft:flint")
})
```

:::
