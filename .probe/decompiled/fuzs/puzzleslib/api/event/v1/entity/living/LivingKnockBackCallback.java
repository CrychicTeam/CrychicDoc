package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedDouble;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingKnockBackCallback {

    EventInvoker<LivingKnockBackCallback> EVENT = EventInvoker.lookup(LivingKnockBackCallback.class);

    EventResult onLivingKnockBack(LivingEntity var1, DefaultedDouble var2, DefaultedDouble var3, DefaultedDouble var4);
}