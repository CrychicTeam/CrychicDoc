package noppes.npcs.packets.server;

import java.util.Vector;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiScrollList;

public class SPacketLinkedRemove extends PacketServerBasic {

    private String name;

    public SPacketLinkedRemove(String name) {
        this.name = name;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_LINKED;
    }

    public static void encode(SPacketLinkedRemove msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.name, 32767);
    }

    public static SPacketLinkedRemove decode(FriendlyByteBuf buf) {
        return new SPacketLinkedRemove(buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        LinkedNpcController.Instance.removeData(this.name);
        Vector<String> list = new Vector();
        for (LinkedNpcController.LinkedData data : LinkedNpcController.Instance.list) {
            list.add(data.name);
        }
        Packets.send(this.player, new PacketGuiScrollList(list));
    }
}