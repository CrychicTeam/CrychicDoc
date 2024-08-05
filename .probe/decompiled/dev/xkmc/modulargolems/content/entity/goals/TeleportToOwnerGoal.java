package dev.xkmc.modulargolems.content.entity.goals;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class TeleportToOwnerGoal extends Goal {

    private final AbstractGolemEntity<?, ?> golem;

    private final LevelReader level;

    private final PathNavigation navigation;

    private final boolean canFly;

    public TeleportToOwnerGoal(AbstractGolemEntity<?, ?> golem) {
        this(golem, false);
    }

    private TeleportToOwnerGoal(AbstractGolemEntity<?, ?> golem, boolean fly) {
        this.golem = golem;
        this.level = golem.m_9236_();
        this.navigation = golem.m_21573_();
        this.canFly = fly;
    }

    @Override
    public boolean canUse() {
        if (this.golem.isInSittingPose() || !this.golem.getMode().isMovable()) {
            return false;
        } else if (this.golem.m_21523_()) {
            return false;
        } else {
            Vec3 target = this.golem.getTargetPos();
            double maxDist = MGConfig.COMMON.maxWanderRadius.get();
            return this.golem.m_20238_(target) >= maxDist * maxDist;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void start() {
        this.teleportToOwner();
    }

    @Override
    public void stop() {
        this.navigation.stop();
    }

    private void teleportToOwner() {
        BlockPos blockpos = BlockPos.containing(this.golem.getTargetPos());
        for (int i = 0; i < 10; i++) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.m_123341_() + j, blockpos.m_123342_() + k, blockpos.m_123343_() + l);
            if (flag) {
                this.golem.setTarget(null);
                return;
            }
        }
    }

    private boolean maybeTeleportTo(int pX, int pY, int pZ) {
        Vec3 target = this.golem.getTargetPos();
        if (Math.abs((double) pX - target.x()) < 2.0 && Math.abs((double) pZ - target.z()) < 2.0) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
            return false;
        } else {
            Entity e = this.golem;
            while (e.getControlledVehicle() instanceof LivingEntity le) {
                e = le;
            }
            e.moveTo((double) pX + 0.5, (double) pY, (double) pZ + 0.5, this.golem.m_146908_(), this.golem.m_146909_());
            this.navigation.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos pPos) {
        BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pPos.mutable());
        boolean allow = blockpathtypes == BlockPathTypes.WALKABLE;
        if (this.golem.hasFlag(GolemFlags.FLOAT) || this.golem.hasFlag(GolemFlags.SWIM)) {
            allow |= blockpathtypes == BlockPathTypes.WATER;
            allow |= blockpathtypes == BlockPathTypes.WATER_BORDER;
        }
        if (!allow) {
            return false;
        } else {
            BlockState blockstate = this.level.m_8055_(pPos.below());
            if (!this.canFly && blockstate.m_60734_() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pPos.subtract(this.golem.m_20183_());
                return this.level.m_45756_(this.golem, this.golem.m_20191_().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int pMin, int pMax) {
        return this.golem.m_217043_().nextInt(pMax - pMin + 1) + pMin;
    }
}