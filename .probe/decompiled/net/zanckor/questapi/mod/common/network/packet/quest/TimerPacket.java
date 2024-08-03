package net.zanckor.questapi.mod.common.network.packet.quest;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.handler.ServerHandler;

public class TimerPacket {

    public TimerPacket() {
    }

    public TimerPacket(FriendlyByteBuf buffer) {
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
    }

    public static void handler(TimerPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> ServerHandler.questTimer(((NetworkEvent.Context) ctx.get()).getSender().serverLevel().getLevel()));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}