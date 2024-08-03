package de.keksuccino.fancymenu.customization.element.elements.splash;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinSplashRenderer;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import de.keksuccino.konkrete.math.MathUtils;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SplashTextElement extends AbstractElement {

    private static final Logger LOGGER = LogManager.getLogger();

    public SplashTextElement.SourceMode sourceMode = SplashTextElement.SourceMode.DIRECT_TEXT;

    public String source = "Splash Text";

    @Nullable
    public ResourceSupplier<IText> textFileSupplier;

    public float scale = 1.0F;

    public boolean shadow = true;

    public boolean bounce = true;

    public float rotation = 20.0F;

    public DrawableColor baseColor = DrawableColor.of(new Color(255, 255, 0));

    public boolean refreshOnMenuReload = false;

    public Font font;

    protected float baseScale;

    protected String renderText;

    protected String lastSource;

    protected SplashTextElement.SourceMode lastSourceMode;

    protected boolean refreshedOnMenuLoad;

    public SplashTextElement(@NotNull ElementBuilder<?, ?> builder) {
        super(builder);
        this.font = Minecraft.getInstance().font;
        this.baseScale = 1.8F;
        this.renderText = null;
        this.lastSource = null;
        this.lastSourceMode = null;
        this.refreshedOnMenuLoad = false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRender()) {
            this.updateSplash();
            this.renderSplash(graphics);
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    public void refresh() {
        this.getBuilder().splashCache.remove(this.getInstanceIdentifier());
        this.renderText = null;
    }

    protected void updateSplash() {
        if (isEditor()) {
            if (!Objects.equals(this.lastSource, this.source) || !Objects.equals(this.lastSourceMode, this.sourceMode)) {
                this.refresh();
            }
            this.lastSource = this.source;
            this.lastSourceMode = this.sourceMode;
        }
        if (this.sourceMode == SplashTextElement.SourceMode.VANILLA || this.source != null) {
            if (this.getBuilder().isNewMenu && this.refreshOnMenuReload && !this.refreshedOnMenuLoad) {
                this.refresh();
                this.refreshedOnMenuLoad = true;
            }
            if (this.renderText == null && this.getBuilder().splashCache.containsKey(this.getInstanceIdentifier())) {
                this.renderText = ((SplashTextElement) this.getBuilder().splashCache.get(this.getInstanceIdentifier())).renderText;
            }
            if (this.renderText == null) {
                if (this.sourceMode == SplashTextElement.SourceMode.VANILLA) {
                    SplashRenderer splashRenderer = Minecraft.getInstance().getSplashManager().getSplash();
                    this.renderText = splashRenderer != null ? ((IMixinSplashRenderer) splashRenderer).getSplashFancyMenu() : "";
                }
                if (this.sourceMode == SplashTextElement.SourceMode.TEXT_FILE && this.textFileSupplier != null) {
                    IText text = this.textFileSupplier.get();
                    if (text != null) {
                        List<String> l = text.getTextLines();
                        if (l != null) {
                            if (l.isEmpty() || l.size() <= 1 && ((String) l.get(0)).replace(" ", "").length() <= 0) {
                                this.renderText = "§cERROR: SPLASH FILE IS EMPTY";
                            } else {
                                int i = MathUtils.getRandomNumberInRange(0, l.size() - 1);
                                this.renderText = (String) l.get(i);
                            }
                        }
                    }
                }
                if (this.sourceMode == SplashTextElement.SourceMode.DIRECT_TEXT) {
                    this.renderText = this.source;
                }
            }
            this.getBuilder().splashCache.put(this.getInstanceIdentifier(), this);
        }
    }

    protected void renderSplash(GuiGraphics graphics) {
        if (this.renderText == null) {
            if (!isEditor()) {
                return;
            }
            this.renderText = "< empty splash element >";
        }
        Component renderTextComponent = buildComponent(this.renderText);
        float splashBaseScale = this.baseScale;
        if (this.bounce) {
            splashBaseScale -= Mth.abs(Mth.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
        }
        splashBaseScale = splashBaseScale * 100.0F / (float) (this.font.width(renderTextComponent) + 32);
        RenderSystem.enableBlend();
        graphics.pose().pushPose();
        graphics.pose().scale(this.scale, this.scale, this.scale);
        graphics.pose().pushPose();
        graphics.pose().translate(((float) this.getAbsoluteX() + (float) this.getAbsoluteWidth() / 2.0F) / this.scale, (float) this.getAbsoluteY() / this.scale, 0.0F);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(this.rotation));
        graphics.pose().scale(splashBaseScale, splashBaseScale, splashBaseScale);
        int alpha = this.baseColor.getColor().getAlpha();
        int i = Mth.ceil(this.opacity * 255.0F);
        if (i < alpha) {
            alpha = i;
        }
        graphics.drawString(this.font, renderTextComponent, -(this.font.width(renderTextComponent) / 2), 0, RenderingUtils.replaceAlphaInColor(this.baseColor.getColorInt(), alpha), this.shadow);
        graphics.pose().popPose();
        graphics.pose().popPose();
    }

    protected SplashTextElementBuilder getBuilder() {
        return (SplashTextElementBuilder) this.builder;
    }

    public static enum SourceMode {

        DIRECT_TEXT("direct"), TEXT_FILE("text_file"), VANILLA("vanilla");

        final String name;

        private SourceMode(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static SplashTextElement.SourceMode getByName(String name) {
            for (SplashTextElement.SourceMode i : values()) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
            return null;
        }
    }
}