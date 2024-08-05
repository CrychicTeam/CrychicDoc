package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class LeapAtTargetGoal extends Goal {

    private final Mob mob;

    private LivingEntity target;

    private final float yd;

    public LeapAtTargetGoal(Mob mob0, float float1) {
        this.mob = mob0;
        this.yd = float1;
        this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_20160_()) {
            return false;
        } else {
            this.target = this.mob.getTarget();
            if (this.target == null) {
                return false;
            } else {
                double $$0 = this.mob.m_20280_(this.target);
                if ($$0 < 4.0 || $$0 > 16.0) {
                    return false;
                } else {
                    return !this.mob.m_20096_() ? false : this.mob.m_217043_().nextInt(m_186073_(5)) == 0;
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.m_20096_();
    }

    @Override
    public void start() {
        Vec3 $$0 = this.mob.m_20184_();
        Vec3 $$1 = new Vec3(this.target.m_20185_() - this.mob.m_20185_(), 0.0, this.target.m_20189_() - this.mob.m_20189_());
        if ($$1.lengthSqr() > 1.0E-7) {
            $$1 = $$1.normalize().scale(0.4).add($$0.scale(0.2));
        }
        this.mob.m_20334_($$1.x, (double) this.yd, $$1.z);
    }
}