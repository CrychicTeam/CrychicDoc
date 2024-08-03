package net.minecraft.server.network;

import net.minecraft.SharedConstants;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.MinecraftServer;

public class ServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {

    private static final Component IGNORE_STATUS_REASON = Component.translatable("disconnect.ignoring_status_request");

    private final MinecraftServer server;

    private final Connection connection;

    public ServerHandshakePacketListenerImpl(MinecraftServer minecraftServer0, Connection connection1) {
        this.server = minecraftServer0;
        this.connection = connection1;
    }

    @Override
    public void handleIntention(ClientIntentionPacket clientIntentionPacket0) {
        switch(clientIntentionPacket0.getIntention()) {
            case LOGIN:
                this.connection.setProtocol(ConnectionProtocol.LOGIN);
                if (clientIntentionPacket0.getProtocolVersion() != SharedConstants.getCurrentVersion().getProtocolVersion()) {
                    Component $$1;
                    if (clientIntentionPacket0.getProtocolVersion() < 754) {
                        $$1 = Component.translatable("multiplayer.disconnect.outdated_client", SharedConstants.getCurrentVersion().getName());
                    } else {
                        $$1 = Component.translatable("multiplayer.disconnect.incompatible", SharedConstants.getCurrentVersion().getName());
                    }
                    this.connection.send(new ClientboundLoginDisconnectPacket($$1));
                    this.connection.disconnect($$1);
                } else {
                    this.connection.setListener(new ServerLoginPacketListenerImpl(this.server, this.connection));
                }
                break;
            case STATUS:
                ServerStatus $$3 = this.server.getStatus();
                if (this.server.repliesToStatus() && $$3 != null) {
                    this.connection.setProtocol(ConnectionProtocol.STATUS);
                    this.connection.setListener(new ServerStatusPacketListenerImpl($$3, this.connection));
                } else {
                    this.connection.disconnect(IGNORE_STATUS_REASON);
                }
                break;
            default:
                throw new UnsupportedOperationException("Invalid intention " + clientIntentionPacket0.getIntention());
        }
    }

    @Override
    public void onDisconnect(Component component0) {
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }
}