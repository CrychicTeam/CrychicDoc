package com.github.alexthe666.alexsmobs.entity.ai;

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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BoneSerpentPathNavigator extends PathNavigation {

    public BoneSerpentPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        this.f_26508_ = new BoneSerpentNodeProcessor();
        return new PathFinder(this.f_26508_, p_179679_1_);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

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
                Vec3 vector3d = this.f_26496_.getNextEntityPos(this.f_26494_);
                if (Mth.floor(this.f_26494_.m_20185_()) == Mth.floor(vector3d.x) && Mth.floor(this.f_26494_.m_20186_()) == Mth.floor(vector3d.y) && Mth.floor(this.f_26494_.m_20189_()) == Mth.floor(vector3d.z)) {
                    this.f_26496_.advance();
                }
            }
            DebugPackets.sendPathFindingPacket(this.f_26495_, this.f_26494_, this.f_26496_, this.f_26505_);
            if (!this.m_26571_()) {
                Vec3 vector3d1 = this.f_26496_.getNextEntityPos(this.f_26494_);
                this.f_26494_.getMoveControl().setWantedPosition(vector3d1.x, vector3d1.y, vector3d1.z, this.f_26497_);
            }
        }
    }

    @Override
    protected void followThePath() {
        if (this.f_26496_ != null) {
            Vec3 vector3d = this.getTempMobPos();
            float f = this.f_26494_.m_20205_();
            float f1 = 3.0F;
            Vec3 vector3d1 = this.f_26494_.m_20184_();
            if (Math.abs(vector3d1.x) > 0.2 || Math.abs(vector3d1.z) > 0.2) {
                f1 = (float) ((double) f1 * vector3d1.length() * 6.0);
            }
            int i = 6;
            Vec3 vector3d2 = Vec3.atBottomCenterOf(this.f_26496_.getNextNodePos());
            if (Math.abs(this.f_26494_.m_20185_() - vector3d2.x) < (double) f1 && Math.abs(this.f_26494_.m_20189_() - vector3d2.z) < (double) f1 && Math.abs(this.f_26494_.m_20186_() - vector3d2.y) < (double) (f1 * 2.0F)) {
                this.f_26496_.advance();
            }
            for (int j = Math.min(this.f_26496_.getNextNodeIndex() + 6, this.f_26496_.getNodeCount() - 1); j > this.f_26496_.getNextNodeIndex(); j--) {
                vector3d2 = this.f_26496_.getEntityPosAtNode(this.f_26494_, j);
                if (!(vector3d2.distanceToSqr(vector3d) > 36.0) && this.canMoveDirectly(vector3d, vector3d2, 0, 0, 0)) {
                    this.f_26496_.setNextNodeIndex(j);
                    break;
                }
            }
            this.doStuckDetection(vector3d);
        }
    }

    @Override
    protected void doStuckDetection(Vec3 positionVec3) {
        if (this.f_26498_ - this.f_26499_ > 100) {
            if (positionVec3.distanceToSqr(this.f_26500_) < 2.25) {
                this.m_26573_();
            }
            this.f_26499_ = this.f_26498_;
            this.f_26500_ = positionVec3;
        }
        if (this.f_26496_ != null && !this.f_26496_.isDone()) {
            Vec3i vector3i = this.f_26496_.getNextNodePos();
            if (vector3i.equals(this.f_26501_)) {
                this.f_26502_ = this.f_26502_ + (Util.getMillis() - this.f_26503_);
            } else {
                this.f_26501_ = vector3i;
                double d0 = positionVec3.distanceTo(Vec3.atCenterOf(this.f_26501_));
                this.f_26504_ = this.f_26494_.m_6113_() > 0.0F ? d0 / (double) this.f_26494_.m_6113_() * 100.0 : 0.0;
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

    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
        Vec3 vector3d = new Vec3(posVec32.x, posVec32.y + (double) this.f_26494_.m_20206_() * 0.5, posVec32.z);
        return this.f_26495_.m_45547_(new ClipContext(posVec31, vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_26494_)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !this.f_26495_.getBlockState(pos).m_60804_(this.f_26495_, pos);
    }

    @Override
    public void setCanFloat(boolean canSwim) {
    }
}