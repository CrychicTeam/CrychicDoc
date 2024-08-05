package fr.frinn.custommachinery.client.element;

import com.google.common.collect.Lists;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.api.guielement.IMachineScreen;
import fr.frinn.custommachinery.common.guielement.DumpGuiElement;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.impl.guielement.TexturedGuiElementWidget;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DumpGuiElementWidget extends TexturedGuiElementWidget<DumpGuiElement> {

    private static final Component TITLE = Component.translatable("custommachinery.gui.element.dump.name");

    private final List<Component> tooltips;

    public DumpGuiElementWidget(DumpGuiElement element, IMachineScreen screen) {
        super(element, screen, TITLE);
        this.tooltips = Lists.newArrayList(new Component[] { Component.translatable("custommachinery.gui.element.dump.name"), Component.translatable("custommachinery.gui.element.dump.tooltip", this.formatComponents(element.getComponents())).withStyle(ChatFormatting.DARK_RED) });
    }

    private String formatComponents(List<MachineComponentType<?>> types) {
        StringBuilder builder = new StringBuilder();
        Iterator<MachineComponentType<?>> iterator = types.iterator();
        while (iterator.hasNext()) {
            MachineComponentType<?> type = (MachineComponentType<?>) iterator.next();
            builder.append(((ResourceLocation) Objects.requireNonNull(Registration.MACHINE_COMPONENT_TYPE_REGISTRY.getId((MachineComponentType<? extends IMachineComponent>) type))).getPath());
            if (iterator.hasNext()) {
                builder.append(Component.literal(", "));
            }
        }
        return builder.toString();
    }

    @Override
    public List<Component> getTooltips() {
        return this.getElement().getTooltips().isEmpty() ? this.tooltips : this.getElement().getTooltips();
    }
}