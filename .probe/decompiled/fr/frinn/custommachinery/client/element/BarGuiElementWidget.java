package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.client.ClientHandler;
import fr.frinn.custommachinery.common.guielement.BarGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BarGuiElementWidget extends AbstractGuiElementWidget<BarGuiElement> {

    private static final Component TITLE = Component.literal("Bar");

    public BarGuiElementWidget(BarGuiElement element, IMachineScreen screen) {
        super(element, screen, TITLE);
    }

    @Override
    public List<Component> getTooltips() {
        List<Component> tooltips = new ArrayList(this.getElement().getTooltips());
        this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.DATA_MACHINE_COMPONENT.get()).ifPresent(component -> tooltips.add(Component.literal(String.format("%s / %s", component.getData().getDouble(this.getElement().getId()), this.getElement().getMax()))));
        return tooltips;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        int filledWidth = (int) ((double) this.f_93618_ * Mth.clamp(this.getFillingPercent(), 0.0, 1.0));
        int filledHeight = (int) ((double) this.f_93619_ * Mth.clamp(this.getFillingPercent(), 0.0, 1.0));
        graphics.blit(this.getElement().getEmptyTexture(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        ResourceLocation filled = this.getElement().getFilledTexture();
        switch(this.getElement().getOrientation()) {
            case RIGHT:
                graphics.blit(filled, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, filledWidth, this.f_93619_, this.f_93618_, this.f_93619_);
                break;
            case LEFT:
                graphics.blit(filled, this.m_252754_() + this.f_93618_ - filledWidth, this.m_252907_(), (float) (this.f_93618_ - filledWidth), 0.0F, filledWidth, this.f_93619_, this.f_93618_, this.f_93619_);
                break;
            case BOTTOM:
                graphics.blit(filled, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, filledHeight, this.f_93618_, this.f_93619_);
                break;
            case TOP:
                graphics.blit(filled, this.m_252754_(), this.m_252907_() + this.f_93619_ - filledHeight, 0.0F, (float) (this.f_93619_ - filledHeight), this.f_93618_, filledHeight, this.f_93618_, this.f_93619_);
        }
        if (this.m_274382_() && this.getElement().isHighlight()) {
            ClientHandler.renderSlotHighlight(graphics, this.m_252754_() + 1, this.m_252907_() + 1, this.f_93618_ - 2, this.f_93619_ - 2);
        }
    }

    private double getFillingPercent() {
        double amount = (Double) this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.DATA_MACHINE_COMPONENT.get()).map(component -> component.getData().getDouble(this.getElement().getId())).orElse(0.0);
        return (amount - (double) this.getElement().getMin()) / (double) this.getElement().getMax();
    }
}