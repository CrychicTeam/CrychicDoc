package com.simibubi.create.content.trains.display;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSink;
import net.minecraft.util.StringDecomposer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class FlapDisplayRenderer extends KineticBlockEntityRenderer<FlapDisplayBlockEntity> {

    public FlapDisplayRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(FlapDisplayBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        Font fontRenderer = Minecraft.getInstance().font;
        FontSet fontSet = fontRenderer.getFontSet(Style.DEFAULT_FONT);
        float scale = 0.03125F;
        if (be.isController) {
            List<FlapDisplayLayout> lines = be.getLines();
            ms.pushPose();
            ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).centre()).rotateY((double) AngleHelper.horizontalAngle((Direction) be.m_58900_().m_61143_(FlapDisplayBlock.HORIZONTAL_FACING)))).unCentre()).translate(0.0, 0.0, -0.1875);
            ms.translate(0.0F, 1.0F, 1.0F);
            ms.scale(scale, scale, scale);
            ms.scale(1.0F, -1.0F, 1.0F);
            ms.translate(0.0F, 0.0F, 0.5F);
            for (int j = 0; j < lines.size(); j++) {
                List<FlapDisplaySection> line = ((FlapDisplayLayout) lines.get(j)).getSections();
                int color = be.getLineColor(j);
                ms.pushPose();
                float w = 0.0F;
                for (FlapDisplaySection section : line) {
                    w += section.getSize() + (float) (section.hasGap ? 8 : 1);
                }
                ms.translate((float) (be.xSize * 16) - w / 2.0F + 1.0F, 4.5F, 0.0F);
                PoseStack.Pose transform = ms.last();
                FlapDisplayRenderer.FlapDisplayRenderOutput renderOutput = new FlapDisplayRenderer.FlapDisplayRenderOutput(buffer, color, transform.pose(), light, j, !be.isSpeedRequirementFulfilled(), be.m_58904_(), be.isLineGlowing(j));
                for (int i = 0; i < line.size(); i++) {
                    FlapDisplaySection section = (FlapDisplaySection) line.get(i);
                    renderOutput.nextSection(section);
                    int ticks = AnimationTickHolder.getTicks(be.m_58904_());
                    String text = !section.renderCharsIndividually() && section.spinning[0] ? section.cyclingOptions[(ticks / 3 + i * 13) % section.cyclingOptions.length] : section.text;
                    StringDecomposer.iterateFormatted(text, Style.EMPTY, renderOutput);
                    ms.translate(section.size + (float) (section.hasGap ? 8 : 1), 0.0F, 0.0F);
                }
                if (buffer instanceof MultiBufferSource.BufferSource bs) {
                    BakedGlyph texturedglyph = fontSet.whiteGlyph();
                    bs.endBatch(texturedglyph.renderType(Font.DisplayMode.NORMAL));
                }
                ms.popPose();
                ms.translate(0.0F, 16.0F, 0.0F);
            }
            ms.popPose();
        }
    }

    protected SuperByteBuffer getRotatedModel(FlapDisplayBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacingVertical(AllPartialModels.SHAFTLESS_COGWHEEL, state, (Direction) state.m_61143_(FlapDisplayBlock.HORIZONTAL_FACING));
    }

    public boolean shouldRenderOffScreen(FlapDisplayBlockEntity be) {
        return be.isController;
    }

    @OnlyIn(Dist.CLIENT)
    static class FlapDisplayRenderOutput implements FormattedCharSink {

        final MultiBufferSource bufferSource;

        final float r;

        final float g;

        final float b;

        final float a;

        final Matrix4f pose;

        final int light;

        final boolean paused;

        FlapDisplaySection section;

        float x;

        private int lineIndex;

        private Level level;

        public FlapDisplayRenderOutput(MultiBufferSource buffer, int color, Matrix4f pose, int light, int lineIndex, boolean paused, Level level, boolean glowing) {
            this.bufferSource = buffer;
            this.lineIndex = lineIndex;
            this.level = level;
            this.a = glowing ? 0.975F : 0.85F;
            this.r = (float) (color >> 16 & 0xFF) / 255.0F;
            this.g = (float) (color >> 8 & 0xFF) / 255.0F;
            this.b = (float) (color & 0xFF) / 255.0F;
            this.pose = pose;
            this.light = glowing ? 15728880 : light;
            this.paused = paused;
        }

        public void nextSection(FlapDisplaySection section) {
            this.section = section;
            this.x = 0.0F;
        }

        @Override
        public boolean accept(int charIndex, Style style, int glyph) {
            FontSet fontset = this.getFontSet();
            int ticks = this.paused ? 0 : AnimationTickHolder.getTicks(this.level);
            float time = this.paused ? 0.0F : AnimationTickHolder.getRenderTime(this.level);
            float dim = 1.0F;
            if (this.section.renderCharsIndividually() && this.section.spinning[Math.min(charIndex, this.section.spinning.length)]) {
                float speed = this.section.spinningTicks > 5 && this.section.spinningTicks < 20 ? 1.75F : 2.5F;
                float cycle = time / speed + (float) charIndex * 16.83F + (float) this.lineIndex * 0.75F;
                float partial = cycle % 1.0F;
                char cyclingGlyph = this.section.cyclingOptions[(int) cycle % this.section.cyclingOptions.length].charAt(0);
                glyph = this.paused ? cyclingGlyph : (partial > 0.5F ? (partial > 0.75F ? 95 : 45) : cyclingGlyph);
                dim = 0.75F;
            }
            GlyphInfo glyphinfo = fontset.getGlyphInfo(glyph, false);
            float glyphWidth = glyphinfo.getAdvance(false);
            if (!this.section.renderCharsIndividually() && this.section.spinning[0]) {
                glyph = ticks % 3 == 0 ? (glyphWidth == 6.0F ? 45 : (glyphWidth == 1.0F ? 39 : glyph)) : glyph;
                glyph = ticks % 3 == 2 ? (glyphWidth == 6.0F ? 95 : (glyphWidth == 1.0F ? 46 : glyph)) : glyph;
                if (ticks % 3 != 1) {
                    dim = 0.75F;
                }
            }
            BakedGlyph bakedglyph = style.isObfuscated() && glyph != 32 ? fontset.getRandomGlyph(glyphinfo) : fontset.getGlyph(glyph);
            TextColor textcolor = style.getColor();
            float red = this.r * dim;
            float green = this.g * dim;
            float blue = this.b * dim;
            if (textcolor != null) {
                int i = textcolor.getValue();
                red = (float) (i >> 16 & 0xFF) / 255.0F;
                green = (float) (i >> 8 & 0xFF) / 255.0F;
                blue = (float) (i & 0xFF) / 255.0F;
            }
            float standardWidth = this.section.wideFlaps ? 9.0F : 7.0F;
            if (this.section.renderCharsIndividually()) {
                this.x += (standardWidth - glyphWidth) / 2.0F;
            }
            if (this.isNotEmpty(bakedglyph)) {
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(this.renderTypeOf(bakedglyph));
                bakedglyph.render(style.isItalic(), this.x, 0.0F, this.pose, vertexconsumer, red, green, blue, this.a, this.light);
            }
            if (this.section.renderCharsIndividually()) {
                this.x += standardWidth - (standardWidth - glyphWidth) / 2.0F;
            } else {
                this.x += glyphWidth;
            }
            return true;
        }

        public float finish(int bgColor) {
            if (bgColor == 0) {
                return this.x;
            } else {
                float a = (float) (bgColor >> 24 & 0xFF) / 255.0F;
                float r = (float) (bgColor >> 16 & 0xFF) / 255.0F;
                float g = (float) (bgColor >> 8 & 0xFF) / 255.0F;
                float b = (float) (bgColor & 0xFF) / 255.0F;
                BakedGlyph bakedglyph = this.getFontSet().whiteGlyph();
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(this.renderTypeOf(bakedglyph));
                bakedglyph.renderEffect(new BakedGlyph.Effect(-1.0F, 9.0F, this.section.size, -2.0F, 0.01F, r, g, b, a), this.pose, vertexconsumer, this.light);
                return this.x;
            }
        }

        private FontSet getFontSet() {
            return Minecraft.getInstance().font.getFontSet(Style.DEFAULT_FONT);
        }

        private RenderType renderTypeOf(BakedGlyph bakedglyph) {
            return bakedglyph.renderType(Font.DisplayMode.NORMAL);
        }

        private boolean isNotEmpty(BakedGlyph bakedglyph) {
            return !(bakedglyph instanceof EmptyGlyph);
        }
    }
}