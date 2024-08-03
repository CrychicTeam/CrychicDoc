package net.raphimc.immediatelyfast.injection.mixins.hud_batching.compat.highlighter;

import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "com.anthonyhilyard.highlighter.Highlighter" }, remap = false)
@Pseudo
public abstract class MixinHighlighter_Highlighter {

    @Inject(method = { "render" }, at = { @At("HEAD") })
    private static void renderItemOverlayIntoBufferStart(CallbackInfo ci) {
        BatchingBuffers.beginItemOverlayRendering();
    }

    @Inject(method = { "render" }, at = { @At("RETURN") })
    private static void renderItemOverlayIntoBufferEnd(CallbackInfo ci) {
        BatchingBuffers.endItemOverlayRendering();
    }
}