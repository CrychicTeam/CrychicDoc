package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;
import noppes.npcs.packets.client.PacketGuiScrollSelected;
import noppes.npcs.roles.RoleTransporter;

public class SPacketNpcTransportGet extends PacketServerBasic {

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketNpcTransportGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketNpcTransportGet decode(FriendlyByteBuf buf) {
        return new SPacketNpcTransportGet();
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 4) {
            RoleTransporter role = (RoleTransporter) this.npc.role;
            if (role.hasTransport()) {
                Packets.send(this.player, new PacketGuiData(role.getLocation().writeNBT()));
                Packets.send(this.player, new PacketGuiScrollSelected(role.getLocation().category.title));
            }
        }
    }
}