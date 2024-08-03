package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.world.entity.player.Player;

public final class PlayerTickEvents {

    public static final EventInvoker<PlayerTickEvents.Start> START = EventInvoker.lookup(PlayerTickEvents.Start.class);

    public static final EventInvoker<PlayerTickEvents.End> END = EventInvoker.lookup(PlayerTickEvents.End.class);

    private PlayerTickEvents() {
    }

    @FunctionalInterface
    public interface End {

        void onEndPlayerTick(Player var1);
    }

    @FunctionalInterface
    public interface Start {

        void onStartPlayerTick(Player var1);
    }
}