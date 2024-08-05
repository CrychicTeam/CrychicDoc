package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketNaturalSpawnRemove extends PacketServerBasic {

    private int id;

    public SPacketNaturalSpawnRemove(int id) {
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_NATURALSPAWN;
    }

    public static void encode(SPacketNaturalSpawnRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketNaturalSpawnRemove decode(FriendlyByteBuf buf) {
        return new SPacketNaturalSpawnRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        SpawnController.instance.removeSpawnData(this.id);
        NoppesUtilServer.sendScrollData(this.player, SpawnController.instance.getScroll());
    }
}