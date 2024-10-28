---
title: 42 矿物生成
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 42 矿物生成 相关教程
image: ./covers/8aeac5a0016d893c1a62e2531d794fa19a47fa61.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 矿物生成

这次我们来说矿物生成，现在的世界生成基本上已经使用json来驱动了，所以你甚至不需要编写代码，仅仅通过json就可以实现了，不过编写这样的json是一件很恐怖的事情，我们还是通过数据生成的方式来生成json的内容，这次的内容比较多。我们还是先来介绍涉及到的类，以及原版的怎么操作的。然后再来讲我们应该怎么做。

我们先来说一个概念就是资源键（ResourceKey），他类似于resourceLoaction这个类，不过不同的是这个key是用于在游戏中唯一标识矿石特性的，用于注册ORE_Freature。当然OreFeature矿石特征是ConfiguredFeature的一种特定类型，ConfiguredFeature是指可以配置的特征，我的世界很多的世界生成内容都依靠这个配置特征，需要用到resourceKey注册。

然后我们来说矿石特性OreFeature，具体来说他指出了矿石在世界生成的时候去替换那些方块。这里提一下我的世界的世界生成是先生成世界，然后在用结构矿物等替换到原有的方块。所以这里定义的是矿物方块要替换到那些方块，对于铁矿了来说，要替换的就是石头之类的方块。此外OreFeature还需要指出这一堆生成的数量。这两个数据是通过OreConfiguration类来配置，这个类的构造也很简单，就是通过该类提供的target传入对应的resourceKey以及targetBlock和size。即上文我们提到的。

对于原版的OreFeature的配置在OreFeatures这个类下面。

好了了还需要说的一点是RuleTest，点开RuleTest该类，你就可以发现该类有几个子类，分别是BlockStateMatchTest,TagMatchTest。。。。这几个类的作用是写你的目标检查方块的，不过你也可以用在其他地方，目前来说就作用是匹配方块是否和你指定的方块一致，类型有很多，比如TagMatChTest就是匹配方块的标签的，例如我要替换掉石头的，那么写入石头的标签即可。其他同理。

更加详细的字段讲解可以看看wiki
https://minecraft.fandom.com/zh/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%B8%96%E7%95%8C%E7%94%9F%E6%88%90/configured_feature

好了来看看我们的写的这个OreFeature吧

```java
public class ModOreFeatures {
    // 创建OreFeature对应的ResourceKey
    // 
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_RUBY = createKey("ruby");

    //BootstapContext 是我们datagen的上下文，等会我们使用数据生成的时候说。
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> pContext) {
        //  创建对应的tag，如果有多个就创建多个
        RuleTest stoneOreReplaceRuleTest = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepSlateOreReplaceRuleTest = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // 创建一个list
        List<OreConfiguration.TargetBlockState> list = List.of(
                OreConfiguration.target(stoneOreReplaceRuleTest, ModBlocks.RUBY_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepSlateOreReplaceRuleTest, ModBlocks.RUBY_BLOCK.get().defaultBlockState())
        );
        // 注册对应orefeature，使用listOreConfiguration，9 上文提到的size
        FeatureUtils.register(pContext, ORE_RUBY, Feature.ORE, new OreConfiguration(list, 9));

    }
    // 创建ResourceKey的方法
    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String pName) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(ExampleMod.MODID,pName));
    }

}
```

好了现在我们来看placements，这个是用于生成矿石的，叫做放置特征PlacedFeature。他的注册同样需要到ResourceKey资源键。我们也需要创建对应的资源键。

PlacedFeature是指的你的配置的ConfigurationFeature在世界中如何生成，PlacedFeature的配置是通过PlacementModifier来设置的，你可以在PlacedFeature中设置多个PlacementModifier。

PlacementModifier可以设置高度的区间范围，生物群系，生成的方式，等等。

我们来看看这个，我们矿物的生成

