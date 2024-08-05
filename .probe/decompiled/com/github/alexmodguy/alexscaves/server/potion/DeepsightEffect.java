package com.github.alexmodguy.alexscaves.server.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class DeepsightEffect extends MobEffect {

    private int lastDuration = -1;

    private int firstDuration = -1;

    protected DeepsightEffect() {
        super(MobEffectCategory.BENEFICIAL, 10610);
    }

    public int getActiveTime() {
        return this.firstDuration - this.lastDuration;
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        this.lastDuration = duration;
        if (duration <= 0) {
            this.lastDuration = -1;
            this.firstDuration = -1;
        }
        if (this.firstDuration == -1) {
            this.firstDuration = duration;
        }
        return duration > 0;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        this.lastDuration = -1;
        this.firstDuration = -1;
        super.removeAttributeModifiers(entity, map, i);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        this.lastDuration = -1;
        this.firstDuration = -1;
        super.addAttributeModifiers(entity, map, i);
    }

    public static float getIntensity(Player player, float partialTicks) {
        MobEffectInstance instance = player.m_21124_(ACEffectRegistry.DEEPSIGHT.get());
        if (instance == null) {
            return 0.0F;
        } else if (instance.isInfiniteDuration()) {
            return 1.0F;
        } else {
            DeepsightEffect deepsightEffect = (DeepsightEffect) instance.getEffect();
            float j = (float) deepsightEffect.getActiveTime() + partialTicks;
            int duration = instance.getDuration();
            return Math.min(20.0F, Math.min(j, (float) duration + partialTicks)) * 0.05F;
        }
    }
}