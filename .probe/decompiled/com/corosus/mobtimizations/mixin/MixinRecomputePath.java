package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ PathNavigation.class })
public abstract class MixinRecomputePath {

    @Inject(method = { "shouldRecomputePath" }, at = { @At("HEAD") }, cancellable = true)
    public void shouldRecomputePath(BlockPos blockPos0, CallbackInfoReturnable<Boolean> cir) {
        if (!Mobtimizations.canRecomputePath()) {
            Mobtimizations.incCancel();
            cir.setReturnValue(false);
        }
    }
}