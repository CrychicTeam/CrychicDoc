---
authors: ['Wudji']
---

# 12.5 KubeJS Termal

***

（本节内容参考了[KubeJS Thermal 官方Wiki](https://mods.latvian.dev/books/kubejs/page/kubejs-thermal)）

你可以使用CTRL + F 键快捷搜索本页内容。

```
onEvent('recipes', event => {
    // 红石炉
    // 将四个煤变为煤炭。
    event.recipes.thermal.furnace('minecraft:diamond', '4x minecraft:coal');
    // 将干海带制为皮革，需要大量能量。
    event.recipes.thermal.furnace('minecraft:leather', 'minecraft:dried_kelp').energy(20000);
    
    // 锯木机
    // 输入一个橡木原木，5%概率输出苹果，10%概率输出树苗。
    event.recipes.thermal.sawmill([Item.of('minecraft:apple').withChance(0.05), Item.of('minecraft:oak_sapling').withChance(0.1)], 'minecraft:oak_leaves')
    // 金合欢台阶合成四个按钮
    event.recipes.thermal.sawmill('4x minecraft:acacia_button', 'minecraft:acacia_slab')
    
    // 磨粉机
    // 将任何树叶方块转换为四根木棍并有50%概率获得五根，需要少量能量。
    event.recipes.thermal.pulverizer(Item.of('minecraft:stick').withChance(4.5), '#minecraft:leaves').energy(100)
    // 将燧石转换为铁粒并有10%概率获得两个。
    event.recipes.thermal.pulverizer(Item.of('minecraft:iron_nugget').withChance(1.1), 'minecraft:flint')
    
    // 感应炉
    // 将煤炭块转换为四个钻石并有50%概率获得五个。
    event.recipes.thermal.smelter(['4x minecraft:diamond', Item.of('minecraft:diamond').withChance(0.5)], 'minecraft:coal_block')
    // 将铁锭和铜锭转换为金锭，需要10,000 FE。
    event.recipes.thermal.smelter('minecraft:gold_ingot', ['minecraft:iron_ingot', 'minecraft:copper_ingot']).energy(10000)
    
    // 离心机
    // 将树苗转换为木棍（50%概率）和300mb水（100%概率）。
    event.recipes.thermal.centrifuge([Item.of('minecraft:stick').withChance(0.5), Fluid.of('minecraft:water', 300)], '#minecraft:saplings')
    // 将两个甜浆果转换为红色染料。
    event.recipes.thermal.centrifuge('minecraft:red_dye', '2x minecraft:sweet_berries')
    
    // 多驱冲压机
    // 将7个骨粉转换为骨头。
    event.recipes.thermal.press('minecraft:bone', '7x minecraft:bone_meal')
    // 使用铁粉和硬币模具合成铁粒，注意配方中模具物品必须具有thermal:crafting/dies标签!
    event.recipes.thermal.press('minecraft:iron_nugget', ['#forge:dusts/iron', 'thermal:press_coin_die'])
  
    // 熔岩炉
    // 将树苗转换为400mb水。
    event.recipes.thermal.crucible(Fluid.of('minecraft:water', 400), '#minecraft:saplings').energy(100)
    // 将矿石转换为500mb岩浆。
    event.recipes.thermal.crucible(Fluid.of('minecraft:lava', 500), '#forge:ores')
    
    // 急速冷冻机
    // 将普通箭转换为迟缓之箭。
    event.recipes.thermal.chiller(Item.of('minecraft:tipped_arrow', '{Potion:"minecraft:slowness"}'), [Fluid.of('minecraft:water', 100), 'minecraft:arrow'])
    // 使用球形铸模将岩浆转换为生铁，注意配方中铸模物品必须具有thermal:crafting/casts标签!
    event.recipes.thermal.chiller('minecraft:raw_iron', [Fluid.of('minecraft:lava', 1000), 'thermal:chiller_ball_cast'])
    
    // 流体精炼机
    // 将杂酚油转换为树油和乳胶，概率获得橡胶。
    event.recipes.thermal.refinery([Item.of('thermal:rubber').withChance(0.8), Fluid.of('thermal:tree_oil', 100), Fluid.of('thermal:latex', 50)], Fluid.of('thermal:creosote', 200))
    // 将书油转换为少量精炼油，需要大量能量。
    event.recipes.thermal.refinery(Fluid.of('thermal:refined_fuel', 50), Fluid.of('thermal:tree_oil', 100)).energy(20000)
    // Unbrew an awkward potion. This uses the cofh core potion fluid with some nbt.
    event.recipes.thermal.refinery([Fluid.of('minecraft:water', 1000), 'minecraft:nether_wart'], Fluid.of('cofh_core:potion', 1000, '{Potion:"minecraft:awkward"}'))
    
    // 药水酿造机
    // 将红石粉和200mb岩浆转换为不稳定熔融红石。
    event.recipes.thermal.brewer(Fluid.of('thermal:redstone', 200), [Fluid.of('minecraft:lava', 200), 'minecraft:redstone'])
    // 使用64个基岩和粗制的药水酿造无法合成的药水（无nbt）。
    event.recipes.thermal.brewer(Fluid.of('cofh_core:potion', 1000), [Fluid.of('cofh_core:potion', 1000, '{Potion:"minecraft:awkward"}'), '64x minecraft:bedrock'])
    
    // 流体灌装机
    // 为海绵注水。
    event.recipes.thermal.bottler('minecraft:wet_sponge', [Fluid.of('minecraft:water', 10000), 'minecraft:sponge'])
    // 将齿轮和不稳定熔融红石转换为机器框架。
    event.recipes.thermal.bottler('thermal:machine_frame', ['#forge:gears', Fluid.of('thermal:redstone', 500)]).energy(500)
})
```
