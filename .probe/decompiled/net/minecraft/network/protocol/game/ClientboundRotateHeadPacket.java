package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class ClientboundRotateHeadPacket implements Packet<ClientGamePacketListener> {

    private final int entityId;

    private final byte yHeadRot;

    public ClientboundRotateHeadPacket(Entity entity0, byte byte1) {
        this.entityId = entity0.getId();
        this.yHeadRot = byte1;
    }

    public ClientboundRotateHeadPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readVarInt();
        this.yHeadRot = friendlyByteBuf0.readByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entityId);
        friendlyByteBuf0.writeByte(this.yHeadRot);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleRotateMob(this);
    }

    public Entity getEntity(Level level0) {
        return level0.getEntity(this.entityId);
    }

    public byte getYHeadRot() {
        return this.yHeadRot;
    }
}