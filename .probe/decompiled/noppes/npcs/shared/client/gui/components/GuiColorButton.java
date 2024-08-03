package noppes.npcs.shared.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.client.gui.custom.GuiCustom;
import noppes.npcs.client.gui.custom.components.CustomGuiButton;

public class GuiColorButton extends CustomGuiButton {

    public int color;

    public GuiColorButton(GuiCustom parent, CustomGuiButtonWrapper component, int color) {
        super(parent, component);
        this.color = color;
    }

    @Override
    public void onRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + 50, this.m_252907_() + 20, -16777216 + this.color);
        }
    }
}