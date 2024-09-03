# 全局战利品
kubejs并没有提供所有战利品的修改，比如猫、猪灵交易、嗅探兽的战利品等都没有提供，这时就可以使用全局战利品直接去修改,只要能在游戏内获取到的id可以直接使用全局战利品去修改
## 覆盖
```js
ServerEvents.genericLootTables(e=>{
    e.addGeneric("minecraft:gameplay/cat_morning_gift",loot=>{
        loot.addPool(pool=>{
            pool.addItem("diamond")
            pool.addItem("iron_ingot")
            pool.addItem("apple")
        })
    })
})
```
覆盖掉猫咪的礼物，让猫咪每天早上送的礼物可能是钻石可能是苹果也可能是铁锭 

## 添加
```js
ServerEvents.genericLootTables(e=>{
    e.modify("minecraft:gameplay/piglin_bartering",loot=>{
        let json = [{
            "type":"minecraft:item",
            "name":"minecraft:diamond",
            "weight":80
        }]
        let poolArr = loot.pools.get(0).asJsonObject.get("entries").asJsonArray
        poolArr.addAll(json)
    })
})
```
在猪灵交易的战利品里面添加猪灵有大概率交易出来钻石