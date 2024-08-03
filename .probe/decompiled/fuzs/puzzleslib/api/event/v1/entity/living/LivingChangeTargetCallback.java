package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedValue;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingChangeTargetCallback {

    EventInvoker<LivingChangeTargetCallback> EVENT = EventInvoker.lookup(LivingChangeTargetCallback.class);

    EventResult onLivingChangeTarget(LivingEntity var1, DefaultedValue<LivingEntity> var2);
}