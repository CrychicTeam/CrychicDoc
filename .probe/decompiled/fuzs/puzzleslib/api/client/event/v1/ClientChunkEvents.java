package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;

public final class ClientChunkEvents {

    public static final EventInvoker<ClientChunkEvents.Load> LOAD = EventInvoker.lookup(ClientChunkEvents.Load.class);

    public static final EventInvoker<ClientChunkEvents.Unload> UNLOAD = EventInvoker.lookup(ClientChunkEvents.Unload.class);

    private ClientChunkEvents() {
    }

    @FunctionalInterface
    public interface Load {

        void onChunkLoad(ClientLevel var1, LevelChunk var2);
    }

    @FunctionalInterface
    public interface Unload {

        void onChunkUnload(ClientLevel var1, LevelChunk var2);
    }
}