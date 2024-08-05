package io.redspace.ironsspellbooks.entity.mobs.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class WispAttackGoal extends Goal {

    private LivingEntity target;

    private PathfinderMob wisp;

    private double speedModifier;

    public WispAttackGoal(PathfinderMob wisp, double speedModifier) {
        this.wisp = wisp;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.wisp.m_5448_();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.wisp.m_21573_().isDone();
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        double distanceSquared = this.target.m_20275_(this.target.m_20185_(), this.target.m_20186_(), this.target.m_20189_());
        boolean hasLineOfSight = this.wisp.m_21574_().hasLineOfSight(this.target);
        boolean moveResult = this.wisp.m_21573_().moveTo(this.target.m_20185_(), this.target.m_20186_(), this.target.m_20189_(), this.speedModifier);
        this.wisp.m_21563_().setLookAt(this.target, 180.0F, 180.0F);
    }

    private void doAction() {
    }
}