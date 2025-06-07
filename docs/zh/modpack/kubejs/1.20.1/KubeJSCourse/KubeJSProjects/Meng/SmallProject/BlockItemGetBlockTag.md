---
authors:
  - Gu-meng
editor: Gu-meng
---
# 物品右键获取该物品的方块tag
本章主要涉及内容：物品右键事件、方块物品，本章所有代码部分都在`server_scripts`里

涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge

## 代码
```js
ItemEvents.firstRightClicked(event=>{
    if (event.getItem().isBlock()){
        /**
         * @type {Internal.BlockItem}
         */
        let item = event.getItem().item
        let tagList = item.getBlock().defaultBlockState().getTags().toList();
        for (const key of tagList) {
            // 会将内容打印到 /logs/kubejs/server.log 内
            console.log(key.location());
        }
    }
})
```
玩家右键物品会判断该物品是否有方块物品，如果有才会将内容打印出来
