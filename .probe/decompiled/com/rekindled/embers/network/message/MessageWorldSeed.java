package com.rekindled.embers.network.message;

import com.rekindled.embers.EmbersClientEvents;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MessageWorldSeed {

    long seed;

    public MessageWorldSeed(long seed) {
        this.seed = seed;
    }

    public static void encode(MessageWorldSeed msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.seed);
    }

    public static MessageWorldSeed decode(FriendlyByteBuf buf) {
        return new MessageWorldSeed(buf.readLong());
    }

    public static void handle(MessageWorldSeed msg, Supplier<NetworkEvent.Context> ctx) {
        if (((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isClient()) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> EmbersClientEvents.seed = msg.seed);
        }
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}