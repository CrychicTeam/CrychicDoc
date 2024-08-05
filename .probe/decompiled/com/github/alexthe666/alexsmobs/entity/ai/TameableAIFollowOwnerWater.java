package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.IFollower;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class TameableAIFollowOwnerWater extends Goal {

    private final TamableAnimal tameable;

    private final LevelReader world;

    private final double followSpeed;

    private final float maxDist;

    private final float minDist;

    private final boolean teleportToLeaves;

    private LivingEntity owner;

    private int timeToRecalcPath;

    private float oldWaterCost;

    public TameableAIFollowOwnerWater(TamableAnimal tamed, double speed, float minDist, float maxDist, boolean leaves) {
        this.tameable = tamed;
        this.world = tamed.m_9236_();
        this.followSpeed = speed;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.teleportToLeaves = leaves;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity lvt_1_1_ = this.tameable.m_269323_();
        if (lvt_1_1_ == null) {
            return false;
        } else if (lvt_1_1_.m_5833_()) {
            return false;
        } else if (((IFollower) this.tameable).shouldFollow() && !this.isInCombat()) {
            if (this.tameable.m_20280_(lvt_1_1_) < (double) (this.minDist * this.minDist)) {
                return false;
            } else if (this.tameable.m_5448_() != null && this.tameable.m_5448_().isAlive()) {
                return false;
            } else {
                this.owner = lvt_1_1_;
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.tameable.m_21573_().isDone() || this.isInCombat()) {
            return false;
        } else if (!((IFollower) this.tameable).shouldFollow()) {
            return false;
        } else {
            return this.tameable.m_5448_() != null && this.tameable.m_5448_().isAlive() ? false : this.tameable.m_20280_(this.owner) > (double) (this.maxDist * this.maxDist);
        }
    }

    private boolean isInCombat() {
        Entity owner = this.tameable.m_269323_();
        return owner == null ? false : this.tameable.m_20270_(owner) < 30.0F && this.tameable.m_5448_() != null && this.tameable.m_5448_().isAlive();
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tameable.m_21439_(BlockPathTypes.WATER);
        this.tameable.m_21441_(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.tameable.m_21573_().stop();
        this.tameable.m_21441_(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        this.tameable.m_21563_().setLookAt(this.owner, 10.0F, (float) this.tameable.m_8132_());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.tameable.m_21523_() && !this.tameable.m_20159_()) {
                if (this.tameable.m_20280_(this.owner) >= 144.0) {
                    this.tryToTeleportNearEntity();
                } else {
                    this.tameable.m_21573_().moveTo(this.owner, this.followSpeed);
                }
            }
        }
    }

    private void tryToTeleportNearEntity() {
        BlockPos lvt_1_1_ = this.owner.m_20183_();
        for (int lvt_2_1_ = 0; lvt_2_1_ < 10; lvt_2_1_++) {
            int lvt_3_1_ = this.getRandomNumber(-3, 3);
            int lvt_4_1_ = this.getRandomNumber(-1, 1);
            int lvt_5_1_ = this.getRandomNumber(-3, 3);
            boolean lvt_6_1_ = this.tryToTeleportToLocation(lvt_1_1_.m_123341_() + lvt_3_1_, lvt_1_1_.m_123342_() + lvt_4_1_, lvt_1_1_.m_123343_() + lvt_5_1_);
            if (lvt_6_1_) {
                return;
            }
        }
    }

    private boolean tryToTeleportToLocation(int p_226328_1_, int p_226328_2_, int p_226328_3_) {
        if (Math.abs((double) p_226328_1_ - this.owner.m_20185_()) < 2.0 && Math.abs((double) p_226328_3_ - this.owner.m_20189_()) < 2.0) {
            return false;
        } else if (!this.isTeleportFriendlyBlock(new BlockPos(p_226328_1_, p_226328_2_, p_226328_3_))) {
            return false;
        } else {
            this.tameable.m_7678_((double) p_226328_1_ + 0.5, (double) p_226328_2_, (double) p_226328_3_ + 0.5, this.tameable.m_146908_(), this.tameable.m_146909_());
            this.tameable.m_21573_().stop();
            return true;
        }
    }

    private boolean isTeleportFriendlyBlock(BlockPos pos) {
        BlockPathTypes blockPathType = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, pos.mutable());
        if (!this.world.m_6425_(pos).is(FluidTags.WATER) && (this.world.m_6425_(pos).is(FluidTags.WATER) || !this.world.m_6425_(pos.below()).is(FluidTags.WATER))) {
            if (blockPathType == BlockPathTypes.WALKABLE && !this.avoidsLand()) {
                BlockState lvt_3_1_ = this.world.m_8055_(pos.below());
                if (!this.teleportToLeaves && lvt_3_1_.m_60734_() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos lvt_4_1_ = pos.subtract(this.tameable.m_20183_());
                    return this.world.m_45756_(this.tameable, this.tameable.m_20191_().move(lvt_4_1_));
                }
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean avoidsLand() {
        return this.tameable instanceof EntityMimicOctopus mimicOctopus ? mimicOctopus.getMoistness() < 2000 : false;
    }

    private int getRandomNumber(int p_226327_1_, int p_226327_2_) {
        return this.tameable.m_217043_().nextInt(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
    }
}