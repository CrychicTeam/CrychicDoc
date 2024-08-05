package io.redspace.ironsspellbooks.effect.guiding_bolt;

import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class GuidingBoltEffect extends MagicMobEffect {

    public GuidingBoltEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int pAmplifier) {
        return duration % 2 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        GuidingBoltManager.INSTANCE.startTracking(pLivingEntity);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        GuidingBoltManager.INSTANCE.stopTracking(pLivingEntity);
    }
}