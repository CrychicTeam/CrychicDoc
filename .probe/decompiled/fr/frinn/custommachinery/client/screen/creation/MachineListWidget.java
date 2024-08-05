package fr.frinn.custommachinery.client.screen.creation;

import fr.frinn.custommachinery.CustomMachinery;
import fr.frinn.custommachinery.client.screen.BaseScreen;
import fr.frinn.custommachinery.client.screen.widget.ListWidget;
import fr.frinn.custommachinery.common.init.CustomMachineItem;
import fr.frinn.custommachinery.common.machine.CustomMachine;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class MachineListWidget extends ListWidget<MachineListWidget.MachineEntry> {

    private final MachineCreationScreen parent;

    public MachineListWidget(MachineCreationScreen parent, int x, int y, int width, int height, int itemHeight) {
        super(x, y, width, height, itemHeight, Component.empty());
        this.parent = parent;
        this.setRenderSelection();
    }

    public void reload() {
        this.clear();
        CustomMachinery.MACHINES.values().stream().sorted(Comparator.comparing(machine -> machine.getName().getString())).forEach(machine -> this.addEntry(new MachineListWidget.MachineEntry(machine)));
    }

    public static class MachineEntry extends ListWidget.Entry {

        private final Minecraft mc = Minecraft.getInstance();

        private final CustomMachine machine;

        public MachineEntry(CustomMachine machine) {
            this.machine = machine;
        }

        public CustomMachine getMachine() {
            return this.machine;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int x, int y, int width, int height, int mouseX, int mouseY, float partialTick) {
            graphics.renderItem(CustomMachineItem.makeMachineItem(this.machine.getId()), x + 2, y + height / 2 - 8);
            graphics.drawString(this.mc.font, this.machine.getName(), x + 20, y + height / 2 - 9 / 2 - 6, 0, false);
            BaseScreen.drawScaledString(graphics, this.mc.font, Component.literal(this.machine.getId().toString()).withStyle(ChatFormatting.DARK_GRAY), x + 20, y + height / 2 - 9 / 2 + 2, 0.8F, 0, false);
            BaseScreen.drawScaledString(graphics, this.mc.font, this.machine.getLocation().getLoader().getTranslatedName().withStyle(ChatFormatting.ITALIC), x + 20, y + height / 2 - 9 / 2 + 9, 0.7F, 0, false);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return true;
        }
    }
}