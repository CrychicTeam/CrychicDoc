package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.guielement.StatusGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class StatusGuiElementBuilder implements IGuiElementBuilder<StatusGuiElement> {

    @Override
    public GuiElementType<StatusGuiElement> type() {
        return (GuiElementType<StatusGuiElement>) Registration.STATUS_GUI_ELEMENT.get();
    }

    public StatusGuiElement make(AbstractGuiElement.Properties properties, @Nullable StatusGuiElement from) {
        return from != null ? new StatusGuiElement(properties, from.getIdleTexture(), from.getRunningTexture(), from.getErroredTexture()) : new StatusGuiElement(properties, StatusGuiElement.BASE_STATUS_IDLE_TEXTURE, StatusGuiElement.BASE_STATUS_RUNNING_TEXTURE, StatusGuiElement.BASE_STATUS_ERRORED_TEXTURE);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable StatusGuiElement from, Consumer<StatusGuiElement> onFinish) {
        return new StatusGuiElementBuilder.StatusGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class StatusGuiElementBuilderPopup extends GuiElementBuilderPopup<StatusGuiElement> {

        private ResourceLocation idleTexture = StatusGuiElement.BASE_STATUS_IDLE_TEXTURE;

        private ResourceLocation runningTexture = StatusGuiElement.BASE_STATUS_RUNNING_TEXTURE;

        private ResourceLocation erroredTexture = StatusGuiElement.BASE_STATUS_ERRORED_TEXTURE;

        public StatusGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable StatusGuiElement from, Consumer<StatusGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
            if (from != null) {
                this.idleTexture = from.getIdleTexture();
                this.runningTexture = from.getRunningTexture();
                this.erroredTexture = from.getErroredTexture();
            }
        }

        public StatusGuiElement makeElement() {
            return new StatusGuiElement(this.properties.build(), this.idleTexture, this.runningTexture, this.erroredTexture);
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.status.idle"), texture -> this.idleTexture = texture, this.idleTexture);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.status.running"), texture -> this.runningTexture = texture, this.runningTexture);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.status.errored"), texture -> this.erroredTexture = texture, this.erroredTexture);
            this.addPriority(row);
        }
    }
}