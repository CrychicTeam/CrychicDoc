package net.minecraft.world.effect;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttackDamageMobEffect extends MobEffect {

    protected final double multiplier;

    protected AttackDamageMobEffect(MobEffectCategory mobEffectCategory0, int int1, double double2) {
        super(mobEffectCategory0, int1);
        this.multiplier = double2;
    }

    @Override
    public double getAttributeModifierValue(int int0, AttributeModifier attributeModifier1) {
        return this.multiplier * (double) (int0 + 1);
    }
}