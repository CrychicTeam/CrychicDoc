package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.block.Block;

public class ClientboundBlockEventPacket implements Packet<ClientGamePacketListener> {

    private final BlockPos pos;

    private final int b0;

    private final int b1;

    private final Block block;

    public ClientboundBlockEventPacket(BlockPos blockPos0, Block block1, int int2, int int3) {
        this.pos = blockPos0;
        this.block = block1;
        this.b0 = int2;
        this.b1 = int3;
    }

    public ClientboundBlockEventPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.b0 = friendlyByteBuf0.readUnsignedByte();
        this.b1 = friendlyByteBuf0.readUnsignedByte();
        this.block = friendlyByteBuf0.readById(BuiltInRegistries.BLOCK);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeByte(this.b0);
        friendlyByteBuf0.writeByte(this.b1);
        friendlyByteBuf0.writeId(BuiltInRegistries.BLOCK, this.block);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleBlockEvent(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public int getB0() {
        return this.b0;
    }

    public int getB1() {
        return this.b1;
    }

    public Block getBlock() {
        return this.block;
    }
}