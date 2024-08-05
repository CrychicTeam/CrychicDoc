package fuzs.puzzleslib.api.event.v1.entity;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import org.jetbrains.annotations.Nullable;

public final class ServerEntityLevelEvents {

    @Deprecated(forRemoval = true)
    public static final EventInvoker<ServerEntityLevelEvents.Load> LOAD = EventInvoker.lookup(ServerEntityLevelEvents.Load.class);

    public static final EventInvoker<ServerEntityLevelEvents.LoadV2> LOAD_V2 = EventInvoker.lookup(ServerEntityLevelEvents.LoadV2.class);

    public static final EventInvoker<ServerEntityLevelEvents.Spawn> SPAWN = EventInvoker.lookup(ServerEntityLevelEvents.Spawn.class);

    public static final EventInvoker<ServerEntityLevelEvents.Remove> REMOVE = EventInvoker.lookup(ServerEntityLevelEvents.Remove.class);

    private ServerEntityLevelEvents() {
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface Load {

        EventResult onEntityLoad(Entity var1, ServerLevel var2, @Nullable MobSpawnType var3);
    }

    @FunctionalInterface
    public interface LoadV2 {

        EventResult onEntityLoad(Entity var1, ServerLevel var2);
    }

    @FunctionalInterface
    public interface Remove {

        void onEntityRemove(Entity var1, ServerLevel var2);
    }

    @FunctionalInterface
    public interface Spawn {

        EventResult onEntitySpawn(Entity var1, ServerLevel var2, @Nullable MobSpawnType var3);
    }
}