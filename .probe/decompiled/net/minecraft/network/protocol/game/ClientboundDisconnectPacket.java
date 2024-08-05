package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundDisconnectPacket implements Packet<ClientGamePacketListener> {

    private final Component reason;

    public ClientboundDisconnectPacket(Component component0) {
        this.reason = component0;
    }

    public ClientboundDisconnectPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.reason = friendlyByteBuf0.readComponent();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeComponent(this.reason);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleDisconnect(this);
    }

    public Component getReason() {
        return this.reason;
    }
}