package com.mna.effects.beneficial;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EffectManaBoost extends MobEffect implements INoCreeperLingering {

    public static final int MANA_BOOST_PER_LEVEL = 50;

    public static final String modifier_key = "9ed65596-16d7-11eb-adc1-0242ac120002";

    public EffectManaBoost() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().addModifier("9ed65596-16d7-11eb-adc1-0242ac120002", (float) (50 * (amplifier + 1))));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().removeModifier("9ed65596-16d7-11eb-adc1-0242ac120002"));
    }
}