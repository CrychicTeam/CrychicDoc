package net.minecraft.network.protocol.login;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundLoginCompressionPacket implements Packet<ClientLoginPacketListener> {

    private final int compressionThreshold;

    public ClientboundLoginCompressionPacket(int int0) {
        this.compressionThreshold = int0;
    }

    public ClientboundLoginCompressionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.compressionThreshold = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.compressionThreshold);
    }

    public void handle(ClientLoginPacketListener clientLoginPacketListener0) {
        clientLoginPacketListener0.handleCompression(this);
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }
}