package io.github.apace100.origins.mixin;

import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public final class WaterBreathingMixin {

    @Mixin({ LivingEntity.class })
    public abstract static class CanBreatheInWater extends Entity {

        public CanBreatheInWater(EntityType<?> type, Level world) {
            super(type, world);
        }

        @Inject(at = { @At("HEAD") }, method = { "canBreatheUnderwater" }, cancellable = true)
        public void doWaterBreathing(CallbackInfoReturnable<Boolean> info) {
            if (IPowerContainer.hasPower(this, OriginsPowerTypes.WATER_BREATHING.get())) {
                info.setReturnValue(true);
            }
        }
    }

    @Mixin({ Player.class })
    public abstract static class UpdateAir extends LivingEntity {

        protected UpdateAir(EntityType<? extends LivingEntity> entityType, Level world) {
            super(entityType, world);
        }

        @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isEyeInFluid(Lnet/minecraft/tags/TagKey;)Z"), method = { "turtleHelmetTick" })
        public boolean isSubmergedInProxy(Player player, TagKey<Fluid> fluidTag) {
            boolean submerged = this.isEyeInFluidType(ForgeMod.WATER_TYPE.get());
            return IPowerContainer.hasPower(this, OriginsPowerTypes.WATER_BREATHING.get()) != submerged;
        }
    }
}