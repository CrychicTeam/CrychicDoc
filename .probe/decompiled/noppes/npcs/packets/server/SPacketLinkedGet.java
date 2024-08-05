package noppes.npcs.packets.server;

import java.util.Vector;
import net.minecraft.network.FriendlyByteBuf;
import noppes.npcs.controllers.LinkedNpcController;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiScrollList;
import noppes.npcs.packets.client.PacketGuiScrollSelected;

public class SPacketLinkedGet extends PacketServerBasic {

    public static void encode(SPacketLinkedGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketLinkedGet decode(FriendlyByteBuf buf) {
        return new SPacketLinkedGet();
    }

    @Override
    protected void handle() {
        Vector<String> list = new Vector();
        for (LinkedNpcController.LinkedData data : LinkedNpcController.Instance.list) {
            list.add(data.name);
        }
        Packets.send(this.player, new PacketGuiScrollList(list));
        if (this.npc != null) {
            Packets.send(this.player, new PacketGuiScrollSelected(this.npc.linkedName));
        }
    }
}