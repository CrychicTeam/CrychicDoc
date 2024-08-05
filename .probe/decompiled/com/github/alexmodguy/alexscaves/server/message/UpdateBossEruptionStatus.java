package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class UpdateBossEruptionStatus {

    private int entityId;

    private boolean erupting;

    public UpdateBossEruptionStatus(int entityId, boolean erupting) {
        this.entityId = entityId;
        this.erupting = erupting;
    }

    public static UpdateBossEruptionStatus read(FriendlyByteBuf buf) {
        return new UpdateBossEruptionStatus(buf.readInt(), buf.readBoolean());
    }

    public static void write(UpdateBossEruptionStatus message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeBoolean(message.erupting);
    }

    public static void handle(UpdateBossEruptionStatus message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
        if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            playerSided = AlexsCaves.PROXY.getClientSidePlayer();
        }
        if (playerSided != null) {
            AlexsCaves.PROXY.setPrimordialBossActive(playerSided.m_9236_(), message.entityId, message.erupting);
        }
    }
}