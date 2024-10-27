---
title: 46 添加维度
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 46 添加维度 相关教程
image: ./covers/dcdf7f7ee8c35900ba6ffaf9bfcb8bf7d0c2f339.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 参考
https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.20.X/commit/c9c8ce9ea968bf834f9553fa6361b510bdc7e0fb#diff-90c4e40141b4edaaf4f71a499fa5124e38d76b36619bb8ad198f2e61e7d4fa39
## 维度生成相关json的wiki
https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E7%BB%B4%E5%BA%A6
https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%B8%96%E7%95%8C%E9%A2%84%E8%AE%BE
## 添加维度
对于维度生成，有比较多的东西，所以我们还是讲一个简单的例子，主要讲下结构，其他的内容大家自己学习，可以去看源码或者wiki。

我们主要和两个类以及这两个类用到的类打交道。其中一个是DimensionType，另一个是LevelStem。

其中第一个DimensionType是关于维度的一些大体上的配置的，例如维度的最大y值，最小y值，使用床是否爆炸等。详细的可以看https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E7%BB%B4%E5%BA%A6

其中另一个LevelStem就是规定了维度生成那些生物群系，以及怎么生成。关于该json的内容可以在wiki查看：https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%B8%96%E7%95%8C%E9%A2%84%E8%AE%BE

这里我们从代码的角度来说相关的内容。

对于配置主要要求我们提供一个LevelStem的类，

```java
    // 而这个类主要有两个参数，第一个参数是对于的维度type，而第二个参数是我们主要的配置维度生成的参数，要求提供的是一个ChunkGenerator，
    LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.EXAMPLE_DIM_TYPE), noiseBasedChunkGenerator);

    // 继续往下看，ChunkGenerator的继承树中，我们主要用的是NoiseBasedChunkGenerator，而DebugLevelSource和FlatLevelSource则是调试和超平坦。
    ChunkGenerator (net.minecraft.world.level.chunk)
    DebugLevelSource (net.minecraft.world.level.levelgen)
    NoiseBasedChunkGenerator (net.minecraft.world.level.levelgen)
    FlatLevelSource (net.minecraft.world.level.levelgen)

    // NoiseBasedChunkGenerator就是我们要使用的类了。我们通过这个类类进行设置。
    // 我们使用是这个构造函数
    public NoiseBasedChunkGenerator(BiomeSource p_256415_, Holder<NoiseGeneratorSettings> p_256182_) {// 第一个参数表示你要生成的生物群系，第二个参数是NoiseGeneratorSettings
        super(p_256415_);
        this.settings = p_256182_;// 也就是你对这个噪声生成器的一些设置
        this.globalFluidPicker = Suppliers.memoize(() -> createFluidPicker(p_256182_.value()));
    }

    // 其中的NoiseGeneratorSettings就决定了你地形生成的样子
    // 我们使用的是一个原版提供的noiseGeneratorSettings
    public static final ResourceKey<NoiseGeneratorSettings> AMPLIFIED = ResourceKey.create(Registries.NOISE_SETTINGS, new ResourceLocation("amplified"));
    // 这是他的定义代码
    // 提示：这里的NoiseGeneratorSettings是生成noise settings json文件的 你看与查看原版的json
    // 以及这里是对应的json的Wiki：https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%B8%96%E7%95%8C%E7%94%9F%E6%88%90
    public static NoiseGeneratorSettings overworld(BootstapContext<?> pContext, boolean pAmplified, boolean pLarge) {
        return new NoiseGeneratorSettings(
            NoiseSettings.OVERWORLD_NOISE_SETTINGS, //  一个类型为NoiseSettings的成员变量。
            Blocks.STONE.defaultBlockState(), // 默认方块
            Blocks.WATER.defaultBlockState(), // 默认流体
            NoiseRouterData.overworld(pContext.lookup(Registries.DENSITY_FUNCTION), pContext.lookup(Registries.NOISE), pLarge, pAmplified), //噪声路由器
            SurfaceRuleData.overworld(), // surface rule 表面规则： SurfaceRules.RuleSource
            new OverworldBiomeBuilder().spawnTarget(), // 生成的目标 ： List<Climate.ParameterPoint>
            63, // 海平面高度
            false, //是否禁用生物生成 
            true,// 含水层：aquifersEnabled
            true,// 是否启用矿脉oreVeinsEnabled,
            false // useLegacyRandomSource
        );
    }
    // 对于第一参数 NoiseSettings 是对世界生成参数的一些设置
    // 地形开始生成的最低高度
    // 地形生成的总高度。
    // 噪声大小（水平）
    // 噪声大小（垂直）
    record NoiseSettings(int minY, int height, int noiseSizeHorizontal, int noiseSizeVertical) 

    //而NoiseRouter将密度函数应用于用于世界生成的噪声参数
    //其中的参数请参阅wiki即可。

    // 表面规则，就是我们上期视频说的，位于SurfaceRuleData类下，大家自行查看。
    
```

