package com.rekindled.embers.api.projectile;

import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectDamage implements IProjectileEffect {

    float damage;

    Function<Entity, DamageSource> source;

    int fire;

    double invinciblityMultiplier = 1.0;

    public EffectDamage(float damage, Function<Entity, DamageSource> source, int fire, double invinciblityMultiplier) {
        this.damage = damage;
        this.source = source;
        this.fire = fire;
        this.invinciblityMultiplier = invinciblityMultiplier;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Function<Entity, DamageSource> getSource() {
        return this.source;
    }

    public void setSource(Function<Entity, DamageSource> source) {
        this.source = source;
    }

    public int getFire() {
        return this.fire;
    }

    public void setFire(int seconds) {
        this.fire = seconds;
    }

    public double getInvinciblityMultiplier() {
        return this.invinciblityMultiplier;
    }

    public void setInvinciblityMultiplier(double multiplier) {
        this.invinciblityMultiplier = multiplier;
    }

    @Override
    public void onEntityImpact(Entity entity, @Nullable IProjectilePreset projectile) {
        Entity shooter = projectile != null ? projectile.getShooter() : null;
        Entity projectileEntity = projectile != null ? projectile.getEntity() : null;
        if (entity.hurt((DamageSource) this.source.apply(projectileEntity), this.damage)) {
            entity.setSecondsOnFire(this.fire);
        }
        if (entity instanceof LivingEntity livingTarget) {
            livingTarget.setLastHurtMob(shooter);
            livingTarget.hurtDuration = (int) ((double) livingTarget.hurtDuration * this.invinciblityMultiplier);
        }
    }
}