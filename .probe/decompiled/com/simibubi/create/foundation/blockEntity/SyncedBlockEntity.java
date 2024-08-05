package com.simibubi.create.foundation.blockEntity;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class SyncedBlockEntity extends BlockEntity {

    public SyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.writeClient(new CompoundTag());
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void handleUpdateTag(CompoundTag tag) {
        this.readClient(tag);
    }

    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        CompoundTag tag = packet.getTag();
        this.readClient(tag == null ? new CompoundTag() : tag);
    }

    public void readClient(CompoundTag tag) {
        this.m_142466_(tag);
    }

    public CompoundTag writeClient(CompoundTag tag) {
        this.m_183515_(tag);
        return tag;
    }

    public void sendData() {
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(this.m_58899_());
        }
    }

    public void notifyUpdate() {
        this.m_6596_();
        this.sendData();
    }

    public PacketDistributor.PacketTarget packetTarget() {
        return PacketDistributor.TRACKING_CHUNK.with(this::containedChunk);
    }

    public LevelChunk containedChunk() {
        return this.f_58857_.getChunkAt(this.f_58858_);
    }

    public HolderGetter<Block> blockHolderGetter() {
        return (HolderGetter<Block>) (this.f_58857_ != null ? this.f_58857_.m_246945_(Registries.BLOCK) : BuiltInRegistries.BLOCK.m_255303_());
    }
}