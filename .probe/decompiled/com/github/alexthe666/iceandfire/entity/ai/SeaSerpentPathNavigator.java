package com.github.alexthe666.iceandfire.entity.ai;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SeaSerpentPathNavigator extends PathNavigation {

    public SeaSerpentPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    @NotNull
    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        this.f_26508_ = new SwimNodeEvaluator(true);
        return new PathFinder(this.f_26508_, p_179679_1_);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @NotNull
    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.f_26494_.m_20185_(), this.f_26494_.m_20227_(0.5), this.f_26494_.m_20189_());
    }

    @Override
    public void tick() {
        this.f_26498_++;
        if (this.f_26506_) {
            this.m_26569_();
        }
        if (!this.m_26571_()) {
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.f_26496_ != null && !this.f_26496_.isDone()) {
                Vec3 lvt_1_2_ = this.f_26496_.getNextEntityPos(this.f_26494_);
                if (Mth.floor(this.f_26494_.m_20185_()) == Mth.floor(lvt_1_2_.x) && Mth.floor(this.f_26494_.m_20186_()) == Mth.floor(lvt_1_2_.y) && Mth.floor(this.f_26494_.m_20189_()) == Mth.floor(lvt_1_2_.z)) {
                    this.f_26496_.advance();
                }
            }
            DebugPackets.sendPathFindingPacket(this.f_26495_, this.f_26494_, this.f_26496_, this.f_26505_);
            if (!this.m_26571_()) {
                Vec3 lvt_1_2_ = this.f_26496_.getNextEntityPos(this.f_26494_);
                this.f_26494_.getMoveControl().setWantedPosition(lvt_1_2_.x, lvt_1_2_.y, lvt_1_2_.z, this.f_26497_);
            }
        }
    }

    @Override
    protected void followThePath() {
        if (this.f_26496_ != null) {
            Vec3 entityPos = this.getTempMobPos();
            float entityWidth = this.f_26494_.m_20205_();
            float lvt_3_1_ = entityWidth > 0.75F ? entityWidth / 2.0F : 0.75F - entityWidth / 2.0F;
            Vec3 lvt_4_1_ = this.f_26494_.m_20184_();
            if (Math.abs(lvt_4_1_.x) > 0.2 || Math.abs(lvt_4_1_.z) > 0.2) {
                lvt_3_1_ = (float) ((double) lvt_3_1_ * lvt_4_1_.length() * 6.0);
            }
            Vec3 lvt_6_1_ = Vec3.atCenterOf(this.f_26496_.getNextNodePos());
            if (Math.abs(this.f_26494_.m_20185_() - lvt_6_1_.x) < (double) lvt_3_1_ && Math.abs(this.f_26494_.m_20189_() - lvt_6_1_.z) < (double) lvt_3_1_ && Math.abs(this.f_26494_.m_20186_() - lvt_6_1_.y) < (double) (lvt_3_1_ * 2.0F)) {
                this.f_26496_.advance();
            }
            for (int lvt_7_1_ = Math.min(this.f_26496_.getNextNodeIndex() + 6, this.f_26496_.getNodeCount() - 1); lvt_7_1_ > this.f_26496_.getNextNodeIndex(); lvt_7_1_--) {
                lvt_6_1_ = this.f_26496_.getEntityPosAtNode(this.f_26494_, lvt_7_1_);
                if (lvt_6_1_.distanceToSqr(entityPos) <= 36.0 && this.canMoveDirectly(entityPos, lvt_6_1_)) {
                    this.f_26496_.setNextNodeIndex(lvt_7_1_);
                    break;
                }
            }
            this.doStuckDetection(entityPos);
        }
    }

    @Override
    protected void doStuckDetection(@NotNull Vec3 positionVec3) {
        if (this.f_26498_ - this.f_26499_ > 100) {
            if (positionVec3.distanceToSqr(this.f_26500_) < 2.25) {
                this.m_26573_();
            }
            this.f_26499_ = this.f_26498_;
            this.f_26500_ = positionVec3;
        }
        if (this.f_26496_ != null && !this.f_26496_.isDone()) {
            Vec3i lvt_2_1_ = this.f_26496_.getNextNodePos();
            if (lvt_2_1_.equals(this.f_26501_)) {
                this.f_26502_ = this.f_26502_ + (Util.getMillis() - this.f_26503_);
            } else {
                this.f_26501_ = lvt_2_1_;
                double lvt_3_1_ = positionVec3.distanceTo(Vec3.atCenterOf(this.f_26501_));
                this.f_26504_ = this.f_26494_.m_6113_() > 0.0F ? lvt_3_1_ / (double) this.f_26494_.m_6113_() * 100.0 : 0.0;
            }
            if (this.f_26504_ > 0.0 && (double) this.f_26502_ > this.f_26504_ * 2.0) {
                this.f_26501_ = Vec3i.ZERO;
                this.f_26502_ = 0L;
                this.f_26504_ = 0.0;
                this.m_26573_();
            }
            this.f_26503_ = Util.getMillis();
        }
    }

    @Override
    protected boolean canMoveDirectly(@NotNull Vec3 posVec31, Vec3 posVec32) {
        Vec3 lvt_6_1_ = new Vec3(posVec32.x, posVec32.y + (double) this.f_26494_.m_20206_() * 0.5, posVec32.z);
        return this.f_26495_.m_45547_(new ClipContext(posVec31, lvt_6_1_, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_26494_)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean isStableDestination(@NotNull BlockPos pos) {
        return !this.f_26495_.getBlockState(pos).m_60804_(this.f_26495_, pos);
    }

    @Override
    public void setCanFloat(boolean canSwim) {
    }
}