package fr.frinn.custommachinery.client.screen;

import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.guielement.BackgroundGuiElement;
import fr.frinn.custommachinery.common.init.CustomMachineContainer;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import fr.frinn.custommachinery.common.network.CGuiElementClickPacket;
import fr.frinn.custommachinery.common.util.Comparators;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElementWidget;
import fr.frinn.custommachinery.impl.guielement.GuiElementWidgetSupplierRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class CustomMachineScreen extends AbstractContainerScreen<CustomMachineContainer> implements IMachineScreen {

    private final CustomMachineTile tile;

    private final CustomMachine machine;

    @Nullable
    private final BackgroundGuiElement background;

    private final List<AbstractGuiElementWidget<?>> elementWidgets = new ArrayList();

    public CustomMachineScreen(CustomMachineContainer container, Inventory inv, Component name) {
        super(container, inv, name);
        this.tile = container.getTile();
        this.machine = container.getTile().getMachine();
        this.f_97726_ = 256;
        this.f_97727_ = 192;
        this.background = (BackgroundGuiElement) this.tile.getGuiElements().stream().filter(element -> element instanceof BackgroundGuiElement).map(element -> (BackgroundGuiElement) element).findFirst().orElse(null);
        if (this.background != null) {
            this.f_97726_ = this.background.getWidth();
            this.f_97727_ = this.background.getHeight();
        }
    }

    @Override
    protected void init() {
        this.f_97735_ = (this.f_96543_ - this.f_97726_) / 2;
        this.f_97736_ = (this.f_96544_ - this.f_97727_) / 2;
        this.elementWidgets.clear();
        this.tile.getGuiElements().stream().filter(element -> GuiElementWidgetSupplierRegistry.hasWidgetSupplier(element.getType())).sorted(Comparators.GUI_ELEMENTS_COMPARATOR.reversed()).forEach(element -> this.addElementWidget(GuiElementWidgetSupplierRegistry.getWidgetSupplier(element.getType()).get(element, this)));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        this.m_280273_(graphics);
        if (this.background != null && this.background.getTexture() != null) {
            graphics.blit(this.background.getTexture(), this.f_97735_, this.f_97736_, 0.0F, 0.0F, this.f_97726_, this.f_97727_, this.f_97726_, this.f_97727_);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        this.m_280072_(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    @Override
    public int getX() {
        return this.f_97735_;
    }

    @Override
    public int getY() {
        return this.f_97736_;
    }

    @Override
    public int getWidth() {
        return this.f_97726_;
    }

    @Override
    public int getHeight() {
        return this.f_97727_;
    }

    public CustomMachine getMachine() {
        return this.machine;
    }

    public CustomMachineTile getTile() {
        return this.tile;
    }

    public Optional<AbstractGuiElementWidget<?>> getElementUnderMouse(double mouseX, double mouseY) {
        for (GuiEventListener widget : this.m_6702_()) {
            if (widget instanceof AbstractGuiElementWidget<?> elementWidget && elementWidget.m_5953_(mouseX, mouseY)) {
                return Optional.of(elementWidget);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (AbstractGuiElementWidget<?> elementWidget : this.elementWidgets) {
            if (elementWidget.m_6375_(mouseX, mouseY, button)) {
                new CGuiElementClickPacket(this.tile.getGuiElements().indexOf(elementWidget.getElement()), (byte) button).sendToServer();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void addElementWidget(AbstractGuiElementWidget<?> widget) {
        this.elementWidgets.add(widget);
        this.m_142416_(widget);
    }
}