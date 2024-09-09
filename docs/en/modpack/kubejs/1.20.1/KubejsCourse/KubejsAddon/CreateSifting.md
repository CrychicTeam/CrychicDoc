# 机械动力：筛子
本章将会介绍如何使用kubejs来修改机械动力的筛子

这里交代本章教程所用到的各个模组和forge的版本，如果版本不同导致报错，可能是作者进行代码更改:
1. forge-47.3.7
2. JEI-15.3.0.4
3. rhino-2001.2.2-build.18
4. architectury-9.2.14
5. kubejs-2001.6.5-build.7
6. probejs-6.0.1
7. create-0.5.1f
8. createsifter-1.20.1-1.8.1.e-22

## 注册筛网
机械动力：筛子提供了筛网的注册，我们只需要注册物品，并赋予材质(大概率可能是模型,孤梦这边没看官方怎么整的)和汉化就可以了

```js
StartupEvents.registry("item", (event) => {
	// 普通筛网
	event.create("meng:mesh", "createsifter:mesh")
	// 高级筛网
	event.create("meng:advanced_mesh", "createsifter:advanced_mesh")
})
```

## 配方的修改
机械动力：筛子 提供了一个修改筛子的配方
```js
ServerEvents.recipes((event) => {
	const { createsifter } = event.recipes

	createsifter.sifting(output[], input[], processingTime, isWater, minimumSpeed)
})
```
`output` : 输出物品 -- ***必须填写***

`input` : 输入物品 -- ***必须填写***

`processingTime` :  处理时间(tick为单位) -- **非必填** -- 默认为 `100`

`isWater` ： 是否浸水处理 -- **非必填** -- 默认为`false`

`minimumSpeed` ： 最小处理速度 -- **非必填** -- 默认为`1.0`

后三项都可以使用方法进行配置, 其中浸水为`.waterlogged()`, 默认为`true`

问题1:

筛网填写在哪里

答:

我们可以看到output是一个数组，这里面一个参数为输入物品第二个参数则为筛网的物品id了

问题2:

两种筛子但是只有一个配方，我该怎么区分两种筛子配方

答:

在官方给的筛子中可以看到有高级黄铜筛网，如果将筛网填写为高级黄铜筛网或者你自己注册的物品类型为`advanced_mesh`，就会识别到黄铜筛子当中，其他的都会被识别到普通筛子当中

## 关于配方修改的简单轮子
```js
// 线筛网
function stringMesh(output, input, time, isWater) {
	sifting(output, [input, "createsifter:string_mesh"], time, isWater)
}

// 安山筛网
function andesiteMesh(output, input, time, isWater) {
	sifting(output, [input, "createsifter:andesite_mesh"], time, isWater)
}
// 锌筛网
function zincMesh(output, input, time, isWater) {
	sifting(output, [input, "createsifter:zinc_mesh"], time, isWater)
}
// 黄铜筛网
function brassMesh(output, input, time, isWater) {
	sifting(output, [input, "createsifter:brass_mesh"], time, isWater)
}
// 高级黄铜筛网
function advancedBrassMesh(output, input, time, isWater) {
	sifting(output, [input, "createsifter:advanced_brass_mesh"], time, isWater)
}

/**
 * 
 * @param {*} output 输出
 * @param {*} input 输入
 * @param {*} time 不填写默认为5秒
 * @param {*} isWater 不填写默认为false
 */
function sifting(output, input, time, isWater) {
	if (time == undefined) time = 5
	if (isWater == undefined) isWater = false
	ServerEvents.recipes((event) => {
		const createsifter = event.recipes.createsifter
		createsifter.sifting(output, input, time * 20, isWater)
	})
}
```
在使用时只需要调用stringMesh()

就不用每次重复传入筛网了

## 关于筛网材质模型问题
孤梦这边简单整了一个模型(当然大家也可以自己去把官方的模型)

模型需要放在`assets/modid/models/item/itemId.json`

这里的modid是你的模组id

itemId是你的物品id

