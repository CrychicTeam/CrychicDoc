---
title: 02 第一个物品
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 02 第一个物品 相关教程
image: ./covers/908b0f9eb44b349604fc2f5b3ff855374bc23664.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
authors: ['Flandre923']
---
# 第一个物品 
我们需要继承原版得物品类，实例化这个物品类，之后把这个物品类添加到游戏中去。

对于在注册表中的很多类型，这样的添加方式是类似的。 

```java

public class RubyItem extends Item {
   public RubyItem(Properties pProperties) {
       super(pProperties);
   }
} 
```


很简单只有一个构造函数，构造函数传入一个item的属性即可。

这里的属性规定了是不是食物，堆叠数量，等内容。你可以进入到该类查看。

之后我们实例化物品注册器和使用注册器注册物品 

```java
public class ModItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ExampleMod.MODID);

   public static final Supplier<Item> RUBY = ITEMS.register("ruby",() -> new RubyItem(new Item.Properties()));

   public static void register(IEventBus eventBus){
       ITEMS.register(eventBus);
   }
} 
```

下面将我们的ITEMS注册器加入到总线中。 
```java
public ExampleMod(IEventBus modEventBus)
   {
       modEventBus.addListener(this::commonSetup);
       ModItems.register(modEventBus);
       NeoForge.EVENT_BUS.register(this);
   } 
```
进入游戏即可看到我们添加的物品。这就是我们的第一个物品。

因为我们的红宝石item没有和Item有任何的区别，所有有更简单的一种方式注册物品 
```java
public class ModItems {
   public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ExampleMod.MODID);

   public static final Supplier<Item> RUBY = ITEMS.register("ruby",() -> new Item(new Item.Properties()));

   public static void register(IEventBus eventBus){
       ITEMS.register(eventBus);
   }
} 
```

下面我们对使用的一些类做一些介绍

## Item 类
该 Item 类是Minecraft游戏中的一个核心类，它代表游戏中的物品。每个物品都有其独特的属性和行为，如耐久度、堆叠限制、使用效果等。此类实现了 FeatureElement、ItemLike 和自定义的 IItemExtension 接口，以提供更多的功能和扩展性。

## Item.Properties类
Properties 是一个嵌套在 Item 类内部的静态类，用于配置和初始化物品的属性。它包含了一系列的设置方法，用于定义物品的最大堆叠数量、耐久度、合成剩余物品、稀有度等特性。

## Registries类
此类定义了一个注册表，其中包含了Minecraft游戏中的各种资源和实体的资源键。每个资源键都代表了一个可以在游戏中注册和检索的不同类型。这些资源键用于访问和操作Minecraft中的各种元素，如方块、物品、实体、生物群系等。

## Items类
Items 类是一个静态类，它包含了Minecraft中所有方块对应的物品。每个方块的物品都有一个静态的常量，用于在游戏中快速访问。

## 模型和材质
在resources下构建这样的目录结构 

```
└───resources
   ├───assets
   │   └───examplemod
   │       ├───lang
   │       ├───models
   │       │   └───item
   │       └───textures
   │           └───item
   └───META-INF 
```


assets是你放模组模型，材质的地方。

在item下面添加一个ruby.json，和你的注册的物品的名称相同。 

```json
{
 "parent": "minecraft:item/generated",
 "textures": {
   "layer0": "examplemod:item/ruby"
 }
} 
```

"parent": "minecraft:item/generated”指定了这个模型的「父模型」是什么，而"layer0": "examplemod:item/ruby”指定了具体的材质。examplemod:item:代表这个是在我们自己的assets文件下，item/ruby代表了是textures/item/ruby.png这张图片。

详细的json描述参考wiki，你可以在包中找到游戏原版的json内容

textures/item/ruby.png 放置材质。最好不要大于32*32像素。

启动游戏之后你就可以看见我们有了模型和材质的物品了。 

## 一个制作模型和材质的方便的工具
这里简单提了一下，之后会详细使用它自定义模型。

一个方便的工具用来制作方块和物品等模型：BlockBench。

## Item和Item stack
Itemstack 是 物品堆，背包物品槽位里里面放的每一个都是itemstack。

Itemstack是对Item的拓展，添加额外的属性，NBT标签等等，Item是不同的堆叠数量的内容的抽象，有共同的行为。

 