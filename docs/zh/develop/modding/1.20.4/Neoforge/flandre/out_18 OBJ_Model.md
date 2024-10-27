---
title: 18 OBJ模型
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 18 OBJ模型 相关教程
image: ./covers/9e6ec36a9a8a2f91e68c5af12c055f1248f63288.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 前言

以下的内容大量参考了https://boson.v2mcdev.com/specialmodel/obj.html

## OBJ模型

添加一个方块类

不在赘述

```java

public class ObsidianObj extends Block {
    public ObsidianObj() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noOcclusion());
    }

}

```

注册方块

```java
    public static final Supplier<Block> OBSIDIAN_OBJ = registerBlock("obsidian_obj", ObsidianObj::new);

```

添加到创造模式物品栏

略

模型和材质

如果你对OBJ模型的一些术语不了解，这里建议先去了解下。这是一个简短的[说明](https://segmentfault.com/a/1190000021126476)

存放位置

```
    ├─assets
    │  └─examplemod
    │      ├─blockstates
    │      ├─lang
    │      └─models
    │          ├─block
               │  ├─obsidian_obj.json  
               │  ├─obsidian_obj.obj
               │  ├─obsidian_obj.mtl  
    │          └─item

```

json文件的内容，使用loader为neoforge加载obj格式的模型，通过model指明模型的具体位置，flip_v设置为true是因为blender中的材质和minecraft中材质是上下颠倒的，需要手动反转。
```json
{
  "loader": "neoforge:obj",
  "model": "modid:models/block/obsidian_obj.obj",
  "flip_v": true
}
```

需要修改的地方

修改obj模型文件中的

mtllib obsidian_obj.mtl

修改mtl文件中

map_Kd examplemod:block/obsidian_obj

将材质存放在材质的文件夹中即可。

生成blockstate

```java
        this.addWithHaveModel(ModBlocks.OBSIDIAN_OBJ.get(),"obsidian_obj");
```

## 常见坑处理方法

以下的内容摘自https://boson.v2mcdev.com/specialmodel/obj.html

1. 环境光遮蔽

2. 夜晚的不自然光

开发小课堂

如果你在使用Blender制作OBJ模型，请将你的模型中心点设置为X:0.5m,Y-0.5m,Z:0.5，这样你就不需要在json文件中进行额外的偏移计算了。Minecraft一个满方块在Blender里刚好是1m*1m*1m。


详细见：
https://boson.v2mcdev.com/specialmodel/obj.html

## 参考

https://github.com/MadsWedendahlKruse/test-mod-please-ignore/blob/main/src/main/resources/assets

https://boson.v2mcdev.com/specialmodel/obj.html