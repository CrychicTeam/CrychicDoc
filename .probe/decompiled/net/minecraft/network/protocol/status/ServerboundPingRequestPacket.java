package net.minecraft.network.protocol.status;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPingRequestPacket implements Packet<ServerStatusPacketListener> {

    private final long time;

    public ServerboundPingRequestPacket(long long0) {
        this.time = long0;
    }

    public ServerboundPingRequestPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.time = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeLong(this.time);
    }

    public void handle(ServerStatusPacketListener serverStatusPacketListener0) {
        serverStatusPacketListener0.handlePingRequest(this);
    }

    public long getTime() {
        return this.time;
    }
}