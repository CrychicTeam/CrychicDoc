package net.raphimc.immediatelyfast.injection.mixins.screen_batching;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { AbstractContainerScreen.class }, priority = 500)
public abstract class MixinHandledScreen {

    @Inject(method = { "render" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;focusedSlot:Lnet/minecraft/screen/slot/Slot;", ordinal = 0) })
    private void beginBatching(CallbackInfo ci) {
        BatchingBuffers.beginHudBatching();
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/gui/DrawContext;II)V", shift = Shift.BEFORE) })
    private void endBatching(CallbackInfo ci) {
        BatchingBuffers.endHudBatching();
    }
}