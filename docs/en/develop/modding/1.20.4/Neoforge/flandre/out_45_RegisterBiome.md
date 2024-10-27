---
title: 45 添加生物群系
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 45 添加生物群系 相关教程
image: ./covers/419afe7190a47e6691de1002662d0341ec325883.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 添加生物群系

这次我们使用上次添加的terrablender lib来给主世界添加生物群系，为什么不直接添加呢，因为我没找到方法，而且添加也比较麻烦，可能需要mixin，所以我们直接使用这个lib来给主世界添加生物群系，不过如果你是给自己的维度添加生物群系倒不是怎么麻烦了。好了然我们开始把。

对于我的世界中的生物群系同样是通过json来配置的，你也可以仅写json完成，不过既然我们能写代码还是通过代码来完成把。

首先生物群系是配置了一系列的feature，就是我们之前的写的矿物生成的那样的feature，不仅仅是矿物还包含了地下洞穴，各种矿物，以及该生物群系的中的怪物生成。这两个分别对应了，biomegenerationsettings和MobSpawnSettings这两个类，这两个类就是配置生成的内容和怪物的生成，并且这两个都是通过builder来构建的。

然后我们通过Biome.BiomeBuilder()这个构建大的方法，来获得对应的Biome实例，其中的BiomeBuilder指定我们的biomegenerationsettings和MobSpawnSettings这两个内容，最后和之前一样通过数据生成得到方式获得json文件。

不过这里要说大的一点就是我们添加了各种的Placeddfeature，你之前也写过placedfeature所以你应该明白这并不会改变生物群系的一些地表的方块什么的。这更像是添加新的内容，例如就是沙漠的地表是沙子，而平原的地表是草方块，这并不是通过placedfeature配置的。这也是我们使用terrablender的理由，据我找到的内容来看，neoforge没有提供直接往主世界中塞生物群系的方法，而对于地表方块的生成的内容则是放在了一个package net.minecraft.data.worldgen包下的SurfaceRuleData类中，你想添加只能修改这个类。

而对于生成你添加的生物群系则是在维度里面，你也需要通过mixin的方法去添加，不过我们使用了terrablender所以就不需要考虑这种事情了。

大概了解之后我们来看代码把。首先是添加生物群系得到代码。

