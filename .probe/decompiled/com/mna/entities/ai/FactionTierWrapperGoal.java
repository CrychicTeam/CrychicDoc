package com.mna.entities.ai;

import com.mna.api.entities.IFactionEnemy;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

public class FactionTierWrapperGoal extends Goal {

    private int requiredTier;

    private IFactionEnemy<?> entity;

    private Goal delegateGoal;

    public FactionTierWrapperGoal(int tier, IFactionEnemy<?> entity, Goal delegateGoal) {
        this.requiredTier = tier;
        this.entity = entity;
        this.delegateGoal = delegateGoal;
    }

    @Override
    public boolean canUse() {
        return this.entity.getTier() >= this.requiredTier && this.delegateGoal.canUse();
    }

    @Override
    public void stop() {
        this.delegateGoal.stop();
    }

    @Override
    public boolean canContinueToUse() {
        return this.delegateGoal.canContinueToUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return this.delegateGoal.requiresUpdateEveryTick();
    }

    @Override
    public void start() {
        this.delegateGoal.start();
    }

    @Override
    public void tick() {
        this.delegateGoal.tick();
    }

    @Override
    public EnumSet<Goal.Flag> getFlags() {
        return this.delegateGoal.getFlags();
    }
}