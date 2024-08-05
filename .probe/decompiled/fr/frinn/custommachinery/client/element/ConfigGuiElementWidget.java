package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.client.screen.CustomMachineScreen;
import fr.frinn.custommachinery.client.screen.MachineConfigScreen;
import fr.frinn.custommachinery.common.guielement.ConfigGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ConfigGuiElementWidget extends AbstractGuiElementWidget<ConfigGuiElement> {

    private static final Component TITLE = Component.translatable("custommachinery.gui.element.config.name");

    public ConfigGuiElementWidget(ConfigGuiElement element, IMachineScreen screen) {
        super(element, screen, TITLE);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int i, int j, float f) {
        if (!this.m_274382_()) {
            graphics.blit(this.getElement().getTexture(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        } else {
            graphics.blit(this.getElement().getTextureHovered(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        Minecraft.getInstance().setScreen(new MachineConfigScreen((CustomMachineScreen) this.getScreen()));
    }
}