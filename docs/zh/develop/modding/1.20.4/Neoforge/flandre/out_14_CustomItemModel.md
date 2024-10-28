---
title: 14 自定义物品模型
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 14 自定义物品模型 相关教程
image: ./covers/9e6ec36a9a8a2f91e68c5af12c055f1248f63288.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 自定义物品模型

添加一个物品类

```java
public class RubyWand extends Item {
    public RubyWand(){
        super(new Properties().stacksTo(1));
    }
}

```

注册物品

```java
    public static final Supplier<Item> RUBY_WAND = ITEMS.register("ruby_wand", RubyWand::new);


```

添加模型和材质

放到对应的路径下就可以了，和之前一样的。

```
│  ├─examplemod
│  │  ├─blockstates
│  │  ├─models
│  │  │  ├─block
│  │  │  └─item
│  │  └─textures
│  │      ├─block
│  │      └─item

```

这里的模型和材质是使用blockbench画的，这里做一下简单的介绍，详细的介绍可以在B战搜索教学视频，或者看我之前的视频。

添加到创造模式物品栏

略

