package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetChunkCacheCenterPacket implements Packet<ClientGamePacketListener> {

    private final int x;

    private final int z;

    public ClientboundSetChunkCacheCenterPacket(int int0, int int1) {
        this.x = int0;
        this.z = int1;
    }

    public ClientboundSetChunkCacheCenterPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readVarInt();
        this.z = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.x);
        friendlyByteBuf0.writeVarInt(this.z);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetChunkCacheCenter(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}