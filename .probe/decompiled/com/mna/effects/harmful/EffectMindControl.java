package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.tools.SummonUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.goal.Goal;

public class EffectMindControl extends MobEffect implements INoCreeperLingering {

    public EffectMindControl() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Mob mob) {
            mob.targetSelector.disableControlFlag(Goal.Flag.TARGET);
            if (mob.getTarget() == null || !mob.getTarget().isAlive()) {
                mob.setTarget(null);
                this.controlTarget(mob);
            }
        }
    }

    private void controlTarget(Mob host) {
        int controllerID = host.getPersistentData().getInt("mind_controller");
        if (controllerID >= 1) {
            Entity controller = host.m_9236_().getEntity(controllerID);
            if (controller != null && controller instanceof LivingEntity) {
                host.m_9236_().m_6443_(LivingEntity.class, host.m_20191_().inflate(10.0), e -> e != host && !SummonUtils.isTargetFriendly(e, (LivingEntity) controller) && Math.abs(e.m_20186_() - host.m_20186_()) <= 10.0 && host.getSensing().hasLineOfSight(e)).stream().sorted((a, b) -> (int) (a.m_20280_(host) - b.m_20280_(host))).findFirst().ifPresent(e -> host.setTarget(e));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        this.resetAIFlags(entityLivingBaseIn);
    }

    private void resetAIFlags(LivingEntity entityLivingBaseIn) {
        if (entityLivingBaseIn instanceof Mob mob) {
            mob.targetSelector.enableControlFlag(Goal.Flag.TARGET);
        }
    }
}