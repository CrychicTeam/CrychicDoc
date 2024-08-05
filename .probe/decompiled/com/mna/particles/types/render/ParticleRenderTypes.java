package com.mna.particles.types.render;

import com.mna.api.config.ClientConfigValues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleRenderTypes {

    public static final ParticleRenderType ADDITIVE = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            Minecraft mc = Minecraft.getInstance();
            RenderSystem.depthMask(false);
            mc.gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.enableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
            tex.setBlurMipmap(ClientConfigValues.ParticleBlur, false);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            Minecraft mc = Minecraft.getInstance();
            mc.textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        public String toString() {
            return "MNA_ADDITIVE";
        }
    };

    public static final ParticleRenderType INVERTED = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, 1, 0);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
            tex.setBlurMipmap(ClientConfigValues.ParticleBlur, false);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            Minecraft mc = Minecraft.getInstance();
            mc.textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        public String toString() {
            return "MNA_INVERTED";
        }
    };
}