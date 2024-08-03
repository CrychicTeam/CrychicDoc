package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketNaturalSpawnGetAll extends PacketServerBasic {

    public static void encode(SPacketNaturalSpawnGetAll msg, FriendlyByteBuf buf) {
    }

    public static SPacketNaturalSpawnGetAll decode(FriendlyByteBuf buf) {
        return new SPacketNaturalSpawnGetAll();
    }

    @Override
    protected void handle() {
        NoppesUtilServer.sendScrollData(this.player, SpawnController.instance.getScroll());
    }
}