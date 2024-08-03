package fuzs.puzzleslib.api.event.v1.server;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.server.MinecraftServer;

public final class ServerTickEvents {

    public static final EventInvoker<ServerTickEvents.Start> START = EventInvoker.lookup(ServerTickEvents.Start.class);

    public static final EventInvoker<ServerTickEvents.End> END = EventInvoker.lookup(ServerTickEvents.End.class);

    private ServerTickEvents() {
    }

    @FunctionalInterface
    public interface End {

        void onEndServerTick(MinecraftServer var1);
    }

    @FunctionalInterface
    public interface Start {

        void onStartServerTick(MinecraftServer var1);
    }
}