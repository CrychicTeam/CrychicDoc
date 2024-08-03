package net.raphimc.immediatelyfast.injection.mixins.screen_batching;

import net.minecraft.client.gui.screens.ChatScreen;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.injection.processors.InjectAboveEverything;
import net.raphimc.immediatelyfast.injection.processors.InjectOnAllReturns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { ChatScreen.class }, priority = 1500)
public abstract class MixinChatScreen {

    @InjectAboveEverything
    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void beginBatching(CallbackInfo ci) {
        BatchingBuffers.beginHudBatching();
    }

    @InjectOnAllReturns
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void endBatching(CallbackInfo ci) {
        BatchingBuffers.endHudBatching();
    }
}