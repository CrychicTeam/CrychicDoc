package net.minecraft.world.level.levelgen.blending;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap.Builder;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction8;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.data.worldgen.NoiseData;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableObject;

public class Blender {

    private static final Blender EMPTY = new Blender(new Long2ObjectOpenHashMap(), new Long2ObjectOpenHashMap()) {

        @Override
        public Blender.BlendingOutput blendOffsetAndFactor(int p_209724_, int p_209725_) {
            return new Blender.BlendingOutput(1.0, 0.0);
        }

        @Override
        public double blendDensity(DensityFunction.FunctionContext p_209727_, double p_209728_) {
            return p_209728_;
        }

        @Override
        public BiomeResolver getBiomeResolver(BiomeResolver p_190232_) {
            return p_190232_;
        }
    };

    private static final NormalNoise SHIFT_NOISE = NormalNoise.create(new XoroshiroRandomSource(42L), NoiseData.DEFAULT_SHIFT);

    private static final int HEIGHT_BLENDING_RANGE_CELLS = QuartPos.fromSection(7) - 1;

    private static final int HEIGHT_BLENDING_RANGE_CHUNKS = QuartPos.toSection(HEIGHT_BLENDING_RANGE_CELLS + 3);

    private static final int DENSITY_BLENDING_RANGE_CELLS = 2;

    private static final int DENSITY_BLENDING_RANGE_CHUNKS = QuartPos.toSection(5);

    private static final double OLD_CHUNK_XZ_RADIUS = 8.0;

    private final Long2ObjectOpenHashMap<BlendingData> heightAndBiomeBlendingData;

    private final Long2ObjectOpenHashMap<BlendingData> densityBlendingData;

    public static Blender empty() {
        return EMPTY;
    }

    public static Blender of(@Nullable WorldGenRegion worldGenRegion0) {
        if (worldGenRegion0 == null) {
            return EMPTY;
        } else {
            ChunkPos $$1 = worldGenRegion0.getCenter();
            if (!worldGenRegion0.isOldChunkAround($$1, HEIGHT_BLENDING_RANGE_CHUNKS)) {
                return EMPTY;
            } else {
                Long2ObjectOpenHashMap<BlendingData> $$2 = new Long2ObjectOpenHashMap();
                Long2ObjectOpenHashMap<BlendingData> $$3 = new Long2ObjectOpenHashMap();
                int $$4 = Mth.square(HEIGHT_BLENDING_RANGE_CHUNKS + 1);
                for (int $$5 = -HEIGHT_BLENDING_RANGE_CHUNKS; $$5 <= HEIGHT_BLENDING_RANGE_CHUNKS; $$5++) {
                    for (int $$6 = -HEIGHT_BLENDING_RANGE_CHUNKS; $$6 <= HEIGHT_BLENDING_RANGE_CHUNKS; $$6++) {
                        if ($$5 * $$5 + $$6 * $$6 <= $$4) {
                            int $$7 = $$1.x + $$5;
                            int $$8 = $$1.z + $$6;
                            BlendingData $$9 = BlendingData.getOrUpdateBlendingData(worldGenRegion0, $$7, $$8);
                            if ($$9 != null) {
                                $$2.put(ChunkPos.asLong($$7, $$8), $$9);
                                if ($$5 >= -DENSITY_BLENDING_RANGE_CHUNKS && $$5 <= DENSITY_BLENDING_RANGE_CHUNKS && $$6 >= -DENSITY_BLENDING_RANGE_CHUNKS && $$6 <= DENSITY_BLENDING_RANGE_CHUNKS) {
                                    $$3.put(ChunkPos.asLong($$7, $$8), $$9);
                                }
                            }
                        }
                    }
                }
                return $$2.isEmpty() && $$3.isEmpty() ? EMPTY : new Blender($$2, $$3);
            }
        }
    }

