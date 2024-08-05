package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.item.UpdatesStackTags;
import com.github.alexthe666.citadel.server.message.PacketBufferUtils;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class UpdateItemTagMessage {

    private int entityId;

    private ItemStack itemStackFrom;

    public UpdateItemTagMessage(int entityId, ItemStack itemStackFrom) {
        this.entityId = entityId;
        this.itemStackFrom = itemStackFrom;
    }

    public static UpdateItemTagMessage read(FriendlyByteBuf buf) {
        return new UpdateItemTagMessage(buf.readInt(), PacketBufferUtils.readItemStack(buf));
    }

    public static void write(UpdateItemTagMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        PacketBufferUtils.writeItemStack(buf, message.itemStackFrom);
    }

    public static void handle(UpdateItemTagMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
        if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            playerSided = AlexsCaves.PROXY.getClientSidePlayer();
        }
        if (playerSided != null) {
            Entity holder = playerSided.m_9236_().getEntity(message.entityId);
            if (holder instanceof LivingEntity living) {
                ItemStack stackFrom = message.itemStackFrom;
                ItemStack to = null;
                if (living.getItemInHand(InteractionHand.MAIN_HAND).is(stackFrom.getItem())) {
                    to = living.getItemInHand(InteractionHand.MAIN_HAND);
                } else if (living.getItemInHand(InteractionHand.OFF_HAND).is(stackFrom.getItem())) {
                    to = living.getItemInHand(InteractionHand.OFF_HAND);
                }
                if (to != null && to.getItem() instanceof UpdatesStackTags updatesStackTags && stackFrom.getTag() != null) {
                    updatesStackTags.updateTagFromServer(holder, to, stackFrom.getTag());
                }
            }
        }
    }
}