package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomItems;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketCloneList extends PacketServerBasic {

    private int tab;

    public SPacketCloneList(int tab) {
        this.tab = tab;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand || item.getItem() == CustomItems.cloner || item.getItem() == CustomItems.mount;
    }

    public static void encode(SPacketCloneList msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.tab);
    }

    public static SPacketCloneList decode(FriendlyByteBuf buf) {
        return new SPacketCloneList(buf.readInt());
    }

    @Override
    protected void handle() {
        sendList(this.player, this.tab);
    }

    public static void sendList(ServerPlayer player, int tab) {
        ListTag list = new ListTag();
        for (String name : ServerCloneController.Instance.getClones(tab)) {
            list.add(StringTag.valueOf(name));
        }
        CompoundTag compound = new CompoundTag();
        compound.put("List", list);
        Packets.send(player, new PacketGuiData(compound));
    }
}