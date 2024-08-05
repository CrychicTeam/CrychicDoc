package net.raphimc.immediatelyfast.injection.mixins.core;

import com.mojang.blaze3d.platform.Window;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Window.class })
public abstract class MixinWindow {

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void initImmediatelyFast(CallbackInfo ci) {
        ImmediatelyFast.windowInit();
    }
}