package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.component.FuelMachineComponent;
import fr.frinn.custommachinery.common.guielement.FuelGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FuelGuiElementWidget extends AbstractGuiElementWidget<FuelGuiElement> {

    public FuelGuiElementWidget(FuelGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Fuel"));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(this.getElement().getEmptyTexture(), this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
        int fuel = (Integer) this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.FUEL_MACHINE_COMPONENT.get()).map(FuelMachineComponent::getFuel).orElse(0);
        int maxFuel = (Integer) this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.FUEL_MACHINE_COMPONENT.get()).map(FuelMachineComponent::getMaxFuel).orElse(0);
        if (fuel != 0 && maxFuel != 0) {
            double filledPercent = (double) fuel / (double) maxFuel;
            graphics.blit(this.getElement().getFilledTexture(), this.m_252754_(), this.m_252907_() + (int) ((double) this.f_93619_ * (1.0 - filledPercent)), 0.0F, (float) ((int) ((double) this.f_93619_ * (1.0 - filledPercent))), this.f_93618_, (int) ((double) this.f_93619_ * filledPercent), this.f_93618_, this.f_93619_);
        }
    }

    @Override
    public List<Component> getTooltips() {
        return !this.getElement().getTooltips().isEmpty() ? this.getElement().getTooltips() : (List) this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.FUEL_MACHINE_COMPONENT.get()).map(component -> Collections.singletonList(Component.translatable("custommachinery.gui.element.fuel.tooltip", component.getFuel()))).orElse(Collections.emptyList());
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }
}