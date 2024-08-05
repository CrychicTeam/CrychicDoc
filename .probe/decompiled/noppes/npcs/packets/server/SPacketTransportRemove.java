package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketTransportRemove extends PacketServerBasic {

    private int id;

    public SPacketTransportRemove(int id) {
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_TRANSPORT;
    }

    public static void encode(SPacketTransportRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketTransportRemove decode(FriendlyByteBuf buf) {
        return new SPacketTransportRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        TransportLocation loc = TransportController.getInstance().removeLocation(this.id);
        if (loc != null) {
            SPacketTransportGet.sendTransportData(this.player, loc.category.id);
        }
    }
}