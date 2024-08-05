package se.mickelus.tetra.effect.howling;

import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HowlingIndicatorGui extends GuiTexture {

    private final KeyframeAnimation animation;

    private final int originX;

    private final int originY;

    public HowlingIndicatorGui(int x, int y, int width, int height, int textureX, int textureY, int transitionOffset, boolean horizontalTransition) {
        super(x, y, width, height, textureX, textureY, GuiTextures.hud);
        this.setOpacity(0.0F);
        if (horizontalTransition) {
            this.animation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(0.5F), new Applier.TranslateX((float) x));
            this.originX = x + transitionOffset;
            this.originY = y;
            this.setX(x + transitionOffset);
        } else {
            this.animation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(0.5F), new Applier.TranslateY((float) y));
            this.originX = x;
            this.originY = y + transitionOffset;
            this.setY(y + transitionOffset);
        }
    }

    public void show() {
        if (!this.animation.isActive() && (double) this.getOpacity() < 0.5) {
            this.animation.start();
        }
    }

    public void reset() {
        this.animation.stop();
        this.setOpacity(0.0F);
        this.setX(this.originX);
        this.setY(this.originY);
    }
}