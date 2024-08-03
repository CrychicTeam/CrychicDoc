package net.raphimc.immediatelyfast.injection.mixins.fast_buffer_upload;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexBuffer;
import java.nio.ByteBuffer;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.fast_buffer_upload.PersistentMappedStreamingBuffer;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL45C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { VertexBuffer.class }, priority = 500)
public abstract class MixinVertexBuffer {

    @Shadow
    private int vertexBufferId;

    @Shadow
    private int indexBufferId;

    @Unique
    private int immediatelyFast$vertexBufferSize = -1;

    @Unique
    private int immediatelyFast$indexBufferSize = -1;

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glGenBuffers()I", remap = false))
    private int allocateOptimizableBuffer() {
        return ImmediatelyFast.persistentMappedStreamingBuffer != null ? GL45C.glCreateBuffers() : GL15C.glGenBuffers();
    }

    @Redirect(method = { "uploadVertexBuffer" }, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;glBufferData(ILjava/nio/ByteBuffer;I)V"))
    private void optimizeVertexDataUploading(int target, ByteBuffer data, int usage, @Local BufferBuilder.DrawState parameters) {
        int dataSize = data.remaining();
        if (dataSize != 0) {
            PersistentMappedStreamingBuffer streamingBuffer = ImmediatelyFast.persistentMappedStreamingBuffer;
            if (dataSize <= this.immediatelyFast$vertexBufferSize) {
                if (streamingBuffer != null && (long) dataSize <= streamingBuffer.getSize()) {
                    streamingBuffer.addUpload(this.vertexBufferId, data);
                    return;
                }
                if (streamingBuffer == null && ImmediatelyFast.runtimeConfig.legacy_fast_buffer_upload) {
                    GL15C.glBufferSubData(target, 0L, data);
                    return;
                }
            }
            this.immediatelyFast$vertexBufferSize = dataSize;
            if (streamingBuffer != null) {
                GL15C.glDeleteBuffers(this.vertexBufferId);
                this.vertexBufferId = GL45C.glCreateBuffers();
                GL45C.glNamedBufferStorage(this.vertexBufferId, data, 0);
                GL15C.glBindBuffer(34962, this.vertexBufferId);
                parameters.format().setupBufferState();
            } else {
                GL15C.glBufferData(target, data, usage);
            }
        }
    }

    @Redirect(method = { "uploadIndexBuffer" }, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;glBufferData(ILjava/nio/ByteBuffer;I)V"))
    private void optimizeIndexDataUploading(int target, ByteBuffer data, int usage) {
        int dataSize = data.remaining();
        if (dataSize != 0) {
            PersistentMappedStreamingBuffer streamingBuffer = ImmediatelyFast.persistentMappedStreamingBuffer;
            if (dataSize <= this.immediatelyFast$indexBufferSize) {
                if (streamingBuffer != null && (long) dataSize <= streamingBuffer.getSize()) {
                    streamingBuffer.addUpload(this.indexBufferId, data);
                    return;
                }
                if (streamingBuffer == null && ImmediatelyFast.runtimeConfig.legacy_fast_buffer_upload) {
                    GL15C.glBufferSubData(target, 0L, data);
                    return;
                }
            }
            this.immediatelyFast$indexBufferSize = dataSize;
            if (streamingBuffer != null) {
                GL15C.glDeleteBuffers(this.indexBufferId);
                this.indexBufferId = GL45C.glCreateBuffers();
                GL45C.glNamedBufferStorage(this.indexBufferId, data, 0);
                GL15C.glBindBuffer(34963, this.indexBufferId);
            } else {
                GL15C.glBufferData(target, data, usage);
            }
        }
    }

    @Inject(method = { "upload" }, at = { @At("RETURN") })
    private void flushBuffers(CallbackInfo ci) {
        if (ImmediatelyFast.persistentMappedStreamingBuffer != null) {
            ImmediatelyFast.persistentMappedStreamingBuffer.flush();
        }
    }

    @Redirect(method = { "close" }, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;glDeleteBuffers(I)V"))
    private void deleteBufferProperly(int buffer) {
        if (ImmediatelyFast.persistentMappedStreamingBuffer != null) {
            GL15C.glDeleteBuffers(buffer);
        } else {
            RenderSystem.glDeleteBuffers(buffer);
        }
    }
}