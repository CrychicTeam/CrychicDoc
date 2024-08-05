package noppes.npcs.client.gui.script;

import net.minecraft.nbt.CompoundTag;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.entity.data.DataScript;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketScriptGet;
import noppes.npcs.packets.server.SPacketScriptSave;

public class GuiScript extends GuiScriptInterface {

    private DataScript script;

    public GuiScript(EntityNPCInterface npc) {
        this.handler = this.script = npc.script;
        Packets.sendServer(new SPacketScriptGet(0));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.script.load(compound);
        super.setGuiData(compound);
    }

    @Override
    public void save() {
        super.save();
        Packets.sendServer(new SPacketScriptSave(0, this.script.save(new CompoundTag())));
    }
}