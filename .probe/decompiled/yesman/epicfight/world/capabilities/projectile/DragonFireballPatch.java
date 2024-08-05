package yesman.epicfight.world.capabilities.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

public class DragonFireballPatch extends ProjectilePatch<DragonFireball> {

    public void onJoinWorld(DragonFireball projectileEntity, EntityJoinLevelEvent event) {
        super.onJoinWorld(projectileEntity, event);
        this.impact = 1.0F;
        projectileEntity.f_36813_ *= 2.0;
        projectileEntity.f_36814_ *= 2.0;
        projectileEntity.f_36815_ *= 2.0;
    }

    protected void setMaxStrikes(DragonFireball projectileEntity, int maxStrikes) {
    }

    @Override
    public boolean onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getRayTraceResult() instanceof EntityHitResult) {
            Entity entity = ((EntityHitResult) event.getRayTraceResult()).getEntity();
            if (!entity.is(event.getProjectile().getOwner())) {
                entity.hurt(entity.level().damageSources().indirectMagic(event.getProjectile(), event.getProjectile().getOwner()), 8.0F);
            }
        }
        return false;
    }
}