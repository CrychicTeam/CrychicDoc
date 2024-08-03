package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundPingPacket implements Packet<ClientGamePacketListener> {

    private final int id;

    public ClientboundPingPacket(int int0) {
        this.id = int0;
    }

    public ClientboundPingPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.id);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handlePing(this);
    }

    public int getId() {
        return this.id;
    }
}