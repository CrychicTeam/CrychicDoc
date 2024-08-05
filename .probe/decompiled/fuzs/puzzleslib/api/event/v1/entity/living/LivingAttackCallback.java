package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface LivingAttackCallback {

    EventInvoker<LivingAttackCallback> EVENT = EventInvoker.lookup(LivingAttackCallback.class);

    EventResult onLivingAttack(LivingEntity var1, DamageSource var2, float var3);
}