package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.FactionController;
import noppes.npcs.controllers.data.Faction;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketFactionRemove extends PacketServerBasic {

    private int id;

    public SPacketFactionRemove(int id) {
        this.id = id;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_FACTION;
    }

    public static void encode(SPacketFactionRemove msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.id);
    }

    public static SPacketFactionRemove decode(FriendlyByteBuf buf) {
        return new SPacketFactionRemove(buf.readInt());
    }

    @Override
    protected void handle() {
        FactionController.instance.delete(this.id);
        SPacketFactionsGet.sendFactionDataAll(this.player);
        CompoundTag compound = new CompoundTag();
        new Faction().writeNBT(compound);
        Packets.send(this.player, new PacketGuiData(compound));
    }
}