package fr.frinn.custommachinery.client.screen.creation.gui;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.ListWidget;
import fr.frinn.custommachinery.common.init.Registration;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;

public class GuiElementCreationPopup extends PopupScreen {

    public static final Component CONFIRM = Component.translatable("custommachinery.gui.popup.confirm").withStyle(ChatFormatting.GREEN);

    public static final Component CANCEL = Component.translatable("custommachinery.gui.popup.cancel").withStyle(ChatFormatting.RED);

    private final Consumer<IGuiElement> onChange;

    private GuiElementCreationPopup.GuiElementCreationListWidget list;

    public GuiElementCreationPopup(MachineEditScreen parent, Consumer<IGuiElement> onChange) {
        super(parent, 116, 144);
        this.onChange = onChange;
    }

    private void confirm() {
        GuiElementCreationPopup.GuiElementCreationListWidget.GuiElementCreationListEntry entry = this.list.getSelected();
        if (entry != null && this.parent instanceof MachineEditScreen editScreen) {
            PopupScreen componentCreationPopup = entry.builder.makeConfigPopup(editScreen, new MutableProperties(), null, element -> {
                editScreen.getBuilder().getGuiElements().add(element);
                editScreen.setChanged();
                this.onChange.accept(element);
            });
            editScreen.closePopup(this);
            editScreen.openPopup(componentCreationPopup);
        }
    }

    private void cancel() {
        this.parent.closePopup(this);
    }

    @Override
    protected void init() {
        super.init();
        Component title = Component.translatable("custommachinery.gui.creation.components.create.title");
        this.m_142416_(new StringWidget(this.x, this.y + 5, this.xSize, 9, title, Minecraft.getInstance().font));
        this.list = (GuiElementCreationPopup.GuiElementCreationListWidget) this.m_142416_(new GuiElementCreationPopup.GuiElementCreationListWidget(this.x + 3, this.y + 15, this.xSize - 10, this.ySize - 50));
        this.m_142416_(Button.builder(CONFIRM, b -> this.confirm()).bounds(this.x + 5, this.y + this.ySize - 30, 50, 20).build());
        this.m_142416_(Button.builder(CANCEL, b -> this.cancel()).bounds(this.x + this.xSize - 55, this.y + this.ySize - 30, 50, 20).build());
    }

    protected static class GuiElementCreationListWidget extends ListWidget<GuiElementCreationPopup.GuiElementCreationListWidget.GuiElementCreationListEntry> {

        public GuiElementCreationListWidget(int x, int y, int width, int height) {
            super(x, y, width, height, 20, Component.empty());
            this.setRenderSelection();
            for (GuiElementType<?> type : Registration.GUI_ELEMENT_TYPE_REGISTRY.entrySet().stream().sorted(Comparator.comparing(entry -> ((ResourceKey) entry.getKey()).location())).map(java.util.Map.Entry::getValue).toList()) {
                IGuiElementBuilder<?> builder = GuiElementBuilderRegistry.getBuilder(type);
                if (builder != null) {
                    this.addEntry(new GuiElementCreationPopup.GuiElementCreationListWidget.GuiElementCreationListEntry(builder));
                }
            }
            this.setSelected(this.getEntries().isEmpty() ? null : (GuiElementCreationPopup.GuiElementCreationListWidget.GuiElementCreationListEntry) this.getEntries().get(0));
        }

        protected static class GuiElementCreationListEntry extends ListWidget.Entry {

            private final IGuiElementBuilder<?> builder;

            private GuiElementCreationListEntry(IGuiElementBuilder<?> builder) {
                this.builder = builder;
            }

            @Override
            public void render(GuiGraphics graphics, int index, int x, int y, int width, int height, int mouseX, int mouseY, float partialTick) {
                graphics.drawString(Minecraft.getInstance().font, this.builder.type().getTranslatedName(), x + 5, y + 5, 0, false);
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return Collections.emptyList();
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                return true;
            }
        }
    }
}