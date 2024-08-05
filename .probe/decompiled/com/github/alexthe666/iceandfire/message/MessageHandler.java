package com.github.alexthe666.iceandfire.message;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.lwjgl.system.windows.MSG;

public class MessageHandler {

    public static <MSG> BiConsumer<MSG, Supplier<NetworkEvent.Context>> handle(BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        return (msg, ctx) -> {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> messageConsumer.accept(msg, ctx)));
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> messageConsumer.accept(msg, ctx)));
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        };
    }

    public static BiConsumer<MSG, Supplier<NetworkEvent.Context>> handleServer(BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        return (msg, ctx) -> {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> messageConsumer.accept(msg, ctx)));
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        };
    }

    public static BiConsumer<MSG, Supplier<NetworkEvent.Context>> handleClient(BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer) {
        return (msg, ctx) -> {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> messageConsumer.accept(msg, ctx)));
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        };
    }
}