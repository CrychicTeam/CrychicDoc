package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.TransportController;
import noppes.npcs.controllers.data.TransportLocation;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.roles.RoleTransporter;

public class SPacketTransportSave extends PacketServerBasic {

    private int category;

    private CompoundTag data;

    public SPacketTransportSave(int category, CompoundTag data) {
        this.data = data;
        this.category = category;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketTransportSave msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.category);
        buf.writeNbt(msg.data);
    }

    public static SPacketTransportSave decode(FriendlyByteBuf buf) {
        return new SPacketTransportSave(buf.readInt(), buf.readNbt());
    }

    @Override
    protected void handle() {
        TransportLocation location = TransportController.getInstance().saveLocation(this.category, this.data, this.player, this.npc);
        if (location != null) {
            if (this.npc.role.getType() != 4) {
                return;
            }
            RoleTransporter role = (RoleTransporter) this.npc.role;
            role.setTransport(location);
        }
    }
}