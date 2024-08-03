package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class MoveTowardsRestrictionGoal extends Goal {

    private final PathfinderMob mob;

    private double wantedX;

    private double wantedY;

    private double wantedZ;

    private final double speedModifier;

    public MoveTowardsRestrictionGoal(PathfinderMob pathfinderMob0, double double1) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_21533_()) {
            return false;
        } else {
            Vec3 $$0 = DefaultRandomPos.getPosTowards(this.mob, 16, 7, Vec3.atBottomCenterOf(this.mob.m_21534_()), (float) (Math.PI / 2));
            if ($$0 == null) {
                return false;
            } else {
                this.wantedX = $$0.x;
                this.wantedY = $$0.y;
                this.wantedZ = $$0.z;
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.mob.m_21573_().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
}