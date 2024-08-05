package icyllis.modernui.mc.text;

import icyllis.modernui.graphics.MathUtil;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ModernTextRenderer {

    public static final Vector3f SHADOW_OFFSET = new Vector3f(0.0F, 0.0F, 0.03F);

    public static final Vector3f OUTLINE_OFFSET = new Vector3f(0.0F, 0.0F, 0.01F);

    public static volatile boolean sAllowShadow = true;

    public static volatile float sShadowOffset = 1.0F;

    public static volatile float sOutlineOffset = 0.5F;

    public static volatile boolean sComputeDeviceFontSize = true;

    public static volatile boolean sAllowSDFTextIn2D = true;

    private final TextLayoutEngine mEngine;

    public ModernTextRenderer(TextLayoutEngine engine) {
        this.mEngine = engine;
    }

    public float drawText(@Nonnull String text, float x, float y, int color, boolean dropShadow, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight) {
        if (text.isEmpty()) {
            return x;
        } else {
            int a = color >>> 24;
            if (a <= 2) {
                a = 255;
            }
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            int mode = this.chooseMode(matrix, displayMode);
            boolean polygonOffset = displayMode == Font.DisplayMode.POLYGON_OFFSET;
            TextLayout layout = this.mEngine.lookupVanillaLayout(text);
            if (layout.hasColorEmoji() && source instanceof MultiBufferSource.BufferSource) {
                ((MultiBufferSource.BufferSource) source).endBatch(Sheets.signSheet());
            }
            if (dropShadow && sAllowShadow) {
                float offset = sShadowOffset;
                layout.drawText(matrix, source, x + offset, y + offset, r >> 2, g >> 2, b >> 2, a, true, mode, polygonOffset, colorBackground, packedLight);
                matrix = new Matrix4f(matrix);
                matrix.translate(SHADOW_OFFSET);
            }
            return x + layout.drawText(matrix, source, x, y, r, g, b, a, false, mode, polygonOffset, colorBackground, packedLight);
        }
    }

    public float drawText(@Nonnull FormattedText text, float x, float y, int color, boolean dropShadow, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight) {
        if (text != CommonComponents.EMPTY && text != FormattedText.EMPTY) {
            int a = color >>> 24;
            if (a <= 2) {
                a = 255;
            }
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            int mode = this.chooseMode(matrix, displayMode);
            boolean polygonOffset = displayMode == Font.DisplayMode.POLYGON_OFFSET;
            TextLayout layout = this.mEngine.lookupFormattedLayout(text);
            if (layout.hasColorEmoji() && source instanceof MultiBufferSource.BufferSource) {
                ((MultiBufferSource.BufferSource) source).endBatch(Sheets.signSheet());
            }
            if (dropShadow && sAllowShadow) {
                float offset = sShadowOffset;
                layout.drawText(matrix, source, x + offset, y + offset, r >> 2, g >> 2, b >> 2, a, true, mode, polygonOffset, colorBackground, packedLight);
                matrix = new Matrix4f(matrix);
                matrix.translate(SHADOW_OFFSET);
            }
            return x + layout.drawText(matrix, source, x, y, r, g, b, a, false, mode, polygonOffset, colorBackground, packedLight);
        } else {
            return x;
        }
    }

    public float drawText(@Nonnull FormattedCharSequence text, float x, float y, int color, boolean dropShadow, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight) {
        if (text == FormattedCharSequence.EMPTY) {
            return x;
        } else {
            int a = color >>> 24;
            if (a <= 2) {
                a = 255;
            }
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            int mode = this.chooseMode(matrix, displayMode);
            boolean polygonOffset = displayMode == Font.DisplayMode.POLYGON_OFFSET;
            TextLayout layout = this.mEngine.lookupFormattedLayout(text);
            if (layout.hasColorEmoji() && source instanceof MultiBufferSource.BufferSource) {
                ((MultiBufferSource.BufferSource) source).endBatch(Sheets.signSheet());
            }
            if (dropShadow && sAllowShadow) {
                float offset = sShadowOffset;
                layout.drawText(matrix, source, x + offset, y + offset, r >> 2, g >> 2, b >> 2, a, true, mode, polygonOffset, colorBackground, packedLight);
                matrix = new Matrix4f(matrix);
                matrix.translate(SHADOW_OFFSET);
            }
            return x + layout.drawText(matrix, source, x, y, r, g, b, a, false, mode, polygonOffset, colorBackground, packedLight);
        }
    }

    public int chooseMode(Matrix4f ctm, Font.DisplayMode displayMode) {
        if (displayMode == Font.DisplayMode.SEE_THROUGH) {
            return 3;
        } else if (TextLayoutEngine.sCurrentInWorldRendering) {
            return 1;
        } else {
            if ((ctm.properties() & 8) == 0 && (sComputeDeviceFontSize || sAllowSDFTextIn2D)) {
                if (MathUtil.isApproxZero(ctm.m01()) && MathUtil.isApproxZero(ctm.m02()) && MathUtil.isApproxZero(ctm.m03()) && MathUtil.isApproxZero(ctm.m10()) && MathUtil.isApproxZero(ctm.m12()) && MathUtil.isApproxZero(ctm.m13()) && MathUtil.isApproxZero(ctm.m20()) && MathUtil.isApproxZero(ctm.m21()) && MathUtil.isApproxZero(ctm.m23()) && MathUtil.isApproxEqual(ctm.m33(), 1.0F)) {
                    if (MathUtil.isApproxEqual(ctm.m00(), 1.0F) && MathUtil.isApproxEqual(ctm.m11(), 1.0F)) {
                        return 0;
                    }
                    if (sComputeDeviceFontSize && MathUtil.isApproxEqual(ctm.m00(), ctm.m11())) {
                        float upperLimit = Math.max(1.0F, 4.0F / (float) this.mEngine.getResLevel());
                        if (ctm.m00() < upperLimit) {
                            return 4;
                        }
                    }
                }
                if (sAllowSDFTextIn2D) {
                    return 1;
                }
            }
            return 0;
        }
    }

    public void drawText8xOutline(@Nonnull FormattedCharSequence text, float x, float y, int color, int outlineColor, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, int packedLight) {
        if (text != FormattedCharSequence.EMPTY) {
            boolean isBlack = (color & 16777215) == 0;
            if (isBlack) {
                color = outlineColor;
            }
            int a = color >>> 24;
            if (a <= 2) {
                a = 255;
            }
            int r = color >> 16 & 0xFF;
            int g = color >> 8 & 0xFF;
            int b = color & 0xFF;
            TextLayout layout = this.mEngine.lookupFormattedLayout(text);
            if (layout.hasColorEmoji() && source instanceof MultiBufferSource.BufferSource) {
                ((MultiBufferSource.BufferSource) source).endBatch(Sheets.signSheet());
            }
            layout.drawText(matrix, source, x, y, r, g, b, a, false, 1, false, 0, packedLight);
            if (!isBlack && (!TextLayoutEngine.sCurrentInWorldRendering || TextLayoutEngine.sUseTextShadersInWorld)) {
                matrix = new Matrix4f(matrix);
                a = outlineColor >>> 24;
                if (a <= 2) {
                    a = 255;
                }
                r = outlineColor >> 16 & 0xFF;
                g = outlineColor >> 8 & 0xFF;
                b = outlineColor & 0xFF;
                matrix.translate(OUTLINE_OFFSET);
                layout.drawTextOutline(matrix, source, x, y, r, g, b, a, packedLight);
            }
        }
    }
}