package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class CryingObsidianBlock extends Block {

    public CryingObsidianBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if (randomSource3.nextInt(5) == 0) {
            Direction $$4 = Direction.getRandom(randomSource3);
            if ($$4 != Direction.UP) {
                BlockPos $$5 = blockPos2.relative($$4);
                BlockState $$6 = level1.getBlockState($$5);
                if (!blockState0.m_60815_() || !$$6.m_60783_(level1, $$5, $$4.getOpposite())) {
                    double $$7 = $$4.getStepX() == 0 ? randomSource3.nextDouble() : 0.5 + (double) $$4.getStepX() * 0.6;
                    double $$8 = $$4.getStepY() == 0 ? randomSource3.nextDouble() : 0.5 + (double) $$4.getStepY() * 0.6;
                    double $$9 = $$4.getStepZ() == 0 ? randomSource3.nextDouble() : 0.5 + (double) $$4.getStepZ() * 0.6;
                    level1.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double) blockPos2.m_123341_() + $$7, (double) blockPos2.m_123342_() + $$8, (double) blockPos2.m_123343_() + $$9, 0.0, 0.0, 0.0);
                }
            }
        }
    }
}