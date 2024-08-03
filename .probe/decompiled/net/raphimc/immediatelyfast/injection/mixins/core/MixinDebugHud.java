package net.raphimc.immediatelyfast.injection.mixins.core;

import java.util.List;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.core.BufferBuilderPool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { DebugScreenOverlay.class }, priority = 9999)
public abstract class MixinDebugHud {

    @Inject(method = { "getRightText" }, at = { @At("RETURN") })
    private void appendAllocationInfo(CallbackInfoReturnable<List<String>> cir) {
        if (!ImmediatelyFast.config.dont_add_info_into_debug_hud) {
            ((List) cir.getReturnValue()).add("");
            ((List) cir.getReturnValue()).add("ImmediatelyFast " + ImmediatelyFast.VERSION);
            ((List) cir.getReturnValue()).add("Buffer Pool: " + BufferBuilderPool.getAllocatedSize());
            if (ImmediatelyFast.persistentMappedStreamingBuffer != null) {
                ((List) cir.getReturnValue()).add("Streaming Buffer: " + this.immediatelyFast$bytesToMiB(ImmediatelyFast.persistentMappedStreamingBuffer.getOffset()) + "/" + this.immediatelyFast$bytesToMiB(ImmediatelyFast.persistentMappedStreamingBuffer.getSize()) + " MiB");
            }
        }
    }

    @Unique
    private String immediatelyFast$bytesToMiB(long bytes) {
        return String.format("%.0f", (float) bytes / 1024.0F / 1024.0F);
    }
}