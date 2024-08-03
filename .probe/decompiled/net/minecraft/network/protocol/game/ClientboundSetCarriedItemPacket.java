package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetCarriedItemPacket implements Packet<ClientGamePacketListener> {

    private final int slot;

    public ClientboundSetCarriedItemPacket(int int0) {
        this.slot = int0;
    }

    public ClientboundSetCarriedItemPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.slot = friendlyByteBuf0.readByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeByte(this.slot);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSetCarriedItem(this);
    }

    public int getSlot() {
        return this.slot;
    }
}