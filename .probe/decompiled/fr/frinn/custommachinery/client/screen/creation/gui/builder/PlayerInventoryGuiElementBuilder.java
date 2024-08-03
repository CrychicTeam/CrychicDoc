package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.guielement.PlayerInventoryGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class PlayerInventoryGuiElementBuilder implements IGuiElementBuilder<PlayerInventoryGuiElement> {

    @Override
    public GuiElementType<PlayerInventoryGuiElement> type() {
        return (GuiElementType<PlayerInventoryGuiElement>) Registration.PLAYER_INVENTORY_GUI_ELEMENT.get();
    }

    public PlayerInventoryGuiElement make(AbstractGuiElement.Properties properties, @Nullable PlayerInventoryGuiElement from) {
        return new PlayerInventoryGuiElement(properties);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable PlayerInventoryGuiElement from, Consumer<PlayerInventoryGuiElement> onFinish) {
        return new PlayerInventoryGuiElementBuilder.PlayerInventoryGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class PlayerInventoryGuiElementBuilderPopup extends GuiElementBuilderPopup<PlayerInventoryGuiElement> {

        public PlayerInventoryGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable PlayerInventoryGuiElement from, Consumer<PlayerInventoryGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public PlayerInventoryGuiElement makeElement() {
            return new PlayerInventoryGuiElement(this.properties.build());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : PlayerInventoryGuiElement.BASE_TEXTURE);
            this.addPriority(row);
        }
    }
}