---
authors: ['Qi Month']
---

# 自定义金属材料集成(Custom Metal Materials Integration)

## 介绍

首先这个项目是一个究极懒人项目, 主要可以直接填入`[id, 颜色, 挖掘等级]`就可以把`锭, 板, 粗矿, 深浅层矿石, 金属块和熔融流体`都可以一并注册的东西

这个项目在代码上看起来非常的简单, 研究的时候主要复杂在矿石的渲染

矿石注册是单独一张矿石的纹理直接渲染在原版的石头上的模型, 实现也很简单, 就单独一层额外模型比石头大那么一点点(一样大小的话会出渲染的问题), 为了可以正常着色(不渲染给石头只渲染矿石), 要在模型内为矿石模型的纹理设置

<!-- ![1](/imgs/CustomMetal/1.png) -->

为了可以正常着色(不渲染给石头只渲染矿石), 要在模型内为矿石模型的纹理设置

<!-- ![2](/imgs/CustomMetal/2.png) -->

效果展示
<!-- ![3](/imgs/CustomMetal/3.png) -->
## 粗略讲解

在矿石的模型中, 有一个`tintindex`键, 这个键在[**Minecraft官方wiki**](https://zh.minecraft.wiki/w/Tutorial:制作资源包/模型?variant:=zh-cn)的解释是

```
使用硬编码的着色索引对纹理重新着色。如果设置为-1，则不进行重新着色。(默认就是-1)
```

因此在模型的纹理中, 加一个`"tintindex: 0"`就可以让这个模型的纹理单独被着色, 具体可以参考上方的代码截图

## 文件下载

<!-- [文件下载](/Code/Projects/CMMI) -->

## 参考文献

[**Minecraft官方wiki**](https://zh.minecraft.wiki/w/Tutorial:制作资源包/模型?variant:=zh-cn)

[**森罗万象资源包制作讲解**](http://sqwatermark.com/resguide/vanilla/model/tintindex.html)

## 源代码展示

<details open>

<summary>项目源码(点击展开或收起)</summary>

```js
// 定义命名空间, 需要的话请自行更改
let namespace  = "new_create:"

// 需要依次填入[id, 颜色, 挖掘等级]
let moltenRegisters = [
	// ["test", 0xabc098, "wooden"]
]
moltenRegisters.forEach(([name, color, level]) => {
	StartupEvents.registry("item", (event) => {
		// 锭
		event.create(`${namespace + name}_ingot`)
			// 指定纹理位置
			.texture(`${namespace}item/metal/ingot`)
			// 定义颜色
			.color(color)
			// 添加Forge标签方便管理统一
			.tag("forge:ingots")
			// 添加Forge标签方便管理统一
			.tag(`forge:ingots/${name}`)

		// 板
		event.create(`${namespace + name}_sheet`)
			// 指定纹理位置
			.texture(`${namespace}item/metal/sheet`)
			// 定义颜色
			.color(color)
			// 添加Forge标签方便管理统一
			.tag("forge:plates")
			// 添加Forge标签方便管理统一
			.tag(`forge:plates/${name}`)

		// 矿
		event.create(`${namespace}raw_${name}`)
			// 指定纹理位置
			.texture(`${namespace}item/metal/raw_ore`)
			// 定义颜色
			.color(color)
			// 添加Forge标签方便管理统一
			.tag("forge:raw_materials")
			// 添加Forge标签方便管理统一
			.tag(`forge:raw_materials/${name}`)
	})

	StartupEvents.registry("block", (event) => {
		let pickaxe = "minecraft:mineable/pickaxe"

		// 挖掘等级
		let miningLevel = {
			wooden: "minecraft:needs_wooden_tool",
			stone: "minecraft:needs_stone_tool",
			iron: "minecraft:needs_iron_tool",
			gold: "minecraft:needs_gold_tool",
			diamond: "minecraft:needs_diamond_tool",
			nether: "forge:needs_netherite_tool"
		}

		// 浅层
		event.create(`${namespace + name}_ore`)
			// 指定模型位置
			.model(`${namespace}block/ore/ore`)
			// 定义颜色(其中的0请看上面的粗略讲解)
			.color(0, color)
			// 设置渲染类型, 让模型可以渲染透明纹理
			.renderType("cutout")
			// 设置声音类型
			.soundType(SoundType.STONE)
			// 添加硬度
			.hardness(3)
			// 添加爆炸抗性
			.resistance(3)
			// 添加Forge标签方便管理统一
			.tag("forge:ores")
			// 添加Forge标签方便管理统一
			.tag(`forge:ores/${name}`)
			// 添加Forge标签方便管理统一
			.tag("forge:ore_rates/dense")
			// 添加镐子挖掘
			.tagBlock(pickaxe)
			// 添加挖掘等级
			.tagBlock(miningLevel[level])
			// 必须工具挖掘
			.requiresTool(true)
			// 设置物品模型渲染颜色
			.item((item) => {
				item.color(0, color)
			})

		// 深层
		event.create(`${namespace}deepslate_${name}_ore`)
			// 指定模型位置
			.model(`${namespace}block/ore/deepslate_ore`)
			// 定义颜色(其中的0请看上面的粗略讲解)
			.color(0, color)
			// 设置渲染类型, 让模型可以渲染透明纹理
			.renderType("cutout")
			// 设置声音类型
			.soundType(SoundType.DEEPSLATE)
			// 添加硬度
			.hardness(4.5)
			// 添加爆炸抗性
			.resistance(4.5)
			// 添加Forge标签方便管理统一
			.tag("forge:ores")
			// 添加Forge标签方便管理统一
			.tag(`forge:ores/${name}`)
			// 添加Forge标签方便管理统一
			.tag("forge:ore_rates/deepslate")
			// 添加镐子挖掘
			.tagBlock(pickaxe)
			// 添加挖掘等级
			.tagBlock(miningLevel[level])
			// 必须工具挖掘
			.requiresTool(true)
			// 设置物品模型渲染颜色
			.item((item) => {
				item.color(0, color)
			})

		// 块
		event.create(`${namespace + name}_block`)
			// 指定纹理位置
			.textureAll(`${namespace}block/metal/block`)
			// 设置声音类型
			.soundType(SoundType.METAL)
			// 定义颜色
			.color(color)
			// 添加硬度
			.hardness(5)
			// 添加爆炸抗性
			.resistance(5)
			// 添加Forge标签方便管理统一
			.tag("forge:storage_blocks")
			// 添加Forge标签方便管理统一
			.tag(`forge:storage_blocks/${name}`)
			// 添加镐子挖掘
			.tagBlock(pickaxe)
			// 添加挖掘等级
			.tagBlock(miningLevel[level])
			// 必须工具挖掘
			.requiresTool(true)
			// 设置物品模型渲染颜色
			.item((item) => {
				item.color(color)
			})
	})

	StartupEvents.registry("fluid", (event) => {
		// 流体需要放在[modid/textures/block/fluid]
		const PATH = "block/fluid/"

		// 熔融金属
		event.create(`${namespace}molten_${name}`)
			// 颜色
			.thinTexture(color)
			// 颜色
			.bucketColor(color)
			// 纹理位置
			.flowingTexture(`${namespace + PATH}flowing`)
			// 纹理位置
			.stillTexture(`${namespace + PATH}still`)
			// 添加Forge标签方便管理统一
			.tag(`forge:molten_${name}`)
			// 添加Forge标签方便管理统一
			.tag("forge:molten_materials")
	})
})
```
</details>