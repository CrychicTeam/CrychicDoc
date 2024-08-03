package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ NearestAttackableTargetGoal.class })
public abstract class MixinNearestAttackableTargetGoal {

    @Inject(method = { "canUse" }, at = { @At("HEAD") }, cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        NearestAttackableTargetGoal self = (NearestAttackableTargetGoal) this;
        if (!Mobtimizations.canTarget(self.f_26135_)) {
            Mobtimizations.incCancel();
            cir.setReturnValue(false);
        }
    }
}