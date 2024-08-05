package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public final class PlayerEvents {

    public static final EventInvoker<PlayerEvents.BreakSpeed> BREAK_SPEED = EventInvoker.lookup(PlayerEvents.BreakSpeed.class);

    public static final EventInvoker<PlayerEvents.Copy> COPY = EventInvoker.lookup(PlayerEvents.Copy.class);

    public static final EventInvoker<PlayerEvents.Respawn> RESPAWN = EventInvoker.lookup(PlayerEvents.Respawn.class);

    public static final EventInvoker<PlayerEvents.StartTracking> START_TRACKING = EventInvoker.lookup(PlayerEvents.StartTracking.class);

    public static final EventInvoker<PlayerEvents.StopTracking> STOP_TRACKING = EventInvoker.lookup(PlayerEvents.StopTracking.class);

    public static final EventInvoker<PlayerEvents.LoggedIn> LOGGED_IN = EventInvoker.lookup(PlayerEvents.LoggedIn.class);

    public static final EventInvoker<PlayerEvents.LoggedOut> LOGGED_OUT = EventInvoker.lookup(PlayerEvents.LoggedOut.class);

    public static final EventInvoker<PlayerEvents.AfterChangeDimension> AFTER_CHANGE_DIMENSION = EventInvoker.lookup(PlayerEvents.AfterChangeDimension.class);

    public static final EventInvoker<PlayerEvents.ItemPickup> ITEM_PICKUP = EventInvoker.lookup(PlayerEvents.ItemPickup.class);

    private PlayerEvents() {
    }

    @FunctionalInterface
    public interface AfterChangeDimension {

        void onAfterChangeDimension(ServerPlayer var1, ServerLevel var2, ServerLevel var3);
    }

    @FunctionalInterface
    public interface BreakSpeed {

        EventResult onBreakSpeed(Player var1, BlockState var2, DefaultedFloat var3);
    }

    @FunctionalInterface
    public interface Copy {

        void onCopy(ServerPlayer var1, ServerPlayer var2, boolean var3);
    }

    @FunctionalInterface
    public interface ItemPickup {

        void onItemPickup(Player var1, ItemEntity var2, ItemStack var3);
    }

    @FunctionalInterface
    public interface LoggedIn {

        void onLoggedIn(ServerPlayer var1);
    }

    @FunctionalInterface
    public interface LoggedOut {

        void onLoggedOut(ServerPlayer var1);
    }

    @FunctionalInterface
    public interface Respawn {

        void onRespawn(ServerPlayer var1, boolean var2);
    }

    @FunctionalInterface
    public interface StartTracking {

        void onStartTracking(Entity var1, ServerPlayer var2);
    }

    @FunctionalInterface
    public interface StopTracking {

        void onStopTracking(Entity var1, ServerPlayer var2);
    }
}