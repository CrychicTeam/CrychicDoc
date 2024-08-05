package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;

@FunctionalInterface
public interface BabyEntitySpawnCallback {

    EventInvoker<BabyEntitySpawnCallback> EVENT = EventInvoker.lookup(BabyEntitySpawnCallback.class);

    EventResult onBabyEntitySpawn(Mob var1, Mob var2, MutableValue<AgeableMob> var3);
}