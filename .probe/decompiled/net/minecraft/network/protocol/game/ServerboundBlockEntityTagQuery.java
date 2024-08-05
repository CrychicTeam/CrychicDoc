package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundBlockEntityTagQuery implements Packet<ServerGamePacketListener> {

    private final int transactionId;

    private final BlockPos pos;

    public ServerboundBlockEntityTagQuery(int int0, BlockPos blockPos1) {
        this.transactionId = int0;
        this.pos = blockPos1;
    }

    public ServerboundBlockEntityTagQuery(FriendlyByteBuf friendlyByteBuf0) {
        this.transactionId = friendlyByteBuf0.readVarInt();
        this.pos = friendlyByteBuf0.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.transactionId);
        friendlyByteBuf0.writeBlockPos(this.pos);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleBlockEntityTagQuery(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}