package se.mickelus.tetra.blocks.workbench.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class GuiInventoryHighlight extends GuiElement {

    private final AnimationChain animation;

    GuiElement dots;

    public GuiInventoryHighlight(int x, int y, int offset) {
        super(x, y, 16, 16);
        GuiTexture texture = new GuiTexture(0, 0, 16, 16, 80, 16, GuiTextures.workbench);
        this.addChild(texture);
        this.dots = new GuiElement(2, 2, 12, 12);
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 16777215));
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 16777215).setAttachment(GuiAttachment.topRight));
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 16777215).setAttachment(GuiAttachment.bottomLeft));
        this.dots.addChild(new GuiRect(0, 0, 1, 1, 16777215).setAttachment(GuiAttachment.bottomRight));
        this.addChild(this.dots);
        this.animation = new AnimationChain(new KeyframeAnimation(200, texture).withDelay(offset * 50).applyTo(new Applier.Opacity(0.0F, 1.0F)), new KeyframeAnimation(300, texture).applyTo(new Applier.Opacity(0.4F)), new KeyframeAnimation(400, this.dots).applyTo(new Applier.Opacity(1.0F)));
    }

    @Override
    protected void onShow() {
        this.dots.setOpacity(0.0F);
        this.animation.start();
    }

    @Override
    protected boolean onHide() {
        this.animation.stop();
        return super.onHide();
    }
}