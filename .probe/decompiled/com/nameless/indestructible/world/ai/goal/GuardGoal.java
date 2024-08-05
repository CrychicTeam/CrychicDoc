package com.nameless.indestructible.world.ai.goal;

import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ProjectileWeaponItem;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class GuardGoal<T extends AdvancedCustomHumanoidMobPatch<?>> extends Goal {

    private final T mobpatch;

    private final float radiusSqr;

    private int targetInactiontime = -1;

    public GuardGoal(T customHumanoidMobPatch, float radius) {
        this.mobpatch = customHumanoidMobPatch;
        this.radiusSqr = radius * radius;
    }

    @Override
    public boolean canUse() {
        return this.checkTargetValid() && this.mobpatch.isBlocking();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && !this.targetInaction();
    }

    @Override
    public void start() {
        super.start();
        this.targetInactiontime = -1;
        this.mobpatch.resetActionTick();
    }

    @Override
    public void stop() {
        super.stop();
        this.targetInactiontime = -1;
        this.mobpatch.setBlocking(false);
        this.mobpatch.<Animator>getAnimator().resetLivingAnimations();
    }

    private boolean checkTargetValid() {
        LivingEntity livingentity = this.mobpatch.getTarget();
        if (livingentity == null) {
            return false;
        } else {
            return !livingentity.isAlive() ? false : !(livingentity instanceof Player) || !livingentity.m_5833_() && !((Player) livingentity).isCreative();
        }
    }

    private boolean withinDistance() {
        LivingEntity target = this.mobpatch.getTarget();
        return this.mobpatch.getOriginal().m_20275_(target.m_20185_(), target.m_20186_(), target.m_20189_()) <= (double) this.radiusSqr;
    }

    private boolean targetInaction() {
        LivingEntityPatch<?> target = EpicFightCapabilities.getEntityPatch(this.mobpatch.getTarget(), LivingEntityPatch.class);
        return target == null ? true : this.targetInactiontime > this.mobpatch.getBlockTick();
    }

    @Override
    public void tick() {
        LivingEntity target = this.mobpatch.getTarget();
        if (target != null) {
            LivingEntityPatch<?> targetPatch = EpicFightCapabilities.getEntityPatch(this.mobpatch.getTarget(), LivingEntityPatch.class);
            if (targetPatch != null) {
                if (targetPatch.getEntityState().getLevel() > 0 && this.withinDistance()) {
                    this.targetInactiontime = 0;
                } else if (this.mobpatch.canBlockProjectile() && target.getUseItem().getItem() instanceof ProjectileWeaponItem && target.isUsingItem()) {
                    this.targetInactiontime = 0;
                } else {
                    this.targetInactiontime++;
                }
            }
        }
    }
}