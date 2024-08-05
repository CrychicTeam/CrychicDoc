package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;

public class EffectTelekinesis extends MobEffect implements INoCreeperLingering {

    public EffectTelekinesis() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.m_19472_(ForgeMod.BLOCK_REACH.get(), "0c280955-9322-497b-8a97-4ea15fd021b2", 0.0, AttributeModifier.Operation.ADDITION);
        this.m_19472_(ForgeMod.ENTITY_REACH.get(), "24b22d16-01b8-4e1f-b905-881ff598eb17", 0.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return (double) pAmplifier;
    }
}