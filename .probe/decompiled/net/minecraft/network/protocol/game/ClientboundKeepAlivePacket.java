package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundKeepAlivePacket implements Packet<ClientGamePacketListener> {

    private final long id;

    public ClientboundKeepAlivePacket(long long0) {
        this.id = long0;
    }

    public ClientboundKeepAlivePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeLong(this.id);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleKeepAlive(this);
    }

    public long getId() {
        return this.id;
    }
}