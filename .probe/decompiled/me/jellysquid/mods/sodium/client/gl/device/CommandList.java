package me.jellysquid.mods.sodium.client.gl.device;

import java.nio.ByteBuffer;
import me.jellysquid.mods.sodium.client.gl.array.GlVertexArray;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferMapFlags;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferMapping;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferStorageFlags;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferTarget;
import me.jellysquid.mods.sodium.client.gl.buffer.GlBufferUsage;
import me.jellysquid.mods.sodium.client.gl.buffer.GlImmutableBuffer;
import me.jellysquid.mods.sodium.client.gl.buffer.GlMutableBuffer;
import me.jellysquid.mods.sodium.client.gl.sync.GlFence;
import me.jellysquid.mods.sodium.client.gl.tessellation.GlPrimitiveType;
import me.jellysquid.mods.sodium.client.gl.tessellation.GlTessellation;
import me.jellysquid.mods.sodium.client.gl.tessellation.TessellationBinding;
import me.jellysquid.mods.sodium.client.gl.util.EnumBitField;

public interface CommandList extends AutoCloseable {

    GlMutableBuffer createMutableBuffer();

    GlImmutableBuffer createImmutableBuffer(long var1, EnumBitField<GlBufferStorageFlags> var3);

    GlTessellation createTessellation(GlPrimitiveType var1, TessellationBinding[] var2);

    void bindVertexArray(GlVertexArray var1);

    void uploadData(GlMutableBuffer var1, ByteBuffer var2, GlBufferUsage var3);

    void copyBufferSubData(GlBuffer var1, GlBuffer var2, long var3, long var5, long var7);

    void bindBuffer(GlBufferTarget var1, GlBuffer var2);

    void unbindVertexArray();

    void allocateStorage(GlMutableBuffer var1, long var2, GlBufferUsage var4);

    void deleteBuffer(GlBuffer var1);

    void deleteVertexArray(GlVertexArray var1);

    void flush();

    DrawCommandList beginTessellating(GlTessellation var1);

    void deleteTessellation(GlTessellation var1);

    default void close() {
        this.flush();
    }

    GlBufferMapping mapBuffer(GlBuffer var1, long var2, long var4, EnumBitField<GlBufferMapFlags> var6);

    void unmap(GlBufferMapping var1);

    void flushMappedRange(GlBufferMapping var1, int var2, int var3);

    GlFence createFence();
}