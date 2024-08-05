package net.minecraft.world.entity.ai.control;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MoveControl implements Control {

    public static final float MIN_SPEED = 5.0E-4F;

    public static final float MIN_SPEED_SQR = 2.5000003E-7F;

    protected static final int MAX_TURN = 90;

    protected final Mob mob;

    protected double wantedX;

    protected double wantedY;

    protected double wantedZ;

    protected double speedModifier;

    protected float strafeForwards;

    protected float strafeRight;

    protected MoveControl.Operation operation = MoveControl.Operation.WAIT;

    public MoveControl(Mob mob0) {
        this.mob = mob0;
    }

    public boolean hasWanted() {
        return this.operation == MoveControl.Operation.MOVE_TO;
    }

    public double getSpeedModifier() {
        return this.speedModifier;
    }

    public void setWantedPosition(double double0, double double1, double double2, double double3) {
        this.wantedX = double0;
        this.wantedY = double1;
        this.wantedZ = double2;
        this.speedModifier = double3;
        if (this.operation != MoveControl.Operation.JUMPING) {
            this.operation = MoveControl.Operation.MOVE_TO;
        }
    }

    public void strafe(float float0, float float1) {
        this.operation = MoveControl.Operation.STRAFE;
        this.strafeForwards = float0;
        this.strafeRight = float1;
        this.speedModifier = 0.25;
    }

    public void tick() {
        if (this.operation == MoveControl.Operation.STRAFE) {
            float $$0 = (float) this.mob.m_21133_(Attributes.MOVEMENT_SPEED);
            float $$1 = (float) this.speedModifier * $$0;
            float $$2 = this.strafeForwards;
            float $$3 = this.strafeRight;
            float $$4 = Mth.sqrt($$2 * $$2 + $$3 * $$3);
            if ($$4 < 1.0F) {
                $$4 = 1.0F;
            }
            $$4 = $$1 / $$4;
            $$2 *= $$4;
            $$3 *= $$4;
            float $$5 = Mth.sin(this.mob.m_146908_() * (float) (Math.PI / 180.0));
            float $$6 = Mth.cos(this.mob.m_146908_() * (float) (Math.PI / 180.0));
            float $$7 = $$2 * $$6 - $$3 * $$5;
            float $$8 = $$3 * $$6 + $$2 * $$5;
            if (!this.isWalkable($$7, $$8)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }
            this.mob.setSpeed($$1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double $$9 = this.wantedX - this.mob.m_20185_();
            double $$10 = this.wantedZ - this.mob.m_20189_();
            double $$11 = this.wantedY - this.mob.m_20186_();
            double $$12 = $$9 * $$9 + $$11 * $$11 + $$10 * $$10;
            if ($$12 < 2.5000003E-7F) {
                this.mob.setZza(0.0F);
                return;
            }
            float $$13 = (float) (Mth.atan2($$10, $$9) * 180.0F / (float) Math.PI) - 90.0F;
            this.mob.m_146922_(this.rotlerp(this.mob.m_146908_(), $$13, 90.0F));
            this.mob.setSpeed((float) (this.speedModifier * this.mob.m_21133_(Attributes.MOVEMENT_SPEED)));
            BlockPos $$14 = this.mob.m_20183_();
            BlockState $$15 = this.mob.m_9236_().getBlockState($$14);
            VoxelShape $$16 = $$15.m_60812_(this.mob.m_9236_(), $$14);
            if ($$11 > (double) this.mob.m_274421_() && $$9 * $$9 + $$10 * $$10 < (double) Math.max(1.0F, this.mob.m_20205_()) || !$$16.isEmpty() && this.mob.m_20186_() < $$16.max(Direction.Axis.Y) + (double) $$14.m_123342_() && !$$15.m_204336_(BlockTags.DOORS) && !$$15.m_204336_(BlockTags.FENCES)) {
                this.mob.getJumpControl().jump();
                this.operation = MoveControl.Operation.JUMPING;
            }
        } else if (this.operation == MoveControl.Operation.JUMPING) {
            this.mob.setSpeed((float) (this.speedModifier * this.mob.m_21133_(Attributes.MOVEMENT_SPEED)));
            if (this.mob.m_20096_()) {
                this.operation = MoveControl.Operation.WAIT;
            }
        } else {
            this.mob.setZza(0.0F);
        }
    }

    private boolean isWalkable(float float0, float float1) {
        PathNavigation $$2 = this.mob.getNavigation();
        if ($$2 != null) {
            NodeEvaluator $$3 = $$2.getNodeEvaluator();
            if ($$3 != null && $$3.getBlockPathType(this.mob.m_9236_(), Mth.floor(this.mob.m_20185_() + (double) float0), this.mob.m_146904_(), Mth.floor(this.mob.m_20189_() + (double) float1)) != BlockPathTypes.WALKABLE) {
                return false;
            }
        }
        return true;
    }

    protected float rotlerp(float float0, float float1, float float2) {
        float $$3 = Mth.wrapDegrees(float1 - float0);
        if ($$3 > float2) {
            $$3 = float2;
        }
        if ($$3 < -float2) {
            $$3 = -float2;
        }
        float $$4 = float0 + $$3;
        if ($$4 < 0.0F) {
            $$4 += 360.0F;
        } else if ($$4 > 360.0F) {
            $$4 -= 360.0F;
        }
        return $$4;
    }

    public double getWantedX() {
        return this.wantedX;
    }

    public double getWantedY() {
        return this.wantedY;
    }

    public double getWantedZ() {
        return this.wantedZ;
    }

    protected static enum Operation {

        WAIT, MOVE_TO, STRAFE, JUMPING
    }
}