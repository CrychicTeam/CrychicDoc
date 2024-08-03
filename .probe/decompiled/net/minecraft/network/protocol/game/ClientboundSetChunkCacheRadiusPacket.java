package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetChunkCacheRadiusPacket implements Packet<ClientGamePacketListener> {

    private final int radius;

    public ClientboundSetChunkCacheRadiusPacket(int int0) {
        this.radius = int0;
    }

    public ClientboundSetChunkCacheRadiusPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.radius = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.radius);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetChunkCacheRadius(this);
    }

    public int getRadius() {
        return this.radius;
    }
}