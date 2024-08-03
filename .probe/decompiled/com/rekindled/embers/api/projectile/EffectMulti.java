package com.rekindled.embers.api.projectile;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EffectMulti implements IProjectileEffect {

    List<IProjectileEffect> effects = new ArrayList();

    public EffectMulti(List<IProjectileEffect> effects) {
        this.effects.addAll(effects);
    }

    public List<IProjectileEffect> getEffects() {
        return this.effects;
    }

    public void addEffect(IProjectileEffect effect) {
        this.effects.add(effect);
    }

    @Override
    public void onHit(Level world, HitResult raytrace, IProjectilePreset projectile) {
        for (IProjectileEffect effect : this.effects) {
            effect.onHit(world, raytrace, projectile);
        }
    }

    @Override
    public void onEntityImpact(Entity entity, IProjectilePreset projectile) {
        for (IProjectileEffect effect : this.effects) {
            effect.onEntityImpact(entity, projectile);
        }
    }

    @Override
    public void onBlockImpact(Level world, BlockPos pos, Direction side, IProjectilePreset projectile) {
        for (IProjectileEffect effect : this.effects) {
            effect.onBlockImpact(world, pos, side, projectile);
        }
    }

    @Override
    public void onFizzle(Level world, Vec3 pos, IProjectilePreset projectile) {
        for (IProjectileEffect effect : this.effects) {
            effect.onFizzle(world, pos, projectile);
        }
    }
}