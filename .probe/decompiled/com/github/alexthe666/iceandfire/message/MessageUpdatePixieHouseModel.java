package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityJar;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageUpdatePixieHouseModel {

    public long blockPos;

    public int houseType;

    public MessageUpdatePixieHouseModel(long blockPos, int houseType) {
        this.blockPos = blockPos;
        this.houseType = houseType;
    }

    public MessageUpdatePixieHouseModel() {
    }

    public static MessageUpdatePixieHouseModel read(FriendlyByteBuf buf) {
        return new MessageUpdatePixieHouseModel(buf.readLong(), buf.readInt());
    }

    public static void write(MessageUpdatePixieHouseModel message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        buf.writeInt(message.houseType);
    }

    public static class Handler {

        public static void handle(MessageUpdatePixieHouseModel message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);
                    BlockEntity blockEntity = player.m_9236_().getBlockEntity(pos);
                    if (blockEntity instanceof TileEntityPixieHouse house) {
                        house.houseType = message.houseType;
                    } else if (blockEntity instanceof TileEntityJar jar) {
                        jar.pixieType = message.houseType;
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}