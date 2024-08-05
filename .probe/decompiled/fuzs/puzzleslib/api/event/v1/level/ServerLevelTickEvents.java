package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class ServerLevelTickEvents {

    public static final EventInvoker<ServerLevelTickEvents.Start> START = EventInvoker.lookup(ServerLevelTickEvents.Start.class);

    public static final EventInvoker<ServerLevelTickEvents.End> END = EventInvoker.lookup(ServerLevelTickEvents.End.class);

    private ServerLevelTickEvents() {
    }

    @FunctionalInterface
    public interface End {

        void onEndLevelTick(MinecraftServer var1, ServerLevel var2);
    }

    @FunctionalInterface
    public interface Start {

        void onStartLevelTick(MinecraftServer var1, ServerLevel var2);
    }
}