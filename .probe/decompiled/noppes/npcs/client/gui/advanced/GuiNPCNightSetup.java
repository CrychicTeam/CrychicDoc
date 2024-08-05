package noppes.npcs.client.gui.advanced;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.constants.EnumMenuType;
import noppes.npcs.controllers.data.DataTransform;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketMenuGet;
import noppes.npcs.packets.server.SPacketMenuSave;
import noppes.npcs.packets.server.SPacketNpcTransform;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.IGuiData;

public class GuiNPCNightSetup extends GuiNPCInterface2 implements IGuiData {

    private DataTransform data;

    public GuiNPCNightSetup(EntityNPCInterface npc) {
        super(npc);
        this.data = npc.transform;
        Packets.sendServer(new SPacketMenuGet(EnumMenuType.TRANSFORM));
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "menu.display", this.guiLeft + 4, this.guiTop + 25));
        this.addButton(new GuiButtonNop(this, 0, this.guiLeft + 104, this.guiTop + 20, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasDisplay ? 1 : 0));
        this.addLabel(new GuiLabel(1, "menu.stats", this.guiLeft + 4, this.guiTop + 47));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 104, this.guiTop + 42, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasStats ? 1 : 0));
        this.addLabel(new GuiLabel(2, "menu.ai", this.guiLeft + 4, this.guiTop + 69));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 104, this.guiTop + 64, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasAi ? 1 : 0));
        this.addLabel(new GuiLabel(3, "menu.inventory", this.guiLeft + 4, this.guiTop + 91));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 104, this.guiTop + 86, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasInv ? 1 : 0));
        this.addLabel(new GuiLabel(4, "menu.advanced", this.guiLeft + 4, this.guiTop + 113));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 104, this.guiTop + 108, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasAdvanced ? 1 : 0));
        this.addLabel(new GuiLabel(5, "role.name", this.guiLeft + 4, this.guiTop + 135));
        this.addButton(new GuiButtonNop(this, 5, this.guiLeft + 104, this.guiTop + 130, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasRole ? 1 : 0));
        this.addLabel(new GuiLabel(6, "job.name", this.guiLeft + 4, this.guiTop + 157));
        this.addButton(new GuiButtonNop(this, 6, this.guiLeft + 104, this.guiTop + 152, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.hasJob ? 1 : 0));
        this.addLabel(new GuiLabel(10, "advanced.editingmode", this.guiLeft + 170, this.guiTop + 9));
        this.addButton(new GuiButtonNop(this, 10, this.guiLeft + 244, this.guiTop + 4, 50, 20, new String[] { "gui.no", "gui.yes" }, this.data.editingModus ? 1 : 0));
        if (this.data.editingModus) {
            this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 170, this.guiTop + 34, "advanced.loadday"));
            this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 170, this.guiTop + 56, "advanced.loadnight"));
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 0) {
            this.data.hasDisplay = guibutton.getValue() == 1;
        }
        if (guibutton.id == 1) {
            this.data.hasStats = guibutton.getValue() == 1;
        }
        if (guibutton.id == 2) {
            this.data.hasAi = guibutton.getValue() == 1;
        }
        if (guibutton.id == 3) {
            this.data.hasInv = guibutton.getValue() == 1;
        }
        if (guibutton.id == 4) {
            this.data.hasAdvanced = guibutton.getValue() == 1;
        }
        if (guibutton.id == 5) {
            this.data.hasRole = guibutton.getValue() == 1;
        }
        if (guibutton.id == 6) {
            this.data.hasJob = guibutton.getValue() == 1;
        }
        if (guibutton.id == 10) {
            this.data.editingModus = guibutton.getValue() == 1;
            this.save();
            this.init();
        }
        if (guibutton.id == 11) {
            Packets.sendServer(new SPacketNpcTransform(false));
        }
        if (guibutton.id == 12) {
            Packets.sendServer(new SPacketNpcTransform(true));
        }
    }

    @Override
    public void save() {
        Packets.sendServer(new SPacketMenuSave(EnumMenuType.TRANSFORM, this.data.writeOptions(new CompoundTag())));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.data.readOptions(compound);
        this.init();
    }
}