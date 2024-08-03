package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class OffsetBlockTile extends BlockEntity {

    private BlockPos offset = new BlockPos(0, 0, 0);

    public OffsetBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public OffsetBlockTile(BlockPos pos, BlockState state) {
        this(TileEntityInit.FILLER_TILE.get(), pos, state);
    }

    public BlockPos getOffset() {
        return this.offset;
    }

    public void setOffset(BlockPos offset) {
        this.offset = offset;
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.put("offset", NbtUtils.writeBlockPos(this.offset));
    }

    @Override
    public void load(CompoundTag nbt) {
        this.offset = NbtUtils.readBlockPos(nbt.getCompound("offset"));
        super.load(nbt);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        this.offset = NbtUtils.readBlockPos(pkt.getTag().getCompound("offset"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.put("offset", NbtUtils.writeBlockPos(this.offset));
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.offset = NbtUtils.readBlockPos(tag.getCompound("offset"));
    }
}