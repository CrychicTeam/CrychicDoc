package noppes.npcs.client.gui.script;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import noppes.npcs.blocks.tiles.TileScriptedDoor;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketScriptGet;
import noppes.npcs.packets.server.SPacketScriptSave;

public class GuiScriptDoor extends GuiScriptInterface {

    private TileScriptedDoor script;

    public GuiScriptDoor(BlockPos pos) {
        this.handler = this.script = (TileScriptedDoor) this.player.m_9236_().getBlockEntity(pos);
        Packets.sendServer(new SPacketScriptGet(5));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.script.setNBT(compound);
        super.setGuiData(compound);
    }

    @Override
    public void save() {
        super.save();
        BlockPos pos = this.script.m_58899_();
        Packets.sendServer(new SPacketScriptSave(5, this.script.getNBT(new CompoundTag())));
    }
}