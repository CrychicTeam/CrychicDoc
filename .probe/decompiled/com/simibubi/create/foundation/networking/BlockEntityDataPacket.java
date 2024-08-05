package com.simibubi.create.foundation.networking;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public abstract class BlockEntityDataPacket<BE extends SyncedBlockEntity> extends SimplePacketBase {

    protected BlockPos pos;

    public BlockEntityDataPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
    }

    public BlockEntityDataPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        this.writeData(buffer);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null) {
                BlockEntity blockEntity = world.m_7702_(this.pos);
                if (blockEntity instanceof SyncedBlockEntity) {
                    this.handlePacket((BE) blockEntity);
                }
            }
        });
        return true;
    }

    protected abstract void writeData(FriendlyByteBuf var1);

    protected abstract void handlePacket(BE var1);
}