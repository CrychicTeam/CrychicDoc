package fr.frinn.custommachinery.client.screen.creation.component.builder;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.IntegerSlider;
import fr.frinn.custommachinery.common.component.ItemMachineComponent;
import fr.frinn.custommachinery.common.component.variant.item.DefaultItemComponentVariant;
import fr.frinn.custommachinery.common.init.Registration;
import java.util.Collections;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ItemComponentBuilder implements IMachineComponentBuilder<ItemMachineComponent, ItemMachineComponent.Template> {

    @Override
    public MachineComponentType<ItemMachineComponent> type() {
        return (MachineComponentType<ItemMachineComponent>) Registration.ITEM_MACHINE_COMPONENT.get();
    }

    public PopupScreen makePopup(MachineEditScreen parent, @Nullable ItemMachineComponent.Template template, Consumer<ItemMachineComponent.Template> onFinish) {
        return new ItemComponentBuilder.ItemComponentBuilderPopup(parent, template, onFinish);
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height, ItemMachineComponent.Template template) {
        graphics.renderFakeItem(Items.DIAMOND.getDefaultInstance(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
        graphics.drawString(Minecraft.getInstance().font, "id: \"" + template.getId() + "\"", x + 25, y + 15, FastColor.ARGB32.color(255, 128, 0, 0), false);
        graphics.drawString(Minecraft.getInstance().font, "mode: " + template.mode(), x + 25, y + 25, FastColor.ARGB32.color(255, 0, 0, 128), false);
    }

    public static class ItemComponentBuilderPopup extends ComponentBuilderPopup<ItemMachineComponent.Template> {

        private EditBox id;

        private CycleButton<ComponentIOMode> mode;

        private IntegerSlider capacity;

        private IntegerSlider maxInput;

        private IntegerSlider maxOutput;

        private Checkbox locked;

        public ItemComponentBuilderPopup(BaseScreen parent, @Nullable ItemMachineComponent.Template template, Consumer<ItemMachineComponent.Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinery.gui.creation.components.item.title"));
        }

        public ItemMachineComponent.Template makeTemplate() {
            return new ItemMachineComponent.Template(this.id.getValue(), this.mode.getValue(), this.capacity.intValue(), this.maxInput.intValue(), this.maxOutput.intValue(), Collections.emptyList(), false, DefaultItemComponentVariant.INSTANCE, this.mode.getValue().getBaseConfig(), this.locked.selected());
        }

        @Override
        protected void init() {
            super.init();
            this.id = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.id"), new EditBox(Minecraft.getInstance().font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.id")));
            this.baseTemplate().ifPresentOrElse(template -> this.id.setValue(template.getId()), () -> this.id.setValue("input"));
            this.id.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.components.id.tooltip")));
            this.mode = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.mode"), CycleButton.<ComponentIOMode>builder(ComponentIOMode::toComponent).displayOnlyValue().withValues(ComponentIOMode.values()).withInitialValue(ComponentIOMode.BOTH).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.mode")));
            this.baseTemplate().ifPresent(template -> this.mode.setValue(template.mode()));
            this.capacity = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.capacity"), IntegerSlider.builder().bounds(0, 64).defaultValue(64).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.capacity")));
            this.baseTemplate().ifPresent(template -> this.capacity.setValue(template.capacity()));
            this.maxInput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxInput"), IntegerSlider.builder().bounds(0, 64).defaultValue(64).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxInput")));
            this.baseTemplate().ifPresent(template -> this.maxInput.setValue(template.maxInput()));
            this.maxOutput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxOutput"), IntegerSlider.builder().bounds(0, 64).defaultValue(64).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxOutput")));
            this.baseTemplate().ifPresent(template -> this.maxOutput.setValue(template.maxOutput()));
            this.locked = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.item.locked"), new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.components.item.locked"), false));
            if ((Boolean) this.baseTemplate().map(ItemMachineComponent.Template::locked).orElse(false) != this.locked.selected()) {
                this.locked.onPress();
            }
        }
    }
}