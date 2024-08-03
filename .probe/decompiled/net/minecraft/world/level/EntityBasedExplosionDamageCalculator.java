package net.minecraft.world.level;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class EntityBasedExplosionDamageCalculator extends ExplosionDamageCalculator {

    private final Entity source;

    public EntityBasedExplosionDamageCalculator(Entity entity0) {
        this.source = entity0;
    }

    @Override
    public Optional<Float> getBlockExplosionResistance(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, FluidState fluidState4) {
        return super.getBlockExplosionResistance(explosion0, blockGetter1, blockPos2, blockState3, fluidState4).map(p_45913_ -> this.source.getBlockExplosionResistance(explosion0, blockGetter1, blockPos2, blockState3, fluidState4, p_45913_));
    }

    @Override
    public boolean shouldBlockExplode(Explosion explosion0, BlockGetter blockGetter1, BlockPos blockPos2, BlockState blockState3, float float4) {
        return this.source.shouldBlockExplode(explosion0, blockGetter1, blockPos2, blockState3, float4);
    }
}