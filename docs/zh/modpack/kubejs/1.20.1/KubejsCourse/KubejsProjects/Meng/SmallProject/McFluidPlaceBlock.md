# 原版修改流体对撞产生块
本章主要涉及内容：ForgeEvents、forge事件里的`BlockEvent.FluidPlaceBlockEvent`，本章所有代码部分都在`startup_scripts`里

## 完整代码
```js
let $BlockEvent = Java.loadClass("net.minecraftforge.event.level.BlockEvent")

ForgeEvents.onEvent($BlockEvent.FluidPlaceBlockEvent,event=>{
    let block = event.getNewState().getBlock();
    if (block.id == "minecraft:stone"){
        event.setNewState(Block.getBlock('minecraft:netherrack').defaultBlockState());
    }
})
```
上面代码是在岩浆和水对撞后产生石头，将其重新设置成为地狱岩（下界岩）