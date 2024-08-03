package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MyceliumBlock extends SpreadingSnowyDirtBlock {

    public MyceliumBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        super.m_214162_(blockState0, level1, blockPos2, randomSource3);
        if (randomSource3.nextInt(10) == 0) {
            level1.addParticle(ParticleTypes.MYCELIUM, (double) blockPos2.m_123341_() + randomSource3.nextDouble(), (double) blockPos2.m_123342_() + 1.1, (double) blockPos2.m_123343_() + randomSource3.nextDouble(), 0.0, 0.0, 0.0);
        }
    }
}