package fuzs.puzzleslib.api.event.v1.server;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleEvents {

    public static final EventInvoker<ServerLifecycleEvents.ServerStarting> STARTING = EventInvoker.lookup(ServerLifecycleEvents.ServerStarting.class);

    public static final EventInvoker<ServerLifecycleEvents.ServerStarted> STARTED = EventInvoker.lookup(ServerLifecycleEvents.ServerStarted.class);

    public static final EventInvoker<ServerLifecycleEvents.ServerStopping> STOPPING = EventInvoker.lookup(ServerLifecycleEvents.ServerStopping.class);

    public static final EventInvoker<ServerLifecycleEvents.ServerStopped> STOPPED = EventInvoker.lookup(ServerLifecycleEvents.ServerStopped.class);

    private ServerLifecycleEvents() {
    }

    @FunctionalInterface
    public interface ServerStarted {

        void onServerStarted(MinecraftServer var1);
    }

    @FunctionalInterface
    public interface ServerStarting {

        void onServerStarting(MinecraftServer var1);
    }

    @FunctionalInterface
    public interface ServerStopped {

        void onServerStopped(MinecraftServer var1);
    }

    @FunctionalInterface
    public interface ServerStopping {

        void onServerStopping(MinecraftServer var1);
    }
}