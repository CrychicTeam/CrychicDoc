package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageKangarooInventorySync {

    public int kangaroo;

    public int slotId;

    public ItemStack stack;

    public MessageKangarooInventorySync(int kangaroo, int slotId, ItemStack stack) {
        this.kangaroo = kangaroo;
        this.slotId = slotId;
        this.stack = stack;
    }

    public MessageKangarooInventorySync() {
    }

    public static MessageKangarooInventorySync read(FriendlyByteBuf buf) {
        return new MessageKangarooInventorySync(buf.readInt(), buf.readInt(), buf.readItem());
    }

    public static void write(MessageKangarooInventorySync message, FriendlyByteBuf buf) {
        buf.writeInt(message.kangaroo);
        buf.writeInt(message.slotId);
        buf.writeItem(message.stack);
    }

    public static class Handler {

        public static void handle(MessageKangarooInventorySync message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            Player player = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = AlexsMobs.PROXY.getClientSidePlayer();
            }
            if (player != null && player.m_9236_() != null) {
                Entity entity = player.m_9236_().getEntity(message.kangaroo);
                if (entity instanceof EntityKangaroo && ((EntityKangaroo) entity).kangarooInventory != null && message.slotId >= 0) {
                    ((EntityKangaroo) entity).kangarooInventory.setItem(message.slotId, message.stack);
                }
            }
        }
    }
}