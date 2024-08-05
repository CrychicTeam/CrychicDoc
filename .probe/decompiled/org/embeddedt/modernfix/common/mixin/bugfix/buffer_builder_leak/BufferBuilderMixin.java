package org.embeddedt.modernfix.common.mixin.bugfix.buffer_builder_leak;

import com.mojang.blaze3d.vertex.BufferBuilder;
import java.nio.ByteBuffer;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.render.UnsafeBufferHelper;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { BufferBuilder.class }, priority = 1500)
@ClientOnlyMixin
public class BufferBuilderMixin {

    @Shadow
    private ByteBuffer buffer;

    private static boolean leakReported = false;

    private boolean mfix$shouldFree = true;

    @Inject(method = { "flywheel$injectForRender" }, at = { @At("RETURN") }, remap = false, require = 0)
    @Dynamic
    private void preventFree(CallbackInfo ci) {
        this.mfix$shouldFree = false;
    }

    @Inject(method = { "<clinit>" }, at = { @At("RETURN") })
    private static void initUnsafeBufferHelper(CallbackInfo ci) {
        UnsafeBufferHelper.init();
    }

    protected void finalize() throws Throwable {
        try {
            ByteBuffer buf = this.buffer;
            if (buf != null && this.mfix$shouldFree) {
                if (!leakReported) {
                    leakReported = true;
                    ModernFix.LOGGER.warn("One or more BufferBuilders have been leaked, ModernFix will attempt to correct this.");
                }
                UnsafeBufferHelper.free(buf);
                this.buffer = null;
            }
        } finally {
            super.finalize();
        }
    }
}