    Blender(Long2ObjectOpenHashMap<BlendingData> longObjectOpenHashMapBlendingData0, Long2ObjectOpenHashMap<BlendingData> longObjectOpenHashMapBlendingData1) {
        this.heightAndBiomeBlendingData = longObjectOpenHashMapBlendingData0;
        this.densityBlendingData = longObjectOpenHashMapBlendingData1;
    }

    public Blender.BlendingOutput blendOffsetAndFactor(int int0, int int1) {
        int $$2 = QuartPos.fromBlock(int0);
        int $$3 = QuartPos.fromBlock(int1);
        double $$4 = this.getBlendingDataValue($$2, 0, $$3, BlendingData::m_190285_);
        if ($$4 != Double.MAX_VALUE) {
            return new Blender.BlendingOutput(0.0, heightToOffset($$4));
        } else {
            MutableDouble $$5 = new MutableDouble(0.0);
            MutableDouble $$6 = new MutableDouble(0.0);
            MutableDouble $$7 = new MutableDouble(Double.POSITIVE_INFINITY);
            this.heightAndBiomeBlendingData.forEach((p_202249_, p_202250_) -> p_202250_.iterateHeights(QuartPos.fromSection(ChunkPos.getX(p_202249_)), QuartPos.fromSection(ChunkPos.getZ(p_202249_)), (p_190199_, p_190200_, p_190201_) -> {
                double $$8x = Mth.length((double) ($$2 - p_190199_), (double) ($$3 - p_190200_));
                if (!($$8x > (double) HEIGHT_BLENDING_RANGE_CELLS)) {
                    if ($$8x < $$7.doubleValue()) {
                        $$7.setValue($$8x);
                    }
                    double $$9x = 1.0 / ($$8x * $$8x * $$8x * $$8x);
                    $$6.add(p_190201_ * $$9x);
                    $$5.add($$9x);
                }
            }));
            if ($$7.doubleValue() == Double.POSITIVE_INFINITY) {
                return new Blender.BlendingOutput(1.0, 0.0);
            } else {
                double $$8 = $$6.doubleValue() / $$5.doubleValue();
                double $$9 = Mth.clamp($$7.doubleValue() / (double) (HEIGHT_BLENDING_RANGE_CELLS + 1), 0.0, 1.0);
                $$9 = 3.0 * $$9 * $$9 - 2.0 * $$9 * $$9 * $$9;
                return new Blender.BlendingOutput($$9, heightToOffset($$8));
            }
        }
    }

    private static double heightToOffset(double double0) {
        double $$1 = 1.0;
        double $$2 = double0 + 0.5;
        double $$3 = Mth.positiveModulo($$2, 8.0);
        return 1.0 * (32.0 * ($$2 - 128.0) - 3.0 * ($$2 - 120.0) * $$3 + 3.0 * $$3 * $$3) / (128.0 * (32.0 - 3.0 * $$3));
    }

    public double blendDensity(DensityFunction.FunctionContext densityFunctionFunctionContext0, double double1) {
        int $$2 = QuartPos.fromBlock(densityFunctionFunctionContext0.blockX());
        int $$3 = densityFunctionFunctionContext0.blockY() / 8;
        int $$4 = QuartPos.fromBlock(densityFunctionFunctionContext0.blockZ());
        double $$5 = this.getBlendingDataValue($$2, $$3, $$4, BlendingData::m_190333_);
        if ($$5 != Double.MAX_VALUE) {
            return $$5;
        } else {
            MutableDouble $$6 = new MutableDouble(0.0);
            MutableDouble $$7 = new MutableDouble(0.0);
            MutableDouble $$8 = new MutableDouble(Double.POSITIVE_INFINITY);
            this.densityBlendingData.forEach((p_202241_, p_202242_) -> p_202242_.iterateDensities(QuartPos.fromSection(ChunkPos.getX(p_202241_)), QuartPos.fromSection(ChunkPos.getZ(p_202241_)), $$3 - 1, $$3 + 1, (p_202230_, p_202231_, p_202232_, p_202233_) -> {
                double $$10x = Mth.length((double) ($$2 - p_202230_), (double) (($$3 - p_202231_) * 2), (double) ($$4 - p_202232_));
                if (!($$10x > 2.0)) {
                    if ($$10x < $$8.doubleValue()) {
                        $$8.setValue($$10x);
                    }
                    double $$11 = 1.0 / ($$10x * $$10x * $$10x * $$10x);
                    $$7.add(p_202233_ * $$11);
                    $$6.add($$11);
                }
            }));
            if ($$8.doubleValue() == Double.POSITIVE_INFINITY) {
                return double1;
            } else {
                double $$9 = $$7.doubleValue() / $$6.doubleValue();
                double $$10 = Mth.clamp($$8.doubleValue() / 3.0, 0.0, 1.0);
                return Mth.lerp($$10, $$9, double1);
            }
        }
    }

