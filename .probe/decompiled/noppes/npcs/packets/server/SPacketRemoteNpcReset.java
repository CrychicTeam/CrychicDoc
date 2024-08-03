package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketRemoteNpcReset extends PacketServerBasic {

    private int entityId;

    public SPacketRemoteNpcReset(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_RESET;
    }

    public static void encode(SPacketRemoteNpcReset msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static SPacketRemoteNpcReset decode(FriendlyByteBuf buf) {
        return new SPacketRemoteNpcReset(buf.readInt());
    }

    @Override
    protected void handle() {
        Entity entity = this.player.m_9236_().getEntity(this.entityId);
        if (entity != null && entity instanceof EntityNPCInterface) {
            this.npc = (EntityNPCInterface) entity;
            this.npc.reset();
        }
    }
}