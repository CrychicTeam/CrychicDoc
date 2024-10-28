---
title: 43 结构生成
published: 2024-04-14
tags: [Minecraft1_20_4, NeoForge20_3, Tutorial]
description: 43 结构生成 相关教程
image: ./covers/33bba4aee6ecbb1717f43c720138144e1b1de251.jpg
category: Minecraft1_20_4_NeoForge_Tutorial

authors: ['Flandre923']
---
# 结构生成

好了这次我们来说结构化生成，这次涉及到的东西比较多，且内容比较杂乱，所以大家自己多看看。

我们主要的一个类是Structure类，这个类是你生成结构的主体类，不过你的建筑是可以多个结构拼在一起的，所以关联到StructurePieces类，这个类表示结构的某一部分，然后你可以通过拼接多个StructurePieces来实现你的Structure，你也可以使用多个StructurePieces的拼接来构建不同的建筑。

而建筑是有两种方式可以获得的，第一种方式就是类似原版的沙漠金字塔和雨林的神庙，这个两个建筑的部分是硬编码在代码中的。第二种方法是使用nbt数据，而nbt数据可以通过你在游戏中建造相应的建组后通天特殊的structure的方块保存为nbt数据的，之后我们的代码通过nbt数据实现生成对应的建组，并且你可以建筑生成前和生成后做一些处理，例如添加一些奖励箱。

先大概看下我们的代码吧：

```java

public class MyStructure extends Structure {

    public static final Codec<MyStructure> CODEC = simpleCodec(MyStructure::new);

    public MyStructure(StructureSettings pSettings) {
        super(pSettings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        return onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, structurePiecesBuilder -> this.generatePieces(structurePiecesBuilder, pContext));
    }

    private void generatePieces(StructurePiecesBuilder pBuilder, Structure.GenerationContext pContext) {
        ChunkPos chunkpos = pContext.chunkPos();
        WorldgenRandom worldgenrandom = pContext.random();
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 64, chunkpos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        MyStructurePieces.addPieces(pContext.structureTemplateManager(), blockpos, rotation, pBuilder, worldgenrandom);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureType.MY_STRUCTURE.get();
    }


}
```

让我们来看看相关的类的以及配置等相关用到了的类把。

首先就是Structure类，该类是结构的基类，通过继承关系你可以看到对应的其他的子类，你看与参考学习其他的子类实现自己的结构，我们来说其中的字段和方法的含义：
- DIRECT_CODEC 和 CODEC：这两个字段是用于序列化和反序列化 Structure 对象的编解码器。
- settings：表示结构的设置，包括生物群系、生成步骤等。
- settingsCodec 和 simpleCodec：这两个方法用于创建 Structure 对象的编解码器。
- 构造函数 Structure(Structure.StructureSettings pSettings)：用于创建 Structure 对象，接受 StructureSettings 作为参数。
- biomes()、spawnOverrides()、step() 和 terrainAdaptation()：这些方法返回结构的相关设置。
- adjustBoundingBox(BoundingBox pBoundingBox)：根据地形适应设置调整结构的边界框。
- generate(...)：生成结构的主要方法，接受多个参数，如注册表访问器、区块生成器、生物群系源等。
- onTopOfChunkCenter(...)：在区块中心的顶部生成结构的辅助方法。
- isValidBiome(Structure.GenerationStub pStub, Structure.GenerationContext pContext)：检查给定位置的生物群系是否适合生成结构。
- afterPlace(...)：在生成结构后执行的方法，可用于后处理。
- getCornerHeights(...) 和 getLowestY(...)：计算结构的角高度和最低高度的辅助方法。
- findGenerationPoint(Structure.GenerationContext pContext) 和 findValidGenerationPoint(Structure.GenerationContext pContext)：查找结构的生成点的抽象方法和具体实现方法
- type()：返回结构的类型的抽象方法。

其中的findGenerationPoint方法和type方法是子类必须要实现的方法，也是我们使用的方法，其中findGenerationPoint方法是我们使用决定建筑的生成的位置的，而type需要我们返回strucutreType类的实例，之后我们会说怎么闯将StructureType的实例。

