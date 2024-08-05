package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPickItemPacket implements Packet<ServerGamePacketListener> {

    private final int slot;

    public ServerboundPickItemPacket(int int0) {
        this.slot = int0;
    }

    public ServerboundPickItemPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.slot = friendlyByteBuf0.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.slot);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePickItem(this);
    }

    public int getSlot() {
        return this.slot;
    }
}