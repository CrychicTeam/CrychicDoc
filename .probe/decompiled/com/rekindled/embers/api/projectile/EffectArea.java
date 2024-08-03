package com.rekindled.embers.api.projectile;

import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EffectArea implements IProjectileEffect {

    IProjectileEffect effect;

    double radius;

    boolean activateOnFizzle;

    public EffectArea(IProjectileEffect effect, double radius, boolean activateOnFizzle) {
        this.effect = effect;
        this.radius = radius;
        this.activateOnFizzle = activateOnFizzle;
    }

    public IProjectileEffect getEffect() {
        return this.effect;
    }

    public void setEffect(IProjectileEffect effect) {
        this.effect = effect;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean doesActivateOnFizzle() {
        return this.activateOnFizzle;
    }

    public void setActivateOnFizzle(boolean activateOnFizzle) {
        this.activateOnFizzle = activateOnFizzle;
    }

    @Override
    public void onHit(Level world, HitResult raytrace, IProjectilePreset projectile) {
        Vec3 pos = raytrace.getLocation();
        this.doAreaEffect(world, projectile, pos);
    }

    @Override
    public void onFizzle(Level world, Vec3 pos, @Nullable IProjectilePreset projectile) {
        if (this.activateOnFizzle) {
            this.doAreaEffect(world, projectile, pos);
        }
    }

    private void doAreaEffect(Level world, IProjectilePreset projectile, Vec3 pos) {
        AABB aabb = new AABB(pos.x - this.radius, pos.y - this.radius, pos.z - this.radius, pos.x + this.radius, pos.y + this.radius, pos.z + this.radius);
        for (Entity entity : world.getEntities(projectile.getShooter(), aabb, entityx -> true)) {
            this.effect.onHit(world, new EntityHitResult(entity), projectile);
        }
    }
}