我们之前一直在回避codec这个概念，不过现在我们无法在回避他了，因为我们会用到他，Codec是新增的一套序列化和反序列的系统，用于同时处理nbt和json。目前看来似乎要在未来转向codec了。

不过基于我并不了解这个东西，所以这里还是说以下怎么在strucutre中创建这个codec吧，我们需要使用到这个codec获得StructureType。


```java
    //一种简单的方法就是通过Structure提供的方法这样创建，这样的创建简单的结构对于我们来说已经是够用了。
    // 直接传入你的structure的构造方法即可。
    public static final Codec<OceanMonumentStructure> CODEC = simpleCodec(OceanMonumentStructure::new);

    // 另一种是自己使用RecordCodecBuilder.create方法类构建
    // 其中你可以在通过Codec读取json文件中的额外属性
    public static final Codec<OceanRuinStructure> CODEC = RecordCodecBuilder.create(
        p_229075_ -> p_229075_.group(
                    settingsCodec(p_229075_),
                    OceanRuinStructure.Type.CODEC.fieldOf("biome_temp").forGetter(p_229079_ -> p_229079_.biomeTemp),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter(p_229077_ -> p_229077_.largeProbability),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter(p_229073_ -> p_229073_.clusterProbability)
                )
                .apply(p_229075_, OceanRuinStructure::new)
    );
```

下面是对应的structure的json文件，你可以看到对应的字段

```json
{
  "type": "minecraft:ocean_ruin",
  "biome_temp": "cold",
  "biomes": "#minecraft:has_structure/ocean_ruin_cold",
  "cluster_probability": 0.9,
  "large_probability": 0.3,
  "spawn_overrides": {},
  "step": "surface_structures"
}
```

好了，然后我们来看StructureSettings类，StructureSettings类是在构造Structure时候要传入的一个参数，是对Structure的配置信息。点开源码你会发现使用了一个纪录类，是Java16新增的一功能，主要有这样的几个字段

biomes：一个HolderSet<Biome/>，表示结构可以生成的生物群落。
spawnOverrides：一个Map<MobCategory, StructureSpawnOverride/>，用于指定哪些生物类别应该在结构中生成，以及它们的生成规则。
step：一个GenerationStep.Decoration，表示结构生成的阶段。
terrainAdaptation：一个TerrainAdjustment，用于指定结构如何适应地形。

那么我们应该怎么去创建这样的一个类呢？

```java
// 第一种方法是直接使用structure类提供的方法

    public static Structure.StructureSettings structure(
            HolderSet<Biome> pBiomes, Map<MobCategory, StructureSpawnOverride> pSpawnOverrides, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation
    ) {
        return new Structure.StructureSettings(pBiomes, pSpawnOverrides, pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, pTerrainAdaptation);
    }

// 第二中是直接new
new Structure.StructureSettings(pBiomes, pSpawnOverrides, pStep, pTerrainAdaptation);
```

而其中的pSpawnOverrides可传入一个空的map的，pStep是Decoration下的枚举，对于地表的建筑可以直接使用SURFACE_STRUCTURES，对于pTerrainAdaptation是TerrainAdjustment下的枚举。

接下来，来看GenerationStub类，这个了类是我们要实现的Structure中的findGenerationPoint要求你返回的一个内容，他说主要有两个字段第一个position，这个position是用于判断生物群系是否合法的。第二个字段是，generator：一个Either<Consumer<StructurePiecesBuilder/>, StructurePiecesBuilder/>类型的字段，你要传入的一个StructurePiecesBuilder的消费者，用于你去编写生成的位置和生成那些建组的piece。你也同样可以附加一些旋转等随机化的内容。

创建这样的一个GenerationStub类型也有多种方法

