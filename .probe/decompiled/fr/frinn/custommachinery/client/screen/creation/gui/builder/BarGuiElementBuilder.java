package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.IntegerEditBox;
import fr.frinn.custommachinery.common.guielement.BarGuiElement;
import fr.frinn.custommachinery.common.guielement.ProgressBarGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class BarGuiElementBuilder implements IGuiElementBuilder<BarGuiElement> {

    @Override
    public GuiElementType<BarGuiElement> type() {
        return (GuiElementType<BarGuiElement>) Registration.BAR_GUI_ELEMENT.get();
    }

    public BarGuiElement make(AbstractGuiElement.Properties properties, @Nullable BarGuiElement from) {
        return from != null ? new BarGuiElement(properties, from.getMin(), from.getMax(), from.isHighlight(), from.getOrientation(), from.getEmptyTexture(), from.getFilledTexture()) : new BarGuiElement(properties, 0, 1000, true, ProgressBarGuiElement.Orientation.TOP, BarGuiElement.BASE_EMPTY_TEXTURE, BarGuiElement.BASE_FILLED_TEXTURE);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable BarGuiElement from, Consumer<BarGuiElement> onFinish) {
        return new BarGuiElementBuilder.BarGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class BarGuiElementBuilderPopup extends GuiElementBuilderPopup<BarGuiElement> {

        private IntegerEditBox min;

        private IntegerEditBox max;

        private Checkbox highlight;

        private CycleButton<ProgressBarGuiElement.Orientation> orientation;

        private ResourceLocation emptyTexture = BarGuiElement.BASE_EMPTY_TEXTURE;

        private ResourceLocation filledTexture = BarGuiElement.BASE_FILLED_TEXTURE;

        public BarGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable BarGuiElement from, Consumer<BarGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
            if (from != null) {
                this.emptyTexture = from.getEmptyTexture();
                this.filledTexture = from.getFilledTexture();
            }
        }

        public BarGuiElement makeElement() {
            return new BarGuiElement(this.properties.build(), this.min.getIntValue(), this.max.getIntValue(), this.highlight.selected(), this.orientation.getValue(), this.emptyTexture, this.filledTexture);
        }

        @Override
        public Component canCreate() {
            return (Component) (this.properties.getId().isEmpty() ? Component.translatable("custommachinery.gui.creation.gui.id.missing") : super.canCreate());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.empty"), texture -> this.emptyTexture = texture, this.emptyTexture);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.filled"), texture -> this.filledTexture = texture, this.filledTexture);
            this.addId(row);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.bar.min"), this.f_96547_));
            this.min = row.addChild(new IntegerEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.bar.min")));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.bar.max"), this.f_96547_));
            this.max = row.addChild(new IntegerEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.bar.max")));
            if (this.baseElement != null) {
                this.min.m_94144_(this.baseElement.getMin() + "");
                this.max.m_94144_(this.baseElement.getMax() + "");
            }
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.highlight"), this.f_96547_));
            this.highlight = row.addChild(new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.gui.highlight"), this.baseElement == null || this.baseElement.isHighlight()));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.bar.orientation"), this.f_96547_));
            this.orientation = row.addChild(CycleButton.<ProgressBarGuiElement.Orientation>builder(orientation -> Component.literal(orientation.name())).withValues(ProgressBarGuiElement.Orientation.values()).withInitialValue(this.baseElement != null ? this.baseElement.getOrientation() : ProgressBarGuiElement.Orientation.TOP).displayOnlyValue().create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.bar.orientation")));
        }
    }
}