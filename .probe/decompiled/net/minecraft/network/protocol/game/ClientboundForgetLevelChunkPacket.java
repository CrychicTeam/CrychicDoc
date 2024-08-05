package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundForgetLevelChunkPacket implements Packet<ClientGamePacketListener> {

    private final int x;

    private final int z;

    public ClientboundForgetLevelChunkPacket(int int0, int int1) {
        this.x = int0;
        this.z = int1;
    }

    public ClientboundForgetLevelChunkPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.x = friendlyByteBuf0.readInt();
        this.z = friendlyByteBuf0.readInt();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeInt(this.x);
        friendlyByteBuf0.writeInt(this.z);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleForgetLevelChunk(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}