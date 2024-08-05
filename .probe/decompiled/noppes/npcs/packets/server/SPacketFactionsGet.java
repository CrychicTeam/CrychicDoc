package noppes.npcs.packets.server;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketFactionsGet extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketFactionsGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketFactionsGet decode(FriendlyByteBuf buf) {
        return new SPacketFactionsGet();
    }

    @Override
    protected void handle() {
        sendFactionDataAll(this.player);
    }

    public static void sendFactionDataAll(ServerPlayer player) {
        Map<String, Integer> map = new HashMap();
        for (Faction faction : FactionController.instance.factions.values()) {
            map.put(faction.name, faction.id);
        }
        NoppesUtilServer.sendScrollData(player, map);
    }
}