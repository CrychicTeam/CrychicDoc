package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

@FunctionalInterface
public interface CheckMobDespawnCallback {

    EventInvoker<CheckMobDespawnCallback> EVENT = EventInvoker.lookup(CheckMobDespawnCallback.class);

    EventResult onCheckMobDespawn(Mob var1, ServerLevel var2);
}