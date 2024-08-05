package com.simibubi.create.content.contraptions.actors.roller;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.actors.harvester.HarvesterRenderer;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class RollerRenderer extends SmartBlockEntityRenderer<RollerBlockEntity> {

    public RollerRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(RollerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        BlockState blockState = be.m_58900_();
        ms.pushPose();
        ms.translate(0.0, -0.25, 0.0);
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.ROLLER_WHEEL, blockState);
        Direction facing = (Direction) blockState.m_61143_(RollerBlock.f_54117_);
        superBuffer.translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(1.0625));
        HarvesterRenderer.transform(be.m_58904_(), facing, superBuffer, be.getAnimatedSpeed(), Vec3.ZERO);
        ((SuperByteBuffer) superBuffer.translate(0.0, -0.5, 0.5).rotateY(90.0)).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        ms.popPose();
        CachedBufferer.partial(AllPartialModels.ROLLER_FRAME, blockState).rotateCentered(Direction.UP, AngleHelper.rad((double) (AngleHelper.horizontalAngle(facing) + 180.0F))).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffers) {
        BlockState blockState = context.state;
        Direction facing = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.ROLLER_WHEEL, blockState);
        float speed = !VecHelper.isVecPointingTowards(context.relativeMotion, facing.getOpposite()) ? context.getAnimationSpeed() : -context.getAnimationSpeed();
        if (context.contraption.stalled) {
            speed = 0.0F;
        }
        superBuffer.transform(matrices.getModel()).translate(Vec3.atLowerCornerOf(facing.getNormal()).scale(1.0625));
        HarvesterRenderer.transform(context.world, facing, superBuffer, speed, Vec3.ZERO);
        PoseStack viewProjection = matrices.getViewProjection();
        viewProjection.pushPose();
        viewProjection.translate(0.0, -0.25, 0.0);
        int contraptionWorldLight = ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld);
        ((SuperByteBuffer) superBuffer.translate(0.0, -0.5, 0.5).rotateY(90.0)).light(matrices.getWorld(), contraptionWorldLight).renderInto(viewProjection, buffers.getBuffer(RenderType.cutoutMipped()));
        viewProjection.popPose();
        CachedBufferer.partial(AllPartialModels.ROLLER_FRAME, blockState).transform(matrices.getModel()).rotateCentered(Direction.UP, AngleHelper.rad((double) (AngleHelper.horizontalAngle(facing) + 180.0F))).light(matrices.getWorld(), contraptionWorldLight).renderInto(viewProjection, buffers.getBuffer(RenderType.cutoutMipped()));
    }
}