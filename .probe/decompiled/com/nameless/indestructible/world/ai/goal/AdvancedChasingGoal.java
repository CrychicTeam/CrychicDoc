package com.nameless.indestructible.world.ai.goal;

import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AdvancedChasingGoal<T extends AdvancedCustomHumanoidMobPatch<?>> extends MeleeAttackGoal {

    private final T mobpatch;

    protected final double attackRadiusSqr;

    private final double speed;

    public AdvancedChasingGoal(T mobpatch, PathfinderMob pathfinderMob, double speedModifier, boolean longMemory, double attackRadius) {
        super(pathfinderMob, speedModifier, longMemory);
        this.mobpatch = mobpatch;
        this.speed = speedModifier;
        this.attackRadiusSqr = attackRadius * attackRadius;
    }

    @Override
    public void tick() {
        if (this.mobpatch.getInactionTime() > 0) {
            this.mobpatch.setInactionTime(this.mobpatch.getInactionTime() - 1);
        }
        LivingEntity target = this.f_25540_.m_5448_();
        if (target != null) {
            if (!this.mobpatch.getEntityState().turningLocked()) {
                this.f_25540_.m_21563_().setLookAt(target, 30.0F, 30.0F);
            }
            if (!this.mobpatch.getEntityState().movementLocked()) {
                boolean withDistance = this.attackRadiusSqr > this.f_25540_.m_20275_(target.m_20185_(), target.m_20186_(), target.m_20189_());
                if (this.mobpatch.getStrafingTime() > 0) {
                    this.mobpatch.setStrafingTime(this.mobpatch.getStrafingTime() - 1);
                    this.f_25540_.m_21573_().stop();
                    this.f_25540_.m_21391_(target, 30.0F, 30.0F);
                    this.f_25540_.m_21566_().strafe(withDistance && this.mobpatch.getStrafingForward() > 0.0F ? 0.0F : this.mobpatch.getStrafingForward(), this.mobpatch.getStrafingClockwise());
                } else if (withDistance) {
                    this.f_25540_.m_21573_().stop();
                } else if (this.mobpatch.isBlocking()) {
                    this.f_25540_.m_21391_(target, 30.0F, 30.0F);
                    this.f_25540_.m_21573_().moveTo(target, this.speed * 0.8F);
                } else {
                    super.tick();
                }
            }
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double double0) {
    }
}