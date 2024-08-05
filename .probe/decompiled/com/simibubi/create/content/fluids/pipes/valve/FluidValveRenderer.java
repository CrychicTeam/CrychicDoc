package com.simibubi.create.content.fluids.pipes.valve;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class FluidValveRenderer extends KineticBlockEntityRenderer<FluidValveBlockEntity> {

    public FluidValveRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(FluidValveBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
            BlockState blockState = be.m_58900_();
            SuperByteBuffer pointer = CachedBufferer.partial(AllPartialModels.FLUID_VALVE_POINTER, blockState);
            Direction facing = (Direction) blockState.m_61143_(FluidValveBlock.FACING);
            float pointerRotation = Mth.lerp(be.pointer.getValue(partialTicks), 0.0F, -90.0F);
            Direction.Axis pipeAxis = FluidValveBlock.getPipeAxis(blockState);
            Direction.Axis shaftAxis = getRotationAxisOf(be);
            int pointerRotationOffset = 0;
            if (pipeAxis.isHorizontal() && shaftAxis == Direction.Axis.X || pipeAxis.isVertical()) {
                pointerRotationOffset = 90;
            }
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) pointer.centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX(facing == Direction.UP ? 0.0 : (facing == Direction.DOWN ? 180.0 : 90.0))).rotateY((double) ((float) pointerRotationOffset + pointerRotation))).unCentre()).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }

    protected BlockState getRenderedBlockState(FluidValveBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }
}