这里简答说一下，如果直接更改的话
1. 0代表中间网格使用的材质
2. 1代表筛网边框使用的材质
```json
{
	"textures": {
		"0": "minecraft:block/iron_block",
		"1": "minecraft:block/oak_planks",
		"particle": "minecraft:block/iron_block"
	},
	"elements": [
		{
			"from": [15, 0, 0],
			"to": [16, 1, 16],
			"rotation": {"angle": 0, "axis": "y", "origin": [15, 0, 16]},
			"faces": {
				"north": {"uv": [0, 0, 1, 1], "texture": "#1"},
				"east": {"uv": [0, 0, 16, 1], "texture": "#1"},
				"south": {"uv": [0, 0, 1, 1], "texture": "#1"},
				"west": {"uv": [0, 0, 16, 1], "texture": "#1"},
				"up": {"uv": [0, 0, 16, 1], "rotation": 270, "texture": "#1"},
				"down": {"uv": [0, 0, 16, 1], "rotation": 90, "texture": "#1"}
			}
		},
		{
			"from": [0, 0, 0],
			"to": [1, 1, 16],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 16]},
			"faces": {
				"north": {"uv": [0, 0, 1, 1], "rotation": 90, "texture": "#1"},
				"east": {"uv": [0, 0, 16, 1], "texture": "#1"},
				"south": {"uv": [0, 0, 1, 1], "rotation": 270, "texture": "#1"},
				"west": {"uv": [0, 0, 16, 1], "rotation": 180, "texture": "#1"},
				"up": {"uv": [0, 0, 16, 1], "rotation": 270, "texture": "#1"},
				"down": {"uv": [0, 0, 16, 1], "rotation": 270, "texture": "#1"}
			}
		},
		{
			"from": [1, 0, 0],
			"to": [15, 1, 1],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 0]},
			"faces": {
				"north": {"uv": [0, 0, 14, 1], "texture": "#1"},
				"east": {"uv": [0, 0, 1, 1], "texture": "#1"},
				"south": {"uv": [0, 0, 14, 1], "texture": "#1"},
				"west": {"uv": [0, 0, 1, 1], "texture": "#1"},
				"up": {"uv": [0, 0, 14, 1], "texture": "#1"},
				"down": {"uv": [0, 0, 14, 1], "texture": "#1"}
			}
		},
		{
			"from": [1, 0, 15],
			"to": [15, 1, 16],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 15]},
			"faces": {
				"north": {"uv": [0, 0, 14, 1], "texture": "#1"},
				"east": {"uv": [0, 0, 1, 1], "texture": "#1"},
				"south": {"uv": [0, 0, 14, 1], "texture": "#1"},
				"west": {"uv": [0, 0, 1, 1], "texture": "#1"},
				"up": {"uv": [0, 0, 14, 1], "texture": "#1"},
				"down": {"uv": [0, 0, 14, 1], "texture": "#1"}
			}
		},
		{
			"from": [6, 0, 1],
			"to": [7, 1, 15],
			"rotation": {"angle": 0, "axis": "y", "origin": [6, 0, 17]},
			"faces": {
				"north": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "rotation": 270, "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "rotation": 90, "texture": "#0"}
			}
		},
		{
			"from": [9, 0, 1],
			"to": [10, 1, 15],
			"rotation": {"angle": 0, "axis": "y", "origin": [9, 0, 17]},
			"faces": {
				"north": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "rotation": 270, "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "rotation": 90, "texture": "#0"}
			}
		},
		{
			"from": [13, 0, 1],
			"to": [14, 1, 15],
			"rotation": {"angle": 0, "axis": "y", "origin": [13, 0, 17]},
			"faces": {
				"north": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "rotation": 270, "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "rotation": 90, "texture": "#0"}
			}
		},
		{
			"from": [1, 0, 3],
			"to": [15, 1, 4],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 3]},
			"faces": {
				"north": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "texture": "#0"}
			}
		},
		{
			"from": [1, 0, 6],
			"to": [15, 1, 7],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 6]},
			"faces": {
				"north": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "texture": "#0"}
			}
		},
		{
			"from": [1, 0, 12],
			"to": [15, 1, 13],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 12]},
			"faces": {
				"north": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "texture": "#0"}
			}
		},
		{
			"from": [1, 0, 9],
			"to": [15, 1, 10],
			"rotation": {"angle": 0, "axis": "y", "origin": [1, 0, 9]},
			"faces": {
				"north": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "texture": "#0"}
			}
		},
		{
			"from": [2, 0, 1],
			"to": [3, 1, 15],
			"rotation": {"angle": 0, "axis": "y", "origin": [2, 0, 17]},
			"faces": {
				"north": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"east": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"south": {"uv": [0, 0, 1, 1], "texture": "#0"},
				"west": {"uv": [0, 0, 14, 1], "texture": "#0"},
				"up": {"uv": [0, 0, 14, 1], "rotation": 270, "texture": "#0"},
				"down": {"uv": [0, 0, 14, 1], "rotation": 90, "texture": "#0"}
			}
		}
	],
	"display": {
		"thirdperson_righthand": {
			"translation": [-5.5, 3, -3.5],
			"scale": [0.84, 0.92, 0.99]
		},
		"firstperson_righthand": {
			"rotation": [13, 0, 0],
			"translation": [8.25, 1.75, -12.75],
			"scale": [1.25, 1.25, 1.25]
		},
		"ground": {
			"translation": [0, 4.75, 0]
		},
		"gui": {
			"rotation": [97, 0, 0],
			"translation": [0, -0.5, 0],
            "scale": [0.8, 0.8, 0.8]
		},
		"fixed": {
			"rotation": [90, 0, 0],
			"scale": [1, 0.04, 1]
		}
	},
	"groups": [
		{
			"name": "group",
			"origin": [1, 0, 11],
			"color": 0,
			"children": [0, 1, 2, 3]
		},
		{
			"name": "group",
			"origin": [10, 0, 17],
			"color": 0,
			"children": [4, 5, 6, 7, 8, 9, 10]
		},
		11
	]
}
```