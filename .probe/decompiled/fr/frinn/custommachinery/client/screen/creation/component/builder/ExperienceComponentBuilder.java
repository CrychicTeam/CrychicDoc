package fr.frinn.custommachinery.client.screen.creation.component.builder;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.common.component.ExperienceMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ExperienceComponentBuilder implements IMachineComponentBuilder<ExperienceMachineComponent, ExperienceMachineComponent.Template> {

    @Override
    public MachineComponentType<ExperienceMachineComponent> type() {
        return (MachineComponentType<ExperienceMachineComponent>) Registration.EXPERIENCE_MACHINE_COMPONENT.get();
    }

    public PopupScreen makePopup(MachineEditScreen parent, @Nullable ExperienceMachineComponent.Template template, Consumer<ExperienceMachineComponent.Template> onFinish) {
        return new ExperienceComponentBuilder.ExperienceComponentBuilderPopup(parent, template, onFinish);
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height, ExperienceMachineComponent.Template template) {
        graphics.renderFakeItem(Items.EXPERIENCE_BOTTLE.getDefaultInstance(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
    }

    public static class ExperienceComponentBuilderPopup extends ComponentBuilderPopup<ExperienceMachineComponent.Template> {

        private EditBox capacity;

        private Checkbox retrieve;

        private EditBox slots;

        public ExperienceComponentBuilderPopup(BaseScreen parent, @Nullable ExperienceMachineComponent.Template template, Consumer<ExperienceMachineComponent.Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinery.gui.creation.components.experience.title"));
        }

        public ExperienceMachineComponent.Template makeTemplate() {
            return new ExperienceMachineComponent.Template((int) this.parseLong(this.capacity.getValue()), this.retrieve.selected(), this.slotsFromString(this.slots.getValue()));
        }

        @Override
        protected void init() {
            super.init();
            this.capacity = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.capacity"), new EditBox(this.f_96547_, 0, 0, 160, 20, Component.translatable("custommachinery.gui.creation.components.capacity")));
            this.capacity.setFilter(this::checkLong);
            this.capacity.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.components.experience.capacity.tooltip")));
            this.baseTemplate().ifPresentOrElse(template -> this.capacity.setValue(template.capacity() + ""), () -> this.capacity.setValue("10000"));
            this.retrieve = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.experience.retrieve"), new Checkbox(0, 0, 20, 20, Component.translatable("custommachinery.gui.creation.components.experience.retrieve"), false, false));
            this.retrieve.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.components.experience.retrieve.tooltip")));
            if ((Boolean) this.baseTemplate().map(ExperienceMachineComponent.Template::retrieve).orElse(false) != this.retrieve.selected()) {
                this.retrieve.onPress();
            }
            this.slots = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.experience.slots"), new EditBox(this.f_96547_, 0, 0, 160, 20, Component.translatable("custommachinery.gui.creation.components.experience.slots")));
            this.slots.m_257544_(Tooltip.create(Component.translatable("custommachinery.gui.creation.components.experience.slots.tooltip")));
            this.baseTemplate().ifPresent(template -> this.slots.setValue(this.stringFromSlots(template.slots())));
        }

        private List<String> slotsFromString(String s) {
            return Arrays.asList(s.split(","));
        }

        private String stringFromSlots(List<String> s) {
            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = s.iterator();
            while (iterator.hasNext()) {
                builder.append((String) iterator.next());
                if (iterator.hasNext()) {
                    builder.append(",");
                }
            }
            return builder.toString();
        }
    }
}