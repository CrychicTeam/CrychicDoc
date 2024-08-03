package icyllis.modernui.mc;

import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import icyllis.arc3d.core.MathUtil;
import icyllis.modernui.graphics.Color;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.StringDecomposer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

@Internal
public final class TooltipRenderer {

    public static volatile boolean sTooltip = true;

    public static final int[] sFillColor = new int[4];

    public static final int[] sStrokeColor = new int[4];

    public static volatile float sBorderWidth = 1.3333334F;

    public static volatile float sCornerRadius = 3.0F;

    public static volatile float sShadowRadius = 10.0F;

    public static volatile float sShadowAlpha = 0.35F;

    public static volatile boolean sAdaptiveColors = true;

    public static final int TOOLTIP_SPACE = 12;

    public static final int H_BORDER = 4;

    public static final int V_BORDER = 4;

    private static final int TITLE_GAP = 2;

    private final int[] mWorkStrokeColor = new int[4];

    private final int[] mActiveStrokeColor = new int[4];

    public static volatile int sBorderColorCycle = 1000;

    public static volatile boolean sExactPositioning = true;

    public static volatile boolean sRoundedShapes = true;

    public static volatile boolean sCenterTitle = true;

    public static volatile boolean sTitleBreak = true;

    public volatile boolean mLayoutRTL;

    private boolean mDraw;

    private float mScroll;

    private int mMarqueeDir;

    private long mMarqueeEndMillis;

    private static final long MARQUEE_DELAY_MILLIS = 1200L;

    private boolean mFrameGap;

    private long mCurrTimeMillis;

    private long mCurrDeltaMillis;

    private ItemStack mLastSeenItem;

    private boolean mUseSpectrum;

    public void update(long deltaMillis, long timeMillis) {
        if (this.mDraw) {
            this.mDraw = false;
            if (this.mFrameGap) {
                this.mMarqueeEndMillis = timeMillis;
                this.mMarqueeDir = 1;
                this.mScroll = 0.0F;
            }
            this.mFrameGap = false;
        } else {
            this.mFrameGap = true;
            this.mLastSeenItem = null;
        }
        this.mCurrTimeMillis = timeMillis;
        this.mCurrDeltaMillis = deltaMillis;
    }

    void computeWorkingColor(@Nonnull ItemStack item) {
        if (sAdaptiveColors && !item.isEmpty()) {
            if (sRoundedShapes && (item.is(Items.DRAGON_EGG) || item.is(Items.MOJANG_BANNER_PATTERN))) {
                this.mUseSpectrum = true;
                return;
            }
            Style baseStyle = Style.EMPTY;
            Rarity rarity = item.getRarity();
            if (rarity != Rarity.COMMON) {
                baseStyle = MuiModApi.get().applyRarityTo(rarity, baseStyle);
            }
            IntOpenHashSet colors = new IntOpenHashSet(16);
            StringDecomposer.iterateFormatted(item.getHoverName(), baseStyle, (i, style, ch) -> {
                TextColor textColor = style.getColor();
                return textColor == null ? false : colors.add(textColor.getValue() & 16777215) && colors.size() >= 16;
            });
            if (!colors.isEmpty()) {
                ArrayList<float[]> hsvColors = new ArrayList(16);
                IntIterator it = colors.iterator();
                while (it.hasNext()) {
                    int c = it.nextInt();
                    float[] hsv = new float[3];
                    Color.RGBToHSV(c, hsv);
                    hsv[1] = Math.min(hsv[1], 0.9F);
                    hsv[2] = MathUtil.clamp(hsv[2], 0.2F, 0.85F);
                    hsvColors.add(hsv);
                }
                if (!hsvColors.isEmpty()) {
                    int size = hsvColors.size();
                    if (size > 4) {
                        if (sRoundedShapes) {
                            this.mUseSpectrum = true;
                            return;
                        }
                        Collections.shuffle(hsvColors);
                    }
                    int c1 = Color.HSVToColor((float[]) hsvColors.get(0));
                    int c3;
                    int c4;
                    int c2;
                    if (size > 2) {
                        c2 = Color.HSVToColor((float[]) hsvColors.get(1));
                        c3 = Color.HSVToColor((float[]) hsvColors.get(2));
                        if (size == 4) {
                            c4 = Color.HSVToColor((float[]) hsvColors.get(3));
                        } else {
                            float[] hsv = (float[]) hsvColors.get(1);
                            hsv[0] = (hsv[0] + 180.0F) % 360.0F;
                            c4 = Color.HSVToColor(hsv);
                        }
                    } else if (size == 2) {
                        c3 = Color.HSVToColor((float[]) hsvColors.get(1));
                        c2 = lerpInLinearSpace(0.5F, c1, c3);
                        float[] hsv = new float[3];
                        Color.RGBToHSV(c2, hsv);
                        c4 = adjustColor(hsv, false, true, false, item.isEnchanted());
                    } else {
                        float[] hsv = (float[]) hsvColors.get(0);
                        boolean mag = item.isEnchanted();
                        c2 = adjustColor(hsv, false, true, false, mag);
                        c3 = adjustColor(hsv, true, true, true, mag);
                        c4 = adjustColor(hsv, true, false, true, mag);
                    }
                    this.mWorkStrokeColor[0] = sStrokeColor[0] & 0xFF000000 | c1;
                    this.mWorkStrokeColor[1] = sStrokeColor[1] & 0xFF000000 | c2;
                    this.mWorkStrokeColor[2] = sStrokeColor[2] & 0xFF000000 | c3;
                    this.mWorkStrokeColor[3] = sStrokeColor[3] & 0xFF000000 | c4;
                    this.mUseSpectrum = false;
                    return;
                }
            }
        }
        System.arraycopy(sStrokeColor, 0, this.mWorkStrokeColor, 0, 4);
        this.mUseSpectrum = false;
    }

