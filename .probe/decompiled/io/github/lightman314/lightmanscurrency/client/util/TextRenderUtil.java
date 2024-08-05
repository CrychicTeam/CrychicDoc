package io.github.lightman314.lightmanscurrency.client.util;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

public class TextRenderUtil {

    public static Font getFont() {
        Minecraft mc = Minecraft.getInstance();
        return mc.font;
    }

    public static Component fitString(String text, int width) {
        return fitString(text, width, "...");
    }

    public static Component fitString(String text, int width, Style style) {
        return fitString(text, width, "...", style);
    }

    public static Component fitString(String text, int width, String edge) {
        return fitString(Component.literal(text), width, edge);
    }

    public static Component fitString(Component component, int width) {
        return fitString(component.getString(), width, "...", component.getStyle());
    }

    public static Component fitString(Component component, int width, String edge) {
        return fitString(component.getString(), width, edge, component.getStyle());
    }

    public static Component fitString(Component component, int width, Style style) {
        return fitString(component.getString(), width, "...", style);
    }

    public static Component fitString(Component component, int width, String edge, Style style) {
        return fitString(component.getString(), width, edge, style);
    }

    public static Component fitString(String text, int width, String edge, Style style) {
        Font font = getFont();
        if (font.width(Component.literal(text).withStyle(style)) <= width) {
            return Component.literal(text).withStyle(style);
        } else {
            while (font.width(Component.literal(text + edge).withStyle(style)) > width && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
            }
            return Component.literal(text + edge).withStyle(style);
        }
    }

    public static void drawCenteredText(EasyGuiGraphics gui, String string, int centerX, int yPos, int color) {
        drawCenteredText(gui, Component.literal(string), centerX, yPos, color);
    }

    public static void drawCenteredText(EasyGuiGraphics gui, Component component, int centerX, int yPos, int color) {
        int width = gui.font.width(component);
        gui.drawString(component, centerX - width / 2, yPos, color);
    }

    public static void drawRightEdgeText(EasyGuiGraphics gui, String string, int rightPos, int yPos, int color) {
        drawRightEdgeText(gui, Component.literal(string), rightPos, yPos, color);
    }

    public static void drawRightEdgeText(EasyGuiGraphics gui, Component component, int rightPos, int yPos, int color) {
        int width = gui.font.width(component);
        gui.drawString(component, rightPos - width, yPos, color);
    }

    public static void drawCenteredMultilineText(EasyGuiGraphics gui, String string, int leftPos, int width, int topPos, int color) {
        drawCenteredMultilineText(gui, Component.literal(string), leftPos, width, topPos, color);
    }

    public static void drawCenteredMultilineText(EasyGuiGraphics gui, Component component, int leftPos, int width, int topPos, int color) {
        Font font = getFont();
        List<FormattedCharSequence> lines = font.split(component, width);
        float centerPos = (float) leftPos + (float) width / 2.0F;
        for (int i = 0; i < lines.size(); i++) {
            FormattedCharSequence line = (FormattedCharSequence) lines.get(i);
            int lineWidth = font.width(line);
            gui.drawString(line, (int) (centerPos - (float) lineWidth / 2.0F), topPos + 9 * i, color);
        }
    }

    public static void drawVerticallyCenteredMultilineText(EasyGuiGraphics gui, String string, int leftPos, int width, int topPos, int height, int color) {
        drawVerticallyCenteredMultilineText(gui, Component.literal(string), leftPos, width, topPos, height, color);
    }

    public static void drawVerticallyCenteredMultilineText(EasyGuiGraphics gui, Component component, int leftPos, int width, int topPos, int height, int color) {
        Font font = getFont();
        List<FormattedCharSequence> lines = font.split(component, width);
        float centerPos = (float) leftPos + (float) width / 2.0F;
        float startHeight = (float) topPos + (float) height / 2.0F - (float) (9 * lines.size()) / 2.0F;
        for (int i = 0; i < lines.size(); i++) {
            FormattedCharSequence line = (FormattedCharSequence) lines.get(i);
            int lineWidth = font.width(line);
            gui.drawString(line, (int) (centerPos - (float) lineWidth / 2.0F), (int) (startHeight + (float) (9 * i)), color);
        }
    }

