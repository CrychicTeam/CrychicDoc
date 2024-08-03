package com.nameless.indestructible.mixin;

import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

@Mixin({ AttackAnimation.class })
public class AttackAnimationMixin {

    @Inject(method = { "getPlaySpeed(Lyesman/epicfight/world/capabilities/entitypatch/LivingEntityPatch;)F" }, at = { @At("RETURN") }, cancellable = true, remap = false)
    private void onGetPlaySpeed(LivingEntityPatch<?> entitypatch, CallbackInfoReturnable<Float> cir) {
        if (entitypatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch) {
            cir.setReturnValue(advancedCustomHumanoidMobPatch.getAttackSpeed());
        }
    }
}