package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

public abstract class PathfinderMob extends Mob {

    protected static final float DEFAULT_WALK_TARGET_VALUE = 0.0F;

    protected PathfinderMob(EntityType<? extends PathfinderMob> entityTypeExtendsPathfinderMob0, Level level1) {
        super(entityTypeExtendsPathfinderMob0, level1);
    }

    public float getWalkTargetValue(BlockPos blockPos0) {
        return this.getWalkTargetValue(blockPos0, this.m_9236_());
    }

    public float getWalkTargetValue(BlockPos blockPos0, LevelReader levelReader1) {
        return 0.0F;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor levelAccessor0, MobSpawnType mobSpawnType1) {
        return this.getWalkTargetValue(this.m_20183_(), levelAccessor0) >= 0.0F;
    }

    public boolean isPathFinding() {
        return !this.m_21573_().isDone();
    }

    @Override
    protected void tickLeash() {
        super.tickLeash();
        Entity $$0 = this.m_21524_();
        if ($$0 != null && $$0.level() == this.m_9236_()) {
            this.m_21446_($$0.blockPosition(), 5);
            float $$1 = this.m_20270_($$0);
            if (this instanceof TamableAnimal && ((TamableAnimal) this).isInSittingPose()) {
                if ($$1 > 10.0F) {
                    this.m_21455_(true, true);
                }
                return;
            }
            this.onLeashDistance($$1);
            if ($$1 > 10.0F) {
                this.m_21455_(true, true);
                this.f_21345_.disableControlFlag(Goal.Flag.MOVE);
            } else if ($$1 > 6.0F) {
                double $$2 = ($$0.getX() - this.m_20185_()) / (double) $$1;
                double $$3 = ($$0.getY() - this.m_20186_()) / (double) $$1;
                double $$4 = ($$0.getZ() - this.m_20189_()) / (double) $$1;
                this.m_20256_(this.m_20184_().add(Math.copySign($$2 * $$2 * 0.4, $$2), Math.copySign($$3 * $$3 * 0.4, $$3), Math.copySign($$4 * $$4 * 0.4, $$4)));
                this.m_245125_();
            } else if (this.shouldStayCloseToLeashHolder()) {
                this.f_21345_.enableControlFlag(Goal.Flag.MOVE);
                float $$5 = 2.0F;
                Vec3 $$6 = new Vec3($$0.getX() - this.m_20185_(), $$0.getY() - this.m_20186_(), $$0.getZ() - this.m_20189_()).normalize().scale((double) Math.max($$1 - 2.0F, 0.0F));
                this.m_21573_().moveTo(this.m_20185_() + $$6.x, this.m_20186_() + $$6.y, this.m_20189_() + $$6.z, this.followLeashSpeed());
            }
        }
    }

    protected boolean shouldStayCloseToLeashHolder() {
        return true;
    }

    protected double followLeashSpeed() {
        return 1.0;
    }

    protected void onLeashDistance(float float0) {
    }
}