```java
     // 第一种是调用我们的onTopOfChunkCenter方法，onTopOfChunkCenter方法是Strucutre提供的方法，、
     // 第一个参数是GenerationContext生成的上下文信息，第二个参数是Heightmap.Types的枚举，你可以自己选择，具体怎么选择参考原版的代码。第三个是你的<Consumer<StructurePiecesBuilder>
    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        return getLowestY(pContext, this.width, this.depth) < pContext.chunkGenerator().getSeaLevel()
            ? Optional.empty()
            : onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, p_226545_ -> this.generatePieces(p_226545_, pContext));
    }
    // 第二中则是直接new出来
    // 第一个参数是用于生物群系判断的位置，第二个参数是<Consumer<StructurePiecesBuilder>
    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        Rotation rotation = Rotation.getRandom(pContext.random());
        BlockPos blockpos = this.getLowestYIn5by5BoxOffset7Blocks(pContext, rotation);
        return blockpos.getY() < 60
            ? Optional.empty()
            : Optional.of(new Structure.GenerationStub(blockpos, p_230240_ -> this.generatePieces(p_230240_, pContext, blockpos, rotation)));
    }

    // 直接调用的onTopOfChunkCenter
        @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        return onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, p_229979_ -> generatePieces(p_229979_, pContext));
    }


```

下面来看看findGenerationPoint方法中的参数GenerationContext类的内容：

- RegistryAccess registryAccess: 注册表访问对象
- ChunkGenerator chunkGenerator: 区块生成器
- BiomeSource biomeSource: 生物群系源
- RandomState randomState: 随机状态
- StructureTemplateManager structureTemplateManager: 结构模板管理器
- WorldgenRandom random: 世界生成随机数
- long seed: 种子
- ChunkPos chunkPos: 区块位置
- LevelHeightAccessor heightAccessor: 高度访问器
- Predicate<Holder<Biome/>> validBiome: 有效生物群系的谓词

可以看到GenerationContext这个类就是保存了一些生成信息，我们可以使用这些信息来生成自己的结构。


StructureType类，这个类是我们的structure对于的一个内容，有点类似我们的entity和EntityType这样的关系，这里的StructureType的作用就是为了标注不同的Structure。每一个structure对应了一个structureType类型。我们来看看怎么创建一个StructureType

```java
public class ModStructureType<S extends Structure> {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, ExampleMod.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<MyStructure>> MY_STRUCTURE = registerType("my_structure", () -> () -> MyStructure.CODEC);

    // 也许你会好奇为什么这里的要求一个supplier提供StructureType，但是给出的是两个() -> () -> MyStructure.CODEC，并且是一个Codec
    // 其实你点开StructureType，会发现这就是一个interface，只有一个方法，就是要求一个没有参数，返回值是codec的函数，
    // 所以() -> MyStructure.CODEC 这个lammbda就是对应StructureType接口的一个实现。
    private static <P extends Structure> DeferredHolder<StructureType<?>, StructureType<P>> registerType(String name, Supplier<StructureType<P>> factory) {
        return STRUCTURE_TYPES.register(name, factory);
    }

    public static void register(IEventBus eventBus){
        STRUCTURE_TYPES.register(eventBus);
    }
}

```

好了，到这里位置我们已经讲解完了structure的内容，并说明了两个需要从重写方法的内容。不过还有一点没解决的就是在findGenerationPoint中要提供的那个Consumer<StructurePiecesBuilder/>没有说怎么构造。而这个方法主要是调用piece的内容，让我们来看看structurePiece的内哦让那个吧。

我们还是先把我们的所有的structurepiece的内容放出来。

