package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;

public class SitWhenOrderedToGoal extends Goal {

    private final TamableAnimal mob;

    public SitWhenOrderedToGoal(TamableAnimal tamableAnimal0) {
        this.mob = tamableAnimal0;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isOrderedToSit();
    }

    @Override
    public boolean canUse() {
        if (!this.mob.isTame()) {
            return false;
        } else if (this.mob.m_20072_()) {
            return false;
        } else if (!this.mob.m_20096_()) {
            return false;
        } else {
            LivingEntity $$0 = this.mob.m_269323_();
            if ($$0 == null) {
                return true;
            } else {
                return this.mob.m_20280_($$0) < 144.0 && $$0.getLastHurtByMob() != null ? false : this.mob.isOrderedToSit();
            }
        }
    }

    @Override
    public void start() {
        this.mob.m_21573_().stop();
        this.mob.setInSittingPose(true);
    }

    @Override
    public void stop() {
        this.mob.setInSittingPose(false);
    }
}