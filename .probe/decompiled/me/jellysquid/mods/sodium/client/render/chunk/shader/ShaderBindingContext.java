package me.jellysquid.mods.sodium.client.render.chunk.shader;

import java.util.function.IntFunction;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniform;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformBlock;

public interface ShaderBindingContext {

    <U extends GlUniform<?>> U bindUniform(String var1, IntFunction<U> var2);

    GlUniformBlock bindUniformBlock(String var1, int var2);
}