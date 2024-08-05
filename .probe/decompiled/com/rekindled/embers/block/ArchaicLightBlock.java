package com.rekindled.embers.block;

import com.rekindled.embers.particle.GlowParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ArchaicLightBlock extends Block {

    public static final GlowParticleOptions EMBER = new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, 2.0F, 120);

    public ArchaicLightBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 12; i++) {
            int chance = random.nextInt(3);
            if (chance == 0) {
                level.addParticle(EMBER, (double) ((float) pos.m_123341_() - 0.03125F + 1.0625F * (float) random.nextInt(2)), (double) ((float) pos.m_123342_() + 0.125F + 0.75F * random.nextFloat()), (double) ((float) pos.m_123343_() + 0.125F + 0.75F * random.nextFloat()), (double) ((random.nextFloat() - 0.5F) * 0.03F), (double) (random.nextFloat() * 0.03F), (double) ((random.nextFloat() - 0.5F) * 0.03F));
            } else if (chance == 1) {
                level.addParticle(EMBER, (double) ((float) pos.m_123341_() + 0.125F + 0.75F * random.nextFloat()), (double) ((float) pos.m_123342_() - 0.03125F + 1.0625F * (float) random.nextInt(2)), (double) ((float) pos.m_123343_() + 0.125F + 0.75F * random.nextFloat()), (double) ((random.nextFloat() - 0.5F) * 0.03F), (double) (random.nextFloat() * 0.03F), (double) ((random.nextFloat() - 0.5F) * 0.03F));
            } else if (chance == 2) {
                level.addParticle(EMBER, (double) ((float) pos.m_123341_() + 0.125F + 0.75F * random.nextFloat()), (double) ((float) pos.m_123342_() + 0.125F + 0.75F * random.nextFloat()), (double) ((float) pos.m_123343_() - 0.03125F + 1.0625F * (float) random.nextInt(2)), (double) ((random.nextFloat() - 0.5F) * 0.03F), (double) (random.nextFloat() * 0.03F), (double) ((random.nextFloat() - 0.5F) * 0.03F));
            }
        }
    }
}