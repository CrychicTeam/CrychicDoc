package se.mickelus.tetra.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class VerticalTabButtonGui extends GuiClickable {

    protected boolean hasContent = false;

    protected boolean isActive;

    protected GuiRect indicator;

    protected GuiString label;

    protected GuiKeybinding keybinding;

    protected AnimationChain indicatorFlash;

    protected KeyframeAnimation labelShow;

    protected KeyframeAnimation labelHide;

    protected KeyframeAnimation keybindShow;

    protected KeyframeAnimation keybindHide;

    public VerticalTabButtonGui(int x, int y, String label, String keybinding, Runnable onClickHandler, boolean initiallyActive) {
        super(x, y, 0, 15, onClickHandler);
        this.setAttachmentPoint(GuiAttachment.topRight);
        this.indicator = new GuiRect(0, 0, 1, 15, 16777215);
        this.indicator.setAttachment(GuiAttachment.topRight);
        this.addChild(this.indicator);
        this.indicatorFlash = new AnimationChain(new KeyframeAnimation(40, this.indicator).applyTo(new Applier.TranslateX(-3.0F)), new KeyframeAnimation(60, this.indicator).applyTo(new Applier.TranslateX(0.0F)));
        this.label = new GuiString(-5, 4, label);
        this.label.setAttachment(GuiAttachment.topRight);
        this.label.setOpacity(0.0F);
        this.addChild(this.label);
        this.labelShow = new KeyframeAnimation(100, this.label).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(-5.0F));
        this.labelHide = new KeyframeAnimation(150, this.label).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(-2.0F));
        this.keybinding = new GuiKeybinding(0, 2, keybinding);
        this.keybinding.setAttachmentPoint(GuiAttachment.topRight);
        this.keybinding.setOpacity(0.0F);
        this.keybindShow = new KeyframeAnimation(100, this.keybinding).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(0.0F));
        this.keybindHide = new KeyframeAnimation(150, this.keybinding).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(3.0F));
        if (keybinding != null) {
            this.addChild(this.keybinding);
        }
        this.width = this.label.getWidth() + 10;
        this.isActive = initiallyActive;
        this.updateStyling();
    }

    protected void updateStyling() {
        if (this.isActive) {
            this.indicator.setOpacity(1.0F);
        } else if (this.hasContent) {
            this.indicator.setOpacity(0.5F);
        } else {
            this.indicator.setOpacity(0.25F);
        }
        this.indicator.setColor(this.hasFocus() ? 16777164 : 16777215);
    }

    public void setHasContent(boolean hasContent) {
        this.hasContent = hasContent;
        this.updateStyling();
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        this.updateStyling();
        if (isActive) {
            this.indicatorFlash.stop();
            this.indicatorFlash.start();
        }
    }

    @Override
    protected void onFocus() {
        this.updateStyling();
        this.labelHide.stop();
        this.keybindHide.stop();
        this.labelShow.stop();
        this.keybindShow.stop();
        this.labelShow.start();
        this.keybindShow.start();
    }

    @Override
    protected void onBlur() {
        this.updateStyling();
        this.labelShow.stop();
        this.keybindShow.stop();
        this.labelHide.stop();
        this.keybindHide.stop();
        this.labelHide.start();
        this.keybindHide.start();
    }
}