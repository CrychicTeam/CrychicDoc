package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.citadel.server.entity.collision.ICustomCollisions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MovementControllerCustomCollisions extends MoveControl {

    public MovementControllerCustomCollisions(Mob mob) {
        super(mob);
    }

    @Override
    public void tick() {
        if (this.f_24981_ == MoveControl.Operation.STRAFE) {
            float f = (float) this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED);
            float f1 = (float) this.f_24978_ * f;
            float f2 = this.f_24979_;
            float f3 = this.f_24980_;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }
            f4 = f1 / f4;
            f2 *= f4;
            f3 *= f4;
            float f5 = Mth.sin(this.f_24974_.m_146908_() * (float) (Math.PI / 180.0));
            float f6 = Mth.cos(this.f_24974_.m_146908_() * (float) (Math.PI / 180.0));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            if (!this.isWalkable(f7, f8)) {
                this.f_24979_ = 1.0F;
                this.f_24980_ = 0.0F;
            }
            this.f_24974_.setSpeed(f1);
            this.f_24974_.setZza(this.f_24979_);
            this.f_24974_.setXxa(this.f_24980_);
            this.f_24981_ = MoveControl.Operation.WAIT;
        } else if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            this.f_24981_ = MoveControl.Operation.WAIT;
            double d0 = this.f_24975_ - this.f_24974_.m_20185_();
            double d1 = this.f_24977_ - this.f_24974_.m_20189_();
            double d2 = this.f_24976_ - this.f_24974_.m_20186_();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < 2.5000003E-7F) {
                this.f_24974_.setZza(0.0F);
                return;
            }
            float f9 = (float) (Mth.atan2(d1, d0) * 180.0F / (float) Math.PI) - 90.0F;
            this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), f9, 90.0F));
            this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
            BlockPos blockpos = this.f_24974_.m_20183_();
            BlockState blockstate = this.f_24974_.m_9236_().getBlockState(blockpos);
            VoxelShape voxelshape = blockstate.m_60816_(this.f_24974_.m_9236_(), blockpos);
            if ((!(this.f_24974_ instanceof ICustomCollisions) || !((ICustomCollisions) this.f_24974_).canPassThrough(blockpos, blockstate, voxelshape)) && (d2 > (double) this.f_24974_.getStepHeight() && d0 * d0 + d1 * d1 < (double) Math.max(1.0F, this.f_24974_.m_20205_()) || !voxelshape.isEmpty() && this.f_24974_.m_20186_() < voxelshape.max(Direction.Axis.Y) + (double) blockpos.m_123342_() && !blockstate.m_204336_(BlockTags.DOORS) && !blockstate.m_204336_(BlockTags.FENCES))) {
                this.f_24974_.getJumpControl().jump();
                this.f_24981_ = MoveControl.Operation.JUMPING;
            }
        } else if (this.f_24981_ == MoveControl.Operation.JUMPING) {
            this.f_24974_.setSpeed((float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED)));
            if (this.f_24974_.m_20096_()) {
                this.f_24981_ = MoveControl.Operation.WAIT;
            }
        } else {
            this.f_24974_.setZza(0.0F);
        }
    }

    private boolean isWalkable(float p_234024_1_, float p_234024_2_) {
        PathNavigation pathnavigator = this.f_24974_.getNavigation();
        if (pathnavigator != null) {
            NodeEvaluator nodeprocessor = pathnavigator.getNodeEvaluator();
            if (nodeprocessor != null && nodeprocessor.getBlockPathType(this.f_24974_.m_9236_(), Mth.floor(this.f_24974_.m_20185_() + (double) p_234024_1_), Mth.floor(this.f_24974_.m_20186_()), Mth.floor(this.f_24974_.m_20189_() + (double) p_234024_2_)) != BlockPathTypes.WALKABLE) {
                return false;
            }
        }
        return true;
    }
}