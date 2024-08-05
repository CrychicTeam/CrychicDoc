package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class FollowMobGoal extends Goal {

    private final Mob mob;

    private final Predicate<Mob> followPredicate;

    @Nullable
    private Mob followingMob;

    private final double speedModifier;

    private final PathNavigation navigation;

    private int timeToRecalcPath;

    private final float stopDistance;

    private float oldWaterCost;

    private final float areaSize;

    public FollowMobGoal(Mob mob0, double double1, float float2, float float3) {
        this.mob = mob0;
        this.followPredicate = p_25278_ -> p_25278_ != null && mob0.getClass() != p_25278_.getClass();
        this.speedModifier = double1;
        this.navigation = mob0.getNavigation();
        this.stopDistance = float2;
        this.areaSize = float3;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(mob0.getNavigation() instanceof GroundPathNavigation) && !(mob0.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    @Override
    public boolean canUse() {
        List<Mob> $$0 = this.mob.m_9236_().m_6443_(Mob.class, this.mob.m_20191_().inflate((double) this.areaSize), this.followPredicate);
        if (!$$0.isEmpty()) {
            for (Mob $$1 : $$0) {
                if (!$$1.m_20145_()) {
                    this.followingMob = $$1;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.followingMob != null && !this.navigation.isDone() && this.mob.m_20280_(this.followingMob) > (double) (this.stopDistance * this.stopDistance);
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.followingMob = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        if (this.followingMob != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float) this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.m_183277_(10);
                double $$0 = this.mob.m_20185_() - this.followingMob.m_20185_();
                double $$1 = this.mob.m_20186_() - this.followingMob.m_20186_();
                double $$2 = this.mob.m_20189_() - this.followingMob.m_20189_();
                double $$3 = $$0 * $$0 + $$1 * $$1 + $$2 * $$2;
                if (!($$3 <= (double) (this.stopDistance * this.stopDistance))) {
                    this.navigation.moveTo(this.followingMob, this.speedModifier);
                } else {
                    this.navigation.stop();
                    LookControl $$4 = this.followingMob.getLookControl();
                    if ($$3 <= (double) this.stopDistance || $$4.getWantedX() == this.mob.m_20185_() && $$4.getWantedY() == this.mob.m_20186_() && $$4.getWantedZ() == this.mob.m_20189_()) {
                        double $$5 = this.followingMob.m_20185_() - this.mob.m_20185_();
                        double $$6 = this.followingMob.m_20189_() - this.mob.m_20189_();
                        this.navigation.moveTo(this.mob.m_20185_() - $$5, this.mob.m_20186_(), this.mob.m_20189_() - $$6, this.speedModifier);
                    }
                }
            }
        }
    }
}