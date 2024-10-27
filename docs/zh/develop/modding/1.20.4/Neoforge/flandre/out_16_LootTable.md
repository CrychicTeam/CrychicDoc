---
title: 16 战利品表
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 16 战利品表 相关教程
image: ./covers/f3ffb98eaf48e06e301e3d134b26ad7cf2d0c5c0.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 战利品表

战利品表是指你破坏了方块或者打死了实体的时候掉落的东西，也包含了奖励箱的内容。

他也是通过json文件描述的。这里我们简单的介绍简单的方块破坏后掉落方块本身，更加复杂的内容和复杂的掉落物可以看原版的json

对于不知道的字段的意思可以去看[wiki](https://minecraft.fandom.com/zh/wiki/%E6%A0%87%E7%AD%BE)

创建如下的目录

```
   ├─assets
    │  └─examplemod
    └─data
        ├─examplemod
        │  ├─advancements
        │  ├─loot_tables
        │  │  └─blocks

```
对于方块的掉落物的战利品表应该放在blocks下面，其他的可以去看原版的结构或者wiki

这里添加一个ruby_ore.json

```json

{
  "type": "minecraft:block",
  "pools": [
    {
      "bonus_rolls": 0.0,
      "conditions": [
        {
          "condition": "minecraft:survives_explosion"
        }
      ],
      "entries": [
        {
          "type": "minecraft:item",
          "name": "examplemod:ruby_ore"
        }
      ],
      "rolls": 1.0
    }
  ],
  "random_sequence": "examplemod:blocks/ruby_ore"
}
```

- "type: 这个字段指定了这个战利品表的类型。在这个例子中，类型是minecraft:block，这意味着这是一个方块破坏时的战利品表。
- pools: 这个字段包含了一个或多个战利品池，每个池都可以定义不同的掉落物品和条件。在这个例子中，只有一个战利品池。
    - bonus_rolls: 这个字段定义了额外的战利品掉落次数。在这个例子中，它被设置为0.0，意味着没有额外的掉落次数。
    - "conditions: 这个字段定义了必须满足的条件，才能从这个战利品池中掉落物品。在这个例子中，只有一个条件
        - condition: 这个字段定义了具体的条件。在这个例子中，条件是
        - minecraft:survives_explosion，这意味着只有当方块在爆炸中幸存下来时，才会掉落物品.
    - entries: 这个字段定义了战利品池中的条目，也就是可以掉落的物品。在这个例子中，只有一个条目：
        - type: 这个字段指定了条目的类型。在这个例子中，类型是minecraft:item，意味着这是一个物品。
        - name: 这个字段定义了条目的名称。在这个例子中，名称是examplemod:ruby_ore，这意味着当Ruby矿石被破坏时，会掉落一个Ruby矿石物品。
    - rolls: 这个字段定义了从战利品池中掉落的次数。在这个例子中，它被设置为1.0，意味着每次破坏Ruby矿石时，都会掉落一个Ruby矿石物品。

更加详细的解释内容见[wiki](https://minecraft.fandom.com/zh/wiki/%E6%A0%87%E7%AD%BE)

## 数据生成

你可以尝试自己手写这样的内容，不过也是比较麻烦的，很可能会写错，datagenerator提供了一种生成的方式。

我们需要创建一个自己的LootTableProvider，和自己的方块的BlockLootSubProvider

对于LootTableProvider,这是一个自定义的战利品表提供者，我们需要给他提供子提供者列表
```java

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(PackOutput pOutput, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> pSubProviders) {
        super(pOutput, pRequiredTables, pSubProviders);
    }
}

```

对于ModBlockLootProvider。

```java
public class ModBlockLootProvider extends BlockLootSubProvider {

    public static final Set<Block> BLOCK = Set.of(
      ModBlocks.RUBY_ORE.get()
    );

    public ModBlockLootProvider() {
        super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.RUBY_ORE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BLOCK;
    }
}
```

this.dropSelf(ModBlocks.RUBY_ORE.get())：这行代码调用dropSelf方法，指定当Ruby矿石方块被破坏时，它应该掉落自身。

然而generate的调用会检查getKnownBlocks方法返回的方块是否都有了对应的掉落物表，如果没有就会报错，所以这里我们要重写这个getKnownBlocks方法。返回我们要求生成战利品表的方块。

super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());方法中的第一个参数是一个空的集合，该集合是指免疫爆炸的方块列表，这里传入的是一个空。第二个参数照这样写即可。

使用

```java

        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModLootTableProvider>)pOutput -> new ModLootTableProvider(pOutput, Collections.emptySet(),
                        List.of(
                                new LootTableProvider.SubProviderEntry(ModBlockLootProvider::new, LootContextParamSets.BLOCK)
                        ))
        );
```
构造函数的第二个参数是指那些需要生成战利品表的特色的名字，或者填空。

之后调用rundata的task即可。

## 原版的类

原版的LootTableProvider，包含了方块破坏掉落，实体击杀掉落，钓鱼掉落，宝箱的内容等等。

```java
public class VanillaLootTableProvider {
    public static LootTableProvider create(PackOutput pOutput) {
        return new LootTableProvider(
            pOutput,
            BuiltInLootTables.all(),
            List.of(
                new LootTableProvider.SubProviderEntry(VanillaFishingLoot::new, LootContextParamSets.FISHING),
                new LootTableProvider.SubProviderEntry(VanillaChestLoot::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(VanillaEntityLoot::new, LootContextParamSets.ENTITY),
                new LootTableProvider.SubProviderEntry(VanillaBlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(VanillaPiglinBarterLoot::new, LootContextParamSets.PIGLIN_BARTER),
                new LootTableProvider.SubProviderEntry(VanillaGiftLoot::new, LootContextParamSets.GIFT),
                new LootTableProvider.SubProviderEntry(VanillaArchaeologyLoot::new, LootContextParamSets.ARCHAEOLOGY)
            )
        );
    }
}

```

原版的VanillaBlockLoot

这包含了原版的方块的破坏掉落的战利品表。

其他的生成请阅读原版的内容

## 拓展阅读

[neoforge 文档](https://docs.neoforged.net/docs/datagen/server/loottables)