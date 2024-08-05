package net.minecraft.world.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class HealthBoostMobEffect extends MobEffect {

    public HealthBoostMobEffect(MobEffectCategory mobEffectCategory0, int int1) {
        super(mobEffectCategory0, int1);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity0, AttributeMap attributeMap1, int int2) {
        super.removeAttributeModifiers(livingEntity0, attributeMap1, int2);
        if (livingEntity0.getHealth() > livingEntity0.getMaxHealth()) {
            livingEntity0.setHealth(livingEntity0.getMaxHealth());
        }
    }
}