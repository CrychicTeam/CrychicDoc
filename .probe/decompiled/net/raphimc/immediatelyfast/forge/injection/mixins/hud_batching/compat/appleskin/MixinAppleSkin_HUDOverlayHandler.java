package net.raphimc.immediatelyfast.forge.injection.mixins.hud_batching.compat.appleskin;

import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "squeek.appleskin.client.HUDOverlayHandler" }, remap = false)
@Pseudo
public abstract class MixinAppleSkin_HUDOverlayHandler {

    @Inject(method = { "drawExhaustionOverlay" }, at = { @At("RETURN") })
    private static void forceDrawBatch(CallbackInfo ci) {
        if (ImmediatelyFast.runtimeConfig.hud_batching && BatchingBuffers.isHudBatching()) {
            BatchingBuffers.endHudBatching();
            BatchingBuffers.beginHudBatching();
        }
    }
}