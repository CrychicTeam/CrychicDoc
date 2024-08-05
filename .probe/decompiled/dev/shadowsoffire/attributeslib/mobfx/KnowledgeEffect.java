package dev.shadowsoffire.attributeslib.mobfx;

import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class KnowledgeEffect extends MobEffect {

    public KnowledgeEffect() {
        super(MobEffectCategory.BENEFICIAL, 16051778);
        this.m_19472_(ALObjects.Attributes.EXPERIENCE_GAINED.get(), "55688e2f-7db8-4d0b-bc90-eff194546c04", (double) AttributesLib.knowledgeMult, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int amp, AttributeModifier modifier) {
        return (double) (amp * ++amp * AttributesLib.knowledgeMult);
    }
}