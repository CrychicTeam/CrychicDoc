package noppes.npcs.packets.server;

import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketDimensionsGet extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.teleporter;
    }

    public static void encode(SPacketDimensionsGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketDimensionsGet decode(FriendlyByteBuf buf) {
        return new SPacketDimensionsGet();
    }

    @Override
    protected void handle() {
        HashMap<String, Integer> map = new HashMap();
        for (ResourceKey<Level> key : CustomNpcs.Server.levelKeys()) {
            map.put(key.location().toString(), 0);
        }
        NoppesUtilServer.sendScrollData(this.player, map);
    }
}