package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class RandomStrollGoal extends Goal {

    public static final int DEFAULT_INTERVAL = 120;

    protected final PathfinderMob mob;

    protected double wantedX;

    protected double wantedY;

    protected double wantedZ;

    protected final double speedModifier;

    protected int interval;

    protected boolean forceTrigger;

    private final boolean checkNoActionTime;

    public RandomStrollGoal(PathfinderMob pathfinderMob0, double double1) {
        this(pathfinderMob0, double1, 120);
    }

    public RandomStrollGoal(PathfinderMob pathfinderMob0, double double1, int int2) {
        this(pathfinderMob0, double1, int2, true);
    }

    public RandomStrollGoal(PathfinderMob pathfinderMob0, double double1, int int2, boolean boolean3) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.interval = int2;
        this.checkNoActionTime = boolean3;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_20160_()) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.checkNoActionTime && this.mob.m_21216_() >= 100) {
                    return false;
                }
                if (this.mob.m_217043_().nextInt(m_186073_(this.interval)) != 0) {
                    return false;
                }
            }
            Vec3 $$0 = this.getPosition();
            if ($$0 == null) {
                return false;
            } else {
                this.wantedX = $$0.x;
                this.wantedY = $$0.y;
                this.wantedZ = $$0.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    @Nullable
    protected Vec3 getPosition() {
        return DefaultRandomPos.getPos(this.mob, 10, 7);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.m_21573_().isDone() && !this.mob.m_20160_();
    }

    @Override
    public void start() {
        this.mob.m_21573_().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }

    @Override
    public void stop() {
        this.mob.m_21573_().stop();
        super.stop();
    }

    public void trigger() {
        this.forceTrigger = true;
    }

    public void setInterval(int int0) {
        this.interval = int0;
    }
}