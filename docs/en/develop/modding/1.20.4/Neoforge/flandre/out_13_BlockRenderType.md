---
title: 13 方块渲染类型
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 13 方块渲染类型 相关教程
image: ./covers/2e76acce7aa48564aa5295a7dc2d4e3bb04668f4.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考了该文章

https://boson.v2mcdev.com/block/rendertype.html

## 方块渲染类型

渲染类型（RenderType)

![alt text](image.png)

图片出自TheGreyGhost博客

- Translucent（半透明）:
    - 这种类型的方块可以渲染透明或半透明的纹理，如玻璃或水。
    - 渲染半透明方块比渲染不透明方块更耗费资源，因为它们需要根据深度和透明度进行混合。
    - Translucent方块的渲染通常在所有其他类型的方块之后进行，以确保正确的混合效果。
- Solid（实体）:
    - 实体方块是Minecraft中最常见的类型，如石头、土块等。
    - 实体方块的纹理不透明，因此它们的渲染相对简单且高效。
    - Solid方块的渲染通常首先进行，因为它们不会受到透明度的影响。
- Cutout（裁剪）:
    - 裁剪方块通常用于具有清晰边缘的方块，如草或树叶。
    - Cutout渲染类型通过不渲染方块内部的像素来提高性能，只渲染方块的轮廓。
    - 这使得方块看起来是透明的，但实际上并不进行透明度混合，因此比半透明渲染更快。
- Cutout Mipped（裁剪+Mipmap）:
    - Cutout Mipped是Cutout的一个变种，它结合了裁剪渲染和Mipmap技术。
    - Mipmap是一种纹理过滤技术，它可以提高远处纹理的视觉质量，减少闪烁和模糊。
    - Cutout Mipped适用于远处看起来也应该清晰的方块，如树叶或栅栏。

Mipmapping技术

Mipmapping是一种纹理映射技术，用于提高远距离纹理的视觉效果。它通过预先计算并存储纹理的不同分辨率版本（称为Mipmaps）来工作。当物体远离摄像机时，渲染引擎会自动选择更低的分辨率Mipmap，从而减少纹理模糊和闪烁，提高渲染效率。

Mipmapping的关键优势是提高了渲染性能和视觉质量。然而，它也需要额外的内存来存储不同分辨率的纹理，并且对于具有清晰边缘或细节的纹理，Mipmaps可能会导致细节丢失。因此，Minecraft在默认情况下只为某些方块类型（如Cutout Mipped）启用Mipmapping，以平衡性能和视觉效果。

## 创建一个半透明的玻璃罐

添加一个类

```java
public class GlassJar extends Block {
    public GlassJar(){
        super(Properties.ofFullCopy(Blocks.GLASS).strength(5f).noOcclusion());
    }
}


```
注册方块和物品

```java
    public static final Supplier<Block> GLASS_JAR = registerBlock("glass_jar", GlassJar::new);

```

模型和材质

这里的模型和材质使用了blockbench画出来的，自己也可以去画一画。关于blockbench的使用可以去B战找视频看。或者看我之前的介绍的视频

我们需要给模型的文件添加上一个指定的渲染的类型，我们要渲染的是一个半透明的玻璃的材质，我们使用的渲染的类型是：translucent

```diff

	"credit": "Made with Blockbench",
	"texture_size": [32, 32],
+ 	"render_type": "translucent",
	"textures": {
		"0": "examplemod:block/glass_jar",
		"particle": "examplemod:block/glass_jar"
	},
    ...<省略>
```

blockstate和物品model生成

```java

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(ModBlocks.RUBY_BLOCK.get(),cubeAll(ModBlocks.RUBY_BLOCK.get()));
        this.propertyBlock(ModBlocks.LAMP_BLOCK.get());
        this.addWithHaveModel(ModBlocks.RUBY_FRAME.get(),"ruby_frame");
        this.glassModel(ModBlocks.GLASS_JAR.get(),"glass_jar");
    }

    public void glassModel(Block block,String name){
        var model = models().getExistingFile(new ResourceLocation(ExampleMod.MODID,name));
        getVariantBuilder(block).partialState().setModels(new ConfiguredModel(model));
        simpleBlockItem(block,model);
    }
```

添加到创造模式物品栏，略和之前一样操作。



