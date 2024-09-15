# 12.1 KubeJS Create

***

| 支持的配方 | 格式                                                                                                     | 说明                                                |
| ----- | ------------------------------------------------------------------------------------------------------ | ------------------------------------------------- |
| 转化    | `event.recipes.createConversion(output[], input[])`                                                    | 如将竖直十字齿轮箱变为十字齿轮箱                                  |
| 粉碎轮   | `event.recipes.createCrushing(output[], input[])`                                                      | 输入和输出不一定为数组，输入可以为ingredients或流体①，输出可以为物品或流体       |
| 动力锯   | `event.recipes.createCutting(output[], input[])`                                                       | -                                                 |
| 石磨    | `event.recipes.createMilling(output[], input[])`                                                       | -                                                 |
| 工作盆   | `event.recipes.createBasin(output[], input[])`                                                         | -                                                 |
| 动力搅拌器 | `event.recipes.createMixing(output[], input[])`                                                        | 在其后可加 .heated() 和.superheated() (是否加热及其程度(加热和高温)) |
| 装配    | `event.recipes.createCompacting(output[], input[])`                                                    | 在其后可加 .heated() 和.superheated() (是否加热及其程度(加热和高温)) |
| 动力辊压机 | `event.recipes.createPressing(output[], input[])`                                                      | -                                                 |
| 砂纸打磨  | `event.recipes.createSandpaperPolishing(output[], input[])`                                            | -                                                 |
| 洗涤    | `event.recipes.createSplashing(output[], input[])`                                                     | -                                                 |
| 机械手   | `event.recipes.createDeploying(output[], input[])`                                                     | -                                                 |
| 注液    | `event.recipes.createFilling(output[], input[])`                                                       | -                                                 |
| 倒出液体  | `event.recipes.createEmptying(output[], input[])`                                                      | -                                                 |
| 动力合成  | `event.recipes.createMechanicalCrafting(output, pattern[], {合成键值})`                                    | 详见下方示例                                            |
| 装配    | `event.recipes.createSequencedAssembly(output[], input, sequence[]).transitionalItem(item).loops(int)` | 详见下方示例及说明                                         |

①：以水为例，流体可以为以下格式：`Fluid.of('minecraft:water', 1000)` 和 `{fluidTag: 'some:fluid_tag', amount: 1000}`

②：带有合成时间的项目可以在配方后加.processingTime(时间(整形))来修改其合成时间，在物品后加.withChance(2.0)可以修改其产出概率

***

示例：

例子1：红石矿石 -> 2个圆石 + 一个红石粉 + 50%概率一个红石粉

```
event.recipes.createCrushing([
  '2x minecraft:cobblestone',
  'minecraft:redstone',
  Item.of('minecraft:redstone').withChance(0.5)//概率的表示方法
], 'minecraft:redstone_ore')
```

例子2：3个萤石粉 + 3个黑曜石粉末 + 磨制玫瑰石英 --高温--> 异彩化合物

```
event.recipes.createMixing('create:chromatic_compound', [
  '#forge:dusts/glowstone',
  '#forge:dusts/glowstone',
  '#forge:dusts/glowstone',
  'create:powdered_obsidian',
  'create:powdered_obsidian',
  'create:powdered_obsidian',
  'create:polished_rose_quartz'
]).superheated()
```

例子3：使用注液器制作烈焰蛋糕

```
event.recipes.createFilling('create:blaze_cake', [
  'create:blaze_cake_base',
  Fluid.of('minecraft:lava', 250)
])
```

例子4：分液：蜂蜜瓶 -> 玻璃瓶 + 250单位蜂蜜

```
event.recipes.createEmptying([
  'minecraft:glass_bottle',
  Fluid.of('create:honey', 250)
], 'minecraft:honey_bottle')
```

例子5：动力合成，基本和标准配方格式一样

```
event.recipes.createMechanicalCrafting('minecraft:piston', [
  'CCCCC',
  'CPIPC',
  'CPRPC'
], {
  C: '#forge:cobblestone',
  P: '#minecraft:planks',
  R: '#forge:dusts/redstone',
  I: '#forge:ingots/iron'
})
```

例子6：序列组装

```
event.recipes.createSequencedAssembly([
  Item.of('6x create:large_cogwheel').withChance(32.0),
  Item.of('create:brass_ingot').withChance(2.0),
  'minecraft:andesite',
  'create:cogwheel',
  'minecraft:stick',
  'minecraft:iron_nugget'
], 'create:brass_ingot', [
  event.recipes.createDeploying('create:incomplete_large_cogwheel', ['create:incomplete_large_cogwheel', '#minecraft:planks']),
  event.recipes.createDeploying('create:incomplete_large_cogwheel', ['create:incomplete_large_cogwheel', '#minecraft:wooden_buttons']),
  event.recipes.createCutting('create:incomplete_large_cogwheel', 'create:incomplete_large_cogwheel').processingTime(50)
]).transitionalItem('create:incomplete_large_cogwheel').loops(6)
```

对这部分代码的解释

```
event.recipes.createSequencedAssembly([
 	//成品：
 	Item.of('6x create:large_cogwheel').withChance(32.0),
 	//随机废料：
 	Item.of('create:brass_ingot').withChance(2.0),
 	'minecraft:andesite',
 	'create:cogwheel',
 	'minecraft:stick',
 	'minecraft:iron_nugget'
	], 
	//输入物品：
	'create:brass_ingot', 
	[
	//每步的配方
	/*
	其中create:incomplete_large_cogwheel作为合成的半成品(即.transitionalItem('半成品注册名'))
	.loops(6)代表合成循环次数
	.transitionalItem和.loops是可选的
	*/
  	event.recipes.createDeploying('create:incomplete_large_cogwheel', ['create:incomplete_large_cogwheel', '#minecraft:planks']),//第一步
 	event.recipes.createDeploying('create:incomplete_large_cogwheel', ['create:incomplete_large_cogwheel', '#minecraft:wooden_buttons']),//第二步
 	event.recipes.createCutting('create:incomplete_large_cogwheel', 'create:incomplete_large_cogwheel').processingTime(50)//第三步
]).transitionalItem('create:incomplete_large_cogwheel').loops(6)
```

即一般格式为：

`event.recipes.createSequencedAssembly(输出物品组(数组)[], 输入物品, 序列合成步骤(数组)[]).transitionalItem(半成品注册名).loops(循环次数(整形))`

7 如果你想要使用你自己注册的半成品，你需要在Startup 脚本中这样填写：

```
onEvent('item.registry', event => {
  // 该物品的纹理应位于 kubejs/assets/kubejs/textures/item/my_part.png
  event.create('my_part').type('create:sequenced_assembly').displayName('My Part')
})
```
