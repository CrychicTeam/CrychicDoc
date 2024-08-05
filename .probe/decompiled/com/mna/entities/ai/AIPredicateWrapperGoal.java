package com.mna.entities.ai;

import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class AIPredicateWrapperGoal<T extends LivingEntity> extends Goal {

    private Goal delegateGoal;

    private Predicate<T> shouldExecute = null;

    private Predicate<T> shouldContinueExecuting = null;

    private T _entity;

    public AIPredicateWrapperGoal(T entity, Goal delegateGoal) {
        this.delegateGoal = delegateGoal;
        this._entity = entity;
    }

    public AIPredicateWrapperGoal<T> executionPredicate(Predicate<T> pred) {
        this.shouldExecute = pred;
        return this;
    }

    public AIPredicateWrapperGoal<T> continuationPredicate(Predicate<T> pred) {
        this.shouldContinueExecuting = pred;
        return this;
    }

    @Override
    public boolean canUse() {
        return this.shouldExecute == null ? this.delegateGoal.canUse() : this.shouldExecute.test(this._entity) && this.delegateGoal.canUse();
    }

    @Override
    public void stop() {
        this.delegateGoal.stop();
    }

    @Override
    public boolean canContinueToUse() {
        return this.shouldContinueExecuting == null ? this.delegateGoal.canContinueToUse() : this.shouldContinueExecuting.test(this._entity) && this.delegateGoal.canContinueToUse();
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