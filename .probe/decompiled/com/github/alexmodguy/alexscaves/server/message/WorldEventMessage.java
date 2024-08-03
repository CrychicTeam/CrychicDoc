package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class WorldEventMessage {

    public int messageId;

    public int blockX;

    public int blockY;

    public int blockZ;

    public WorldEventMessage(int messageId, int blockX, int blockY, int blockZ) {
        this.messageId = messageId;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }

    public WorldEventMessage() {
    }

    public static WorldEventMessage read(FriendlyByteBuf buf) {
        return new WorldEventMessage(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void write(WorldEventMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.messageId);
        buf.writeInt(message.blockX);
        buf.writeInt(message.blockY);
        buf.writeInt(message.blockZ);
    }

    public static void handle(WorldEventMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
            Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
            if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                playerSided = AlexsCaves.PROXY.getClientSidePlayer();
            }
            if (playerSided.m_9236_() != null) {
                BlockPos blockPos = new BlockPos(message.blockX, message.blockY, message.blockZ);
                AlexsCaves.PROXY.playWorldEvent(message.messageId, playerSided.m_9236_(), blockPos);
            }
        });
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
    }
}