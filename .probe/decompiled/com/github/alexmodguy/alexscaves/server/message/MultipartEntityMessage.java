package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MultipartEntityMessage {

    public int parentId;

    public int playerId;

    public int type;

    public double damage;

    public MultipartEntityMessage(int parentId, int playerId, int type, double damage) {
        this.parentId = parentId;
        this.playerId = playerId;
        this.type = type;
        this.damage = damage;
    }

    public MultipartEntityMessage() {
    }

    public static MultipartEntityMessage read(FriendlyByteBuf buf) {
        return new MultipartEntityMessage(buf.readInt(), buf.readInt(), buf.readInt(), buf.readDouble());
    }

    public static void write(MultipartEntityMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.parentId);
        buf.writeInt(message.playerId);
        buf.writeInt(message.type);
        buf.writeDouble(message.damage);
    }

    public static void handle(MultipartEntityMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
            Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = AlexsCaves.PROXY.getClientSidePlayer();
            }
            Entity parent = playerSided.m_9236_().getEntity(message.parentId);
            Entity interacter = playerSided.m_9236_().getEntity(message.playerId);
            if (interacter != null && parent != null && parent.isMultipartEntity() && interacter.distanceTo(parent) < 16.0F) {
                if (message.type == 0) {
                    if (interacter instanceof Player player) {
                        parent.interact(player, player.m_7655_());
                    }
                } else if (message.type == 1) {
                    parent.hurt(parent.damageSources().generic(), (float) message.damage);
                }
            }
        });
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }
}