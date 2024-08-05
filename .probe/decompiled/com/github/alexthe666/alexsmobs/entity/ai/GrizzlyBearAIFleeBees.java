package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.google.common.base.Predicate;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class GrizzlyBearAIFleeBees extends Goal {

    private final double farSpeed;

    private final double nearSpeed;

    private final float avoidDistance;

    private final Predicate<Bee> avoidTargetSelector = new Predicate<Bee>() {

        public boolean apply(@Nullable Bee p_apply_1_) {
            return p_apply_1_.m_6084_() && GrizzlyBearAIFleeBees.this.entity.m_21574_().hasLineOfSight(p_apply_1_) && !GrizzlyBearAIFleeBees.this.entity.isAlliedTo(p_apply_1_) && p_apply_1_.getRemainingPersistentAngerTime() > 0;
        }
    };

    protected EntityGrizzlyBear entity;

    protected Bee closestLivingEntity;

    private Path path;

    public GrizzlyBearAIFleeBees(EntityGrizzlyBear entityIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
        this.entity = entityIn;
        this.avoidDistance = avoidDistanceIn;
        this.farSpeed = farSpeedIn;
        this.nearSpeed = nearSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.entity.m_21824_()) {
            return false;
        } else {
            if (this.entity.isSitting() && !this.entity.forcedSit) {
                this.entity.setOrderedToSit(false);
            }
            if (this.entity.isSitting()) {
                return false;
            } else {
                List<Bee> beeEntities = this.entity.m_9236_().m_6443_(Bee.class, this.entity.m_20191_().inflate((double) this.avoidDistance, 8.0, (double) this.avoidDistance), this.avoidTargetSelector);
                if (beeEntities.isEmpty()) {
                    return false;
                } else {
                    this.closestLivingEntity = (Bee) beeEntities.get(0);
                    Vec3 vec3d = LandRandomPos.getPosAway(this.entity, 16, 7, new Vec3(this.closestLivingEntity.m_20185_(), this.closestLivingEntity.m_20186_(), this.closestLivingEntity.m_20189_()));
                    if (vec3d == null) {
                        return false;
                    } else if (this.closestLivingEntity.m_20275_(vec3d.x, vec3d.y, vec3d.z) < this.closestLivingEntity.m_20280_(this.entity)) {
                        return false;
                    } else {
                        this.path = this.entity.m_21573_().createPath(AMBlockPos.fromCoords(vec3d.x, vec3d.y, vec3d.z), 0);
                        return this.path != null;
                    }
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.m_21573_().isDone();
    }

    @Override
    public void start() {
        this.entity.m_21573_().moveTo(this.path, this.farSpeed);
    }

    @Override
    public void stop() {
        this.entity.m_21573_().stop();
        this.closestLivingEntity = null;
    }

    @Override
    public void tick() {
        if (this.closestLivingEntity != null && this.closestLivingEntity.getRemainingPersistentAngerTime() <= 0) {
            this.stop();
        }
        this.entity.m_21573_().setSpeedModifier(this.getRunSpeed());
    }

    public double getRunSpeed() {
        return 0.7F;
    }
}