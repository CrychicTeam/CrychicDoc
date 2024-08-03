package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectBurningRage extends MobEffect implements INoCreeperLingering {

    public EffectBurningRage() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "c085fd06-e784-11ed-a05b-0242ac120003", 0.5, AttributeModifier.Operation.MULTIPLY_BASE);
        this.m_19472_(Attributes.ATTACK_DAMAGE, "dedc59ee-e784-11ed-a05b-0242ac120003", 6.0, AttributeModifier.Operation.ADDITION);
        this.m_19472_(Attributes.KNOCKBACK_RESISTANCE, "ed360102-e784-11ed-a05b-0242ac120003", 0.5, AttributeModifier.Operation.ADDITION);
    }
}