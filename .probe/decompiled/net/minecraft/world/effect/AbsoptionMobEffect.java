package net.minecraft.world.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class AbsoptionMobEffect extends MobEffect {

    protected AbsoptionMobEffect(MobEffectCategory mobEffectCategory0, int int1) {
        super(mobEffectCategory0, int1);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity0, AttributeMap attributeMap1, int int2) {
        livingEntity0.setAbsorptionAmount(livingEntity0.getAbsorptionAmount() - (float) (4 * (int2 + 1)));
        super.removeAttributeModifiers(livingEntity0, attributeMap1, int2);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity0, AttributeMap attributeMap1, int int2) {
        livingEntity0.setAbsorptionAmount(livingEntity0.getAbsorptionAmount() + (float) (4 * (int2 + 1)));
        super.addAttributeModifiers(livingEntity0, attributeMap1, int2);
    }
}