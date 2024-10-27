---
title: 17 Tag
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 17 Tag 相关教程
image: ./covers/db5375b0efcd53976a3fe5d47fbd8f3b3216e0f8.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

---
# tag表

当你现在使用稿子挖掘我们添加的方块时候，你会发现无论你设置的硬度是多少挖掘速度都是一样的。会有掉落的物品，这个物品是你在上一节的战利品表中设置的。

挖掘速度一样这是不正常的。我们需要解决这个问题，与此相关的就是tag

我们需要在指定的目录下的指定的json文件中将我们的方块添加到其中去。这样我的世界才能正确认识的我们添加的方块对于的挖掘工具和挖掘所需要的等级，例如铁或者石头。

创建如下的目录结构

```

└─data
    ├─examplemod
    └─minecraft
        └─tags
            └─blocks
                └─mineable

```

如果我们要求我们的方块可以被石头及其以上等级工具挖掘，那么我们需要在blocks下创建一个needs_stone_tool.json文件

写入如下的内容：

```json
{
  "values": [
    "examplemod:ruby_ore"
  ]
}
```
表示将我们的ruby_ore方块添加到这个tag下。

并且我们还需要我们的方块是应该被稿子挖掘的。

那么我们还需要在minebale文件夹下创建pickaxe.json文件，内容如下

```json

{
  "values": [
    "examplemod:ruby_ore"
  ]
}
```

表示我们的方块应该被稿子挖掘。

对于其他的等级和挖掘的工具也是同理，自行查看原版的内容，或者查看[wiki](https://minecraft.fandom.com/zh/wiki/%E6%A0%87%E7%AD%BE)

## 数据生成

当然这里的json我们同样可以通过datagenerator生成，都是常规的操作这里简单介绍一下。

```java

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(ModBlocks.RUBY_ORE.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.RUBY_ORE.get());
    }
}

```

protected void addTags(HolderLookup.Provider pProvider)：这是一个受保护的方法，用于添加方块标签。在这个方法中，它为Ruby矿石方块添加了两个标签。

this.tag(BlockTags.NEEDS_STONE_TOOL).add(ModBlocks.RUBY_ORE.get());：这行代码为Ruby矿石方块添加了一个标签，名为BlockTags.NEEDS_STONE_TOOL。这个标签表示Ruby矿石方块需要石质工具或更好的工具来挖掘。

this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.RUBY_ORE.get());：这行代码为Ruby矿石方块添加了一个标签，名为BlockTags.MINEABLE_WITH_PICKAXE。这个标签表示Ruby矿石方块可以用镐挖掘。

加入到GatherDataEvent 事件中去：

```java

        // tag
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModBlockTagProvider>) pOutput -> new ModBlockTagProvider(pOutput,lp,ExampleMod.MODID,efh)
        );


```
## 原版的几个tag的类

- GameEventTagsProvider (net.minecraft.data.tags) 游戏事件（game events）的标签
- ItemTagsProvider (net.minecraft.data.tags) 处理物品（items）的标签
- EntityTypeTagsProvider (net.minecraft.data.tags) 处理实体类型（entity types）的标签。
- VanillaBlockTagsProvider (net.minecraft.data.tags) 处理原版Minecraft（vanilla）中的方块标签。
- FluidTagsProvider (net.minecraft.data.tags) 处理流体（fluids）的标签。
- BlockTagsProvider (net.neoforged.neoforge.common.data) 

## 拓展阅读

wiki:
https://minecraft.fandom.com/zh/wiki/%E6%A0%87%E7%AD%BE

neoforge document:
https://docs.neoforged.net/docs/datagen/server/tags







