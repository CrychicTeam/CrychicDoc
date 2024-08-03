package com.mna.blocks.tileentities;

import com.mna.blocks.tileentities.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BrazierTile extends BlockEntity {

    private int color = -1;

    public BrazierTile(BlockPos pPos, BlockState pBlockState) {
        super(TileEntityInit.BRAZIER.get(), pPos, pBlockState);
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
        if (!this.m_58904_().isClientSide()) {
            this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        nbt.putInt("color", this.color);
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("color")) {
            this.color = nbt.getInt("color");
        }
        super.load(nbt);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("color", this.color);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag superTag = super.getUpdateTag();
        superTag.putInt("color", this.color);
        return superTag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.color = tag.getInt("color");
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.color = pkt.getTag().getInt("color");
    }
}