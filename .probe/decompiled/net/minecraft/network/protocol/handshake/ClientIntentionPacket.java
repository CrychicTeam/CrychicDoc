package net.minecraft.network.protocol.handshake;

import net.minecraft.SharedConstants;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientIntentionPacket implements Packet<ServerHandshakePacketListener> {

    private static final int MAX_HOST_LENGTH = 255;

    private final int protocolVersion;

    private final String hostName;

    private final int port;

    private final ConnectionProtocol intention;

    public ClientIntentionPacket(String string0, int int1, ConnectionProtocol connectionProtocol2) {
        this.protocolVersion = SharedConstants.getCurrentVersion().getProtocolVersion();
        this.hostName = string0;
        this.port = int1;
        this.intention = connectionProtocol2;
    }

    public ClientIntentionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.protocolVersion = friendlyByteBuf0.readVarInt();
        this.hostName = friendlyByteBuf0.readUtf(255);
        this.port = friendlyByteBuf0.readUnsignedShort();
        this.intention = ConnectionProtocol.getById(friendlyByteBuf0.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.protocolVersion);
        friendlyByteBuf0.writeUtf(this.hostName);
        friendlyByteBuf0.writeShort(this.port);
        friendlyByteBuf0.writeVarInt(this.intention.getId());
    }

    public void handle(ServerHandshakePacketListener serverHandshakePacketListener0) {
        serverHandshakePacketListener0.handleIntention(this);
    }

    public ConnectionProtocol getIntention() {
        return this.intention;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPort() {
        return this.port;
    }
}