# 机械动力和KubeJs 
在本章中将介绍如何使用KubeJs去修改机械动力模组的配方

这里交代本章教程所用到的各个模组和forge的版本，如果版本不同导致报错，可能是作者进行代码更改:
1. forge-47.2.32
2. JEI-15.3.0.4
3. rhino-2001.2.2-build.18
4. architectury-9.2.14
5. kubejs-2001.6.5-build.7
6. probejs-6.0.1
7. create-0.5.1f
8. kubejs create-2001.2.5.bulid.2

### 配方类型表格
(不代表全部,仅仅是常用的,带有`[]`说明可以写入多个输入或输出或是数组)

烘烤和烧炼读取的是原版的配方,因此没有专门的配方类型

`.haetLevel`方法需要额外Mod [**CreateHeatJS**](../KubeJSAddon/CreateHeatJS)的支持
|                                         配方格式                                          |      配方类型       |                     补充说明                      |
| :---------------------------------------------------------------------------------------: | :-----------------: | :-----------------------------------------------: |
|                          `create.conversion(output[], input[])`                           |        转换         |              参考两种齿轮箱和信息表               |
|                           `create.crushing(output[], input[])`                            |        粉碎         |                         -                         |
|                            `create.milling(output[], input[])`                            |        磨粉         |                         -                         |
|                            `create.cutting(output[], input[])`                            |        切割         |                         -                         |
|                            `create.mixing(output[], input[])`                             |      动力搅拌       |    可以在其后可加`.heated()`和`.superheated()`    |
|                          `create.compacting(output[], input[])`                           |      压块塑形       |    可以在其后可加`.heated()`和`.superheated()`    |
|                      `create.sandpaperPolishing(output[], input[])`                       |      砂纸打磨       |                         -                         |
|                           `create.splashing(output[], input[])`                           |   洗涤(鼓风机+水)   |                         -                         |
|                           `create.haunting(output[], input[])`                            | 缠魂(鼓风机+灵魂火) |                         -                         |
|                           `create.deploying(output[], input[])`                           |       机械手        |                         -                         |
|                         `create.item_application(output, input)`                          |      直接使用       | 参考几种机壳的配方(1.18及以上,并且自动适配机械手) |
|                            `create.filling(output[], input[])`                            |        注液         |                         -                         |
|                           `create.emptying(output[], input[])`                            |        分液         |                         -                         |
|                  `create.mechanicalCrafting(output[], pattern[], {key})`                  |      动力合成       |                  详细见下方示例                   |
| `create.sequencedAssembly(output[], input, sequence[]).transitionalItem(item).loops(int)` |      序列装配       |                  详细见下方示例                   |

### 一些描述
下面是所有的代码最外层形式，下面展示的只是局部代码
```js
ServerEvents.recipes((event) => {
	/* 
	 * 这里是一个很基础的recipes事件的解构
	 * 不单单限制于Create这个Mod
	 * 有其他Mod支持的时候直接在大括号内写便是,例如:
	 * const { create, thermal, kubejs, minecraft } = event.recipes
	 * 如果不知道哪些Mod支持请尝试使用ProbeJS
	*/
	const { create } = event.recipes
})
```
如果标注支持**加热**那么也支持**超级加热**

只需要在机器后面添加`.heated()`或者`superheated()`,前者是普通加热后者是超级加热
当然加热的方法也可以用`.heatRequirement(string)`,括号内填`"heated"`或`"superheated"`,或是其它的热量等级

如果标注了流体输出,则说明可以添加流体,像下面这样
```js
Fluid.of("流体id", 数量)
Fluid.of("minecraft:lava", 810)
```

如果标注可以概率输出，则是下面这种写法
```js
Item.of("物品id", 数量).withChance(0.5)
```
在上面代码中则代表该物品有百分之五十的概率输出,所以在这个地方1是100%

如果标注可以输出多个物品则是可以像下面这种写法
```js
[Item.of("物品id", 数量),Item.of("物品id", 数量)]
```
这里可以注意的一点，如果标注了可以输出多个物品和概率，那么也可以在`of()`加上`.withChance()`

