package yesman.epicfight.client.gui.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Static extends AbstractWidget implements ResizableComponent {

    private Font font;

    private final int x1;

    private final int x2;

    private final int y1;

    private final int y2;

    private final ResizableComponent.HorizontalSizing horizontalSizingOption;

    private final ResizableComponent.VerticalSizing verticalSizingOption;

    public Static(Font font, int x1, int x2, int y1, int y2, ResizableComponent.HorizontalSizing horizontal, ResizableComponent.VerticalSizing vertical, Component message) {
        super(x1, x2, y1, y2, message);
        this.font = font;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.horizontalSizingOption = horizontal;
        this.verticalSizingOption = vertical;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        guiGraphics.drawString(this.font, this.m_6035_(), this.m_252754_(), this.m_252907_() + this.f_93619_ / 2 - 9 / 2, 16777215, false);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        return false;
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        return false;
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        return false;
    }

    @Override
    public int getX1() {
        return this.x1;
    }

    @Override
    public int getX2() {
        return this.x2;
    }

    @Override
    public int getY1() {
        return this.y1;
    }

    @Override
    public int getY2() {
        return this.y2;
    }

    @Override
    public ResizableComponent.HorizontalSizing getHorizontalSizingOption() {
        return this.horizontalSizingOption;
    }

    @Override
    public ResizableComponent.VerticalSizing getVerticalSizingOption() {
        return this.verticalSizingOption;
    }
}