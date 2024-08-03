package com.mna.entities.ai;

import com.mna.tools.SummonUtils;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class FollowOwnerGoal extends Goal {

    private final Mob tameable;

    private LivingEntity owner;

    private final LevelReader world;

    private final double followSpeed;

    private final PathNavigation navigator;

    private int timeToRecalcPath;

    private final float pathDist;

    private final float attackDist;

    private final float snapDist;

    private float oldWaterCost;

    private final boolean teleportToLeaves;

    public FollowOwnerGoal(Mob tameable, double speed, float pathDist, float attackDist, float snapDist, boolean teleportToLeaves) {
        this.tameable = tameable;
        this.world = tameable.m_9236_();
        this.followSpeed = speed;
        this.navigator = tameable.getNavigation();
        this.pathDist = pathDist;
        this.snapDist = snapDist;
        this.attackDist = attackDist;
        this.teleportToLeaves = teleportToLeaves;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(tameable.getNavigation() instanceof GroundPathNavigation) && !(tameable.getNavigation() instanceof FlyingPathNavigation) && !(tameable.getNavigation() instanceof WaterBoundPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = SummonUtils.getSummoner(this.tameable);
        if (livingentity == null) {
            this.tameable.m_6469_(this.tameable.m_269291_().fellOutOfWorld(), Float.MAX_VALUE);
            return false;
        } else if (this.tameable.m_20280_(livingentity) < (double) (this.pathDist * this.pathDist)) {
            return false;
        } else if (this.tameable.getTarget() != null && this.tameable.m_20280_(livingentity) < (double) (this.attackDist * this.attackDist)) {
            return false;
        } else {
            this.owner = livingentity;
            this.tameable.setTarget(null);
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.navigator.isDone() && this.owner != null;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tameable.getPathfindingMalus(BlockPathTypes.WATER);
        this.tameable.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigator.stop();
        this.tameable.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.tameable.getLookControl().setLookAt(this.owner, 10.0F, (float) this.tameable.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.tameable.isLeashed() && !this.tameable.m_20159_()) {
                if (this.tameable.m_20280_(this.owner) >= (double) (this.snapDist * this.snapDist)) {
                    this.tryToTeleportNearEntity();
                } else {
                    this.navigator.moveTo(this.owner, this.followSpeed);
                }
            }
        }
    }

    private void tryToTeleportNearEntity() {
        BlockPos blockpos = this.owner.m_20183_();
        for (int i = 0; i < 10; i++) {
            int j = this.getRandomNumber(-3, 3);
            int k = this.getRandomNumber(-1, 1);
            int l = this.getRandomNumber(-3, 3);
            boolean flag = this.tryToTeleportToLocation(blockpos.m_123341_() + j, blockpos.m_123342_() + k, blockpos.m_123343_() + l);
            if (flag) {
                return;
            }
        }
    }

    private boolean tryToTeleportToLocation(int x, int y, int z) {
        if (Math.abs((double) x - this.owner.m_20185_()) < 2.0 && Math.abs((double) z - this.owner.m_20189_()) < 2.0) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
            return false;
        } else {
            this.tameable.m_7678_((double) x + 0.5, (double) y, (double) z + 0.5, this.tameable.m_146908_(), this.tameable.m_146909_());
            this.navigator.stop();
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(BlockPos pos) {
        BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, pos.mutable());
        if (pathnodetype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.world.m_8055_(pos.below());
            if (!this.teleportToLeaves && blockstate.m_60734_() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = pos.subtract(this.tameable.m_20183_());
                return this.world.m_45756_(this.tameable, this.tameable.m_20191_().move(blockpos));
            }
        }
    }

    private int getRandomNumber(int min, int max) {
        return this.tameable.m_217043_().nextInt(max - min + 1) + min;
    }
}