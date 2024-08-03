package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundKeepAlivePacket implements Packet<ServerGamePacketListener> {

    private final long id;

    public ServerboundKeepAlivePacket(long long0) {
        this.id = long0;
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleKeepAlive(this);
    }

    public ServerboundKeepAlivePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeLong(this.id);
    }

    public long getId() {
        return this.id;
    }
}