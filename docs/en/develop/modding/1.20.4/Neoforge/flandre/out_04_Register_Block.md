---
title: 04 添加一个方块
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 04 添加一个方块 相关教程
image: ./covers/03c577cdc4bd410a1bdf32827f6442f89e1ad15e.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
---
# 第一个方块
在Minecraft中，如果你想添加一个新的方块，你需要创建一个新的Java类来代表这个方块。这个类需要继承自Block类，并使用Properties类来设置方块的一些属性，如硬度、爆炸抗性等。在下面的代码中，我们创建了一个名为RubyBlock的类，这个类代表了一个新的方块，它的属性与石头相似，但硬度更高。

```java
package net.flandre923.examplemod.block.custom;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;


public class RubyBlock extends Block {
    public RubyBlock() {
        super(Properties.ofFullCopy(Blocks.STONE).strength(5f));
    }
}
```

接下来，你需要注册这个新的方块，这样Minecraft才知道它的存在。这通常是通过创建一个DeferredRegister对象来完成的，这个对象会帮助你将方块注册到Minecraft的注册表中。在下面的代码中，我们创建了一个名为ModBlocks的类，这个类包含了我们的RubyBlock方块的注册代码。

```java
package net.flandre923.examplemod.block;

import net.flandre923.examplemod.ExampleMod;
import net.flandre923.examplemod.block.custom.RubyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, ExampleMod.MODID);

    public static final Supplier<Block> RUBY_BLOCK = BLOCKS.register("ruby_block", RubyBlock::new);

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
```

然后，你需要为这个方块创建一个对应的物品形式，这样玩家才能在游戏中捡起和使用它。这通常是通过创建一个BlockItem对象来完成的，这个对象将方块和物品关联起来。在下面的代码中，我们创建了一个名为ModItems的类，这个类包含了我们的RubyBlock物品的注册代码。

```java
package net.flandre923.examplemod.item;

import net.flandre923.examplemod.ExampleMod;
import net.flandre923.examplemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, ExampleMod.MODID);

    public static final Supplier<Item> RUBY = ITEMS.register("ruby",() -> new Item(new Item.Properties()));

    public static final Supplier<Item> RUBY_BLOCK = ITEMS.register("ruby_block",()->new BlockItem(ModBlocks.RUBY_BLOCK.get(), new Item.Properties()));
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
```

最后，你需要将这个物品添加到创造模式的物品栏中，这样玩家才能在游戏中找到它。

```java
package net.flandre923.examplemod.item;

import net.flandre923.examplemod.ExampleMod;
import net.flandre923.examplemod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ExampleMod.MODID);

    public static final String EXAMPLE_MOD_TAB_STRING = "creativetab.example_tab";

    public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
            .icon(()->ModItems.RUBY.get().getDefaultInstance())
            .displayItems((pParameters, pOutput) -> {
                pOutput.accept(ModItems.RUBY.get());
                pOutput.accept(ModItems.RUBY_BLOCK.get());
            })
            .build());


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
```

注册到总线

```java
public ExampleMod(IEventBus modEventBus)
{
    modEventBus.addListener(this::commonSetup);
    ModItems.register(modEventBus);
    ModBlocks.register(modEventBus);
    ModCreativeTab.register(modEventBus);
    NeoForge.EVENT_BUS.register(this);
}
```

## Item和Block的区别
在Minecraft中，Item和Block是两个不同的概念。Item代表玩家可以捡起、放入背包、使用和丢弃的物品，如工具、食物和材料。Block代表游戏中固定在地面上的物体，如石头、木头和矿石。虽然Item和Block是不同的对象，但它们之间有紧密的联系。例如，当玩家破坏一个方块时，它通常会掉落一个对应的物品形式。同样，当玩家将一个物品放入世界中时，它通常会变成一个对应的方块形式。

## Block 和Block State
Block是Minecraft中所有实体的基类，代表了一个基本的、不可再分的地形单元。每个Block都有其独特的属性和行为，如硬度、爆炸抗性、是否能被点燃等。在代码中，每个方块都由一个Block类的实例表示。当你创建一个新的方块时，你需要继承Block类并覆写一些方法来定义你的方块的行为。
例如，你可以通过覆写randomTick方法来让方块有几率在游戏世界中产生随机变化，或者通过覆写onPlace方法来定义方块被放置时的行为。

