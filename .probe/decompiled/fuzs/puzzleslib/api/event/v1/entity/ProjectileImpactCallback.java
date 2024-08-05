package fuzs.puzzleslib.api.event.v1.entity;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;

@FunctionalInterface
public interface ProjectileImpactCallback {

    EventInvoker<ProjectileImpactCallback> EVENT = EventInvoker.lookup(ProjectileImpactCallback.class);

    EventResult onProjectileImpact(Projectile var1, HitResult var2);
}