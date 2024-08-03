package se.mickelus.tetra.gui.stats.bar;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;

@ParametersAreNonnullByDefault
public class GuiBarSplit extends GuiBar {

    private final GuiBar negativeBar;

    private final GuiBar positiveBar;

    public GuiBarSplit(int x, int y, int barLength, double range, boolean inverted) {
        super(x, y, barLength, -range, range, inverted);
        this.negativeBar = new GuiBar(0, 0, (barLength - 5) / 2, 0.0, range, !inverted);
        this.negativeBar.setAlignment(GuiAlignment.right);
        this.addChild(this.negativeBar);
        this.positiveBar = new GuiBar(0, 0, (barLength - 5) / 2, 0.0, range, inverted);
        this.positiveBar.setAttachment(GuiAttachment.topRight);
        this.addChild(this.positiveBar);
        GuiElement separator = new GuiRect(0, 5, 1, 3, 8355711);
        separator.setAttachment(GuiAttachment.topCenter);
        this.addChild(separator);
    }

    @Override
    protected void calculateBarLengths() {
        this.negativeBar.setValue(this.value > 0.0 ? 0.0 : -this.value, this.diffValue > 0.0 ? 0.0 : -this.diffValue);
        this.positiveBar.setValue(this.value < 0.0 ? 0.0 : this.value, this.diffValue < 0.0 ? 0.0 : this.diffValue);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}