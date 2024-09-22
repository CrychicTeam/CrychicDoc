---
authors: ['Gu-meng']
---
# CreateHeatJS(CreateJS的附属Mod)
### CreateHeatJS可以允许你通过使用CreateJS自定义部分加工配方所需的热源

**例子**\
![例子](/imgs/createheadjs/Example_1.png)![例子](/imgs/createheadjs/Example_2.png)

在配方后使用`.heatLevel()`方法来自定义热源等级
**JEI内显示默认的是本地化键名,需要自己写语言文件进行本地化**

## 代码展示
`server_scripts`
```js
ServerEvents.recipes((event) => {
    const { create } = event.recipes
     
    create.mixing('minecrafrt:diamond', [
        'minecrafrt:coal_block'
    ]).heatLevel('melt')
     
    create.compacting('minecrafrt:diamond', [
        'minecrafrt:coal_block'
    ]).heatLevel('frozen')
})
```

`starup_scripts`
```js
CreateHeatJS.registerHeatEvent((event) => {
	// 熔化
    event.registerHeat('melt', 1, 0xFF8C00)
		.addHeatSource('minecraft:fire')
		.register()

	// 冻结
	event.registerHeat('frozen', -1, 0x87CEFA)
		.addHeatSource('minecraft:blue_ice')
		.register()
})
```

## 进阶
`starup_scripts`
```js
// 读取熔炉类
const $AbstractFurnaceBlock = Java.loadClass("net.minecraft.world.level.block.AbstractFurnaceBlock")
/*
 * 下面这种写法也可以
 * 但是需要v7.0及以上的ProbeJS
*/
const { $AbstractFurnaceBlock } = require("package/net/minecraft/world/level/block/AbstractFurnaceBlock")

CreateHeatJS.registerHeatEvent((event) => {
	// 点燃的熔炉
	event.registerHeat("BLAZE", 3, 0xed9c33)
		.addHeatSource("minecraft:furnace", "minecraft:furnace[lit=true]", (level, pos, blockStack) => {
			if (blockStack.hasProperty($AbstractFurnaceBlock.LIT)) {
				return blockStack.getValue($AbstractFurnaceBlock.LIT).booleanValue()
			}
			return false
		})
		.register()
	
	// 冻结(需要冰刺之地群系)
	event.registerHeat("CRYOTHEUM", -1, 0x8BAAFF)
		.addHeatSource("minecraft:blue_ice", (level, pos, blockStack) => {
			return level.getBiome(pos)
				.is(new ResourceLocation("minecraft:ice_spikes"))
		})
		.jeiTip()
		.register()
})
```