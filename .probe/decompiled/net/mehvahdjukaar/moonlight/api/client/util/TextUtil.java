package net.mehvahdjukaar.moonlight.api.client.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TextUtil {

    private static final FormattedCharSequence CURSOR_MARKER = FormattedCharSequence.forward("_", Style.EMPTY);

    public static Pair<List<FormattedCharSequence>, Float> fitLinesToBox(Font font, FormattedText text, float width, float height) {
        int fontWidth = font.width(text);
        int scalingFactor;
        List<FormattedCharSequence> splitLines;
        float maxLines;
        do {
            scalingFactor = Mth.floor(Mth.sqrt((float) fontWidth * 8.0F / (width * height)));
            splitLines = font.split(text, Mth.floor(width * (float) scalingFactor));
            maxLines = height * (float) scalingFactor / 8.0F;
            fontWidth++;
        } while (maxLines < (float) splitLines.size());
        return Pair.of(splitLines, 1.0F / (float) scalingFactor);
    }

    public static FormattedText parseText(String s) {
        try {
            FormattedText mutableComponent = Component.Serializer.fromJson(s);
            if (mutableComponent != null) {
                return mutableComponent;
            }
        } catch (Exception var2) {
        }
        return FormattedText.of(s);
    }

    public static void renderGuiLine(TextUtil.RenderProperties properties, String string, Font font, GuiGraphics graphics, MultiBufferSource.BufferSource buffer, int cursorPos, int selectionPos, boolean isSelected, boolean blink, int yOffset) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        Matrix4f matrix4f = poseStack.last().pose();
        if (string != null) {
            if (font.isBidirectional()) {
                string = font.bidirectionalShaping(string);
            }
            FormattedCharSequence charSequence = FormattedCharSequence.forward(string, properties.style);
            float centerX = (float) (-font.width(charSequence)) / 2.0F;
            renderLineInternal(charSequence, font, centerX, (float) yOffset, matrix4f, buffer, properties);
            String substring = string.substring(0, Math.min(cursorPos, string.length()));
            if (isSelected) {
                int pX = (int) ((float) font.width(FormattedCharSequence.forward(substring, properties.style)) + centerX);
                if (blink) {
                    if (cursorPos >= string.length()) {
                        renderLineInternal(CURSOR_MARKER, font, (float) pX, (float) yOffset, matrix4f, buffer, properties);
                    }
                    buffer.endBatch();
                }
                if (blink && cursorPos < string.length()) {
                    graphics.fill(pX, yOffset - 1, pX + 1, yOffset + 9, 0xFF000000 | properties.textColor);
                }
                if (selectionPos != cursorPos) {
                    int l3 = Math.min(cursorPos, selectionPos);
                    int l1 = Math.max(cursorPos, selectionPos);
                    int i2 = font.width(string.substring(0, l3)) - font.width(string) / 2;
                    int j2 = font.width(string.substring(0, l1)) - font.width(string) / 2;
                    int startX = Math.min(i2, j2);
                    int startY = Math.max(i2, j2);
                    RenderSystem.enableColorLogicOp();
                    RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
                    graphics.fill(startX, startY, yOffset, yOffset + 9, -16776961);
                    RenderSystem.disableColorLogicOp();
                }
            }
            if (!isSelected || !blink) {
                buffer.endBatch();
            }
        }
    }

    public static void renderGuiText(TextUtil.RenderProperties properties, String[] guiLines, Font font, GuiGraphics graphics, MultiBufferSource.BufferSource buffer, int cursorPos, int selectionPos, int currentLine, boolean blink, int lineSpacing) {
        int nOfLines = guiLines.length;
        for (int line = 0; line < nOfLines; line++) {
            int yOffset = line * lineSpacing - nOfLines * 5;
            renderGuiLine(properties, guiLines[line], font, graphics, buffer, cursorPos, selectionPos, line == currentLine, blink, yOffset);
        }
    }

    public static void renderLine(FormattedCharSequence formattedCharSequences, Font font, float yOffset, PoseStack poseStack, MultiBufferSource buffer, TextUtil.RenderProperties properties) {
        if (formattedCharSequences != null) {
            float x = (float) (-font.width(formattedCharSequences)) / 2.0F;
            renderLineInternal(formattedCharSequences, font, x, yOffset, poseStack.last().pose(), buffer, properties);
        }
    }

    public static void renderAllLines(FormattedCharSequence[] charSequences, int ySeparation, Font font, PoseStack poseStack, MultiBufferSource buffer, TextUtil.RenderProperties properties) {
        for (int i = 0; i < charSequences.length; i++) {
            renderLine(charSequences[i], font, (float) (ySeparation * i), poseStack, buffer, properties);
        }
    }

    private static void renderLineInternal(FormattedCharSequence formattedCharSequences, Font font, float xOffset, float yOffset, Matrix4f matrix4f, MultiBufferSource buffer, TextUtil.RenderProperties properties) {
        if (properties.outline) {
            font.drawInBatch8xOutline(formattedCharSequences, xOffset, yOffset, properties.textColor, properties.darkenedColor, matrix4f, buffer, properties.light);
        } else {
            font.drawInBatch(formattedCharSequences, xOffset, yOffset, properties.darkenedColor, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, properties.light);
        }
    }

    private static int getDarkenedColor(int color, boolean glowing, float mult) {
        return color == DyeColor.BLACK.getTextColor() && glowing ? -988212 : ColorUtils.multiply(color, 0.4F * (glowing ? 1.0F : mult));
    }

    private static int getDarkenedColor(int color, boolean glowing) {
        return getDarkenedColor(color, glowing, 1.0F);
    }

    public static TextUtil.RenderProperties renderProperties(DyeColor dyeColor, boolean glowing, int combinedLight, Style style, Vector3f normal, BooleanSupplier isVeryNear) {
        return renderProperties(dyeColor, glowing, 1.0F, combinedLight, style, normal, isVeryNear);
    }

    public static TextUtil.RenderProperties renderProperties(DyeColor dyeColor, boolean glowing, float darkColorMult, int combinedLight, Style style, Vector3f normal, BooleanSupplier isVeryNear) {
        boolean outline = glowing && (dyeColor == DyeColor.BLACK || isVeryNear.getAsBoolean());
        int textColor = dyeColor.getTextColor();
        float shading = ColorUtils.getShading(normal);
        int color = glowing ? textColor : ColorUtils.multiply(textColor, shading);
        int dark;
        if (glowing && !outline) {
            dark = color;
        } else {
            dark = getDarkenedColor(textColor, glowing, darkColorMult * shading);
        }
        return new TextUtil.RenderProperties(color, dark, outline, glowing ? 15728880 : combinedLight, style);
    }

    public static record RenderProperties(int textColor, int darkenedColor, boolean outline, int light, Style style) {

        @Deprecated(forRemoval = true)
        public RenderProperties(DyeColor color, boolean outline, int combinedLight, Style style, BooleanSupplier isVeryNear) {
            this(color.getTextColor(), TextUtil.getDarkenedColor(color.getTextColor(), outline), outline && (isVeryNear.getAsBoolean() || color == DyeColor.BLACK), outline ? 15728880 : combinedLight, style);
        }
    }
}