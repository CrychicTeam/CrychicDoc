package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingFallCallback {

    EventInvoker<LivingFallCallback> EVENT = EventInvoker.lookup(LivingFallCallback.class);

    EventResult onLivingFall(LivingEntity var1, MutableFloat var2, MutableFloat var3);
}