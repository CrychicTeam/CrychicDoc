package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class HoloCrossGui extends GuiElement {

    protected AnimationChain openAnimation;

    protected AnimationChain reopenAnimation;

    public HoloCrossGui(int x, int y, int delay) {
        this(x, y, delay, 0.2F);
    }

    public HoloCrossGui(int x, int y, int delay, float targetOpacity) {
        super(x, y, 5, 5);
        this.setAttachment(GuiAttachment.middleCenter);
        this.addChild(new GuiRect(0, 0, 2, 1, 16777215).setAttachment(GuiAttachment.middleLeft));
        this.addChild(new GuiRect(0, 0, 3, 1, 16777215).setAttachment(GuiAttachment.middleRight));
        this.addChild(new GuiRect(0, 0, 1, 2, 16777215).setAttachment(GuiAttachment.topCenter));
        this.addChild(new GuiRect(0, 0, 1, 2, 16777215).setAttachment(GuiAttachment.bottomCenter));
        this.setOpacity(0.0F);
        this.openAnimation = new AnimationChain(new KeyframeAnimation(300, this).withDelay(delay).applyTo(new Applier.Opacity(targetOpacity + 0.3F)), new KeyframeAnimation(200, this).applyTo(new Applier.Opacity(targetOpacity)));
        this.reopenAnimation = new AnimationChain(new KeyframeAnimation(300, this).withDelay(delay / 10).applyTo(new Applier.Opacity(targetOpacity + 0.6F)), new KeyframeAnimation(200, this).applyTo(new Applier.Opacity(targetOpacity)));
    }

    public void animateOpen() {
        this.openAnimation.stop();
        this.reopenAnimation.stop();
        this.setOpacity(0.0F);
        this.openAnimation.start();
    }

    public void animateReopen() {
        this.openAnimation.stop();
        this.reopenAnimation.stop();
        this.setOpacity(0.0F);
        this.reopenAnimation.start();
    }

    public void stopAnimations() {
        this.openAnimation.stop();
        this.reopenAnimation.stop();
        this.setOpacity(0.0F);
    }
}