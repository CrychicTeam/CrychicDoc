package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.damage.DamageSources;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

public class BurningDashEffect extends MobEffect {

    public BurningDashEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        List<Entity> list = livingEntity.f_19853_.m_45933_(livingEntity, livingEntity.m_20191_().inflate(0.25, 0.5, 0.25));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (entity instanceof LivingEntity) {
                    DamageSources.applyDamage(entity, (float) amplifier, SpellRegistry.BURNING_DASH_SPELL.get().getDamageSource(livingEntity));
                    entity.invulnerableTime = 20;
                }
            }
        } else if (livingEntity.f_19862_) {
            livingEntity.removeEffect(this);
        }
        livingEntity.f_19789_ = 0.0F;
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.addAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.setLivingEntityFlag(4, true);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.setLivingEntityFlag(4, false);
    }
}