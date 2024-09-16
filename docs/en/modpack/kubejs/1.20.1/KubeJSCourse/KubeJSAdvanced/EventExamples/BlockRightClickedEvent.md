# 方块右键事件
该代码在server脚本里

方块右键事件有两种调取方式
1. `BlockEvents.rightClicked(blockId, event => {})` 第一种是指定方块右键捕捉获取事件
2. `BlockEvents.rightClicked(event => {})` 第二种是捕捉所有被右键的方块

这边建议使用第一种指定一种方块，第二种看情况再选择

## 可被直接访问的方法
|   方法名    |                 方法用处                 |     返回类型     | 直接调用值 |
| :---------: | :--------------------------------------: | :--------------: | :--------: |
| getBlock()  |            获取被右键方块属性            | BlockContainerJS |   block    |
| getEntity() |   获取右键方块的实体（在这里获取的是玩家）   |      Player      |   entity   |
| getFacing() |           获取被右键方块的朝向           |    Direction     |   facing   |
|  getHand()  |           获取哪个手右键的方块           | InteractionHand  |    hand    |
|  getItem()  |            获取右键方块的物品            |    ItemStack     |    item    |
| getLevel()  |           获取被右键方块的世界           |      Level       |   level    |
| getPlayer() |            获取右键方块的玩家            |      Player      |   player   |
| getServer() |          获取被右键方块的服务端          | MinecraftServer  |   server   |

## 示例
```js
BlockEvents.rightClicked('minecraft:oak_planks', event => {
    if (event.hand == "OFF_HAND") return
    let player = event.getPlayer()
    if (player == null) return
    let isBreak = false;
    event.getItem().getTags().iterator().forEachRemaining(value => {
        if (isBreak) return
        if (value.location() == "minecraft:axes") {
            isBreak = true
            let spawnItem = event.getLevel().createEntity("item")
            spawnItem.pos = event.block.pos
            spawnItem.item = Item.of('minecraft:stick', 8);
            event.level.destroyBlock(event.block.pos,false)
            spawnItem.spawn();
            event.getItem().setDamageValue(event.getItem().getDamageValue() + 2)
        }
    })
})
```
方块右键事件会获取玩家两次，第一次为主手第二次为副手

所以需要判断一下`if (event.hand == "OFF_HAND")`是为哪个手点击的物品

这里的`event.getItem()`获取的是`event.hand`的物品