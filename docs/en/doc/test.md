---
layout: doc
title: ts-check Sample
---
# Test ts-check
```js twoslash
// @filename: index.ts
// @ts-check
import 'typefiles/1.20.1/.probe/server/probe-types/global/index.d.ts';
// @filename: com.almostreliable.d.ts
import 'typefiles/1.20.1/.probe/server/probe-types/packages/com.almostreliable.d.ts'
// @filename: dev.latvian.d.ts
import 'typefiles/1.20.1/.probe/server/probe-types/packages/dev.latvian.d.ts'
// @filename: net.minecraft.d.ts
import 'typefiles/1.20.1/.probe/server/probe-types/packages/net.minecraft.d.ts'
// ---cut-before---
LootJS.modifiers(event=>{
    event.addBlockLootModifier("minecraft:grass")
    .addLoot(Item.of("minecraft:dirt").setCount(1))
})

ItemEvents.rightClicked("acacia_button",event=>{
    event.player.tell("You right clicked an acacia button")
})
```
