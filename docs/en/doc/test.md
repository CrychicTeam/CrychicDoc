---
layout: doc
title: ts-check Sample
---
# Test ts-check
```js twoslash
LootJS.modifiers(event=>{
    event.addBlockLootModifier("minecraft:grass")
    .addLoot(Item.of("minecraft:dirt").setCount(1))
})

ItemEvents.rightClicked("acacia_button",event=>{
    event.player.tell("You right clicked an acacia button")
})
```
