package net.minecraft.network.protocol.login;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundLoginDisconnectPacket implements Packet<ClientLoginPacketListener> {

    private final Component reason;

    public ClientboundLoginDisconnectPacket(Component component0) {
        this.reason = component0;
    }

    public ClientboundLoginDisconnectPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.reason = Component.Serializer.fromJsonLenient(friendlyByteBuf0.readUtf(262144));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeComponent(this.reason);
    }

    public void handle(ClientLoginPacketListener clientLoginPacketListener0) {
        clientLoginPacketListener0.handleDisconnect(this);
    }

    public Component getReason() {
        return this.reason;
    }
}