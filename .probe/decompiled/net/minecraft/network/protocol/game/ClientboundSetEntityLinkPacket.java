package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public class ClientboundSetEntityLinkPacket implements Packet<ClientGamePacketListener> {

    private final int sourceId;

    private final int destId;

    public ClientboundSetEntityLinkPacket(Entity entity0, @Nullable Entity entity1) {
        this.sourceId = entity0.getId();
        this.destId = entity1 != null ? entity1.getId() : 0;
    }

    public ClientboundSetEntityLinkPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.sourceId = friendlyByteBuf0.readInt();
        this.destId = friendlyByteBuf0.readInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.sourceId);
        friendlyByteBuf0.writeInt(this.destId);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleEntityLinkPacket(this);
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public int getDestId() {
        return this.destId;
    }
}