package se.mickelus.tetra.items.modular.impl;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class GuiBlockProgress extends GuiRoot {

    private static final int width = 16;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final GuiElement container = new GuiElement(-1, 20, 16, 2).setAttachment(GuiAttachment.middleCenter).setOpacity(0.0F);

    private final GuiRect bar;

    public GuiBlockProgress(Minecraft mc) {
        super(mc);
        this.addChild(this.container);
        this.container.addChild(new GuiRect(0, 0, 16, 2, 16777215).setOpacity(0.2F));
        this.bar = new GuiRect(0, 0, 0, 2, 16777215);
        this.bar.setOpacity(0.6F);
        this.container.addChild(this.bar);
        this.showAnimation = new KeyframeAnimation(60, this.container).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(0.0F));
        this.hideAnimation = new KeyframeAnimation(100, this.container).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(-2.0F)).withDelay(1000);
    }

    public void setProgress(float progress) {
        if (progress > 0.0F) {
            this.bar.setWidth(Mth.clamp((int) (progress * 16.0F), 0, 16));
            if (!this.showAnimation.isActive() && this.container.getOpacity() < 1.0F) {
                this.showAnimation.start();
            }
            this.hideAnimation.stop();
        } else {
            if (!this.hideAnimation.isActive() && this.container.getOpacity() > 0.0F) {
                this.hideAnimation.start();
            }
            this.showAnimation.stop();
        }
    }

    @Override
    public void draw(GuiGraphics graphics) {
        if (this.isVisible()) {
            Window window = this.mc.getWindow();
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            int mouseX = (int) (this.mc.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
            int mouseY = (int) (this.mc.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
            this.drawChildren(graphics, width / 2, height / 2, 0, 0, mouseX, mouseY, 1.0F);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}