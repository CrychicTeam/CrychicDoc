package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedFloat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface ShieldBlockCallback {

    EventInvoker<ShieldBlockCallback> EVENT = EventInvoker.lookup(ShieldBlockCallback.class);

    EventResult onShieldBlock(LivingEntity var1, DamageSource var2, DefaultedFloat var3);
}