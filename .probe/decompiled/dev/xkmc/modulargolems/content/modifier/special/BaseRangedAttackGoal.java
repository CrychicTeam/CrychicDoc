package dev.xkmc.modulargolems.content.modifier.special;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class BaseRangedAttackGoal extends Goal {

    private final int waitTime;

    private final int near;

    private final int far;

    protected final AbstractGolemEntity<?, ?> golem;

    protected final int lv;

    private int attackTime;

    public BaseRangedAttackGoal(int waitTime, int near, int far, AbstractGolemEntity<?, ?> golem, int lv) {
        this.golem = golem;
        this.lv = lv;
        this.waitTime = waitTime;
        this.attackTime = waitTime;
        this.near = near * near;
        this.far = far * far;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.golem.m_5448_();
        return livingentity != null && livingentity.isAlive() && this.golem.canAttack(livingentity) && this.golem.m_20280_(livingentity) < (double) this.far && (this.golem.m_21573_().isStuck() || this.golem.m_20280_(livingentity) > (double) this.near);
    }

    @Override
    public void start() {
        this.attackTime = this.waitTime;
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.attackTime--;
        LivingEntity le = this.golem.m_5448_();
        if (this.attackTime <= 0 && le != null && le.isAlive()) {
            this.performAttack(le);
            this.attackTime = this.waitTime;
        }
        super.tick();
    }

    protected abstract void performAttack(LivingEntity var1);
}