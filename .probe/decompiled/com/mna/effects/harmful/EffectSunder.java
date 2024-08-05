package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectSunder extends MobEffect implements INoCreeperLingering {

    public EffectSunder() {
        super(MobEffectCategory.HARMFUL, 0);
        this.m_19472_(Attributes.ARMOR, "e1a36b85-b471-4a65-b01b-44dc13584c6f", -1.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return (double) (-pAmplifier - 1);
    }
}