package me.jellysquid.mods.sodium.client.gl.shader.uniform;

import me.jellysquid.mods.sodium.client.gl.buffer.GlBuffer;
import org.lwjgl.opengl.GL32C;

public class GlUniformBlock {

    private final int binding;

    public GlUniformBlock(int uniformBlockBinding) {
        this.binding = uniformBlockBinding;
    }

    public void bindBuffer(GlBuffer buffer) {
        GL32C.glBindBufferBase(35345, this.binding, buffer.handle());
    }
}