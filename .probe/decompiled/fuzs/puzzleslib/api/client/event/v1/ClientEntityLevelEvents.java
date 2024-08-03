package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

public final class ClientEntityLevelEvents {

    public static final EventInvoker<ClientEntityLevelEvents.Load> LOAD = EventInvoker.lookup(ClientEntityLevelEvents.Load.class);

    public static final EventInvoker<ClientEntityLevelEvents.Unload> UNLOAD = EventInvoker.lookup(ClientEntityLevelEvents.Unload.class);

    private ClientEntityLevelEvents() {
    }

    @FunctionalInterface
    public interface Load {

        EventResult onEntityLoad(Entity var1, ClientLevel var2);
    }

    @FunctionalInterface
    public interface Unload {

        void onEntityUnload(Entity var1, ClientLevel var2);
    }
}