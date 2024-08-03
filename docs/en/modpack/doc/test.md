---
layout: doc
---

```js twoslash
// @filename: index.ts
// @ts-check
import '.probe/server/probe-types/global/index.d.ts';
// @filename: com.almostreliable.d.ts
import '.probe/server/probe-types/packages/com.almostreliable.d.ts'
// @filename: dev.latvian.d.ts
import '.probe/server/probe-types/packages/dev.latvian.d.ts'
// @filename: net.minecraft.d.ts
import '.probe/server/probe-types/packages/net.minecraft.d.ts'
// ---cut-before---
LootJS.modifiers(event=>{
    event.addBlockLootModifier("minecraft:grass")
    .addLoot(Item.of("minecraft:dirt").setCount(1))
})

ItemEvents.rightClicked("acacia_button",event=>{
    event.player.tell("You right clicked an acacia button")
})
```
test
test2
server {
    listen 1300;
    server_name docs.mihono.cn;

    root /home/mihono/vitepress;
    index index.html;

    location / {
        try_files $uri $uri/ =404;
    }

    location = /favicon.ico {
        log_not_found off;
        access_log off;
    }
}
