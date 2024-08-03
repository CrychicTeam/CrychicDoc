package com.mna.blocks.artifice;

import com.mna.blocks.utility.BlockWithOffset;
import com.mna.tools.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class RunicTorchBlock extends BlockWithOffset {

    public RunicTorchBlock(BlockBehaviour.Properties properties) {
        super(properties, new BlockPos(0, 1, 0));
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        for (int i = 0; i < 15; i++) {
            worldIn.addParticle(ParticleTypes.FLAME, (double) ((float) pos.m_123341_() + MathUtils.RandomBetween(0.4F, 0.6F)), (double) (pos.m_123342_() + 2), (double) ((float) pos.m_123343_() + MathUtils.RandomBetween(0.4F, 0.6F)), 0.0, (double) MathUtils.RandomBetween(0.0F, 0.02F), 0.0);
        }
        if (rand.nextInt(10) == 0) {
            worldIn.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
        }
    }
}