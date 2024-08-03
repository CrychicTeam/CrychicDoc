package com.simibubi.create.content.kinetics.motor;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class CreativeMotorBlockEntity extends GeneratingKineticBlockEntity {

    public static final int DEFAULT_SPEED = 16;

    public static final int MAX_SPEED = 256;

    protected ScrollValueBehaviour generatedSpeed;

    public CreativeMotorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        int max = 256;
        this.generatedSpeed = new KineticScrollValueBehaviour(Lang.translateDirect("kinetics.creative_motor.rotation_speed"), this, new CreativeMotorBlockEntity.MotorValueBox());
        this.generatedSpeed.between(-max, max);
        this.generatedSpeed.value = 16;
        this.generatedSpeed.withCallback(i -> this.updateGeneratedRotation());
        behaviours.add(this.generatedSpeed);
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!this.hasSource() || this.getGeneratedSpeed() > this.getTheoreticalSpeed()) {
            this.updateGeneratedRotation();
        }
    }

    @Override
    public float getGeneratedSpeed() {
        return !AllBlocks.CREATIVE_MOTOR.has(this.m_58900_()) ? 0.0F : convertToDirection((float) this.generatedSpeed.getValue(), (Direction) this.m_58900_().m_61143_(CreativeMotorBlock.FACING));
    }

    class MotorValueBox extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8.0, 8.0, 12.5);
        }

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            Direction facing = (Direction) state.m_61143_(CreativeMotorBlock.FACING);
            return super.getLocalOffset(state).add(Vec3.atLowerCornerOf(facing.getNormal()).scale(-0.0625));
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            super.rotate(state, ms);
            Direction facing = (Direction) state.m_61143_(CreativeMotorBlock.FACING);
            if (facing.getAxis() != Direction.Axis.Y) {
                if (this.getSide() == Direction.UP) {
                    TransformStack.cast(ms).rotateZ((double) (-AngleHelper.horizontalAngle(facing) + 180.0F));
                }
            }
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            Direction facing = (Direction) state.m_61143_(CreativeMotorBlock.FACING);
            return facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN ? false : direction.getAxis() != facing.getAxis();
        }
    }
}