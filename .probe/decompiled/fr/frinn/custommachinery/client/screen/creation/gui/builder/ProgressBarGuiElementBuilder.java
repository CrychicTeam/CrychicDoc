package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.DoubleSlider;
import fr.frinn.custommachinery.common.guielement.ProgressBarGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ProgressBarGuiElementBuilder implements IGuiElementBuilder<ProgressBarGuiElement> {

    @Override
    public GuiElementType<ProgressBarGuiElement> type() {
        return (GuiElementType<ProgressBarGuiElement>) Registration.PROGRESS_GUI_ELEMENT.get();
    }

    public ProgressBarGuiElement make(AbstractGuiElement.Properties properties, @Nullable ProgressBarGuiElement from) {
        return from != null ? new ProgressBarGuiElement(properties, from.getEmptyTexture(), from.getFilledTexture(), from.getDirection(), from.getStart(), from.getEnd()) : new ProgressBarGuiElement(properties, ProgressBarGuiElement.BASE_EMPTY_TEXTURE, ProgressBarGuiElement.BASE_FILLED_TEXTURE, ProgressBarGuiElement.Orientation.RIGHT, 0.0F, 0.0F);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable ProgressBarGuiElement from, Consumer<ProgressBarGuiElement> onFinish) {
        return new ProgressBarGuiElementBuilder.ProgressBarGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class ProgressBarGuiElementBuilderPopup extends GuiElementBuilderPopup<ProgressBarGuiElement> {

        private ResourceLocation emptyTexture = ProgressBarGuiElement.BASE_EMPTY_TEXTURE;

        private ResourceLocation filledTexture = ProgressBarGuiElement.BASE_FILLED_TEXTURE;

        private CycleButton<ProgressBarGuiElement.Orientation> orientation;

        private float start = 0.0F;

        private float end = 1.0F;

        public ProgressBarGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable ProgressBarGuiElement from, Consumer<ProgressBarGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
            if (from != null) {
                this.emptyTexture = from.getEmptyTexture();
                this.filledTexture = from.getFilledTexture();
                this.start = from.getStart();
                this.end = from.getEnd();
            }
        }

        public ProgressBarGuiElement makeElement() {
            return new ProgressBarGuiElement(this.properties.build(), this.emptyTexture, this.filledTexture, this.orientation.getValue(), this.start, this.end);
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.empty"), texture -> this.emptyTexture = texture, this.emptyTexture);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.filled"), texture -> this.filledTexture = texture, this.filledTexture);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.progress.orientation"), this.f_96547_));
            this.orientation = row.addChild(CycleButton.<ProgressBarGuiElement.Orientation>builder(orientation -> Component.literal(orientation.toString())).withValues(ProgressBarGuiElement.Orientation.values()).withInitialValue(this.baseElement == null ? ProgressBarGuiElement.Orientation.RIGHT : this.baseElement.getDirection()).displayOnlyValue().create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.progress.orientation")));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.progress.start"), this.f_96547_));
            row.addChild(DoubleSlider.builder().bounds(-1.0, 1.0).defaultValue((double) this.start).displayOnlyValue().setResponder(value -> this.start = value.floatValue()).create(0, 0, 100, 20, Component.empty()));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.progress.end"), this.f_96547_));
            row.addChild(DoubleSlider.builder().bounds(0.0, 2.0).defaultValue((double) this.end).displayOnlyValue().setResponder(value -> this.end = value.floatValue()).create(0, 0, 100, 20, Component.empty()));
        }
    }
}