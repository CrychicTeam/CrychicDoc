package net.minecraft.world.level.levelgen.feature;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
import net.minecraft.world.phys.AABB;

public class SpikeFeature extends Feature<SpikeConfiguration> {

    public static final int NUMBER_OF_SPIKES = 10;

    private static final int SPIKE_DISTANCE = 42;

    private static final LoadingCache<Long, List<SpikeFeature.EndSpike>> SPIKE_CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(new SpikeFeature.SpikeCacheLoader());

    public SpikeFeature(Codec<SpikeConfiguration> codecSpikeConfiguration0) {
        super(codecSpikeConfiguration0);
    }

    public static List<SpikeFeature.EndSpike> getSpikesForLevel(WorldGenLevel worldGenLevel0) {
        RandomSource $$1 = RandomSource.create(worldGenLevel0.getSeed());
        long $$2 = $$1.nextLong() & 65535L;
        return (List<SpikeFeature.EndSpike>) SPIKE_CACHE.getUnchecked($$2);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpikeConfiguration> featurePlaceContextSpikeConfiguration0) {
        SpikeConfiguration $$1 = featurePlaceContextSpikeConfiguration0.config();
        WorldGenLevel $$2 = featurePlaceContextSpikeConfiguration0.level();
        RandomSource $$3 = featurePlaceContextSpikeConfiguration0.random();
        BlockPos $$4 = featurePlaceContextSpikeConfiguration0.origin();
        List<SpikeFeature.EndSpike> $$5 = $$1.getSpikes();
        if ($$5.isEmpty()) {
            $$5 = getSpikesForLevel($$2);
        }
        for (SpikeFeature.EndSpike $$6 : $$5) {
            if ($$6.isCenterWithinChunk($$4)) {
                this.placeSpike($$2, $$3, $$1, $$6);
            }
        }
        return true;
    }

    private void placeSpike(ServerLevelAccessor serverLevelAccessor0, RandomSource randomSource1, SpikeConfiguration spikeConfiguration2, SpikeFeature.EndSpike spikeFeatureEndSpike3) {
        int $$4 = spikeFeatureEndSpike3.getRadius();
        for (BlockPos $$5 : BlockPos.betweenClosed(new BlockPos(spikeFeatureEndSpike3.getCenterX() - $$4, serverLevelAccessor0.m_141937_(), spikeFeatureEndSpike3.getCenterZ() - $$4), new BlockPos(spikeFeatureEndSpike3.getCenterX() + $$4, spikeFeatureEndSpike3.getHeight() + 10, spikeFeatureEndSpike3.getCenterZ() + $$4))) {
            if ($$5.m_203202_((double) spikeFeatureEndSpike3.getCenterX(), (double) $$5.m_123342_(), (double) spikeFeatureEndSpike3.getCenterZ()) <= (double) ($$4 * $$4 + 1) && $$5.m_123342_() < spikeFeatureEndSpike3.getHeight()) {
                this.m_5974_(serverLevelAccessor0, $$5, Blocks.OBSIDIAN.defaultBlockState());
            } else if ($$5.m_123342_() > 65) {
                this.m_5974_(serverLevelAccessor0, $$5, Blocks.AIR.defaultBlockState());
            }
        }
        if (spikeFeatureEndSpike3.isGuarded()) {
            int $$6 = -2;
            int $$7 = 2;
            int $$8 = 3;
            BlockPos.MutableBlockPos $$9 = new BlockPos.MutableBlockPos();
            for (int $$10 = -2; $$10 <= 2; $$10++) {
                for (int $$11 = -2; $$11 <= 2; $$11++) {
                    for (int $$12 = 0; $$12 <= 3; $$12++) {
                        boolean $$13 = Mth.abs($$10) == 2;
                        boolean $$14 = Mth.abs($$11) == 2;
                        boolean $$15 = $$12 == 3;
                        if ($$13 || $$14 || $$15) {
                            boolean $$16 = $$10 == -2 || $$10 == 2 || $$15;
                            boolean $$17 = $$11 == -2 || $$11 == 2 || $$15;
                            BlockState $$18 = (BlockState) ((BlockState) ((BlockState) ((BlockState) Blocks.IRON_BARS.defaultBlockState().m_61124_(IronBarsBlock.f_52309_, $$16 && $$11 != -2)).m_61124_(IronBarsBlock.f_52311_, $$16 && $$11 != 2)).m_61124_(IronBarsBlock.f_52312_, $$17 && $$10 != -2)).m_61124_(IronBarsBlock.f_52310_, $$17 && $$10 != 2);
                            this.m_5974_(serverLevelAccessor0, $$9.set(spikeFeatureEndSpike3.getCenterX() + $$10, spikeFeatureEndSpike3.getHeight() + $$12, spikeFeatureEndSpike3.getCenterZ() + $$11), $$18);
                        }
                    }
                }
            }
        }
        EndCrystal $$19 = EntityType.END_CRYSTAL.create(serverLevelAccessor0.getLevel());
        if ($$19 != null) {
            $$19.setBeamTarget(spikeConfiguration2.getCrystalBeamTarget());
            $$19.m_20331_(spikeConfiguration2.isCrystalInvulnerable());
            $$19.m_7678_((double) spikeFeatureEndSpike3.getCenterX() + 0.5, (double) (spikeFeatureEndSpike3.getHeight() + 1), (double) spikeFeatureEndSpike3.getCenterZ() + 0.5, randomSource1.nextFloat() * 360.0F, 0.0F);
            serverLevelAccessor0.m_7967_($$19);
            this.m_5974_(serverLevelAccessor0, new BlockPos(spikeFeatureEndSpike3.getCenterX(), spikeFeatureEndSpike3.getHeight(), spikeFeatureEndSpike3.getCenterZ()), Blocks.BEDROCK.defaultBlockState());
        }
    }

