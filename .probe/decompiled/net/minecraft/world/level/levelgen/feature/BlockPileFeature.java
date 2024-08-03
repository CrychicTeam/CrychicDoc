package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;

public class BlockPileFeature extends Feature<BlockPileConfiguration> {

    public BlockPileFeature(Codec<BlockPileConfiguration> codecBlockPileConfiguration0) {
        super(codecBlockPileConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockPileConfiguration> featurePlaceContextBlockPileConfiguration0) {
        BlockPos $$1 = featurePlaceContextBlockPileConfiguration0.origin();
        WorldGenLevel $$2 = featurePlaceContextBlockPileConfiguration0.level();
        RandomSource $$3 = featurePlaceContextBlockPileConfiguration0.random();
        BlockPileConfiguration $$4 = featurePlaceContextBlockPileConfiguration0.config();
        if ($$1.m_123342_() < $$2.m_141937_() + 5) {
            return false;
        } else {
            int $$5 = 2 + $$3.nextInt(2);
            int $$6 = 2 + $$3.nextInt(2);
            for (BlockPos $$7 : BlockPos.betweenClosed($$1.offset(-$$5, 0, -$$6), $$1.offset($$5, 1, $$6))) {
                int $$8 = $$1.m_123341_() - $$7.m_123341_();
                int $$9 = $$1.m_123343_() - $$7.m_123343_();
                if ((float) ($$8 * $$8 + $$9 * $$9) <= $$3.nextFloat() * 10.0F - $$3.nextFloat() * 6.0F) {
                    this.tryPlaceBlock($$2, $$7, $$3, $$4);
                } else if ((double) $$3.nextFloat() < 0.031) {
                    this.tryPlaceBlock($$2, $$7, $$3, $$4);
                }
            }
            return true;
        }
    }

    private boolean mayPlaceOn(LevelAccessor levelAccessor0, BlockPos blockPos1, RandomSource randomSource2) {
        BlockPos $$3 = blockPos1.below();
        BlockState $$4 = levelAccessor0.m_8055_($$3);
        return $$4.m_60713_(Blocks.DIRT_PATH) ? randomSource2.nextBoolean() : $$4.m_60783_(levelAccessor0, $$3, Direction.UP);
    }

    private void tryPlaceBlock(LevelAccessor levelAccessor0, BlockPos blockPos1, RandomSource randomSource2, BlockPileConfiguration blockPileConfiguration3) {
        if (levelAccessor0.m_46859_(blockPos1) && this.mayPlaceOn(levelAccessor0, blockPos1, randomSource2)) {
            levelAccessor0.m_7731_(blockPos1, blockPileConfiguration3.stateProvider.getState(randomSource2, blockPos1), 4);
        }
    }
}