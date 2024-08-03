package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.guielement.ButtonGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.network.CButtonGuiElementPacket;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ButtonGuiElementWidget extends AbstractGuiElementWidget<ButtonGuiElement> {

    private static final Component TITLE = Component.literal("Button");

    public ButtonGuiElementWidget(ButtonGuiElement element, IMachineScreen screen) {
        super(element, screen, TITLE);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture;
        if (this.getElement().isToggle() && (Boolean) this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.DATA_MACHINE_COMPONENT.get()).map(component -> component.getData().getBoolean(this.getElement().getId())).orElse(false)) {
            if (this.m_274382_()) {
                texture = this.getElement().getTextureToggleHovered();
            } else {
                texture = this.getElement().getTextureToggle();
            }
        } else if (this.m_274382_()) {
            texture = this.getElement().getTextureHovered();
        } else {
            texture = this.getElement().getTexture();
        }
        graphics.blit(texture, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        if (!this.getElement().getText().getString().isEmpty()) {
            graphics.drawString(Minecraft.getInstance().font, this.getElement().getText(), (int) ((float) this.m_252754_() + (float) this.f_93618_ / 2.0F - (float) Minecraft.getInstance().font.width(this.getElement().getText()) / 2.0F), (int) ((float) this.m_252907_() + (float) this.f_93619_ / 2.0F - 9.0F / 2.0F), 0);
        }
        if (!this.getElement().getItem().isEmpty()) {
            graphics.renderItem(this.getElement().getItem(), (int) ((float) this.m_252754_() + (float) this.f_93618_ / 2.0F - 8.0F), (int) ((float) this.m_252907_() + (float) this.f_93619_ / 2.0F - 8.0F));
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        new CButtonGuiElementPacket(this.getElement().getId(), this.getElement().isToggle()).sendToServer();
    }
}