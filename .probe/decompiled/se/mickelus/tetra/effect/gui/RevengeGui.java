package se.mickelus.tetra.effect.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.effect.revenge.RevengeTracker;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class RevengeGui extends GuiElement {

    private final KeyframeAnimation showAnimationLeft;

    private final KeyframeAnimation hideAnimationLeft;

    private final KeyframeAnimation showAnimationRight;

    private final KeyframeAnimation hideAnimationRight;

    private final GuiTexture indicatorLeft;

    public RevengeGui() {
        super(0, 14, 13, 3);
        this.setAttachment(GuiAttachment.middleCenter);
        this.indicatorLeft = new GuiTexture(-3, 0, 5, 3, 9, 4, GuiTextures.hud);
        this.indicatorLeft.setOpacity(0.0F);
        this.addChild(this.indicatorLeft);
        GuiTexture indicatorRight = new GuiTexture(2, 0, 5, 3, 15, 4, GuiTextures.hud);
        indicatorRight.setOpacity(0.0F);
        indicatorRight.setAttachment(GuiAttachment.topRight);
        this.addChild(indicatorRight);
        this.showAnimationLeft = new KeyframeAnimation(120, this.indicatorLeft).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(0.0F));
        this.hideAnimationLeft = new KeyframeAnimation(60, this.indicatorLeft).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(-3.0F));
        this.showAnimationRight = new KeyframeAnimation(120, indicatorRight).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX(0.0F));
        this.hideAnimationRight = new KeyframeAnimation(60, indicatorRight).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX(2.0F));
    }

    public void update(Player player, HitResult mouseover) {
        if (mouseover != null && mouseover.getType() == HitResult.Type.ENTITY && RevengeTracker.canRevenge(player) && RevengeTracker.canRevenge(player, ((EntityHitResult) mouseover).getEntity())) {
            if (!this.showAnimationLeft.isActive() && this.indicatorLeft.getOpacity() < 1.0F) {
                this.hideAnimationLeft.stop();
                this.hideAnimationRight.stop();
                this.showAnimationLeft.start();
                this.showAnimationRight.start();
            }
        } else if (!this.hideAnimationLeft.isActive() && this.indicatorLeft.getOpacity() > 0.0F) {
            this.showAnimationLeft.stop();
            this.showAnimationRight.stop();
            this.hideAnimationLeft.start();
            this.hideAnimationRight.start();
        }
    }
}