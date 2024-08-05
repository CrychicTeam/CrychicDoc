package io.github.lightman314.lightmanscurrency.util;

import io.github.lightman314.lightmanscurrency.LightmansCurrency;
import io.github.lightman314.lightmanscurrency.network.message.CPacketRequestNBT;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityUtil {

    public static void sendUpdatePacket(BlockEntity tileEntity) {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(tileEntity);
        if (packet != null) {
            sendUpdatePacket(tileEntity.getLevel(), tileEntity.getBlockPos(), packet);
        } else {
            LightmansCurrency.LogError(tileEntity.getClass().getName() + ".getUpdatePacket() returned null!");
        }
    }

    public static void sendUpdatePacket(BlockEntity tileEntity, CompoundTag compound) {
        ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(tileEntity, be -> compound);
        sendUpdatePacket(tileEntity.getLevel(), tileEntity.getBlockPos(), packet);
    }

    private static void sendUpdatePacket(Level world, BlockPos pos, ClientboundBlockEntityDataPacket packet) {
        if (world instanceof ServerLevel server) {
            List<ServerPlayer> players = server.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false);
            players.forEach(player -> player.connection.send(packet));
        } else {
            LightmansCurrency.LogWarning("Cannot send Tile Entity Update Packet from a client.");
        }
    }

    public static void requestUpdatePacket(BlockEntity be) {
        if (be != null) {
            requestUpdatePacket(be.getLevel(), be.getBlockPos());
        }
    }

    public static void requestUpdatePacket(Level level, BlockPos pos) {
        if (level.isClientSide) {
            new CPacketRequestNBT(pos).send();
        }
    }
}