    static int adjustColor(float[] hsv, boolean hue, boolean sat, boolean val, boolean magnified) {
        float h = hsv[0];
        float s = hsv[1];
        float v = hsv[2];
        if (hue) {
            if (h >= 60.0F && h <= 240.0F) {
                h += magnified ? 27.0F : 15.0F;
            } else {
                h -= magnified ? 18.0F : 10.0F;
            }
            h = (h + 360.0F) % 360.0F;
        }
        if (sat) {
            if (s < 0.6F) {
                s += magnified ? 0.18F : 0.12F;
            } else {
                s -= magnified ? 0.12F : 0.06F;
            }
        }
        if (val) {
            if (v < 0.6F) {
                v += magnified ? 0.12F : 0.08F;
            } else {
                v -= magnified ? 0.08F : 0.04F;
            }
        }
        return Color.HSVToColor(h, s, v);
    }

    void updateBorderColor() {
        float p = (float) (this.mCurrTimeMillis % (long) sBorderColorCycle) / (float) sBorderColorCycle;
        if (this.mLayoutRTL) {
            int pos = (int) (this.mCurrTimeMillis / (long) sBorderColorCycle & 3L);
            for (int i = 0; i < 4; i++) {
                this.mActiveStrokeColor[i] = lerpInLinearSpace(p, this.mWorkStrokeColor[i + pos & 3], this.mWorkStrokeColor[i + pos + 1 & 3]);
            }
        } else {
            int pos = 3 - (int) (this.mCurrTimeMillis / (long) sBorderColorCycle & 3L);
            for (int i = 0; i < 4; i++) {
                this.mActiveStrokeColor[i] = lerpInLinearSpace(p, this.mWorkStrokeColor[i + pos & 3], this.mWorkStrokeColor[i + pos + 3 & 3]);
            }
        }
    }

