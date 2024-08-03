package fr.frinn.custommachinery.client.element;

import dev.architectury.hooks.fluid.FluidStackHooks;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.client.ClientHandler;
import fr.frinn.custommachinery.client.render.FluidRenderer;
import fr.frinn.custommachinery.common.guielement.FluidGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.impl.guielement.TexturedGuiElementWidget;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FluidGuiElementWidget extends TexturedGuiElementWidget<FluidGuiElement> {

    public FluidGuiElementWidget(FluidGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Fluid"));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        this.getScreen().getTile().getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(fluidHandler -> fluidHandler.getComponentForID(this.getElement().getComponentId())).ifPresent(component -> FluidRenderer.renderFluid(graphics.pose(), this.m_252754_() + 1, this.m_252907_() + 1, this.f_93618_ - 2, this.f_93619_ - 2, component.getFluidStack(), component.getCapacity()));
        if (this.m_274382_() && this.getElement().highlight()) {
            ClientHandler.renderSlotHighlight(graphics, this.m_252754_() + 1, this.m_252907_() + 1, this.f_93618_ - 2, this.f_93619_ - 2);
        }
    }

    @Override
    public List<Component> getTooltips() {
        return !this.getElement().getTooltips().isEmpty() ? this.getElement().getTooltips() : (List) this.getScreen().getTile().getComponentManager().getComponentHandler((MachineComponentType) Registration.FLUID_MACHINE_COMPONENT.get()).flatMap(fluidHandler -> fluidHandler.getComponentForID(this.getElement().getComponentId())).map(component -> {
            long amount = component.getFluidStack().getAmount() * 1000L / FluidStackHooks.bucketAmount();
            long capacity = component.getCapacity() * 1000L / FluidStackHooks.bucketAmount();
            Component tooltip;
            if (!component.getFluidStack().isEmpty() && amount > 0L) {
                tooltip = Component.translatable(component.getFluidStack().getTranslationKey()).append(Component.translatable("custommachinery.gui.element.fluid.tooltip", Utils.format(amount), Utils.format(capacity)));
            } else {
                tooltip = Component.translatable("custommachinery.gui.element.fluid.empty", 0, Utils.format(capacity));
            }
            return Collections.singletonList(tooltip);
        }).orElse(Collections.emptyList());
    }
}