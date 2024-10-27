---
title: 15 合成表
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 15 合成表 相关教程
image: ./covers/6c533fcb8a32acb5574e0733cf9e436177ec55db.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 合成表配方

你可以在wiki中查找有关数据包制作的内容，也就是json的内容。我们这里简单的介绍几种，你可以查找原版的json学习和通过wiki学习。

创建如下的目录结构，数据包的内容在data文件夹下。而不是assets下。

```java

resources
    ├─assets
    ├─data
    │  └─examplemod
    │      ├─advancements
    │      │  └─recipes
    │      │      └─misc
    │      └─recipes

```
其中的data/examplemod就是你自己的数据包了。

recipes（配方）:

这个目录用于存放所有的合成、烧炼、酿造等配方。

advancements（进度）:

这个目录用于存放自定义的进度，进度可以看作是游戏内的成就或者任务系统。

```json
{
  "type": "minecraft:crafting_shaped",
  "category": "misc",
  "key": {
    "a": {
      "item": "examplemod:ruby"
    },
    "b": {
      "item": "minecraft:stick"
    }
  },
  "pattern": [
    "aaa",
    " b ",
    " b "
  ],
  "result": {
    "item": "examplemod:ruby_pickaxe"
  }
}
```
type: "minecraft:crafting_shaped" 表示这是一个有序合成配方。

category: "misc" 指定了配方类别，通常设置为 "misc"。

key: 包含了合成配方中使用的符号和对应的物品。在这个例子中，"a" 代表了 examplemod:ruby，"b" 代表了 minecraft:stick。

pattern: 表示3x3合成格中的物品布局。"aaa" 表示第一行三个位置都是 examplemod:ruby，" b " 表示第二行中间是 minecraft:stick，两边是空格。

result: 指定了合成后得到的物品。这里合成的结果是 examplemod:ruby_pickaxe。

[wiki](https://zh.minecraft.wiki/w/%E9%85%8D%E6%96%B9)

无序合成

```json
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "ingredients": [
    {
      "item": "minecraft:iron_ingot"
    },
    {
      "item": "minecraft:iron_ingot"
    },
    {
      "item": "minecraft:iron_ingot"
    }
  ],
  "result": {
    "item": "examplemod:magic_ingot"
  }
}
```
ingredients: 列出了合成配方所需的物品。在这个例子中，需要三个 minecraft:iron_ingot。

熔炉和其他

以熔炉为例,其他详见wiki

```json
{
  "type": "minecraft:smelting",
  "category": "misc",
  "cookingtime": 100,
  "experience": 0.3,
  "ingredient": {
    "item": "examplemod:ruby_ore"
  },
  "result": "examplemod:ruby"
}
```

type: "minecraft:smelting" 表示这是一个熔炉配方。

category: "misc" 同上。

cookingtime: 烧炼所需的时间，以刻为单位。100刻等于5秒。

experience: 烧炼此物品后玩家获得的经验值。

ingredient: 需要烧炼的物品。这里需要烧炼的是 examplemod:ruby_ore。

result: 烧炼后得到的物品。这里烧炼的结果是 examplemod:ruby。

## datagenerator 生成合成表

你可以自己手写合成表，但是难免会出现错误，我们还可以使用之前讲解的数据生成的内容。

```java


public class ModRecipeProvider extends RecipeProvider {

    //https://docs.neoforged.net/docs/datagen/server/recipes
    public ModRecipeProvider(PackOutput pPackOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(pPackOutput, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        // 有序合成
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RUBY_PICKAXE.get())
                .pattern("aaa")
                .pattern(" b ")
                .pattern(" b ")
                .define('a',ModItems.RUBY.get())
                .define('b', Items.STICK)
                .unlockedBy("has_ruby",has(ModItems.RUBY.get()))
                .save(pRecipeOutput);
        // 无序合成
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC,ModItems.MAGIC_INGOT.get())
                .requires(Items.IRON_INGOT,3)
                .unlockedBy("has_iron_ingot",has(Items.IRON_INGOT))
                .save(pRecipeOutput);
        // 冶炼
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModBlocks.RUBY_ORE.get()),RecipeCategory.MISC,ModItems.RUBY.get(),0.3f,100)
                .unlockedBy("has_ruby_ore",has(ModBlocks.RUBY_ORE.get()))
                .save(pRecipeOutput);
        // 切割石头
//        SingleItemRecipeBuilder.stonecutting()
    }
}
```

buildRecipes这个方法在数据生成过程中被调用，用于构建和保存配方。RecipeOutput参数用于输出生成的配方数据。

ShapedRecipeBuilder.shaped创建一个有序合成配方

unlockedBy("has_ruby",has(ModItems.RUBY.get()))配方有一个解锁条件，要求玩家拥有ModItems.RUBY。《在你获得ruby时候右上角提示》

ShapelessRecipeBuilder。shapeless创建一个无序合成配方，用于制作MAGIC_INGOT。配方需要3个IRON_INGOT。配方有一个解锁条件，要求玩家拥有IRON_INGOT。

SimpleCookingRecipeBuilder.smelting创建一个冶炼配方，用于将RUBY_ORE冶炼成RUBY。冶炼时间为100刻，提供0.3的经验值。配方有一个解锁条件，要求玩家拥有RUBY_ORE。

gleItemRecipeBuilder.stonecutting()它将使用SingleItemRecipeBuilder来创建一个切割配方。

添加到GatherDataEvent事件中去：


```java

@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        ExistingFileHelper efh = event.getExistingFileHelper();
        var lp = event.getLookupProvider();
        // recipe
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModRecipeProvider>) pOutput -> new ModRecipeProvider(pOutput,lp)
        );
    }
}

```

rundata即可生成合成表。其他的合成表可以参考原版的实现和原版的类。

## 拓展阅读

[wiki](https://zh.minecraft.wiki/w/%E9%85%8D%E6%96%B9)

[boson教程](https://boson.v2mcdev.com/datapack/recipes.html)

[Neoforge document](https://docs.neoforged.net/docs/datagen/server/recipes)