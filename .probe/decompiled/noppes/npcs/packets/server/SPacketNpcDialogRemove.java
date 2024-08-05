package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketNpcDialogRemove extends PacketServerBasic {

    private int slot;

    public SPacketNpcDialogRemove(int slot) {
        this.slot = slot;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketNpcDialogRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.slot);
    }

    public static SPacketNpcDialogRemove decode(FriendlyByteBuf buf) {
        return new SPacketNpcDialogRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        this.npc.dialogs.remove(this.slot);
    }
}