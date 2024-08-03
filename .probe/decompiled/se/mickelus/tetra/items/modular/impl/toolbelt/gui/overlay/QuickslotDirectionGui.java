package se.mickelus.tetra.items.modular.impl.toolbelt.gui.overlay;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class QuickslotDirectionGui extends GuiElement {

    private final GuiTexture arrow;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    public QuickslotDirectionGui(int x, int y, int width, int height, boolean right) {
        super(x, y, width, height);
        if (right) {
            this.arrow = new GuiTexture(7, -1, 5, 7, 5, 42, GuiTextures.toolbelt);
            this.arrow.setAttachment(GuiAttachment.middleLeft);
        } else {
            this.arrow = new GuiTexture(-7, -1, 5, 7, 0, 42, GuiTextures.toolbelt);
            this.arrow.setAttachment(GuiAttachment.middleRight);
        }
        this.arrow.setColor(4210752);
        this.arrow.setOpacity(0.0F);
        this.addChild(this.arrow);
        this.showAnimation = new KeyframeAnimation(100, this.arrow).applyTo(new Applier.TranslateX((float) (this.arrow.getX() + (right ? 1 : -1))), new Applier.Opacity(1.0F));
        this.hideAnimation = new KeyframeAnimation(200, this.arrow).applyTo(new Applier.TranslateX((float) this.arrow.getX()), new Applier.Opacity(0.0F));
    }

    public void animateIn() {
        this.hideAnimation.stop();
        this.showAnimation.start();
    }

    public void animateOut() {
        this.arrow.setColor(4210752);
        this.showAnimation.stop();
        this.hideAnimation.start();
    }

    @Override
    protected void onFocus() {
        this.arrow.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        this.arrow.setColor(4210752);
    }
}