package se.mickelus.tetra.items.modular.impl.toolbelt.booster;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.VisibilityFilter;

@ParametersAreNonnullByDefault
public class GuiBarBooster extends GuiElement {

    private final int indicatorCount = 20;

    private final VisibilityFilter filter;

    private int visibleIndicators = 0;

    public GuiBarBooster(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.opacity = 0.0F;
        this.filter = new VisibilityFilter(0.0F, 20.0F);
    }

    public void setFuel(float fuel) {
        this.visibleIndicators = Math.round(fuel * 20.0F);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        opacity = this.filter.apply((float) this.visibleIndicators) * opacity;
        if (opacity > 0.0F) {
            graphics.pose().translate(opacity * -10.0F, 0.0F, 0.0F);
            for (int i = 0; i < this.visibleIndicators; i++) {
                drawRect(graphics, refX + this.x + 2 * i, refY + this.y, refX + this.x + 2 * i + 1, refY + this.y + 4, 16777215, opacity * 0.9F);
            }
            for (int i = this.visibleIndicators; i < 20; i++) {
                drawRect(graphics, refX + this.x + 2 * i, refY + this.y, refX + this.x + 2 * i + 1, refY + this.y + 4, 0, opacity * 0.3F);
            }
            drawRect(graphics, refX + this.x - 2, refY + this.y + 3, refX + this.x - 1, refY + this.y + 5, 16777215, opacity * 0.3F);
            drawRect(graphics, refX + this.x - 2, refY + this.y + 5, refX + this.x + 10, refY + this.y + 6, 16777215, opacity * 0.3F);
            graphics.pose().translate(opacity * 10.0F, 0.0F, 0.0F);
        }
    }
}