好了，从上面的内容，我们大致已经把维度生成的结构说清楚了，更详细的内容详见：https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%B8%96%E7%95%8C%E7%94%9F%E6%88%90

为了生成你想要的维度，还是建议多看原版的代码，如果无法满足你的要求在尝试自己写，主要的几个部分已经说了，你可以参考原版的代码去写。也去看看开源的模组，以及相关的视频教程，比如一些游戏教程利用噪声是怎么生成地形的。 

现在回到我们的NoiseBasedChunkGenerator的第一参数，要求我们传入的是生物群系的源，这里并不能直接传入生物群系进去，还需要做一些配置，

```java
    public NoiseBasedChunkGenerator(BiomeSource p_256415_, Holder<NoiseGeneratorSettings> p_256182_) {// 第一个参数表示你要生成的生物群系，第二个参数是NoiseGeneratorSettings
        super(p_256415_);
        this.settings = p_256182_;// 也就是你对这个噪声生成器的一些设置
        this.globalFluidPicker = Suppliers.memoize(() -> createFluidPicker(p_256182_.value()));
    }

    /// 这个createFromList方法帮助我们返回就是第一个参数所需要的BiomeSource
    // 我们通过下面的方法传入一系列的生物群系，还需要知名生物群系的一些设置。
    // 这里传入了个四个生物群系
    MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomeData.EXAMPLE_BIOME)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OCEAN)),
                                Pair.of(
                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.DARK_FOREST))

                        ))),
    // 来看对应的生物群系的内容是上面
    Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F),
    
    // 定义
    // 这些参数是生物群系的噪声的设置参数，主要决定了生物群系的一些生成，例如高度，悬崖，峡谷等等地形的生成
    // temperature：温度。不要与生物群系的温度混淆。
    // humidity：湿度。格式与temperature相同，可以为一个对象、一个数组或者一个浮点数。
    // continentalness：大陆性。在原版主世界，低大陆性对应于海洋地形，高大陆性对应于内陆地形。格式与temperature相同。
    // erosion：侵蚀度。在原版主世界，高侵蚀度对应平坦地形。格式与temperature相同。
    // weirdness：怪异度。在原版主世界，用于生物群系崎岖程度。格式与temperature相同。
    // depth：深度。在原版主世界，只有此参数会随垂直位置改变：在XZ坐标不变的情况下，Y轴坐标越高，该值越高。格式与temperature相同。
    // offset：偏移。取值为0.0到1.0的闭区间。类似于其他参数，但是offset在任何地方都是0，因此若其他参数都相等，将这个参数设为靠近0的值会使其占更多优势。
    public static Climate.ParameterPoint parameters(
        float pTemperature, float pHumidity, float pContinentalness, float pErosion, float pDepth, float pWeirdness, float pOffset
    ) 
```

以上的内容同样可以在wiki中查询得到：https://zh.minecraft.wiki/w/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%B8%96%E7%95%8C%E7%94%9F%E6%88%90


好了大体上理解之后我们来看我们的代码把，这里只是简单添加了一个维度。以及一个传送的方块，将玩家送到对应的维度。

```java

public class ModDimensions {
    // 对应的key，这里需要注意的是LevelStem和Level应该是一样的名字
    public static final ResourceKey<LevelStem> EXAMPLE_DIM_STEM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(ExampleMod.MODID, "examplemod_dim"));
    public static final ResourceKey<Level> EXAMPLE_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(ExampleMod.MODID, "examplemod_dim"));
    public static final ResourceKey<DimensionType> EXAMPLE_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(ExampleMod.MODID, "examplemod_dim_type"));
    // 生成DimensionType 对应的json
    public static void bootstrapType(BootstapContext<DimensionType> context) {
        // 详细的参数含义看wiki
        context.register(EXAMPLE_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // 是否是固定时间,即固定在白天或者夜晚的某一个时间
                false, // 天空光照
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                0, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), 0)));
    }

    // 生成dimension的json文件
    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

    // 这里的代码都说过了。
        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
                MultiNoiseBiomeSource.createFromList(
                        new Climate.ParameterList<>(List.of(Pair.of(
                                        Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomeData.EXAMPLE_BIOME)),
                                Pair.of(
                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST)),
                                Pair.of(
                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OCEAN)),
                                Pair.of(
                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.DARK_FOREST))

                        ))),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));
        // 一个stem 给context
        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.EXAMPLE_DIM_TYPE), noiseBasedChunkGenerator);

        context.register(ModDimensions.EXAMPLE_DIM_STEM_KEY, stem);
    }

}
```

