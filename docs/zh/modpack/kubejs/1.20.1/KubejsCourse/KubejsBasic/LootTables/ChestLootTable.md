# 宝箱战利品
在下面会使用原生kubeJs实现对宝箱战利品生成做出修改
## 基础写法
### 覆盖原版的战利品
下面代码中，我们将原来的末地城的战利品覆盖掉了，使其重新生成战利品为玻璃、玻璃瓶
```js
ServerEvents.chestLootTables(event=>{
    event.addChest("minecraft:end_city_treasure",loot=>{
        loot.addPool(pool=>{
            pool.addItem("glass")
            pool.addItem("glass_bottle")
            pool.rolls = 3
            console.log(pool.conditions);
        })
    })
})
```
使用`addChest()`的第一个参数，是需要覆盖掉的宝箱战利品表的名称([原版宝箱战利品表](../../Digression/LootTableId#宝箱战利品id))

如果你不需要覆盖掉而是**直接在原有的基础上进行添加**，则可以使用`modify()`,参数和`addChest()`一样

覆盖原有的战利品，只需要奖池内的物品和[权重占比](../../Digression/Weight)

如果你希望给物品添加一些条件才能生成可以使用[战利品谓词](https://zh.minecraft.wiki/w/%E6%88%98%E5%88%A9%E5%93%81%E8%A1%A8?variant=zh-cn)和[物品修饰词](https://zh.minecraft.wiki/w/%E7%89%A9%E5%93%81%E4%BF%AE%E9%A5%B0%E5%99%A8)

### 添加新的战利品
其实添加新的战利品和直接覆盖原版的是一样的
```js
ServerEvents.chestLootTables(event=>{
    event.addChest("meng:chest_loot",loot=>{
        loot.addPool(pool=>{
            pool.addItem("glass")
            pool.addItem("glass_bottle")
            pool.rolls = 3
            console.log(pool.conditions);
        })
    })
})
```
这个地方我们设置了一个自己的战利品列表，id为`meng:chest_loot`

可以在游戏内使用下面的指令生成一个nbt为战利品的箱子来看看
```
/setblock ~ ~ ~ chest{LootTable:"meng:chests/chest_loot"}
```
如果需要将自己的战利品添加进游戏内进行生成，这边需要使用到[世界生成事件]()，来进行将自己的战利品部署到世界内