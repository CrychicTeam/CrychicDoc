package net.minecraftforge.event.entity;

import java.util.Objects;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.NotNull;

@Cancelable
public class ProjectileImpactEvent extends EntityEvent {

    private final HitResult ray;

    private final Projectile projectile;

    private ProjectileImpactEvent.ImpactResult result = ProjectileImpactEvent.ImpactResult.DEFAULT;

    public ProjectileImpactEvent(Projectile projectile, HitResult ray) {
        super(projectile);
        this.ray = ray;
        this.projectile = projectile;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    public void setCanceled(boolean cancel) {
        super.setCanceled(cancel);
    }

    public HitResult getRayTraceResult() {
        return this.ray;
    }

    public Projectile getProjectile() {
        return this.projectile;
    }

    public void setImpactResult(@NotNull ProjectileImpactEvent.ImpactResult newResult) {
        this.result = (ProjectileImpactEvent.ImpactResult) Objects.requireNonNull(newResult, "ImpactResult cannot be null");
    }

    public ProjectileImpactEvent.ImpactResult getImpactResult() {
        return this.result;
    }

    public static enum ImpactResult {

        DEFAULT, SKIP_ENTITY, STOP_AT_CURRENT, STOP_AT_CURRENT_NO_DAMAGE
    }
}