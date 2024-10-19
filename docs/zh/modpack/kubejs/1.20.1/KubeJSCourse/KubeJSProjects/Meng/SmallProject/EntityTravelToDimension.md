---
authors: ['Gu-meng']
---
# 禁止实体穿越到指定维度
本章主要涉及内容：ForgeEvents、forge事件里的`EntityTravelToDimensionEvent`，本章所有代码部分都在`startup_scripts`里

## 完整代码
```js
const $EntityTravelToDimensionEvent = Java.loadClass("net.minecraftforge.event.entity.EntityTravelToDimensionEvent")

ForgeEvents.onEvent($EntityTravelToDimensionEvent, event => {
    let resourceKey = event.dimension;
    if (resourceKey.getPath() == "the_nether") {
        event.setCanceled(true)
    }
})
```
以上代码判断了准备穿越维度的实体是否穿越过去的维度为地狱（下界），如果是就取消该事件，让实体无法穿越维度

## 阶段限制
阶段限制主要是限制玩家，因为只有玩家才有阶段
```js
const $EntityTravelToDimensionEvent = Java.loadClass("net.minecraftforge.event.entity.EntityTravelToDimensionEvent")
const $ServerPlayer = Java.loadClass("net.minecraft.server.level.ServerPlayer")

ForgeEvents.onEvent($EntityTravelToDimensionEvent, event => {
    let resourceKey = event.dimension;
    /**
     * @type {Internal.ServerPlayer}
     */
    let player = event.entity;
    if (resourceKey.getPath() == "the_nether") {
        if (player instanceof $ServerPlayer){
            if (!player.stages.has("nether")){
                event.setCanceled(true)
            }
        }
    }
})
```