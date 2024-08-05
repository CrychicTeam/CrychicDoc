package se.mickelus.tetra.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class VerticalTabIconButtonGui extends VerticalTabButtonGui {

    private final GuiTexture icon;

    private final AnimationChain iconFlash;

    public VerticalTabIconButtonGui(int x, int y, ResourceLocation texture, int textureX, int textureY, String label, String keybinding, Runnable onClickHandler, boolean initiallyActive) {
        super(x, y, label, keybinding, onClickHandler, initiallyActive);
        this.icon = new GuiTexture(-3, 3, 10, 9, textureX, textureY, texture);
        this.icon.setAttachment(GuiAttachment.topRight);
        this.addChild(this.icon);
        this.iconFlash = new AnimationChain(new KeyframeAnimation(40, this.icon).applyTo(new Applier.TranslateX(-4.0F)), new KeyframeAnimation(60, this.icon).applyTo(new Applier.TranslateX(-3.0F)));
        this.label.setX(-16);
        this.labelShow = new KeyframeAnimation(100, this.label).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(-16.0F));
        this.labelHide = new KeyframeAnimation(150, this.label).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(-13.0F));
        this.width = this.label.getWidth() + 20;
        this.updateStyling();
    }

    @Override
    protected void updateStyling() {
        super.updateStyling();
        if (this.icon != null) {
            if (this.isActive) {
                this.icon.setColor(this.hasFocus() ? 16777164 : 16777215);
            } else if (this.hasContent) {
                this.icon.setColor(this.hasFocus() ? 9408367 : 8355711);
            } else {
                this.icon.setColor(this.hasFocus() ? 9408367 : 8355711);
            }
        }
    }

    @Override
    public void setActive(boolean isActive) {
        super.setActive(isActive);
        if (isActive) {
            this.iconFlash.stop();
            this.iconFlash.start();
        }
    }
}