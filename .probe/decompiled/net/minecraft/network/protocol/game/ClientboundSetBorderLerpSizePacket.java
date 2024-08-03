package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundSetBorderLerpSizePacket implements Packet<ClientGamePacketListener> {

    private final double oldSize;

    private final double newSize;

    private final long lerpTime;

    public ClientboundSetBorderLerpSizePacket(WorldBorder worldBorder0) {
        this.oldSize = worldBorder0.getSize();
        this.newSize = worldBorder0.getLerpTarget();
        this.lerpTime = worldBorder0.getLerpRemainingTime();
    }

    public ClientboundSetBorderLerpSizePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.oldSize = friendlyByteBuf0.readDouble();
        this.newSize = friendlyByteBuf0.readDouble();
        this.lerpTime = friendlyByteBuf0.readVarLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.oldSize);
        friendlyByteBuf0.writeDouble(this.newSize);
        friendlyByteBuf0.writeVarLong(this.lerpTime);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetBorderLerpSize(this);
    }

    public double getOldSize() {
        return this.oldSize;
    }

    public double getNewSize() {
        return this.newSize;
    }

    public long getLerpTime() {
        return this.lerpTime;
    }
}