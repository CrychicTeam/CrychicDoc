package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.SubterranodonEntity;
import java.util.EnumSet;
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

public class SubterranodonFollowOwnerGoal extends Goal {

    private final SubterranodonEntity subterranodon;

    private LivingEntity owner;

    private final LevelReader world;

    private final double followSpeed;

    private final PathNavigation navigator;

    private int timeToRecalcPath;

    private final float maxDist;

    private final float minDist;

    private float oldWaterCost;

    private final boolean teleportToLeaves;

    public SubterranodonFollowOwnerGoal(SubterranodonEntity subterranodon, double speed, float minDist, float maxDist, boolean teleportToLeaves) {
        this.subterranodon = subterranodon;
        this.world = subterranodon.m_9236_();
        this.followSpeed = speed;
        this.navigator = subterranodon.m_21573_();
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.teleportToLeaves = teleportToLeaves;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.subterranodon.m_269323_();
        if (livingentity == null) {
            return false;
        } else if (livingentity.m_5833_()) {
            return false;
        } else if (this.subterranodon.m_21827_()) {
            return false;
        } else if (!(this.subterranodon.m_20280_(livingentity) < (double) (this.minDist * this.minDist)) && !this.isInCombat()) {
            this.owner = livingentity;
            return this.subterranodon.getCommand() == 2;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.subterranodon.m_21827_() && !this.isInCombat() ? this.subterranodon.m_20280_(this.owner) > (double) (this.maxDist * this.maxDist) : false;
    }

    private boolean isInCombat() {
        Entity owner = this.subterranodon.m_269323_();
        return owner == null ? false : this.subterranodon.m_20270_(owner) < 30.0F && this.subterranodon.m_5448_() != null && this.subterranodon.m_5448_().isAlive();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.subterranodon.m_21439_(BlockPathTypes.WATER);
        this.subterranodon.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigator.stop();
        this.subterranodon.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.subterranodon.m_21563_().setLookAt(this.owner, 10.0F, (float) this.subterranodon.m_8132_());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.subterranodon.m_21523_() && !this.subterranodon.m_20159_()) {
                if (this.subterranodon.m_20280_(this.owner) >= 144.0) {
                    this.tryToTeleportNearEntity();
                }
                if (this.subterranodon.m_20270_(this.owner) > 5.0F) {
                    if (!this.subterranodon.isFlying()) {
                        this.subterranodon.setFlying(true);
                        this.subterranodon.setHovering(true);
                    }
                    this.subterranodon.m_21566_().setWantedPosition(this.owner.m_20185_(), this.owner.m_20186_() + (double) this.owner.m_20206_(), this.owner.m_20189_(), this.followSpeed);
                } else {
                    if (this.subterranodon.m_20096_()) {
                        this.subterranodon.setFlying(false);
                    }
                    this.subterranodon.m_21573_().moveTo(this.owner, this.followSpeed);
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
            this.subterranodon.m_7678_((double) x + 0.5, (double) y, (double) z + 0.5, this.subterranodon.m_146908_(), this.subterranodon.m_146909_());
            this.navigator.stop();
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(BlockPos pos) {
        if (this.world.m_8055_(pos).m_60795_()) {
            BlockPos blockpos = pos.subtract(this.subterranodon.m_20183_());
            return this.world.m_45756_(this.subterranodon, this.subterranodon.m_20191_().move(blockpos));
        } else {
            BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, pos.mutable());
            if (pathnodetype != BlockPathTypes.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = this.world.m_8055_(pos.below());
                if (!this.teleportToLeaves && blockstate.m_60734_() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(this.subterranodon.m_20183_());
                    return this.world.m_45756_(this.subterranodon, this.subterranodon.m_20191_().move(blockpos));
                }
            }
        }
    }

    private int getRandomNumber(int min, int max) {
        return this.subterranodon.m_217043_().nextInt(max - min + 1) + min;
    }
}