---
authors: ['Gu-meng']
---
# 机械动力修改流体管道流体和流体产生块
本章主要涉及内容：ForgeEvents、机械动力事件里的`PipeCollisionEvent.Spill`，本章所有代码部分都在`startup_scripts`里

关于`PipeCollisionEvent`的[参考位置](https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/api/event/PipeCollisionEvent.java):https://github.com/Creators-of-Create/Create/blob/mc1.20.1/dev/src/main/java/com/simibubi/create/api/event/PipeCollisionEvent.java
## 完整代码
```js
const $PipeCollisionEvent = Java.loadClass("com.simibubi.create.api.event.PipeCollisionEvent")

ForgeEvents.onEvent($PipeCollisionEvent.Spill, event => {
    let block = event.getState().block;
    if (block.id == "minecraft:stone"){
        event.setState(Block.getBlock('minecraft:netherrack').defaultBlockState())
    }
})
```
上面代码是在流体管道输出流体如果产生的方块为石头那么就将其重新设置为下界岩（地狱岩）