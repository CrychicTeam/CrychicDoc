package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class MoveTowardsTargetGoal extends Goal {

    private final PathfinderMob mob;

    @Nullable
    private LivingEntity target;

    private double wantedX;

    private double wantedY;

    private double wantedZ;

    private final double speedModifier;

    private final float within;

    public MoveTowardsTargetGoal(PathfinderMob pathfinderMob0, double double1, float float2) {
        this.mob = pathfinderMob0;
        this.speedModifier = double1;
        this.within = float2;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.target = this.mob.m_5448_();
        if (this.target == null) {
            return false;
        } else if (this.target.m_20280_(this.mob) > (double) (this.within * this.within)) {
            return false;
        } else {
            Vec3 $$0 = DefaultRandomPos.getPosTowards(this.mob, 16, 7, this.target.m_20182_(), (float) (Math.PI / 2));
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
        return !this.mob.m_21573_().isDone() && this.target.isAlive() && this.target.m_20280_(this.mob) < (double) (this.within * this.within);
    }

    @Override
    public void stop() {
        this.target = null;
    }

    @Override
    public void start() {
        this.mob.m_21573_().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
}