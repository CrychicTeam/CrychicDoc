package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.data.DialogOption;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketNpcDialogsGet extends PacketServerBasic {

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketNpcDialogsGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketNpcDialogsGet decode(FriendlyByteBuf buf) {
        return new SPacketNpcDialogsGet();
    }

    @Override
    protected void handle() {
        for (int pos : this.npc.dialogs.keySet()) {
            DialogOption option = (DialogOption) this.npc.dialogs.get(pos);
            if (option != null && option.hasDialog()) {
                CompoundTag compound = option.writeNBT();
                compound.putInt("Position", pos);
                Packets.send(this.player, new PacketGuiData(compound));
            }
        }
    }
}