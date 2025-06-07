---
authors:
  - Gu-meng
editor: Gu-meng
---
# 模型介绍

首先模型不仅仅适用于方块, 同样可以适用于物品, 在这篇文档不会教你怎么制作模型, 若想学习模型制作请前往[**另一个文档**](../Digression/BlockbenchBasic)

这篇文档的作用主要就是简单讲解一下模型相关的内容, 包括模型文件的存放, 代码注册时的方法说明

# 正文

## 模型文件存放位置
那么首先是模型的存放, 很简单

物品的模型存放于`assets/${modid}/models/item`

方块的模型存放于`assets/${modid}/models/block`

并且模型文件名必须和方块/物品的id一致, 其次文件格式**必须为**`.json`

## 注册时的方法

首先是物品的注册方法`.model`, 这个方法根据直接填入模型的路径即可, 例如

```js
StartupEvents.registry("item", (event) => {
	let MODID = "test_mod"
	event.create(MODID + "test_item")
		.model("test_mod:item/test_item")
})
```

上面的`let MODID = "test_mod"`是自定义的modid(命名空间), 在指定模型的时候必须和这个一样, 如果不写那就是默认的`kubejs`

接下来是方块的注册方法

```js
StartupEvents.registry("block", (event) => {
	let MODID = "test_mod"
	event.create("test_mod:test_block")
		.model("test_mod:block/test_block")
})
```

和上面一样, `let MODID = "test_mod"`是自定义的modid(命名空间), 在指定模型的时候必须和这个一样, 如果不写那就是默认的`kubejs`

## 简单的模型制作

### 多面方块

这里是一个简单的方块模型, 用不着blockbench

我们拿类似于原版熔炉这种几个面的方块参考

首先你需要准备`正面` `侧面`以及`顶面`的贴图

在`models/block`下创建一个方块id的`json`文件, 然后里面首先写上(不要直接复制这个`json`文件, 真正的`json`文件是不允许注释的哦)

```json
{
	// 其实这个minecraft:可以不用写, 但是按照规范我选择写上
	"parent": "minecraft:block/orientable"
}
```

首先这个是读取父模型, 也就是你的这个模型是基于这个的父模型来进行编写的

然后到纹理, 继续写上

```json
{
	// 其实这个minecraft:可以不用写, 但是按照规范我选择写上
	"parent": "minecraft:block/orientable",
	"textures": {
		// 那么首先这里第一个是正面的纹理
		"front": "test_mod:block/front",
		// 侧面
		"side": "test_mod:block/side",
		// 顶面
		"top": "test_mod:block/top"
	}
}
```

这样一个简单的方块模型就写好了, 贴图只需要放在`assets/test_mod/textures/block`下即可

### 物品

然后轮到物品, 物品更简单, 直接看源码

```json
{
	"parent": "minecraft:item/generated",
	"textures": {
		"layer0": "test_mod:item/test_item"
	}
}
```
