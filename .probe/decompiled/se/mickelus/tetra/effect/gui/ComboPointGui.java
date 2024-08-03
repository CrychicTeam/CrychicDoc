package se.mickelus.tetra.effect.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.effect.ComboPoints;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.InvertColorGui;

@ParametersAreNonnullByDefault
public class ComboPointGui extends GuiElement {

    private final GuiElement container;

    private final ComboPointGui.Point[] points;

    public ComboPointGui() {
        super(-1, 18, 15, 3);
        this.setAttachment(GuiAttachment.middleCenter);
        this.container = new InvertColorGui(0, 0, 16, 3);
        this.addChild(this.container);
        this.points = new ComboPointGui.Point[4];
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = new ComboPointGui.Point(i * 4, 0, i);
            this.container.addChild(this.points[i]);
        }
    }

    public void update(int points) {
        if (points > 0) {
            for (int i = 0; i < this.points.length; i++) {
                this.points[i].setVisible(true);
                this.points[i].toggle(points > i);
            }
        } else {
            for (int i = 0; i < this.points.length; i++) {
                this.points[i].setVisible(false);
            }
        }
    }

    public void update(Player player) {
        if (ComboPoints.canSpend(player)) {
            this.update(ComboPoints.get(player));
        } else {
            this.update(0);
        }
    }

    static class Point extends GuiElement {

        private final KeyframeAnimation showAnimation;

        private final KeyframeAnimation hideAnimation;

        private final GuiTexture active = new GuiTexture(0, 0, 3, 3, 3, 4, GuiTextures.hud);

        private final GuiTexture inactive;

        public Point(int x, int y, int offset) {
            super(x, y - 3, 3, 3);
            this.addChild(this.active);
            this.inactive = new GuiTexture(0, 0, 3, 3, 6, 4, GuiTextures.hud);
            this.addChild(this.inactive);
            this.showAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY((float) y)).withDelay(50 * offset);
            this.hideAnimation = new KeyframeAnimation(100, this).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY((float) (y - 3))).withDelay(50 * offset).onStop(complete -> {
                if (complete) {
                    this.isVisible = false;
                }
            });
        }

        public void toggle(boolean on) {
            this.active.setVisible(on);
            this.inactive.setVisible(!on);
        }

        @Override
        protected void onShow() {
            if (!this.showAnimation.isActive()) {
                this.showAnimation.start();
            }
            this.hideAnimation.stop();
        }

        @Override
        protected boolean onHide() {
            if (!this.hideAnimation.isActive()) {
                this.hideAnimation.start();
            }
            this.showAnimation.stop();
            return false;
        }
    }
}