    private double getBlendingDataValue(int int0, int int1, int int2, Blender.CellValueGetter blenderCellValueGetter3) {
        int $$4 = QuartPos.toSection(int0);
        int $$5 = QuartPos.toSection(int2);
        boolean $$6 = (int0 & 3) == 0;
        boolean $$7 = (int2 & 3) == 0;
        double $$8 = this.getBlendingDataValue(blenderCellValueGetter3, $$4, $$5, int0, int1, int2);
        if ($$8 == Double.MAX_VALUE) {
            if ($$6 && $$7) {
                $$8 = this.getBlendingDataValue(blenderCellValueGetter3, $$4 - 1, $$5 - 1, int0, int1, int2);
            }
            if ($$8 == Double.MAX_VALUE) {
                if ($$6) {
                    $$8 = this.getBlendingDataValue(blenderCellValueGetter3, $$4 - 1, $$5, int0, int1, int2);
                }
                if ($$8 == Double.MAX_VALUE && $$7) {
                    $$8 = this.getBlendingDataValue(blenderCellValueGetter3, $$4, $$5 - 1, int0, int1, int2);
                }
            }
        }
        return $$8;
    }

    private double getBlendingDataValue(Blender.CellValueGetter blenderCellValueGetter0, int int1, int int2, int int3, int int4, int int5) {
        BlendingData $$6 = (BlendingData) this.heightAndBiomeBlendingData.get(ChunkPos.asLong(int1, int2));
        return $$6 != null ? blenderCellValueGetter0.get($$6, int3 - QuartPos.fromSection(int1), int4, int5 - QuartPos.fromSection(int2)) : Double.MAX_VALUE;
    }

    public BiomeResolver getBiomeResolver(BiomeResolver biomeResolver0) {
        return (p_204669_, p_204670_, p_204671_, p_204672_) -> {
            Holder<Biome> $$5 = this.blendBiome(p_204669_, p_204670_, p_204671_);
            return $$5 == null ? biomeResolver0.getNoiseBiome(p_204669_, p_204670_, p_204671_, p_204672_) : $$5;
        };
    }

    @Nullable
    private Holder<Biome> blendBiome(int int0, int int1, int int2) {
        MutableDouble $$3 = new MutableDouble(Double.POSITIVE_INFINITY);
        MutableObject<Holder<Biome>> $$4 = new MutableObject();
        this.heightAndBiomeBlendingData.forEach((p_224716_, p_224717_) -> p_224717_.iterateBiomes(QuartPos.fromSection(ChunkPos.getX(p_224716_)), int1, QuartPos.fromSection(ChunkPos.getZ(p_224716_)), (p_224723_, p_224724_, p_224725_) -> {
            double $$7 = Mth.length((double) (int0 - p_224723_), (double) (int2 - p_224724_));
            if (!($$7 > (double) HEIGHT_BLENDING_RANGE_CELLS)) {
                if ($$7 < $$3.doubleValue()) {
                    $$4.setValue(p_224725_);
                    $$3.setValue($$7);
                }
            }
        }));
        if ($$3.doubleValue() == Double.POSITIVE_INFINITY) {
            return null;
        } else {
            double $$5 = SHIFT_NOISE.getValue((double) int0, 0.0, (double) int2) * 12.0;
            double $$6 = Mth.clamp(($$3.doubleValue() + $$5) / (double) (HEIGHT_BLENDING_RANGE_CELLS + 1), 0.0, 1.0);
            return $$6 > 0.5 ? null : (Holder) $$4.getValue();
        }
    }

