package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.AtlatitanEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class AtlatitanNibbleTreesGoal extends MoveToBlockGoal {

    private AtlatitanEntity atlatitan;

    private boolean stopFlag = false;

    private int reachCheckTime = 50;

    public AtlatitanNibbleTreesGoal(AtlatitanEntity atlatitan, int range) {
        super(atlatitan, 1.0, range, 16);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        this.atlatitan = atlatitan;
    }

    @Override
    public boolean canUse() {
        this.f_25603_ = this.atlatitan.m_6162_() ? 3 : 6;
        return super.canUse();
    }

    @Override
    protected int nextStartTick(PathfinderMob mob) {
        return m_186073_(200 + this.atlatitan.m_217043_().nextInt(200));
    }

    @Override
    public double acceptedDistance() {
        return (double) ((int) Math.floor((double) (14.0F * this.atlatitan.getScale())));
    }

    @Override
    protected boolean isReachedTarget() {
        BlockPos target = this.getMoveToTarget();
        return target != null && this.atlatitan.m_20275_((double) ((float) target.m_123341_() + 0.5F), this.atlatitan.m_20186_(), (double) ((float) target.m_123343_() + 0.5F)) < this.acceptedDistance();
    }

    @Override
    protected BlockPos getMoveToTarget() {
        return this.atlatitan.getStandAtTreePos(this.f_25602_);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos target = this.getMoveToTarget();
        if (target != null) {
            if (this.reachCheckTime > 0) {
                this.reachCheckTime--;
            } else {
                this.reachCheckTime = 50 + this.atlatitan.m_217043_().nextInt(100);
                if (!this.canReach(target)) {
                    this.stopFlag = true;
                    this.f_25602_ = BlockPos.ZERO;
                    return;
                }
            }
            if (this.isReachedTarget()) {
                if (this.atlatitan.lockTreePosition(this.f_25602_)) {
                    if (this.atlatitan.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                        this.atlatitan.setEatingPos(this.f_25602_);
                        this.atlatitan.setAnimation(AtlatitanEntity.ANIMATION_EAT_LEAVES);
                    } else if (this.atlatitan.getAnimation() == AtlatitanEntity.ANIMATION_EAT_LEAVES) {
                        if (this.atlatitan.getAnimationTick() >= 35) {
                            this.stopFlag = true;
                            this.f_25602_ = BlockPos.ZERO;
                            return;
                        }
                        if (this.atlatitan.getAnimationTick() == 20) {
                            BlockState back = this.atlatitan.m_9236_().getBlockState(this.f_25602_);
                            this.atlatitan.setLastEatenBlock(back);
                            this.atlatitan.m_9236_().m_46953_(this.f_25602_, false, this.atlatitan);
                            this.atlatitan.m_9236_().setBlock(this.f_25602_, back, 3);
                        }
                    }
                }
            } else if (this.atlatitan.m_21573_().isDone()) {
                Vec3 vec31 = Vec3.atCenterOf(target);
                this.atlatitan.m_21566_().setWantedPosition(vec31.x, this.atlatitan.m_20186_(), vec31.z, 1.0);
            }
        }
    }

    @Override
    protected void moveMobToBlock() {
        BlockPos pos = this.getMoveToTarget();
        this.f_25598_.m_21573_().moveTo((double) ((float) pos.m_123341_()) + 0.5, (double) pos.m_123342_(), (double) ((float) pos.m_123343_()) + 0.5, this.f_25599_);
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !this.stopFlag;
    }

    @Override
    public void stop() {
        this.f_25602_ = BlockPos.ZERO;
        super.m_8041_();
        this.stopFlag = false;
    }

    private int getHeightOfBlock(LevelReader worldIn, BlockPos pos) {
        int i;
        for (i = 0; pos.m_123342_() > worldIn.getMinBuildHeight() && (worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_NIBBLES) || worldIn.m_8055_(pos).m_60795_() || worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_KNOCKABLE_LOGS)); i++) {
            pos = pos.below();
        }
        return i;
    }

    private boolean highEnough(LevelReader worldIn, BlockPos pos) {
        int height = this.getHeightOfBlock(worldIn, pos);
        return this.atlatitan.m_6162_() ? height <= 2 : height > 3 && height < 20;
    }

    @Override
    protected boolean isValidTarget(LevelReader worldIn, BlockPos pos) {
        return worldIn.m_8055_(pos).m_204336_(ACTagRegistry.RELICHEIRUS_NIBBLES) && this.highEnough(worldIn, pos);
    }

    private boolean canReach(BlockPos target) {
        Path path = this.atlatitan.m_21573_().createPath(target, 0);
        if (path == null) {
            return false;
        } else {
            Node node = path.getEndNode();
            if (node == null) {
                return false;
            } else {
                int i = node.x - target.m_123341_();
                int j = node.y - target.m_123342_();
                int k = node.z - target.m_123343_();
                return (double) (i * i + j * j + k * k) <= 3.0;
            }
        }
    }
}