package dev.shadowsoffire.attributeslib.mobfx;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingEffect extends MobEffect {

    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 9109504);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(entity.m_9236_().damageSources().source(ALObjects.DamageTypes.BLEEDING, entity.getLastAttacker()), 1.0F + (float) amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}