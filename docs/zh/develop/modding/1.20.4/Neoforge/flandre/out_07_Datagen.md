---
title: 07 数据生成
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 07 数据生成 相关教程
image: ./covers/9e6ec36a9a8a2f91e68c5af12c055f1248f63288.jpg
category: Minecraft1_20_4_NeoForge_Tutorial
draft: false
---
# Data Generators

数据生成器是一种以编程方式生成模组资源和数据的方法。它允许在代码中定义这些文件的内容并自动生成它们，而无需担心细节。


## 语言文件

```java

import net.flandre923.examplemod.block.ModBlocks;
import net.flandre923.examplemod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {


    public ModLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(ModItems.RUBY.get(),"Ruby");
        this.add(ModBlocks.RUBY_BLOCK.get(),"Ruby Block");
        this.add("object.examplemod.example_object","Example Object");
    }
}

```
LanguageProvider 是一个类，用于生成和修改Minecraft游戏中的语言文件。语言文件包含了游戏中所有的文本内容，如物品名称、方块名称、提示信息等。通过扩展 LanguageProvider 类，开发者可以自定义自己的语言文件内容。

addTranslations 是 LanguageProvider 类中的一个方法。在自定义的 LanguageProvider 子类中，你需要重写（override）这个方法来添加你想要的语言翻译。这个方法在数据生成过程中被调用，用于生成语言文件。

这行代码的作用是将游戏内的一个物品（这里是指 ModItems.RUBY）与一个字符串（“Ruby”）相关联。在生成的语言文件中，ModItems.RUBY 将会被翻译为 “Ruby”。这样，当玩家在游戏中看到这个物品时，它的名称将会显示为 “Ruby”。

与上一条类似，这行代码将一个方块（ModBlocks.RUBY_BLOCK）与字符串 “Ruby Block” 相关联。在游戏内，这个方块将会显示为 “Ruby Block”。

这行代码为语言文件添加了一个自定义的翻译。"object.examplemod.example_object" 是一个键（key），它可以在游戏的其它地方被引用来显示对应的文本。“Example Object” 是这个键对应的值（value），也就是实际显示的文本。

## 物品模型

```java


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ModItems.RUBY.get());
        this.magicIngotModel(getResourceLocation(ModItems.MAGIC_INGOT.get()));
    }


    public ResourceLocation getResourceLocation(Item item){
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    }
    public void magicIngotModel(ResourceLocation item){
        this.getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0",new ResourceLocation("item/iron_ingot"))
                .override().predicate(new ResourceLocation(ExampleMod.MODID,"size"),16).model(new ModelFile.UncheckedModelFile("item/gold_ingot")).end();
    }
}

```
ItemModelProvider 是一个类，它用于生成物品的模型文件。

registerModels 是 ItemModelProvider 类中的一个方法。在自定义的 ItemModelProvider 子类中，你需要重写这个方法来注册你想要创建的物品模型。这个方法在数据生成过程中被调用，用于生成物品的模型文件。

这行代码调用了 basicItem 方法，它是一个便利方法，用于为 ModItems.RUBY 创建一个基本的物品模型。这个方法会生成一个默认的物品模型，通常是一个简单的方块或物品形状。


这个方法用于创建一个自定义的物品模型。它接受一个 ResourceLocation 参数，这是物品的唯一标识符。
在这个方法中，首先调用 getBuilder(item.toString()) 来获取一个模型构建器，然后设置模型的父级为 "item/generated"，这是一个Minecraft内置的通用物品模型。
接着，使用 .texture("layer0",new ResourceLocation("item/iron_ingot")) 为模型设置纹理。这里使用了铁锭的纹理。
最后，使用 .override().predicate(new ResourceLocation(ExampleMod.MODID,"size"),16).model(new ModelFile.UncheckedModelFile("item/gold_ingot")).end() 添加了一个条件覆盖。这意味着当物品具有一个名为 “size” 的属性，并且该属性的值大于等于16时，将使用金锭的模型来代替当前的模型。
总的来说，这段代码的作用是为 ModItems.RUBY 创建一个基本的物品模型，并为 ModItems.MAGIC_INGOT 创建一个自定义的模型，该模型在特定条件下会使用金锭的外观。



