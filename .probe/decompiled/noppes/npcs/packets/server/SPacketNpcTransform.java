package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketNpcTransform extends PacketServerBasic {

    private boolean isActive;

    public SPacketNpcTransform(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketNpcTransform msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.isActive);
    }

    public static SPacketNpcTransform decode(FriendlyByteBuf buf) {
        return new SPacketNpcTransform(buf.readBoolean());
    }

    @Override
    protected void handle() {
        if (this.npc.transform.isValid()) {
            this.npc.transform.transform(this.isActive);
        }
    }
}