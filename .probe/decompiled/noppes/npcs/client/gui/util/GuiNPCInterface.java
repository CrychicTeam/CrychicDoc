package noppes.npcs.client.gui.util;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.shared.client.gui.components.GuiBasic;

public abstract class GuiNPCInterface extends GuiBasic {

    public EntityNPCInterface npc;

    public GuiNPCInterface(EntityNPCInterface npc) {
        this.npc = npc;
    }

    public GuiNPCInterface() {
    }

    @Override
    public void setSubGui(Screen gui) {
        if (gui instanceof GuiNPCInterface) {
            ((GuiNPCInterface) gui).npc = this.npc;
        }
        if (gui instanceof GuiContainerNPCInterface) {
            ((GuiContainerNPCInterface) gui).npc = this.npc;
        }
        super.setSubGui(gui);
    }

    public void drawNpc(GuiGraphics graphics, int x, int y) {
        this.drawNpc(graphics, this.npc, x, y, 1.0F, 0);
    }
}