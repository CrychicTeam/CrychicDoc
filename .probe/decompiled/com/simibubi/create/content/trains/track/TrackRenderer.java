package com.simibubi.create.content.trains.track;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class TrackRenderer extends SafeBlockEntityRenderer<TrackBlockEntity> {

    public TrackRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(TrackBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Level level = be.m_58904_();
        if (!Backend.canUseInstancing(level)) {
            VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
            be.connections.values().forEach(bc -> renderBezierTurn(level, bc, ms, vb));
        }
    }

    public static void renderBezierTurn(Level level, BezierConnection bc, PoseStack ms, VertexConsumer vb) {
        if (bc.isPrimary()) {
            ms.pushPose();
            BlockPos tePosition = bc.tePositions.getFirst();
            BlockState air = Blocks.AIR.defaultBlockState();
            BezierConnection.SegmentAngles[] segments = bc.getBakedSegments();
            renderGirder(level, bc, ms, vb, tePosition);
            for (int i = 1; i < segments.length; i++) {
                BezierConnection.SegmentAngles segment = segments[i];
                int light = LevelRenderer.getLightColor(level, segment.lightPosition.offset(tePosition));
                TrackMaterial.TrackModelHolder modelHolder = bc.getMaterial().getModelHolder();
                CachedBufferer.partial(modelHolder.tie(), air).mulPose(segment.tieTransform.pose()).mulNormal(segment.tieTransform.normal()).light(light).renderInto(ms, vb);
                for (boolean first : Iterate.trueAndFalse) {
                    PoseStack.Pose transform = segment.railTransforms.get(first);
                    CachedBufferer.partial(first ? modelHolder.segment_left() : modelHolder.segment_right(), air).mulPose(transform.pose()).mulNormal(transform.normal()).light(light).renderInto(ms, vb);
                }
            }
            ms.popPose();
        }
    }

    private static void renderGirder(Level level, BezierConnection bc, PoseStack ms, VertexConsumer vb, BlockPos tePosition) {
        if (bc.hasGirder) {
            BlockState air = Blocks.AIR.defaultBlockState();
            BezierConnection.GirderAngles[] girders = bc.getBakedGirders();
            for (int i = 1; i < girders.length; i++) {
                BezierConnection.GirderAngles segment = girders[i];
                int light = LevelRenderer.getLightColor(level, segment.lightPosition.offset(tePosition));
                for (boolean first : Iterate.trueAndFalse) {
                    PoseStack.Pose beamTransform = segment.beams.get(first);
                    CachedBufferer.partial(AllPartialModels.GIRDER_SEGMENT_MIDDLE, air).mulPose(beamTransform.pose()).mulNormal(beamTransform.normal()).light(light).renderInto(ms, vb);
                    for (boolean top : Iterate.trueAndFalse) {
                        PoseStack.Pose beamCapTransform = segment.beamCaps.get(top).get(first);
                        CachedBufferer.partial(top ? AllPartialModels.GIRDER_SEGMENT_TOP : AllPartialModels.GIRDER_SEGMENT_BOTTOM, air).mulPose(beamCapTransform.pose()).mulNormal(beamCapTransform.normal()).light(light).renderInto(ms, vb);
                    }
                }
            }
        }
    }

    public static Vec3 getModelAngles(Vec3 normal, Vec3 diff) {
        double diffX = diff.x();
        double diffY = diff.y();
        double diffZ = diff.z();
        double len = (double) Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));
        double yaw = Mth.atan2(diffX, diffZ);
        double pitch = Mth.atan2(len, diffY) - (Math.PI / 2);
        Vec3 yawPitchNormal = VecHelper.rotate(VecHelper.rotate(new Vec3(0.0, 1.0, 0.0), (double) AngleHelper.deg(pitch), Direction.Axis.X), (double) AngleHelper.deg(yaw), Direction.Axis.Y);
        double signum = Math.signum(yawPitchNormal.dot(normal));
        if (Math.abs(signum) < 0.5) {
            signum = yawPitchNormal.distanceToSqr(normal) < 0.5 ? -1.0 : 1.0;
        }
        double dot = diff.cross(normal).normalize().dot(yawPitchNormal);
        double roll = Math.acos(Mth.clamp(dot, -1.0, 1.0)) * signum;
        return new Vec3(pitch, yaw, roll);
    }

    public boolean shouldRenderOffScreen(TrackBlockEntity pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 192;
    }
}