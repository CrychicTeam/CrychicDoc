package net.zanckor.questapi.mod.common.network.packet.quest;

import java.io.IOException;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;
import net.zanckor.questapi.util.Util;

public class QuestDataPacket {

    Enum quest;

    UUID entity;

    public QuestDataPacket(Enum quest, Entity entity) {
        this.quest = quest;
        this.entity = entity.getUUID();
    }

    public QuestDataPacket(FriendlyByteBuf buffer) {
        this.quest = buffer.readEnum(Enum.class);
        this.entity = buffer.readUUID();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.quest);
        buffer.writeUUID(this.entity);
    }

    public static void handler(QuestDataPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            try {
                ServerPlayer player = ((NetworkEvent.Context) ctx.get()).getSender();
                LivingEntity entity = (LivingEntity) Util.getEntityByUUID((ServerLevel) player.m_9236_(), msg.entity);
                ServerHandler.questHandler(msg.quest, player, entity);
            } catch (IOException var4) {
                throw new RuntimeException(var4);
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}