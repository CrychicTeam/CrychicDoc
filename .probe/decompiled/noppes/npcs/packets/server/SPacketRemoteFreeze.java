package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiScrollSelected;

public class SPacketRemoteFreeze extends PacketServerBasic {

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_FREEZE;
    }

    public static void encode(SPacketRemoteFreeze msg, FriendlyByteBuf buf) {
    }

    public static SPacketRemoteFreeze decode(FriendlyByteBuf buf) {
        return new SPacketRemoteFreeze();
    }

    @Override
    protected void handle() {
        CustomNpcs.FreezeNPCs = !CustomNpcs.FreezeNPCs;
        Packets.send(this.player, new PacketGuiScrollSelected(CustomNpcs.FreezeNPCs ? "Unfreeze Npcs" : "Freeze Npcs"));
    }
}