package com.mna.mixins;

import com.mna.effects.EffectInit;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public class LivingEntityOnClimbableMixin {

    @Inject(at = { @At("RETURN") }, method = { "onClimbable" }, cancellable = true)
    public void mna$onClimbable(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) this;
        if (self.hasEffect(EffectInit.SPIDER_CLIMBING.get()) && self.f_19862_) {
            cir.setReturnValue(true);
        }
    }
}