```java
// 原版的生物群系的配置位于OverworldBiomes类下。
public class ExampleOverworldBiomes{
    // 一个辅助的函数用于根据温度判断段生物群系的天空的颜色
    protected static int calculateSkyColor(float pTemperature) {
        float $$1 = pTemperature / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }
    // 一个构造方法 用于返回一个biome实例
    private static Biome ExampleOverworldBiomes(
            boolean pHasPercipitation,// 是否有降水
            float pTemperature,// 温度
            float pDownfall, // 降水量
            MobSpawnSettings.Builder pMobSpawnSettings,// 生物群系生物生成设置
            BiomeGenerationSettings.Builder pGenerationSettings,// 生物群系生成设置
            @Nullable Music pBackgroundMusic // 生物群系背景音乐
    ) {
        return ExampleOverworldBiomes(pHasPercipitation, pTemperature, pDownfall, 4159204, 329011, null, null, pMobSpawnSettings, pGenerationSettings, pBackgroundMusic);
    }
    // 另一个构造方法 同样返回bioome 能配置的更多
    private static Biome ExampleOverworldBiomes(
            boolean pHasPrecipitation, /
            float pTemperature,// 温度
            float pDownfall, 
            int pWaterColor, // 水的颜色
            int pWaterFogColor, // 水的雾颜色
            @Nullable Integer pGrassColorOverride, // 草方块颜色
            @Nullable Integer pFoliageColorOverride, // 树叶颜色
            MobSpawnSettings.Builder pMobSpawnSettings,
            BiomeGenerationSettings.Builder pGenerationSettings,
            @Nullable Music pBackgroundMusic
    ) {
        BiomeSpecialEffects.Builder biomespecialeffects$builder = new BiomeSpecialEffects.Builder()
                .waterColor(pWaterColor)
                .waterFogColor(pWaterFogColor)
                .fogColor(12638463)
                .skyColor(calculateSkyColor(pTemperature))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .backgroundMusic(pBackgroundMusic);
        if (pGrassColorOverride != null) {
            biomespecialeffects$builder.grassColorOverride(pGrassColorOverride);
        }

        if (pFoliageColorOverride != null) {
            biomespecialeffects$builder.foliageColorOverride(pFoliageColorOverride);
        }

        return new Biome.BiomeBuilder()
                .hasPrecipitation(pHasPrecipitation)
                .temperature(pTemperature)
                .downfall(pDownfall)
                .specialEffects(biomespecialeffects$builder.build())
                .mobSpawnSettings(pMobSpawnSettings.build())
                .generationSettings(pGenerationSettings.build())
                .build();
    }

    // 添加一个自己的生物群系，使用了一些原版的方法。具体的内容自己点到方法里面看下把，不是很难，都是一些重复的配置的内容。
    public static Biome exampleBiome(HolderGetter<PlacedFeature> pPlacedFeatures, HolderGetter<ConfiguredWorldCarver<?>> pWorldCarvers) {
        // 创建一个MobSpawnSettings.Builder对象，用于配置生物生成设置。
        MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
        // 配置沙漠生成的生物。
        BiomeDefaultFeatures.desertSpawns(mobspawnsettings$builder);
        // 用于配置生物群系生成设置。
        BiomeGenerationSettings.Builder biomegenerationsettings$builder = new BiomeGenerationSettings.Builder(pPlacedFeatures, pWorldCarvers);
        // 添加化石装饰物到生物群系生成设置中
        BiomeDefaultFeatures.addFossilDecoration(biomegenerationsettings$builder);
        // 一些通用的配置
        globalOverworldGeneration(biomegenerationsettings$builder);
        // 矿物
        BiomeDefaultFeatures.addDefaultOres(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultFlowers(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultGrass(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDesertVegetation(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDefaultMushrooms(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDesertExtraVegetation(biomegenerationsettings$builder);
        BiomeDefaultFeatures.addDesertExtraDecoration(biomegenerationsettings$builder);
        // 调用构造返回biome
        return ExampleOverworldBiomes(false, 2.0F, 0.0F, mobspawnsettings$builder, biomegenerationsettings$builder, Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DESERT));
    }




    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder pGenerationSettings) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(pGenerationSettings);
        BiomeDefaultFeatures.addDefaultCrystalFormations(pGenerationSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(pGenerationSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(pGenerationSettings);
        BiomeDefaultFeatures.addDefaultSprings(pGenerationSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(pGenerationSettings);
    }

}

```

下面让我们使用我们的配置的类，进行json数据的生成

```java
// 原版的内容位于BiomeData类下
public class ModBiomeData {

    // 依旧添加一个对应biome的key
    public static final ResourceKey<Biome> EXAMPLE_BIOME = register("example_biome");
    // 对应大的create方法
    private static ResourceKey<Biome> register(String pKey) {
        return ResourceKey.create(Registries.BIOME, new ResourceLocation(ExampleMod.MODID,pKey));
    }

    // bootstrap和之前的一样
    public static void bootstrap(BootstapContext<Biome> pContext) {
        // 获得placefeature的holdgetter
        HolderGetter<PlacedFeature> holdergetter = pContext.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> holdergetter1 = pContext.lookup(Registries.CONFIGURED_CARVER);
        // 给数据生成注册我们的biome
        pContext.register(ModBiomeData.EXAMPLE_BIOME, ExampleOverworldBiomes.exampleBiome(holdergetter, holdergetter1));

    }
}

```

添加到provider中，和之前一样。不介绍了。

```java
public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModOreFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModOrePlacements::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap)
            .add(Registries.BIOME, ModBiomeData::bootstrap);

    public ModWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER,Set.of(ExampleMod.MODID));
    }
}



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
好了，这里你点击data gen之后就会生成你的biome.json文件了，但是你会发现没有在主世界中生成对应的biome，甚至你会发现我们上面的代码都没指定过在那个维度生成。而对于这部分内容，我并没有在neoforge中找到提供的方法， 同样如上，这部分在维度生成相关大的代码中，你或许需要mixin，不过这里我们使用terrablender lib帮帮助我们，在上一次的视频中我们已经配置过了这个lib。

所以我们直接使用。

```java
// 我们使用terrablender的Region类给主世界添加生物群系