如果标注了可以添加处理时间，则可以在机器后面添加上`processingTime(tick)`
### 动力辊压机
```js
//压块塑性
create.compacting("minecraft:golden_apple", [
	"minecraft:apple"
])

//压块-加热
create.compacting("minecraft:iron_nugget", [
	"minecraft:iron_ore"
]).heated()

// 压块-超级加热
create.compacting("minecraft:diamond", [
	"minecraft:deepslate_diamond_ore"
]).superheated()

// 压块-流体
create.compacting(Fluid.of("minecraft:water", 1000), [
	Fluid.of("minecraft:lava", 1000)
])

// 压块-概率输出(1%的概率出钻石)
create.compacting([
	"minecraft:stone", 
	Item.of("minecraft:diamond").withChance(0.01)
], "minecraft:magma_block")

// 压片
create.pressing("minecraft:enchanted_golden_apple", [
	"minecraft:golden_apple"
])

// 压片 - 概率输出
create.pressing([
	"minecraft:stone",
	Item.of("diamond").withChance(0.5)
], "minecraft:crying_obsidian")
```
简单解释一下再上面的代码

**压块**和**压片**的第一个参数是<font color=yellow>输出物品和流体,可以使用数组形式,也可以给物品后面添加<font color=blue>.withChance()</font>来添加输出概率,这里注意的是,1为100%</font>,第二个参数是<font color=yellow>输入物品</font>

如果在后面添加<font color=blue>.heated()</font>是需要加热

如果在后面添加<font color=blue>.superheated()</font>是需要超级加热
### 动力搅拌机
```js
// 混合搅拌 - 多个输入
create.mixing("minecraft:grass_block", [
	Fluid.of("minecrft:water", 500),
	"minecraft:dirt"
])

// 混合搅拌 - 加热
create.mixing("minecraft:cooked_cod", [
	"minecraft:cod"
]).heated()

// 混合搅拌 - 超级加热
create.mixing("minecraft:golden_carrot", [
	"minecraft:carrot"
]).superheated()

// 混合搅拌 - 流体输出和概率输出
create.mixing([
	Fluid.of("minecraft:lava", 100),
	Item.of("diamond").withChance(0.3)
], Item.of("minecraft:fire_charge", 64)).superheated()
```

### 鼓风机
```js
//鼓风机 - 洗涤(支持多个输出结果和概率输出)
create.splashing("minecraft:golden_apple", [
	"minecraft:enchanted_golden_apple"
])

//鼓风机 灵魂火烧制(支持多个输出结果和概率输出)
create.haunting(Item.of("minecraft:diamond").withChance(0.1),[
	"minecraft:stone"
])
```

### 石磨
```js
//石磨 - 研磨(支持多个输出结果和概率输出)
create.milling(Item.of("minecraft:oak_planks", 6), [
	"minecraft:chest"
])

```
### 粉碎轮
```js
//粉碎轮 - 粉碎(支持多个输出结果和概率) - 添加处理时间
create.crushing("minecraft:netherite_scrap", [
	"minecraft:ancient_debris"
]).processingTime(20 * 200)
```
### 注液器
```js
//注液 
create.filling("minecraft:soul_torch", [
	"minecraft:torch",
	Fluid.of("minecraft:milk", 500)
])
```
第一个参数为<font color=yellow>输出物品</font>

第二个参数为<font color=yellow>输入物品和流体</font>

这里注意一点，输入一定是物品和流体
### 分液池
```js
//分液
create.emptying([
	Fluid.of("minecraft:lava", 50),
	"minecraft:slime_ball"
], "minecraft:magma_cream")
```
第一个参数为<font color=yellow>输出物品和流体</font>

第二个参数为<font color=yellow>输入物品</font>

这里注意一点，输出一定是物品和流体
### 动力锯
```js
//动力锯 - 切割(支持多个输出结果和概率也可以添加处理时间[processingTime])
create.cutting("minecraft:glowstone", [
	"minecraft:shroomlight"
])
```
### 机械手
```js
//机械手 - 安装(支持多个输出结果和概率)
create.deploying("minecraft:chipped_anvil", [
	"minecraft:damaged_anvil",
	"minecraft:iron_ingot"
])
//机械手 - 安装 - 不消耗手持物品
create.deploying("minecraft:anvil", [
	"minecraft:chipped_anvil",
	"minecraft:iron_ingot"
]).keepHeldItem()
```
第一个参数为<font color=yellow>输出物品</font>

第二个参数为<font color=yellow>输入物品和机械手手持物品</font>

