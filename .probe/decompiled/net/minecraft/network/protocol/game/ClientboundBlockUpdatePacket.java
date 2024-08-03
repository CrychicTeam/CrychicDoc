package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ClientboundBlockUpdatePacket implements Packet<ClientGamePacketListener> {

    private final BlockPos pos;

    private final BlockState blockState;

    public ClientboundBlockUpdatePacket(BlockPos blockPos0, BlockState blockState1) {
        this.pos = blockPos0;
        this.blockState = blockState1;
    }

    public ClientboundBlockUpdatePacket(BlockGetter blockGetter0, BlockPos blockPos1) {
        this(blockPos1, blockGetter0.getBlockState(blockPos1));
    }

    public ClientboundBlockUpdatePacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.blockState = friendlyByteBuf0.readById(Block.BLOCK_STATE_REGISTRY);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeId(Block.BLOCK_STATE_REGISTRY, this.blockState);
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleBlockUpdate(this);
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}