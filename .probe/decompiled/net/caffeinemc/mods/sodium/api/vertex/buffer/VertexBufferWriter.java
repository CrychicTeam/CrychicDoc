package net.caffeinemc.mods.sodium.api.vertex.buffer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.memory.MemoryIntrinsics;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryStack;

public interface VertexBufferWriter {

    static VertexBufferWriter of(VertexConsumer consumer) {
        if (consumer instanceof VertexBufferWriter writer && writer.canUseIntrinsics()) {
            return writer;
        }
        throw createUnsupportedVertexConsumerThrowable(consumer);
    }

    @Nullable
    static VertexBufferWriter tryOf(VertexConsumer consumer) {
        if (consumer instanceof VertexBufferWriter writer && writer.canUseIntrinsics()) {
            return writer;
        }
        return null;
    }

    private static RuntimeException createUnsupportedVertexConsumerThrowable(VertexConsumer consumer) {
        Class<? extends VertexConsumer> clazz = consumer.getClass();
        String name = clazz.getName();
        return new IllegalArgumentException("The class %s does not implement interface VertexBufferWriter, which is required for compatibility with Sodium (see: https://github.com/CaffeineMC/sodium-fabric/issues/1620)".formatted(name));
    }

    void push(MemoryStack var1, long var2, int var4, VertexFormatDescription var5);

    @Deprecated
    default boolean isFullWriter() {
        return true;
    }

    default boolean canUseIntrinsics() {
        return this.isFullWriter();
    }

    static void copyInto(VertexBufferWriter writer, MemoryStack stack, long ptr, int count, VertexFormatDescription format) {
        int length = count * format.stride();
        long copy = stack.nmalloc(length);
        MemoryIntrinsics.copyMemory(ptr, copy, length);
        writer.push(stack, copy, count, format);
    }
}