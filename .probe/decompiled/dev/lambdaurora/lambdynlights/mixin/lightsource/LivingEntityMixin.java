package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import dev.lambdaurora.lambdynlights.config.DynamicLightsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity implements DynamicLightSource {

    @Unique
    protected int lambdynlights$luminance;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public void tdv$dynamicLightTick() {
        if (DynamicLightsConfig.TileEntityLighting.get() && DynamicLightHandlers.canLightUp(this)) {
            if (!this.m_6060_() && !this.m_142038_()) {
                int luminance = 0;
                BlockPos eyePos = BlockPos.containing(this.m_20185_(), this.m_20188_(), this.m_20189_());
                boolean submergedInFluid = !this.m_9236_().getFluidState(eyePos).isEmpty();
                for (ItemStack equipped : this.m_20158_()) {
                    if (!equipped.isEmpty()) {
                        luminance = Math.max(luminance, LambDynLights.getLuminanceFromItemStack(equipped, submergedInFluid));
                    }
                }
                this.lambdynlights$luminance = luminance;
            } else {
                this.lambdynlights$luminance = 15;
            }
            int luminance = DynamicLightHandlers.getLuminanceFrom(this);
            if (luminance > this.lambdynlights$luminance) {
                this.lambdynlights$luminance = luminance;
            }
        } else {
            this.lambdynlights$luminance = 0;
        }
    }

    @Override
    public int tdv$getLuminance() {
        return this.lambdynlights$luminance;
    }
}