```java

public class MyStructurePieces {
    static final ResourceLocation STRUCTURE_LOCATION_MY_STRUCTURE = new ResourceLocation(ExampleMod.MODID,"my_structure");
    static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(
        STRUCTURE_LOCATION_MY_STRUCTURE, new BlockPos(4, 0, 4)
    );
    static final Map<ResourceLocation, BlockPos> OFFSETS = ImmutableMap.of(
            STRUCTURE_LOCATION_MY_STRUCTURE, BlockPos.ZERO
    );

    public static void addPieces(
            StructureTemplateManager pStructureTemplateManager, BlockPos pStartPos, Rotation pRotation, StructurePieceAccessor pPieces, RandomSource pRandom
    ) {
        pPieces.addPiece(new MyStructurePieces.MyStructurePiece(pStructureTemplateManager, STRUCTURE_LOCATION_MY_STRUCTURE, pStartPos, pRotation, 0));
    }

    public static class MyStructurePiece extends TemplateStructurePiece {

        public MyStructurePiece(StructureTemplateManager pStructureTemplateManager, ResourceLocation pLocation, BlockPos pStartPos, Rotation pRotation, int pDown) {
            super(ModStructurePieceTypes.MY_STRUCTURE_PIECE.get(), 0, pStructureTemplateManager, pLocation, pLocation.toString(),
                    makeSettings(pRotation, pLocation),
                    makePosition(pLocation, pStartPos, pDown)
            );
        }


        public MyStructurePiece(StructureTemplateManager pStructureTemplateManager, CompoundTag pTag) {
            super(ModStructurePieceTypes.MY_STRUCTURE_PIECE.get(), pTag, pStructureTemplateManager,   resourceLocation -> makeSettings(Rotation.valueOf(pTag.getString("Rot")), resourceLocation));
        }

        private static StructurePlaceSettings makeSettings(Rotation pRotation, ResourceLocation pLocation) {
            return new StructurePlaceSettings()
                    .setRotation(pRotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(MyStructurePieces.PIVOTS.get(pLocation))
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation pLocation, BlockPos pPos, int pDown) {
            return pPos.offset(MyStructurePieces.OFFSETS.get(pLocation)).below(pDown);
        }
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
            super.addAdditionalSaveData(pContext, pTag);
            pTag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
            if ("chest".equals(pName)) {
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity blockentity = pLevel.getBlockEntity(pPos.below());
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(BuiltInLootTables.IGLOO_CHEST, pRandom.nextLong());
                }
            }
        }

        @Override
        public void postProcess(
                WorldGenLevel pLevel,
                StructureManager pStructureManager,
                ChunkGenerator pGenerator,
                RandomSource pRandom,
                BoundingBox pBox,
                ChunkPos pChunkPos,
                BlockPos pPos
        ) {
            super.postProcess(pLevel, pStructureManager, pGenerator, pRandom, pBox, pChunkPos, pPos);
        }
    }
}
public class MyStructurePieces {
    static final ResourceLocation STRUCTURE_LOCATION_MY_STRUCTURE = new ResourceLocation(ExampleMod.MODID,"my_structure");
    static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(
        STRUCTURE_LOCATION_MY_STRUCTURE, new BlockPos(4, 0, 4)
    );
    static final Map<ResourceLocation, BlockPos> OFFSETS = ImmutableMap.of(
            STRUCTURE_LOCATION_MY_STRUCTURE, BlockPos.ZERO
    );

    public static void addPieces(
            StructureTemplateManager pStructureTemplateManager, BlockPos pStartPos, Rotation pRotation, StructurePieceAccessor pPieces, RandomSource pRandom
    ) {
        pPieces.addPiece(new MyStructurePieces.MyStructurePiece(pStructureTemplateManager, STRUCTURE_LOCATION_MY_STRUCTURE, pStartPos, pRotation, 0));
    }

    public static class MyStructurePiece extends TemplateStructurePiece {

        public MyStructurePiece(StructureTemplateManager pStructureTemplateManager, ResourceLocation pLocation, BlockPos pStartPos, Rotation pRotation, int pDown) {
            super(ModStructurePieceTypes.MY_STRUCTURE_PIECE.get(), 0, pStructureTemplateManager, pLocation, pLocation.toString(),
                    makeSettings(pRotation, pLocation),
                    makePosition(pLocation, pStartPos, pDown)
            );
        }


        public MyStructurePiece(StructureTemplateManager pStructureTemplateManager, CompoundTag pTag) {
            super(ModStructurePieceTypes.MY_STRUCTURE_PIECE.get(), pTag, pStructureTemplateManager,   resourceLocation -> makeSettings(Rotation.valueOf(pTag.getString("Rot")), resourceLocation));
        }

        private static StructurePlaceSettings makeSettings(Rotation pRotation, ResourceLocation pLocation) {
            return new StructurePlaceSettings()
                    .setRotation(pRotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(MyStructurePieces.PIVOTS.get(pLocation))
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation pLocation, BlockPos pPos, int pDown) {
            return pPos.offset(MyStructurePieces.OFFSETS.get(pLocation)).below(pDown);
        }
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
            super.addAdditionalSaveData(pContext, pTag);
            pTag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
            if ("chest".equals(pName)) {
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity blockentity = pLevel.getBlockEntity(pPos.below());
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(BuiltInLootTables.IGLOO_CHEST, pRandom.nextLong());
                }
            }
        }

        @Override
        public void postProcess(
                WorldGenLevel pLevel,
                StructureManager pStructureManager,
                ChunkGenerator pGenerator,
                RandomSource pRandom,
                BoundingBox pBox,
                ChunkPos pChunkPos,
                BlockPos pPos
        ) {
            super.postProcess(pLevel, pStructureManager, pGenerator, pRandom, pBox, pChunkPos, pPos);
        }
    }
}
```

