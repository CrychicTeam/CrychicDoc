package com.mna.blocks.artifice;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.utility.BlockWithOffset;
import com.mna.tools.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class RunicLightBlock extends BlockWithOffset {

    public RunicLightBlock(BlockBehaviour.Properties properties) {
        super(properties, new BlockPos(0, 1, 0));
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        for (int i = 0; i < 15; i++) {
            worldIn.addParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_STATIONARY.get()), (double) ((float) pos.m_123341_() + MathUtils.RandomBetween(0.0F, 1.0F)), (double) ((float) pos.m_123342_() + MathUtils.RandomBetween(1.65F, 2.25F)), (double) ((float) pos.m_123343_() + MathUtils.RandomBetween(0.0F, 1.0F)), (double) MathUtils.RandomBetween(-0.02F, 0.02F), (double) MathUtils.RandomBetween(0.0F, 0.02F), (double) MathUtils.RandomBetween(-0.02F, 0.02F));
        }
    }
}