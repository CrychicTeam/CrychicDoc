package com.corosus.mobtimizations.mixin;

import com.corosus.mobtimizations.Mobtimizations;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PathNavigation.class })
public abstract class MixinRecomputePath2 {

    @Inject(method = { "recomputePath" }, at = { @At("HEAD") }, cancellable = true)
    public void recomputePath(CallbackInfo ci) {
        if (!Mobtimizations.canRecomputePath()) {
            Mobtimizations.incCancel();
            ci.cancel();
        }
    }
}