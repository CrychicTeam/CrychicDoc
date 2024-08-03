package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketFactionGet extends PacketServerBasic {

    private int id;

    public SPacketFactionGet(int id) {
        this.id = id;
    }

    public static void encode(SPacketFactionGet msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketFactionGet decode(FriendlyByteBuf buf) {
        return new SPacketFactionGet(buf.readInt());
    }

    @Override
    protected void handle() {
        CompoundTag compound = new CompoundTag();
        Faction faction = FactionController.instance.getFaction(this.id);
        faction.writeNBT(compound);
        Packets.send(this.player, new PacketGuiData(compound));
    }
}