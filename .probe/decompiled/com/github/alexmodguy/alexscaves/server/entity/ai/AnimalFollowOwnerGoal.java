package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public abstract class AnimalFollowOwnerGoal extends FollowOwnerGoal {

    private TamableAnimal tameable;

    public AnimalFollowOwnerGoal(TamableAnimal tameable, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        super(tameable, speed, minDist, maxDist, teleportToLeaves);
        this.tameable = tameable;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.shouldFollow() && !this.isInCombat();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.shouldFollow() && !this.isInCombat();
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity livingentity = this.tameable.m_269323_();
        if (livingentity != null) {
            this.tickDistance(this.tameable.m_20270_(livingentity));
        }
    }

    public void tickDistance(float distanceTo) {
    }

    public abstract boolean shouldFollow();

    private boolean isInCombat() {
        Entity owner = this.tameable.m_269323_();
        return owner == null ? false : this.tameable.m_20270_(owner) < 30.0F && this.tameable.m_5448_() != null && this.tameable.m_5448_().isAlive();
    }
}