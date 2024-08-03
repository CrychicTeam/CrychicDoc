package fr.frinn.custommachinery.client.screen.creation.component.builder;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.component.FluidMachineComponent;
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

public class FluidComponentBuilder implements IMachineComponentBuilder<FluidMachineComponent, FluidMachineComponent.Template> {

    @Override
    public MachineComponentType<FluidMachineComponent> type() {
        return (MachineComponentType<FluidMachineComponent>) Registration.FLUID_MACHINE_COMPONENT.get();
    }

    public PopupScreen makePopup(MachineEditScreen parent, @Nullable FluidMachineComponent.Template template, Consumer<FluidMachineComponent.Template> onFinish) {
        return new FluidComponentBuilder.FluidComponentBuilderPopup(parent, template, onFinish);
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height, FluidMachineComponent.Template template) {
        graphics.renderFakeItem(Items.WATER_BUCKET.getDefaultInstance(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
        graphics.drawString(Minecraft.getInstance().font, "id: \"" + template.getId() + "\"", x + 25, y + 15, FastColor.ARGB32.color(255, 128, 0, 0), false);
        graphics.drawString(Minecraft.getInstance().font, "mode: " + template.mode(), x + 25, y + 25, FastColor.ARGB32.color(255, 0, 0, 128), false);
    }

    public static class FluidComponentBuilderPopup extends ComponentBuilderPopup<FluidMachineComponent.Template> {

        private EditBox id;

        private CycleButton<ComponentIOMode> mode;

        private EditBox capacity;

        private EditBox maxInput;

        private EditBox maxOutput;

        private Checkbox unique;

        public FluidComponentBuilderPopup(BaseScreen parent, @Nullable FluidMachineComponent.Template template, Consumer<FluidMachineComponent.Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinery.gui.creation.components.fluid.title"));
        }

        public FluidMachineComponent.Template makeTemplate() {
            return new FluidMachineComponent.Template(this.id.getValue(), this.parseLong(this.capacity.getValue()), this.parseLong(this.maxInput.getValue()), this.parseLong(this.maxOutput.getValue()), Collections.emptyList(), false, this.mode.getValue(), this.mode.getValue().getBaseConfig(), this.unique.selected());
        }

        @Override
        protected void init() {
            super.init();
            this.id = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.id"), new EditBox(Minecraft.getInstance().font, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.id")));
            this.baseTemplate().ifPresentOrElse(template -> this.id.setValue(template.getId()), () -> this.id.setValue("input"));
            this.id.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.components.id.tooltip")));
            this.mode = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.mode"), CycleButton.<ComponentIOMode>builder(ComponentIOMode::toComponent).displayOnlyValue().withValues(ComponentIOMode.values()).withInitialValue(ComponentIOMode.BOTH).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.mode")));
            this.baseTemplate().ifPresent(template -> this.mode.setValue(template.mode()));
            this.capacity = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.capacity"), new EditBox(this.f_96547_, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.capacity")));
            this.capacity.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.capacity.setValue(template.capacity() + ""), () -> this.capacity.setValue("10000"));
            this.maxInput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxInput"), new EditBox(this.f_96547_, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxInput")));
            this.maxInput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxInput.setValue(template.maxInput() + ""), () -> this.maxInput.setValue("10000"));
            this.maxOutput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxOutput"), new EditBox(this.f_96547_, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxOutput")));
            this.maxOutput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxOutput.setValue(template.maxOutput() + ""), () -> this.maxOutput.setValue("10000"));
            this.unique = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.fluid.unique"), new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.components.fluid.unique"), false));
            if ((Boolean) this.baseTemplate().map(FluidMachineComponent.Template::unique).orElse(false) != this.unique.selected()) {
                this.unique.onPress();
            }
        }
    }
}