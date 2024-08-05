package se.mickelus.tetra.items.modular.impl.holo.gui.scan;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.AnimationChain;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class ScannerBarGui extends GuiElement {

    private static final int unitWidth = 6;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final GuiString statusLabel;

    private final KeyframeAnimation statusInAnimation;

    private final KeyframeAnimation statusOutAnimation;

    int horizontalSpread;

    private AnimationChain[] upAnimations;

    private AnimationChain[] upHighlightAnimations;

    private AnimationChain[] midAnimations;

    private AnimationChain[] midHighlightAnimations;

    private AnimationChain[] downAnimations;

    private AnimationChain[] downHighlightAnimations;

    private boolean hasStatus;

    public ScannerBarGui(int x, int y, int horizontalSpread) {
        super(x, y, horizontalSpread * 6, 9);
        this.horizontalSpread = horizontalSpread;
        this.statusLabel = new GuiStringOutline(-2, 0, "");
        this.statusLabel.setAttachment(GuiAttachment.middleCenter);
        this.statusLabel.setOpacity(0.0F);
        this.addChild(this.statusLabel);
        this.statusInAnimation = new KeyframeAnimation(200, this.statusLabel).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(-2.0F, 0.0F));
        this.statusOutAnimation = new KeyframeAnimation(300, this.statusLabel).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(0.0F, 2.0F));
        this.showAnimation = new KeyframeAnimation(300, this).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY((float) y, (float) (y - 4)));
        this.hideAnimation = new KeyframeAnimation(300, this).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY((float) (y - 4), (float) y)).withDelay(200).onStop(complete -> this.isVisible = false);
        this.setup();
    }

    public static double getDegreesPerUnit() {
        return 1.0 * (double) Minecraft.getInstance().options.fov().get().intValue() * 6.0 / (double) Minecraft.getInstance().getWindow().getGuiScaledWidth();
    }

    private void setup() {
        this.setWidth(this.horizontalSpread * 6);
        this.upAnimations = new AnimationChain[this.horizontalSpread];
        this.midAnimations = new AnimationChain[this.horizontalSpread];
        this.downAnimations = new AnimationChain[this.horizontalSpread];
        this.upHighlightAnimations = new AnimationChain[this.horizontalSpread];
        this.midHighlightAnimations = new AnimationChain[this.horizontalSpread];
        this.downHighlightAnimations = new AnimationChain[this.horizontalSpread];
        this.addChild(new GuiRect(-3, -2, this.getWidth() + 3, this.getHeight() + 2, 0).setOpacity(0.5F));
        this.addChild(new GuiTexture(-2, -1, 2, 2, 1, 1, GuiTextures.hud).setOpacity(0.8F).setAttachment(GuiAttachment.topLeft));
        this.addChild(new GuiTexture(-2, -1, 2, 2, 1, 0, GuiTextures.hud).setOpacity(0.8F).setAttachment(GuiAttachment.bottomLeft));
        this.addChild(new GuiTexture(-1, -1, 2, 2, 0, 1, GuiTextures.hud).setOpacity(0.8F).setAttachment(GuiAttachment.topRight));
        this.addChild(new GuiTexture(-1, -1, 2, 2, 0, 0, GuiTextures.hud).setOpacity(0.8F).setAttachment(GuiAttachment.bottomRight));
        this.addChild(new GuiTexture(-2, -1, 3, 2, 0, 1, GuiTextures.hud).setOpacity(0.8F).setAttachment(GuiAttachment.topCenter));
        this.addChild(new GuiTexture(-2, -1, 3, 2, 0, 0, GuiTextures.hud).setOpacity(0.8F).setAttachment(GuiAttachment.bottomCenter));
        for (int i = 0; i < this.horizontalSpread; i++) {
            GuiElement up = new GuiTexture(i * 6, 0, 3, 3, GuiTextures.hud).setOpacity(0.3F);
            this.addChild(up);
            this.upAnimations[i] = new AnimationChain(new KeyframeAnimation(100, up).applyTo(new Applier.Opacity(0.7F)), new KeyframeAnimation(600, up).applyTo(new Applier.Opacity(0.3F)));
            GuiElement upHighlight = new GuiTexture(i * 6, 0, 3, 3, GuiTextures.hud).setColor(15882800).setOpacity(0.0F);
            this.addChild(upHighlight);
            this.upHighlightAnimations[i] = new AnimationChain(new KeyframeAnimation(100, upHighlight).applyTo(new Applier.Opacity(0.9F)), new KeyframeAnimation(1000, upHighlight).applyTo(new Applier.Opacity(0.0F)));
            GuiElement down = new GuiTexture(i * 6, 4, 3, 3, GuiTextures.hud).setOpacity(0.3F);
            this.addChild(down);
            this.downAnimations[i] = new AnimationChain(new KeyframeAnimation(100, down).applyTo(new Applier.Opacity(0.7F)), new KeyframeAnimation(600, down).applyTo(new Applier.Opacity(0.3F)));
            GuiElement downHighlight = new GuiTexture(i * 6, 4, 3, 3, GuiTextures.hud).setColor(15882800).setOpacity(0.0F);
            this.addChild(downHighlight);
            this.downHighlightAnimations[i] = new AnimationChain(new KeyframeAnimation(100, downHighlight).applyTo(new Applier.Opacity(0.9F)), new KeyframeAnimation(1000, downHighlight).applyTo(new Applier.Opacity(0.0F)));
        }
        for (int i = 0; i < this.horizontalSpread - 1; i++) {
            GuiElement center = new GuiTexture(i * 6 + 3, 2, 3, 3, GuiTextures.hud).setOpacity(0.3F);
            this.addChild(center);
            this.midAnimations[i] = new AnimationChain(new KeyframeAnimation(100, center).applyTo(new Applier.Opacity(0.7F)), new KeyframeAnimation(600, center).applyTo(new Applier.Opacity(0.3F)));
            GuiElement centerHighlight = new GuiTexture(i * 6 + 3, 2, 3, 3, GuiTextures.hud).setColor(15882800).setOpacity(0.0F);
            this.addChild(centerHighlight);
            this.midHighlightAnimations[i] = new AnimationChain(new KeyframeAnimation(100, centerHighlight).applyTo(new Applier.Opacity(0.9F)), new KeyframeAnimation(1000, centerHighlight).applyTo(new Applier.Opacity(0.0F)));
        }
        this.addChild(this.statusLabel);
    }

    public void setHorizontalSpread(int horizontalSpread) {
        if (horizontalSpread != 0 && this.horizontalSpread != horizontalSpread) {
            this.horizontalSpread = horizontalSpread;
            this.clearChildren();
            this.setup();
        }
    }

    public void setStatus(String status) {
        if (status != null != this.hasStatus) {
            this.hasStatus = status != null;
            if (this.hasStatus) {
                this.statusOutAnimation.stop();
                this.statusInAnimation.start();
            } else {
                this.statusInAnimation.stop();
                this.statusOutAnimation.start();
            }
        }
        this.statusLabel.setString(status);
    }

    protected void show() {
        this.isVisible = true;
        if (this.opacity < 1.0F && !this.showAnimation.isActive()) {
            this.isVisible = true;
            this.hideAnimation.stop();
            this.showAnimation.start();
        }
    }

    protected boolean hide() {
        if (this.opacity > 0.0F && !this.hideAnimation.isActive()) {
            this.showAnimation.stop();
            this.hideAnimation.start();
        }
        return false;
    }

    public void highlightUp(int index, boolean wasHit) {
        if (this.opacity == 1.0F) {
            if (wasHit) {
                this.upHighlightAnimations[index].start();
            } else {
                this.upAnimations[index].start();
            }
        }
    }

    public void highlightMid(int index, boolean wasHit) {
        if (this.opacity == 1.0F) {
            if (wasHit) {
                this.midHighlightAnimations[index].start();
            } else {
                this.midAnimations[index].start();
            }
        }
    }

    public void highlightDown(int index, boolean wasHit) {
        if (this.opacity == 1.0F) {
            if (wasHit) {
                this.downHighlightAnimations[index].start();
            } else {
                this.downAnimations[index].start();
            }
        }
    }
}