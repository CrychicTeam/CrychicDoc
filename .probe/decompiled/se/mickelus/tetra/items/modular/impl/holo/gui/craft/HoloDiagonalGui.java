package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class HoloDiagonalGui extends GuiElement {

    private static final float targetOpacity = 0.2F;

    protected AnimationChain openAnimation;

    protected AnimationChain reopenAnimation;

    protected boolean upRight = false;

    public HoloDiagonalGui(int x, int y, int size, GuiAttachment attachment, int delay) {
        super(x, y, size, size);
        this.setAttachmentAnchor(GuiAttachment.middleCenter);
        this.setAttachmentPoint(attachment);
        if (attachment == GuiAttachment.bottomLeft || attachment == GuiAttachment.topRight) {
            this.upRight = true;
        }
        this.setOpacity(0.0F);
        this.openAnimation = new AnimationChain(new KeyframeAnimation(300, this).withDelay(delay).applyTo(new Applier.Opacity(0.3F)), new KeyframeAnimation(150, this).applyTo(new Applier.Opacity(0.2F)));
        this.reopenAnimation = new AnimationChain(new KeyframeAnimation(300, this).withDelay(delay / 10).applyTo(new Applier.Opacity(0.8F)), new KeyframeAnimation(150, this).applyTo(new Applier.Opacity(0.2F)));
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

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.upRight) {
            for (int i = 0; i < this.width; i++) {
                drawRect(graphics, refX + this.x + this.width - i - 1, refY + this.y + i, refX + this.x + this.width - i, refY + this.y + i + 1, 16777215, this.getOpacity() * opacity);
            }
        } else {
            for (int i = 0; i < this.width; i++) {
                drawRect(graphics, refX + this.x + i, refY + this.y + i, refX + this.x + i + 1, refY + this.y + i + 1, 16777215, this.getOpacity() * opacity);
            }
        }
    }
}