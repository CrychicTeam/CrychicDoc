package io.redspace.ironsspellbooks.entity.mobs.goals;

import io.redspace.ironsspellbooks.IronsSpellbooks;
import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

public class DebugTargetClosestEntityGoal extends TargetGoal {

    @Nullable
    protected LivingEntity target;

    public DebugTargetClosestEntityGoal(Mob pMob) {
        super(pMob, false, false);
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        this.findTarget();
        return this.target != null;
    }

    protected void findTarget() {
        LivingEntity tmp = this.target;
        this.target = this.f_26135_.m_9236_().m_45930_(this.f_26135_, 40.0);
        if (tmp != this.target) {
            IronsSpellbooks.LOGGER.debug("DebugTargetClosestEntityGoal: Target Changed old:{} new:{}", tmp, this.target);
            this.f_26135_.setTarget(this.target);
        }
    }

    @Override
    public void start() {
        this.f_26135_.setTarget(this.target);
        super.start();
    }

    public void setTarget(@Nullable LivingEntity pTarget) {
        this.target = pTarget;
    }
}