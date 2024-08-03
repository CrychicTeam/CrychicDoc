package fr.frinn.custommachinery.client.screen.creation.component.builder;

import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.creation.MachineEditScreen;
import fr.frinn.custommachinery.client.screen.creation.component.ComponentBuilderPopup;
import fr.frinn.custommachinery.client.screen.creation.component.IMachineComponentBuilder;
import fr.frinn.custommachinery.client.screen.popup.PopupScreen;
import fr.frinn.custommachinery.client.screen.widget.IntegerSlider;
import fr.frinn.custommachinery.common.component.ChunkloadMachineComponent;
import fr.frinn.custommachinery.common.init.Registration;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class ChunkloadComponentBuilder implements IMachineComponentBuilder<ChunkloadMachineComponent, ChunkloadMachineComponent.Template> {

    @Override
    public MachineComponentType<ChunkloadMachineComponent> type() {
        return (MachineComponentType<ChunkloadMachineComponent>) Registration.CHUNKLOAD_MACHINE_COMPONENT.get();
    }

    public PopupScreen makePopup(MachineEditScreen parent, @Nullable ChunkloadMachineComponent.Template template, Consumer<ChunkloadMachineComponent.Template> onFinish) {
        return new ChunkloadComponentBuilder.ChunkloadComponentBuilderPopup(parent, template, onFinish);
    }

    public void render(GuiGraphics graphics, int x, int y, int width, int height, ChunkloadMachineComponent.Template template) {
        graphics.renderFakeItem(Items.ENDER_EYE.getDefaultInstance(), x, y + height / 2 - 8);
        graphics.drawString(Minecraft.getInstance().font, "type: " + template.getType().getId().getPath(), x + 25, y + 5, 0, false);
    }

    public static class ChunkloadComponentBuilderPopup extends ComponentBuilderPopup<ChunkloadMachineComponent.Template> {

        private IntegerSlider radius;

        public ChunkloadComponentBuilderPopup(BaseScreen parent, @Nullable ChunkloadMachineComponent.Template template, Consumer<ChunkloadMachineComponent.Template> onFinish) {
            super(parent, template, onFinish, Component.translatable("custommachinery.gui.creation.components.chunkload.title"));
        }

        public ChunkloadMachineComponent.Template makeTemplate() {
            return new ChunkloadMachineComponent.Template(this.radius.intValue());
        }

        @Override
        protected void init() {
            super.init();
            this.radius = this.propertyList.add(Component.translatable("custommachinery.gui.creation.components.chunkload.radius"), IntegerSlider.builder().bounds(1, 32).defaultValue(1).create(0, 0, 180, 20, Component.translatable("custommachinery.gui.creation.components.chunkload.radius")));
            this.baseTemplate().ifPresent(template -> this.radius.setValue(template.radius()));
        }
    }
}