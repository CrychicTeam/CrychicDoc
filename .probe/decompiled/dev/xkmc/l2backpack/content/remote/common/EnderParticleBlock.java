package dev.xkmc.l2backpack.content.remote.common;

import dev.xkmc.l2modularblock.mult.AnimateTickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EnderParticleBlock implements AnimateTickBlockMethod {

    public static EnderParticleBlock INSTANCE = new EnderParticleBlock();

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        for (int i = 0; i < 3; i++) {
            int j = source.nextInt(2) * 2 - 1;
            int k = source.nextInt(2) * 2 - 1;
            double d0 = (double) pos.m_123341_() + 0.5 + 0.25 * (double) j;
            double d1 = (double) ((float) pos.m_123342_() + source.nextFloat());
            double d2 = (double) pos.m_123343_() + 0.5 + 0.25 * (double) k;
            double d3 = (double) (source.nextFloat() * (float) j);
            double d4 = ((double) source.nextFloat() - 0.5) * 0.125;
            double d5 = (double) (source.nextFloat() * (float) k);
            level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
        }
    }
}