package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ AbstractHurtingProjectile.class })
public abstract class ExplosiveProjectileEntityMixin extends Entity implements DynamicLightSource {

    public ExplosiveProjectileEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tdv$dynamicLightTick() {
        if (!this.tdv$isDynamicLightEnabled()) {
            this.tdv$setDynamicLightEnabled(true);
        }
    }

    @Override
    public int tdv$getLuminance() {
        return DynamicLightsConfig.TileEntityLighting.get() && DynamicLightHandlers.canLightUp(this) ? 14 : 0;
    }
}