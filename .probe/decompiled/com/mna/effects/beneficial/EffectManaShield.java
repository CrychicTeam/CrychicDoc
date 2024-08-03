package com.mna.effects.beneficial;

import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EffectManaShield extends EffectWithCustomClientParticles implements INoCreeperLingering {

    public static final int MANA_BOOST_PER_LEVEL = 20;

    public static final String modifier_key = "9ed65596-16d7-11eb-adc1-0242ac120002";

    public EffectManaShield() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.MANA_SHIELD);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.m_6385_(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().addModifier("9ed65596-16d7-11eb-adc1-0242ac120002", (float) (20 * (amplifier + 1))));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.m_6386_(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().removeModifier("9ed65596-16d7-11eb-adc1-0242ac120002"));
    }

    public float getReductionCost(int amplifier) {
        return (float) (amplifier * 20);
    }
}