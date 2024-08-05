package com.rekindled.embers.api.projectile;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EffectPotion implements IProjectileEffect {

    MobEffectInstance effect;

    public EffectPotion(MobEffectInstance effect) {
        this.effect = effect;
    }

    public MobEffectInstance getEffect() {
        return this.effect;
    }

    public void setEffect(MobEffectInstance effect) {
        this.effect = effect;
    }

    @Override
    public void onEntityImpact(Entity entity, IProjectilePreset projectile) {
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addEffect(this.effect);
        }
    }
}