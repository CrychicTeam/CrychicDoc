package dev.xkmc.l2complements.content.effect.force;

import dev.xkmc.l2library.base.effects.api.ForceEffect;
import dev.xkmc.l2library.util.math.MathHelper;
import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ArmorReduceEffect extends MobEffect implements ForceEffect {

    public static final UUID ID = MathHelper.getUUIDFromString("l2complements:armor_reduce");

    public ArmorReduceEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.m_19472_(Attributes.ARMOR, ID.toString(), -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return Math.pow(1.0 + pModifier.getAmount(), (double) (pAmplifier + 1)) - 1.0;
    }
}