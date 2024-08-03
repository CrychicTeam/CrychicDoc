package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.Minecraft;

public final class ClientTickEvents {

    public static final EventInvoker<ClientTickEvents.Start> START = EventInvoker.lookup(ClientTickEvents.Start.class);

    public static final EventInvoker<ClientTickEvents.End> END = EventInvoker.lookup(ClientTickEvents.End.class);

    private ClientTickEvents() {
    }

    @FunctionalInterface
    public interface End {

        void onEndClientTick(Minecraft var1);
    }

    @FunctionalInterface
    public interface Start {

        void onStartClientTick(Minecraft var1);
    }
}