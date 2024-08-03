package com.github.alexthe666.citadel.server.message;

import com.github.alexthe666.citadel.Citadel;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class SyncClientTickRateMessage {

    private CompoundTag compound;

    public SyncClientTickRateMessage(CompoundTag compound) {
        this.compound = compound;
    }

    public static void write(SyncClientTickRateMessage message, FriendlyByteBuf packetBuffer) {
        PacketBufferUtils.writeTag(packetBuffer, message.compound);
    }

    public static SyncClientTickRateMessage read(FriendlyByteBuf packetBuffer) {
        return new SyncClientTickRateMessage(PacketBufferUtils.readTag(packetBuffer));
    }

    public static class Handler {

        public static void handle(SyncClientTickRateMessage message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
                if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    Citadel.PROXY.handleClientTickRatePacket(message.compound);
                }
            });
        }
    }
}