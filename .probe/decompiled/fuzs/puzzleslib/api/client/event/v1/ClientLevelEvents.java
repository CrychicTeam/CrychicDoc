package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class ClientLevelEvents {

    public static final EventInvoker<ClientLevelEvents.Load> LOAD = EventInvoker.lookup(ClientLevelEvents.Load.class);

    public static final EventInvoker<ClientLevelEvents.Unload> UNLOAD = EventInvoker.lookup(ClientLevelEvents.Unload.class);

    private ClientLevelEvents() {
    }

    @FunctionalInterface
    public interface Load {

        void onLevelLoad(Minecraft var1, ClientLevel var2);
    }

    @FunctionalInterface
    public interface Unload {

        void onLevelUnload(Minecraft var1, ClientLevel var2);
    }
}