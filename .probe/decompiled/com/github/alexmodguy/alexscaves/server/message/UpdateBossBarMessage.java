package com.github.alexmodguy.alexscaves.server.message;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class UpdateBossBarMessage {

    private UUID bossBar;

    private int renderType;

    public UpdateBossBarMessage(UUID bossBar, int renderType) {
        this.bossBar = bossBar;
        this.renderType = renderType;
    }

    public static UpdateBossBarMessage read(FriendlyByteBuf buf) {
        return new UpdateBossBarMessage(buf.readUUID(), buf.readInt());
    }

    public static void write(UpdateBossBarMessage message, FriendlyByteBuf buf) {
        buf.writeUUID(message.bossBar);
        buf.writeInt(message.renderType);
    }

    public static void handle(UpdateBossBarMessage message, Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context) context.get()).setPacketHandled(true);
        Player playerSided = ((NetworkEvent.Context) context.get()).getSender();
        if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            playerSided = AlexsCaves.PROXY.getClientSidePlayer();
        }
        if (message.renderType == -1) {
            AlexsCaves.PROXY.removeBossBarRender(message.bossBar);
        } else {
            AlexsCaves.PROXY.setBossBarRender(message.bossBar, message.renderType);
        }
    }
}