我们使用的是一个TemplateStructurePiece的类，不过先说说他的父类的StructurePiece吧，下面是StructurePiece是我们会常用到的三个方法，其中两个是继承要是实现的方法。

```java
    // 该方法是传入一个接口序列化的上下文信息和一个nbt数据
    // 作用是在序列化之后，从nbt读取数据之前，你可以向nbt数据附加一些内容。用于你的结构化生成。
    protected abstract void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag);
    
    // 该方法用于处理标志的数据的
    // 例如你要给你一个结构中的一些位置添加战利品箱子，你可以利用这个方法来实现。
    protected abstract void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox);

    //postProcess的作用是在结构生成之后，对结构piece的进一步的处理和修饰。
    // 不过也可以在生成之前操作。等会我们会看到怎么操作。
    // 这里的参数的类名也比较清晰了就不过多介绍了。
    @Override
    public void postProcess(
        WorldGenLevel pLevel,
        StructureManager pStructureManager,
        ChunkGenerator pGenerator,
        RandomSource pRandom,
        BoundingBox pBox,
        ChunkPos pChunkPos,
        BlockPos pPos
    ) 
```

好了，让我们来看TemplateStructurePiece，这是一个StructurePiece类的具体实现，用于表示一个由结构模板生成的结构方块。

```java
// 第一个构造函数
  public TemplateStructurePiece(
        StructurePieceType pType,// structurepiecetype 需要我们去注册的。
        int pGenDepth, // 结构生成时候的深度
        StructureTemplateManager pStructureTemplateManager, // 结构模板管理器
        ResourceLocation pLocation, // 模板的位置
        String pTemplateName, // 模板的名称
        StructurePlaceSettings pPlaceSettings, // 结构piece放置的设置
        BlockPos pTemplatePosition // 结构piece放置的位置
    )

     public TemplateStructurePiece(
        StructurePieceType pType,  // piecetype 需要我们自己注册 
        CompoundTag pTag,  // nbt
        StructureTemplateManager pStructureTemplateManager, // 结构模板管理器
        Function<ResourceLocation, StructurePlaceSettings> pPlaceSettingsFactory //  用于创建 StructurePlaceSettings 实例的函数
    )

    // 给nbt添加了放置的位置的数据
        @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        pTag.putInt("TPX", this.templatePosition.getX());
        pTag.putInt("TPY", this.templatePosition.getY());
        pTag.putInt("TPZ", this.templatePosition.getZ());
        pTag.putString("Template", this.templateName);
    }
    // 该类对该方法进行了实现，主要的功能就是放置结构piece模板到世界中，并调用handleDataMarker处理标记数据。
    @Override
    public void postProcess(
        WorldGenLevel pLevel,
        StructureManager pStructureManager,
        ChunkGenerator pGenerator,
        RandomSource pRandom,
        BoundingBox pBox,
        ChunkPos pChunkPos,
        BlockPos pPos
    ) 



```

下面来看我们的piece的类吧

