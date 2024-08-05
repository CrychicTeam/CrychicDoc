package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageUpdateDragonforge {

    public long blockPos;

    public int cookTime;

    public MessageUpdateDragonforge(long blockPos, int houseType) {
        this.blockPos = blockPos;
        this.cookTime = houseType;
    }

    public MessageUpdateDragonforge() {
    }

    public static MessageUpdateDragonforge read(FriendlyByteBuf buf) {
        return new MessageUpdateDragonforge(buf.readLong(), buf.readInt());
    }

    public static void write(MessageUpdateDragonforge message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        buf.writeInt(message.cookTime);
    }

    public static class Handler {

        public static void handle(MessageUpdateDragonforge message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);
                    if (player.m_9236_().getBlockEntity(pos) instanceof TileEntityDragonforge forge) {
                        forge.cookTime = message.cookTime;
                        if (message.cookTime > 0) {
                            forge.lastDragonFlameTimer = 40;
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}