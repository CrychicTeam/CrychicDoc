package net.minecraft.world.level.levelgen;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.OptionalInt;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import org.apache.commons.lang3.mutable.MutableObject;

public final class NoiseBasedChunkGenerator extends ChunkGenerator {

    public static final Codec<NoiseBasedChunkGenerator> CODEC = RecordCodecBuilder.create(p_255585_ -> p_255585_.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(p_255584_ -> p_255584_.f_62137_), NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(p_224278_ -> p_224278_.settings)).apply(p_255585_, p_255585_.stable(NoiseBasedChunkGenerator::new)));

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    private final Holder<NoiseGeneratorSettings> settings;

    private final Supplier<Aquifer.FluidPicker> globalFluidPicker;

    public NoiseBasedChunkGenerator(BiomeSource biomeSource0, Holder<NoiseGeneratorSettings> holderNoiseGeneratorSettings1) {
        super(biomeSource0);
        this.settings = holderNoiseGeneratorSettings1;
        this.globalFluidPicker = Suppliers.memoize(() -> createFluidPicker(holderNoiseGeneratorSettings1.value()));
    }

    private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings noiseGeneratorSettings0) {
        Aquifer.FluidStatus $$1 = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
        int $$2 = noiseGeneratorSettings0.seaLevel();
        Aquifer.FluidStatus $$3 = new Aquifer.FluidStatus($$2, noiseGeneratorSettings0.defaultFluid());
        Aquifer.FluidStatus $$4 = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
        return (p_224274_, p_224275_, p_224276_) -> p_224275_ < Math.min(-54, $$2) ? $$1 : $$3;
    }

    @Override
    public CompletableFuture<ChunkAccess> createBiomes(Executor executor0, RandomState randomState1, Blender blender2, StructureManager structureManager3, ChunkAccess chunkAccess4) {
        return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("init_biomes", (Supplier) (() -> {
            this.doCreateBiomes(blender2, randomState1, structureManager3, chunkAccess4);
            return chunkAccess4;
        })), Util.backgroundExecutor());
    }

    private void doCreateBiomes(Blender blender0, RandomState randomState1, StructureManager structureManager2, ChunkAccess chunkAccess3) {
        NoiseChunk $$4 = chunkAccess3.getOrCreateNoiseChunk(p_224340_ -> this.createNoiseChunk(p_224340_, structureManager2, blender0, randomState1));
        BiomeResolver $$5 = BelowZeroRetrogen.getBiomeResolver(blender0.getBiomeResolver(this.f_62137_), chunkAccess3);
        chunkAccess3.fillBiomesFromNoise($$5, $$4.cachedClimateSampler(randomState1.router(), this.settings.value().spawnTarget()));
    }

    private NoiseChunk createNoiseChunk(ChunkAccess chunkAccess0, StructureManager structureManager1, Blender blender2, RandomState randomState3) {
        return NoiseChunk.forChunk(chunkAccess0, randomState3, Beardifier.forStructuresInChunk(structureManager1, chunkAccess0.getPos()), this.settings.value(), (Aquifer.FluidPicker) this.globalFluidPicker.get(), blender2);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    public Holder<NoiseGeneratorSettings> generatorSettings() {
        return this.settings;
    }

    public boolean stable(ResourceKey<NoiseGeneratorSettings> resourceKeyNoiseGeneratorSettings0) {
        return this.settings.is(resourceKeyNoiseGeneratorSettings0);
    }

    @Override
    public int getBaseHeight(int int0, int int1, Heightmap.Types heightmapTypes2, LevelHeightAccessor levelHeightAccessor3, RandomState randomState4) {
        return this.iterateNoiseColumn(levelHeightAccessor3, randomState4, int0, int1, null, heightmapTypes2.isOpaque()).orElse(levelHeightAccessor3.getMinBuildHeight());
    }

    @Override
    public NoiseColumn getBaseColumn(int int0, int int1, LevelHeightAccessor levelHeightAccessor2, RandomState randomState3) {
        MutableObject<NoiseColumn> $$4 = new MutableObject();
        this.iterateNoiseColumn(levelHeightAccessor2, randomState3, int0, int1, $$4, null);
        return (NoiseColumn) $$4.getValue();
    }

    @Override
    public void addDebugScreenInfo(List<String> listString0, RandomState randomState1, BlockPos blockPos2) {
        DecimalFormat $$3 = new DecimalFormat("0.000");
        NoiseRouter $$4 = randomState1.router();
        DensityFunction.SinglePointContext $$5 = new DensityFunction.SinglePointContext(blockPos2.m_123341_(), blockPos2.m_123342_(), blockPos2.m_123343_());
        double $$6 = $$4.ridges().compute($$5);
        listString0.add("NoiseRouter T: " + $$3.format($$4.temperature().compute($$5)) + " V: " + $$3.format($$4.vegetation().compute($$5)) + " C: " + $$3.format($$4.continents().compute($$5)) + " E: " + $$3.format($$4.erosion().compute($$5)) + " D: " + $$3.format($$4.depth().compute($$5)) + " W: " + $$3.format($$6) + " PV: " + $$3.format((double) NoiseRouterData.peaksAndValleys((float) $$6)) + " AS: " + $$3.format($$4.initialDensityWithoutJaggedness().compute($$5)) + " N: " + $$3.format($$4.finalDensity().compute($$5)));
    }

    private OptionalInt iterateNoiseColumn(LevelHeightAccessor levelHeightAccessor0, RandomState randomState1, int int2, int int3, @Nullable MutableObject<NoiseColumn> mutableObjectNoiseColumn4, @Nullable Predicate<BlockState> predicateBlockState5) {
        NoiseSettings $$6 = this.settings.value().noiseSettings().clampToHeightAccessor(levelHeightAccessor0);
        int $$7 = $$6.getCellHeight();
        int $$8 = $$6.minY();
        int $$9 = Mth.floorDiv($$8, $$7);
        int $$10 = Mth.floorDiv($$6.height(), $$7);
        if ($$10 <= 0) {
            return OptionalInt.empty();
        } else {
            BlockState[] $$11;
            if (mutableObjectNoiseColumn4 == null) {
                $$11 = null;
            } else {
                $$11 = new BlockState[$$6.height()];
                mutableObjectNoiseColumn4.setValue(new NoiseColumn($$8, $$11));
            }
            int $$13 = $$6.getCellWidth();
            int $$14 = Math.floorDiv(int2, $$13);
            int $$15 = Math.floorDiv(int3, $$13);
            int $$16 = Math.floorMod(int2, $$13);
            int $$17 = Math.floorMod(int3, $$13);
            int $$18 = $$14 * $$13;
            int $$19 = $$15 * $$13;
            double $$20 = (double) $$16 / (double) $$13;
            double $$21 = (double) $$17 / (double) $$13;
            NoiseChunk $$22 = new NoiseChunk(1, randomState1, $$18, $$19, $$6, DensityFunctions.BeardifierMarker.INSTANCE, this.settings.value(), (Aquifer.FluidPicker) this.globalFluidPicker.get(), Blender.empty());
            $$22.initializeForFirstCellX();
            $$22.advanceCellX(0);
            for (int $$23 = $$10 - 1; $$23 >= 0; $$23--) {
                $$22.selectCellYZ($$23, 0);
                for (int $$24 = $$7 - 1; $$24 >= 0; $$24--) {
                    int $$25 = ($$9 + $$23) * $$7 + $$24;
                    double $$26 = (double) $$24 / (double) $$7;
                    $$22.updateForY($$25, $$26);
                    $$22.updateForX(int2, $$20);
                    $$22.updateForZ(int3, $$21);
                    BlockState $$27 = $$22.getInterpolatedState();
                    BlockState $$28 = $$27 == null ? this.settings.value().defaultBlock() : $$27;
                    if ($$11 != null) {
                        int $$29 = $$23 * $$7 + $$24;
                        $$11[$$29] = $$28;
                    }
                    if (predicateBlockState5 != null && predicateBlockState5.test($$28)) {
                        $$22.stopInterpolation();
                        return OptionalInt.of($$25 + 1);
                    }
                }
            }
            $$22.stopInterpolation();
            return OptionalInt.empty();
        }
    }

    @Override
    public void buildSurface(WorldGenRegion worldGenRegion0, StructureManager structureManager1, RandomState randomState2, ChunkAccess chunkAccess3) {
        if (!SharedConstants.debugVoidTerrain(chunkAccess3.getPos())) {
            WorldGenerationContext $$4 = new WorldGenerationContext(this, worldGenRegion0);
            this.buildSurface(chunkAccess3, $$4, randomState2, structureManager1, worldGenRegion0.getBiomeManager(), worldGenRegion0.registryAccess().registryOrThrow(Registries.BIOME), Blender.of(worldGenRegion0));
        }
    }

    @VisibleForTesting
    public void buildSurface(ChunkAccess chunkAccess0, WorldGenerationContext worldGenerationContext1, RandomState randomState2, StructureManager structureManager3, BiomeManager biomeManager4, Registry<Biome> registryBiome5, Blender blender6) {
        NoiseChunk $$7 = chunkAccess0.getOrCreateNoiseChunk(p_224321_ -> this.createNoiseChunk(p_224321_, structureManager3, blender6, randomState2));
        NoiseGeneratorSettings $$8 = this.settings.value();
        randomState2.surfaceSystem().buildSurface(randomState2, biomeManager4, registryBiome5, $$8.useLegacyRandomSource(), worldGenerationContext1, chunkAccess0, $$7, $$8.surfaceRule());
    }

    @Override
    public void applyCarvers(WorldGenRegion worldGenRegion0, long long1, RandomState randomState2, BiomeManager biomeManager3, StructureManager structureManager4, ChunkAccess chunkAccess5, GenerationStep.Carving generationStepCarving6) {
        BiomeManager $$7 = biomeManager3.withDifferentSource((p_255581_, p_255582_, p_255583_) -> this.f_62137_.getNoiseBiome(p_255581_, p_255582_, p_255583_, randomState2.sampler()));
        WorldgenRandom $$8 = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
        int $$9 = 8;
        ChunkPos $$10 = chunkAccess5.getPos();
        NoiseChunk $$11 = chunkAccess5.getOrCreateNoiseChunk(p_224250_ -> this.createNoiseChunk(p_224250_, structureManager4, Blender.of(worldGenRegion0), randomState2));
        Aquifer $$12 = $$11.aquifer();
        CarvingContext $$13 = new CarvingContext(this, worldGenRegion0.registryAccess(), chunkAccess5.getHeightAccessorForGeneration(), $$11, randomState2, this.settings.value().surfaceRule());
        CarvingMask $$14 = ((ProtoChunk) chunkAccess5).getOrCreateCarvingMask(generationStepCarving6);
        for (int $$15 = -8; $$15 <= 8; $$15++) {
            for (int $$16 = -8; $$16 <= 8; $$16++) {
                ChunkPos $$17 = new ChunkPos($$10.x + $$15, $$10.z + $$16);
                ChunkAccess $$18 = worldGenRegion0.getChunk($$17.x, $$17.z);
                BiomeGenerationSettings $$19 = $$18.carverBiome(() -> this.m_223131_(this.f_62137_.getNoiseBiome(QuartPos.fromBlock($$17.getMinBlockX()), 0, QuartPos.fromBlock($$17.getMinBlockZ()), randomState2.sampler())));
                Iterable<Holder<ConfiguredWorldCarver<?>>> $$20 = $$19.getCarvers(generationStepCarving6);
                int $$21 = 0;
                for (Holder<ConfiguredWorldCarver<?>> $$22 : $$20) {
                    ConfiguredWorldCarver<?> $$23 = $$22.value();
                    $$8.setLargeFeatureSeed(long1 + (long) $$21, $$17.x, $$17.z);
                    if ($$23.isStartChunk($$8)) {
                        $$23.carve($$13, chunkAccess5, $$7::m_204214_, $$8, $$12, $$17, $$14);
                    }
                    $$21++;
                }
            }
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor0, Blender blender1, RandomState randomState2, StructureManager structureManager3, ChunkAccess chunkAccess4) {
        NoiseSettings $$5 = this.settings.value().noiseSettings().clampToHeightAccessor(chunkAccess4.getHeightAccessorForGeneration());
        int $$6 = $$5.minY();
        int $$7 = Mth.floorDiv($$6, $$5.getCellHeight());
        int $$8 = Mth.floorDiv($$5.height(), $$5.getCellHeight());
        if ($$8 <= 0) {
            return CompletableFuture.completedFuture(chunkAccess4);
        } else {
            int $$9 = chunkAccess4.m_151564_($$8 * $$5.getCellHeight() - 1 + $$6);
            int $$10 = chunkAccess4.m_151564_($$6);
            Set<LevelChunkSection> $$11 = Sets.newHashSet();
            for (int $$12 = $$9; $$12 >= $$10; $$12--) {
                LevelChunkSection $$13 = chunkAccess4.getSection($$12);
                $$13.acquire();
                $$11.add($$13);
            }
            return CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", (Supplier) (() -> this.doFill(blender1, structureManager3, randomState2, chunkAccess4, $$7, $$8))), Util.backgroundExecutor()).whenCompleteAsync((p_224309_, p_224310_) -> {
                for (LevelChunkSection $$3 : $$11) {
                    $$3.release();
                }
            }, executor0);
        }
    }

    private ChunkAccess doFill(Blender blender0, StructureManager structureManager1, RandomState randomState2, ChunkAccess chunkAccess3, int int4, int int5) {
        NoiseChunk $$6 = chunkAccess3.getOrCreateNoiseChunk(p_224255_ -> this.createNoiseChunk(p_224255_, structureManager1, blender0, randomState2));
        Heightmap $$7 = chunkAccess3.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap $$8 = chunkAccess3.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        ChunkPos $$9 = chunkAccess3.getPos();
        int $$10 = $$9.getMinBlockX();
        int $$11 = $$9.getMinBlockZ();
        Aquifer $$12 = $$6.aquifer();
        $$6.initializeForFirstCellX();
        BlockPos.MutableBlockPos $$13 = new BlockPos.MutableBlockPos();
        int $$14 = $$6.cellWidth();
        int $$15 = $$6.cellHeight();
        int $$16 = 16 / $$14;
        int $$17 = 16 / $$14;
        for (int $$18 = 0; $$18 < $$16; $$18++) {
            $$6.advanceCellX($$18);
            for (int $$19 = 0; $$19 < $$17; $$19++) {
                int $$20 = chunkAccess3.m_151559_() - 1;
                LevelChunkSection $$21 = chunkAccess3.getSection($$20);
                for (int $$22 = int5 - 1; $$22 >= 0; $$22--) {
                    $$6.selectCellYZ($$22, $$19);
                    for (int $$23 = $$15 - 1; $$23 >= 0; $$23--) {
                        int $$24 = (int4 + $$22) * $$15 + $$23;
                        int $$25 = $$24 & 15;
                        int $$26 = chunkAccess3.m_151564_($$24);
                        if ($$20 != $$26) {
                            $$20 = $$26;
                            $$21 = chunkAccess3.getSection($$26);
                        }
                        double $$27 = (double) $$23 / (double) $$15;
                        $$6.updateForY($$24, $$27);
                        for (int $$28 = 0; $$28 < $$14; $$28++) {
                            int $$29 = $$10 + $$18 * $$14 + $$28;
                            int $$30 = $$29 & 15;
                            double $$31 = (double) $$28 / (double) $$14;
                            $$6.updateForX($$29, $$31);
                            for (int $$32 = 0; $$32 < $$14; $$32++) {
                                int $$33 = $$11 + $$19 * $$14 + $$32;
                                int $$34 = $$33 & 15;
                                double $$35 = (double) $$32 / (double) $$14;
                                $$6.updateForZ($$33, $$35);
                                BlockState $$36 = $$6.getInterpolatedState();
                                if ($$36 == null) {
                                    $$36 = this.settings.value().defaultBlock();
                                }
                                $$36 = this.debugPreliminarySurfaceLevel($$6, $$29, $$24, $$33, $$36);
                                if ($$36 != AIR && !SharedConstants.debugVoidTerrain(chunkAccess3.getPos())) {
                                    $$21.setBlockState($$30, $$25, $$34, $$36, false);
                                    $$7.update($$30, $$24, $$34, $$36);
                                    $$8.update($$30, $$24, $$34, $$36);
                                    if ($$12.shouldScheduleFluidUpdate() && !$$36.m_60819_().isEmpty()) {
                                        $$13.set($$29, $$24, $$33);
                                        chunkAccess3.markPosForPostprocessing($$13);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            $$6.swapSlices();
        }
        $$6.stopInterpolation();
        return chunkAccess3;
    }

    private BlockState debugPreliminarySurfaceLevel(NoiseChunk noiseChunk0, int int1, int int2, int int3, BlockState blockState4) {
        return blockState4;
    }

    @Override
    public int getGenDepth() {
        return this.settings.value().noiseSettings().height();
    }

    @Override
    public int getSeaLevel() {
        return this.settings.value().seaLevel();
    }

    @Override
    public int getMinY() {
        return this.settings.value().noiseSettings().minY();
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion worldGenRegion0) {
        if (!this.settings.value().disableMobGeneration()) {
            ChunkPos $$1 = worldGenRegion0.getCenter();
            Holder<Biome> $$2 = worldGenRegion0.m_204166_($$1.getWorldPosition().atY(worldGenRegion0.m_151558_() - 1));
            WorldgenRandom $$3 = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            $$3.setDecorationSeed(worldGenRegion0.getSeed(), $$1.getMinBlockX(), $$1.getMinBlockZ());
            NaturalSpawner.spawnMobsForChunkGeneration(worldGenRegion0, $$2, $$1, $$3);
        }
    }
}