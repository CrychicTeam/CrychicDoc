package me.jellysquid.mods.sodium.mixin.core.render.immediate.consumer;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultedVertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import me.jellysquid.mods.sodium.client.render.vertex.buffer.ExtendedBufferBuilder;
import me.jellysquid.mods.sodium.client.render.vertex.buffer.SodiumBufferBuilder;
import net.caffeinemc.mods.sodium.api.memory.MemoryIntrinsics;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatRegistry;
import net.caffeinemc.mods.sodium.api.vertex.serializer.VertexSerializerRegistry;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ BufferBuilder.class })
public abstract class BufferBuilderMixin extends DefaultedVertexConsumer implements VertexBufferWriter, ExtendedBufferBuilder {

    @Shadow
    private ByteBuffer buffer;

    @Shadow
    private int vertices;

    @Shadow
    private int nextElementByte;

    @Shadow
    private VertexFormat.Mode mode;

    @Unique
    private VertexFormatDescription format;

    @Unique
    private int stride;

    private SodiumBufferBuilder fastDelegate;

    @Shadow
    protected abstract void ensureCapacity(int var1);

    @Inject(method = { "switchFormat" }, at = { @At(value = "FIELD", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;format:Lcom/mojang/blaze3d/vertex/VertexFormat;", opcode = 181) })
    private void onFormatChanged(VertexFormat format, CallbackInfo ci) {
        this.format = VertexFormatRegistry.instance().get(format);
        this.stride = format.getVertexSize();
        this.fastDelegate = this.format.isSimpleFormat() ? new SodiumBufferBuilder(this) : null;
    }

    @Inject(method = { "discard", "reset", "begin" }, at = { @At("RETURN") })
    private void resetDelegate(CallbackInfo ci) {
        if (this.fastDelegate != null) {
            this.fastDelegate.reset();
        }
    }

    @Override
    public boolean canUseIntrinsics() {
        return this.format != null && this.format.isSimpleFormat();
    }

    @Override
    public void push(MemoryStack stack, long src, int count, VertexFormatDescription format) {
        int length = count * this.stride;
        this.ensureCapacity(length + this.stride);
        long dst = MemoryUtil.memAddress(this.buffer, this.nextElementByte);
        if (format == this.format) {
            MemoryIntrinsics.copyMemory(src, dst, length);
        } else {
            this.copySlow(src, dst, count, format);
        }
        this.vertices += count;
        this.nextElementByte += length;
    }

    @Unique
    private void copySlow(long src, long dst, int count, VertexFormatDescription format) {
        VertexSerializerRegistry.instance().get(format, this.format).serialize(src, dst, count);
    }

    @Override
    public ByteBuffer sodium$getBuffer() {
        return this.buffer;
    }

    @Override
    public int sodium$getElementOffset() {
        return this.nextElementByte;
    }

    @Override
    public VertexFormatDescription sodium$getFormatDescription() {
        return this.format;
    }

    @Override
    public SodiumBufferBuilder sodium$getDelegate() {
        return this.fastDelegate;
    }

    @Unique
    private boolean shouldDuplicateVertices() {
        return this.mode == VertexFormat.Mode.LINES || this.mode == VertexFormat.Mode.LINE_STRIP;
    }

    @Unique
    private void duplicateVertex() {
        MemoryIntrinsics.copyMemory(MemoryUtil.memAddress(this.buffer, this.nextElementByte - this.stride), MemoryUtil.memAddress(this.buffer, this.nextElementByte), this.stride);
        this.nextElementByte = this.nextElementByte + this.stride;
        this.vertices++;
        this.ensureCapacity(this.stride);
    }

    @Override
    public void sodium$moveToNextVertex() {
        this.vertices++;
        this.nextElementByte = this.nextElementByte + this.stride;
        this.ensureCapacity(this.stride);
        if (this.shouldDuplicateVertices()) {
            this.duplicateVertex();
        }
    }

    @Override
    public boolean sodium$usingFixedColor() {
        return this.f_85824_;
    }
}