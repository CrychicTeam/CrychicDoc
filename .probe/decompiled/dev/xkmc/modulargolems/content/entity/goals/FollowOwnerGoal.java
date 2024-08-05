package dev.xkmc.modulargolems.content.entity.goals;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class FollowOwnerGoal extends Goal {

    private final AbstractGolemEntity<?, ?> golem;

    private final double speedModifier;

    private int timeToRecalcPath;

    private float oldWaterCost;

    public FollowOwnerGoal(AbstractGolemEntity<?, ?> golem) {
        this(golem, 1.0);
    }

    private FollowOwnerGoal(AbstractGolemEntity<?, ?> golem, double speed) {
        this.golem = golem;
        this.speedModifier = speed;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.golem.isInSittingPose() && this.golem.getMode().isMovable()) {
            Vec3 target = this.golem.getTargetPos();
            double startDistance = this.golem.getMode().getStartFollowDistance(this.golem);
            return this.golem.m_20238_(target) > startDistance * startDistance;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.golem.m_21573_().isDone()) {
            this.golem.getMode().tick(this.golem);
            if (this.golem.m_21573_().isDone()) {
                return false;
            }
        }
        if (!this.golem.isInSittingPose() && this.golem.getMode().isMovable()) {
            Vec3 target = this.golem.getTargetPos();
            double stop = this.golem.getMode().getStopDistance();
            return !(this.golem.m_20238_(target) <= stop * stop);
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.golem.m_21439_(BlockPathTypes.WATER);
        this.golem.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.golem.m_21573_().stop();
        this.golem.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        Vec3 target = this.golem.getTargetPos();
        LivingEntity owner = this.golem.getOwner();
        if (owner != null) {
            this.golem.m_21563_().setLookAt(owner, 10.0F, (float) this.golem.m_8132_());
        }
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.m_183277_(10);
            if (!this.golem.m_21523_()) {
                this.golem.m_21573_().moveTo(target.x(), target.y(), target.z(), this.speedModifier);
                return;
            }
        }
        this.golem.getMode().tick(this.golem);
    }
}