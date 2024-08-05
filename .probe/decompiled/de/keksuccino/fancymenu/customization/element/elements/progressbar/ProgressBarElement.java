package de.keksuccino.fancymenu.customization.element.elements.progressbar;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.placeholder.PlaceholderParser;
import de.keksuccino.fancymenu.util.enums.LocalizedCycleEnum;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.texture.ITexture;
import de.keksuccino.konkrete.math.MathUtils;
import java.awt.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProgressBarElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    public ProgressBarElement.BarDirection direction = ProgressBarElement.BarDirection.RIGHT;

    public DrawableColor barColor = DrawableColor.of(new Color(82, 149, 255));

    @Nullable
    public ResourceSupplier<ITexture> barTextureSupplier;

    public DrawableColor backgroundColor = DrawableColor.of(new Color(171, 200, 247));

    @Nullable
    public ResourceSupplier<ITexture> backgroundTextureSupplier;

    public boolean useProgressForElementAnchor = false;

    public String progressSource = null;

    public ProgressBarElement.ProgressValueMode progressValueMode = ProgressBarElement.ProgressValueMode.PERCENTAGE;

    protected int lastProgressX = 0;

    protected int lastProgressY = 0;

    protected int lastProgressWidth = 0;

    protected int lastProgressHeight = 0;

    protected float renderProgress = 0.0F;

    public ProgressBarElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRender()) {
            this.renderBackground(graphics);
            this.renderProgress(graphics);
        }
    }

    protected void renderProgress(@NotNull GuiGraphics graphics) {
        float actualProgress = Math.max(0.0F, Math.min(1.0F, this.getCurrentProgress()));
        this.renderProgress = Mth.clamp(this.renderProgress * 0.95F + actualProgress * 0.050000012F, 0.0F, 1.0F);
        int progressWidth = this.getAbsoluteWidth();
        int progressHeight = this.getAbsoluteHeight();
        int progressX = this.getAbsoluteX();
        int progressY = this.getAbsoluteY();
        float offsetX = 0.0F;
        float offsetY = 0.0F;
        Mth.lerp(1.0F, 1.0F, 1.0F);
        if (this.direction == ProgressBarElement.BarDirection.LEFT || this.direction == ProgressBarElement.BarDirection.RIGHT) {
            progressWidth = (int) ((float) this.getAbsoluteWidth() * this.renderProgress);
        }
        if (this.direction == ProgressBarElement.BarDirection.UP || this.direction == ProgressBarElement.BarDirection.DOWN) {
            progressHeight = (int) ((float) this.getAbsoluteHeight() * this.renderProgress);
        }
        if (this.direction == ProgressBarElement.BarDirection.LEFT) {
            progressX += this.getAbsoluteWidth() - progressWidth;
            offsetX = (float) (this.getAbsoluteWidth() - progressWidth);
        }
        if (this.direction == ProgressBarElement.BarDirection.UP) {
            progressY += this.getAbsoluteHeight() - progressHeight;
            offsetY = (float) (this.getAbsoluteHeight() - progressHeight);
        }
        this.lastProgressX = progressX;
        this.lastProgressY = progressY;
        this.lastProgressWidth = progressWidth;
        this.lastProgressHeight = progressHeight;
        RenderSystem.enableBlend();
        if (this.barTextureSupplier != null) {
            ITexture t = this.barTextureSupplier.get();
            if (t != null) {
                ResourceLocation loc = t.getResourceLocation();
                if (loc != null) {
                    graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
                    graphics.blit(loc, progressX, progressY, offsetX, offsetY, progressWidth, progressHeight, this.getAbsoluteWidth(), this.getAbsoluteHeight());
                    RenderingUtils.resetShaderColor(graphics);
                }
            }
        } else if (this.barColor != null) {
            RenderingUtils.resetShaderColor(graphics);
            graphics.fill(progressX, progressY, progressX + progressWidth, progressY + progressHeight, RenderingUtils.replaceAlphaInColor(this.barColor.getColorInt(), this.opacity));
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected void renderBackground(@NotNull GuiGraphics graphics) {
        RenderSystem.enableBlend();
        if (this.backgroundTextureSupplier != null) {
            this.backgroundTextureSupplier.forRenderable((iTexture, location) -> {
                graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
                graphics.blit(location, this.getAbsoluteX(), this.getAbsoluteY(), 0.0F, 0.0F, this.getAbsoluteWidth(), this.getAbsoluteHeight(), this.getAbsoluteWidth(), this.getAbsoluteHeight());
                RenderingUtils.resetShaderColor(graphics);
            });
        } else if (this.backgroundColor != null) {
            RenderingUtils.resetShaderColor(graphics);
            graphics.fill(this.getAbsoluteX(), this.getAbsoluteY(), this.getAbsoluteX() + this.getAbsoluteWidth(), this.getAbsoluteY() + this.getAbsoluteHeight(), RenderingUtils.replaceAlphaInColor(this.backgroundColor.getColorInt(), this.opacity));
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    public float getCurrentProgress() {
        if (isEditor()) {
            return 0.5F;
        } else {
            if (this.progressSource != null) {
                String s = StringUtils.replace(PlaceholderParser.replacePlaceholders(this.progressSource), " ", "");
                if (MathUtils.isFloat(s)) {
                    if (this.progressValueMode == ProgressBarElement.ProgressValueMode.PERCENTAGE) {
                        return Float.parseFloat(s) / 100.0F;
                    }
                    return Float.parseFloat(s);
                }
            }
            return 0.0F;
        }
    }

    @Override
    public int getChildElementAnchorPointX() {
        if (this.useProgressForElementAnchor) {
            return this.direction == ProgressBarElement.BarDirection.RIGHT ? this.getProgressX() + this.getProgressWidth() : this.getProgressX();
        } else {
            return super.getChildElementAnchorPointX();
        }
    }

    @Override
    public int getChildElementAnchorPointY() {
        if (this.useProgressForElementAnchor) {
            return this.direction == ProgressBarElement.BarDirection.DOWN ? this.getProgressY() + this.getProgressHeight() : this.getProgressY();
        } else {
            return super.getChildElementAnchorPointY();
        }
    }

    public int getProgressX() {
        return this.lastProgressX;
    }

    public int getProgressY() {
        return this.lastProgressY;
    }

    public int getProgressWidth() {
        return this.lastProgressWidth;
    }

    public int getProgressHeight() {
        return this.lastProgressHeight;
    }

    public static enum BarDirection implements LocalizedCycleEnum<ProgressBarElement.BarDirection> {

        LEFT("left"), RIGHT("right"), UP("up"), DOWN("down");

        private final String name;

        private BarDirection(String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.editor.elements.progress_bar.direction";
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) WARNING_TEXT_STYLE.get();
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public ProgressBarElement.BarDirection[] getValues() {
            return values();
        }

        @Nullable
        public ProgressBarElement.BarDirection getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static ProgressBarElement.BarDirection getByName(@NotNull String name) {
            for (ProgressBarElement.BarDirection d : values()) {
                if (d.name.equals(name)) {
                    return d;
                }
            }
            return null;
        }
    }

    public static enum ProgressValueMode implements LocalizedCycleEnum<ProgressBarElement.ProgressValueMode> {

        PERCENTAGE("percentage"), FLOATING_POINT("float");

        private final String name;

        private ProgressValueMode(String name) {
            this.name = name;
        }

        @NotNull
        @Override
        public String getLocalizationKeyBase() {
            return "fancymenu.editor.elements.progress_bar.mode";
        }

        @NotNull
        @Override
        public Style getValueComponentStyle() {
            return (Style) WARNING_TEXT_STYLE.get();
        }

        @NotNull
        @Override
        public String getName() {
            return this.name;
        }

        @NotNull
        public ProgressBarElement.ProgressValueMode[] getValues() {
            return values();
        }

        @Nullable
        public ProgressBarElement.ProgressValueMode getByNameInternal(@NotNull String name) {
            return getByName(name);
        }

        @Nullable
        public static ProgressBarElement.ProgressValueMode getByName(@NotNull String name) {
            for (ProgressBarElement.ProgressValueMode d : values()) {
                if (d.name.equals(name)) {
                    return d;
                }
            }
            return null;
        }
    }
}