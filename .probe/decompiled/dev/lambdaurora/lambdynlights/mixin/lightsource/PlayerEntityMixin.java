package dev.lambdaurora.lambdynlights.mixin.lightsource;

import dev.lambdaurora.lambdynlights.DynamicLightSource;
import dev.lambdaurora.lambdynlights.LambDynLights;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ Player.class })
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource {

    @Unique
    protected int lambdynlights$luminance;

    @Unique
    private Level lambdynlights$lastWorld;

    @Shadow
    @Override
    public abstract boolean isSpectator();

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tdv$dynamicLightTick() {
        if (!DynamicLightHandlers.canLightUp(this)) {
            this.lambdynlights$luminance = 0;
        } else {
            if (!this.m_6060_() && !this.m_142038_()) {
                int luminance = DynamicLightHandlers.getLuminanceFrom(this);
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
            if (this.isSpectator()) {
                this.lambdynlights$luminance = 0;
            }
            if (this.lambdynlights$lastWorld != this.m_20193_()) {
                this.lambdynlights$lastWorld = this.m_20193_();
                this.lambdynlights$luminance = 0;
            }
        }
    }

    @Override
    public int tdv$getLuminance() {
        return this.lambdynlights$luminance;
    }
}