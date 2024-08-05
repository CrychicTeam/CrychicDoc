package com.github.alexthe666.alexsmobs.effect;

import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityEnderiophage;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectEnderFlu extends MobEffect {

    private int lastDuration = -1;

    public EffectEnderFlu() {
        super(MobEffectCategory.HARMFUL, 6829738);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this.lastDuration == 1) {
            int phages = amplifier + 1;
            entity.hurt(entity.m_269291_().magic(), (float) (phages * 10));
            for (int i = 0; i < phages; i++) {
                EntityEnderiophage phage = AMEntityRegistry.ENDERIOPHAGE.get().create(entity.m_9236_());
                phage.m_20359_(entity);
                phage.onSpawnFromEffect();
                phage.setSkinForDimension();
                if (!entity.m_9236_().isClientSide) {
                    phage.setStandardFleeTime();
                    entity.m_9236_().m_7967_(phage);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        this.lastDuration = duration;
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.ender_flu";
    }
}