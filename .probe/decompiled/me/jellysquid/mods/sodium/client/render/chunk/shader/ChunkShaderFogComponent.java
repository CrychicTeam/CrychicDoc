package me.jellysquid.mods.sodium.client.render.chunk.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformFloat;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformFloat4v;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;

public abstract class ChunkShaderFogComponent {

    public abstract void setup();

    public static class None extends ChunkShaderFogComponent {

        public None(ShaderBindingContext context) {
        }

        @Override
        public void setup() {
        }
    }

    public static class Smooth extends ChunkShaderFogComponent {

        private final GlUniformFloat4v uFogColor;

        private final GlUniformInt uFogShape;

        private final GlUniformFloat uFogStart;

        private final GlUniformFloat uFogEnd;

        public Smooth(ShaderBindingContext context) {
            this.uFogColor = context.bindUniform("u_FogColor", GlUniformFloat4v::new);
            this.uFogShape = context.bindUniform("u_FogShape", GlUniformInt::new);
            this.uFogStart = context.bindUniform("u_FogStart", GlUniformFloat::new);
            this.uFogEnd = context.bindUniform("u_FogEnd", GlUniformFloat::new);
        }

        @Override
        public void setup() {
            this.uFogColor.set(RenderSystem.getShaderFogColor());
            this.uFogShape.set(RenderSystem.getShaderFogShape().getIndex());
            this.uFogStart.setFloat(RenderSystem.getShaderFogStart());
            this.uFogEnd.setFloat(RenderSystem.getShaderFogEnd());
        }
    }
}