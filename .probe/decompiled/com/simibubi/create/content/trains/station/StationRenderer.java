package com.simibubi.create.content.trains.station;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.Transform;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.depot.DepotRenderer;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class StationRenderer extends SafeBlockEntityRenderer<StationBlockEntity> {

    public StationRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(StationBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockPos pos = be.m_58899_();
        TrackTargetingBehaviour<GlobalStation> target = be.edgePoint;
        BlockPos targetPosition = target.getGlobalPosition();
        Level level = be.m_58904_();
        DepotRenderer.renderItemsOf(be, partialTicks, ms, buffer, light, overlay, be.depotBehaviour);
        BlockState trackState = level.getBlockState(targetPosition);
        Block block = trackState.m_60734_();
        if (block instanceof ITrackBlock) {
            GlobalStation station = be.getStation();
            boolean isAssembling = (Boolean) be.m_58900_().m_61143_(StationBlock.ASSEMBLING);
            if (isAssembling && (station != null && station.getPresentTrain() == null || be.isVirtual())) {
                renderFlag(AllPartialModels.STATION_ASSEMBLE, be, partialTicks, ms, buffer, light, overlay);
                ITrackBlock track = (ITrackBlock) block;
                Direction direction = be.assemblyDirection;
                if (be.isVirtual() && be.bogeyLocations == null) {
                    be.refreshAssemblyInfo();
                }
                if (direction != null && be.assemblyLength != 0 && be.bogeyLocations != null) {
                    ms.pushPose();
                    BlockPos offset = targetPosition.subtract(pos);
                    ms.translate((float) offset.m_123341_(), (float) offset.m_123342_(), (float) offset.m_123343_());
                    BlockPos.MutableBlockPos currentPos = targetPosition.mutable();
                    PartialModel assemblyOverlay = track.prepareAssemblyOverlay(level, targetPosition, trackState, direction, ms);
                    int colorWhenValid = 9876991;
                    int colorWhenCarriage = 13303702;
                    VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
                    currentPos.move(direction, 1);
                    ms.translate(0.0F, 0.0F, 1.0F);
                    for (int i = 0; i < be.assemblyLength; i++) {
                        int valid = be.isValidBogeyOffset(i) ? colorWhenValid : -1;
                        for (int j : be.bogeyLocations) {
                            if (i == j) {
                                valid = colorWhenCarriage;
                                break;
                            }
                        }
                        if (valid != -1) {
                            int lightColor = LevelRenderer.getLightColor(level, currentPos);
                            SuperByteBuffer sbb = CachedBufferer.partial(assemblyOverlay, trackState);
                            sbb.color(valid);
                            sbb.light(lightColor);
                            sbb.renderInto(ms, vb);
                        }
                        ms.translate(0.0F, 0.0F, 1.0F);
                        currentPos.move(direction);
                    }
                    ms.popPose();
                }
            } else {
                renderFlag(be.flag.getValue(partialTicks) > 0.75F ? AllPartialModels.STATION_ON : AllPartialModels.STATION_OFF, be, partialTicks, ms, buffer, light, overlay);
                ms.pushPose();
                TransformStack.cast(ms).translate(targetPosition.subtract(pos));
                TrackTargetingBehaviour.render(level, targetPosition, target.getTargetDirection(), target.getTargetBezier(), ms, buffer, light, overlay, TrackTargetingBehaviour.RenderedTrackOverlayType.STATION, 1.0F);
                ms.popPose();
            }
        }
    }

    public static void renderFlag(PartialModel flag, StationBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (be.resolveFlagAngle()) {
            SuperByteBuffer flagBB = CachedBufferer.partial(flag, be.m_58900_());
            transformFlag(flagBB, be, partialTicks, be.flagYRot, be.flagFlipped);
            ((SuperByteBuffer) flagBB.translate(0.03125, 0.0, 0.0).rotateY(be.flagFlipped ? 0.0 : 180.0)).translate(-0.03125, 0.0, 0.0).light(light).renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
        }
    }

    public static void transformFlag(Transform<?> flag, StationBlockEntity be, float partialTicks, int yRot, boolean flipped) {
        float value = be.flag.getValue(partialTicks);
        float progress = (float) Math.pow((double) Math.min(value * 5.0F, 1.0F), 2.0);
        if (be.flag.getChaseTarget() > 0.0F && !be.flag.settled() && progress == 1.0F) {
            float wiggleProgress = (value - 0.2F) / 0.8F;
            progress = (float) ((double) progress + Math.sin((double) (wiggleProgress * (float) (Math.PI * 2) * 4.0F)) / 8.0 / (double) Math.max(1.0F, 8.0F * wiggleProgress));
        }
        float nudge = 0.001953125F;
        ((Transform) ((Transform) ((Transform) ((Transform) flag.centre()).rotateY((double) yRot)).translate((double) nudge, 0.59375, flipped ? (double) (0.875F - nudge) : (double) (0.125F + nudge))).unCentre()).rotateX((double) ((float) (flipped ? 1 : -1) * (progress * 90.0F + 270.0F)));
    }

    public boolean shouldRenderOffScreen(StationBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 192;
    }
}