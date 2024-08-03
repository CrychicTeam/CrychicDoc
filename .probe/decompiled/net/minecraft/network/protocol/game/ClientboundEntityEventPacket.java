package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientboundEntityEventPacket implements Packet<ClientGamePacketListener> {

    private final int entityId;

    private final byte eventId;

    public ClientboundEntityEventPacket(Entity entity0, byte byte1) {
        this.entityId = entity0.getId();
        this.eventId = byte1;
    }

    public ClientboundEntityEventPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readInt();
        this.eventId = friendlyByteBuf0.readByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.entityId);
        friendlyByteBuf0.writeByte(this.eventId);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleEntityEvent(this);
    }

    @Nullable
    public Entity getEntity(Level level0) {
        return level0.getEntity(this.entityId);
    }

    public byte getEventId() {
        return this.eventId;
    }
}