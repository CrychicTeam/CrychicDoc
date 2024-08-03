package journeymap.client.model;

import java.awt.geom.Rectangle2D.Double;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import journeymap.client.Constants;
import journeymap.client.texture.Texture;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.component.Button;
import net.minecraft.client.gui.Font;

public class SplashPerson {

    public final String name;

    public final String uuid;

    public final String title;

    public Button button;

    public int width;

    public double moveX;

    public double moveY;

    private double moveDistance = 1.0;

    private Random r = new Random();

    public SplashPerson(String uuid, String name, String titleKey) {
        this.uuid = uuid;
        this.name = name;
        if (titleKey != null) {
            this.title = Constants.getString(titleKey);
        } else {
            this.title = "";
        }
    }

    public Button getButton() {
        return this.button;
    }

    public void setButton(Button button) {
        this.button = button;
        this.randomizeVector();
    }

    public Texture getSkin() {
        return TextureCache.getPlayerSkin(UUID.fromString(this.uuid), this.name);
    }

    public int getWidth(Font fr) {
        this.width = fr.width(this.title);
        String[] nameParts = this.name.trim().split(" ");
        for (String part : nameParts) {
            this.width = Math.max(this.width, fr.width(part));
        }
        return this.width;
    }

    public void setWidth(int minWidth) {
        this.width = minWidth;
    }

    public void randomizeVector() {
        this.moveDistance = this.r.nextDouble() + 0.5;
        this.moveX = this.r.nextBoolean() ? this.moveDistance : -this.moveDistance;
        this.moveDistance = this.r.nextDouble() + 0.5;
        this.moveY = this.r.nextBoolean() ? this.moveDistance : -this.moveDistance;
    }

    public void adjustVector(Double screenBounds) {
        Double buttonBounds = this.button.getBounds();
        if (!screenBounds.contains(buttonBounds)) {
            int xMargin = this.button.m_5711_();
            int yMargin = this.button.m_93694_();
            if (buttonBounds.getMinX() <= (double) xMargin) {
                this.moveX = this.moveDistance;
            } else if (buttonBounds.getMaxX() >= screenBounds.getWidth() - (double) xMargin) {
                this.moveX = -this.moveDistance;
            }
            if (buttonBounds.getMinY() <= (double) yMargin) {
                this.moveY = this.moveDistance;
            } else if (buttonBounds.getMaxY() >= screenBounds.getHeight() - (double) yMargin) {
                this.moveY = -this.moveDistance;
            }
        }
        this.continueVector();
    }

    public void continueVector() {
        this.button.setX((int) Math.round((double) this.button.m_252754_() + this.moveX));
        this.button.setY((int) Math.round((double) this.button.m_252907_() + this.moveY));
    }

    public void avoid(List<SplashPerson> others) {
        for (SplashPerson other : others) {
            if (this != other && this.getDistance(other) <= (double) this.button.m_5711_()) {
                this.randomizeVector();
                break;
            }
        }
    }

    public double getDistance(SplashPerson other) {
        double px = (double) (this.button.getCenterX() - other.button.getCenterX());
        double py = (double) (this.button.getMiddleY() - other.button.getMiddleY());
        return Math.sqrt(px * px + py * py);
    }

    public static class Fake extends SplashPerson {

        private Texture texture;

        public Fake(String name, String title, Texture texture) {
            super(name, title, null);
            this.texture = texture;
        }

        @Override
        public Texture getSkin() {
            return this.texture;
        }
    }
}