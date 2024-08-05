package com.simibubi.create.content.trains.track;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.RaycastHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.WorldAttached;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class TrackBlockOutline {

    public static WorldAttached<Map<BlockPos, TrackBlockEntity>> TRACKS_WITH_TURNS = new WorldAttached<>(w -> new HashMap());

    public static TrackBlockOutline.BezierPointSelection result;

    private static final VoxelShape LONG_CROSS = Shapes.or(TrackVoxelShapes.longOrthogonalZ(), TrackVoxelShapes.longOrthogonalX());

    private static final VoxelShape LONG_ORTHO = TrackVoxelShapes.longOrthogonalZ();

    private static final VoxelShape LONG_ORTHO_OFFSET = TrackVoxelShapes.longOrthogonalZOffset();

    public static void pickCurves() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.cameraEntity instanceof LocalPlayer player) {
            if (mc.level != null) {
                Vec3 origin = player.m_20299_(AnimationTickHolder.getPartialTicks(mc.level));
                double maxRange = mc.hitResult == null ? Double.MAX_VALUE : mc.hitResult.getLocation().distanceToSqr(origin);
                result = null;
                AttributeInstance range = player.m_21051_(ForgeMod.BLOCK_REACH.get());
                Vec3 target = RaycastHelper.getTraceTarget(player, Math.min(maxRange, range.getValue()) + 1.0, origin);
                Map<BlockPos, TrackBlockEntity> turns = TRACKS_WITH_TURNS.get(mc.level);
                for (TrackBlockEntity be : turns.values()) {
                    for (BezierConnection bc : be.connections.values()) {
                        if (bc.isPrimary()) {
                            AABB bounds = bc.getBounds();
                            if (bounds.contains(origin) || !bounds.clip(origin, target).isEmpty()) {
                                float[] stepLUT = bc.getStepLUT();
                                int segments = (int) (bc.getLength() * 2.0);
                                AABB segmentBounds = AllShapes.TRACK_ORTHO.get(Direction.SOUTH).bounds();
                                segmentBounds = segmentBounds.move(-0.5, segmentBounds.getYsize() / -2.0, -0.5);
                                int bestSegment = -1;
                                double bestDistance = Double.MAX_VALUE;
                                double newMaxRange = maxRange;
                                for (int i = 0; i < stepLUT.length - 2; i++) {
                                    float t = stepLUT[i] * (float) i / (float) segments;
                                    float t1 = stepLUT[i + 1] * (float) (i + 1) / (float) segments;
                                    float t2 = stepLUT[i + 2] * (float) (i + 2) / (float) segments;
                                    Vec3 v1 = bc.getPosition((double) t);
                                    Vec3 v2 = bc.getPosition((double) t2);
                                    Vec3 diff = v2.subtract(v1);
                                    Vec3 angles = TrackRenderer.getModelAngles(bc.getNormal((double) t1), diff);
                                    Vec3 anchor = v1.add(diff.scale(0.5));
                                    Vec3 localOrigin = origin.subtract(anchor);
                                    Vec3 localDirection = target.subtract(origin);
                                    localOrigin = VecHelper.rotate(localOrigin, (double) AngleHelper.deg(-angles.x), Direction.Axis.X);
                                    localOrigin = VecHelper.rotate(localOrigin, (double) AngleHelper.deg(-angles.y), Direction.Axis.Y);
                                    localDirection = VecHelper.rotate(localDirection, (double) AngleHelper.deg(-angles.x), Direction.Axis.X);
                                    localDirection = VecHelper.rotate(localDirection, (double) AngleHelper.deg(-angles.y), Direction.Axis.Y);
                                    Optional<Vec3> clip = segmentBounds.clip(localOrigin, localOrigin.add(localDirection));
                                    if (!clip.isEmpty() && (bestSegment == -1 || !(bestDistance < ((Vec3) clip.get()).distanceToSqr(0.0, 0.25, 0.0)))) {
                                        double distanceToSqr = ((Vec3) clip.get()).distanceToSqr(localOrigin);
                                        if (!(distanceToSqr > maxRange)) {
                                            bestSegment = i;
                                            newMaxRange = distanceToSqr;
                                            bestDistance = ((Vec3) clip.get()).distanceToSqr(0.0, 0.25, 0.0);
                                            BezierTrackPointLocation location = new BezierTrackPointLocation(bc.getKey(), i);
                                            result = new TrackBlockOutline.BezierPointSelection(be, location, anchor, angles, diff.normalize());
                                        }
                                    }
                                }
                                if (bestSegment != -1) {
                                    maxRange = newMaxRange;
                                }
                            }
                        }
                    }
                }
                if (result != null) {
                    if (mc.hitResult != null && mc.hitResult.getType() != HitResult.Type.MISS) {
                        Vec3 priorLoc = mc.hitResult.getLocation();
                        mc.hitResult = BlockHitResult.miss(priorLoc, Direction.UP, BlockPos.containing(priorLoc));
                    }
                }
            }
        }
    }

    public static void drawCurveSelection(PoseStack ms, MultiBufferSource buffer, Vec3 camera) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.hideGui && mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            TrackBlockOutline.BezierPointSelection result = TrackBlockOutline.result;
            if (result != null) {
                VertexConsumer vb = buffer.getBuffer(RenderType.lines());
                Vec3 vec = result.vec().subtract(camera);
                Vec3 angles = result.angles();
                ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).pushPose()).translate(vec.x, vec.y + 0.125, vec.z)).rotateYRadians(angles.y)).rotateXRadians(angles.x)).translate(-0.5, -0.125, -0.5);
                boolean holdingTrack = AllTags.AllBlockTags.TRACKS.matches(Minecraft.getInstance().player.m_21205_());
                renderShape(AllShapes.TRACK_ORTHO.get(Direction.SOUTH), ms, vb, holdingTrack ? false : null);
                ms.popPose();
            }
        }
    }

    @SubscribeEvent
    public static void drawCustomBlockSelection(RenderHighlightEvent.Block event) {
        Minecraft mc = Minecraft.getInstance();
        BlockHitResult target = event.getTarget();
        BlockPos pos = target.getBlockPos();
        BlockState blockstate = mc.level.m_8055_(pos);
        if (blockstate.m_60734_() instanceof TrackBlock) {
            if (mc.level.m_6857_().isWithinBounds(pos)) {
                VertexConsumer vb = event.getMultiBufferSource().getBuffer(RenderType.lines());
                Vec3 camPos = event.getCamera().getPosition();
                PoseStack ms = event.getPoseStack();
                ms.pushPose();
                ms.translate((double) pos.m_123341_() - camPos.x, (double) pos.m_123342_() - camPos.y, (double) pos.m_123343_() - camPos.z);
                boolean holdingTrack = AllTags.AllBlockTags.TRACKS.matches(Minecraft.getInstance().player.m_21205_());
                TrackShape shape = (TrackShape) blockstate.m_61143_(TrackBlock.SHAPE);
                boolean canConnectFrom = !shape.isJunction() && (!(mc.level.m_7702_(pos) instanceof TrackBlockEntity tbe) || !tbe.isTilted());
                walkShapes(shape, TransformStack.cast(ms), s -> {
                    renderShape(s, ms, vb, holdingTrack ? canConnectFrom : null);
                    event.setCanceled(true);
                });
                ms.popPose();
            }
        }
    }

    public static void renderShape(VoxelShape s, PoseStack ms, VertexConsumer vb, Boolean valid) {
        PoseStack.Pose transform = ms.last();
        s.forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            float xDiff = (float) (x2 - x1);
            float yDiff = (float) (y2 - y1);
            float zDiff = (float) (z2 - z1);
            float length = Mth.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
            xDiff /= length;
            yDiff /= length;
            zDiff /= length;
            float r = 0.0F;
            float g = 0.0F;
            float b = 0.0F;
            if (valid != null && valid) {
                g = 1.0F;
                b = 1.0F;
                r = 1.0F;
            }
            if (valid != null && !valid) {
                r = 1.0F;
                b = 0.125F;
                g = 0.25F;
            }
            vb.vertex(transform.pose(), (float) x1, (float) y1, (float) z1).color(r, g, b, 0.4F).normal(transform.normal(), xDiff, yDiff, zDiff).endVertex();
            vb.vertex(transform.pose(), (float) x2, (float) y2, (float) z2).color(r, g, b, 0.4F).normal(transform.normal(), xDiff, yDiff, zDiff).endVertex();
        });
    }

    private static void walkShapes(TrackShape shape, TransformStack msr, Consumer<VoxelShape> renderer) {
        float angle45 = (float) (Math.PI / 4);
        if (shape == TrackShape.XO || shape == TrackShape.CR_NDX || shape == TrackShape.CR_PDX) {
            renderer.accept(AllShapes.TRACK_ORTHO.get(Direction.EAST));
        } else if (shape == TrackShape.ZO || shape == TrackShape.CR_NDZ || shape == TrackShape.CR_PDZ) {
            renderer.accept(AllShapes.TRACK_ORTHO.get(Direction.SOUTH));
        }
        if (shape.isPortal()) {
            for (Direction d : Iterate.horizontalDirections) {
                if (TrackShape.asPortal(d) == shape) {
                    msr.rotateCentered(Direction.UP, AngleHelper.rad((double) AngleHelper.horizontalAngle(d)));
                    renderer.accept(LONG_ORTHO_OFFSET);
                    return;
                }
            }
        }
        if (shape == TrackShape.PD || shape == TrackShape.CR_PDX || shape == TrackShape.CR_PDZ) {
            msr.rotateCentered(Direction.UP, angle45);
            renderer.accept(LONG_ORTHO);
        } else if (shape == TrackShape.ND || shape == TrackShape.CR_NDX || shape == TrackShape.CR_NDZ) {
            msr.rotateCentered(Direction.UP, (float) (-Math.PI / 4));
            renderer.accept(LONG_ORTHO);
        }
        if (shape == TrackShape.CR_O) {
            renderer.accept(AllShapes.TRACK_CROSS);
        } else if (shape == TrackShape.CR_D) {
            msr.rotateCentered(Direction.UP, angle45);
            renderer.accept(LONG_CROSS);
        }
        if (shape == TrackShape.AE || shape == TrackShape.AN || shape == TrackShape.AW || shape == TrackShape.AS) {
            msr.translate(0.0, 1.0, 0.0);
            msr.rotateCentered(Direction.UP, (float) Math.PI - AngleHelper.rad((double) shape.getModelRotation()));
            msr.rotateXRadians((double) angle45);
            msr.translate(0.0, -0.1875, 0.0625);
            renderer.accept(LONG_ORTHO);
        }
    }

    public static record BezierPointSelection(TrackBlockEntity blockEntity, BezierTrackPointLocation loc, Vec3 vec, Vec3 angles, Vec3 direction) {
    }
}