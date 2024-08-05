package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPongPacket implements Packet<ServerGamePacketListener> {

    private final int id;

    public ServerboundPongPacket(int int0) {
        this.id = int0;
    }

    public ServerboundPongPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.id);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handlePong(this);
    }

    public int getId() {
        return this.id;
    }
}