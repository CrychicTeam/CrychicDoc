package com.simibubi.create.content.redstone.nixieTube;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.DyeHelper;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class NixieTubeRenderer extends SafeBlockEntityRenderer<NixieTubeBlockEntity> {

    private static Random r = new Random();

    public NixieTubeRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(NixieTubeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ms.pushPose();
        BlockState blockState = be.m_58900_();
        DoubleFaceAttachedBlock.DoubleAttachFace face = (DoubleFaceAttachedBlock.DoubleAttachFace) blockState.m_61143_(NixieTubeBlock.FACE);
        float yRot = AngleHelper.horizontalAngle((Direction) blockState.m_61143_(NixieTubeBlock.f_54117_)) - 90.0F + (float) (face == DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED ? 180 : 0);
        float xRot = face == DoubleFaceAttachedBlock.DoubleAttachFace.WALL ? -90.0F : (face == DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED ? 90.0F : 0.0F);
        TransformStack msr = TransformStack.cast(ms);
        ((TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotateY((double) yRot)).rotateZ((double) xRot)).unCentre();
        if (be.signalState != null) {
            this.renderAsSignal(be, partialTicks, ms, buffer, light, overlay);
            ms.popPose();
        } else {
            msr.centre();
            float height = face == DoubleFaceAttachedBlock.DoubleAttachFace.CEILING ? 5.0F : 3.0F;
            float scale = 0.05F;
            Couple<String> s = be.getDisplayedStrings();
            DyeColor color = NixieTubeBlock.colorOf(be.m_58900_());
            ms.pushPose();
            ms.translate(-0.25F, 0.0F, 0.0F);
            ms.scale(scale, -scale, scale);
            drawTube(ms, buffer, s.getFirst(), height, color);
            ms.popPose();
            ms.pushPose();
            ms.translate(0.25F, 0.0F, 0.0F);
            ms.scale(scale, -scale, scale);
            drawTube(ms, buffer, s.getSecond(), height, color);
            ms.popPose();
            ms.popPose();
        }
    }

    public static void drawTube(PoseStack ms, MultiBufferSource buffer, String c, float height, DyeColor color) {
        Font fontRenderer = Minecraft.getInstance().font;
        float charWidth = (float) fontRenderer.width(c);
        float shadowOffset = 0.5F;
        float flicker = r.nextFloat();
        Couple<Integer> couple = (Couple<Integer>) DyeHelper.DYE_TABLE.get(color);
        int brightColor = couple.getFirst();
        int darkColor = couple.getSecond();
        int flickeringBrightColor = Color.mixColors(brightColor, darkColor, flicker / 4.0F);
        ms.pushPose();
        ms.translate((charWidth - shadowOffset) / -2.0F, -height, 0.0F);
        drawInWorldString(ms, buffer, c, flickeringBrightColor);
        ms.pushPose();
        ms.translate(shadowOffset, shadowOffset, -0.0625F);
        drawInWorldString(ms, buffer, c, darkColor);
        ms.popPose();
        ms.popPose();
        ms.pushPose();
        ms.scale(-1.0F, 1.0F, 1.0F);
        ms.translate((charWidth - shadowOffset) / -2.0F, -height, 0.0F);
        drawInWorldString(ms, buffer, c, darkColor);
        ms.pushPose();
        ms.translate(-shadowOffset, shadowOffset, -0.0625F);
        drawInWorldString(ms, buffer, c, Color.mixColors(darkColor, 0, 0.35F));
        ms.popPose();
        ms.popPose();
    }

    public static void drawInWorldString(PoseStack ms, MultiBufferSource buffer, String c, int color) {
        Font fontRenderer = Minecraft.getInstance().font;
        fontRenderer.drawInBatch(c, 0.0F, 0.0F, color, false, ms.last().pose(), buffer, Font.DisplayMode.NORMAL, 0, 15728880);
        if (buffer instanceof MultiBufferSource.BufferSource) {
            BakedGlyph texturedglyph = fontRenderer.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
            ((MultiBufferSource.BufferSource) buffer).endBatch(texturedglyph.renderType(Font.DisplayMode.NORMAL));
        }
    }

    private void renderAsSignal(NixieTubeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.m_58900_();
        Direction facing = NixieTubeBlock.getFacing(blockState);
        Vec3 observerVec = Minecraft.getInstance().cameraEntity.getEyePosition(partialTicks);
        TransformStack msr = TransformStack.cast(ms);
        if (facing == Direction.DOWN) {
            ((TransformStack) ((TransformStack) msr.centre()).rotateZ(180.0)).unCentre();
        }
        boolean invertTubes = facing == Direction.DOWN || blockState.m_61143_(NixieTubeBlock.FACE) == DoubleFaceAttachedBlock.DoubleAttachFace.WALL_REVERSED;
        CachedBufferer.partial(AllPartialModels.SIGNAL_PANEL, blockState).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        ms.pushPose();
        ms.translate(0.5F, 0.46875F, 0.5F);
        float renderTime = AnimationTickHolder.getRenderTime(be.m_58904_());
        for (boolean first : Iterate.trueAndFalse) {
            Vec3 lampVec = Vec3.atCenterOf(be.m_58899_());
            Vec3 diff = lampVec.subtract(observerVec);
            if ((!first || be.signalState.isRedLight(renderTime)) && (first || be.signalState.isGreenLight(renderTime) || be.signalState.isYellowLight(renderTime))) {
                boolean flip = first == invertTubes;
                boolean yellow = be.signalState.isYellowLight(renderTime);
                ms.pushPose();
                ms.translate(flip ? 0.25F : -0.25F, 0.0F, 0.0F);
                if (diff.lengthSqr() < 9216.0) {
                    boolean vert = first ^ facing.getAxis().isHorizontal();
                    float longSide = yellow ? 1.0F : 4.0F;
                    float longSideGlow = yellow ? 2.0F : 5.125F;
                    CachedBufferer.partial(AllPartialModels.SIGNAL_WHITE_CUBE, blockState).light(15728880).disableDiffuse().scale(vert ? longSide : 1.0F, vert ? 1.0F : longSide, 1.0F).renderInto(ms, buffer.getBuffer(RenderType.translucent()));
                    CachedBufferer.partial(first ? AllPartialModels.SIGNAL_RED_GLOW : (yellow ? AllPartialModels.SIGNAL_YELLOW_GLOW : AllPartialModels.SIGNAL_WHITE_GLOW), blockState).light(15728880).disableDiffuse().scale(vert ? longSideGlow : 2.0F, vert ? 2.0F : longSideGlow, 2.0F).renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
                }
                ((SuperByteBuffer) CachedBufferer.partial(first ? AllPartialModels.SIGNAL_RED : (yellow ? AllPartialModels.SIGNAL_YELLOW : AllPartialModels.SIGNAL_WHITE), blockState).light(15728880).disableDiffuse().scale(1.0625F)).renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
                ms.popPose();
            }
        }
        ms.popPose();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}