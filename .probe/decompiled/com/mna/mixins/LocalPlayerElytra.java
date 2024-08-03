package com.mna.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mna.effects.beneficial.EffectSoar;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ LocalPlayer.class })
public class LocalPlayerElytra {

    @ModifyExpressionValue(method = { "aiStep" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z", remap = false) })
    public boolean elytraOverride(boolean original) {
        return original || EffectSoar.canSoar((LivingEntity) this);
    }
}