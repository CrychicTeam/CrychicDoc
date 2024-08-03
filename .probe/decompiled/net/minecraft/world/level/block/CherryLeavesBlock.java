package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CherryLeavesBlock extends LeavesBlock {

    public CherryLeavesBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        super.animateTick(blockState0, level1, blockPos2, randomSource3);
        if (randomSource3.nextInt(10) == 0) {
            BlockPos $$4 = blockPos2.below();
            BlockState $$5 = level1.getBlockState($$4);
            if (!m_49918_($$5.m_60812_(level1, $$4), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level1, blockPos2, randomSource3, ParticleTypes.CHERRY_LEAVES);
            }
        }
    }
}