package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

public abstract class SpreadingSnowyDirtBlock extends SnowyDirtBlock {

    protected SpreadingSnowyDirtBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    private static boolean canBeGrass(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.above();
        BlockState $$4 = levelReader1.m_8055_($$3);
        if ($$4.m_60713_(Blocks.SNOW) && (Integer) $$4.m_61143_(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if ($$4.m_60819_().getAmount() == 8) {
            return false;
        } else {
            int $$5 = LightEngine.getLightBlockInto(levelReader1, blockState0, blockPos2, $$4, $$3, Direction.UP, $$4.m_60739_(levelReader1, $$3));
            return $$5 < levelReader1.m_7469_();
        }
    }

    private static boolean canPropagate(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        BlockPos $$3 = blockPos2.above();
        return canBeGrass(blockState0, levelReader1, blockPos2) && !levelReader1.m_6425_($$3).is(FluidTags.WATER);
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if (!canBeGrass(blockState0, serverLevel1, blockPos2)) {
            serverLevel1.m_46597_(blockPos2, Blocks.DIRT.defaultBlockState());
        } else {
            if (serverLevel1.m_46803_(blockPos2.above()) >= 9) {
                BlockState $$4 = this.m_49966_();
                for (int $$5 = 0; $$5 < 4; $$5++) {
                    BlockPos $$6 = blockPos2.offset(randomSource3.nextInt(3) - 1, randomSource3.nextInt(5) - 3, randomSource3.nextInt(3) - 1);
                    if (serverLevel1.m_8055_($$6).m_60713_(Blocks.DIRT) && canPropagate($$4, serverLevel1, $$6)) {
                        serverLevel1.m_46597_($$6, (BlockState) $$4.m_61124_(f_56637_, serverLevel1.m_8055_($$6.above()).m_60713_(Blocks.SNOW)));
                    }
                }
            }
        }
    }
}