---
title: 19 添加画
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 19 添加画 相关教程
image: ./covers/9ca1e418b3a11d6f74c00b0e86e36a790e3173db.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 画

```java
public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(Registries.PAINTING_VARIANT, ExampleMod.MODID);

    public static final Supplier<PaintingVariant> XI_GUA = PAINTING_VARIANTS.register("xi_gua",()-/>new PaintingVariant(16,32));

    public static final Supplier<PaintingVariant> JIU = PAINTING_VARIANTS.register("jiu",()-/>new PaintingVariant(16,16));

    public static void register(IEventBus eventBus){
        PAINTING_VARIANTS.register(eventBus);
    }
}
```
这个类ModPaintings使用NeoForge提供的DeferredRegister来注册新的画作变体到Minecraft中。

DeferredRegister<PaintingVariant/>：在这里，它用于注册PaintingVariant对象。

Registries.PAINTING_VARIANT：这是Minecraft的PaintingVariant注册表，用于存储所有可用的画作变体。

PAINTING_VARIANTS.register("xi_gua",()->new PaintingVariant(16,32))：这里通过DeferredRegister的register方法注册一个名为"xi_gua"的画作变体，其尺寸为16x32。

其他都是常规操作了。

别忘记了将你的DeferredRegister实例添加到总线上去。

```java
        ModPaintings.register(modEventBus);
```

创建 如下的目录结构

```
├─data
│  └─minecraft
│      └─tags
│          └─painting_variant

```

在└─painting_variant下创建placeable.json 文件

添加如下的内容：

```java
{
  "values": [
    "examplemod:xi_gua",
    "examplemod:jiu"
  ]
}
```

你的图的位置应放在painting下

```
│  │  └─textures
│  │      ├─block
│  │      ├─item
│  │      └─painting

```

该json文件也可以使用datagenerator生成，就留给大家去尝试了。