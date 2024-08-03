package com.github.alexmodguy.alexscaves.server.potion;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class MagnetizedEffect extends MobEffect {

    protected MagnetizedEffect() {
        super(MobEffectCategory.NEUTRAL, 5461356);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int tick) {
        if (!entity.m_9236_().isClientSide && entity.f_19797_ % 20 == 0) {
            MobEffectInstance instance = entity.getEffect(this);
            if (instance != null) {
                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.m_19879_(), entity.m_19879_(), 2, instance.getDuration()));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        if (!entity.m_9236_().isClientSide) {
            MobEffectInstance instance = entity.getEffect(this);
            if (instance != null) {
                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.m_19879_(), entity.m_19879_(), 2, instance.getDuration()));
            }
        }
        super.addAttributeModifiers(entity, map, i);
    }
}