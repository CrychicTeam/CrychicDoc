package com.mna.effects.neutral;

import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class EffectAmplifyMagic extends MobEffect implements INoCreeperLingering {

    public EffectAmplifyMagic() {
        super(MobEffectCategory.NEUTRAL, 0);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().addRegenerationModifier("a7ba491b-39c3-480d-adc8-0af061d7ef04", -0.25F + (float) pAmplifier * -0.1F));
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> m.getCastingResource().removeRegenerationModifier("a7ba491b-39c3-480d-adc8-0af061d7ef04"));
    }
}