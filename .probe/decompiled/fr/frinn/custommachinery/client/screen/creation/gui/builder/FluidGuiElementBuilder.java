package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.guielement.FluidGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class FluidGuiElementBuilder implements IGuiElementBuilder<FluidGuiElement> {

    @Override
    public GuiElementType<FluidGuiElement> type() {
        return (GuiElementType<FluidGuiElement>) Registration.FLUID_GUI_ELEMENT.get();
    }

    public FluidGuiElement make(AbstractGuiElement.Properties properties, @Nullable FluidGuiElement from) {
        return from != null ? new FluidGuiElement(properties, properties.id(), from.highlight()) : new FluidGuiElement(properties, "", true);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable FluidGuiElement from, Consumer<FluidGuiElement> onFinish) {
        return new FluidGuiElementBuilder.FluidGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class FluidGuiElementBuilderPopup extends GuiElementBuilderPopup<FluidGuiElement> {

        private Checkbox highlight;

        public FluidGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable FluidGuiElement from, Consumer<FluidGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public FluidGuiElement makeElement() {
            return new FluidGuiElement(this.properties.build(), this.properties.getId(), this.highlight.selected());
        }

        @Override
        public Component canCreate() {
            return (Component) (this.properties.getId().isEmpty() ? Component.translatable("custommachinery.gui.creation.gui.id.missing") : super.canCreate());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : FluidGuiElement.BASE_TEXTURE);
            this.addId(row);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.highlight"), this.f_96547_));
            this.highlight = row.addChild(new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.gui.highlight"), this.baseElement == null || this.baseElement.highlight()));
        }
    }
}