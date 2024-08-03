package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import java.util.function.Supplier;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageKangarooEat {

    public int kangaroo;

    public ItemStack stack;

    public MessageKangarooEat(int kangaroo, ItemStack stack) {
        this.kangaroo = kangaroo;
        this.stack = stack;
    }

    public MessageKangarooEat() {
    }

    public static MessageKangarooEat read(FriendlyByteBuf buf) {
        return new MessageKangarooEat(buf.readInt(), buf.readItem());
    }

    public static void write(MessageKangarooEat message, FriendlyByteBuf buf) {
        buf.writeInt(message.kangaroo);
        buf.writeItem(message.stack);
    }

    public static class Handler {

        public static void handle(MessageKangarooEat message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            Player player = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = AlexsMobs.PROXY.getClientSidePlayer();
            }
            if (player != null && player.m_9236_() != null) {
                Entity entity = player.m_9236_().getEntity(message.kangaroo);
                if (entity instanceof EntityKangaroo && ((EntityKangaroo) entity).kangarooInventory != null) {
                    EntityKangaroo kangaroo = (EntityKangaroo) entity;
                    for (int i = 0; i < 7; i++) {
                        double d2 = kangaroo.m_217043_().nextGaussian() * 0.02;
                        double d0 = kangaroo.m_217043_().nextGaussian() * 0.02;
                        double d1 = kangaroo.m_217043_().nextGaussian() * 0.02;
                        entity.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, message.stack), entity.getX() + (double) (kangaroo.m_217043_().nextFloat() * entity.getBbWidth()) - (double) entity.getBbWidth() * 0.5, entity.getY() + (double) (entity.getBbHeight() * 0.5F) + (double) (kangaroo.m_217043_().nextFloat() * entity.getBbHeight() * 0.5F), entity.getZ() + (double) (kangaroo.m_217043_().nextFloat() * entity.getBbWidth()) - (double) entity.getBbWidth() * 0.5, d0, d1, d2);
                    }
                }
            }
        }
    }
}