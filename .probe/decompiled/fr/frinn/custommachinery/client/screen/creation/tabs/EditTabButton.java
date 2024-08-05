package fr.frinn.custommachinery.client.screen.creation.tabs;

import fr.frinn.custommachinery.client.screen.BaseScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.TabButton;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.resources.ResourceLocation;

public class EditTabButton extends TabButton {

    public EditTabButton(TabManager tabManager, MachineEditTab tab, int width, int height) {
        super(tabManager, tab, width, height);
    }

    public MachineEditTab tab() {
        return (MachineEditTab) super.tab();
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = new ResourceLocation("custommachinery", "textures/gui/creation/tab_button.png");
        graphics.blitNineSliced(texture, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_, 4, 4, 4, 4, 28, 32, this.m_274319_() ? 28 : 0, 0);
        Font font = Minecraft.getInstance().font;
        int color = this.f_93623_ ? -1 : -6250336;
        BaseScreen.drawCenteredScaledString(graphics, font, this.m_6035_(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + this.f_93619_ / 2, 0.9F, color, true);
    }
}