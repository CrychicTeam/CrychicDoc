package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.ISyncMount;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageStartRidingMob {

    public int dragonId;

    public boolean ride;

    public boolean baby;

    public MessageStartRidingMob(int dragonId, boolean ride, boolean baby) {
        this.dragonId = dragonId;
        this.ride = ride;
        this.baby = baby;
    }

    public MessageStartRidingMob() {
    }

    public static MessageStartRidingMob read(FriendlyByteBuf buf) {
        return new MessageStartRidingMob(buf.readInt(), buf.readBoolean(), buf.readBoolean());
    }

    public static void write(MessageStartRidingMob message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.ride);
        buf.writeBoolean(message.baby);
    }

    public static class Handler {

        public static void handle(MessageStartRidingMob message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null) {
                    Entity entity = player.m_9236_().getEntity(message.dragonId);
                    if (entity instanceof ISyncMount && entity instanceof TamableAnimal tamable && tamable.isOwnedBy(player) && tamable.m_20270_(player) < 14.0F) {
                        if (message.ride) {
                            if (message.baby) {
                                tamable.m_7998_(player, true);
                            } else {
                                player.m_7998_(tamable, true);
                            }
                        } else if (message.baby) {
                            tamable.m_8127_();
                        } else {
                            player.m_8127_();
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}