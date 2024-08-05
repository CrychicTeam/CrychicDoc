package noppes.npcs.packets.server;

import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.TransportCategory;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketTransportGet extends PacketServerBasic {

    private int id;

    public SPacketTransportGet(int id) {
        this.id = id;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketTransportGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketTransportGet decode(FriendlyByteBuf buf) {
        return new SPacketTransportGet(buf.readInt());
    }

    @Override
    protected void handle() {
        sendTransportData(this.player, this.id);
    }

    public static void sendTransportData(ServerPlayer player, int categoryid) {
        TransportCategory category = (TransportCategory) TransportController.getInstance().categories.get(categoryid);
        if (category != null) {
            HashMap<String, Integer> map = new HashMap();
            for (TransportLocation transport : category.locations.values()) {
                map.put(transport.name, transport.id);
            }
            NoppesUtilServer.sendScrollData(player, map);
        }
    }
}