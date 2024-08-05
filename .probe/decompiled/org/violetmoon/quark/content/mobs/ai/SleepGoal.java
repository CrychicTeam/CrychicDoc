package org.violetmoon.quark.content.mobs.ai;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.Goal;
import org.violetmoon.quark.content.mobs.entity.Foxhound;

public class SleepGoal extends Goal {

    private final Foxhound foxhound;

    private boolean isSleeping;

    private boolean wasSitting;

    public SleepGoal(Foxhound foxhound) {
        this.foxhound = foxhound;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.foxhound.m_21824_() && !this.foxhound.m_20069_() && this.foxhound.m_20096_()) {
            LivingEntity living = this.foxhound.m_269323_();
            return living == null ? true : (!(this.foxhound.m_20280_(living) < 144.0) || living.getLastHurtByMob() == null) && this.isSleeping;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.foxhound.m_20089_() == Pose.SLEEPING;
    }

    @Override
    public void start() {
        this.foxhound.m_21573_().stop();
        this.wasSitting = this.foxhound.m_21827_();
        this.foxhound.m_21839_(true);
        this.foxhound.m_21837_(true);
    }

    @Override
    public void stop() {
        this.foxhound.m_21839_(this.wasSitting);
        this.foxhound.m_21837_(false);
        this.foxhound.m_21258_();
    }

    public void setSleeping(boolean sitting) {
        this.isSleeping = sitting;
        this.foxhound.m_20124_(Pose.STANDING);
    }
}