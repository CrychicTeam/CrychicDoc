---
authors: ['Gu-meng']
---
# 本地化lang(语言文件)
这章主要是教会大家如何添加以及修改别的Mod的本地化文件(语言文件)

* ### 文件路径
  `lang`文件主要存储于`assets/${modid}/lang`,不同的国家也有着不同的文件名称,具体请查看[**Wiki**](https://zh.minecraft.wiki/w/语言)或者文档[内部链接](../Digression/LangFileNamingChart.md),而各文件都以`json`作为后缀,例如`en_us.json` `zh_cn.json` `zh_tw.json`等 

  `minecraft:sand` `minecraft`是`modid`

* ### 写法
  * `lang`主要由本地化键名和文本组成,例如
```json
{
    "item.kubejs.test_item": "测试物品",
    "block.kubejs.test_block": "测试物品",
    "fluid.kubejs.test_fluid": "测试流体"
}
```
  在写`lang`的时候最后一行**千万不可以加逗号**作为结尾,也**不可以不写逗号就写下一行,更不可以写注释!**
  * 而本地化键名(**key**)主要由`type.modid.id`组成
  `type`指的是类型,例如:
  流体是`fluid`
  物品是`item`
  方块是`block`
  * 而在写代码的时候也可以自己添加一串自定义的`key`,例如给石头添加`Tooltip`,举个简单的例子

```js
ItemEvents.tooltip((event) => {
	event.add('minecraft:stone', [Text.translate("tip.mc.stone")])
})
```

```json
{
    "tip.mc.stone": "我是一块石头"
}
```
  同时在写`lang`的时候也可以给文本添加`颜色`或者使用`转义字符`

* ### 修改 & 本地化
  * 在修改别的Mod的`lang`文件时需要先创建一个资源包,具体教程请看[**Wiki**](https://zh.minecraft.wiki/w/Tutorial:制作资源包)
  随后在`assets`文件夹下创建一个和`Modid`相同的文件夹
  
  * 这种本地化资源包建议直接内置, 所以和前面说的一样, 需要一个Mod来进行资源包的内置加载[**Json Things**](https://www.mcmod.cn/class/7734.html)

  * 你也可以复制`ModFile.jar/assets`下的文件夹名称,然后自己创建一个文件夹,随后和开头所说的一样,`${modid}`下再创建一个`lang`文件,随后把需要修改的`lang`文件解压进去便是
  * 如果是汉化Mod文本也一样,把原先的`lang`文件解压出来后改名为`zh_cn.json`**(请严格确保不是`zh_cn.json.txt`或`zh_cn.txt.json`)**

  **一切的Modid只能由`小写字母,数字和下划线(a-z,0-9和_)`组成**

* ### 快捷编写语言文件(仅限于你自己注册的新东西)
  * 在KubeJS 6中有一个`ClientEvents.lang`事件, 相对完整的格式是
```js
ClientEvents.lang("zh_cn", (event) => {
	event.add("item.kubejs.test_item", "科比吉斯测试物品")
	event.add("block.kubejs.test_block", "科比吉斯测试方块")
	event.add("fluid.kubejs.test_fluid", "科比吉斯测试流体")
})
```
  * 不难看出`ClientEvents.lang`后的第一个参数为上面所提到的语言代码, 而下面的写法和Json的几乎一样
  * 或许有人会问  **"誒那和Json有什么大的区别呢?"**
  * 首先Json本质上不属于编程语言, 按理说是属于存储数据的东西, 自然也不存在各种奇怪的语法(**不代表Json没有语法需求**), 但是这是JavaScript, JavaScript可是有着循环功能的, 有编程基础的朋友可能已经明白我在说什么了,我也废话不多说, 直接上代码!
```js
ClientEvents.lang("zh_cn", (event) => {
	const MODID = "kubejs"

	// 物品翻译
	let itemResourceLang = [
		["test_item_1", "科比吉斯测试物品1"],
		["test_item_2", "科比吉斯测试物品2"]
	]
	itemResourceLang.forEach(([key, text]) => {
		event.add(`item.${MODID}.${key}`, text)
	})

	// 方块翻译
	let blockResourceLang = [
		["test_block_1", "科比吉斯测试方块1"],
		["test_block_2", "科比吉斯测试方块2"]
	]
	blockResourceLang.forEach(([key, text]) => {
		event.add(`block.${MODID}.${key}`, text)
	})

	// 流体翻译
	let fluidResourceLang = [
		["test_fluid_1", "科比吉斯测试流体1"],
		["test_fluid_2", "科比吉斯测试流体2"]
	]
	fluidResourceLang.forEach(([key, text]) => {
		/*
		 * 由于流体在不同的地方显示的类型不同
		 * 因此需要为不同的环境配方单独的lang
		 * JEI显示的是fluid
		 * 大世界显示的block
		 * 而桶显示的是item,由于id后面带桶,因此需要加上_bucket和桶
		 */
		event.add(`fluid.${MODID}.${key}`, text)
		event.add(`block.${MODID}.${key}`, text)
		event.add(`item.${MODID}.${key}_bucket`, `${text}桶`)
	})
})
```