package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class UseItemEvents {

    public static final EventInvoker<UseItemEvents.Start> START = EventInvoker.lookup(UseItemEvents.Start.class);

    public static final EventInvoker<UseItemEvents.Tick> TICK = EventInvoker.lookup(UseItemEvents.Tick.class);

    public static final EventInvoker<UseItemEvents.Stop> STOP = EventInvoker.lookup(UseItemEvents.Stop.class);

    public static final EventInvoker<UseItemEvents.Finish> FINISH = EventInvoker.lookup(UseItemEvents.Finish.class);

    private UseItemEvents() {
    }

    @FunctionalInterface
    public interface Finish {

        void onUseItemFinish(LivingEntity var1, MutableValue<ItemStack> var2, int var3, ItemStack var4);
    }

    @FunctionalInterface
    public interface Start {

        EventResult onUseItemStart(LivingEntity var1, ItemStack var2, MutableInt var3);
    }

    @FunctionalInterface
    public interface Stop {

        EventResult onUseItemStop(LivingEntity var1, ItemStack var2, int var3);
    }

    @FunctionalInterface
    public interface Tick {

        EventResult onUseItemTick(LivingEntity var1, ItemStack var2, MutableInt var3);
    }
}