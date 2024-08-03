package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundBlockDestructionPacket implements Packet<ClientGamePacketListener> {

    private final int id;

    private final BlockPos pos;

    private final int progress;

    public ClientboundBlockDestructionPacket(int int0, BlockPos blockPos1, int int2) {
        this.id = int0;
        this.pos = blockPos1;
        this.progress = int2;
    }

    public ClientboundBlockDestructionPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.id = friendlyByteBuf0.readVarInt();
        this.pos = friendlyByteBuf0.readBlockPos();
        this.progress = friendlyByteBuf0.readUnsignedByte();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.id);
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeByte(this.progress);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleBlockDestruction(this);
    }

    public int getId() {
        return this.id;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getProgress() {
        return this.progress;
    }
}