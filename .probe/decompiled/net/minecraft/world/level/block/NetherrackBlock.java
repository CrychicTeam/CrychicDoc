package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class NetherrackBlock extends Block implements BonemealableBlock {

    public NetherrackBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
        if (!levelReader0.m_8055_(blockPos1.above()).m_60631_(levelReader0, blockPos1)) {
            return false;
        } else {
            for (BlockPos $$4 : BlockPos.betweenClosed(blockPos1.offset(-1, -1, -1), blockPos1.offset(1, 1, 1))) {
                if (levelReader0.m_8055_($$4).m_204336_(BlockTags.NYLIUM)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean isBonemealSuccess(Level level0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        boolean $$4 = false;
        boolean $$5 = false;
        for (BlockPos $$6 : BlockPos.betweenClosed(blockPos2.offset(-1, -1, -1), blockPos2.offset(1, 1, 1))) {
            BlockState $$7 = serverLevel0.m_8055_($$6);
            if ($$7.m_60713_(Blocks.WARPED_NYLIUM)) {
                $$5 = true;
            }
            if ($$7.m_60713_(Blocks.CRIMSON_NYLIUM)) {
                $$4 = true;
            }
            if ($$5 && $$4) {
                break;
            }
        }
        if ($$5 && $$4) {
            serverLevel0.m_7731_(blockPos2, randomSource1.nextBoolean() ? Blocks.WARPED_NYLIUM.defaultBlockState() : Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
        } else if ($$5) {
            serverLevel0.m_7731_(blockPos2, Blocks.WARPED_NYLIUM.defaultBlockState(), 3);
        } else if ($$4) {
            serverLevel0.m_7731_(blockPos2, Blocks.CRIMSON_NYLIUM.defaultBlockState(), 3);
        }
    }
}