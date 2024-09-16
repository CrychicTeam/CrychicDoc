BlockEvents.broken("meng:test_block", e => {
    let block = e.getBlock()
    let data = block.getEntityData().get("data")
    let { x, y, z } = block.getPos()
    let world = block.getLevel()
    if (data.get("item") == "minecraft:air") return
    world.getEntities($EntityType.ITEM, (value) => {
        if (value.x == x + 0.5 && value.y == y + 1.42 && value.z == z + 0.5) {
            value.age = 0
            value.setPickUpDelay(0)
            value.setNoGravity(false)
            return true

        }
        return false
    });
})