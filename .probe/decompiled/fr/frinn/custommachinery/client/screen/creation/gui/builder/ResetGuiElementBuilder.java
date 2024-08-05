package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.guielement.ResetGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ResetGuiElementBuilder implements IGuiElementBuilder<ResetGuiElement> {

    @Override
    public GuiElementType<ResetGuiElement> type() {
        return (GuiElementType<ResetGuiElement>) Registration.RESET_GUI_ELEMENT.get();
    }

    public ResetGuiElement make(AbstractGuiElement.Properties properties, @Nullable ResetGuiElement from) {
        return new ResetGuiElement(properties);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable ResetGuiElement from, Consumer<ResetGuiElement> onFinish) {
        return new ResetGuiElementBuilder.ResetGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class ResetGuiElementBuilderPopup extends GuiElementBuilderPopup<ResetGuiElement> {

        public ResetGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable ResetGuiElement from, Consumer<ResetGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public ResetGuiElement makeElement() {
            return new ResetGuiElement(this.properties.build());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : ResetGuiElement.BASE_TEXTURE);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture_hovered"), this.properties::setTextureHovered, this.baseElement != null ? this.baseElement.getTextureHovered() : ResetGuiElement.BASE_TEXTURE_HOVERED);
            this.addPriority(row);
        }
    }
}