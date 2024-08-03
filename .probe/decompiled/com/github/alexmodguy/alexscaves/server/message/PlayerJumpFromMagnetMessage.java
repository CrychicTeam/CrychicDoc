package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.server.entity.util.MagnetUtil;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class PlayerJumpFromMagnetMessage {

    private int entityID;

    private boolean jumping;

    public PlayerJumpFromMagnetMessage(int entityID, boolean jumping) {
        this.entityID = entityID;
        this.jumping = jumping;
    }

    public static PlayerJumpFromMagnetMessage read(FriendlyByteBuf buf) {
        return new PlayerJumpFromMagnetMessage(buf.readInt(), buf.readBoolean());
    }

    public static void write(PlayerJumpFromMagnetMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityID);
        buf.writeBoolean(message.jumping);
    }

    public static void handle(PlayerJumpFromMagnetMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        Player player = ((NetworkEvent.Context) context.get()).getSender();
        if (player != null) {
            Entity entity = player.m_9236_().getEntity(message.entityID);
            if (MagnetUtil.isPulledByMagnets(entity) && entity instanceof LivingEntity living) {
                living.jumping = message.jumping;
            }
        }
    }
}