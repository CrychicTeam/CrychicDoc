---
authors:
  - Gu-meng
editor: Gu-meng
---
# 方块掉落
在mc中方块掉落也属于战利品，我们可以想到并不是在有的时候方块挖掘后并不是掉落它本身，甚至有的可以受到时运影响而掉落更多，因为它本质上就是战利品，方块掉落什么取决于战利品池里抽到什么
## 覆盖方块掉落
下面给大家写一个类似于幸运方块的随机物品掉落效果
```js
ServerEvents.blockLootTables(e=>{
    Block.getTypeList().forEach(block=>{
        e.addBlock(block,l=>{
            l.addPool(p=>{
                Ingredient.of("@minecraft").getItemIds().forEach(itemId=>{
                    p.addItem(Item.of(itemId))
                })
            })
        })
    })
})
```
在上面代码中我们将所有方块挖掘后的掉落全部改成了随机掉落我的世界中的物品

## 在原有的里增加
下面是在原本泥土的掉落物里增加物品，使其只会掉落泥土和增加的物品
```js
ServerEvents.blockLootTables(e=>{
    e.modifyBlock('minecraft:dirt',loot=>{
        let pool = [{
            "type":"minecraft:item",
            "name":"minecraft:diamond"
        }]
        let arr = loot.pools.get(0).asJsonObject.get("entries").asJsonArray
        arr.addAll(pool)
    })
})
```
在kubejs-forge-2001.6.5-build.14的版本中直接使用`JsonArray.add()`会报错,所以这里使用addAll,因为addAll需要传入一个数组，所以上面的物品json得再用[]包裹起来，这样就可以添加进去了
