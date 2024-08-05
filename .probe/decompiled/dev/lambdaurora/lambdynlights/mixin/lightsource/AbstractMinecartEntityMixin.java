package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AbstractMinecart.class })
public abstract class AbstractMinecartEntityMixin extends Entity implements DynamicLightSource {

    @Unique
    private int lambdynlights$luminance;

    @Shadow
    public abstract BlockState getDisplayBlockState();

    public AbstractMinecartEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    private void onTick(CallbackInfo ci) {
        if (this.m_9236_().isClientSide()) {
            if (this.m_213877_()) {
                this.tdv$setDynamicLightEnabled(false);
            } else {
                if (DynamicLightsConfig.TileEntityLighting.get() && DynamicLightHandlers.canLightUp(this)) {
                    this.tdv$dynamicLightTick();
                } else {
                    this.lambdynlights$luminance = 0;
                }
                LambDynLights.updateTracking(this);
            }
        }
    }

    @Override
    public void tdv$dynamicLightTick() {
        this.lambdynlights$luminance = Math.max(Math.max(this.m_6060_() ? 15 : 0, this.getDisplayBlockState().m_60791_()), DynamicLightHandlers.getLuminanceFrom(this));
    }

    @Override
    public int tdv$getLuminance() {
        return this.lambdynlights$luminance;
    }
}