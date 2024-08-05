package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.ComponentEditBox;
import fr.frinn.custommachinery.client.screen.widget.IntegerSlider;
import fr.frinn.custommachinery.client.screen.widget.SuggestedEditBox;
import fr.frinn.custommachinery.common.guielement.ButtonGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.function.Consumer;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ButtonGuiElementBuilder implements IGuiElementBuilder<ButtonGuiElement> {

    @Override
    public GuiElementType<ButtonGuiElement> type() {
        return (GuiElementType<ButtonGuiElement>) Registration.BUTTON_GUI_ELEMENT.get();
    }

    public ButtonGuiElement make(AbstractGuiElement.Properties properties, @Nullable ButtonGuiElement from) {
        return from != null ? new ButtonGuiElement(properties, from.getTextureToggle(), from.getTextureToggleHovered(), from.isToggle(), from.getText(), from.getItem(), from.getHoldTime()) : new ButtonGuiElement(properties, ButtonGuiElement.BASE_TEXTURE_TOGGLE, ButtonGuiElement.BASE_TEXTURE_TOGGLE_HOVERED, false, Component.literal(""), ItemStack.EMPTY, 1);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, @Nullable ButtonGuiElement from, Consumer<ButtonGuiElement> onFinish) {
        return new ButtonGuiElementBuilder.ButtonGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class ButtonGuiElementBuilderPopup extends GuiElementBuilderPopup<ButtonGuiElement> {

        private ResourceLocation textureToggle = ButtonGuiElement.BASE_TEXTURE_TOGGLE;

        private ResourceLocation textureToggleHovered = ButtonGuiElement.BASE_TEXTURE_TOGGLE_HOVERED;

        private Checkbox toggle;

        private ComponentEditBox text;

        private SuggestedEditBox item;

        private IntegerSlider holdTime;

        public ButtonGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable ButtonGuiElement from, Consumer<ButtonGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
            if (from != null) {
                this.textureToggle = from.getTextureToggle();
                this.textureToggleHovered = from.getTextureToggleHovered();
            }
        }

        private ItemStack getItem() {
            try {
                return BuiltInRegistries.ITEM.get(new ResourceLocation(this.item.m_94155_())).getDefaultInstance();
            } catch (NullPointerException | ResourceLocationException var2) {
                return ItemStack.EMPTY;
            }
        }

        public ButtonGuiElement makeElement() {
            return new ButtonGuiElement(this.properties.build(), this.textureToggle, this.textureToggleHovered, this.toggle.selected(), this.text.getComponent(), this.getItem(), this.holdTime.intValue());
        }

        @Override
        public Component canCreate() {
            return (Component) (this.properties.getId().isEmpty() ? Component.translatable("custommachinery.gui.creation.gui.id.missing") : super.canCreate());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement != null ? this.baseElement.getTexture() : ButtonGuiElement.BASE_TEXTURE);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture_hovered"), this.properties::setTextureHovered, this.baseElement != null ? this.baseElement.getTextureHovered() : ButtonGuiElement.BASE_TEXTURE_HOVERED);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.button.texture_toggle"), texture -> this.textureToggle = texture, this.textureToggle);
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.button.texture_toggle_hovered"), texture -> this.textureToggleHovered = texture, this.textureToggleHovered);
            this.addId(row);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.button.toggle"), this.f_96547_));
            this.toggle = row.addChild(new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.gui.button.toggle"), this.baseElement != null && this.baseElement.isToggle()));
            this.toggle.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.button.toggle.tooltip")));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.button.text"), this.f_96547_));
            this.text = row.addChild(new ComponentEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.button.text")));
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.button.item"), this.f_96547_));
            this.item = row.addChild(new SuggestedEditBox(this.f_96547_, 0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.button.item"), 5));
            this.item.m_94153_(s -> ResourceLocation.tryParse(s) != null);
            this.item.addSuggestions(BuiltInRegistries.ITEM.m_6566_().stream().map(ResourceLocation::toString).toList());
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.button.hold_time"), this.f_96547_));
            this.holdTime = row.addChild(IntegerSlider.builder().bounds(1, 40).defaultValue(1).displayOnlyValue().create(0, 0, 100, 20, Component.translatable("custommachinery.gui.creation.gui.button.hold_time")));
            this.holdTime.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.button.hold_time.tooltip")));
            if (this.baseElement != null) {
                this.text.setComponent(this.baseElement.getText());
                this.item.m_94144_(BuiltInRegistries.ITEM.getKey(this.baseElement.getItem().getItem()).toString());
                this.holdTime.setValue(this.baseElement.getHoldTime());
            }
        }
    }
}