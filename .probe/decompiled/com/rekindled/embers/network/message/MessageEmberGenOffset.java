package com.rekindled.embers.network.message;

import com.rekindled.embers.util.EmberGenUtil;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class MessageEmberGenOffset {

    public int offX = 0;

    public int offZ = 0;

    public MessageEmberGenOffset() {
    }

    public MessageEmberGenOffset(int x, int z) {
        this.offX = x;
        this.offZ = z;
    }

    public static void encode(MessageEmberGenOffset msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.offX);
        buf.writeInt(msg.offZ);
    }

    public static MessageEmberGenOffset decode(FriendlyByteBuf buf) {
        return new MessageEmberGenOffset(buf.readInt(), buf.readInt());
    }

    public static void handle(MessageEmberGenOffset msg, Supplier<NetworkEvent.Context> ctx) {
        if (((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isClient()) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
                EmberGenUtil.offX = msg.offX;
                EmberGenUtil.offZ = msg.offZ;
            });
        }
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}