```java

    public static class MyStructurePiece extends TemplateStructurePiece {
        // 构造方法1，等会我们在structure中讲，怎么new，也就是我们刚刚没说的方法addpiece方法。
        public MyStructurePiece(StructureTemplateManager pStructureTemplateManager, ResourceLocation pLocation, BlockPos pStartPos, Rotation pRotation, int pDown) {
            super(ModStructurePieceTypes.MY_STRUCTURE_PIECE.get(), 0, pStructureTemplateManager, pLocation, pLocation.toString(),
                    makeSettings(pRotation, pLocation),
                    makePosition(pLocation, pStartPos, pDown)
            );
        }

        // 构造方法二
        public MyStructurePiece(StructureTemplateManager pStructureTemplateManager, CompoundTag pTag) {
            super(ModStructurePieceTypes.MY_STRUCTURE_PIECE.get(), pTag, pStructureTemplateManager,   resourceLocation -> makeSettings(Rotation.valueOf(pTag.getString("Rot")), resourceLocation));
        }
        // 辅助函数用于生成StructurePlaceSettings，第一个参数是构建时候传入的随机数
        private static StructurePlaceSettings makeSettings(Rotation pRotation, ResourceLocation pLocation) {
            return new StructurePlaceSettings()
                    .setRotation(pRotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(MyStructurePieces.PIVOTS.get(pLocation))
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }
        // 辅助函数用于返回piece生成的位置
        private static BlockPos makePosition(ResourceLocation pLocation, BlockPos pPos, int pDown) {
            return pPos.offset(MyStructurePieces.OFFSETS.get(pLocation)).below(pDown);
        }
        // 给nbt添加一个rot的字段，表示随机旋转的信息。
        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
            super.addAdditionalSaveData(pContext, pTag);
            pTag.putString("Rot", this.placeSettings.getRotation().name());
        }

        // 下面还是我们的piece类中的hhandleDataMarker
        // 这个主要处理了结构模板中的箱子，并放置战利品箱子
        // 其中的这个chest数据你可以到对应的nbt下看，nbt打开格式需要你用idea装下那个minecraft development插件
        protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pBox) {
            if ("chest".equals(pName)) {
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity blockentity = pLevel.getBlockEntity(pPos.below());
                if (blockentity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockentity).setLootTable(BuiltInLootTables.IGLOO_CHEST, pRandom.nextLong());
                }
            }
        }

// 下面是我们自己类中的postProcess的方法，我们说下如何在生成前处理，或者生成后处理。
 @Override
        public void postProcess(
                WorldGenLevel pLevel,
                StructureManager pStructureManager,
                ChunkGenerator pGenerator,
                RandomSource pRandom,
                BoundingBox pBox,
                ChunkPos pChunkPos,
                BlockPos pPos
        ) {
            // 对生成前进行处理的话
            // 写在这里
            super.postProcess(pLevel, pStructureManager, pGenerator, pRandom, pBox, pChunkPos, pPos);
            // 生成后处理
            // 写在这里
        }
    }
```
让我们回到strcutre类中，来看这个方法
```java
    private void generatePieces(StructurePiecesBuilder pBuilder, Structure.GenerationContext pContext) {
        ChunkPos chunkpos = pContext.chunkPos();
        WorldgenRandom worldgenrandom = pContext.random();
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 64, chunkpos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        MyStructurePieces.addPieces(pContext.structureTemplateManager(), blockpos, rotation, pBuilder, worldgenrandom);
    }
```

StructurePiecesBuilder类实现了StructurePieceAccessor接口，StructurePiecesBuilder是管理piece的类，你需要通过这个类向你的建组添加piece，通过addpieces方法，这里这里我们获得了随机数和位置信息，以及一个随机的旋转方向，传入给了辅助的方法addPieces。
以下是辅助方法的内容:

```java
    public static void addPieces(
            StructureTemplateManager pStructureTemplateManager, BlockPos pStartPos, Rotation pRotation, StructurePieceAccessor pPieces, RandomSource pRandom
    ) {
        pPieces.addPiece(new MyStructurePieces.MyStructurePiece(pStructureTemplateManager,  , pStartPos, pRotation, 0));
    }
```

好了，下面来看看这个MyStructurePiece的构造方法，这个是我们写的方法，其中的我们在父类的构造方法中需要传入一个StructurePlaceSettings的东西，这个StructurePlaceSettings是我们的在MyStructurePiece类中的makeSettings方法中获得的。

