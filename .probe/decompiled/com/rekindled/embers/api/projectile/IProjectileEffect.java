package com.rekindled.embers.api.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public interface IProjectileEffect {

    default void onHit(Level world, HitResult raytrace, IProjectilePreset projectile) {
        if (raytrace instanceof EntityHitResult entityRaytrace && entityRaytrace.getEntity() != null) {
            this.onEntityImpact(entityRaytrace.getEntity(), projectile);
            return;
        }
        if (raytrace instanceof BlockHitResult blockRaytrace) {
            this.onBlockImpact(world, blockRaytrace.getBlockPos(), blockRaytrace.getDirection(), projectile);
        }
    }

    default void onEntityImpact(Entity entity, IProjectilePreset projectile) {
    }

    default void onBlockImpact(Level world, BlockPos pos, Direction side, IProjectilePreset projectile) {
    }

    default void onFizzle(Level world, Vec3 pos, IProjectilePreset projectile) {
    }
}