package me.jellysquid.mods.sodium.client.render.chunk.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.EnumMap;
import java.util.Map;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformFloat3v;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformInt;
import me.jellysquid.mods.sodium.client.gl.shader.uniform.GlUniformMatrix4f;
import me.jellysquid.mods.sodium.client.util.TextureUtil;
import org.joml.Matrix4fc;

public class ChunkShaderInterface {

    private final Map<ChunkShaderTextureSlot, GlUniformInt> uniformTextures;

    private final GlUniformMatrix4f uniformModelViewMatrix;

    private final GlUniformMatrix4f uniformProjectionMatrix;

    private final GlUniformFloat3v uniformRegionOffset;

    private final ChunkShaderFogComponent fogShader;

    public ChunkShaderInterface(ShaderBindingContext context, ChunkShaderOptions options) {
        this.uniformModelViewMatrix = context.bindUniform("u_ModelViewMatrix", GlUniformMatrix4f::new);
        this.uniformProjectionMatrix = context.bindUniform("u_ProjectionMatrix", GlUniformMatrix4f::new);
        this.uniformRegionOffset = context.bindUniform("u_RegionOffset", GlUniformFloat3v::new);
        this.uniformTextures = new EnumMap(ChunkShaderTextureSlot.class);
        this.uniformTextures.put(ChunkShaderTextureSlot.BLOCK, (GlUniformInt) context.bindUniform("u_BlockTex", GlUniformInt::new));
        this.uniformTextures.put(ChunkShaderTextureSlot.LIGHT, (GlUniformInt) context.bindUniform("u_LightTex", GlUniformInt::new));
        this.fogShader = (ChunkShaderFogComponent) options.fog().getFactory().apply(context);
    }

    @Deprecated
    public void setupState() {
        this.bindTexture(ChunkShaderTextureSlot.BLOCK, TextureUtil.getBlockTextureId());
        this.bindTexture(ChunkShaderTextureSlot.LIGHT, TextureUtil.getLightTextureId());
        this.fogShader.setup();
    }

    @Deprecated(forRemoval = true)
    private void bindTexture(ChunkShaderTextureSlot slot, int textureId) {
        GlStateManager._activeTexture(33984 + slot.ordinal());
        GlStateManager._bindTexture(textureId);
        GlUniformInt uniform = (GlUniformInt) this.uniformTextures.get(slot);
        uniform.setInt(slot.ordinal());
    }

    public void setProjectionMatrix(Matrix4fc matrix) {
        this.uniformProjectionMatrix.set(matrix);
    }

    public void setModelViewMatrix(Matrix4fc matrix) {
        this.uniformModelViewMatrix.set(matrix);
    }

    public void setRegionOffset(float x, float y, float z) {
        this.uniformRegionOffset.set(x, y, z);
    }
}