package com.mna.effects.neutral;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EffectManaStunt extends MobEffect implements INoCreeperLingering {

    public static final float increase_per_level = 0.2F;

    public static final String modifier_key = "f85477b0-216d-11eb-adc1-0242ac120002";

    public EffectManaStunt() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().addRegenerationModifier("f85477b0-216d-11eb-adc1-0242ac120002", 0.2F * (float) (amplifier + 1)));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().removeRegenerationModifier("f85477b0-216d-11eb-adc1-0242ac120002"));
    }
}