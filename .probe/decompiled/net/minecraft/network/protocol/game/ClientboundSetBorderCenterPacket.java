package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundSetBorderCenterPacket implements Packet<ClientGamePacketListener> {

    private final double newCenterX;

    private final double newCenterZ;

    public ClientboundSetBorderCenterPacket(WorldBorder worldBorder0) {
        this.newCenterX = worldBorder0.getCenterX();
        this.newCenterZ = worldBorder0.getCenterZ();
    }

    public ClientboundSetBorderCenterPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.newCenterX = friendlyByteBuf0.readDouble();
        this.newCenterZ = friendlyByteBuf0.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.newCenterX);
        friendlyByteBuf0.writeDouble(this.newCenterZ);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetBorderCenter(this);
    }

    public double getNewCenterZ() {
        return this.newCenterZ;
    }

    public double getNewCenterX() {
        return this.newCenterX;
    }
}