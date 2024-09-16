# 钓鱼战利品
本章中将会举例简单的钓鱼战利品原生kubejs写法
## 基础写法
下面代码中使用`addFishing`覆盖掉原来的战利品列表(如果只想在原来的战利品表里添加使用)
```js
ServerEvents.fishingLootTables(event=>{
    event.addFishing("minecraft:fish",loot=>{
        loot.addPool(pool=>{
            pool.addItem("arrow")
            pool.addItem("glass")
            pool.addItem("bamboo_planks")
        })
    })
})
```

## 删除战利品
有时候我们并不是需要添加或者修改战利品，而是从奖池中进行删除，我们就可以像下面这样写
```js
ServerEvents.fishingLootTables(event=>{
    event.modify("treasure",loot=>{
        loot.pools.forEach(pool =>{
            let pArr = pool.get("entries").asJsonArray
            for (let index = 0; index < pArr.size(); index++) {
                let vName = pArr.get(index).asJsonObject.get("name").asString
                if (vName == "minecraft:bow"){
                    pArr.remove(pArr.get(index))
                }   
            }
        })
    })
})
```
在上面的案例中将钓鱼宝箱奖池的弓删掉了