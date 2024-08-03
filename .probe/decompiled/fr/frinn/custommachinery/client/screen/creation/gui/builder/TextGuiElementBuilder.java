package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.ComponentEditBox;
import fr.frinn.custommachinery.common.guielement.TextGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class TextGuiElementBuilder implements IGuiElementBuilder<TextGuiElement> {

    @Override
    public GuiElementType<TextGuiElement> type() {
        return (GuiElementType<TextGuiElement>) Registration.TEXT_GUI_ELEMENT.get();
    }

    public TextGuiElement make(AbstractGuiElement.Properties properties, @Nullable TextGuiElement from) {
        return from != null ? new TextGuiElement(properties, from.getText(), from.getAlignment(), from.showInJei()) : new TextGuiElement(properties, Component.translatable("custommachinery.gui.creation.gui.text.default"), TextGuiElement.Alignment.LEFT, false);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable TextGuiElement from, Consumer<TextGuiElement> onFinish) {
        return new TextGuiElementBuilder.TextGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class TextGuiElementBuilderPopup extends GuiElementBuilderPopup<TextGuiElement> {

        private ComponentEditBox text;

        private CycleButton<TextGuiElement.Alignment> alignment;

        private Checkbox showInJei;

        public TextGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable TextGuiElement from, Consumer<TextGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public TextGuiElement makeElement() {
            return new TextGuiElement(this.properties.build(), this.text.getComponent(), this.alignment.getValue(), this.showInJei.selected());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.text.text"), this.f_96547_));
            this.text = row.addChild(new ComponentEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.text.text")));
            if (this.baseElement != null) {
                this.text.setComponent(this.baseElement.getText());
            }
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.text.alignment"), this.f_96547_));
            this.alignment = row.addChild(CycleButton.<TextGuiElement.Alignment>builder(alignment -> Component.literal(alignment.toString())).withValues(TextGuiElement.Alignment.values()).withInitialValue(this.baseElement == null ? TextGuiElement.Alignment.LEFT : this.baseElement.getAlignment()).displayOnlyValue().create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.text.alignment")));
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.text.jei"), this.f_96547_));
            this.showInJei = row.addChild(new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.gui.text.jei"), this.baseElement != null && this.baseElement.showInJei()));
        }
    }
}