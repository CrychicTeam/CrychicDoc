package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedDouble;
import fuzs.puzzleslib.api.event.v1.data.DefaultedInt;
import fuzs.puzzleslib.api.event.v1.data.MutableDouble;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public final class LivingEvents {

    public static final EventInvoker<LivingEvents.Tick> TICK = EventInvoker.lookup(LivingEvents.Tick.class);

    public static final EventInvoker<LivingEvents.Jump> JUMP = EventInvoker.lookup(LivingEvents.Jump.class);

    public static final EventInvoker<LivingEvents.Visibility> VISIBILITY = EventInvoker.lookup(LivingEvents.Visibility.class);

    public static final EventInvoker<LivingEvents.Breathe> BREATHE = EventInvoker.lookup(LivingEvents.Breathe.class);

    public static final EventInvoker<LivingEvents.Drown> DROWN = EventInvoker.lookup(LivingEvents.Drown.class);

    private LivingEvents() {
    }

    @FunctionalInterface
    public interface Breathe {

        EventResult onLivingBreathe(LivingEntity var1, DefaultedInt var2, boolean var3, boolean var4);
    }

    @FunctionalInterface
    public interface Drown {

        EventResult onLivingDrown(LivingEntity var1, int var2, boolean var3);
    }

    @FunctionalInterface
    public interface Jump {

        EventResult onLivingJump(LivingEntity var1, DefaultedDouble var2);
    }

    @FunctionalInterface
    public interface Tick {

        EventResult onLivingTick(LivingEntity var1);
    }

    @FunctionalInterface
    public interface Visibility {

        void onLivingVisibility(LivingEntity var1, @Nullable Entity var2, MutableDouble var3);
    }
}