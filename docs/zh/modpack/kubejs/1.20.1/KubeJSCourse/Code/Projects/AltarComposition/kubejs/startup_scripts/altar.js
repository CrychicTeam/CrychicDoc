const { $InteractionHand } = require("packages/net/minecraft/world/$InteractionHand")
const { $EntityType } = require("packages/net/minecraft/world/entity/$EntityType")
const { $ItemEntity } = require("packages/net/minecraft/world/entity/item/$ItemEntity")
const { $BlockPos } = require("packages/net/minecraft/core/$BlockPos");

StartupEvents.registry("block", event => {
    event.create("meng:test_block", "basic")
        .hardness(1)
        .defaultCutout()
        .box(1,0,1,15,3,15,true)
        .box(4,3,4,12,21,12,true)
        .box(6,21,6,10,21.2,10,true)
        .blockEntity(e => {
            e.inventory(1, 1)
            e.initialData({ item: "minecraft:air" })
        }).rightClick(e => {
            if (e.getHand() != $InteractionHand.MAIN_HAND) return
            let block = e.getBlock()
            let blockPos = block.getPos()
            let player = e.getPlayer()
            let world = player.getLevel()
            let mainHandItem = player.getMainHandItem().copy()
            let data = block.getEntityData().get("data")
            let dataInItem = data.get("item")
            if (player.isShiftKeyDown()) {
                if (getBlockStructure(world, blockPos)) {
                    let output = global.recipeStructure(dataInItem, getStructureItems(world, blockPos))
                    if (output != null) {
                        removeFloatItem(world, dataInItem, blockPos)
                        itemFloat(world, Item.of(output), block)
                        removeStructureItems(world, blockPos)
                    }
                }
                return
            }
            if (dataInItem != "minecraft:air") {
                removeFloatItem(world, dataInItem, blockPos)
                player.give(dataInItem)
            }

            itemFloat(world, mainHandItem, block, blockPos)
            player.getMainHandItem().count--
        })
})

/**
 * 删除悬浮的物品
 * @param {$Level} world 
 * @param {String} itemId 
 * @param {$BlockPos} pos 
 */
function removeFloatItem(world,itemId,pos){
    const {x,y,z} = pos
    world.getEntities($EntityType.ITEM, (value) => {
        // value type is $ItemEntity
        if (value.item.getId() == itemId) {
            if (value.x == x + 0.5 && value.y == y + 1.42 && value.z == z + 0.5) {
                // 删除物品的方式是丢弃(discarded)
                value.remove("discarded")
                return true
            }
        }
        return false
    });
}

/**
 * 使物品悬浮
 * @param {$Level} world 
 * @param {$ItemStack_} item 
 * @param {$BlockContainerJS} block 
 */
function itemFloat(world,item,block){
    let {x,y,z} = block.getPos()
    /**
     * @type {$ItemEntity}
     */
    let itemEntity = world.createEntity("item")
    itemEntity.setPos(x + 0.5, y + 1.42, z + 0.5)
    itemEntity.item = item
    itemEntity.age = -32768
    itemEntity.setPickUpDelay(-1)
    itemEntity.setNoGravity(true)
    itemEntity.item.count = 1
    itemEntity.spawn()
    block.setEntityData({ data: { item: itemEntity.item.id } })
}
/**
 * 获取结构的方块
 * @param {$Level} world 
 * @param {$BlockPos} blockPos 
 * @returns 
 */
function structureBlocksPos(world,blockPos){
    let { x, y, z } = blockPos
    let posList = [
        [x + 3, y, z],
        [x - 3, y, z],
        [x, y, z + 3],
        [x, y, z - 3],
        [x + 2, y, z + 2],
        [x + 2, y, z - 2],
        [x - 2, y, z + 2],
        [x - 2, y, z - 2]
    ]
    let blocks = []
    for (const pos of posList){
        blocks.push(world.getBlock(pos[0], pos[1], pos[2]))
    }
    return blocks
}

/**
 * 检测结构完整性
 * @param {$Level} world
 * @param {$BlockPos} blockPos 
 */
function getBlockStructure(world, blockPos) {
    let blocks = structureBlocksPos(world, blockPos)
    for (const block of blocks) if (block != "meng:test_block") return false
    return true
}

/**
 * 获取结构内的物品
 * @param {$Level} world
 * @param {$BlockPos} blockPos 
 */
function getStructureItems(world, blockPos) {
    let blocks = structureBlocksPos(world, blockPos)
    let itemList = []
    for (const block of blocks){
        let item = block.getEntityData().get("data").get("item")
        if (item == "minecraft:air") continue
        itemList.push(item.getAsString())
    }
    return itemList
}

/**
 * 删除结构内的物品
 * @param {$Level} world
 * @param {$BlockPos} blockPos 
 */
function removeStructureItems(world,blockPos){
    let blocks = structureBlocksPos(world, blockPos)
    for (const block of blocks){
        let itemId = block.getEntityData().get("data").get("item")
        removeFloatItem(world,itemId,block.getPos())
        itemFloat(world,Item.of("minecraft:air"),block)
    }
}