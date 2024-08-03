package net.zanckor.questapi.mod.common.network.packet.quest;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;

public class ToastPacket {

    String questName;

    public ToastPacket(String questName) {
        this.questName = questName;
    }

    public ToastPacket(FriendlyByteBuf buffer) {
        this.questName = buffer.readUtf();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.questName);
    }

    public static void handler(ToastPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.toastQuestCompleted(msg.questName)));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}