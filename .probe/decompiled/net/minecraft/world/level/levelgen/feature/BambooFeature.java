package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;

public class BambooFeature extends Feature<ProbabilityFeatureConfiguration> {

    private static final BlockState BAMBOO_TRUNK = (BlockState) ((BlockState) ((BlockState) Blocks.BAMBOO.defaultBlockState().m_61124_(BambooStalkBlock.AGE, 1)).m_61124_(BambooStalkBlock.LEAVES, BambooLeaves.NONE)).m_61124_(BambooStalkBlock.STAGE, 0);

    private static final BlockState BAMBOO_FINAL_LARGE = (BlockState) ((BlockState) BAMBOO_TRUNK.m_61124_(BambooStalkBlock.LEAVES, BambooLeaves.LARGE)).m_61124_(BambooStalkBlock.STAGE, 1);

    private static final BlockState BAMBOO_TOP_LARGE = (BlockState) BAMBOO_TRUNK.m_61124_(BambooStalkBlock.LEAVES, BambooLeaves.LARGE);

    private static final BlockState BAMBOO_TOP_SMALL = (BlockState) BAMBOO_TRUNK.m_61124_(BambooStalkBlock.LEAVES, BambooLeaves.SMALL);

    public BambooFeature(Codec<ProbabilityFeatureConfiguration> codecProbabilityFeatureConfiguration0) {
        super(codecProbabilityFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityFeatureConfiguration> featurePlaceContextProbabilityFeatureConfiguration0) {
        int $$1 = 0;
        BlockPos $$2 = featurePlaceContextProbabilityFeatureConfiguration0.origin();
        WorldGenLevel $$3 = featurePlaceContextProbabilityFeatureConfiguration0.level();
        RandomSource $$4 = featurePlaceContextProbabilityFeatureConfiguration0.random();
        ProbabilityFeatureConfiguration $$5 = featurePlaceContextProbabilityFeatureConfiguration0.config();
        BlockPos.MutableBlockPos $$6 = $$2.mutable();
        BlockPos.MutableBlockPos $$7 = $$2.mutable();
        if ($$3.m_46859_($$6)) {
            if (Blocks.BAMBOO.defaultBlockState().m_60710_($$3, $$6)) {
                int $$8 = $$4.nextInt(12) + 5;
                if ($$4.nextFloat() < $$5.probability) {
                    int $$9 = $$4.nextInt(4) + 1;
                    for (int $$10 = $$2.m_123341_() - $$9; $$10 <= $$2.m_123341_() + $$9; $$10++) {
                        for (int $$11 = $$2.m_123343_() - $$9; $$11 <= $$2.m_123343_() + $$9; $$11++) {
                            int $$12 = $$10 - $$2.m_123341_();
                            int $$13 = $$11 - $$2.m_123343_();
                            if ($$12 * $$12 + $$13 * $$13 <= $$9 * $$9) {
                                $$7.set($$10, $$3.m_6924_(Heightmap.Types.WORLD_SURFACE, $$10, $$11) - 1, $$11);
                                if (m_159759_($$3.m_8055_($$7))) {
                                    $$3.m_7731_($$7, Blocks.PODZOL.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }
                for (int $$14 = 0; $$14 < $$8 && $$3.m_46859_($$6); $$14++) {
                    $$3.m_7731_($$6, BAMBOO_TRUNK, 2);
                    $$6.move(Direction.UP, 1);
                }
                if ($$6.m_123342_() - $$2.m_123342_() >= 3) {
                    $$3.m_7731_($$6, BAMBOO_FINAL_LARGE, 2);
                    $$3.m_7731_($$6.move(Direction.DOWN, 1), BAMBOO_TOP_LARGE, 2);
                    $$3.m_7731_($$6.move(Direction.DOWN, 1), BAMBOO_TOP_SMALL, 2);
                }
            }
            $$1++;
        }
        return $$1 > 0;
    }
}