package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ RandomStrollGoal.class })
public abstract class MixinPreventWandering {

    @Shadow
    protected PathfinderMob mob;

    @Inject(method = { "canUse" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/RandomStrollGoal;getPosition()Lnet/minecraft/world/phys/Vec3;", shift = Shift.BEFORE) }, cancellable = true)
    public void canUse(CallbackInfoReturnable<Boolean> cir) {
        if (!Mobtimizations.canWander(this.mob)) {
            Mobtimizations.incCancel();
            cir.setReturnValue(false);
        }
    }
}