package com.simibubi.create.content.trains.track;

import com.google.common.base.Objects;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackGraphLocation;
import com.simibubi.create.foundation.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TrackTargetingClient {

    static BlockPos lastHovered;

    static boolean lastDirection;

    static EdgePointType<?> lastType;

    static BezierTrackPointLocation lastHoveredBezierSegment;

    static TrackTargetingBlockItem.OverlapResult lastResult;

    static TrackGraphLocation lastLocation;

    public static void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        Vec3 lookAngle = player.m_20154_();
        BlockPos hovered = null;
        boolean direction = false;
        EdgePointType<?> type = null;
        BezierTrackPointLocation hoveredBezier = null;
        ItemStack stack = player.m_21205_();
        if (stack.getItem() instanceof TrackTargetingBlockItem ttbi) {
            type = ttbi.getType(stack);
        }
        if (type == EdgePointType.SIGNAL) {
            Create.RAILWAYS.sided(null).tickSignalOverlay();
        }
        boolean alreadySelected = stack.hasTag() && stack.getTag().contains("SelectedPos");
        if (type != null) {
            TrackBlockOutline.BezierPointSelection bezierSelection = TrackBlockOutline.result;
            if (alreadySelected) {
                CompoundTag tag = stack.getTag();
                hovered = NbtUtils.readBlockPos(tag.getCompound("SelectedPos"));
                direction = tag.getBoolean("SelectedDirection");
                if (tag.contains("Bezier")) {
                    CompoundTag bezierNbt = tag.getCompound("Bezier");
                    BlockPos key = NbtUtils.readBlockPos(bezierNbt.getCompound("Key"));
                    hoveredBezier = new BezierTrackPointLocation(key, bezierNbt.getInt("Segment"));
                }
            } else if (bezierSelection != null) {
                hovered = bezierSelection.blockEntity().m_58899_();
                hoveredBezier = bezierSelection.loc();
                direction = lookAngle.dot(bezierSelection.direction()) < 0.0;
            } else {
                HitResult hitResult = mc.hitResult;
                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                    BlockPos pos = blockHitResult.getBlockPos();
                    BlockState blockState = mc.level.m_8055_(pos);
                    if (blockState.m_60734_() instanceof ITrackBlock track) {
                        direction = track.getNearestTrackAxis(mc.level, pos, blockState, lookAngle).getSecond() == Direction.AxisDirection.POSITIVE;
                        hovered = pos;
                    }
                }
            }
        }
        if (hovered == null) {
            lastHovered = null;
            lastResult = null;
            lastLocation = null;
            lastHoveredBezierSegment = null;
        } else if (!Objects.equal(hovered, lastHovered) || !Objects.equal(hoveredBezier, lastHoveredBezierSegment) || direction != lastDirection || type != lastType) {
            lastType = type;
            lastHovered = hovered;
            lastDirection = direction;
            lastHoveredBezierSegment = hoveredBezier;
            TrackTargetingBlockItem.withGraphLocation(mc.level, hovered, direction, hoveredBezier, type, (result, location) -> {
                lastResult = result;
                lastLocation = location;
            });
        }
    }

    public static void render(PoseStack ms, SuperRenderTypeBuffer buffer, Vec3 camera) {
        if (lastLocation != null && lastResult.feedback == null) {
            Minecraft mc = Minecraft.getInstance();
            BlockPos pos = lastHovered;
            int light = LevelRenderer.getLightColor(mc.level, pos);
            Direction.AxisDirection direction = lastDirection ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE;
            TrackTargetingBehaviour.RenderedTrackOverlayType type = lastType == EdgePointType.SIGNAL ? TrackTargetingBehaviour.RenderedTrackOverlayType.SIGNAL : (lastType == EdgePointType.OBSERVER ? TrackTargetingBehaviour.RenderedTrackOverlayType.OBSERVER : TrackTargetingBehaviour.RenderedTrackOverlayType.STATION);
            ms.pushPose();
            TransformStack.cast(ms).translate(Vec3.atLowerCornerOf(pos).subtract(camera));
            TrackTargetingBehaviour.render(mc.level, pos, direction, lastHoveredBezierSegment, ms, buffer, light, OverlayTexture.NO_OVERLAY, type, 1.0625F);
            ms.popPose();
        }
    }
}