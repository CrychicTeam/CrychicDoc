package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;

public final class ClientPlayerEvents {

    public static final EventInvoker<ClientPlayerEvents.LoggedIn> LOGGED_IN = EventInvoker.lookup(ClientPlayerEvents.LoggedIn.class);

    public static final EventInvoker<ClientPlayerEvents.LoggedOut> LOGGED_OUT = EventInvoker.lookup(ClientPlayerEvents.LoggedOut.class);

    public static final EventInvoker<ClientPlayerEvents.Copy> COPY = EventInvoker.lookup(ClientPlayerEvents.Copy.class);

    private ClientPlayerEvents() {
    }

    @FunctionalInterface
    public interface Copy {

        void onCopy(LocalPlayer var1, LocalPlayer var2, MultiPlayerGameMode var3, Connection var4);
    }

    @FunctionalInterface
    public interface LoggedIn {

        void onLoggedIn(LocalPlayer var1, MultiPlayerGameMode var2, Connection var3);
    }

    @FunctionalInterface
    public interface LoggedOut {

        void onLoggedOut(LocalPlayer var1, MultiPlayerGameMode var2, Connection var3);
    }
}