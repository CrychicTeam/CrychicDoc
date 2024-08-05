package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageMultipartInteract {

    public int creatureID;

    public float dmg;

    public MessageMultipartInteract(int creatureID, float dmg) {
        this.creatureID = creatureID;
        this.dmg = dmg;
    }

    public MessageMultipartInteract() {
    }

    public static MessageMultipartInteract read(FriendlyByteBuf buf) {
        return new MessageMultipartInteract(buf.readInt(), buf.readFloat());
    }

    public static void write(MessageMultipartInteract message, FriendlyByteBuf buf) {
        buf.writeInt(message.creatureID);
        buf.writeFloat(message.dmg);
    }

    public static class Handler {

        public static void handle(MessageMultipartInteract message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null && player.m_9236_().getEntity(message.creatureID) instanceof LivingEntity livingEntity) {
                    double dist = (double) player.m_20270_(livingEntity);
                    if (dist < 100.0) {
                        if (message.dmg > 0.0F) {
                            livingEntity.hurt(player.m_9236_().damageSources().mobAttack(player), message.dmg);
                        } else {
                            livingEntity.m_6096_(player, InteractionHand.MAIN_HAND);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}