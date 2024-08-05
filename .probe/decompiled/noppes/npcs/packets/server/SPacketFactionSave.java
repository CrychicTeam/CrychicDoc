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

public class SPacketFactionSave extends PacketServerBasic {

    private CompoundTag data;

    public SPacketFactionSave(CompoundTag data) {
        this.data = data;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.GLOBAL_FACTION;
    }

    public static void encode(SPacketFactionSave msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static SPacketFactionSave decode(FriendlyByteBuf buf) {
        return new SPacketFactionSave(buf.readNbt());
    }

    @Override
    protected void handle() {
        Faction faction = new Faction();
        faction.readNBT(this.data);
        FactionController.instance.saveFaction(faction);
        SPacketFactionsGet.sendFactionDataAll(this.player);
        CompoundTag compound = new CompoundTag();
        faction.writeNBT(compound);
        Packets.send(this.player, new PacketGuiData(compound));
    }
}