package journeymap.client.render.draw;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import java.awt.geom.Point2D.Double;
import java.util.List;
import journeymap.client.api.model.ShapeProperties;
import journeymap.client.cartography.color.RGB;
import journeymap.client.render.RenderWrapper;
import journeymap.client.texture.Texture;
import journeymap.client.ui.GuiHooks;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.theme.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DrawUtil {

    public static double zLevel = 0.0;

    public static void drawCenteredLabel(GuiGraphics graphics, String text, double x, double y, Integer bgColor, float bgAlpha, Integer color, float alpha, double fontScale) {
        drawLabel(graphics, text, x, y, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, bgColor, bgAlpha, color, alpha, fontScale, true, 0.0);
    }

    public static void drawCenteredLabel(GuiGraphics graphics, String text, double x, double y, Integer bgColor, float bgAlpha, Integer color, float alpha, double fontScale, boolean fontShadow) {
        drawLabel(graphics, text, x, y, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, bgColor, bgAlpha, color, alpha, fontScale, fontShadow, 0.0);
    }

    public static void drawCenteredLabel(GuiGraphics graphics, String text, double x, double y, Integer bgColor, float bgAlpha, Integer color, float alpha, double fontScale, double rotation) {
        drawLabel(graphics, text, x, y, DrawUtil.HAlign.Center, DrawUtil.VAlign.Middle, bgColor, bgAlpha, color, alpha, fontScale, false, rotation);
    }

    public static void drawLabel(GuiGraphics graphics, String text, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, int color, float alpha, double fontScale, boolean fontShadow) {
        drawLabel(graphics, text, x, y, hAlign, vAlign, bgColor, bgAlpha, color, alpha, fontScale, fontShadow, 0.0);
    }

    public static void drawLabels(PoseStack poseStack, MultiBufferSource buffers, String[] lines, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, Integer color, float alpha, double fontScale, boolean fontShadow, double rotation) {
        if (lines.length != 0) {
            if (lines.length == 1) {
                drawBatchLabel(poseStack, Component.literal(lines[0]), buffers, x, y, hAlign, vAlign, bgColor, bgAlpha, color, alpha, fontScale, fontShadow, rotation);
            } else {
                Font fontRenderer = Minecraft.getInstance().font;
                double vpad = fontRenderer.isBidirectional() ? 0.0 : (fontShadow ? 6.0 : 4.0);
                double lineHeight = 9.0 * fontScale;
                double bgHeight = lineHeight * (double) lines.length + vpad;
                double bgWidth = 0.0;
                if (bgColor != null && bgAlpha > 0.0F) {
                    for (String line : lines) {
                        bgWidth = Math.max(bgWidth, (double) fontRenderer.width(line) * fontScale);
                    }
                    if (bgWidth % 2.0 == 0.0) {
                        bgWidth++;
                    }
                }
                if (lines.length > 1) {
                    switch(vAlign) {
                        case Above:
                            y -= lineHeight * (double) lines.length;
                            bgHeight += vpad / 2.0;
                            break;
                        case Middle:
                            y -= bgHeight / 2.0;
                        case Below:
                    }
                }
                for (String line : lines) {
                    drawBatchLabel(poseStack, Component.literal(line), buffers, x, y, hAlign, vAlign, bgColor, bgAlpha, bgWidth, bgHeight, color, alpha, fontScale, fontShadow, rotation);
                    bgColor = null;
                    y += lineHeight;
                }
            }
        }
    }

    public static void drawLabel(GuiGraphics graphics, String text, Theme.LabelSpec labelSpec, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, double fontScale, double rotation) {
        drawLabel(graphics, text, x, y, hAlign, vAlign, labelSpec.background.getColor(), labelSpec.background.alpha, labelSpec.foreground.getColor(), labelSpec.foreground.alpha, fontScale, labelSpec.shadow, rotation);
    }

    public static void drawLabel(GuiGraphics graphics, String text, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, Integer color, float alpha, double fontScale, boolean fontShadow, double rotation) {
        double bgWidth = 0.0;
        double bgHeight = 0.0;
        if (bgColor != null && bgAlpha > 0.0F) {
            Font fontRenderer = Minecraft.getInstance().font;
            bgWidth = (double) fontRenderer.width(text);
            bgHeight = (double) getLabelHeight(fontRenderer, fontShadow);
        }
        drawLabel(graphics, text, x, y, hAlign, vAlign, bgColor, bgAlpha, bgWidth, bgHeight, color, alpha, fontScale, fontShadow, rotation);
    }

    public static void drawLabel(GuiGraphics graphics, String text, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, double bgWidth, double bgHeight, Integer color, float alpha, double fontScale, boolean fontShadow, double rotation) {
        if (text != null && text.length() != 0) {
            Font fontRenderer = Minecraft.getInstance().font;
            boolean drawRect = bgColor != null && bgAlpha > 0.0F;
            double width = (double) fontRenderer.width(text);
            int height = drawRect ? getLabelHeight(fontRenderer, fontShadow) : 9;
            if (!drawRect && fontRenderer.isBidirectional()) {
                height--;
            }
            graphics.pose().pushPose();
            try {
                if (fontScale != 1.0) {
                    x /= fontScale;
                    y /= fontScale;
                    graphics.pose().scale((float) fontScale, (float) fontScale, 1.0F);
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
                if (rotation != 0.0) {
                    graphics.pose().translate(x, y, 0.0);
                    graphics.pose().mulPose(Axis.ZP.rotationDegrees((float) (-rotation)));
                    graphics.pose().translate(-x, -y, 0.0);
                }
                if (drawRect) {
                    int hpad = 2;
                    drawRectangle(graphics.pose(), rectX - 2.0 - 0.5, rectY, bgWidth + 4.0, bgHeight, bgColor, bgAlpha);
                }
                if (alpha < 1.0F) {
                    color = RGB.toArbg(color, alpha);
                }
                graphics.pose().translate((double) textX - Math.floor((double) textX), (double) textY - Math.floor((double) textY), 0.0);
                graphics.drawString(fontRenderer, text, (int) Math.floor((double) textX), (int) Math.floor((double) textY), color, fontShadow);
            } finally {
                graphics.pose().popPose();
            }
        }
    }

    public static void drawBatchLabel(PoseStack poseStack, Component text, MultiBufferSource buffers, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, int color, float alpha, double fontScale, boolean fontShadow) {
        drawBatchLabel(poseStack, text, buffers, x, y, hAlign, vAlign, bgColor, bgAlpha, color, alpha, fontScale, fontShadow, 0.0);
    }

    public static void drawBatchLabel(PoseStack poseStack, Component text, MultiBufferSource buffers, Theme.LabelSpec labelSpec, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, double fontScale, double rotation) {
        drawBatchLabel(poseStack, text, buffers, x, y, hAlign, vAlign, labelSpec.background.getColor(), labelSpec.background.alpha, labelSpec.foreground.getColor(), labelSpec.foreground.alpha, fontScale, labelSpec.shadow, rotation);
    }

    public static void drawBatchLabel(PoseStack poseStack, Component text, MultiBufferSource buffers, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, int color, float alpha, double fontScale, boolean fontShadow, double rotation) {
        double bgWidth = 0.0;
        double bgHeight = 0.0;
        if (bgColor != null && bgAlpha > 0.0F) {
            Font fontRenderer = Minecraft.getInstance().font;
            bgWidth = (double) fontRenderer.width(text);
            bgHeight = (double) getLabelHeight(fontRenderer, fontShadow);
        }
        drawBatchLabel(poseStack, text, buffers, x, y, hAlign, vAlign, bgColor, bgAlpha, bgWidth, bgHeight, color, alpha, fontScale, fontShadow, rotation);
    }

    private static void drawBatchLabel(PoseStack poseStack, Component text, MultiBufferSource buffers, double x, double y, DrawUtil.HAlign hAlign, DrawUtil.VAlign vAlign, Integer bgColor, float bgAlpha, double bgWidth, double bgHeight, int color, float alpha, double fontScale, boolean fontShadow, double rotation) {
        if (text != null && text.getString().length() != 0) {
            Font fontRenderer = Minecraft.getInstance().font;
            boolean drawRect = bgColor != null && bgAlpha > 0.0F;
            double width = (double) fontRenderer.width(text);
            int height = drawRect ? getLabelHeight(fontRenderer, fontShadow) : 9;
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
            if (rotation != 0.0) {
                poseStack.translate(x, y, 0.0);
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) (-rotation)));
                poseStack.translate(-x, -y, 0.0);
            }
            Matrix4f matrixPos = poseStack.last().pose();
            if (drawRect) {
                int backgroundColor = Minecraft.getInstance().options.getBackgroundColor(bgAlpha);
                RenderWrapper.disableDepthTest();
                int hpad = 2;
                drawRectangle(poseStack, rectX - 2.0 - 0.5, rectY, bgWidth + 4.0, bgHeight, bgColor, bgAlpha);
            }
            if (alpha < 0.0F) {
                color = RGB.toArbg(color, alpha);
            }
            poseStack.translate((double) textX - Math.floor((double) textX), (double) textY - Math.floor((double) textY), 0.0);
            fontRenderer.drawInBatch(text, textX, textY, color, fontShadow, matrixPos, buffers, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
            RenderWrapper.disableDepthTest();
            RenderWrapper.depthMask(false);
            poseStack.popPose();
        }
    }

    public static int getLabelHeight(Font fr, boolean fontShadow) {
        int vpad = fr.isBidirectional() ? 0 : (fontShadow ? 6 : 4);
        return 9 + vpad;
    }

    public static void drawImage(PoseStack poseStack, Texture texture, double x, double y, boolean flip, float scale, double rotation) {
        drawQuad(poseStack, texture, x, y, (double) ((float) texture.getWidth() * scale), (double) ((float) texture.getHeight() * scale), flip, rotation);
    }

    public static void drawImage(PoseStack poseStack, Texture texture, float alpha, double x, double y, boolean flip, float scale, double rotation) {
        drawQuad(poseStack, texture, 16777215, alpha, x, y, (double) ((float) texture.getWidth() * scale), (double) ((float) texture.getHeight() * scale), false, rotation);
    }

    public static void drawClampedImage(PoseStack poseStack, Texture texture, double x, double y, float scale, double rotation) {
        drawClampedImage(poseStack, texture, 16777215, 1.0F, x, y, scale, rotation);
    }

    public static void drawClampedImage(PoseStack poseStack, Texture texture, int color, float alpha, double x, double y, float scale, double rotation) {
        drawQuad(poseStack, texture, color, alpha, x, y, (double) ((float) texture.getWidth() * scale), (double) ((float) texture.getHeight() * scale), false, rotation);
    }

    public static void drawColoredImage(PoseStack poseStack, Texture texture, int color, float alpha, double x, double y, float scale, double rotation) {
        drawQuad(poseStack, texture, color, alpha, x, y, (double) ((float) texture.getWidth() * scale), (double) ((float) texture.getHeight() * scale), false, rotation);
    }

    public static void drawColoredSprite(PoseStack poseStack, Texture texture, double displayWidth, double displayHeight, double spriteX, double spriteY, double spriteWidth, double spriteHeight, Integer color, float alpha, double x, double y, float scale, double rotation) {
        double texWidth = (double) texture.getWidth();
        double texHeight = (double) texture.getHeight();
        double minU = Math.max(0.0, spriteX / texWidth);
        double minV = Math.max(0.0, spriteY / texHeight);
        double maxU = Math.min(1.0, (spriteX + spriteWidth) / texWidth);
        double maxV = Math.min(1.0, (spriteY + spriteHeight) / texHeight);
        drawQuad(poseStack, texture, color, alpha, x, y, displayWidth * (double) scale, displayHeight * (double) scale, minU, minV, maxU, maxV, rotation, false, true, 770, 771, false);
    }

    public static void drawColoredImage(PoseStack poseStack, Texture texture, int color, float alpha, double x, double y, double rotation) {
        drawQuad(poseStack, texture, color, alpha, x, y, (double) texture.getWidth(), (double) texture.getHeight(), false, rotation);
    }

    public static void drawWaypointIcon(PoseStack poseStack, Texture texture, float scale, int color, float alpha, double x, double y, double rotation) {
        if (scale > 1.0F) {
            texture = texture.getScaledImage(scale);
            scale = 1.0F;
        }
        double width = (double) ((float) texture.getWidth() * scale);
        double height = (double) ((float) texture.getHeight() * scale);
        double drawX = x - width / 2.0;
        double drawY = y - height / 2.0;
        drawQuad(poseStack, texture, color, alpha, drawX, drawY, width, height, false, rotation);
    }

    public static void drawColoredImage(PoseStack poseStack, Texture texture, int color, float alpha, double x, double y, int width, int height, double rotation) {
        drawQuad(poseStack, texture, color, alpha, x, y, (double) width, (double) height, false, rotation);
    }

    public static void drawQuad(PoseStack poseStack, Texture texture, double x, double y, double width, double height, boolean flip, double rotation) {
        drawQuad(poseStack, texture, 16777215, 1.0F, x, y, width, height, 0.0, 0.0, 1.0, 1.0, rotation, flip, true, 770, 771, false);
    }

    public static void drawQuad(PoseStack poseStack, Texture texture, int color, float alpha, double x, double y, double width, double height, boolean flip, double rotation) {
        drawQuad(poseStack, texture, color, alpha, x, y, width, height, 0.0, 0.0, 1.0, 1.0, rotation, flip, true, 770, 771, false);
    }

    public static void drawQuad(PoseStack poseStack, Texture texture, int color, float alpha, double x, double y, double width, double height, double minU, double minV, double maxU, double maxV, double rotation, boolean flip, boolean blend, int glBlendSfactor, int glBlendDFactor, boolean clampTexture) {
        poseStack.pushPose();
        try {
            RenderWrapper.setShader(GameRenderer::m_172817_);
            RenderWrapper.activeTexture(33984);
            RenderWrapper.bindTexture(texture.getTextureId());
            RenderWrapper.setShaderTexture(0, texture.getTextureId());
            if (blend) {
                RenderWrapper.enableBlend();
                RenderWrapper.blendFuncSeparate(glBlendSfactor, glBlendDFactor, 1, 0);
            }
            RenderWrapper.enableTexture();
            if (alpha > 1.0F) {
                alpha /= 255.0F;
            }
            if (blend) {
                float[] c = RGB.floats(color);
                RenderWrapper.setColor4f(c[0], c[1], c[2], alpha);
            } else {
                RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, alpha);
            }
            RenderWrapper.texParameter(3553, 10241, 9729);
            RenderWrapper.texParameter(3553, 10240, 9729);
            int texEdgeBehavior = clampTexture ? '脯' : 10497;
            RenderWrapper.texParameter(3553, 10242, texEdgeBehavior);
            RenderWrapper.texParameter(3553, 10243, texEdgeBehavior);
            if (rotation != 0.0) {
                double transX = x + width / 2.0;
                double transY = y + height / 2.0;
                poseStack.translate(transX, transY, 0.0);
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) rotation));
                poseStack.translate(-transX, -transY, 0.0);
            }
            double direction = flip ? -maxU : maxU;
            RenderWrapper.disableTexture();
            RenderWrapper.enableBlend();
            RenderWrapper.blendFuncSeparate(770, 771, 1, 0);
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            addVertexWithUV(poseStack, buffer, x, height + y, zLevel, minU, maxV);
            addVertexWithUV(poseStack, buffer, x + width, height + y, zLevel, direction, maxV);
            addVertexWithUV(poseStack, buffer, x + width, y, zLevel, direction, minV);
            addVertexWithUV(poseStack, buffer, x, y, zLevel, minU, minV);
            tessellator.end();
            RenderWrapper.enableTexture();
            RenderWrapper.enableBlend();
            if (blend) {
                RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if (glBlendSfactor != 770 || glBlendDFactor != 771) {
                    RenderWrapper.enableBlend();
                    RenderWrapper.blendFuncSeparate(770, 771, 1, 0);
                }
            }
        } finally {
            poseStack.popPose();
        }
    }

    public static void drawRectangle(PoseStack poseStack, double x, double y, double width, double height, int color, float alpha) {
        int[] rgba = RGB.ints(color, alpha);
        RenderWrapper.enableBlend();
        RenderWrapper.disableTexture();
        RenderWrapper.defaultBlendFunc();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        RenderWrapper.setShader(GameRenderer::m_172811_);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        addVertex(poseStack.last().pose(), bufferBuilder, x, height + y, zLevel, rgba);
        addVertex(poseStack.last().pose(), bufferBuilder, x + width, height + y, zLevel, rgba);
        addVertex(poseStack.last().pose(), bufferBuilder, x + width, y, zLevel, rgba);
        addVertex(poseStack.last().pose(), bufferBuilder, x, y, zLevel, rgba);
        BufferUploader.drawWithShader(bufferBuilder.end());
        RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderWrapper.disableBlend();
        RenderWrapper.enableTexture();
    }

    public static void drawPolygon(PoseStack poseStack, double xOffset, double yOffset, List<Double> fillPoints, List<List<Double>> strokePoints, ShapeProperties shapeProperties, int zoom) {
        RenderWrapper.enableBlend();
        RenderWrapper.disableTexture();
        RenderWrapper.blendFuncSeparate(770, 771, 1, 0);
        if (shapeProperties.getFillOpacity() >= 0.01F) {
            float[] rgba = RGB.floats(shapeProperties.getFillColor(), shapeProperties.getFillOpacity() + 0.4F);
            RenderWrapper.setColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
            int lastIndex = fillPoints.size() - 1;
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();
            RenderWrapper.disableTexture();
            RenderWrapper.disableCull();
            RenderWrapper.setShader(GameRenderer::m_172811_);
            buffer.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
            for (int i = 0; i <= lastIndex; i++) {
                Double point = (Double) fillPoints.get(i);
                addVertex(poseStack.last().pose(), buffer, point.getX() + xOffset, point.getY() + yOffset, zLevel, rgba);
            }
            tessellator.end();
            RenderWrapper.enableCull();
            RenderWrapper.enableTexture();
        }
        if (shapeProperties.getStrokeOpacity() >= 0.01F && shapeProperties.getStrokeWidth() > 0.0F) {
            float[] rgba = RGB.floats(shapeProperties.getStrokeColor(), shapeProperties.getStrokeOpacity() + 0.4F);
            RenderWrapper.setColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
            float halfWidth = shapeProperties.getStrokeWidth() / 2.0F;
            for (List<Double> screenPoints : strokePoints) {
                if (screenPoints.size() >= 3) {
                    int lastIndex = screenPoints.size() - 1;
                    RenderWrapper.disableTexture();
                    RenderWrapper.disableCull();
                    RenderWrapper.setShader(GameRenderer::m_172811_);
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder buffer = tesselator.getBuilder();
                    buffer.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
                    for (int i = 0; i <= lastIndex + 1; i++) {
                        int iPoint = i <= lastIndex ? i : 0;
                        int iPrev = iPoint > 0 ? iPoint - 1 : lastIndex;
                        int iNext = iPoint < lastIndex ? iPoint + 1 : 0;
                        Double point = (Double) screenPoints.get(iPoint);
                        Double prev = (Double) screenPoints.get(iPrev);
                        Double next = (Double) screenPoints.get(iNext);
                        Double normal = calculateNormal(prev, point, next);
                        addVertex(poseStack.last().pose(), buffer, point.getX() + normal.getX() * (double) halfWidth + xOffset, point.getY() + normal.getY() * (double) halfWidth + yOffset, zLevel, rgba);
                        addVertex(poseStack.last().pose(), buffer, point.getX() - normal.getX() * (double) halfWidth + xOffset, point.getY() - normal.getY() * (double) halfWidth + yOffset, zLevel, rgba);
                    }
                    tesselator.end();
                }
            }
            RenderWrapper.enableCull();
            RenderWrapper.enableTexture();
        }
        RenderWrapper.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderWrapper.enableTexture();
        RenderWrapper.disableBlend();
    }

    private static Double calculateNormal(Double prev, Double point, Double next) {
        Vec2 edge1 = new Vec2((float) (point.x - prev.x), (float) (point.y - prev.y));
        Vec2 edge2 = new Vec2((float) (next.x - point.x), (float) (next.y - point.y));
        Vec2 edgeNormal1 = new Vec2(-edge1.y, edge1.x).normalized();
        Vec2 edgeNormal2 = new Vec2(-edge2.y, edge2.x).normalized();
        Vec2 vertexNormal = edgeNormal1.add(edgeNormal2).normalized();
        double angle = Math.acos((double) vertexNormal.dot(edgeNormal1));
        double factor = Math.cos(angle);
        if (factor < 0.1) {
            factor = 0.1;
        }
        factor = 1.0 / factor;
        return new Double((double) vertexNormal.x * factor, (double) vertexNormal.y * factor);
    }

    private static Double calculateLineVector(Double value, double xOffset, double yOffset) {
        double locX = value.x + xOffset;
        double locY = value.y + yOffset;
        boolean minimapDrawing = UIManager.INSTANCE.getMiniMap().isDrawing() && UIManager.INSTANCE.isMiniMapEnabled();
        double screenVecX = minimapDrawing ? UIManager.INSTANCE.getMiniMap().getDisplayVars().centerPoint.x : locX;
        double screenVecY = minimapDrawing ? UIManager.INSTANCE.getMiniMap().getDisplayVars().centerPoint.y : locY;
        return new Double(screenVecX * 1.004, screenVecY * 1.004);
    }

    public static void drawGradientRect(PoseStack poseStack, double x, double y, double width, double height, int startColor, float startAlpha, int endColor, float endAlpha) {
        if (startAlpha > 1.0F) {
            startAlpha /= 255.0F;
        }
        if (endAlpha > 1.0F) {
            endAlpha /= 255.0F;
        }
        int[] rgbaStart = RGB.ints(startColor, startAlpha);
        int[] rgbaEnd = RGB.ints(endColor, endAlpha);
        RenderWrapper.disableTexture();
        RenderWrapper.enableBlend();
        RenderWrapper.blendFuncSeparate(770, 771, 1, 0);
        RenderWrapper.setShader(GameRenderer::m_172811_);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        addVertexUV(poseStack, buffer, x, height + y, zLevel, 0.0, 1.0, rgbaEnd);
        addVertexUV(poseStack, buffer, x + width, height + y, zLevel, 1.0, 1.0, rgbaEnd);
        addVertexUV(poseStack, buffer, x + width, y, zLevel, 1.0, 0.0, rgbaStart);
        addVertexUV(poseStack, buffer, x, y, zLevel, 0.0, 0.0, rgbaStart);
        tessellator.end();
        RenderWrapper.enableTexture();
        RenderWrapper.enableBlend();
    }

    public static void drawBoundTexture(PoseStack poseStack, double startU, double startV, double startX, double startY, double z, double endU, double endV, double endX, double endY) {
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        addVertexWithUV(poseStack, bufferBuilder, startX, endY, z, startU, endV);
        addVertexWithUV(poseStack, bufferBuilder, endX, endY, z, endU, endV);
        addVertexWithUV(poseStack, bufferBuilder, endX, startY, z, endU, startV);
        addVertexWithUV(poseStack, bufferBuilder, startX, startY, z, startU, startV);
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public static void drawEntity(PoseStack poseStack, double x, double y, double heading, Texture texture, float scale, double rotation) {
        drawEntity(poseStack, x, y, heading, texture, 1.0F, scale, rotation);
    }

    public static void drawEntity(PoseStack poseStack, double x, double y, double heading, Texture texture, float alpha, float scale, double rotation) {
        double width = (double) ((float) texture.getWidth() * scale);
        double height = (double) ((float) texture.getHeight() * scale);
        double drawX = x - width / 2.0;
        double drawY = y - height / 2.0;
        drawImage(poseStack, texture, alpha, drawX, drawY, false, scale, heading);
    }

    public static void drawColoredEntity(PoseStack poseStack, double x, double y, Texture texture, int color, float alpha, float scale, double rotation) {
        double width = (double) ((float) texture.getWidth() * scale);
        double height = (double) ((float) texture.getHeight() * scale);
        double drawX = x - width / 2.0;
        double drawY = y - height / 2.0;
        drawColoredImage(poseStack, texture, color, alpha, drawX, drawY, scale, rotation);
    }

    public static void sizeDisplay(PoseStack poseStack, double width, double height) {
        if (width > 0.0 && height > 0.0) {
            RenderWrapper.clear(256);
            Matrix4f matrix4f = new Matrix4f().setOrtho(0.0F, (float) width, (float) height, 0.0F, 100.0F, GuiHooks.getGuiFarPlane());
            RenderWrapper.setProjectionMatrix(matrix4f);
            PoseStack posestack = RenderWrapper.getModelViewStack();
            posestack.setIdentity();
            posestack.translate(0.0, 0.0, (double) (1000.0F - GuiHooks.getGuiFarPlane()));
        }
    }

    public static void addVertexWithUV(PoseStack poseStack, BufferBuilder buff, float x, float y, float z, float u, float v) {
        PoseStack.Pose entry = poseStack.last();
        Matrix4f matrix4f = entry.pose();
        buff.m_252986_(matrix4f, x, y, z).uv(u, v).endVertex();
    }

    public static void addVertexWithUV(PoseStack poseStack, BufferBuilder buff, double x, double y, double z, double u, double v) {
        addVertexWithUV(poseStack, buff, (float) x, (float) y, (float) z, (float) u, (float) v);
    }

    public static void addVertexUV(PoseStack poseStack, BufferBuilder buff, double x, double y, double z, double u, double v, int[] rgba) {
        PoseStack.Pose entry = poseStack.last();
        Matrix4f matrix4f = entry.pose();
        buff.m_252986_(matrix4f, (float) x, (float) y, (float) z).color(rgba[0], rgba[1], rgba[2], rgba[3]).uv((float) u, (float) v).endVertex();
    }

    private static void addVertex(Matrix4f mat, BufferBuilder buff, double x, double y, double z, int[] rgba) {
        buff.m_252986_(mat, (float) x, (float) y, (float) z).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
    }

    private static void addVertex(Matrix4f mat, BufferBuilder buff, double x, double y, double z, float[] rgba) {
        buff.m_252986_(mat, (float) x, (float) y, (float) z).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
    }

    private static void addVertexNormal(Matrix4f mat, BufferBuilder buff, double x, double y, double z, float pX, float pY, float pZ, float[] rgba) {
        buff.m_252986_(mat, (float) x, (float) y, (float) z).color(rgba[0], rgba[1], rgba[2], rgba[3]).normal(pX, pY, pZ).endVertex();
    }

    private static void addVertexNormal3f(Matrix4f mat, Matrix3f normal, BufferBuilder buff, double x, double y, double z, float pX, float pY, float pZ, float[] rgba) {
        buff.m_252986_(mat, (float) x, (float) y, (float) z).color(rgba[0], rgba[1], rgba[2], rgba[3]).normal(normal, pX, pY, pZ).endVertex();
    }

    public static enum HAlign {

        Left, Center, Right
    }

    public static enum VAlign {

        Above, Middle, Below
    }
}