package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.guielement.ExperienceGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ExperienceGuiElementBuilder implements IGuiElementBuilder<ExperienceGuiElement> {

    @Override
    public GuiElementType<ExperienceGuiElement> type() {
        return (GuiElementType<ExperienceGuiElement>) Registration.EXPERIENCE_GUI_ELEMENT.get();
    }

    public ExperienceGuiElement make(AbstractGuiElement.Properties properties, @Nullable ExperienceGuiElement from) {
        return from != null ? new ExperienceGuiElement(properties, from.getDisplayMode(), from.getMode()) : new ExperienceGuiElement(properties, ExperienceGuiElement.DisplayMode.LEVEL, ExperienceGuiElement.Mode.DISPLAY_BAR);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable ExperienceGuiElement from, Consumer<ExperienceGuiElement> onFinish) {
        return new ExperienceGuiElementBuilder.ExperienceGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class ExperienceGuiElementBuilderPopup extends GuiElementBuilderPopup<ExperienceGuiElement> {

        private CycleButton<ExperienceGuiElement.DisplayMode> displayMode;

        private CycleButton<ExperienceGuiElement.Mode> mode;

        public ExperienceGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable ExperienceGuiElement from, Consumer<ExperienceGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public ExperienceGuiElement makeElement() {
            return new ExperienceGuiElement(this.properties.build(), this.displayMode.getValue(), this.mode.getValue());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : ExperienceGuiElement.BASE_TEXTURE);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture_hovered"), this.properties::setTextureHovered, this.baseElement != null ? this.baseElement.getTextureHovered() : ExperienceGuiElement.BASE_TEXTURE_HOVERED);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.experience.display_mode"), this.f_96547_));
            this.displayMode = row.addChild(CycleButton.<ExperienceGuiElement.DisplayMode>builder(display -> Component.literal(display.name())).withValues(ExperienceGuiElement.DisplayMode.values()).withInitialValue(this.baseElement != null ? this.baseElement.getDisplayMode() : ExperienceGuiElement.DisplayMode.LEVEL).displayOnlyValue().create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.experience.display_mode")));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.experience.mode"), this.f_96547_));
            this.mode = row.addChild(CycleButton.<ExperienceGuiElement.Mode>builder(mode -> Component.literal(mode.name())).withValues(ExperienceGuiElement.Mode.values()).withInitialValue(this.baseElement != null ? this.baseElement.getMode() : ExperienceGuiElement.Mode.DISPLAY_BAR).displayOnlyValue().create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.experience.mode")));
        }
    }
}