package net.minecraft.network.protocol.status;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundPongResponsePacket implements Packet<ClientStatusPacketListener> {

    private final long time;

    public ClientboundPongResponsePacket(long long0) {
        this.time = long0;
    }

    public ClientboundPongResponsePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.time = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeLong(this.time);
    }

    public void handle(ClientStatusPacketListener clientStatusPacketListener0) {
        clientStatusPacketListener0.handlePongResponse(this);
    }

    public long getTime() {
        return this.time;
    }
}