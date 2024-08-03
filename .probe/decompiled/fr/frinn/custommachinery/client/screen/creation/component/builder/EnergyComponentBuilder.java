package fr.frinn.custommachinery.client.screen.creation.component.builder;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.component.config.SideConfig;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class EnergyComponentBuilder implements IMachineComponentBuilder<EnergyMachineComponent, EnergyMachineComponent.Template> {

    @Override
    public MachineComponentType<EnergyMachineComponent> type() {
        return (MachineComponentType<EnergyMachineComponent>) Registration.ENERGY_MACHINE_COMPONENT.get();
    }

    public PopupScreen makePopup(MachineEditScreen parent, @Nullable EnergyMachineComponent.Template template, Consumer<EnergyMachineComponent.Template> onFinish) {
        return new EnergyComponentBuilder.EnergyComponentBuilderPopup(parent, template, onFinish);
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height, EnergyMachineComponent.Template template) {
        graphics.renderFakeItem(Items.LANTERN.getDefaultInstance(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
    }

    public static class EnergyComponentBuilderPopup extends ComponentBuilderPopup<EnergyMachineComponent.Template> {

        private EditBox capacity;

        private EditBox maxInput;

        private EditBox maxOutput;

        public EnergyComponentBuilderPopup(BaseScreen parent, @Nullable EnergyMachineComponent.Template template, Consumer<EnergyMachineComponent.Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinery.gui.creation.components.energy.title"));
        }

        public EnergyMachineComponent.Template makeTemplate() {
            return new EnergyMachineComponent.Template(this.parseLong(this.capacity.getValue()), this.parseLong(this.maxInput.getValue()), this.parseLong(this.maxOutput.getValue()), SideConfig.Template.DEFAULT_ALL_INPUT);
        }

        @Override
        protected void init() {
            super.init();
            this.capacity = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.capacity"), new EditBox(this.f_96547_, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.capacity")));
            this.capacity.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.capacity.setValue(template.capacity() + ""), () -> this.capacity.setValue("10000"));
            this.maxInput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxInput"), new EditBox(this.f_96547_, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxInput")));
            this.maxInput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxInput.setValue(template.maxInput() + ""), () -> this.maxInput.setValue("10000"));
            this.maxOutput = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.maxOutput"), new EditBox(this.f_96547_, 0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.maxOutput")));
            this.maxOutput.setFilter(this::checkLong);
            this.baseTemplate().ifPresentOrElse(template -> this.maxOutput.setValue(template.maxOutput() + ""), () -> this.maxOutput.setValue("10000"));
        }
    }
}