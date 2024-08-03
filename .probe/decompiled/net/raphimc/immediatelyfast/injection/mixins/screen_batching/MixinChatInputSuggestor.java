package net.raphimc.immediatelyfast.injection.mixins.screen_batching;

import net.minecraft.client.gui.components.CommandSuggestions;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.injection.processors.InjectAboveEverything;
import net.raphimc.immediatelyfast.injection.processors.InjectOnAllReturns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { CommandSuggestions.class }, priority = 1500)
public abstract class MixinChatInputSuggestor {

    @Unique
    private boolean immediatelyFast$wasBatching;

    @InjectAboveEverything
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void beginBatching(CallbackInfo ci) {
        this.immediatelyFast$wasBatching = BatchingBuffers.isHudBatching();
        if (!this.immediatelyFast$wasBatching) {
            BatchingBuffers.beginHudBatching();
        }
    }

    @InjectOnAllReturns
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void endBatching(CallbackInfo ci) {
        if (!this.immediatelyFast$wasBatching) {
            BatchingBuffers.endHudBatching();
        }
    }
}