Block State指的是一个方块在其生命周期中的具体状态。由于Minecraft中的大多数方块都有多种形态，如木头的方向、石头的类型等，因此需要一个机制来存储和检索这些信息。Block State就是用来描述这些不同形态的。
每个Block都可以有多个Block State，每个Block State都有一个唯一的标识符和一组属性。属性是Block State的组成部分，它们定义了方块的外观和行为。例如，一个木台阶可以有六个不同的方向，每个方向都是一个不同的Block State。
在代码中，Block State由BlockState类表示，你可以通过这个类的实例来获取和设置方块的状态。例如，你可以通过调用getBlockState().with(Properties.HORIZONTAL_FACING, Direction.NORTH)来设置一个方块面向北方。

## 方块模型和材质
建立这样的目录结构

```
resources
    ├───assets
    │   └───examplemod
    │       ├───blockstates
    │       ├───lang
    │       ├───models
    │       │   ├───block
    │       │   └───item
    │       └───textures
    │           ├───block
    │           └───item
```

这段代码是JSON格式的数据，用于告诉Minecraft游戏，当渲染名为ruby_block的方块时，应该使用哪个模型文件。在这里，”model”: “examplemod:block/ruby_block_model”指定了模型的路径，examplemod是模组的名称，block/ruby_block_model是模型文件相对于模组资源文件夹的路径。这个模型文件将定义方块的外观和结构。

```json
{
  "variants": {
    "": { "model": "examplemod:block/ruby_block_model" }
  }
}

```


这段代码同样使用JSON格式，它定义了一个模型文件的内容。”parent”: “block/cube_all”指定了这个模型是基于Minecraft内置的cube_all模型，这意味着它是一个简单的立方体。”textures”: {“all”: “examplemod:block/ruby_block”}定义了立方体六个面使用的纹理。这里，”all”表示六个面使用同一个纹理，纹理的路径是examplemod:block/ruby_block。
```json

{
  "parent": "block/cube_all",
  "textures": {
    "all": "examplemod:block/ruby_block"
  }
}
```

这段代码用于定义物品的模型。在Minecraft中，物品和方块通常使用相同的模型。”parent”: “examplemod:block/ruby_block”表明物品模型将使用与ruby_block方块相同的模型。这意味着，当你在创造模式物品栏或者手中拿着这个物品时，它会看起来和它在世界中作为方块时一样。
```json
{
  "parent": "examplemod:block/ruby_block"
}

```

ruby_block.png是一个纹理文件，通常是一个图片文件，它包含了方块的表面图案。在Minecraft中，纹理文件被用于给方块和物品提供详细的外观。这个文件应该放在模组的资源文件夹中对应于纹理路径examplemod:block/ruby_block的位置。纹理文件通常包含多个Minecraft方块的图案，每个方块的一部分通常被安排在一个大图中，通过纹理坐标来指定使用大图中的哪一部分。

ruby_block.png

下面我对相关的类做一个简单的介绍详细的内容可以自己查看

## Block类
Block类是创建和定义新方块的地方

## Block Behaviour Properties 类
BlockBehaviourProperties类用于设置方块的行为属性

## Blocks 类
Blocks类则是一个方便的仓库，用于访问游戏中的所有方块。

## 简化我们注册Block的步骤
简化在Minecraft模组中注册方块和对应物品的过程。通过将这些步骤封装在方法中，更容易地添加新的方块，并确保每个方块都有一个对应的物品形式。

```java
public static final Supplier<Block> RUBY_BLOCK = registerBlock("ruby_block", RubyBlock::new);

 public static Supplier<Block> registerBlock(String name,Supplier<Block> block){
        Supplier<Block> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name,toReturn);
        return toReturn;
    }
    public static void registerBlockItem(String name, Supplier<Block> block){
        registerBlockItem(name, block, new Item.Properties());
    }
    public static void registerBlockItem(String name, Supplier<Block> block, Item.Properties properties){
        ITEMS.register(name, () -> new BlockItem(block.get(), properties));
    }

```

现在创造模式物品栏中可以直接添加方块，以为方块实现了asitem这个接口

```java
public static final Supplier<CreativeModeTab> EXAMPLE_TAB  = CREATIVE_MODE_TABS.register("example_tab",() -> CreativeModeTab.builder()
        .withTabsBefore(CreativeModeTabs.COMBAT)
        .title(Component.translatable(EXAMPLE_MOD_TAB_STRING))
        .icon(()->ModItems.RUBY.get().getDefaultInstance())
        .displayItems((pParameters, pOutput) -> {
            pOutput.accept(ModItems.RUBY.get());
            pOutput.accept(ModBlocks.RUBY_BLOCK.get());
        })
        .build());

```
这样做简化Minecraft模组中方块和物品的注册过程。减少了重复代码.