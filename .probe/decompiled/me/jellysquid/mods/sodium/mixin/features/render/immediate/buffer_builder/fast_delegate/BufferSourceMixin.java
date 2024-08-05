package me.jellysquid.mods.sodium.mixin.features.render.immediate.buffer_builder.fast_delegate;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Set;
import me.jellysquid.mods.sodium.client.render.vertex.buffer.ExtendedBufferBuilder;
import me.jellysquid.mods.sodium.client.render.vertex.buffer.SodiumBufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({ MultiBufferSource.BufferSource.class })
public class BufferSourceMixin {

    @Shadow
    @Final
    @Mutable
    protected final Set<BufferBuilder> startedBuffers = new ReferenceOpenHashSet();

    @ModifyReturnValue(method = { "getBuffer" }, at = { @At("RETURN") })
    private VertexConsumer useFasterVertexConsumer(VertexConsumer vertexConsumer) {
        if (vertexConsumer instanceof ExtendedBufferBuilder bufferBuilder) {
            SodiumBufferBuilder replacement = bufferBuilder.sodium$getDelegate();
            if (replacement != null) {
                return replacement;
            }
        }
        return vertexConsumer;
    }

    @ModifyVariable(method = { "lambda$endBatch$0" }, at = @At(value = "LOAD", ordinal = 0))
    private VertexConsumer changeComparedVertexConsumer(VertexConsumer input) {
        return (VertexConsumer) (input instanceof SodiumBufferBuilder replacement ? replacement.getOriginalBufferBuilder() : input);
    }
}