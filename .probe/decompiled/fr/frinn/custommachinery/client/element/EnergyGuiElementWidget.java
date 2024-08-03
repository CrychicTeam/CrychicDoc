package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.PlatformHelper;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.client.ClientHandler;
import fr.frinn.custommachinery.common.guielement.EnergyGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.impl.guielement.TexturedGuiElementWidget;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class EnergyGuiElementWidget extends TexturedGuiElementWidget<EnergyGuiElement> {

    public EnergyGuiElementWidget(EnergyGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Energy"));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).ifPresent(energy -> {
            double fillPercent = energy.getFillPercent();
            int eneryHeight = (int) (fillPercent * (double) this.f_93619_);
            graphics.blit(this.getElement().getFilledTexture(), this.m_252754_(), this.m_252907_() + this.f_93619_ - eneryHeight, 0.0F, (float) (this.f_93619_ - eneryHeight), this.f_93618_, eneryHeight, this.f_93618_, this.f_93619_);
        });
        if (this.m_274382_() && this.getElement().highlight()) {
            ClientHandler.renderSlotHighlight(graphics, this.m_252754_() + 1, this.m_252907_() + 1, this.f_93618_ - 2, this.f_93619_ - 2);
        }
    }

    @Override
    public List<Component> getTooltips() {
        return !this.getElement().getTooltips().isEmpty() ? this.getElement().getTooltips() : (List) this.getScreen().getTile().getComponentManager().getComponent((MachineComponentType) Registration.ENERGY_MACHINE_COMPONENT.get()).map(component -> Collections.singletonList(Component.translatable("custommachinery.gui.element.energy.tooltip", Utils.format(component.getEnergy()), PlatformHelper.energy().unit(), Utils.format(component.getCapacity()), PlatformHelper.energy().unit()))).orElse(Collections.emptyList());
    }
}