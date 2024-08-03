package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class OcelotAttackGoal extends Goal {

    private final Mob mob;

    private LivingEntity target;

    private int attackTime;

    public OcelotAttackGoal(Mob mob0) {
        this.mob = mob0;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity $$0 = this.mob.getTarget();
        if ($$0 == null) {
            return false;
        } else {
            this.target = $$0;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (!this.target.isAlive()) {
            return false;
        } else {
            return this.mob.m_20280_(this.target) > 225.0 ? false : !this.mob.getNavigation().isDone() || this.canUse();
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        double $$0 = (double) (this.mob.m_20205_() * 2.0F * this.mob.m_20205_() * 2.0F);
        double $$1 = this.mob.m_20275_(this.target.m_20185_(), this.target.m_20186_(), this.target.m_20189_());
        double $$2 = 0.8;
        if ($$1 > $$0 && $$1 < 16.0) {
            $$2 = 1.33;
        } else if ($$1 < 225.0) {
            $$2 = 0.6;
        }
        this.mob.getNavigation().moveTo(this.target, $$2);
        this.attackTime = Math.max(this.attackTime - 1, 0);
        if (!($$1 > $$0)) {
            if (this.attackTime <= 0) {
                this.attackTime = 20;
                this.mob.doHurtTarget(this.target);
            }
        }
    }
}