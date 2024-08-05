package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public final class MobEffectEvents {

    public static final EventInvoker<MobEffectEvents.Affects> AFFECTS = EventInvoker.lookup(MobEffectEvents.Affects.class);

    public static final EventInvoker<MobEffectEvents.Apply> APPLY = EventInvoker.lookup(MobEffectEvents.Apply.class);

    public static final EventInvoker<MobEffectEvents.Remove> REMOVE = EventInvoker.lookup(MobEffectEvents.Remove.class);

    public static final EventInvoker<MobEffectEvents.Expire> EXPIRE = EventInvoker.lookup(MobEffectEvents.Expire.class);

    private MobEffectEvents() {
    }

    @FunctionalInterface
    public interface Affects {

        EventResult onMobEffectAffects(LivingEntity var1, MobEffectInstance var2);
    }

    @FunctionalInterface
    public interface Apply {

        void onMobEffectApply(LivingEntity var1, MobEffectInstance var2, @Nullable MobEffectInstance var3, @Nullable Entity var4);
    }

    @FunctionalInterface
    public interface Expire {

        void onMobEffectExpire(LivingEntity var1, MobEffectInstance var2);
    }

    @FunctionalInterface
    public interface Remove {

        EventResult onMobEffectRemove(LivingEntity var1, MobEffectInstance var2);
    }
}