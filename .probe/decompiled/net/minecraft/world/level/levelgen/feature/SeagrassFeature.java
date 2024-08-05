package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class SeagrassFeature extends Feature<ProbabilityFeatureConfiguration> {

    public SeagrassFeature(Codec<ProbabilityFeatureConfiguration> codecProbabilityFeatureConfiguration0) {
        super(codecProbabilityFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> featurePlaceContextProbabilityFeatureConfiguration0) {
        boolean $$1 = false;
        RandomSource $$2 = featurePlaceContextProbabilityFeatureConfiguration0.random();
        WorldGenLevel $$3 = featurePlaceContextProbabilityFeatureConfiguration0.level();
        BlockPos $$4 = featurePlaceContextProbabilityFeatureConfiguration0.origin();
        ProbabilityFeatureConfiguration $$5 = featurePlaceContextProbabilityFeatureConfiguration0.config();
        int $$6 = $$2.nextInt(8) - $$2.nextInt(8);
        int $$7 = $$2.nextInt(8) - $$2.nextInt(8);
        int $$8 = $$3.m_6924_(Heightmap.Types.OCEAN_FLOOR, $$4.m_123341_() + $$6, $$4.m_123343_() + $$7);
        BlockPos $$9 = new BlockPos($$4.m_123341_() + $$6, $$8, $$4.m_123343_() + $$7);
        if ($$3.m_8055_($$9).m_60713_(Blocks.WATER)) {
            boolean $$10 = $$2.nextDouble() < (double) $$5.probability;
            BlockState $$11 = $$10 ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
            if ($$11.m_60710_($$3, $$9)) {
                if ($$10) {
                    BlockState $$12 = (BlockState) $$11.m_61124_(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER);
                    BlockPos $$13 = $$9.above();
                    if ($$3.m_8055_($$13).m_60713_(Blocks.WATER)) {
                        $$3.m_7731_($$9, $$11, 2);
                        $$3.m_7731_($$13, $$12, 2);
                    }
                } else {
                    $$3.m_7731_($$9, $$11, 2);
                }
                $$1 = true;
            }
        }
        return $$1;
    }
}