    public static void generateBorderTicks(WorldGenRegion worldGenRegion0, ChunkAccess chunkAccess1) {
        ChunkPos $$2 = chunkAccess1.getPos();
        boolean $$3 = chunkAccess1.isOldNoiseGeneration();
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        BlockPos $$5 = new BlockPos($$2.getMinBlockX(), 0, $$2.getMinBlockZ());
        BlendingData $$6 = chunkAccess1.getBlendingData();
        if ($$6 != null) {
            int $$7 = $$6.getAreaWithOldGeneration().getMinBuildHeight();
            int $$8 = $$6.getAreaWithOldGeneration().getMaxBuildHeight() - 1;
            if ($$3) {
                for (int $$9 = 0; $$9 < 16; $$9++) {
                    for (int $$10 = 0; $$10 < 16; $$10++) {
                        generateBorderTick(chunkAccess1, $$4.setWithOffset($$5, $$9, $$7 - 1, $$10));
                        generateBorderTick(chunkAccess1, $$4.setWithOffset($$5, $$9, $$7, $$10));
                        generateBorderTick(chunkAccess1, $$4.setWithOffset($$5, $$9, $$8, $$10));
                        generateBorderTick(chunkAccess1, $$4.setWithOffset($$5, $$9, $$8 + 1, $$10));
                    }
                }
            }
            for (Direction $$11 : Direction.Plane.HORIZONTAL) {
                if (worldGenRegion0.getChunk($$2.x + $$11.getStepX(), $$2.z + $$11.getStepZ()).isOldNoiseGeneration() != $$3) {
                    int $$12 = $$11 == Direction.EAST ? 15 : 0;
                    int $$13 = $$11 == Direction.WEST ? 0 : 15;
                    int $$14 = $$11 == Direction.SOUTH ? 15 : 0;
                    int $$15 = $$11 == Direction.NORTH ? 0 : 15;
                    for (int $$16 = $$12; $$16 <= $$13; $$16++) {
                        for (int $$17 = $$14; $$17 <= $$15; $$17++) {
                            int $$18 = Math.min($$8, chunkAccess1.getHeight(Heightmap.Types.MOTION_BLOCKING, $$16, $$17)) + 1;
                            for (int $$19 = $$7; $$19 < $$18; $$19++) {
                                generateBorderTick(chunkAccess1, $$4.setWithOffset($$5, $$16, $$19, $$17));
                            }
                        }
                    }
                }
            }
        }
    }

    private static void generateBorderTick(ChunkAccess chunkAccess0, BlockPos blockPos1) {
        BlockState $$2 = chunkAccess0.m_8055_(blockPos1);
        if ($$2.m_204336_(BlockTags.LEAVES)) {
            chunkAccess0.markPosForPostprocessing(blockPos1);
        }
        FluidState $$3 = chunkAccess0.m_6425_(blockPos1);
        if (!$$3.isEmpty()) {
            chunkAccess0.markPosForPostprocessing(blockPos1);
        }
    }

