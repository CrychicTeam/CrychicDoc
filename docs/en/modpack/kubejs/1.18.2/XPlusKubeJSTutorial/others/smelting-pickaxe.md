---
authors: ['Wudji']
---

# 18.1 熔炼之镐

***

本示例是对添加配方和Loot Table修改的综合运用

效果：使用下界合金镐挖掘方块时，矿石和石头类的方块会被自动熔炼

```
//代码块 1
onEvent('recipes', event => {
  event.shapeless(Item.of('minecraft:netherite_pickaxe', {furnaceSmelt:"true"}), [Item.of('minecraft:netherite_pickaxe').ignoreNBT(), 'minecraft:lava_bucket']).replaceIngredient('minecraft:lava_bucket', 'minecraft:bucket')
})
//代码块 2
onEvent('block.loot_tables', event => {  
  event.modifyBlock(/minecraft:.*_ore/, table => {
    table.furnaceSmelt()
    table.addCondition({"condition": "minecraft:alternative","terms": [{"condition": "minecraft:match_tool","predicate": {"nbt": "{id:'furnaceSmelt'}"}}]})
  })
  event.modifyBlock(/minecraft:.*stone/, table => {
    table.furnaceSmelt()
    table.addCondition({"condition": "minecraft:alternative","terms": [{"condition": "minecraft:match_tool","predicate": {"nbt": "{id:'furnaceSmelt'}"}}]})
  })
})
```

代码块1中使用了 2.1配方的添加中的nbt合成 和 11.3非标准配方修改中的替换输入物品

代码块2中使用了 4自定义loot table和其addCondition的写法(即原版json格式)
