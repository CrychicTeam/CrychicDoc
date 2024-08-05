package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;

public class SeaPickleFeature extends Feature<CountConfiguration> {

    public SeaPickleFeature(Codec<CountConfiguration> codecCountConfiguration0) {
        super(codecCountConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<CountConfiguration> featurePlaceContextCountConfiguration0) {
        int $$1 = 0;
        RandomSource $$2 = featurePlaceContextCountConfiguration0.random();
        WorldGenLevel $$3 = featurePlaceContextCountConfiguration0.level();
        BlockPos $$4 = featurePlaceContextCountConfiguration0.origin();
        int $$5 = featurePlaceContextCountConfiguration0.config().count().sample($$2);
        for (int $$6 = 0; $$6 < $$5; $$6++) {
            int $$7 = $$2.nextInt(8) - $$2.nextInt(8);
            int $$8 = $$2.nextInt(8) - $$2.nextInt(8);
            int $$9 = $$3.m_6924_(Heightmap.Types.OCEAN_FLOOR, $$4.m_123341_() + $$7, $$4.m_123343_() + $$8);
            BlockPos $$10 = new BlockPos($$4.m_123341_() + $$7, $$9, $$4.m_123343_() + $$8);
            BlockState $$11 = (BlockState) Blocks.SEA_PICKLE.defaultBlockState().m_61124_(SeaPickleBlock.PICKLES, $$2.nextInt(4) + 1);
            if ($$3.m_8055_($$10).m_60713_(Blocks.WATER) && $$11.m_60710_($$3, $$10)) {
                $$3.m_7731_($$10, $$11, 2);
                $$1++;
            }
        }
        return $$1 > 0;
    }
}