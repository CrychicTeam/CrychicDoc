package noppes.npcs.client.gui.advanced;

import java.util.HashMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.select.GuiDialogSelection;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketNpcDialogRemove;
import noppes.npcs.packets.server.SPacketNpcDialogSet;
import noppes.npcs.packets.server.SPacketNpcDialogsGet;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNPCDialogNpcOptions extends GuiNPCInterface2 implements GuiSelectionListener, IGuiData {

    private Screen parent;

    private HashMap<Integer, DialogOption> data = new HashMap();

    private int selectedSlot;

    public GuiNPCDialogNpcOptions(EntityNPCInterface npc, Screen parent) {
        super(npc);
        this.parent = parent;
        this.drawDefaultBackground = true;
        Packets.sendServer(new SPacketNpcDialogsGet());
    }

    @Override
    public void init() {
        super.init();
        for (int i = 0; i < 12; i++) {
            int offset = i >= 6 ? 200 : 0;
            this.addButton(new GuiButtonNop(this, i + 20, this.guiLeft + 20 + offset, this.guiTop + 13 + i % 6 * 22, 20, 20, "X"));
            this.addLabel(new GuiLabel(i, i + "", this.guiLeft + 6 + offset, this.guiTop + 18 + i % 6 * 22));
            String title = "dialog.selectoption";
            if (this.data.containsKey(i)) {
                title = ((DialogOption) this.data.get(i)).title;
            }
            this.addButton(new GuiButtonNop(this, i, this.guiLeft + 44 + offset, this.guiTop + 13 + i % 6 * 22, 140, 20, title));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id >= 0 && id < 20) {
            this.selectedSlot = id;
            int dialogID = -1;
            if (this.data.containsKey(id)) {
                dialogID = ((DialogOption) this.data.get(id)).dialogId;
            }
            this.setSubGui(new GuiDialogSelection(dialogID));
        }
        if (id >= 20 && id < 40) {
            int slot = id - 20;
            this.data.remove(slot);
            Packets.sendServer(new SPacketNpcDialogRemove(slot));
            this.init();
        }
    }

    @Override
    public void save() {
    }

    @Override
    public void selected(int id, String name) {
        Packets.sendServer(new SPacketNpcDialogSet(this.selectedSlot, id));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        int pos = compound.getInt("Position");
        DialogOption dialog = new DialogOption();
        dialog.readNBT(compound);
        this.data.put(pos, dialog);
        this.init();
    }
}