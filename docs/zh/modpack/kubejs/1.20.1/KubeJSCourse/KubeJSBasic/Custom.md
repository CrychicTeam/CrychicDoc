---
authors:
  - Gu-meng
editor: Gu-meng
---
# 通用配方修改
在本章中将会介绍如何去修改基本上所有的配方，无论该模组是否和kjs联动，我们都可以进行修改，当然前提是他是一个标准配方

## 找配方
在本章节中使用的是虚无世界3(AoA3)里的聚合台进行添加配方

首先在游戏内使用 **F3+H** 打开高级提示框，然后进入聚合台的配房里，可以看到物品下面有一个**配方ID**

之后我们进入到`mods`文件夹下，找到我们需要添加配方的模组，这里我们是`AoA3`所以，这里使用解压软件打开jar文件(这里也可以将jar文件复制一份，然后将.jar后缀改称为.zip文件然后打开)

打开压缩包后进入文件路径`data\modid\recipes`文件夹路径下，这里我的modid是`aoa3`所以路径是`data\aoa3\recipes`

然后我们随便找一个聚合台的配方,查看配方id,然后在`data\modid\recipes`找到该配方id点进去

这里我的是aoa3里的`infusion_crystallis_helmet.json`文件,打开后是下面这样的

```json
{
	"type": "aoa3:infusion",
	"ingredients": [
		{
			"item": "aoa3:padded_cloth"
		},
		{
			"item": "aoa3:padded_cloth"
		},
		{
			"item": "aoa3:armour_plating"
		},
		{
			"item": "aoa3:rainbow_druse"
		},
		{
			"item": "aoa3:rainbow_druse"
		},
		{
			"item": "aoa3:green_druse"
		},
		{
			"item": "aoa3:blue_druse"
		}
	],
	"input": {
		"item": "aoa3:helmet_frame"
	},
	"result": {
		"item": "aoa3:crystallis_helmet"
	}
}
```

## 添加配方
在上面我们找到了聚合台的配方添加写法，kjs提供了数据包的添加配方方式，这样我们就可以照葫芦画瓢的写一个配方

```js
ServerEvents.recipes(e => {
	e.custom({
		"type": "aoa3:infusion",
		"ingredients": [
			{
				"item": "minecraft:soul_lantern"
			},
			{
				"item": "minecraft:soul_lantern"
			},
			{
				"item": "minecraft:lantern"
			},
			{
				"item": "minecraft:lantern"
			}
		],
		"input": {
			"item": "minecraft:emerald"
		},
		"result": {
			"item": "minecraft:grass_block"
		}
	})
})
```

## 总结
其实通用配方和自己手写数据包添加或修改配方是一样的

但是kjs可以将他写成一个方法(函数/function)这样就可以重复的复用(该内容不在本章节中)
