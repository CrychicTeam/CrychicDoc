package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface Fallable {

    default void onLand(Level level0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3, FallingBlockEntity fallingBlockEntity4) {
    }

    default void onBrokenAfterFall(Level level0, BlockPos blockPos1, FallingBlockEntity fallingBlockEntity2) {
    }

    default DamageSource getFallDamageSource(Entity entity0) {
        return entity0.damageSources().fallingBlock(entity0);
    }
}