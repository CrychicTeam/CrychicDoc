package snownee.jade.impl.ui;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import org.joml.Vector3f;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IProgressStyle;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.overlay.OverlayRenderer;
import snownee.jade.util.Color;

public class ProgressStyle implements IProgressStyle {

    public boolean autoTextColor = true;

    public int color;

    public int color2;

    public int textColor;

    public boolean vertical;

    public IElement overlay;

    @Deprecated
    public boolean glowText;

    public boolean shadow = true;

    public ProgressStyle() {
        this.color(-1);
    }

    private static Vector3f RGBtoHSV(int rgb) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        float v = (float) max;
        float delta = (float) (max - min);
        if (max != 0) {
            float s = delta / (float) max;
            float h;
            if (r == max) {
                h = (float) (g - b) / delta;
            } else if (g == max) {
                h = 2.0F + (float) (b - r) / delta;
            } else {
                h = 4.0F + (float) (r - g) / delta;
            }
            h /= 6.0F;
            if (h < 0.0F) {
                h++;
            }
            return new Vector3f(h, s, v / 255.0F);
        } else {
            float sx = 0.0F;
            float hx = -1.0F;
            return new Vector3f(hx, sx, 0.0F);
        }
    }

    @Override
    public IProgressStyle color(int color, int color2) {
        this.color = color;
        this.color2 = color2;
        return this;
    }

    @Override
    public IProgressStyle vertical(boolean vertical) {
        this.vertical = vertical;
        return this;
    }

    @Override
    public IProgressStyle overlay(IElement overlay) {
        this.overlay = overlay;
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float width, float height, float progress, Component text) {
        progress *= this.choose(true, width, height);
        float progressY = y;
        if (this.vertical) {
            progressY = y + (height - progress);
        }
        if (progress > 0.0F) {
            if (this.overlay != null) {
                Vec2 size = new Vec2(this.choose(true, progress, width), this.choose(false, progress, height));
                this.overlay.size(size);
                this.overlay.render(guiGraphics, x, progressY, size.x, size.y);
            } else {
                Color color3 = Color.rgb(this.color);
                int lighter = Color.hsl(color3.getHue(), color3.getSaturation(), color3.getLightness() * 0.7F, color3.getOpacity()).toInt();
                float half = this.choose(true, height, width) / 2.0F;
                DisplayHelper.INSTANCE.drawGradientRect(guiGraphics, x, progressY, this.choose(true, progress, half), this.choose(false, progress, half), lighter, this.color, this.vertical);
                DisplayHelper.INSTANCE.drawGradientRect(guiGraphics, x + this.choose(false, half, 0.0F), progressY + this.choose(true, half, 0.0F), this.choose(true, progress, half), this.choose(false, progress, half), this.color, lighter, this.vertical);
                if (this.color != this.color2) {
                    if (this.vertical) {
                        for (float yy = y + height; yy > progressY; yy -= 2.0F) {
                            float fy = Math.max(progressY, yy + 1.0F);
                            DisplayHelper.fill(guiGraphics, x, yy, x + width, fy, this.color2);
                        }
                    } else {
                        for (float xx = x + 1.0F; xx < x + progress; xx += 2.0F) {
                            float fx = Math.min(x + width, xx + 1.0F);
                            DisplayHelper.fill(guiGraphics, xx, y, fx, y + height, this.color2);
                        }
                    }
                }
            }
        }
        if (text != null) {
            Font font = Minecraft.getInstance().font;
            if (this.autoTextColor) {
                this.autoTextColor = false;
                if (this.overlay == null && RGBtoHSV(this.color2).z() > 0.75F) {
                    this.textColor = -16777216;
                } else {
                    this.textColor = -1;
                }
            }
            y += height - 9.0F;
            if (this.vertical && 9.0F < progress) {
                y -= progress;
                y += (float) (9 + 2);
            }
            int color = IWailaConfig.IConfigOverlay.applyAlpha(this.textColor, OverlayRenderer.alpha);
            if (this.glowText) {
                MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                font.drawInBatch8xOutline(text.getVisualOrderText(), x + 1.0F, y, -1, -13421773, guiGraphics.pose().last().pose(), multibuffersource$buffersource, 15728880);
                multibuffersource$buffersource.endBatch();
            } else {
                DisplayHelper.setBetterTextShadow(true);
                guiGraphics.drawString(font, text, (int) x + 1, (int) y, color, this.shadow);
                DisplayHelper.setBetterTextShadow(false);
            }
        }
    }

    private float choose(boolean expand, float x, float y) {
        return this.vertical ^ expand ? x : y;
    }

    @Override
    public IProgressStyle textColor(int color) {
        this.textColor = color;
        this.autoTextColor = false;
        return this;
    }

    public IProgressStyle glowText(boolean glowText) {
        this.glowText = glowText;
        return this;
    }
}