package dev.ftb.mods.ftblibrary.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class TooltipList {

    private final List<Component> lines = new ArrayList();

    public int zOffset = 950;

    public int zOffsetItemTooltip = 0;

    public int backgroundColor = -1072693232;

    public int borderColorStart = 1347420415;

    public int borderColorEnd = (this.borderColorStart & 16711422) >> 1 | this.borderColorStart & 0xFF000000;

    public int maxWidth = 0;

    public int xOffset = 0;

    public int yOffset = 0;

    public boolean shouldRender() {
        return !this.lines.isEmpty();
    }

    public void reset() {
        this.lines.clear();
        this.zOffset = 950;
        this.zOffsetItemTooltip = 0;
        this.backgroundColor = -1072693232;
        this.borderColorStart = 1347420415;
        this.borderColorEnd = (this.borderColorStart & 16711422) >> 1 | this.borderColorStart & 0xFF000000;
        this.maxWidth = 0;
        this.xOffset = 0;
        this.yOffset = 0;
    }

    public void add(Component component) {
        this.lines.add(component);
    }

    public void blankLine() {
        this.add(Component.empty());
    }

    public void styledString(String text, Style style) {
        this.add(Component.literal(text).withStyle(style));
    }

    public void styledString(String text, ChatFormatting color) {
        this.add(Component.literal(text).withStyle(color));
    }

    public void styledTranslate(String key, Style style, Object... objects) {
        this.add(Component.translatable(key, objects).withStyle(style));
    }

    public void string(String text) {
        this.styledString(text, Style.EMPTY);
    }

    public void translate(String key, Object... objects) {
        this.styledTranslate(key, Style.EMPTY, objects);
    }

    public List<Component> getLines() {
        return this.lines;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics graphics, int mouseX, int mouseY, int screenWidth, int screenHeight, Font font) {
        mouseX += this.xOffset;
        mouseY += this.yOffset;
        List<FormattedCharSequence> textLines = new ArrayList(this.lines.size());
        for (Component component : this.lines) {
            textLines.add(component.getVisualOrderText());
        }
        RenderSystem.disableDepthTest();
        int tooltipTextWidth = 0;
        for (FormattedCharSequence textLine : textLines) {
            int textLineWidth = font.width(textLine);
            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth;
            }
        }
        boolean needsWrap = false;
        int titleLinesCount = 1;
        int tooltipX = mouseX + 12;
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth;
            if (tooltipX < 4) {
                if (mouseX > screenWidth / 2) {
                    tooltipTextWidth = mouseX - 12 - 8;
                } else {
                    tooltipTextWidth = screenWidth - 16 - mouseX;
                }
                needsWrap = true;
            }
        }
        if (this.maxWidth > 0 && tooltipTextWidth > this.maxWidth) {
            tooltipTextWidth = this.maxWidth;
            needsWrap = true;
        }
        if (needsWrap) {
            int wrappedTooltipWidth = 0;
            List<FormattedCharSequence> wrappedTextLines = new ArrayList();
            for (int i = 0; i < this.lines.size(); i++) {
                Component textLinex = (Component) this.lines.get(i);
                List<FormattedCharSequence> wrappedLine = font.split(textLinex, tooltipTextWidth);
                if (i == 0) {
                    titleLinesCount = wrappedLine.size();
                }
                for (FormattedCharSequence line : wrappedLine) {
                    int lineWidth = font.width(line);
                    if (lineWidth > wrappedTooltipWidth) {
                        wrappedTooltipWidth = lineWidth;
                    }
                    wrappedTextLines.add(line);
                }
            }
            tooltipTextWidth = wrappedTooltipWidth;
            textLines = wrappedTextLines;
            if (mouseX > screenWidth / 2) {
                tooltipX = mouseX - 16 - wrappedTooltipWidth;
            } else {
                tooltipX = mouseX + 12;
            }
        }
        int tooltipY = mouseY - 12;
        int tooltipHeight = 8;
        if (textLines.size() > 1) {
            tooltipHeight += (textLines.size() - 1) * 10;
            if (textLines.size() > titleLinesCount) {
                tooltipHeight += 2;
            }
        }
        if (tooltipY < 4) {
            tooltipY = 4;
        } else if (tooltipY + tooltipHeight + 4 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 4;
        }
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, (float) this.zOffset);
        Matrix4f mat = poseStack.last().pose();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172811_);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        drawGradientRect(mat, buffer, tooltipX - 3, tooltipY - 4, tooltipX + tooltipTextWidth + 3, tooltipY - 3, this.backgroundColor, this.backgroundColor);
        drawGradientRect(mat, buffer, tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 4, this.backgroundColor, this.backgroundColor);
        drawGradientRect(mat, buffer, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, this.backgroundColor, this.backgroundColor);
        drawGradientRect(mat, buffer, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, this.backgroundColor, this.backgroundColor);
        drawGradientRect(mat, buffer, tooltipX + tooltipTextWidth + 3, tooltipY - 3, tooltipX + tooltipTextWidth + 4, tooltipY + tooltipHeight + 3, this.backgroundColor, this.backgroundColor);
        drawGradientRect(mat, buffer, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, this.borderColorStart, this.borderColorEnd);
        drawGradientRect(mat, buffer, tooltipX + tooltipTextWidth + 2, tooltipY - 3 + 1, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3 - 1, this.borderColorStart, this.borderColorEnd);
        drawGradientRect(mat, buffer, tooltipX - 3, tooltipY - 3, tooltipX + tooltipTextWidth + 3, tooltipY - 3 + 1, this.borderColorStart, this.borderColorStart);
        drawGradientRect(mat, buffer, tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + tooltipTextWidth + 3, tooltipY + tooltipHeight + 3, this.borderColorEnd, this.borderColorEnd);
        tesselator.end();
        RenderSystem.disableBlend();
        MultiBufferSource.BufferSource renderType = MultiBufferSource.immediate(buffer);
        for (int lineNumber = 0; lineNumber < textLines.size(); lineNumber++) {
            FormattedCharSequence line = (FormattedCharSequence) textLines.get(lineNumber);
            if (line != null) {
                font.drawInBatch(line, (float) tooltipX, (float) tooltipY, -1, true, mat, renderType, Font.DisplayMode.NORMAL, 0, 15728880);
            }
            if (lineNumber + 1 == titleLinesCount) {
                tooltipY += 2;
            }
            tooltipY += 10;
        }
        renderType.endBatch();
        poseStack.popPose();
        RenderSystem.enableDepthTest();
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawGradientRect(Matrix4f mat, BufferBuilder buffer, int left, int top, int right, int bottom, int startColor, int endColor) {
        int startAlpha = startColor >> 24 & 0xFF;
        int startRed = startColor >> 16 & 0xFF;
        int startGreen = startColor >> 8 & 0xFF;
        int startBlue = startColor & 0xFF;
        int endAlpha = endColor >> 24 & 0xFF;
        int endRed = endColor >> 16 & 0xFF;
        int endGreen = endColor >> 8 & 0xFF;
        int endBlue = endColor & 0xFF;
        buffer.m_252986_(mat, (float) right, (float) top, 0.0F).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.m_252986_(mat, (float) left, (float) top, 0.0F).color(startRed, startGreen, startBlue, startAlpha).endVertex();
        buffer.m_252986_(mat, (float) left, (float) bottom, 0.0F).color(endRed, endGreen, endBlue, endAlpha).endVertex();
        buffer.m_252986_(mat, (float) right, (float) bottom, 0.0F).color(endRed, endGreen, endBlue, endAlpha).endVertex();
    }
}