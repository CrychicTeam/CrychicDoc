package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class StringWidget extends AbstractStringWidget {

    private float alignX = 0.5F;

    public StringWidget(Component component0, Font font1) {
        this(0, 0, font1.width(component0.getVisualOrderText()), 9, component0, font1);
    }

    public StringWidget(int int0, int int1, Component component2, Font font3) {
        this(0, 0, int0, int1, component2, font3);
    }

    public StringWidget(int int0, int int1, int int2, int int3, Component component4, Font font5) {
        super(int0, int1, int2, int3, component4, font5);
        this.f_93623_ = false;
    }

    public StringWidget setColor(int int0) {
        super.setColor(int0);
        return this;
    }

    private StringWidget horizontalAlignment(float float0) {
        this.alignX = float0;
        return this;
    }

    public StringWidget alignLeft() {
        return this.horizontalAlignment(0.0F);
    }

    public StringWidget alignCenter() {
        return this.horizontalAlignment(0.5F);
    }

    public StringWidget alignRight() {
        return this.horizontalAlignment(1.0F);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        Component $$4 = this.m_6035_();
        Font $$5 = this.m_269100_();
        int $$6 = this.m_252754_() + Math.round(this.alignX * (float) (this.m_5711_() - $$5.width($$4)));
        int $$7 = this.m_252907_() + (this.m_93694_() - 9) / 2;
        guiGraphics0.drawString($$5, $$4, $$6, $$7, this.m_269468_());
    }
}