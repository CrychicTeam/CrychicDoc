package fr.frinn.custommachinery.client.element;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.api.machine.MachineStatus;
import fr.frinn.custommachinery.common.guielement.StatusGuiElement;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class StatusGuiElementWidget extends AbstractGuiElementWidget<StatusGuiElement> {

    public StatusGuiElementWidget(StatusGuiElement element, IMachineScreen screen) {
        super(element, screen, Component.literal("Status"));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        graphics.blit(switch(this.getScreen().getTile().getStatus()) {
            case RUNNING ->
                this.getElement().getRunningTexture();
            case ERRORED ->
                this.getElement().getErroredTexture();
            default ->
                this.getElement().getIdleTexture();
        }, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
    }

    @Override
    public List<Component> getTooltips() {
        if (!this.getElement().getTooltips().isEmpty()) {
            return this.getElement().getTooltips();
        } else {
            List<Component> tooltips = new ArrayList();
            tooltips.add(Component.translatable("custommachinery.craftingstatus." + this.getScreen().getTile().getStatus().toString().toLowerCase(Locale.ENGLISH)));
            if (this.getScreen().getTile().getStatus() == MachineStatus.ERRORED) {
                tooltips.add(this.getScreen().getTile().getMessage());
            }
            return tooltips;
        }
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return false;
    }
}