package net.raphimc.immediatelyfast.injection.mixins.core;

import com.mojang.blaze3d.platform.GlDebug;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ GlDebug.class })
public abstract class MixinGlDebug {

    @Unique
    private static long immediatelyFast$lastTime;

    @Inject(method = { "info" }, at = { @At("RETURN") })
    private static void printAdditionalInfo(CallbackInfo ci) {
        if (ImmediatelyFast.config.debug_only_print_additional_error_information && System.currentTimeMillis() - immediatelyFast$lastTime > 1000L) {
            immediatelyFast$lastTime = System.currentTimeMillis();
            Thread.dumpStack();
        }
    }
}