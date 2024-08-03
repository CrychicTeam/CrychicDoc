package net.minecraft.server.network;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;
import net.minecraft.server.MinecraftServer;

public class MemoryServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {

    private final MinecraftServer server;

    private final Connection connection;

    public MemoryServerHandshakePacketListenerImpl(MinecraftServer minecraftServer0, Connection connection1) {
        this.server = minecraftServer0;
        this.connection = connection1;
    }

    @Override
    public void handleIntention(ClientIntentionPacket clientIntentionPacket0) {
        this.connection.setProtocol(clientIntentionPacket0.getIntention());
        this.connection.setListener(new ServerLoginPacketListenerImpl(this.server, this.connection));
    }

    @Override
    public void onDisconnect(Component component0) {
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }
}