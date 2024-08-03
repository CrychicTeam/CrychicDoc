package net.minecraft.world.level;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class ExplosionDamageCalculator {

    public Optional<Float> getBlockExplosionResistance(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, FluidState fluidState4) {
        return blockState3.m_60795_() && fluidState4.isEmpty() ? Optional.empty() : Optional.of(Math.max(blockState3.m_60734_().getExplosionResistance(), fluidState4.getExplosionResistance()));
    }

    public boolean shouldBlockExplode(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, float float4) {
        return true;
    }
}