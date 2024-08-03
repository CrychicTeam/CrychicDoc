package de.keksuccino.fancymenu.customization.background.backgrounds.image;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.background.MenuBackground;
import de.keksuccino.fancymenu.customization.background.MenuBackgroundBuilder;
import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ImageMenuBackground extends MenuBackground {

    private static final DrawableColor BACKGROUND_COLOR = DrawableColor.BLACK;

    public ResourceSupplier<ITexture> textureSupplier;

    public ResourceSupplier<ITexture> fallbackTextureSupplier;

    public boolean slideLeftRight = false;

    public boolean repeat = false;

    protected double slidePos = 0.0;

    protected boolean slideMoveBack = false;

    protected boolean slideStop = false;

    protected int slideTick = 0;

    public ImageMenuBackground(MenuBackgroundBuilder<ImageMenuBackground> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        graphics.fill(0, 0, getScreenWidth(), getScreenHeight(), BACKGROUND_COLOR.getColorIntWithAlpha(this.opacity));
        RenderingUtils.resetShaderColor(graphics);
        ResourceLocation resourceLocation = null;
        ITexture tex = null;
        AspectRatio ratio = new AspectRatio(10, 10);
        if (this.textureSupplier != null) {
            ITexture background = this.textureSupplier.get();
            if (background != null) {
                tex = background;
                ratio = background.getAspectRatio();
                resourceLocation = background.getResourceLocation();
            }
        }
        if (resourceLocation == null && this.fallbackTextureSupplier != null) {
            ITexture fallback = this.fallbackTextureSupplier.get();
            if (fallback != null) {
                tex = fallback;
                ratio = fallback.getAspectRatio();
                resourceLocation = fallback.getResourceLocation();
            }
        }
        if (resourceLocation != null) {
            RenderSystem.enableBlend();
            graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
            if (this.repeat) {
                RenderingUtils.blitRepeat(graphics, resourceLocation, 0, 0, getScreenWidth(), getScreenHeight(), tex.getWidth(), tex.getHeight());
            } else if (this.slideLeftRight) {
                int w = ratio.getAspectRatioWidth(getScreenHeight());
                if (this.slidePos + (double) (w - getScreenWidth()) <= 0.0) {
                    this.slideMoveBack = true;
                }
                if (this.slidePos >= 0.0) {
                    this.slideMoveBack = false;
                }
                if (this.slidePos + (double) (w - getScreenWidth()) < 0.0) {
                    this.slidePos = (double) (-(w - getScreenWidth()));
                }
                if (this.slidePos > 0.0) {
                    this.slidePos = 0.0;
                }
                if (!this.slideStop) {
                    if (this.slideTick >= 1) {
                        this.slideTick = 0;
                        if (this.slideMoveBack) {
                            this.slidePos += 0.5;
                        } else {
                            this.slidePos -= 0.5;
                        }
                        if (this.slidePos + (double) (w - getScreenWidth()) == 0.0) {
                            this.slideStop = true;
                        }
                        if (this.slidePos == 0.0) {
                            this.slideStop = true;
                        }
                    } else {
                        this.slideTick++;
                    }
                } else if (this.slideTick >= 300) {
                    this.slideStop = false;
                    this.slideTick = 0;
                } else {
                    this.slideTick++;
                }
                if (w <= getScreenWidth()) {
                    if (this.keepBackgroundAspectRatio) {
                        this.renderKeepAspectRatio(graphics, ratio, resourceLocation);
                    } else {
                        graphics.blit(resourceLocation, 0, 0, 0.0F, 0.0F, getScreenWidth(), getScreenHeight(), getScreenWidth(), getScreenHeight());
                    }
                } else {
                    RenderingUtils.blitF(graphics, resourceLocation, (float) this.slidePos, 0.0F, 0.0F, 0.0F, (float) w, (float) getScreenHeight(), (float) w, (float) getScreenHeight());
                }
            } else if (this.keepBackgroundAspectRatio) {
                this.renderKeepAspectRatio(graphics, ratio, resourceLocation);
            } else {
                graphics.blit(resourceLocation, 0, 0, 0.0F, 0.0F, getScreenWidth(), getScreenHeight(), getScreenWidth(), getScreenHeight());
            }
        }
        RenderingUtils.resetShaderColor(graphics);
    }

    protected void renderKeepAspectRatio(@NotNull GuiGraphics graphics, @NotNull AspectRatio ratio, @NotNull ResourceLocation resourceLocation) {
        int[] size = ratio.getAspectRatioSizeByMinimumSize(getScreenWidth(), getScreenHeight());
        int x = 0;
        if (size[0] > getScreenWidth()) {
            x = -((size[0] - getScreenWidth()) / 2);
        }
        int y = 0;
        if (size[1] > getScreenHeight()) {
            y = -((size[1] - getScreenHeight()) / 2);
        }
        graphics.blit(resourceLocation, x, y, 0.0F, 0.0F, size[0], size[1], size[0], size[1]);
    }
}