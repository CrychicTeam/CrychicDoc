package com.simibubi.create.content.contraptions.actors.harvester;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class HarvesterRenderer extends SafeBlockEntityRenderer<HarvesterBlockEntity> {

    private static final Vec3 PIVOT = new Vec3(0.0, 6.0, 9.0);

    public HarvesterRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(HarvesterBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.m_58900_();
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.HARVESTER_BLADE, blockState);
        transform(be.m_58904_(), (Direction) blockState.m_61143_(HarvesterBlock.f_54117_), superBuffer, be.getAnimatedSpeed(), PIVOT);
        superBuffer.light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffers) {
        BlockState blockState = context.state;
        Direction facing = (Direction) blockState.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        SuperByteBuffer superBuffer = CachedBufferer.partial(AllPartialModels.HARVESTER_BLADE, blockState);
        float speed = !VecHelper.isVecPointingTowards(context.relativeMotion, facing.getOpposite()) ? context.getAnimationSpeed() : 0.0F;
        if (context.contraption.stalled) {
            speed = 0.0F;
        }
        superBuffer.transform(matrices.getModel());
        transform(context.world, facing, superBuffer, speed, PIVOT);
        superBuffer.light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), buffers.getBuffer(RenderType.cutoutMipped()));
    }

    public static void transform(Level world, Direction facing, SuperByteBuffer superBuffer, float speed, Vec3 pivot) {
        float originOffset = 0.0625F;
        Vec3 rotOffset = new Vec3(0.0, pivot.y * (double) originOffset, pivot.z * (double) originOffset);
        float time = AnimationTickHolder.getRenderTime(world) / 20.0F;
        float angle = time * speed % 360.0F;
        ((SuperByteBuffer) superBuffer.rotateCentered(Direction.UP, AngleHelper.rad((double) AngleHelper.horizontalAngle(facing))).translate(rotOffset.x, rotOffset.y, rotOffset.z).rotate(Direction.WEST, AngleHelper.rad((double) angle))).translate(-rotOffset.x, -rotOffset.y, -rotOffset.z);
    }
}