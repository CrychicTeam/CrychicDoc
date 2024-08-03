package se.mickelus.tetra.items.modular.impl.holo.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class HoloRootBaseGui extends GuiElement {

    KeyframeAnimation showAnimation;

    KeyframeAnimation hideAnimation;

    public HoloRootBaseGui(int x, int y) {
        super(x, y, 320, 205);
        this.showAnimation = new KeyframeAnimation(100, this).applyTo(new Applier.TranslateY((float) (y - 5), (float) y), new Applier.Opacity(0.0F, 1.0F)).withDelay(50);
        this.hideAnimation = new KeyframeAnimation(50, this).applyTo(new Applier.TranslateY((float) (y - 5), (float) y), new Applier.Opacity(0.0F, 1.0F));
    }

    public void animateOpen() {
        new KeyframeAnimation(200, this).applyTo(new Applier.TranslateY((float) (this.y - 4), (float) this.y), new Applier.Opacity(0.0F, 1.0F)).withDelay(800).start();
    }

    @Override
    protected void onShow() {
        this.hideAnimation.stop();
        this.showAnimation.start();
    }

    @Override
    protected boolean onHide() {
        this.showAnimation.stop();
        this.hideAnimation.start();
        return super.onHide();
    }

    public void onReload() {
    }
}