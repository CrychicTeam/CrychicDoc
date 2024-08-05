package noppes.npcs.client.gui.advanced;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.GuiNPCLinesEdit;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiButtonYesNo;
import noppes.npcs.shared.client.gui.components.GuiLabel;

public class GuiNPCLinesMenu extends GuiNPCInterface2 {

    public GuiNPCLinesMenu(EntityNPCInterface npc) {
        super(npc);
    }

    @Override
    public void init() {
        super.init();
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 85, this.guiTop + 20, "lines.world"));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 85, this.guiTop + 43, "lines.attack"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 85, this.guiTop + 66, "lines.interact"));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 85, this.guiTop + 89, "lines.killed"));
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 85, this.guiTop + 112, "lines.kill"));
        this.addButton(new GuiButtonNop(this, 7, this.guiLeft + 85, this.guiTop + 135, "lines.npcinteract"));
        this.addLabel(new GuiLabel(16, "lines.random", this.guiLeft + 85, this.guiTop + 157));
        this.addButton(new GuiButtonYesNo(this, 16, this.guiLeft + 175, this.guiTop + 152, !this.npc.advanced.orderedLines));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 0) {
            NoppesUtil.openGUI(this.player, new GuiNPCLinesEdit(this.npc, this.npc.advanced.worldLines));
        }
        if (id == 1) {
            NoppesUtil.openGUI(this.player, new GuiNPCLinesEdit(this.npc, this.npc.advanced.attackLines));
        }
        if (id == 2) {
            NoppesUtil.openGUI(this.player, new GuiNPCLinesEdit(this.npc, this.npc.advanced.interactLines));
        }
        if (id == 5) {
            NoppesUtil.openGUI(this.player, new GuiNPCLinesEdit(this.npc, this.npc.advanced.killedLines));
        }
        if (id == 6) {
            NoppesUtil.openGUI(this.player, new GuiNPCLinesEdit(this.npc, this.npc.advanced.killLines));
        }
        if (id == 7) {
            NoppesUtil.openGUI(this.player, new GuiNPCLinesEdit(this.npc, this.npc.advanced.npcInteractLines));
        }
        if (id == 16) {
            this.npc.advanced.orderedLines = !((GuiButtonYesNo) guibutton).getBoolean();
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.ADVANCED, this.npc.advanced.save(new CompoundTag())));
    }
}