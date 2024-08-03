package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundSetCarriedItemPacket implements Packet<ServerGamePacketListener> {

    private final int slot;

    public ServerboundSetCarriedItemPacket(int int0) {
        this.slot = int0;
    }

    public ServerboundSetCarriedItemPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.slot = friendlyByteBuf0.readShort();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeShort(this.slot);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetCarriedItem(this);
    }

    public int getSlot() {
        return this.slot;
    }
}