---
layout: doc
title: 类型检查示例
authors:
  - M1hono
noguide: true
---
# 测试类型检查

::: demo 示例
```js twoslash
LootJS.modifiers(event => {
    event.addBlockLootModifier("minecraft:grass")
    .addLoot(Item.of("minecraft:dirt").setCount(1))
})

ItemEvents.rightClicked("acacia_button", event => {
    event.player.tell("You right clicked an acacia button")
})
```
:::
非常好