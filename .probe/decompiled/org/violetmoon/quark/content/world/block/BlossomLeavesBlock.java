package org.violetmoon.quark.content.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.world.module.BlossomTreesModule;
import org.violetmoon.zeta.block.ZetaLeavesBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class BlossomLeavesBlock extends ZetaLeavesBlock {

    public BlossomLeavesBlock(String id, @Nullable ZetaModule module, MapColor color) {
        super(id, module, color);
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, Level worldIn, BlockPos pos, @NotNull RandomSource rand) {
        if (BlossomTreesModule.dropLeafParticles && rand.nextInt(5) == 0 && worldIn.m_46859_(pos.below())) {
            double windStrength = 5.0 + Math.cos((double) worldIn.getGameTime() / 2000.0) * 2.0;
            double windX = Math.cos((double) worldIn.getGameTime() / 1200.0) * windStrength;
            double windZ = Math.sin((double) worldIn.getGameTime() / 1000.0) * windStrength;
            worldIn.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, stateIn), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, windX, -1.0, windZ);
        }
        super.m_214162_(stateIn, worldIn, pos, rand);
    }
}