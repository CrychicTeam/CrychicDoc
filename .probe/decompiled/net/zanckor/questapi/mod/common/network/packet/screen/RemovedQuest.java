package net.zanckor.questapi.mod.common.network.packet.screen;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.zanckor.questapi.mod.common.network.handler.ClientHandler;

public class RemovedQuest {

    private final String id;

    public RemovedQuest(String questID) {
        this.id = questID;
    }

    public RemovedQuest(FriendlyByteBuf buffer) {
        this.id = buffer.readUtf();
    }

    public void encodeBuffer(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.id);
    }

    public static void handler(RemovedQuest msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHandler.removeQuest(msg.id)));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}