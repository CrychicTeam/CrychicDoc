package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class TrueInvisibilityEffect extends MagicMobEffect {

    int lastHurtTimestamp;

    public TrueInvisibilityEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(livingEntity, pAttributeMap, pAmplifier);
        if (livingEntity instanceof Player || livingEntity instanceof AbstractSpellCastingMob) {
            MagicData.getPlayerMagicData(livingEntity).getSyncedData().addEffects(32L);
        }
        TargetingConditions targetingCondition = TargetingConditions.forCombat().ignoreLineOfSight().selector(e -> ((Mob) e).getTarget() == livingEntity);
        livingEntity.f_19853_.m_45971_(Mob.class, targetingCondition, livingEntity, livingEntity.m_20191_().inflate(40.0)).forEach(entityTargetingCaster -> {
            entityTargetingCaster.setTarget(null);
            entityTargetingCaster.m_21335_(null);
            entityTargetingCaster.m_6703_(null);
            entityTargetingCaster.targetSelector.getAvailableGoals().forEach(WrappedGoal::m_8041_);
        });
        this.lastHurtTimestamp = livingEntity.getLastHurtMobTimestamp();
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (!pLivingEntity.f_19853_.isClientSide && this.lastHurtTimestamp != pLivingEntity.getLastHurtMobTimestamp()) {
            pLivingEntity.removeEffect(this);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(livingEntity, pAttributeMap, pAmplifier);
        if (livingEntity instanceof Player || livingEntity instanceof AbstractSpellCastingMob) {
            MagicData.getPlayerMagicData(livingEntity).getSyncedData().removeEffects(32L);
        }
    }
}