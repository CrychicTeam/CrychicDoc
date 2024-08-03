package com.simibubi.create.content.trains.observer;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TrackObserverRenderer extends SmartBlockEntityRenderer<TrackObserverBlockEntity> {

    public TrackObserverRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(TrackObserverBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        BlockPos pos = be.m_58899_();
        TrackTargetingBehaviour<TrackObserver> target = be.edgePoint;
        BlockPos targetPosition = target.getGlobalPosition();
        Level level = be.m_58904_();
        BlockState trackState = level.getBlockState(targetPosition);
        Block block = trackState.m_60734_();
        if (block instanceof ITrackBlock) {
            ms.pushPose();
            TransformStack.cast(ms).translate(targetPosition.subtract(pos));
            TrackTargetingBehaviour.RenderedTrackOverlayType type = TrackTargetingBehaviour.RenderedTrackOverlayType.OBSERVER;
            TrackTargetingBehaviour.render(level, targetPosition, target.getTargetDirection(), target.getTargetBezier(), ms, buffer, light, overlay, type, 1.0F);
            ms.popPose();
        }
    }
}