```java
        // 可以看出来是可以直接new出来的，new出来之后，你可以根据自己的需求进行设置，这里的设置了一些内容，
        // 比如设置piece的旋转
        // 设置mirror镜像
        // 设置旋转点
        // 以及最后一个addProcessor，表示添加一个新的StructureProcessor，这里用的是BlockIgnoreProcessor下面的STRUCTURE_BLOCK，
        // 表示生成的时候不要生成STRUCTURE_BLOCK方块。
        private static StructurePlaceSettings makeSettings(Rotation pRotation, ResourceLocation pLocation) {
            return new StructurePlaceSettings()
                    .setRotation(pRotation)
                    .setMirror(Mirror.NONE)
                    .setRotationPivot(MyStructurePieces.PIVOTS.get(pLocation))
                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

```

到此为止，关于，strcture和strcutrepiece相关的内容就讲解完毕了，你也可以去看看其他的建筑的生成相关的内容学习。

好了下面我们来说于注册相关的内容，首先要解决的是关于structure中的type返回的StructureType内容。注册strcturetype就如上面说的一样，就不在重复赘述了，这里直接给出注册的代码和type的代码

```java
// 注册StructureType方法
public class ModStructureType<S extends Structure> {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, ExampleMod.MODID);

    public static final DeferredHolder<StructureType<?>, StructureType<MyStructure>> MY_STRUCTURE = registerType("my_structure", () -> () -> MyStructure.CODEC);

    private static <P extends Structure> DeferredHolder<StructureType<?>, StructureType<P>> registerType(String name, Supplier<StructureType<P>> factory) {
        return STRUCTURE_TYPES.register(name, factory);
    }

    public static void register(IEventBus eventBus){
        STRUCTURE_TYPES.register(eventBus);
    }
}

```
type方法

```java
    @Override
    public StructureType<?> type() {
        return ModStructureType.MY_STRUCTURE.get();
    }
```

下面我们在解决StrcturePieceType的问题，在StrcturePiece的构造方法中需要你传入StructurePieceType的实例，这里我们给出注册的方法。

```java
//StructurePieceType注册
public class ModStructurePieceTypes {
    // 注册器
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, ExampleMod.MODID);
    // 注册的对象
    public static final DeferredHolder<StructurePieceType, StructurePieceType> MY_STRUCTURE_PIECE  = registerPieceType("my_structure_piece", MyStructurePieces.MyStructurePiece::new);
    // 辅助注册的方法
    private static DeferredHolder<StructurePieceType, StructurePieceType> registerPieceType(String name, StructurePieceType.StructureTemplateType structurePieceType) {
        // 第一个参数是name，第二个参数是构造方法
        // 第二个参数补充一点是StructureTemplateType是一个接口，只有一个方法，其实就是要求传入一个该接口的实例，这个接口实现方法应该传入
        // 两个StructureTemplateManager和CompoundTag参数返回一个StructurePiece
        // 所以这里可以直接是使用new构造方法。
        // 而register要求返回一个supplier，所以就这样写了。
        return STRUCTURE_PIECE_TYPES.register(name.toLowerCase(Locale.ROOT), () -> structurePieceType);
    }
    // 别忘记添加到总线
    public static void register(IEventBus eventBus){
        STRUCTURE_PIECE_TYPES.register(eventBus);
    }
}

```
StructureSets类是描述你就建筑在世界上的生成的情况的，比如建组在地图上的生成策略，例如随机生成，或者是像暮色那样的棋盘形状，salt是一个盐或者说是随机种子数，要保持唯一的数值不要和其他的建筑的数值一样，不然可能随机生成的位置会发生冲突，以及最小间距等等。不过这个类主要的功能是生成json文件，其实整个建筑的生成都可以使用json文件而不编写代码，不过我们既然可以写代码，还是采用了代码的形势，感觉是比写json来更加直观和容易理解一些，也采取了和原版类似的方式，方便对大家阅读理解原版的代码。

