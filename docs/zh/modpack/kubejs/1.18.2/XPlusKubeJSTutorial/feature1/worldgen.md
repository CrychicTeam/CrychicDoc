---
authors: ['Wudji']
---

# 6 自定义世界生成

***

暂不支持1.16.5 fabric。

```
onEvent('worldgen.add', event => {
  event.addLake(lake => { // 自定义湖
    lake.block = 'minecraft:diamond_block' // 方块ID (使用 [] 来为其添加属性)
    lake.chance = 3 // 约3个区块生成一次
  })

  event.addOre(ore => { //自定义矿石
    ore.block = 'minecraft:glowstone' // 方块ID (使用 [] 来为其添加属性)
    ore.spawnsIn.blacklist = false // 是否在矿石生成黑名单位置处生成
    ore.spawnsIn.values = [ // 该矿石可以在以下位置生成（支持方块ID、标签）
      '#minecraft:base_stone_overworld' // 默认的生成方式: 用于决定作为地下矿石生成时，该矿石能取代哪些方块。你可以在https://wiki.biligame.com/mc/%E6%A0%87%E7%AD%BE查看更多信息。
    ]
    
    ore.biomes.blacklist = true // 是否在矿石生成黑名单群系中生成
    ore.biomes.values = [ // 矿石可以生成的群系
      'minecraft:plains', // 群系ID
      '#nether' // 或者你可以使用“# + 群系类别”来代表群系, 在文末查看可用的列表
    ]
    
    ore.clusterMinSize = 5 // 每矿簇最少的矿石数量 (现在 ore.clusterMinSize 选项是被忽略的, 该功能将在以后更新, 现在它恒为1)
    ore.clusterMaxSize = 9 // 每矿簇最多的矿石数量
    ore.clusterCount = 30 // 每个区块矿石数量
    ore.minHeight = 0 // 最小Y值
    ore.maxHeight = 64 // 最大Y值
    ore.squared = true // 对X和Z值添加0~16的随机值. 推荐设置为 true
    // ore.chance = 4 // 每大约4个区块生成一次. 对于稀有的矿石来说, 你可以将它和 clusterCount = 1 一同使用
  })
  
  event.addSpawn(spawn => { // 自定义实体生成
    spawn.category = 'monster' // 实体类别, 可以设为 'creature', 'monster', 'ambient', 'water_creature' 和 'water_ambient'
    spawn.entity = 'minecraft:magma_cube' // 实体ID
    spawn.weight = 10 // 生成权重
    spawn.minCount = 4 // 每组最小数量
    spawn.maxCount = 4 // 每组最大数量
  })
})

/*
        可用的群系类别:
                taiga(针叶林类)
                extreme_hills(高山类)
                jungle(丛林类)
                mesa(恶地类)(太怪了，作者为什么要在这里用基岩版的名称...)
                plains(平原类)
                savanna(热带草原类)
                icy(冰原类)
                the_end(末地)
                beach(沙滩类)
                forest(树林类)
                ocean(海洋类)
                desert(沙漠类)
                river(河流类)
                swamp(沼泽类)
                mushroom(蘑菇岛类)
                nether(下界)

        以下是香草(划掉)世界生成的顺序:
                raw_generation
                lakes
                local_modifications
                underground_structures
                surface_structures
                strongholds
                underground_ores
                underground_decoration
                vegetal_decoration
                top_layer_modification
*/

onEvent('worldgen.remove', event => {
  event.removeOres(ores => {//移除矿石
    ores.blocks = [ 'minecraft:coal_ore', 'minecraft:iron_ore' ] // 移除铁矿和煤矿
    ores.biomes.values = [ 'minecraft:plains' ] // 限制该选项仅在平原生效
  })
  
  event.removeSpawnsByID(spawns => {//通过实体ID来禁止指定实体生成
    spawns.entities.values = [
      'minecraft:cow',
      'minecraft:chicken',
      'minecraft:pig',
      'minecraft:zombie'
    ]
  })
  
  event.removeSpawnsByCategory(spawns => {//移除实体生成
    spawns.biomes.values = [
      'minecraft:plains'//指定为平原群系
    ]
    spawns.categories.values = [//类型为怪物
      'monster'
    ]
  })
})
```
