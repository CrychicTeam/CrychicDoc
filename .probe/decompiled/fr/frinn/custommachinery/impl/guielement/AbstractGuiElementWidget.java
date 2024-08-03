package fr.frinn.custommachinery.impl.guielement;

import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public abstract class AbstractGuiElementWidget<T extends IGuiElement> extends AbstractWidget {

    private final T element;

    private final IMachineScreen screen;

    public AbstractGuiElementWidget(T element, IMachineScreen screen, Component title) {
        super(element.getX() + screen.getX(), element.getY() + screen.getY(), element.getWidth(), element.getHeight(), title);
        this.element = element;
        this.screen = screen;
    }

    public List<Component> getTooltips() {
        return this.element.getTooltips();
    }

    public T getElement() {
        return this.element;
    }

    public IMachineScreen getScreen() {
        return this.screen;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (this.f_93622_) {
            this.updateTooltips();
        }
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.HINT, (Component[]) this.getTooltips().toArray(new Component[0]));
    }

    private void updateTooltips() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen != null) {
            if (this.m_278622_() != null) {
                screen.setTooltipForNextRenderPass(this.m_278622_(), this.createTooltipPositioner(), this.m_93696_());
            } else {
                if (!this.getTooltips().isEmpty()) {
                    List<FormattedCharSequence> tooltips = this.getTooltips().stream().flatMap(component -> Tooltip.splitTooltip(Minecraft.getInstance(), component).stream()).toList();
                    screen.setTooltipForNextRenderPass(tooltips, this.createTooltipPositioner(), this.m_93696_());
                }
            }
        }
    }

    public void setHeight(int height) {
        this.f_93619_ = height;
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }
}