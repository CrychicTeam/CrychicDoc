package com.simibubi.create.foundation.networking;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public abstract class BlockEntityConfigurationPacket<BE extends SyncedBlockEntity> extends SimplePacketBase {

    protected BlockPos pos;

    public BlockEntityConfigurationPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.readSettings(buffer);
    }

    public BlockEntityConfigurationPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        this.writeSettings(buffer);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Level world = player.m_9236_();
                if (world != null && world.isLoaded(this.pos)) {
                    if (this.pos.m_123314_(player.m_20183_(), (double) this.maxRange())) {
                        BlockEntity blockEntity = world.getBlockEntity(this.pos);
                        if (blockEntity instanceof SyncedBlockEntity) {
                            this.applySettings(player, (BE) blockEntity);
                            if (!this.causeUpdate()) {
                                return;
                            }
                            ((SyncedBlockEntity) blockEntity).sendData();
                            blockEntity.setChanged();
                        }
                    }
                }
            }
        });
        return true;
    }

    protected int maxRange() {
        return 20;
    }

    protected abstract void writeSettings(FriendlyByteBuf var1);

    protected abstract void readSettings(FriendlyByteBuf var1);

    protected void applySettings(ServerPlayer player, BE be) {
        this.applySettings(be);
    }

    protected boolean causeUpdate() {
        return true;
    }

    protected abstract void applySettings(BE var1);
}