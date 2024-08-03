package net.mehvahdjukaar.dummmmmmy.mixins;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Arrow.class })
public abstract class ArrowMixin {

    @Inject(method = { "doPostHurtEffects" }, at = { @At("HEAD") })
    protected void allowEffectsToHurt(LivingEntity target, CallbackInfo ci, @Share("lastInvTimer") LocalIntRef lastInvTimer) {
        lastInvTimer.set(target.f_19802_);
        target.f_19802_ = 0;
    }

    @Inject(method = { "doPostHurtEffects" }, at = { @At("RETURN") })
    protected void resetInvTimer(LivingEntity target, CallbackInfo ci, @Share("lastInvTimer") LocalIntRef lastInvTimer) {
        target.f_19802_ = Math.max(target.f_19802_, lastInvTimer.get());
    }
}