    static int lerpInLinearSpace(float fraction, int startValue, int endValue) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            float s = (float) (startValue >> (i << 3) & 0xFF) / 255.0F;
            float t = (float) (endValue >> (i << 3) & 0xFF) / 255.0F;
            float v = MathUtil.lerp(s, t, fraction);
            result |= Math.round(v * 255.0F) << (i << 3);
        }
        return result;
    }

    int chooseBorderColor(int corner) {
        return sBorderColorCycle > 0 ? this.mActiveStrokeColor[corner] : this.mWorkStrokeColor[corner];
    }

    void chooseBorderColor(int corner, AbstractUniform uniform) {
        int color = this.chooseBorderColor(corner);
        int a = color >>> 24;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        uniform.set((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F, (float) a / 255.0F);
    }

    public void drawTooltip(@Nonnull ItemStack itemStack, @Nonnull GuiGraphics gr, @Nonnull List<ClientTooltipComponent> list, int mouseX, int mouseY, @Nonnull Font font, int screenWidth, int screenHeight, float partialX, float partialY, @Nullable ClientTooltipPositioner positioner) {
        this.mDraw = true;
        if (itemStack != this.mLastSeenItem) {
            this.mLastSeenItem = itemStack;
            this.computeWorkingColor(itemStack);
        }
        boolean titleGap = false;
        int titleBreakHeight = 0;
        int tooltipWidth;
        int tooltipHeight;
        if (list.size() == 1) {
            ClientTooltipComponent component = (ClientTooltipComponent) list.get(0);
            tooltipWidth = component.getWidth(font);
            tooltipHeight = component.getHeight() - 2;
        } else {
            tooltipWidth = 0;
            tooltipHeight = 0;
            for (int i = 0; i < list.size(); i++) {
                ClientTooltipComponent component = (ClientTooltipComponent) list.get(i);
                tooltipWidth = Math.max(tooltipWidth, component.getWidth(font));
                int componentHeight = component.getHeight();
                tooltipHeight += componentHeight;
                if (i == 0 && !itemStack.isEmpty() && component instanceof ClientTextTooltip) {
                    titleGap = true;
                    titleBreakHeight = componentHeight;
                }
            }
            if (!titleGap) {
                tooltipHeight -= 2;
            }
        }
        float tooltipX;
        float tooltipY;
        float maxScroll;
        if (positioner != null) {
            Vector2ic pos = positioner.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
            tooltipX = (float) pos.x();
            tooltipY = (float) pos.y();
            maxScroll = 0.0F;
        } else {
            if (this.mLayoutRTL) {
                tooltipX = (float) (mouseX + 12) + partialX - 24.0F - (float) tooltipWidth;
                if (tooltipX - partialX < 4.0F) {
                    tooltipX += (float) (24 + tooltipWidth);
                }
            } else {
                tooltipX = (float) (mouseX + 12) + partialX;
                if (tooltipX - partialX + (float) tooltipWidth + 4.0F > (float) screenWidth) {
                    tooltipX -= (float) (28 + tooltipWidth);
                }
            }
            partialX = tooltipX - (float) ((int) tooltipX);
            tooltipY = (float) (mouseY - 12) + partialY;
            if (tooltipY + (float) tooltipHeight + 6.0F > (float) screenHeight) {
                tooltipY = (float) (screenHeight - tooltipHeight - 6);
            }
            if (tooltipY < 6.0F) {
                tooltipY = 6.0F;
            }
            partialY = tooltipY - (float) ((int) tooltipY);
            maxScroll = (float) (6 + tooltipHeight + 6 - screenHeight);
        }
        if (maxScroll > 0.0F) {
            this.mScroll = MathUtil.clamp(this.mScroll, 0.0F, maxScroll);
            if (this.mMarqueeDir != 0 && this.mCurrTimeMillis - this.mMarqueeEndMillis >= 1200L) {
                float t = MathUtil.clamp(0.5F * (float) tooltipWidth / (float) screenWidth, 0.0F, 0.5F);
                float baseMultiplier = (1.5F - t) * 0.01F;
                this.mScroll = this.mScroll + (float) ((long) this.mMarqueeDir * this.mCurrDeltaMillis) * (baseMultiplier + baseMultiplier * Math.min(maxScroll / (float) screenHeight, 1.5F));
                if (this.mMarqueeDir > 0) {
                    if (this.mScroll >= maxScroll) {
                        this.mMarqueeDir = -1;
                        this.mMarqueeEndMillis = this.mCurrTimeMillis;
                    }
                } else if (this.mScroll <= 0.0F) {
                    this.mMarqueeDir = 1;
                    this.mMarqueeEndMillis = this.mCurrTimeMillis;
                }
            }
        } else {
            this.mScroll = 0.0F;
        }
        if (sBorderColorCycle > 0) {
            this.updateBorderColor();
        }
        gr.flush();
        gr.pose().pushPose();
        gr.pose().translate(0.0F, -this.mScroll, 400.0F);
        Matrix4f pose = gr.pose().last().pose();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (sRoundedShapes) {
            this.drawRoundedBackground(gr, pose, tooltipX, tooltipY, tooltipWidth, tooltipHeight, titleGap, titleBreakHeight);
        } else {
            this.drawVanillaBackground(gr, pose, tooltipX, tooltipY, tooltipWidth, tooltipHeight, titleGap, titleBreakHeight);
        }
        int drawX = (int) tooltipX;
        int drawY = (int) tooltipY;
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        MultiBufferSource.BufferSource source = gr.bufferSource();
        gr.pose().translate(partialX, partialY, 0.0F);
        for (int ix = 0; ix < list.size(); ix++) {
            ClientTooltipComponent component = (ClientTooltipComponent) list.get(ix);
            if (titleGap && ix == 0 && sCenterTitle) {
                component.renderText(font, drawX + (tooltipWidth - component.getWidth(font)) / 2, drawY, pose, source);
            } else if (this.mLayoutRTL) {
                component.renderText(font, drawX + tooltipWidth - component.getWidth(font), drawY, pose, source);
            } else {
                component.renderText(font, drawX, drawY, pose, source);
            }
            if (titleGap && ix == 0) {
                drawY += 2;
            }
            drawY += component.getHeight();
        }
        gr.flush();
        drawY = (int) tooltipY;
        for (int ix = 0; ix < list.size(); ix++) {
            ClientTooltipComponent componentx = (ClientTooltipComponent) list.get(ix);
            if (this.mLayoutRTL) {
                componentx.renderImage(font, drawX + tooltipWidth - componentx.getWidth(font), drawY, gr);
            } else {
                componentx.renderImage(font, drawX, drawY, gr);
            }
            if (titleGap && ix == 0) {
                drawY += 2;
            }
            drawY += componentx.getHeight();
        }
        gr.pose().popPose();
    }

    private void drawRoundedBackground(@Nonnull GuiGraphics gr, Matrix4f pose, float tooltipX, float tooltipY, int tooltipWidth, int tooltipHeight, boolean titleGap, int titleBreakHeight) {
        float halfWidth = (float) tooltipWidth / 2.0F;
        float halfHeight = (float) tooltipHeight / 2.0F;
        float centerX = tooltipX + halfWidth;
        float centerY = tooltipY + halfHeight;
        float sizeX = halfWidth + 4.0F;
        float sizeY = halfHeight + 4.0F;
        float shadowRadius = Math.max(sShadowRadius, 1.0E-5F);
        ShaderInstance shader = TooltipRenderType.getShaderTooltip();
        shader.safeGetUniform("u_PushData0").set(sizeX, sizeY, sCornerRadius, sBorderWidth / 2.0F);
        float rainbowOffset = 0.0F;
        if (this.mUseSpectrum) {
            rainbowOffset = 1.0F;
            if (sBorderColorCycle > 0) {
                long overallCycle = (long) sBorderColorCycle * 4L;
                rainbowOffset += (float) (this.mCurrTimeMillis % overallCycle) / (float) overallCycle;
            }
            if (!this.mLayoutRTL) {
                rainbowOffset = -rainbowOffset;
            }
        }
        shader.safeGetUniform("u_PushData1").set(sShadowAlpha, 4.0F / shadowRadius, (float) (sFillColor[0] >>> 24) / 255.0F, rainbowOffset);
        if (rainbowOffset == 0.0F) {
            this.chooseBorderColor(0, shader.safeGetUniform("u_PushData2"));
            this.chooseBorderColor(1, shader.safeGetUniform("u_PushData3"));
            this.chooseBorderColor(3, shader.safeGetUniform("u_PushData4"));
            this.chooseBorderColor(2, shader.safeGetUniform("u_PushData5"));
        }
        VertexConsumer buffer = gr.bufferSource().getBuffer(TooltipRenderType.tooltip());
        RenderSystem.getModelViewStack().pushPose();
        RenderSystem.getModelViewStack().mulPoseMatrix(pose);
        RenderSystem.getModelViewStack().translate(centerX, centerY, 0.0F);
        RenderSystem.applyModelViewMatrix();
        float extent = sBorderWidth / 2.0F + 0.5F + shadowRadius * 1.2F;
        float extentX = sizeX + extent;
        float extentY = sizeY + extent;
        buffer.vertex((double) extentX, (double) extentY, 0.0).endVertex();
        buffer.vertex((double) extentX, (double) (-extentY), 0.0).endVertex();
        buffer.vertex((double) (-extentX), (double) (-extentY), 0.0).endVertex();
        buffer.vertex((double) (-extentX), (double) extentY, 0.0).endVertex();
        gr.flush();
        RenderSystem.getModelViewStack().popPose();
        RenderSystem.applyModelViewMatrix();
        if (titleGap && sTitleBreak) {
            fillGrad(gr, pose, tooltipX, tooltipY + (float) titleBreakHeight - 0.5F, tooltipX + (float) tooltipWidth, tooltipY + (float) titleBreakHeight + 0.5F, -523712312, -523712312, -523712312, -523712312);
        }
    }

    private void drawVanillaBackground(@Nonnull GuiGraphics gr, Matrix4f pose, float tooltipX, float tooltipY, int tooltipWidth, int tooltipHeight, boolean titleGap, int titleBreakHeight) {
        float left = tooltipX - 4.0F;
        float top = tooltipY - 4.0F;
        float right = tooltipX + (float) tooltipWidth + 4.0F;
        float bottom = tooltipY + (float) tooltipHeight + 4.0F;
        fillGrad(gr, pose, left, top - 1.0F, right, top, sFillColor[0], sFillColor[1], sFillColor[1], sFillColor[0]);
        fillGrad(gr, pose, left, bottom, right, bottom + 1.0F, sFillColor[3], sFillColor[2], sFillColor[2], sFillColor[3]);
        fillGrad(gr, pose, left, top, right, bottom, sFillColor[0], sFillColor[1], sFillColor[2], sFillColor[3]);
        fillGrad(gr, pose, left - 1.0F, top, left, bottom, sFillColor[0], sFillColor[0], sFillColor[3], sFillColor[3]);
        fillGrad(gr, pose, right, top, right + 1.0F, bottom, sFillColor[1], sFillColor[1], sFillColor[2], sFillColor[2]);
        if (titleGap && sTitleBreak) {
            fillGrad(gr, pose, tooltipX, tooltipY + (float) titleBreakHeight - 0.5F, tooltipX + (float) tooltipWidth, tooltipY + (float) titleBreakHeight + 0.5F, -523712312, -523712312, -523712312, -523712312);
        }
        fillGrad(gr, pose, left, top, right, top + 1.0F, this.chooseBorderColor(0), this.chooseBorderColor(1), this.chooseBorderColor(1), this.chooseBorderColor(0));
        fillGrad(gr, pose, right - 1.0F, top, right, bottom, this.chooseBorderColor(1), this.chooseBorderColor(1), this.chooseBorderColor(2), this.chooseBorderColor(2));
        fillGrad(gr, pose, left, bottom - 1.0F, right, bottom, this.chooseBorderColor(3), this.chooseBorderColor(2), this.chooseBorderColor(2), this.chooseBorderColor(3));
        fillGrad(gr, pose, left, top, left + 1.0F, bottom, this.chooseBorderColor(0), this.chooseBorderColor(0), this.chooseBorderColor(3), this.chooseBorderColor(3));
    }

    private static void fillGrad(GuiGraphics gr, Matrix4f pose, float left, float top, float right, float bottom, int colorUL, int colorUR, int colorLR, int colorLL) {
        VertexConsumer buffer = gr.bufferSource().getBuffer(RenderType.gui());
        int a = colorLR >>> 24;
        int r = colorLR >> 16 & 0xFF;
        int g = colorLR >> 8 & 0xFF;
        int b = colorLR & 0xFF;
        buffer.vertex(pose, right, bottom, 0.0F).color(r, g, b, a).endVertex();
        a = colorUR >>> 24;
        r = colorUR >> 16 & 0xFF;
        g = colorUR >> 8 & 0xFF;
        b = colorUR & 0xFF;
        buffer.vertex(pose, right, top, 0.0F).color(r, g, b, a).endVertex();
        a = colorUL >>> 24;
        r = colorUL >> 16 & 0xFF;
        g = colorUL >> 8 & 0xFF;
        b = colorUL & 0xFF;
        buffer.vertex(pose, left, top, 0.0F).color(r, g, b, a).endVertex();
        a = colorLL >>> 24;
        r = colorLL >> 16 & 0xFF;
        g = colorLL >> 8 & 0xFF;
        b = colorLL & 0xFF;
        buffer.vertex(pose, left, bottom, 0.0F).color(r, g, b, a).endVertex();
        gr.flush();
    }
}