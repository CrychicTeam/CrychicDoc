package com.simibubi.create.content.fluids.pipes;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

public class SmartFluidPipeBlockEntity extends SmartBlockEntity {

    private FilteringBehaviour filter;

    public SmartFluidPipeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new SmartFluidPipeBlockEntity.SmartPipeBehaviour(this));
        behaviours.add(this.filter = new FilteringBehaviour(this, new SmartFluidPipeBlockEntity.SmartPipeFilterSlot()).forFluids().withCallback(this::onFilterChanged));
        this.registerAwardables(behaviours, FluidPropagator.getSharedTriggers());
    }

    private void onFilterChanged(ItemStack newFilter) {
        if (!this.f_58857_.isClientSide) {
            FluidPropagator.propagateChangedPipe(this.f_58857_, this.f_58858_, this.m_58900_());
        }
    }

    class SmartPipeBehaviour extends StraightPipeBlockEntity.StraightPipeFluidTransportBehaviour {

        public SmartPipeBehaviour(SmartBlockEntity be) {
            super(be);
        }

        @Override
        public boolean canPullFluidFrom(FluidStack fluid, BlockState state, Direction direction) {
            return !fluid.isEmpty() && (SmartFluidPipeBlockEntity.this.filter == null || !SmartFluidPipeBlockEntity.this.filter.test(fluid)) ? false : super.canPullFluidFrom(fluid, state, direction);
        }

        @Override
        public boolean canHaveFlowToward(BlockState state, Direction direction) {
            return state.m_60734_() instanceof SmartFluidPipeBlock && SmartFluidPipeBlock.getPipeAxis(state) == direction.getAxis();
        }
    }

    class SmartPipeFilterSlot extends ValueBoxTransform {

        @Override
        public Vec3 getLocalOffset(BlockState state) {
            AttachFace face = (AttachFace) state.m_61143_(SmartFluidPipeBlock.f_53179_);
            float y = face == AttachFace.CEILING ? 0.55F : (face == AttachFace.WALL ? 11.4F : 15.45F);
            float z = face == AttachFace.CEILING ? 4.6F : (face == AttachFace.WALL ? 0.55F : 4.625F);
            return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, (double) y, (double) z), (double) this.angleY(state), Direction.Axis.Y);
        }

        @Override
        public float getScale() {
            return super.getScale() * 1.02F;
        }

        @Override
        public void rotate(BlockState state, PoseStack ms) {
            AttachFace face = (AttachFace) state.m_61143_(SmartFluidPipeBlock.f_53179_);
            ((TransformStack) TransformStack.cast(ms).rotateY((double) this.angleY(state))).rotateX(face == AttachFace.CEILING ? -45.0 : 45.0);
        }

        protected float angleY(BlockState state) {
            AttachFace face = (AttachFace) state.m_61143_(SmartFluidPipeBlock.f_53179_);
            float horizontalAngle = AngleHelper.horizontalAngle((Direction) state.m_61143_(SmartFluidPipeBlock.f_54117_));
            if (face == AttachFace.WALL) {
                horizontalAngle += 180.0F;
            }
            return horizontalAngle;
        }
    }
}