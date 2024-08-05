package org.violetmoon.zeta.block.be;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ZetaBlockEntity extends BlockEntity {

    public ZetaBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {
        super(tileEntityTypeIn, pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.writeSharedNBT(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.readSharedNBT(nbt);
    }

    public void writeSharedNBT(CompoundTag cmp) {
    }

    public void readSharedNBT(CompoundTag cmp) {
    }

    public void sync() {
        if (this.m_58904_() instanceof ServerLevel slevel) {
            slevel.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag cmp = new CompoundTag();
        this.writeSharedNBT(cmp);
        return cmp;
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(net, packet);
        if (packet != null) {
            this.readSharedNBT(packet.getTag());
        }
    }
}