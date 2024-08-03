package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class ClientLevelTickEvents {

    public static final EventInvoker<ClientLevelTickEvents.Start> START = EventInvoker.lookup(ClientLevelTickEvents.Start.class);

    public static final EventInvoker<ClientLevelTickEvents.End> END = EventInvoker.lookup(ClientLevelTickEvents.End.class);

    private ClientLevelTickEvents() {
    }

    @FunctionalInterface
    public interface End {

        void onEndLevelTick(Minecraft var1, ClientLevel var2);
    }

    @FunctionalInterface
    public interface Start {

        void onStartLevelTick(Minecraft var1, ClientLevel var2);
    }
}