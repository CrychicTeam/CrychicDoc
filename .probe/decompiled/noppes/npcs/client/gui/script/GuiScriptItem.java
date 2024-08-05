package noppes.npcs.client.gui.script;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.api.wrapper.ItemScriptedWrapper;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketScriptGet;
import noppes.npcs.packets.server.SPacketScriptSave;

public class GuiScriptItem extends GuiScriptInterface {

    private ItemScriptedWrapper item;

    public GuiScriptItem(Player player) {
        this.handler = this.item = new ItemScriptedWrapper(new ItemStack(CustomItems.scripted_item));
        Packets.sendServer(new SPacketScriptGet(2));
    }

    @Override
    public void setGuiData(CompoundTag compound) {
        this.item.setMCNbt(compound);
        super.setGuiData(compound);
    }

    @Override
    public void save() {
        super.save();
        Packets.sendServer(new SPacketScriptSave(2, this.item.getMCNbt()));
    }
}