```java
public class ModStructureSets {
    // 我们structureset的资源名称
    public static final ResourceKey<StructureSet> MY_STRUCTURE_SET = register("my_structure");
    // 等会使用数据生成要要传递的方法引用
    public static void bootstrap(BootstapContext<StructureSet> pContext) {
        // BootstapContext数据生成时候的上下文
        // 从上下文中获得HolderGetter<Structure> 
        HolderGetter<Structure> holdergetter = pContext.lookup(Registries.STRUCTURE);
        // 通过上下文注册我们的set
        // 第一个参数是我们的key
        // 第二个参数是set的实例，new处理
        // set的第一个参数是对应的strcture，这里通过holder获得，第二个参数就是描述放置的信息的类
        // RandomSpreadStructurePlacement是随机放置，也有另一个ConcentricRingsStructurePlacement放置，他们继承于StructurePlacement
        // 我们使用了随机放置
        // 随机放置的第一个参数是当前位置生成失败时候寻找下一生成点的距离
        // 第二个参数建组之间的最小间隔。
        // 第三个参数是随机放置的类型，这里填的线性的， 也有其他的可以选择。
        // 第四个参数是盐
        pContext.register(
                ModStructureSets.MY_STRUCTURE_SET,
                new StructureSet(holdergetter.getOrThrow(ModStructures.MY_STRUCTURE), new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 14357619))
        );
    }

    private static ResourceKey<StructureSet> register(String pName) {
        return ResourceKey.create(Registries.STRUCTURE_SET, new ResourceLocation(ExampleMod.MODID,pName));
    }

}

```

以上代码其实就是用于生成json，而关于json的内容，你看可以在wiki上查到的。这里贴出wiki地址：https://minecraft.wiki/w/Structure_set

Structures类，同样适用于生成json的内容，不过这个描述的建组的生成的生物群系，以及生成的阶段。
```java
public class ModStructures {
    // 以下的三个辅助的方法用于获得StructureSettings这个类 
    // 和之前讲解StructureSettings的一致，就不赘述了。
    public static Structure.StructureSettings structure(
            HolderSet<Biome> pBiomes, Map<MobCategory, StructureSpawnOverride> pSpawnOverrides, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation
    ) {
        return new Structure.StructureSettings(pBiomes, pSpawnOverrides, pStep, pTerrainAdaptation);
    }
    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, GenerationStep.Decoration pStep, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), pStep, pTerrainAdaptation);
    }

    private static Structure.StructureSettings structure(HolderSet<Biome> pBiomes, TerrainAdjustment pTerrainAdaptation) {
        return structure(pBiomes, Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, pTerrainAdaptation);
    }
    // strucutre的key
    public static final ResourceKey<Structure> MY_STRUCTURE = registerKey("my_structure");
    // 创建key的方法
    public static ResourceKey<Structure> registerKey(String name) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(ExampleMod.MODID,name));
    }
    // bootstrap在数据生成时候调用
    public static void bootstrap(BootstapContext<Structure> context) {
        // 从上下文中获得所有的生物群系
        HolderGetter<Biome> biomeHolderGetter = context.lookup(Registries.BIOME);
            // 第一个参数是key
            // 第二个参数就是我们的strucutre的构造
            // 构造的要传入一个StructureSettings
            // setting的一个参数是生物群系，我们指定了生物群系是主世界，对于第二个参数设置的是TerrainAdjustment.NONE，当然你也可以选择其他的。
        context.register(
                ModStructures.MY_STRUCTURE,
                new MyStructure(structure(biomeHolderGetter.getOrThrow(BiomeTags.IS_OVERWORLD), TerrainAdjustment.NONE))
        );

    }
}


```

总线
```java

     public ExampleMod(IEventBus modEventBus)
    {
        modEventBus.addListener(this::commonSetup);
        ModStructureType.register(modEventBus);
        ModStructurePieceTypes.register(modEventBus);
        NeoForge.EVENT_BUS.register(this);
    }

```

以及数据生成

```java
public class ModWorldGen extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModOreFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModOrePlacements::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap);

    public ModWorldGen(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER,Set.of(ExampleMod.MODID));
    }
}

```

添加到事件。

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