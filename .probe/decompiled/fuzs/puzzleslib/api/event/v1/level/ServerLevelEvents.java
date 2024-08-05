package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class ServerLevelEvents {

    public static final EventInvoker<ServerLevelEvents.Load> LOAD = EventInvoker.lookup(ServerLevelEvents.Load.class);

    public static final EventInvoker<ServerLevelEvents.Unload> UNLOAD = EventInvoker.lookup(ServerLevelEvents.Unload.class);

    private ServerLevelEvents() {
    }

    @FunctionalInterface
    public interface Load {

        void onLevelLoad(MinecraftServer var1, ServerLevel var2);
    }

    @FunctionalInterface
    public interface Unload {

        void onLevelUnload(MinecraftServer var1, ServerLevel var2);
    }
}