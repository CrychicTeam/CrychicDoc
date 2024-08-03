package net.raphimc.immediatelyfast.injection.mixins.hud_batching.compat.armorchroma;

import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "nukeduck.armorchroma.GuiArmor" }, remap = false)
@Pseudo
public abstract class MixinArmorChroma_GuiArmor {

    @Unique
    private boolean immediatelyFast$wasHudBatching;

    @Inject(method = { "draw" }, at = { @At("HEAD") })
    private void endHudBatching(CallbackInfo ci) {
        if (ImmediatelyFast.runtimeConfig.hud_batching && BatchingBuffers.isHudBatching()) {
            BatchingBuffers.endHudBatching();
            this.immediatelyFast$wasHudBatching = true;
        }
    }

    @Inject(method = { "draw" }, at = { @At("RETURN") })
    private void beginHudBatching(CallbackInfo ci) {
        if (this.immediatelyFast$wasHudBatching) {
            BatchingBuffers.beginHudBatching();
            this.immediatelyFast$wasHudBatching = false;
        }
    }
}