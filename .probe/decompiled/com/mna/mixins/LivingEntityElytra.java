package com.mna.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mna.effects.beneficial.EffectSoar;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ LivingEntity.class })
public class LivingEntityElytra {

    @ModifyExpressionValue(method = { "updateFallFlying" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z", remap = false) })
    public boolean elytraOverride(boolean original) {
        return original || EffectSoar.canSoar((LivingEntity) this);
    }

    @ModifyExpressionValue(method = { "updateFallFlying" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;elytraFlightTick(Lnet/minecraft/world/entity/LivingEntity;I)Z", remap = false) })
    public boolean eytraValidOverride(boolean original) {
        return original || EffectSoar.canSoar((LivingEntity) this);
    }
}