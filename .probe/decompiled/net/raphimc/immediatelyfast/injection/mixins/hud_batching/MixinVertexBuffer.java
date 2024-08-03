package net.raphimc.immediatelyfast.injection.mixins.hud_batching;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffer;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ VertexBuffer.class })
public abstract class MixinVertexBuffer {

    @Unique
    private static boolean immediatelyFast$isForceDrawing;

    @Shadow
    public abstract void bind();

    @Inject(method = { "drawInternal" }, at = { @At("HEAD") })
    private void checkForDrawCallWhileBatching(CallbackInfo ci) {
        if (!immediatelyFast$isForceDrawing && !BatchingBuffer.IS_DRAWING && BatchingBuffers.FILL_CONSUMER != null && BatchingBuffers.hasDataToDraw()) {
            immediatelyFast$isForceDrawing = true;
            BatchingBuffers.forceDrawBuffers();
            this.bind();
            immediatelyFast$isForceDrawing = false;
        }
    }
}