    public static class EndSpike {

        public static final Codec<SpikeFeature.EndSpike> CODEC = RecordCodecBuilder.create(p_66890_ -> p_66890_.group(Codec.INT.fieldOf("centerX").orElse(0).forGetter(p_160382_ -> p_160382_.centerX), Codec.INT.fieldOf("centerZ").orElse(0).forGetter(p_160380_ -> p_160380_.centerZ), Codec.INT.fieldOf("radius").orElse(0).forGetter(p_160378_ -> p_160378_.radius), Codec.INT.fieldOf("height").orElse(0).forGetter(p_160376_ -> p_160376_.height), Codec.BOOL.fieldOf("guarded").orElse(false).forGetter(p_160374_ -> p_160374_.guarded)).apply(p_66890_, SpikeFeature.EndSpike::new));

        private final int centerX;

        private final int centerZ;

        private final int radius;

        private final int height;

        private final boolean guarded;

        private final AABB topBoundingBox;

        public EndSpike(int int0, int int1, int int2, int int3, boolean boolean4) {
            this.centerX = int0;
            this.centerZ = int1;
            this.radius = int2;
            this.height = int3;
            this.guarded = boolean4;
            this.topBoundingBox = new AABB((double) (int0 - int2), (double) DimensionType.MIN_Y, (double) (int1 - int2), (double) (int0 + int2), (double) DimensionType.MAX_Y, (double) (int1 + int2));
        }

        public boolean isCenterWithinChunk(BlockPos blockPos0) {
            return SectionPos.blockToSectionCoord(blockPos0.m_123341_()) == SectionPos.blockToSectionCoord(this.centerX) && SectionPos.blockToSectionCoord(blockPos0.m_123343_()) == SectionPos.blockToSectionCoord(this.centerZ);
        }

        public int getCenterX() {
            return this.centerX;
        }

        public int getCenterZ() {
            return this.centerZ;
        }

        public int getRadius() {
            return this.radius;
        }

        public int getHeight() {
            return this.height;
        }

        public boolean isGuarded() {
            return this.guarded;
        }

        public AABB getTopBoundingBox() {
            return this.topBoundingBox;
        }
    }

    static class SpikeCacheLoader extends CacheLoader<Long, List<SpikeFeature.EndSpike>> {

        public List<SpikeFeature.EndSpike> load(Long long0) {
            IntArrayList $$1 = Util.toShuffledList(IntStream.range(0, 10), RandomSource.create(long0));
            List<SpikeFeature.EndSpike> $$2 = Lists.newArrayList();
            for (int $$3 = 0; $$3 < 10; $$3++) {
                int $$4 = Mth.floor(42.0 * Math.cos(2.0 * (-Math.PI + (Math.PI / 10) * (double) $$3)));
                int $$5 = Mth.floor(42.0 * Math.sin(2.0 * (-Math.PI + (Math.PI / 10) * (double) $$3)));
                int $$6 = $$1.get($$3);
                int $$7 = 2 + $$6 / 3;
                int $$8 = 76 + $$6 * 3;
                boolean $$9 = $$6 == 1 || $$6 == 2;
                $$2.add(new SpikeFeature.EndSpike($$4, $$5, $$7, $$8, $$9));
            }
            return $$2;
        }
    }
}