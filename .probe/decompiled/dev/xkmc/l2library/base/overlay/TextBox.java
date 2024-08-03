package dev.xkmc.l2library.base.overlay;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class TextBox extends OverlayUtil {

    private final int anchorX;

    private final int anchorY;

    public TextBox(GuiGraphics g, int anchorX, int anchorY, int x, int y, int width) {
        super(g, x, y, width);
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    @Override
    public Vector2ic positionTooltip(int gw, int gh, int x, int y, int tw, int th) {
        return new Vector2i(x - tw * this.anchorX / 2, y - th * this.anchorY / 2);
    }

    @Override
    public int getMaxWidth() {
        if (this.anchorX == 0) {
            return this.g.guiWidth() - this.x0 - 8;
        } else if (this.anchorX == 1) {
            return Math.max(this.x0 / 2 - 4, this.g.guiWidth() - this.x0 / 2 - 4);
        } else {
            return this.anchorX == 2 ? this.x0 - 8 : this.g.guiWidth();
        }
    }
}