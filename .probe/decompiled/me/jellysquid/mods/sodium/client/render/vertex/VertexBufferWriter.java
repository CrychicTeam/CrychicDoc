package me.jellysquid.mods.sodium.client.render.vertex;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.lwjgl.system.MemoryStack;

@Deprecated
public interface VertexBufferWriter extends net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter {

    @Deprecated
    static VertexBufferWriter of(VertexConsumer consumer) {
        return (VertexBufferWriter) consumer;
    }

    @Deprecated
    void push(MemoryStack var1, long var2, int var4, VertexFormatDescription var5);

    @Override
    default void push(MemoryStack stack, long ptr, int count, net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription format) {
        this.push(stack, ptr, count, VertexFormatDescription.translateModern(format));
    }

    @Override
    default boolean canUseIntrinsics() {
        return true;
    }
}