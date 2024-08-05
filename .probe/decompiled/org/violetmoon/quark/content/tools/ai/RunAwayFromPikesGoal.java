package org.violetmoon.quark.content.tools.ai;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.tools.entity.SkullPike;

public class RunAwayFromPikesGoal extends Goal {

    protected final PathfinderMob entity;

    private final double farSpeed;

    private final double nearSpeed;

    protected SkullPike avoidTarget;

    protected final float avoidDistance;

    protected Path path;

    protected final PathNavigation navigation;

    public RunAwayFromPikesGoal(PathfinderMob entityIn, float distance, double nearSpeedIn, double farSpeedIn) {
        this.entity = entityIn;
        this.avoidDistance = distance;
        this.farSpeed = nearSpeedIn;
        this.nearSpeed = farSpeedIn;
        this.navigation = entityIn.m_21573_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.avoidTarget = this.getClosestEntity(this.entity.m_9236_(), this.entity, this.entity.m_20185_(), this.entity.m_20186_(), this.entity.m_20189_(), this.entity.m_20191_().inflate((double) this.avoidDistance, 3.0, (double) this.avoidDistance));
        if (this.avoidTarget == null) {
            return false;
        } else {
            Vec3 posToMove = DefaultRandomPos.getPosAway(this.entity, 16, 7, this.avoidTarget.m_20182_());
            if (posToMove == null) {
                return false;
            } else if (this.avoidTarget.m_20275_(posToMove.x, posToMove.y, posToMove.z) < this.avoidTarget.m_20280_(this.entity)) {
                return false;
            } else {
                this.path = this.navigation.createPath(posToMove.x, posToMove.y, posToMove.z, 0);
                return this.path != null;
            }
        }
    }

    @Nullable
    private SkullPike getClosestEntity(Level world, LivingEntity living, double x, double y, double z, AABB bounds) {
        return this.getClosestEntity(world.m_6443_(SkullPike.class, bounds, skullPike -> true), living, x, y, z);
    }

    @Nullable
    private SkullPike getClosestEntity(List<SkullPike> entities, LivingEntity target, double x, double y, double z) {
        double d0 = -1.0;
        SkullPike t = null;
        for (SkullPike t1 : entities) {
            if (t1.isVisible(target)) {
                double d1 = t1.m_20275_(x, y, z);
                if (d0 == -1.0 || d1 < d0) {
                    d0 = d1;
                    t = t1;
                }
            }
        }
        return t;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.navigation.isDone();
    }

    @Override
    public void start() {
        this.navigation.moveTo(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.avoidTarget = null;
    }

    @Override
    public void tick() {
        if (this.entity.m_20280_(this.avoidTarget) < 49.0) {
            this.entity.m_21573_().setSpeedModifier(this.nearSpeed);
        } else {
            this.entity.m_21573_().setSpeedModifier(this.farSpeed);
        }
    }
}