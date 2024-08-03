package net.raphimc.immediatelyfast.injection.mixins.core;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.core.BatchableBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ MultiBufferSource.class })
public interface MixinVertexConsumerProvider {

    @Overwrite
    static MultiBufferSource.BufferSource immediateWithBuffers(Map<RenderType, BufferBuilder> layerBuffers, BufferBuilder fallbackBuffer) {
        if (!ImmediatelyFast.config.debug_only_and_not_recommended_disable_universal_batching && fallbackBuffer != null) {
            return new BatchableBufferSource(fallbackBuffer, layerBuffers);
        } else {
            if (fallbackBuffer == null) {
                fallbackBuffer = Tesselator.getInstance().getBuilder();
            }
            return new MultiBufferSource.BufferSource(fallbackBuffer, layerBuffers);
        }
    }
}