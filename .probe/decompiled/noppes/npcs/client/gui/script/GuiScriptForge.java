package noppes.npcs.client.gui.script;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.controllers.data.ForgeScriptData;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketScriptGet;
import noppes.npcs.packets.server.SPacketScriptSave;

public class GuiScriptForge extends GuiScriptInterface {

    private ForgeScriptData script = new ForgeScriptData();

    public GuiScriptForge() {
        this.handler = this.script;
        Packets.sendServer(new SPacketScriptGet(3));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.script.load(compound);
        super.setGuiData(compound);
    }

    @Override
    public void save() {
        super.save();
        Packets.sendServer(new SPacketScriptSave(3, this.script.save(new CompoundTag())));
    }
}