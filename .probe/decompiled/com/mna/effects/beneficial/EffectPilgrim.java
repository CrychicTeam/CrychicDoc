package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;

public class EffectPilgrim extends MobEffect implements INoCreeperLingering {

    public EffectPilgrim() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "0fddd148-bfc1-41ba-95af-5e9dd9a25f97", 0.2F, AttributeModifier.Operation.MULTIPLY_BASE);
        this.m_19472_(ForgeMod.SWIM_SPEED.get(), "8b243cec-6b01-4018-a6c6-dbfbf319012e", 0.2F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.KNOCKBACK_RESISTANCE, "845ba178-29e0-4845-84ec-69a11b705380", 1.0, AttributeModifier.Operation.ADDITION);
        this.m_19472_(ForgeMod.STEP_HEIGHT_ADDITION.get(), "845ba178-29e0-4845-84ec-69a11b705380", 1.0, AttributeModifier.Operation.ADDITION);
    }
}