package de.keksuccino.fancymenu.customization.deep.layers.titlescreen.splash;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import de.keksuccino.fancymenu.customization.deep.AbstractDeepElement;
import de.keksuccino.fancymenu.customization.deep.DeepElementBuilder;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinSplashRenderer;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class TitleScreenSplashDeepElement extends AbstractDeepElement {

    private static final DrawableColor DEFAULT_COLOR = DrawableColor.of(new Color(255, 255, 0));

    public static String cachedSplashText;

    public TitleScreenSplashDeepElement(DeepElementBuilder<?, ?, ?> builder) {
        super(builder);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.shouldRender()) {
            this.baseWidth = 100;
            this.baseHeight = 30;
            RenderSystem.enableBlend();
            this.renderSplash(graphics, Minecraft.getInstance().font);
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    protected void renderSplash(GuiGraphics graphics, Font font) {
        if (cachedSplashText == null) {
            SplashRenderer splashRenderer = Minecraft.getInstance().getSplashManager().getSplash();
            if (splashRenderer == null) {
                return;
            }
            cachedSplashText = ((IMixinSplashRenderer) splashRenderer).getSplashFancyMenu();
        }
        if (cachedSplashText == null) {
            cachedSplashText = "§c< ERROR! UNABLE TO GET SPLASH TEXT! >";
        }
        graphics.pose().pushPose();
        graphics.pose().translate((float) (this.getAbsoluteX() + 50), (float) (this.getAbsoluteY() + 15), 0.0F);
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(-20.0F));
        float f = 1.8F - Mth.abs(Mth.sin((float) (System.currentTimeMillis() % 1000L) / 1000.0F * (float) (Math.PI * 2)) * 0.1F);
        f = f * 100.0F / (float) (font.width(cachedSplashText) + 32);
        graphics.pose().scale(f, f, f);
        graphics.setColor(1.0F, 1.0F, 1.0F, this.opacity);
        graphics.drawCenteredString(font, Component.literal(cachedSplashText), 0, -8, DEFAULT_COLOR.getColorIntWithAlpha(this.opacity));
        graphics.pose().popPose();
    }

    @Override
    public int getAbsoluteX() {
        return getScreenWidth() / 2 + 90 - 50;
    }

    @Override
    public int getAbsoluteY() {
        return 55;
    }

    @Override
    public int getAbsoluteWidth() {
        return 100;
    }

    @Override
    public int getAbsoluteHeight() {
        return 30;
    }
}