---
layout: doc
title: 类型检查示例
authors:
  - M1hono
prev:
  text: Crychic文档编写示例
  link: /zh/doc/samples
next:
  text: 侧边栏设置教程
  link: /zh/doc/sidebarTutorial
---
# 测试类型检查
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
非常好

::: stepper
@tab 第一步

只是测试

OK

```yaml
---
noguide: true
root:
  title: example // [!code focus]
  collapsed: true
  subDir:
      - title: subDir a
        path: test
        collapsed: true
      - title: subDir back
        path: test
        noScan: true
        file: README
---
```

@tab 第二步
```yaml
---
noguide: true
root:
  title: example
  collapsed: true
  subDir: // [!code focus:4]
      - title: subDir a
        path: test
        collapsed: true
      - title: subDir back
        path: test
        noScan: true
        file: README
---
```

@tab 第三步
```yaml
---
noguide: true // [!code focus]
root:
  title: example
  collapsed: true
  subDir:
      - title: subDir a
        path: test
        collapsed: true
      - title: subDir back
        path: test
        noScan: true
        file: README
---
```

@tab 第四步
```yaml
---
noguide: true // [!code focus]
root:
  title: example
  collapsed: true
  subDir:
      - title: subDir a
        path: test
        collapsed: true
      - title: subDir back
        path: test
        noScan: true
        file: README
---

:::