    public static void addAroundOldChunksCarvingMaskFilter(WorldGenLevel worldGenLevel0, ProtoChunk protoChunk1) {
        ChunkPos $$2 = protoChunk1.m_7697_();
        Builder<Direction8, BlendingData> $$3 = ImmutableMap.builder();
        for (Direction8 $$4 : Direction8.values()) {
            int $$5 = $$2.x + $$4.getStepX();
            int $$6 = $$2.z + $$4.getStepZ();
            BlendingData $$7 = worldGenLevel0.m_6325_($$5, $$6).getBlendingData();
            if ($$7 != null) {
                $$3.put($$4, $$7);
            }
        }
        ImmutableMap<Direction8, BlendingData> $$8 = $$3.build();
        if (protoChunk1.m_187675_() || !$$8.isEmpty()) {
            Blender.DistanceGetter $$9 = makeOldChunkDistanceGetter(protoChunk1.m_183407_(), $$8);
            CarvingMask.Mask $$10 = (p_202262_, p_202263_, p_202264_) -> {
                double $$4x = (double) p_202262_ + 0.5 + SHIFT_NOISE.getValue((double) p_202262_, (double) p_202263_, (double) p_202264_) * 4.0;
                double $$5x = (double) p_202263_ + 0.5 + SHIFT_NOISE.getValue((double) p_202263_, (double) p_202264_, (double) p_202262_) * 4.0;
                double $$6x = (double) p_202264_ + 0.5 + SHIFT_NOISE.getValue((double) p_202264_, (double) p_202262_, (double) p_202263_) * 4.0;
                return $$9.getDistance($$4x, $$5x, $$6x) < 4.0;
            };
            Stream.of(GenerationStep.Carving.values()).map(protoChunk1::m_183613_).forEach(p_202259_ -> p_202259_.setAdditionalMask($$10));
        }
    }

    public static Blender.DistanceGetter makeOldChunkDistanceGetter(@Nullable BlendingData blendingData0, Map<Direction8, BlendingData> mapDirectionBlendingData1) {
        List<Blender.DistanceGetter> $$2 = Lists.newArrayList();
        if (blendingData0 != null) {
            $$2.add(makeOffsetOldChunkDistanceGetter(null, blendingData0));
        }
        mapDirectionBlendingData1.forEach((p_224734_, p_224735_) -> $$2.add(makeOffsetOldChunkDistanceGetter(p_224734_, p_224735_)));
        return (p_202267_, p_202268_, p_202269_) -> {
            double $$4 = Double.POSITIVE_INFINITY;
            for (Blender.DistanceGetter $$5 : $$2) {
                double $$6 = $$5.getDistance(p_202267_, p_202268_, p_202269_);
                if ($$6 < $$4) {
                    $$4 = $$6;
                }
            }
            return $$4;
        };
    }

    private static Blender.DistanceGetter makeOffsetOldChunkDistanceGetter(@Nullable Direction8 direction0, BlendingData blendingData1) {
        double $$2 = 0.0;
        double $$3 = 0.0;
        if (direction0 != null) {
            for (Direction $$4 : direction0.getDirections()) {
                $$2 += (double) ($$4.getStepX() * 16);
                $$3 += (double) ($$4.getStepZ() * 16);
            }
        }
        double $$5 = $$2;
        double $$6 = $$3;
        double $$7 = (double) blendingData1.getAreaWithOldGeneration().getHeight() / 2.0;
        double $$8 = (double) blendingData1.getAreaWithOldGeneration().getMinBuildHeight() + $$7;
        return (p_224703_, p_224704_, p_224705_) -> distanceToCube(p_224703_ - 8.0 - $$5, p_224704_ - $$8, p_224705_ - 8.0 - $$6, 8.0, $$7, 8.0);
    }

    private static double distanceToCube(double double0, double double1, double double2, double double3, double double4, double double5) {
        double $$6 = Math.abs(double0) - double3;
        double $$7 = Math.abs(double1) - double4;
        double $$8 = Math.abs(double2) - double5;
        return Mth.length(Math.max(0.0, $$6), Math.max(0.0, $$7), Math.max(0.0, $$8));
    }

    public static record BlendingOutput(double f_209729_, double f_209730_) {

        private final double alpha;

        private final double blendingOffset;

        public BlendingOutput(double f_209729_, double f_209730_) {
            this.alpha = f_209729_;
            this.blendingOffset = f_209730_;
        }
    }

    interface CellValueGetter {

        double get(BlendingData var1, int var2, int var3, int var4);
    }

    public interface DistanceGetter {

        double getDistance(double var1, double var3, double var5);
    }
}