```java

public class ModOrePlacements {
    // 创建对应的placement的资源键
    public static final ResourceKey<PlacedFeature> ORE_RUBY = createKey("ore_ruby");

    // BootstapContext数据生成时候传入上下文对象，我们数据生成时候再说
    public static void bootstrap(BootstapContext<PlacedFeature> pContext) {
        // 我们通过上下文获得我们的设置的个矿物的configurationfeature
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = pContext.lookup(Registries.CONFIGURED_FEATURE);
        // 通过上下文获得的是一个HolderGetter，这个HolderGetter是对ConfiguredFeature主要用于处理null的问题，感兴趣可以去了解下，不过这里我们写了对应的ConfigurationFeature所有获得不会是空，就可以拿到对应的Holder
        Holder<ConfiguredFeature<?, ?>> oreRubyHolder = holdergetter.getOrThrow(ModOreFeatures.ORE_RUBY);
        // 让后我们就可以使用PlacementUtils的register方法，注册我们的PlacedFeature
        // 第一个参数就是上下文，第二个参数是对应ConfiguredFeature的holder，第三个参数是list的PlacementModifier，说明你的PlacedFeature的设置内容，
        // 这里commonOrePlacement是指设置了一系列的PlacementModifier，对应的是常规的矿物的生成，和原版的一样。
        // 对于PlacementModifier有很多的子类，你可以去继承关系中看
        // 这里我们用的一个是HeightRangePlacement，是指设置生成的最低高度和最高的高度。
        // 对于commonOrePlacement方法中用的CountPlacement则是设置一个数值
        // HeightRangePlacement的uniform方法是指从低到高平均生成，还有一个三角的，是指中间生成多，两边生成少。
        PlacementUtils.register(
                pContext, ORE_RUBY, oreRubyHolder, commonOrePlacement(16, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(72)))
        );
    }



    private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }
    // 我们没有使用这个，对应了原版的稀有矿物生成
    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }

    public static ResourceKey<PlacedFeature> createKey(String pKey) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(ExampleMod.MODID,pKey));
    }

}

```

我们创建了矿石特性的OreFeature和矿石的PlacedFeature，不过这些Feature并没有添加到生物群系的生成中，现在我们需要将它们添加到生物群系中去了。

我们依旧需要一个ResourceKey不过这次的ResourceKey对应的内容是BiomeModifier这个内容，我们注册的是BiomeModifier。

BiomeModifier表示了对生物群系的修改，我们使用BiomeModifiers的AddFeaturesBiomeModifier子类

```java
public class ModBiomeModifiers {
    // 注册的key
    public static final ResourceKey<BiomeModifier> ADD_RUBY_ORE = registerKey("add_ruby_ore");
    // BootstapContext 数据生成的上下文
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        // 通过上下文获得PLACED_FEATURE的注册HolderGetter
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        // 通过上下文获得BIOME的HolderGetter
        var biomes = context.lookup(Registries.BIOME);
        // 生成json文件，第一个参数是key，第二个参数是BiomeModifiers，
        // 我们使用了子类AddFeaturesBiomeModifier，是指添加feature给biome
        // 第一个参数是holderset的biome ，这里是否是主世界的生物群系。即返回了主世界的生物群系
        // 第二个holdlerSet是指所有的feature，我们通过placedFeatures获得
        // 丢三个参数要求给出在世界生成的什么阶段加你的feature，我们这里是地下矿物生成的时候，你可以到该类下面看看，这是个枚举，
        context.register(ADD_RUBY_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModOrePlacements.ORE_RUBY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(ExampleMod.MODID, name));
    }

}

```

下面我们来说怎么使用写的东西生成对应的json。我们需要写这样的类，然后使用RegistrySetBuilder构建一个builder，通过add方法将我们写的那几个bootstrap的方法引用传入。

其中BIOME_MODIFIERS比较特殊的是registries.keys是注册的地方。

```java

public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModOreFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModOrePlacements::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER,Set.of(ExampleMod.MODID));
    }
}

```

和我们之前的使用方法一致，没什么区别。就不多家介绍了。

```java

@Mod.EventBusSubscriber(modid = ExampleMod.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        ExistingFileHelper efh = event.getExistingFileHelper();
        var lp = event.getLookupProvider();
        //world  gen
        event.getGenerator().addProvider(event.includeServer(), (DataProvider.Factory<ModWorldGen>) pOutput -> new ModWorldGen(pOutput,lp));
    }
}

```

好了，你运行rundata，应该可以获得下面的几个json目录了。
../src/generated/resources/data/examplemod/neoforge/biome_modifier/add_ruby_ore.json
../src/generated/resources/data/examplemod/worldgen/configured_feature/ruby.json
../src/generated/resources/data/examplemod/worldgen/placed_feature/ore_ruby.json

之后创建个新的世界看看矿物是否生成了吧