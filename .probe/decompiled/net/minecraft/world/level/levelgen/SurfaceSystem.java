package net.minecraft.world.level.levelgen;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BlockColumn;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class SurfaceSystem {

    private static final BlockState WHITE_TERRACOTTA = Blocks.WHITE_TERRACOTTA.defaultBlockState();

    private static final BlockState ORANGE_TERRACOTTA = Blocks.ORANGE_TERRACOTTA.defaultBlockState();

    private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();

    private static final BlockState YELLOW_TERRACOTTA = Blocks.YELLOW_TERRACOTTA.defaultBlockState();

    private static final BlockState BROWN_TERRACOTTA = Blocks.BROWN_TERRACOTTA.defaultBlockState();

    private static final BlockState RED_TERRACOTTA = Blocks.RED_TERRACOTTA.defaultBlockState();

    private static final BlockState LIGHT_GRAY_TERRACOTTA = Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();

    private static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();

    private static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();

    private final BlockState defaultBlock;

    private final int seaLevel;

    private final BlockState[] clayBands;

    private final NormalNoise clayBandsOffsetNoise;

    private final NormalNoise badlandsPillarNoise;

    private final NormalNoise badlandsPillarRoofNoise;

    private final NormalNoise badlandsSurfaceNoise;

    private final NormalNoise icebergPillarNoise;

    private final NormalNoise icebergPillarRoofNoise;

    private final NormalNoise icebergSurfaceNoise;

    private final PositionalRandomFactory noiseRandom;

    private final NormalNoise surfaceNoise;

    private final NormalNoise surfaceSecondaryNoise;

    public SurfaceSystem(RandomState randomState0, BlockState blockState1, int int2, PositionalRandomFactory positionalRandomFactory3) {
        this.defaultBlock = blockState1;
        this.seaLevel = int2;
        this.noiseRandom = positionalRandomFactory3;
        this.clayBandsOffsetNoise = randomState0.getOrCreateNoise(Noises.CLAY_BANDS_OFFSET);
        this.clayBands = generateBands(positionalRandomFactory3.fromHashOf(new ResourceLocation("clay_bands")));
        this.surfaceNoise = randomState0.getOrCreateNoise(Noises.SURFACE);
        this.surfaceSecondaryNoise = randomState0.getOrCreateNoise(Noises.SURFACE_SECONDARY);
        this.badlandsPillarNoise = randomState0.getOrCreateNoise(Noises.BADLANDS_PILLAR);
        this.badlandsPillarRoofNoise = randomState0.getOrCreateNoise(Noises.BADLANDS_PILLAR_ROOF);
        this.badlandsSurfaceNoise = randomState0.getOrCreateNoise(Noises.BADLANDS_SURFACE);
        this.icebergPillarNoise = randomState0.getOrCreateNoise(Noises.ICEBERG_PILLAR);
        this.icebergPillarRoofNoise = randomState0.getOrCreateNoise(Noises.ICEBERG_PILLAR_ROOF);
        this.icebergSurfaceNoise = randomState0.getOrCreateNoise(Noises.ICEBERG_SURFACE);
    }

    public void buildSurface(RandomState randomState0, BiomeManager biomeManager1, Registry<Biome> registryBiome2, boolean boolean3, WorldGenerationContext worldGenerationContext4, final ChunkAccess chunkAccess5, NoiseChunk noiseChunk6, SurfaceRules.RuleSource surfaceRulesRuleSource7) {
        final BlockPos.MutableBlockPos $$8 = new BlockPos.MutableBlockPos();
        final ChunkPos $$9 = chunkAccess5.getPos();
        int $$10 = $$9.getMinBlockX();
        int $$11 = $$9.getMinBlockZ();
        BlockColumn $$12 = new BlockColumn() {

            @Override
            public BlockState getBlock(int p_190006_) {
                return chunkAccess5.m_8055_($$8.setY(p_190006_));
            }

            @Override
            public void setBlock(int p_190008_, BlockState p_190009_) {
                LevelHeightAccessor $$2 = chunkAccess5.getHeightAccessorForGeneration();
                if (p_190008_ >= $$2.getMinBuildHeight() && p_190008_ < $$2.getMaxBuildHeight()) {
                    chunkAccess5.setBlockState($$8.setY(p_190008_), p_190009_, false);
                    if (!p_190009_.m_60819_().isEmpty()) {
                        chunkAccess5.markPosForPostprocessing($$8);
                    }
                }
            }

            public String toString() {
                return "ChunkBlockColumn " + $$9;
            }
        };
        SurfaceRules.Context $$13 = new SurfaceRules.Context(this, randomState0, chunkAccess5, noiseChunk6, biomeManager1::m_204214_, registryBiome2, worldGenerationContext4);
        SurfaceRules.SurfaceRule $$14 = (SurfaceRules.SurfaceRule) surfaceRulesRuleSource7.apply($$13);
        BlockPos.MutableBlockPos $$15 = new BlockPos.MutableBlockPos();
        for (int $$16 = 0; $$16 < 16; $$16++) {
            for (int $$17 = 0; $$17 < 16; $$17++) {
                int $$18 = $$10 + $$16;
                int $$19 = $$11 + $$17;
                int $$20 = chunkAccess5.getHeight(Heightmap.Types.WORLD_SURFACE_WG, $$16, $$17) + 1;
                $$8.setX($$18).setZ($$19);
                Holder<Biome> $$21 = biomeManager1.getBiome($$15.set($$18, boolean3 ? 0 : $$20, $$19));
                if ($$21.is(Biomes.ERODED_BADLANDS)) {
                    this.erodedBadlandsExtension($$12, $$18, $$19, $$20, chunkAccess5);
                }
                int $$22 = chunkAccess5.getHeight(Heightmap.Types.WORLD_SURFACE_WG, $$16, $$17) + 1;
                $$13.updateXZ($$18, $$19);
                int $$23 = 0;
                int $$24 = Integer.MIN_VALUE;
                int $$25 = Integer.MAX_VALUE;
                int $$26 = chunkAccess5.getMinBuildHeight();
                for (int $$27 = $$22; $$27 >= $$26; $$27--) {
                    BlockState $$28 = $$12.getBlock($$27);
                    if ($$28.m_60795_()) {
                        $$23 = 0;
                        $$24 = Integer.MIN_VALUE;
                    } else if (!$$28.m_60819_().isEmpty()) {
                        if ($$24 == Integer.MIN_VALUE) {
                            $$24 = $$27 + 1;
                        }
                    } else {
                        if ($$25 >= $$27) {
                            $$25 = DimensionType.WAY_BELOW_MIN_Y;
                            for (int $$29 = $$27 - 1; $$29 >= $$26 - 1; $$29--) {
                                BlockState $$30 = $$12.getBlock($$29);
                                if (!this.isStone($$30)) {
                                    $$25 = $$29 + 1;
                                    break;
                                }
                            }
                        }
                        $$23++;
                        int $$31 = $$27 - $$25 + 1;
                        $$13.updateY($$23, $$31, $$24, $$18, $$27, $$19);
                        if ($$28 == this.defaultBlock) {
                            BlockState $$32 = $$14.tryApply($$18, $$27, $$19);
                            if ($$32 != null) {
                                $$12.setBlock($$27, $$32);
                            }
                        }
                    }
                }
                if ($$21.is(Biomes.FROZEN_OCEAN) || $$21.is(Biomes.DEEP_FROZEN_OCEAN)) {
                    this.frozenOceanExtension($$13.getMinSurfaceLevel(), $$21.value(), $$12, $$15, $$18, $$19, $$20);
                }
            }
        }
    }

    protected int getSurfaceDepth(int int0, int int1) {
        double $$2 = this.surfaceNoise.getValue((double) int0, 0.0, (double) int1);
        return (int) ($$2 * 2.75 + 3.0 + this.noiseRandom.at(int0, 0, int1).nextDouble() * 0.25);
    }

    protected double getSurfaceSecondary(int int0, int int1) {
        return this.surfaceSecondaryNoise.getValue((double) int0, 0.0, (double) int1);
    }

    private boolean isStone(BlockState blockState0) {
        return !blockState0.m_60795_() && blockState0.m_60819_().isEmpty();
    }

    @Deprecated
    public Optional<BlockState> topMaterial(SurfaceRules.RuleSource surfaceRulesRuleSource0, CarvingContext carvingContext1, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome2, ChunkAccess chunkAccess3, NoiseChunk noiseChunk4, BlockPos blockPos5, boolean boolean6) {
        SurfaceRules.Context $$7 = new SurfaceRules.Context(this, carvingContext1.randomState(), chunkAccess3, noiseChunk4, functionBlockPosHolderBiome2, carvingContext1.registryAccess().registryOrThrow(Registries.BIOME), carvingContext1);
        SurfaceRules.SurfaceRule $$8 = (SurfaceRules.SurfaceRule) surfaceRulesRuleSource0.apply($$7);
        int $$9 = blockPos5.m_123341_();
        int $$10 = blockPos5.m_123342_();
        int $$11 = blockPos5.m_123343_();
        $$7.updateXZ($$9, $$11);
        $$7.updateY(1, 1, boolean6 ? $$10 + 1 : Integer.MIN_VALUE, $$9, $$10, $$11);
        BlockState $$12 = $$8.tryApply($$9, $$10, $$11);
        return Optional.ofNullable($$12);
    }

    private void erodedBadlandsExtension(BlockColumn blockColumn0, int int1, int int2, int int3, LevelHeightAccessor levelHeightAccessor4) {
        double $$5 = 0.2;
        double $$6 = Math.min(Math.abs(this.badlandsSurfaceNoise.getValue((double) int1, 0.0, (double) int2) * 8.25), this.badlandsPillarNoise.getValue((double) int1 * 0.2, 0.0, (double) int2 * 0.2) * 15.0);
        if (!($$6 <= 0.0)) {
            double $$7 = 0.75;
            double $$8 = 1.5;
            double $$9 = Math.abs(this.badlandsPillarRoofNoise.getValue((double) int1 * 0.75, 0.0, (double) int2 * 0.75) * 1.5);
            double $$10 = 64.0 + Math.min($$6 * $$6 * 2.5, Math.ceil($$9 * 50.0) + 24.0);
            int $$11 = Mth.floor($$10);
            if (int3 <= $$11) {
                for (int $$12 = $$11; $$12 >= levelHeightAccessor4.getMinBuildHeight(); $$12--) {
                    BlockState $$13 = blockColumn0.getBlock($$12);
                    if ($$13.m_60713_(this.defaultBlock.m_60734_())) {
                        break;
                    }
                    if ($$13.m_60713_(Blocks.WATER)) {
                        return;
                    }
                }
                for (int $$14 = $$11; $$14 >= levelHeightAccessor4.getMinBuildHeight() && blockColumn0.getBlock($$14).m_60795_(); $$14--) {
                    blockColumn0.setBlock($$14, this.defaultBlock);
                }
            }
        }
    }

    private void frozenOceanExtension(int int0, Biome biome1, BlockColumn blockColumn2, BlockPos.MutableBlockPos blockPosMutableBlockPos3, int int4, int int5, int int6) {
        double $$7 = 1.28;
        double $$8 = Math.min(Math.abs(this.icebergSurfaceNoise.getValue((double) int4, 0.0, (double) int5) * 8.25), this.icebergPillarNoise.getValue((double) int4 * 1.28, 0.0, (double) int5 * 1.28) * 15.0);
        if (!($$8 <= 1.8)) {
            double $$9 = 1.17;
            double $$10 = 1.5;
            double $$11 = Math.abs(this.icebergPillarRoofNoise.getValue((double) int4 * 1.17, 0.0, (double) int5 * 1.17) * 1.5);
            double $$12 = Math.min($$8 * $$8 * 1.2, Math.ceil($$11 * 40.0) + 14.0);
            if (biome1.shouldMeltFrozenOceanIcebergSlightly(blockPosMutableBlockPos3.set(int4, 63, int5))) {
                $$12 -= 2.0;
            }
            double $$13;
            if ($$12 > 2.0) {
                $$13 = (double) this.seaLevel - $$12 - 7.0;
                $$12 += (double) this.seaLevel;
            } else {
                $$12 = 0.0;
                $$13 = 0.0;
            }
            double $$15 = $$12;
            RandomSource $$16 = this.noiseRandom.at(int4, 0, int5);
            int $$17 = 2 + $$16.nextInt(4);
            int $$18 = this.seaLevel + 18 + $$16.nextInt(10);
            int $$19 = 0;
            for (int $$20 = Math.max(int6, (int) $$12 + 1); $$20 >= int0; $$20--) {
                if (blockColumn2.getBlock($$20).m_60795_() && $$20 < (int) $$15 && $$16.nextDouble() > 0.01 || blockColumn2.getBlock($$20).m_60713_(Blocks.WATER) && $$20 > (int) $$13 && $$20 < this.seaLevel && $$13 != 0.0 && $$16.nextDouble() > 0.15) {
                    if ($$19 <= $$17 && $$20 > $$18) {
                        blockColumn2.setBlock($$20, SNOW_BLOCK);
                        $$19++;
                    } else {
                        blockColumn2.setBlock($$20, PACKED_ICE);
                    }
                }
            }
        }
    }

    private static BlockState[] generateBands(RandomSource randomSource0) {
        BlockState[] $$1 = new BlockState[192];
        Arrays.fill($$1, TERRACOTTA);
        for (int $$2 = 0; $$2 < $$1.length; $$2++) {
            $$2 += randomSource0.nextInt(5) + 1;
            if ($$2 < $$1.length) {
                $$1[$$2] = ORANGE_TERRACOTTA;
            }
        }
        makeBands(randomSource0, $$1, 1, YELLOW_TERRACOTTA);
        makeBands(randomSource0, $$1, 2, BROWN_TERRACOTTA);
        makeBands(randomSource0, $$1, 1, RED_TERRACOTTA);
        int $$3 = randomSource0.nextIntBetweenInclusive(9, 15);
        int $$4 = 0;
        for (int $$5 = 0; $$4 < $$3 && $$5 < $$1.length; $$5 += randomSource0.nextInt(16) + 4) {
            $$1[$$5] = WHITE_TERRACOTTA;
            if ($$5 - 1 > 0 && randomSource0.nextBoolean()) {
                $$1[$$5 - 1] = LIGHT_GRAY_TERRACOTTA;
            }
            if ($$5 + 1 < $$1.length && randomSource0.nextBoolean()) {
                $$1[$$5 + 1] = LIGHT_GRAY_TERRACOTTA;
            }
            $$4++;
        }
        return $$1;
    }

    private static void makeBands(RandomSource randomSource0, BlockState[] blockState1, int int2, BlockState blockState3) {
        int $$4 = randomSource0.nextIntBetweenInclusive(6, 15);
        for (int $$5 = 0; $$5 < $$4; $$5++) {
            int $$6 = int2 + randomSource0.nextInt(3);
            int $$7 = randomSource0.nextInt(blockState1.length);
            for (int $$8 = 0; $$7 + $$8 < blockState1.length && $$8 < $$6; $$8++) {
                blockState1[$$7 + $$8] = blockState3;
            }
        }
    }

    protected BlockState getBand(int int0, int int1, int int2) {
        int $$3 = (int) Math.round(this.clayBandsOffsetNoise.getValue((double) int0, 0.0, (double) int2) * 4.0);
        return this.clayBands[(int1 + $$3 + this.clayBands.length) % this.clayBands.length];
    }
}