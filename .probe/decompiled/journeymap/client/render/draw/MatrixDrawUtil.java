package journeymap.client.render.draw;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import journeymap.client.cartography.color.RGB;
import journeymap.client.render.RenderWrapper;
import journeymap.client.texture.Texture;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MatrixDrawUtil {

    public static int zLevel = 0;

    public static void drawColoredImage(Texture texture, PoseStack poseStack, VertexConsumer vertexBuilder, int color, float alpha, double x, double y, double rotation) {
        drawQuad(poseStack, vertexBuilder, color, alpha, x, y, (double) texture.getWidth(), (double) texture.getHeight(), false, rotation);
    }

    public static void drawQuad(PoseStack poseStack, VertexConsumer vertexBuilder, int color, float alpha, double x, double y, double width, double height, boolean flip, double rotation) {
        drawQuad(poseStack, vertexBuilder, color, alpha, x, y, width, height, 0.0, 0.0, 1.0, 1.0, rotation, flip, true, 770, 771, false);
    }

    public static void drawLabel(String text, PoseStack poseStack, MultiBufferSource buffers, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, int color, float alpha, double fontScale, boolean fontShadow) {
        double bgWidth = 0.0;
        double bgHeight = 0.0;
        if (bgColor != null && bgAlpha > 0.0F) {
            Font fontRenderer = Minecraft.getInstance().font;
            bgWidth = (double) fontRenderer.width(text);
            bgHeight = (double) DrawUtil.getLabelHeight(fontRenderer, fontShadow);
        }
        drawLabel(text, poseStack, buffers, x, y, hAlign, vAlign, bgColor, bgAlpha, bgWidth, bgHeight, color, alpha, fontScale, fontShadow, 0);
    }

    private static void drawLabel(String text, PoseStack poseStack, MultiBufferSource buffers, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, double bgWidth, double bgHeight, int color, float alpha, double fontScale, boolean fontShadow, int rotation) {
        if (text != null && text.length() != 0) {
            Font fontRenderer = Minecraft.getInstance().font;
            boolean drawRect = bgColor != null && bgAlpha > 0.0F;
            double width = (double) fontRenderer.width(text);
            int height = drawRect ? DrawUtil.getLabelHeight(fontRenderer, fontShadow) : 9;
            if (!drawRect && fontRenderer.isBidirectional()) {
                height--;
            }
            poseStack.pushPose();
            if (fontScale != 1.0) {
                x /= fontScale;
                y /= fontScale;
                poseStack.scale((float) fontScale, (float) fontScale, 0.0F);
            }
            float textX = (float) x;
            float textY = (float) y;
            double rectX = x;
            double rectY = y;
            switch(hAlign) {
                case Left:
                    textX = (float) (x - width);
                    rectX = (double) textX;
                    break;
                case Center:
                    textX = (float) (x - width / 2.0 + (fontScale > 1.0 ? 0.5 : 0.0));
                    rectX = (double) ((float) (x - Math.max(1.0, bgWidth) / 2.0 + (fontScale > 1.0 ? 0.5 : 0.0)));
                    break;
                case Right:
                    textX = (float) x;
                    rectX = (double) ((float) x);
            }
            double vpad = drawRect ? (double) (height - 9) / 2.0 : 0.0;
            switch(vAlign) {
                case Above:
                    rectY = y - (double) height;
                    textY = (float) (rectY + vpad + (double) (fontRenderer.isBidirectional() ? 0 : 1));
                    break;
                case Middle:
                    rectY = y - (double) (height / 2) + (fontScale > 1.0 ? 0.5 : 0.0);
                    textY = (float) (rectY + vpad);
                    break;
                case Below:
                    rectY = y;
                    textY = (float) (y + vpad);
            }
            if (rotation != 0) {
                poseStack.translate(x, y, 0.0);
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (-rotation)));
                poseStack.translate(-x, -y, 0.0);
            }
            Matrix4f matrixPos = poseStack.last().pose();
            if (drawRect) {
                int backgroundColor = Minecraft.getInstance().options.getBackgroundColor(bgAlpha);
                int hpad = 2;
                drawRectangle(matrixPos, rectX - 2.0 - 0.5, rectY, bgWidth + 4.0, bgHeight, backgroundColor);
            }
            if (alpha < 0.0F) {
                color = RGB.toArbg(color, alpha);
            }
            poseStack.translate((double) textX - Math.floor((double) textX), (double) textY - Math.floor((double) textY), 0.0);
            RenderWrapper.enableDepthTest();
            RenderWrapper.depthMask(true);
            fontRenderer.drawInBatch(Component.literal(text), textX, textY, color, fontShadow, matrixPos, buffers, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            RenderWrapper.disableDepthTest();
            RenderWrapper.depthMask(false);
            poseStack.popPose();
        }
    }

    public static void drawQuad(PoseStack poseStack, VertexConsumer vertexBuilder, int color, float alpha, double x, double y, double width, double height, double minU, double minV, double maxU, double maxV, double rotation, boolean flip, boolean blend, int glBlendSfactor, int glBlendDFactor, boolean clampTexture) {
        try {
            poseStack.pushPose();
            if (alpha > 1.0F) {
                alpha /= 255.0F;
            }
            if (rotation != 0.0) {
                double transX = x + width / 2.0;
                double transY = y + height / 2.0;
                poseStack.translate(transX, transY, 0.0);
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (-rotation)));
                poseStack.translate(-transX, -transY, 0.0);
            }
            float[] rgba = RGB.floats(color, alpha);
            float direction = (float) (flip ? -maxU : maxU);
            PoseStack.Pose entry = poseStack.last();
            Matrix4f matrix4f = entry.pose();
            Matrix3f matrix3f = entry.normal();
            addVertexUV(matrix4f, matrix3f, vertexBuilder, rgba[0], rgba[1], rgba[2], rgba[3], (int) (height + y), (float) x, (float) zLevel, (float) minU, (float) maxV);
            addVertexUV(matrix4f, matrix3f, vertexBuilder, rgba[0], rgba[1], rgba[2], rgba[3], (int) (height + y), (float) (x + width), (float) zLevel, direction, (float) maxV);
            addVertexUV(matrix4f, matrix3f, vertexBuilder, rgba[0], rgba[1], rgba[2], rgba[3], (int) y, (float) (x + width), (float) zLevel, direction, (float) minV);
            addVertexUV(matrix4f, matrix3f, vertexBuilder, rgba[0], rgba[1], rgba[2], rgba[3], (int) y, (float) x, (float) zLevel, (float) minU, (float) minV);
        } catch (Exception var35) {
            Journeymap.getLogger().error(var35);
        } finally {
            poseStack.popPose();
        }
    }

    public static void addBufferedVertexWithUV(BufferBuilder bufferIn, double x, double y, double z, double texU, double texV) {
        bufferIn.m_5483_(x, y, z).uv((float) texU, (float) texV).endVertex();
    }

    public static void drawRectangle(Matrix4f matrixPos, double x, double y, double width, double height, int color) {
        fill(matrixPos, (int) x, (int) y, (int) (x + width), (int) (height + y), color);
    }

    public static void fill(Matrix4f matrixPos, int x, int y, int bottomX, int bottomY, int color) {
        if (x < bottomX) {
            int i = x;
            x = bottomX;
            bottomX = i;
        }
        if (y < bottomY) {
            int j = y;
            y = bottomY;
            bottomY = j;
        }
        float f3 = (float) (color >> 24 & 0xFF) / 255.0F;
        float f = (float) (color >> 16 & 0xFF) / 255.0F;
        float f1 = (float) (color >> 8 & 0xFF) / 255.0F;
        float f2 = (float) (color & 0xFF) / 255.0F;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderWrapper.enableBlend();
        RenderWrapper.disableTexture();
        RenderWrapper.setShader(GameRenderer::m_172811_);
        RenderWrapper.blendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.m_252986_(matrixPos, (float) x, (float) bottomY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.m_252986_(matrixPos, (float) bottomX, (float) bottomY, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.m_252986_(matrixPos, (float) bottomX, (float) y, 0.0F).color(f, f1, f2, f3).endVertex();
        bufferbuilder.m_252986_(matrixPos, (float) x, (float) y, 0.0F).color(f, f1, f2, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderWrapper.enableTexture();
        RenderWrapper.disableBlend();
    }

    public static void addVertexUVOverlay(Matrix4f matrixPos, Matrix3f matrixNormal, VertexConsumer bufferIn, float red, float green, float blue, float alpha, int y, float x, float z, float texU, float texV, int overlayUV) {
        bufferIn.vertex(matrixPos, x, (float) y, z).color(red, green, blue, alpha).uv(texU, texV).overlayCoords(overlayUV).uv2(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void addVertexUV(Matrix4f matrixPos, Matrix3f matrixNormal, VertexConsumer bufferIn, float red, float green, float blue, float alpha, int y, float x, float z, float texU, float texV) {
        bufferIn.vertex(matrixPos, x, (float) y, z).color(red, green, blue, alpha).uv(texU, texV).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, VertexConsumer bufferIn, float red, float green, float blue, float alpha, double x, double y, float z) {
        bufferIn.vertex(matrixPos, (float) x, (float) y, z).color(red, green, blue, alpha).normal(matrixNormal, 0.0F, 1.0F, 0.0F).endVertex();
    }
}