好了下面来看看我们的怎么实现传送的,添加一个方块，实现玩家右键方块后传送到另一个维度，如果在另一个维度点击传送到主世界。

```java


public class PortalBlock extends Block {

    public PortalBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noLootTable());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(pPlayer.canChangeDimensions()){
            handlePortalTeleport(pLevel, pPos, pPlayer);
            return InteractionResult.SUCCESS;
        }else{
            return   InteractionResult.FAIL;
        }
    }
    //  传送的方法
    // 注意传送的逻辑应该在服务端
    private void handlePortalTeleport(Level pLevel, BlockPos pPos, Player pPlayer) {
        if(pPlayer.level() instanceof ServerLevel serverLevel){
            MinecraftServer minecraftServer = serverLevel.getServer();
            // 根据玩家当前所在的维度（dimension）判断传送目标维度的key。
            ResourceKey<Level> resourceKey = pPlayer.level().dimension() == ModDimensions.EXAMPLE_LEVEL_KEY ? Level.OVERWORLD :  ModDimensions.EXAMPLE_LEVEL_KEY;
            ServerLevel portalDimension = minecraftServer.getLevel(resourceKey);
            if (portalDimension != null && !pPlayer.isPassenger()) {
                if(resourceKey == ModDimensions.EXAMPLE_LEVEL_KEY){
                    // 如果前往EXAMPLE_LEVEL_KEY维度，则为true
                    pPlayer.changeDimension(portalDimension,new ModTeleporter(pPos,true));
                }else{
                    // 如果前往OVERWORLD维度，则为false
                    // 其中的第二个参数是实现了ITeleporter接口的一个类。
                    // 这里我们自己写了一个类
                    pPlayer.changeDimension(portalDimension,new ModTeleporter(pPos,false));
                }
            }
        }
    }
}

///  传送的类
public class ModTeleporter implements ITeleporter {
    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideDimension = false;

    public ModTeleporter(BlockPos pos ,boolean insideDimension) {
        this.thisPos = pos;
        this.insideDimension = insideDimension;
    }
    // 主要重写的是这个方法。实现将实体放在什么位置的逻辑。
    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        entity = repositionEntity.apply(false); // 获得对应的实体。
        int y = 61;
        // false 时 dest 是主世界
        if(!insideDimension){
            y = thisPos.getY();
        }

        BlockPos destinationPos = new BlockPos(thisPos.getX(), y, thisPos.getZ());

        int tries = 0;
        //使用循环（最多25次）来尝试找到一个合适的空气或可替换水的位置，通过逐次向上移动 destinationPos 来进行搜索。
        while((destWorld.getBlockState(destinationPos).getBlock() != Blocks.AIR) &&
        !destWorld.getBlockState(destinationPos).canBeReplaced(Fluids.WATER) &&
                (destWorld.getBlockState(destinationPos.above()).getBlock() != Blocks.AIR) &&
                !destWorld.getBlockState(destinationPos.above()).canBeReplaced(Fluids.WATER) && (tries < 25)){

            destinationPos = destinationPos.above(2);
            tries++;
        }
        // 将实体对象 entity 移动到找到的合适位置。
        entity.setPos(destinationPos.getX(), destinationPos.getY(), destinationPos.getZ());

        // 如果是去往Exampledim的维度
        if (insideDimension) {
            //如果 insideDimension 为真，则在目的地世界内以 destinationPos 为中心的周围区域内检查是否存在传送门方块，若不存在则在 destinationPos 处生成一个传送门方块。
            boolean doSetBlock = true;
            for (BlockPos checkPos : BlockPos.betweenClosed(destinationPos.below(10).west(10),
                    destinationPos.above(10).east(10))) {
                if (destWorld.getBlockState(checkPos).getBlock() instanceof PortalBlock) {
                    doSetBlock = false;
                    break;
                }
            }
            if (doSetBlock) {
                destWorld.setBlock(destinationPos, ModBlocks.PORTAL_BLOCK.get().defaultBlockState(), 3);
            }
        }

        return entity;
    }
}
```

对于方块的注册就自己实现把。

好了可以进入到世界中查看了。

最后给大家推荐一个生成json的网站，你可以通过手动配置选择的方式获得json，这个网站应该可以帮助你理解各个字段的含义。
https://misode.github.io/worldgen/noise-settings/

## 原版的类的位置

levelstem
net.minecraft.world.level.dimension.LevelStem

dimensiontype
net.minecraft.data.worldgen.DimensionTypes

