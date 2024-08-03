package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import java.util.Arrays;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiVerticalLayoutGroup;
import se.mickelus.tetra.gui.GuiTextures;

public class HolosphereActionGui extends GuiElement {

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation focusLabel;

    private final KeyframeAnimation blurLabel;

    GuiTexture backdrop;

    GuiTexture icon;

    Runnable performRunnable;

    public HolosphereActionGui(int x, int y, int u, int v, String label, Runnable perform) {
        super(x, y, 23, 23);
        this.performRunnable = perform;
        this.backdrop = new GuiTexture(0, 0, 23, 23, 54, 28, GuiTextures.toolbelt);
        this.addChild(this.backdrop);
        this.icon = new GuiTexture(0, 0, 23, 23, u, v, GuiTextures.toolbelt);
        this.addChild(this.icon);
        GuiElement labelContainer = new GuiVerticalLayoutGroup(2, 1, 0, -1);
        Arrays.stream(label.split("\n")).map(l -> new GuiStringOutline(0, 0, l).setAttachment(GuiAttachment.topCenter)).forEach(labelContainer::addChild);
        labelContainer.setAttachment(GuiAttachment.middleCenter);
        labelContainer.setOpacity(0.0F);
        this.addChild(labelContainer);
        this.isVisible = false;
        this.showAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.TranslateY((float) (y + 2), (float) y), new Applier.Opacity(0.0F, 1.0F)).withDelay((int) (Math.random() * 300.0));
        this.focusLabel = new KeyframeAnimation(80, labelContainer).applyTo(new Applier.TranslateY(-2.0F, 0.0F), new Applier.Opacity(1.0F));
        this.blurLabel = new KeyframeAnimation(120, labelContainer).applyTo(new Applier.TranslateY(0.0F, 2.0F), new Applier.Opacity(0.0F));
    }

    public void perform() {
        this.performRunnable.run();
    }

    @Override
    protected void onShow() {
        this.showAnimation.start();
    }

    @Override
    protected boolean onHide() {
        if (this.showAnimation.isActive()) {
            this.showAnimation.stop();
        }
        return true;
    }

    @Override
    protected void onFocus() {
        this.backdrop.setColor(16777164);
        this.icon.setColor(8355711);
        this.blurLabel.stop();
        this.focusLabel.start();
    }

    @Override
    protected void onBlur() {
        this.backdrop.setColor(16777215);
        this.icon.setColor(16777215);
        this.focusLabel.stop();
        this.blurLabel.start();
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        float ox = (float) (mouseX - refX - this.x) - (float) this.width / 2.0F;
        float oy = (float) (mouseY - refY - this.y) - (float) this.height / 2.0F;
        boolean gainFocus = Math.abs(ox + oy) <= (float) this.width / 2.0F + 1.0F && Math.abs(ox - oy) <= (float) this.width / 2.0F + 1.0F;
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }
}