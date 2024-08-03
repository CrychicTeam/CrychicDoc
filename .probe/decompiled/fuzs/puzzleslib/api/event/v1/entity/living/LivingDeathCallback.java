package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingDeathCallback {

    EventInvoker<LivingDeathCallback> EVENT = EventInvoker.lookup(LivingDeathCallback.class);

    EventResult onLivingDeath(LivingEntity var1, DamageSource var2);
}