public class ModOverworldRegion extends Region {
    public ModOverworldRegion(ResourceLocation name, int weight) {
        // name，维度，权重
        super(name, RegionType.OVERWORLD, weight);
    }
    // 重写addbiome方法，这个方法修改原版的主世界的维度中生成生物群系的方法
    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addModifiedVanillaOverworldBiomes(mapper,modifiedVanillaOverworldBuilder -> {
            modifiedVanillaOverworldBuilder.replaceBiome(Biomes.FOREST,ModBiomeData.EXAMPLE_BIOME);
        });
    }
}

```

注册我们写的这个region

```java
public class ModTerrablender {
    public static void registerBiome(){
        // 第一个参数我们的region，第二个参数是维度的定位符，第三个是权重。
        // 其中权重的数值设置你可以参考原版进行设置
        Regions.register(new ModOverworldRegion(new ResourceLocation(ExampleMod.MODID,"overworld"),5));
    }
}

```

然后，接下来我们说下怎么利用Terrablender来对生物群系的表面的方块进行修改。

教程只是对其中的一部分内容进行了介绍，并没有涵盖所有的内容，所有内容还是很多且有些复杂的，这说一部分，另一部分放在了维度的，剩下的就大家自己探索了。

其实surface规则的定义是放在维度的json中的。

```java
// 对我们的生物群系的表面进行修改
// 首先说明的一点是surfaceRule是针对维度的，通过定义一系列的sequence来指定生成，如果具有一些条件判断是可以使用ConditionSource类来判断的，RuleSource通常作为一个结果。

public class ModSurfaceRules {
    //  SurfaceRules.RuleSource 表面生成的规则，这里我们使用makeStateRule来创建一个SurfaceRules.RuleSource
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource RUBY_BLOCK = makeStateRule(ModBlocks.RUBY_BLOCK.get());
    private static final SurfaceRules.RuleSource DIAMOND_ORE = makeStateRule(Blocks.DIAMOND_ORE);

    // 静态方法，用于产生我们的surface规则。
    public static SurfaceRules.RuleSource makeRules() {
        // 第一个规则是一个条件规则，表示水面上的得的位置
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        // 使用第一个规则表示如果isAtOrAboveWaterLevel满足条件则使用GRASS_BLOCK方块。否则使用DIRT
        SurfaceRules.RuleSource   = SurfaceRules.sequence(SurfaceRules.ifTrue(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);

        // 返回我们的SurfaceRules.RuleSource
        return SurfaceRules.sequence(
            // 满足生物群系情况下，在地板使用RUBY_BLOCK和在天花板使用DIAMOND_ORE
            // 这里的地板是指在生成地表的时候的地板和天花板
                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ModBiomeData.EXAMPLE_BIOME),
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, RUBY_BLOCK)),
                        SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, DIAMOND_ORE)),

                // 这是一条原版的默认规则，如果不是这个生物群系，那么使用默认的草方块
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, grassSurface)
        );
    }
    // 这个makeStateRule也很简单，就是返回对应方块默认状态的 SurfaceRules.RuleSource
    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
```


注册相关的几个类和方法
```java

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    private static final Logger LOGGER = LogUtils.getLogger();
     public ExampleMod(IEventBus modEventBus)
    {
        // 你的region写在这里
        ModTerrablender.registerBiome();
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // 给主世界添加新的surfaceRule
        event.enqueueWork(()->{
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, ExampleMod.MODID, ModSurfaceRules.makeRules());
        });
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }
}

```

关于surfaceRule的教程，可以找一些教程，去看看具体得到相关内容，同样wiki上也有解释，你可以到wiki上查看.

但对于surfaceRule的原版代码内容在SurfaceRuleData这个类下面

到世界中看看你的生物群系吧.