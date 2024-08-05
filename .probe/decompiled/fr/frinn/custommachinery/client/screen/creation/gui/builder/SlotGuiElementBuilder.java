package fr.frinn.custommachinery.client.screen.creation.gui.builder;

import fr.frinn.custommachinery.api.guielement.GuiElementType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.gui.GuiElementBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.gui.IGuiElementBuilder;
import fr.frinn.custommachinery.client.screen.creation.gui.MutableProperties;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.GroupWidget;
import fr.frinn.custommachinery.client.screen.widget.IntegerSlider;
import fr.frinn.custommachinery.client.screen.widget.SuggestedEditBox;
import fr.frinn.custommachinery.common.guielement.SlotGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.Color;
import fr.frinn.custommachinery.common.util.GhostItem;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.common.util.ingredient.ItemIngredient;
import fr.frinn.custommachinery.impl.guielement.AbstractGuiElement;
import java.util.Collections;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

public class SlotGuiElementBuilder implements IGuiElementBuilder<SlotGuiElement> {

    @Override
    public GuiElementType<SlotGuiElement> type() {
        return (GuiElementType<SlotGuiElement>) Registration.SLOT_GUI_ELEMENT.get();
    }

    public SlotGuiElement make(AbstractGuiElement.Properties properties, @Nullable SlotGuiElement from) {
        return from != null ? new SlotGuiElement(properties, from.getGhost()) : new SlotGuiElement(properties, GhostItem.EMPTY);
    }

    public PopupScreen makeConfigPopup(MachineEditScreen parent, MutableProperties properties, SlotGuiElement from, Consumer<SlotGuiElement> onFinish) {
        return new SlotGuiElementBuilder.SlotGuiElementBuilderPopup(parent, properties, from, onFinish);
    }

    public static class GhostItemWidget extends GroupWidget {

        private static final ResourceLocation WIDGETS = new ResourceLocation("custommachinery", "textures/gui/creation/style_widget.png");

        private static final ResourceLocation SLOT_TEXTURE = new ResourceLocation("custommachinery", "textures/gui/base_slot.png");

        private final SuggestedEditBox items;

        private final Checkbox alwaysVisible;

        private final IntegerSlider transparency;

        private Color color = Color.WHITE;

        public GhostItemWidget() {
            super(0, 0, 100, 60, Component.empty());
            this.items = this.addWidget(new SuggestedEditBox(Minecraft.getInstance().font, 0, 0, 100, 20, Component.empty(), 5));
            this.items.addSuggestions(BuiltInRegistries.ITEM.m_6566_().stream().map(ResourceLocation::toString).toList());
            this.alwaysVisible = this.addWidget(new Checkbox(80, 22, 20, 20, Component.empty(), false));
            this.alwaysVisible.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.gui.slot.ghost.alwaysVisible")));
            this.transparency = this.addWidget(IntegerSlider.builder().displayOnlyValue().bounds(0, 100).defaultValue(100).setResponder(value -> this.color = Color.fromColors((int) ((float) value.intValue() * 2.55F), this.color.getRed(), this.color.getGreen(), this.color.getBlue())).create(0, 43, 100, 20, Component.translatable("custommachinery.gui.creation.gui.slot.ghost.transparency", 100)));
            for (int i = 0; i < 16; i++) {
                ChatFormatting format = ChatFormatting.getById(i);
                if (format != null) {
                    ImageButton button = new ImageButton(i % 8 * 10 - 1, (i < 8 ? 0 : 10) + 22, 10, 10, i * 10, 0, WIDGETS, b -> this.color = format.getColor() != null ? Color.fromARGB(this.color.getAlpha() << 24 | format.getColor()) : Color.WHITE);
                    this.addWidget(button);
                    button.m_257544_(Tooltip.create(Component.translatable(format.getName()).withStyle(format)));
                }
            }
        }

        public void setGhost(GhostItem ghost) {
            this.items.m_94144_(BuiltInRegistries.ITEM.getKey((Item) ((IIngredient) ghost.items().get(0)).getAll().get(0)).toString());
            this.items.hideSuggestions();
            if (ghost.alwaysRender() != this.alwaysVisible.selected()) {
                this.alwaysVisible.onPress();
            }
            this.transparency.setValue((int) ((float) ghost.color().getAlpha() / 255.0F * 100.0F));
            this.color = ghost.color();
        }

        public GhostItem getGhost() {
            try {
                return new GhostItem(Collections.singletonList(new ItemIngredient(BuiltInRegistries.ITEM.get(new ResourceLocation(this.items.m_94155_())))), this.color, this.alwaysVisible.selected());
            } catch (NullPointerException | ResourceLocationException var2) {
                return GhostItem.EMPTY;
            }
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            graphics.blit(SLOT_TEXTURE, this.m_252754_() - 20, this.m_252907_(), 18, 18, 0.0F, 0.0F, 18, 18, 18, 18);
            try {
                graphics.setColor((float) this.color.getRed() / 255.0F, (float) this.color.getGreen() / 255.0F, (float) this.color.getBlue() / 255.0F, (float) this.color.getAlpha() / 255.0F);
                graphics.renderItem(BuiltInRegistries.ITEM.get(new ResourceLocation(this.items.m_94155_())).getDefaultInstance(), this.m_252754_() - 19, this.m_252907_() + 1);
                graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            } catch (NullPointerException | ResourceLocationException var6) {
                System.out.println("NULL");
            }
            super.renderWidget(graphics, mouseX, mouseY, partialTick);
        }
    }

    public static class SlotGuiElementBuilderPopup extends GuiElementBuilderPopup<SlotGuiElement> {

        private SlotGuiElementBuilder.GhostItemWidget ghostItem;

        public SlotGuiElementBuilderPopup(BaseScreen parent, MutableProperties properties, @Nullable SlotGuiElement from, Consumer<SlotGuiElement> onFinish) {
            super(parent, properties, from, onFinish);
        }

        public SlotGuiElement makeElement() {
            return new SlotGuiElement(this.properties.build(), this.ghostItem.getGhost());
        }

        @Override
        public Component canCreate() {
            return (Component) (this.properties.getId().isEmpty() ? Component.translatable("custommachinery.gui.creation.gui.id.missing") : super.canCreate());
        }

        @Override
        public void addWidgets(GridLayout.RowHelper row) {
            this.addTexture(row, Component.translatable("custommachinery.gui.creation.gui.texture"), this.properties::setTexture, this.baseElement == null ? SlotGuiElement.BASE_TEXTURE : this.baseElement.getTexture());
            this.addId(row);
            this.addPriority(row);
            row.addChild(new StringWidget(Component.translatable("custommachinery.gui.creation.gui.slot.ghost"), this.f_96547_));
            this.ghostItem = row.addChild(new SlotGuiElementBuilder.GhostItemWidget());
            if (this.baseElement != null && this.baseElement.getGhost() != GhostItem.EMPTY) {
                this.ghostItem.setGhost(this.baseElement.getGhost());
            }
        }
    }
}