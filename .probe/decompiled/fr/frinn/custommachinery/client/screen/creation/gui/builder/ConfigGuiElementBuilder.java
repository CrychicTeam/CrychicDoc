package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.guielement.ConfigGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ConfigGuiElementBuilder implements IGuiElementBuilder<ConfigGuiElement> {

    @Override
    public GuiElementType<ConfigGuiElement> type() {
        return (GuiElementType<ConfigGuiElement>) Registration.CONFIG_GUI_ELEMENT.get();
    }

    public ConfigGuiElement make(AbstractGuiElement.Properties properties, @Nullable ConfigGuiElement from) {
        return new ConfigGuiElement(properties);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable ConfigGuiElement from, Consumer<ConfigGuiElement> onFinish) {
        return new ConfigGuiElementBuilder.ConfigGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class ConfigGuiElementBuilderPopup extends GuiElementBuilderPopup<ConfigGuiElement> {

        public ConfigGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable ConfigGuiElement from, Consumer<ConfigGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public ConfigGuiElement makeElement() {
            return new ConfigGuiElement(this.properties.build());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : ConfigGuiElement.BASE_TEXTURE);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture_hovered"), this.properties::setTextureHovered, this.baseElement != null ? this.baseElement.getTextureHovered() : ConfigGuiElement.BASE_TEXTURE_HOVERED);
            this.addPriority(row);
        }
    }
}