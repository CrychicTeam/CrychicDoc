package com.simibubi.create.content.kinetics.drill;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class DrillRenderer extends KineticBlockEntityRenderer<DrillBlockEntity> {

    public DrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected SuperByteBuffer getRotatedModel(DrillBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.DRILL_HEAD, state);
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer) {
        BlockState state = context.state;
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.DRILL_HEAD, state);
        Direction facing = (Direction) state.m_61143_(DrillBlock.FACING);
        float speed = !context.contraption.stalled && VecHelper.isVecPointingTowards(context.relativeMotion, facing.getOpposite()) ? 0.0F : context.getAnimationSpeed();
        float time = AnimationTickHolder.getRenderTime() / 20.0F;
        float angle = time * speed % 360.0F;
        ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) superBuffer.transform(matrices.getModel()).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) AngleHelper.verticalAngle(facing))).rotateZ((double) angle)).unCentre()).light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.solid()));
    }
}