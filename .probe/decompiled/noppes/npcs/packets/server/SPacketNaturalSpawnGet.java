package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import noppes.npcs.controllers.SpawnController;
import noppes.npcs.controllers.data.SpawnData;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketNaturalSpawnGet extends PacketServerBasic {

    private int id;

    public SPacketNaturalSpawnGet(int id) {
        this.id = id;
    }

    public static void encode(SPacketNaturalSpawnGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketNaturalSpawnGet decode(FriendlyByteBuf buf) {
        return new SPacketNaturalSpawnGet(buf.readInt());
    }

    @Override
    protected void handle() {
        SpawnData spawn = SpawnController.instance.getSpawnData(this.id);
        if (spawn != null) {
            Packets.send(this.player, new PacketGuiData(spawn.writeNBT(new CompoundTag())));
        }
    }
}