    public static MutableComponent changeStyle(Component component, UnaryOperator<Style> styleChanges) {
        return component instanceof MutableComponent mc ? mc.withStyle(styleChanges) : Component.empty().append(component).withStyle(component.getStyle()).withStyle(styleChanges);
    }

    public static class TextFormatting {

        private TextRenderUtil.TextFormatting.Centering centering = TextRenderUtil.TextFormatting.Centering.MIDDLE_CENTER;

        private int color = 16777215;

        public TextRenderUtil.TextFormatting.Centering centering() {
            return this.centering;
        }

        public int color() {
            return this.color;
        }

        private TextFormatting() {
        }

        public static TextRenderUtil.TextFormatting create() {
            return new TextRenderUtil.TextFormatting();
        }

        public TextRenderUtil.TextFormatting topEdge() {
            this.centering = this.centering.makeTop();
            return this;
        }

        public TextRenderUtil.TextFormatting middle() {
            this.centering = this.centering.makeMiddle();
            return this;
        }

        public TextRenderUtil.TextFormatting bottomEdge() {
            this.centering = this.centering.makeBottom();
            return this;
        }

        public TextRenderUtil.TextFormatting leftEdge() {
            this.centering = this.centering.makeLeft();
            return this;
        }

        public TextRenderUtil.TextFormatting centered() {
            this.centering = this.centering.makeCenter();
            return this;
        }

        public TextRenderUtil.TextFormatting rightEdge() {
            this.centering = this.centering.makeRight();
            return this;
        }

        public TextRenderUtil.TextFormatting color(int color) {
            this.color = color;
            return this;
        }

        public static enum Centering {

            TOP_LEFT(-1, 1),
            TOP_CENTER(0, 1),
            TOP_RIGHT(1, 1),
            MIDDLE_LEFT(-1, 0),
            MIDDLE_CENTER(0, 0),
            MIDDLE_RIGHT(1, 0),
            BOTTOM_LEFT(-1, -1),
            BOTTOM_CENTER(0, -1),
            BOTTOM_RIGHT(1, -1);

            private final int horiz;

            private final int vert;

            private Centering(int horiz, int vert) {
                this.horiz = horiz;
                this.vert = vert;
            }

            public boolean isTop() {
                return this.vert > 0;
            }

            public boolean isMiddle() {
                return this.vert == 0;
            }

            public boolean isBottom() {
                return this.vert < 0;
            }

            public boolean isLeft() {
                return this.horiz < 0;
            }

            public boolean isCenter() {
                return this.horiz == 0;
            }

            public boolean isRight() {
                return this.horiz > 1;
            }

            public TextRenderUtil.TextFormatting.Centering makeTop() {
                return this.of(this.horiz, 1);
            }

            public TextRenderUtil.TextFormatting.Centering makeMiddle() {
                return this.of(this.horiz, 0);
            }

            public TextRenderUtil.TextFormatting.Centering makeBottom() {
                return this.of(this.horiz, -1);
            }

            public TextRenderUtil.TextFormatting.Centering makeLeft() {
                return this.of(-1, this.vert);
            }

            public TextRenderUtil.TextFormatting.Centering makeCenter() {
                return this.of(0, this.vert);
            }

            public TextRenderUtil.TextFormatting.Centering makeRight() {
                return this.of(1, this.vert);
            }

            private TextRenderUtil.TextFormatting.Centering of(int horiz, int vert) {
                for (TextRenderUtil.TextFormatting.Centering c : values()) {
                    if (c.horiz == horiz && c.vert == vert) {
                        return c;
                    }
                }
                return this;
            }
        }
    }
}