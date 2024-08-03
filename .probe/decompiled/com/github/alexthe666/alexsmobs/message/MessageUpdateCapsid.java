package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityCapsid;
import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageUpdateCapsid {

    public long blockPos;

    public ItemStack heldStack;

    public MessageUpdateCapsid(long blockPos, ItemStack heldStack) {
        this.blockPos = blockPos;
        this.heldStack = heldStack;
    }

    public MessageUpdateCapsid() {
    }

    public static MessageUpdateCapsid read(FriendlyByteBuf buf) {
        return new MessageUpdateCapsid(buf.readLong(), PacketBufferUtils.readItemStack(buf));
    }

    public static void write(MessageUpdateCapsid message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        PacketBufferUtils.writeItemStack(buf, message.heldStack);
    }

    public static class Handler {

        public static void handle(MessageUpdateCapsid message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            Player player = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = AlexsMobs.PROXY.getClientSidePlayer();
            }
            if (player != null && player.m_9236_() != null) {
                BlockPos pos = BlockPos.of(message.blockPos);
                if (player.m_9236_().getBlockEntity(pos) != null && player.m_9236_().getBlockEntity(pos) instanceof TileEntityCapsid) {
                    TileEntityCapsid podium = (TileEntityCapsid) player.m_9236_().getBlockEntity(pos);
                    podium.setItem(0, message.heldStack);
                }
            }
        }
    }
}