package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.entity.EntityBaldEagle;
import com.github.alexthe666.alexsmobs.entity.EntityCrimsonMosquito;
import com.github.alexthe666.alexsmobs.entity.EntityEnderiophage;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageMosquitoMountPlayer {

    public int rider;

    public int mount;

    public MessageMosquitoMountPlayer(int rider, int mount) {
        this.rider = rider;
        this.mount = mount;
    }

    public MessageMosquitoMountPlayer() {
    }

    public static MessageMosquitoMountPlayer read(FriendlyByteBuf buf) {
        return new MessageMosquitoMountPlayer(buf.readInt(), buf.readInt());
    }

    public static void write(MessageMosquitoMountPlayer message, FriendlyByteBuf buf) {
        buf.writeInt(message.rider);
        buf.writeInt(message.mount);
    }

    public static class Handler {

        public static void handle(MessageMosquitoMountPlayer message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            Player player = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                player = AlexsMobs.PROXY.getClientSidePlayer();
            }
            if (player != null && player.m_9236_() != null) {
                Entity entity = player.m_9236_().getEntity(message.rider);
                Entity mountEntity = player.m_9236_().getEntity(message.mount);
                if ((entity instanceof EntityCrimsonMosquito || entity instanceof EntityEnderiophage || entity instanceof EntityBaldEagle) && mountEntity instanceof Player && (double) entity.distanceTo(mountEntity) < 16.0) {
                    entity.startRiding(mountEntity, true);
                }
            }
        }
    }
}