## 方块状态
 


 ```java



public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockWithItem(ModBlocks.RUBY_BLOCK.get(),cubeAll(ModBlocks.RUBY_BLOCK.get()));
        this.propertyBlock(ModBlocks.LAMP_BLOCK.get());
    }

    public void propertyBlock(Block block){
        var block_off = models().cubeAll("lamp_off",new ResourceLocation(ExampleMod.MODID,ModelProvider.BLOCK_FOLDER+"/"+"zircon_lamp_off"));
        var block_on = models().cubeAll("lamp_on",new ResourceLocation(ExampleMod.MODID, ModelProvider.BLOCK_FOLDER+"/"+"zircon_lamp_on"));
        getVariantBuilder(block).partialState().with(LampBlock.LIT,true)
                .modelForState().modelFile(block_on).addModel()
                .partialState().with(LampBlock.LIT,false)
                .modelForState().modelFile(block_off).addModel();
        simpleBlockItem(block,block_off);
    }
}
 ```

BlockStateProvider 是一个数据生成器，它用于创建方块的状态和模型。这些状态和模型定义了方块在游戏中的外观和行为。

registerStatesAndModels 是 BlockStateProvider 类中的一个方法。在自定义的 BlockStateProvider 子类中，你需要重写这个方法来注册你想要创建的方块状态和模型。这个方法在数据生成过程中被调用，用于生成方块的状态文件和模型文件。

这行代码调用了 simpleBlockWithItem 方法，它用于为 ModBlocks.RUBY_BLOCK 创建一个简单的方块状态和对应的物品模型。cubeAll 方法创建了一个简单的立方体模型，它将使用与方块相同的纹理。

public void propertyBlock(Block block){

这个方法用于创建具有不同状态的方块模型。它首先创建了两个模型：block_off 和 block_on，分别代表方块关闭和开启时的外观。
然后，使用 getVariantBuilder(block) 获取方块变体的构建器，并使用 partialState() 方法来指定方块的特定状态。这里使用了 LampBlock.LIT 属性来区分方块是开启还是关闭。
对于每个状态，使用 modelForState().modelFile(...).addModel() 来设置相应的模型文件。
最后，调用 simpleBlockItem(block,block_off) 来创建与方块关联的物品模型。这个物品模型将使用关闭状态的方块模型。

这段代码的作用是为 ModBlocks.RUBY_BLOCK 创建一个简单的方块状态和模型，并为 ModBlocks.LAMP_BLOCK 创建一个具有两种不同状态的模型，分别代表灯方块开启和关闭时的外观。

## 如何使用他们

```java

@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        ExistingFileHelper efh = event.getExistingFileHelper();

        // 语言文件
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModLanguageProvider>) pOutput -> new ModLanguageProvider(pOutput,ExampleMod.MODID,"en_us")
        );
        // 物品模型
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModItemModelProvider>) pOutput -> new ModItemModelProvider(pOutput,ExampleMod.MODID,efh)
        );
        // 方块state
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModBlockStateProvider>) pOutput -> new ModBlockStateProvider(pOutput,ExampleMod.MODID,efh)
        );

    }
}

```

## GatherDataEvent

GatherDataEvent 是一个事件，它在数据生成过程中被触发。这个事件允许开发者注册自己的数据提供者（Data Providers），这些提供者负责生成具体的数据文件。

ExistingFileHelper

ExistingFileHelper 是一个辅助类，用于检查文件是否存在，以避免覆盖现有文件。在数据生成过程中，使用 ExistingFileHelper 可以确保数据的完整性。


getGenerator()

getGenerator() 方法是 GatherDataEvent 的一部分，它返回一个 DataGenerator 实例，这个实例用于管理数据提供者和数据生成过程。

event.includeClient()告诉生成器仅当客户端资源生成时候调用

Poutput

Poutput 是 PackOutput 类的一个实例，它代表数据生成器的输出目标。这个类负责管理数据生成器的输出目录和文件结构。

## 更多详细内容

https://docs.neoforged.net/docs/datagen/