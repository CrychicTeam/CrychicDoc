---
authors: ['Gu-meng']
---
# 玩家库存改变事件
该代码在server脚本里

玩家库存改变事件有两种调取方式
1. `PlayerEvents.inventoryChanged(itemId, event => {})` 第一种是指定物品进入玩家库存捕捉获取事件
2. `PlayerEvents.inventoryChanged(event => {})` 第二种是捕捉所有进入玩家库存事件

## 可被直接访问的方法
|         方法名          |            方法用处            |    返回类型     | 直接调用值 |
| :---------------------: | :----------------------------: | :-------------: | :--------: |
|       getEntity()       | 获取改变库存的实体，一般指玩家 |     Entity      |   entity   |
|        getItem()        |     导致玩家库存改变的物品     |    ItemStack    |    item    |
|       getLevel()        |     玩家库存改变所在的世界     |      Level      |   level    |
|       getPlayer()       |         改变库存的玩家         |     Player      |   player   |
|       getServer()       |      改变库存的玩家服务端      | MinecraftServer |   server   |
|        getSlot()        |     玩家库存改变的所在位置     |     number      |    slot    |
|  hasGameStage(String)   |           是否有阶段           |     boolean     |     -      |
| removeGameStage(String) |            删除阶段            |      void       |     -      |
|  addGameStage(String)   |            添加阶段            |      void       |     -      |

## 示例
下方示例在玩家背包库存发生改变时，检测导致改变的物品是否为被禁用的物品，如果是则删除该物品
```js
const banItems = [
    'minecraft:nether_star',
    'minecraft:lapis_lazuli',
    'minecraft:emerald',
    'minecraft:copper_ingot'
];

PlayerEvents.inventoryChanged(event=>{
    let item = event.getItem()
    if (undefined != banItems.find(value=> value==item.id)){
        event.getPlayer().getInventory().clear(item)
        event.player.tell("发现违禁品 " + item.displayName.getString() + " 已删除！！！")
    }  
})
```
`const banItems = []` 定义一个数组，里面放上我们管控的物品id

使用 `banItems.find(value=> value==item.id)` 来判断物品是否为管控物品，如果如果部位管控物品会返回`undefined`

所以这里只需要是否为`undefined`就可以判断物品是否是禁用的

`event.getPlayer().getInventory().clear(item)` 这行代码代表清除该物品

并且使用 `event.player.tell()` 来给玩家发送信息提示告知玩家物品被清除了