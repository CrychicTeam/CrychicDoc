package fr.frinn.custommachinery.impl.guielement;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TexturedGuiElementWidget<T extends AbstractTexturedGuiElement> extends AbstractGuiElementWidget<T> {

    public TexturedGuiElementWidget(T element, IMachineScreen screen, Component title) {
        super(element, screen, title);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.getElement().getTextureHovered() != null && this.m_274382_()) {
            graphics.blit(this.getElement().getTextureHovered(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        } else {
            graphics.blit(this.getElement().getTexture(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        }
    }
}