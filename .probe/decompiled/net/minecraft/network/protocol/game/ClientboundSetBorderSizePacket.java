package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.border.WorldBorder;

public class ClientboundSetBorderSizePacket implements Packet<ClientGamePacketListener> {

    private final double size;

    public ClientboundSetBorderSizePacket(WorldBorder worldBorder0) {
        this.size = worldBorder0.getLerpTarget();
    }

    public ClientboundSetBorderSizePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.size = friendlyByteBuf0.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeDouble(this.size);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetBorderSize(this);
    }

    public double getSize() {
        return this.size;
    }
}