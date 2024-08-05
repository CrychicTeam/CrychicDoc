package se.mickelus.tetra.gui.stats.bar;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiAlignment;

@ParametersAreNonnullByDefault
public class GuiBarSegmented extends GuiBar {

    private final int maxSegments;

    private int segmentCount;

    private int diffCount;

    private int segmentLength;

    public GuiBarSegmented(int x, int y, int barLength, double min, double max) {
        super(x, y, barLength, min, max);
        this.maxSegments = (int) (max - min);
    }

    public GuiBarSegmented(int x, int y, int barLength, double min, double max, boolean invertedDiff) {
        super(x, y, barLength + 1, min, max, invertedDiff);
        this.maxSegments = (int) (max - min);
    }

    @Override
    protected void calculateBarLengths() {
        double minValue = Math.min(this.value, this.diffValue);
        this.segmentCount = (int) Math.round(minValue - this.min);
        this.diffCount = (int) Math.ceil(Math.abs(this.value - this.diffValue));
        this.segmentLength = this.width / this.maxSegments;
        this.diffColor = this.invertedDiff ^ this.value < this.diffValue ? -2007629995 : -1996532395;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.alignment == GuiAlignment.right) {
            for (int i = 0; i < this.segmentCount; i++) {
                this.drawSegmentReverse(graphics, refX, refY, i, -1, opacity);
            }
            for (int i = this.segmentCount; i < this.segmentCount + this.diffCount; i++) {
                this.drawSegmentReverse(graphics, refX, refY, i, this.diffColor, 1.0F);
            }
            for (int i = this.segmentCount + this.diffCount; i < this.maxSegments; i++) {
                this.drawSegmentReverse(graphics, refX, refY, i, 16777215, 0.14F * opacity);
            }
        } else {
            for (int i = 0; i < this.segmentCount; i++) {
                this.drawSegment(graphics, refX, refY, i, -1, opacity);
            }
            for (int i = this.segmentCount; i < this.segmentCount + this.diffCount; i++) {
                this.drawSegment(graphics, refX, refY, i, this.diffColor, 1.0F);
            }
            for (int i = this.segmentCount + this.diffCount; i < this.maxSegments; i++) {
                this.drawSegment(graphics, refX, refY, i, 16777215, 0.14F * opacity);
            }
        }
    }

    private void drawSegment(GuiGraphics graphics, int refX, int refY, int index, int color, float opacity) {
        drawRect(graphics, refX + this.x + index * this.segmentLength, refY + this.y + 6, refX + this.x + (index + 1) * this.segmentLength - 1, refY + this.y + 6 + this.height, color, opacity);
    }

    private void drawSegmentReverse(GuiGraphics graphics, int refX, int refY, int index, int color, float opacity) {
        this.drawSegment(graphics, refX + this.width, refY, -index - 1, color, opacity);
    }
}