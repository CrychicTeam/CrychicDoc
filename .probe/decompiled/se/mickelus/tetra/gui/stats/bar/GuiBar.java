package se.mickelus.tetra.gui.stats.bar;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiElement;

@ParametersAreNonnullByDefault
public class GuiBar extends GuiElement {

    protected static final int increaseColorBar = -2007629995;

    protected static final int decreaseColorBar = -1996532395;

    protected int diffColor;

    protected int color = 16777215;

    protected double min;

    protected double max;

    protected double value;

    protected double diffValue;

    protected int barLength;

    protected int diffLength;

    protected boolean invertedDiff = false;

    protected GuiAlignment alignment = GuiAlignment.left;

    public GuiBar(int x, int y, int barLength, double min, double max) {
        this(x, y, barLength, min, max, false);
    }

    public GuiBar(int x, int y, int barLength, double min, double max, boolean invertedDiff) {
        super(x, y, barLength, 1);
        this.min = min;
        this.max = max;
        this.invertedDiff = invertedDiff;
    }

    public GuiBar setAlignment(GuiAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public GuiBar setColor(int color) {
        this.color = color;
        return this;
    }

    public void setMin(double min) {
        this.min = min;
        this.calculateBarLengths();
    }

    public void setMax(double max) {
        this.max = max;
        this.calculateBarLengths();
    }

    public void setValue(double value, double diffValue) {
        this.value = Math.min(Math.max(value, this.min), this.max);
        this.diffValue = Math.min(Math.max(diffValue, this.min), this.max);
        this.calculateBarLengths();
    }

    protected void calculateBarLengths() {
        double minValue = Math.min(this.value, this.diffValue);
        this.barLength = (int) Math.floor((minValue - this.min) / (this.max - this.min) * (double) this.width);
        this.diffLength = (int) Math.ceil(Math.abs(this.value - this.diffValue) / (this.max - this.min) * (double) this.width);
        this.diffColor = this.invertedDiff ^ this.value < this.diffValue ? -2007629995 : -1996532395;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        drawRect(graphics, refX + this.x, refY + this.y + 6, refX + this.x + this.width, refY + this.y + 6 + this.height, this.color, 0.14F * opacity);
        if (this.alignment == GuiAlignment.right) {
            drawRect(graphics, refX + this.x + this.width - this.barLength, refY + this.y + 6, refX + this.x + this.width, refY + this.y + 6 + this.height, this.color, opacity);
            drawRect(graphics, refX + this.x + this.width - this.barLength - this.diffLength, refY + this.y + 6, refX + this.x + this.width - this.barLength, refY + this.y + 6 + this.height, this.diffColor, 1.0F);
        } else {
            drawRect(graphics, refX + this.x, refY + this.y + 6, refX + this.x + this.barLength, refY + this.y + 6 + this.height, this.color, opacity);
            drawRect(graphics, refX + this.x + this.barLength, refY + this.y + 6, refX + this.x + this.barLength + this.diffLength, refY + this.y + 6 + this.height, this.diffColor, 1.0F);
        }
    }
}