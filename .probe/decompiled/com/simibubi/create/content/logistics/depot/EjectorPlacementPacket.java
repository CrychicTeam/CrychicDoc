package com.simibubi.create.content.logistics.depot;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class EjectorPlacementPacket extends SimplePacketBase {

    private int h;

    private int v;

    private BlockPos pos;

    private Direction facing;

    public EjectorPlacementPacket(int h, int v, BlockPos pos, Direction facing) {
        this.h = h;
        this.v = v;
        this.pos = pos;
        this.facing = facing;
    }

    public EjectorPlacementPacket(FriendlyByteBuf buffer) {
        this.h = buffer.readInt();
        this.v = buffer.readInt();
        this.pos = buffer.readBlockPos();
        this.facing = Direction.from3DDataValue(buffer.readVarInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(this.h);
        buffer.writeInt(this.v);
        buffer.writeBlockPos(this.pos);
        buffer.writeVarInt(this.facing.get3DDataValue());
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Level world = player.m_9236_();
                if (world != null && world.isLoaded(this.pos)) {
                    BlockEntity blockEntity = world.getBlockEntity(this.pos);
                    BlockState state = world.getBlockState(this.pos);
                    if (blockEntity instanceof EjectorBlockEntity) {
                        ((EjectorBlockEntity) blockEntity).setTarget(this.h, this.v);
                    }
                    if (AllBlocks.WEIGHTED_EJECTOR.has(state)) {
                        world.setBlockAndUpdate(this.pos, (BlockState) state.m_61124_(EjectorBlock.HORIZONTAL_FACING, this.facing));
                    }
                }
            }
        });
        return true;
    }

    public static class ClientBoundRequest extends SimplePacketBase {

        BlockPos pos;

        public ClientBoundRequest(BlockPos pos) {
            this.pos = pos;
        }

        public ClientBoundRequest(FriendlyByteBuf buffer) {
            this.pos = buffer.readBlockPos();
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeBlockPos(this.pos);
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> EjectorTargetHandler.flushSettings(this.pos)));
            return true;
        }
    }
}