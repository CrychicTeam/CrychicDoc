---
authors:
  - Gu-meng
editor: Gu-meng
---
# 纹理

这篇文档的作用主要就是简单讲解一下纹理(贴图)相关的内容, 包括纹理文件的存放, 代码注册时和纹理有关的方法说明

# 正文

## 纹理文件存放位置

纹理文件存放在`assets/${modid}/textures`目录下, 其中包含有`block`, `entity`, `item`以及`models`等文件夹, 分别对应不同的纹理类型, 其中

* `block`对应方块的纹理
* `item`对应物品的纹理
* `entity`对应着实体(生物还有三叉戟, 箭矢等特殊处理模型的纹理)
* `models`文件夹内还有一层`armor`, 主要用于存放盔甲的纹理

以上介绍的几个目录是在`KubeJS`注册中常用的几个文件夹, 在高级一点的`gui`啥的就不是`KubeJS`能做到的了

在Mod的注册方式中不单单将纹理放在对应的位置, 还需要在模型中将纹理给应用上, 详见[**关于模型的初步讲解**](./Model)

## 注册时的方法介绍

在物品的注册中只有一个`texture`方法, 这个直接将对应路径写上去即可(这个方法一般用于指定某些不在默认位置的纹理, 还有那种指定一张白底图然后直接着色处理的情况(需要搭配`color`方法使用))

但是在方块的注册中有着`textureAll`, `textureSide`, `texture`三种方法负责处理方块上不同位置的纹理

首先是`textureAll`, 这个方法用于将一个纹理渲染到每个面上, 只需要填入路径定位到纹理文件即可, `textureSide`同理, 不同的是这个方法用于将一个纹理渲染到侧边的面上

而`texture`则用于将一个纹理渲染到指定位置, 根据`probejs`的提示, 在填写纹理路径之前还需要填入一个`id`的参数, 这个参数用于指明这个纹理在哪个位置上

类似于熔炉那样的不同的面有不同纹理的方块, 模型文件中会有一个`textures`的键, 键值中的每个键名都是一个纹理对象, 键值则是纹理的路径, 其中`id`就是纹理在纹理数组中的每一个键名

```json
{
	"parent": "minecraft:block/orientable",
	"textures": {
		"front": "minecraft:block/furnace_front",
		"side": "minecraft:block/furnace_side",
		"top": "minecraft:block/furnace_top"
	}
}
```

我们来看原版的熔炉模型

`front`对应着正面的纹理
`side`对应着侧边的纹理
`top`对应着顶面的纹理

在上面的方法中第一个参数中的参数则是需要填入这些键名[**String**], 然后才是纹理的路径