输入物品的第一个为被安装物品，输入物品的第二个为机械手手持物品,所以千万<font color=red>不要写反</font>
### 砂纸
```js
// 砂纸打磨 (支持概率输出)
create.sandpaper_polishing("minecraft:glow_item_frame", [
	"minecraft:item_frame"
])
```
### 动力合成器
```js
// 动力合成 最大9*9
create.mechanical_crafting("minecraft:cow_spawn_egg", [
	"BBBBB",
	"B B B",
	"BBEBB",
	"BMMMB",
	"MMMMM"
], {
	B: "minecraft:beef",
	E: "minecraft:egg",
	M: "minecraft:milk_bucket"
})
```
这个地方他只能类似于json的写法，最大支持9*9的配方

第一个参数为<font color=yellow>输出物品</font>

第二个参数为<font color=yellow>输入配方</font>

第三个参数为<font color=yellow>解释输入配方占位符</font>

<font color=green>建议在写输入配方占位符时，一行看作一个工作台的一排,写完一排换一行,如果有四行,但是在第二行是**完全空出来的**,<font color=red>请用空格填满!!!</font></font>

### 序列组装
```js
/*
 * 将重复使用的物品id单独存入一个常量里
 * 这一步其实可有可无,个人建议不要搞这个
 * 如果使用重复的名称,那么只有最后一条是有效的
 * 为了避免这种情况产生,请避免使用重复的常量&变量命名
*/
const incomplete = "create:incomplete_precision_mechanism"

//序列组装
	create.sequenced_assembly(
		// 输出物品及概率(这里的概率更偏向于占比),
		// 占比越高输出该物品的概率越高
		// 数组内的第一个物品为主要输出物品
		// 其他输出物品则为“废料”
		[
			Item.of("diamond").withChance(0.02),
			Item.of("cobblestone").withChance(0.5),
			Item.of("stone").withChance(0.8)
		],
		// 输入物品
		"minecraft:deepslate",
		// 参与机器--按顺序加工
		[
			// 参与机器--机械手
			create.deploying(incomplete, [incomplete, "minecraft:tnt"]).keepHeldItem(),
			// 参与机器--切石机
			create.cutting(incomplete, incomplete),
			// 参与机器--注液器
			create.filling(incomplete, [incomplete, Fluid.of("minecraft:lava", 100)]),
			// 参与机器--压片
			create.pressing(incomplete,incomplete)
		]
	)
	// 中间件--加工成的半成品物品
	.transitionalItem(incomplete)
	// 循环次数--如果不写默认为5次
	.loops(3)
```

```js
// 下方是无注释写法
const incomplete = "create:incomplete_precision_mechanism"

create.sequenced_assembly([
	Item.of("diamond").withChance(0.02),
	Item.of("cobblestone").withChance(0.5),
	Item.of("stone").withChance(0.8)
], "minecraft:deepslate", [
	create.deploying(incomplete, [incomplete, "minecraft:tnt"]).keepHeldItem(),
	create.cutting(incomplete, incomplete),
	create.filling(incomplete, [incomplete, Fluid.of("minecraft:lava", 100)]),
	create.pressing(incomplete, incomplete)
]).transitionalItem(incomplete).loops(3)
这里值得一提的是，目前官方总共提供只有上面的四种方式进行机器参与

注:序列组装的半成品是可以使用原版的物品的

### 序列组装添加物品为半成品
在序列组装是可以自定义半成品的，这里需要写在游戏加载(startup_scripts)时的文件夹内
```js
StartupEvents.registry("item", (event) => {
	event.create("meng:diamond", "create:sequenced_assembly")
})
```
在`event.create`里的第一个参数为:**物品id**第二个参数为**物品类型**

### 神秘配方
神秘配方从本质上来说只是一个配方显示，他并不是一个可以被合成的配方，所以它并不需要添加任何配方事件
只需要在客户端加载(client_scripts)的文件夹内进行书写就可以了
像下面这样

```js
const $MysteriousItemConversionCategory = Java.loadClass("com.simibubi.create.compat.jei.category.MysteriousItemConversionCategory")
const $ConversionRecipe = Java.loadClass("com.simibubi.create.compat.jei.ConversionRecipe")

$MysteriousItemConversionCategory.RECIPES.add($ConversionRecipe.create("apple", "minecraft:diamond"))
```
这样写的就是代表苹果通过神秘配方转换为钻石，但是这个神秘配方是什么，怎么做，得魔改作者们自己完成，他提供的只是一个JEI提醒，并不具备任何功能