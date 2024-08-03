package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PrimedTnt.class })
public abstract class TntEntityMixin extends Entity implements DynamicLightSource {

    @Unique
    private int startFuseTimer = 80;

    @Unique
    private int lambdynlights$luminance;

    @Shadow
    public abstract int getFuse();

    public TntEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = { "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V" }, at = { @At("TAIL") })
    private void onNew(EntityType<? extends PrimedTnt> entityType, Level world, CallbackInfo ci) {
        this.startFuseTimer = this.getFuse();
    }

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    private void onTick(CallbackInfo ci) {
        if (this.m_20193_().isClientSide()) {
            if (!LambDynLights.isEnabled()) {
                return;
            }
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

    @Override
    public void tdv$dynamicLightTick() {
        if (this.m_6060_()) {
            this.lambdynlights$luminance = 15;
        } else if (LambDynLights.isEnabled()) {
            int fuse = this.getFuse() / this.startFuseTimer;
            this.lambdynlights$luminance = (int) ((double) (-(fuse * fuse)) * 10.0) + 10;
        } else {
            this.lambdynlights$luminance = 10;
        }
    }

    @Override
    public int tdv$getLuminance() {
        return this.lambdynlights$luminance;
    }
}