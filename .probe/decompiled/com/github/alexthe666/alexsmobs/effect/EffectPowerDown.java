package com.github.alexthe666.alexsmobs.effect;

import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.gameevent.GameEvent;

public class EffectPowerDown extends MobEffect {

    private int lastDuration = -1;

    private int firstDuration = -1;

    protected EffectPowerDown() {
        super(MobEffectCategory.NEUTRAL, 0);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.m_20184_().y > 0.0 && !entity.m_20072_()) {
            entity.m_20256_(entity.m_20184_().multiply(1.0, 0.0, 1.0));
        }
        if (this.firstDuration == this.lastDuration) {
            entity.m_5496_(AMSoundRegistry.APRIL_FOOLS_POWER_OUTAGE.get(), 1.5F, 1.0F);
            entity.m_146850_(GameEvent.ENTITY_ROAR);
        }
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

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.power_down";
    }
}