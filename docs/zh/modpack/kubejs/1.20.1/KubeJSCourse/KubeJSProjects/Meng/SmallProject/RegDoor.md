# 注册门
本章主要涉及内容：createCustom，本章所有代码部分都在`startup_scripts`里

涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge

## 完整代码
```js

const $DoorBlock = Java.loadClass("net.minecraft.world.level.block.DoorBlock")
const $BlockBehaviourProperties = Java.loadClass("net.minecraft.world.level.block.state.BlockBehaviour$Properties")
const $BlockSetType = Java.loadClass("net.minecraft.world.level.block.state.properties.BlockSetType")
const $BlockItem = Java.loadClass("net.minecraft.world.item.BlockItem")
const $ItemProberties = Java.loadClass("net.minecraft.world.item.Item$Properties")

StartupEvents.registry("block",event=>{
    event.createCustom("meng:test_door",()=>new $DoorBlock(
        $BlockBehaviourProperties.of()
            .instrument("bass")
            .strength(3)
            .noOcclusion()
            .ignitedByLava()
            .pushReaction("destroy"),
        $BlockSetType.STONE
    ))
})

StartupEvents.registry("item",event=>{
    event.createCustom("meng:test_door",()=>new $BlockItem(Block.getBlock("meng:test_door"),new $ItemProberties()))
})
```