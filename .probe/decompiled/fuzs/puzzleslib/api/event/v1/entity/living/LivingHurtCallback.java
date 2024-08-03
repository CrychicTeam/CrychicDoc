package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingHurtCallback {

    EventInvoker<LivingHurtCallback> EVENT = EventInvoker.lookup(LivingHurtCallback.class);

    EventResult onLivingHurt(LivingEntity var1, DamageSource var2, MutableFloat var3);
}