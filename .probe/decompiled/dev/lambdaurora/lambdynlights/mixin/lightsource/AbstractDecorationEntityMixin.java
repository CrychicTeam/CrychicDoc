package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ HangingEntity.class })
public abstract class AbstractDecorationEntityMixin extends Entity implements DynamicLightSource {

    public AbstractDecorationEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    private void onTick(CallbackInfo ci) {
        if (this.m_20193_().isClientSide()) {
            if (this.m_213877_()) {
                this.tdv$setDynamicLightEnabled(false);
            } else {
                if (DynamicLightsConfig.TileEntityLighting.get() && DynamicLightHandlers.canLightUp(this)) {
                    this.tdv$dynamicLightTick();
                } else {
                    this.tdv$resetDynamicLight();
                }
                LambDynLights.updateTracking(this);
            }
        }
    }
}