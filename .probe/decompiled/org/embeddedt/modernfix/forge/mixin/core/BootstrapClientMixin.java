package org.embeddedt.modernfix.forge.mixin.core;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.Bootstrap;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Bootstrap.class })
@ClientOnlyMixin
public class BootstrapClientMixin {

    @Inject(method = { "validate" }, at = { @At("HEAD") })
    private static void loadClientClasses(CallbackInfo ci) {
        RenderType.solid();
    }
}