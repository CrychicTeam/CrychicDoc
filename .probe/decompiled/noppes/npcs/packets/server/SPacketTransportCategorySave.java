package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketTransportCategorySave extends PacketServerBasic {

    private int id;

    private String name;

    public SPacketTransportCategorySave(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_TRANSPORT;
    }

    public static void encode(SPacketTransportCategorySave msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
        buf.writeUtf(msg.name);
    }

    public static SPacketTransportCategorySave decode(FriendlyByteBuf buf) {
        return new SPacketTransportCategorySave(buf.readInt(), buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        TransportController.getInstance().saveCategory(this.name, this.id);
    }
}