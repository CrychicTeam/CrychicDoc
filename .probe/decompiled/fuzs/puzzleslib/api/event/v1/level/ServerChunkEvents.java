package fuzs.puzzleslib.api.event.v1.level;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

public final class ServerChunkEvents {

    public static final EventInvoker<ServerChunkEvents.Load> LOAD = EventInvoker.lookup(ServerChunkEvents.Load.class);

    public static final EventInvoker<ServerChunkEvents.Unload> UNLOAD = EventInvoker.lookup(ServerChunkEvents.Unload.class);

    public static final EventInvoker<ServerChunkEvents.Watch> WATCH = EventInvoker.lookup(ServerChunkEvents.Watch.class);

    public static final EventInvoker<ServerChunkEvents.Unwatch> UNWATCH = EventInvoker.lookup(ServerChunkEvents.Unwatch.class);

    private ServerChunkEvents() {
    }

    @FunctionalInterface
    public interface Load {

        void onChunkLoad(ServerLevel var1, LevelChunk var2);
    }

    @FunctionalInterface
    public interface Unload {

        void onChunkUnload(ServerLevel var1, LevelChunk var2);
    }

    @FunctionalInterface
    public interface Unwatch {

        void onChunkUnwatch(ServerPlayer var1, ChunkPos var2, ServerLevel var3);
    }

    @FunctionalInterface
    public interface Watch {

        void onChunkWatch(ServerPlayer var1, LevelChunk var2, ServerLevel var3);
    }
}