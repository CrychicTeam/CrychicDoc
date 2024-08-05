package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketRemoteNpcTp extends PacketServerBasic {

    private int entityId;

    public SPacketRemoteNpcTp(int entityId) {
        this.entityId = entityId;
    }

    public static void encode(SPacketRemoteNpcTp msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static SPacketRemoteNpcTp decode(FriendlyByteBuf buf) {
        return new SPacketRemoteNpcTp(buf.readInt());
    }

    @Override
    protected void handle() {
        Entity entity = this.player.m_9236_().getEntity(this.entityId);
        if (entity != null && entity instanceof EntityNPCInterface) {
            this.npc = (EntityNPCInterface) entity;
            this.player.connection.teleport(this.npc.m_20185_(), this.npc.m_20186_(), this.npc.m_20189_(), 0.0F, 0.0F);
        }
    }
}