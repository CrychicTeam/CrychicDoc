package fr.frinn.custommachinery.client.screen.creation.tabs;

import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.BackgroundEditorPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiEditorWidget;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementCreationPopup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;

public class GuiTab extends MachineEditTab {

    private final GuiEditorWidget guiEditor;

    private GuiTab.AddGuiElementButton addButton;

    private ImageButton backgroundButton;

    public GuiTab(MachineEditScreen parent) {
        super(Component.translatable("custommachinery.gui.creation.tab.gui"), parent);
        GridLayout.RowHelper row = this.f_267367_.createRowHelper(1);
        row.addChild(new StringWidget(parent.f_96543_, 0, Component.empty(), Minecraft.getInstance().font));
        this.guiEditor = row.addChild(new GuiEditorWidget(parent, parent.x, parent.y, 256, 192, parent.getBuilder().getGuiElements()), row.newCellSettings().alignHorizontallyCenter());
    }

    @Override
    public void opened() {
        this.addButton = this.parent.addRenderableWidget(new GuiTab.AddGuiElementButton(this.parent.x - 28, this.parent.y + 85, button -> this.create()));
        this.backgroundButton = this.parent.addRenderableWidget(new ImageButton(this.parent.x - 28, this.parent.y + 110, 20, 20, 80, 0, MachineEditScreen.WIDGETS, button -> this.background()));
        this.backgroundButton.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.background")));
    }

    @Override
    public void closed() {
        this.parent.removeWidget(this.addButton);
        this.parent.removeWidget(this.backgroundButton);
    }

    private void create() {
        this.parent.openPopup(new GuiElementCreationPopup(this.parent, this.guiEditor::addCreatedElement));
    }

    private void background() {
        this.parent.openPopup(new BackgroundEditorPopup(this.parent));
    }

    public static class AddGuiElementButton extends ImageButton {

        public AddGuiElementButton(int x, int y, Button.OnPress onPress) {
            super(x, y, 20, 20, 60, 0, MachineEditScreen.WIDGETS, onPress);
            this.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.add")));
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            BaseScreen.blankBackground(graphics, this.m_252754_() - 5, this.m_252907_() - 5, this.m_5711_() + 10, this.m_93694_() + 35);
            super.renderWidget(graphics, mouseX, mouseY, partialTick);
        }
    }
}