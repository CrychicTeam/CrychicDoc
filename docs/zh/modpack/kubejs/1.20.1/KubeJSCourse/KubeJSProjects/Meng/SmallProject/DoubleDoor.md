---
editor: Gu-meng
---
# 双开门
本章主要涉及内容：方块右键事件、修改方块状态，本章所有代码部分都在`server_scripts`里

涉及模组及版本:
1. jei-1.20.1-forge-15.3.0.4
2. rhino-forge-2001.2.2-build.18
3. architectury-9.2.14-forge
4. kubejs-forge-2001.6.5-build.14
5. probejs-6.0.1-forge

## 完整代码
```js
const $DoorBlock = Java.loadClass("net.minecraft.world.level.block.DoorBlock");
const $Boolean = Java.loadClass("java.lang.Boolean");

BlockEvents.rightClicked(event => {
    if (event.getHand() != "MAIN_HAND") return;
    if (event.player.isShiftKeyDown()) return;
    let block = event.block;
    let bs = block.blockState;
    if (!(bs.block instanceof $DoorBlock)) return;
    if (block.id == 'minecraft:iron_door' || bs.block.idLocation.namespace == "create") return;
    let open = $DoorBlock.OPEN,
        facing = $DoorBlock.FACING,
        hinge = $DoorBlock.HINGE, neighborBlock;
    if (bs.getValue(hinge) == $DoorHingeSide.LEFT) {
        if (bs.getValue(facing) == $Direction.SOUTH) neighborBlock = block.west;
        else if (bs.getValue(facing) == $Direction.WEST) neighborBlock = block.north;
        else if (bs.getValue(facing) == $Direction.NORTH) neighborBlock = block.east;
        else if (bs.getValue(facing) == $Direction.EAST) neighborBlock = block.south;

    } else {
        if (bs.getValue(facing) == $Direction.SOUTH) neighborBlock = block.east;
        else if (bs.getValue(facing) == $Direction.WEST) neighborBlock = block.south;
        else if (bs.getValue(facing) == $Direction.NORTH) neighborBlock = block.west;
        else if (bs.getValue(facing) == $Direction.EAST) neighborBlock = block.north;
    }
    if (neighborBlock.id == block.id && neighborBlock.blockState.getValue(hinge) != bs.getValue(hinge)) {
        neighborBlock.setBlockState(neighborBlock.blockState.setValue(open, !bs.getValue(open) ? $Boolean.TRUE : $Boolean.FALSE), 3)
    }
})
```
该内容是玩家在打开和关闭门时，如果是双开门则会一起被打开或关闭

如果玩家蹲下来开门或关门则不会触发双开门事件
