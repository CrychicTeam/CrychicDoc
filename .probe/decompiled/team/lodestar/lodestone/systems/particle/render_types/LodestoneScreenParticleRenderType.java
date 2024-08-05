package team.lodestar.lodestone.systems.particle.render_types;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Supplier;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;

public interface LodestoneScreenParticleRenderType {

    LodestoneScreenParticleRenderType ADDITIVE = new LodestoneScreenParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(770, 1);
            RenderSystem.setShader(LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };

    LodestoneScreenParticleRenderType TRANSPARENT = new LodestoneScreenParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance());
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    };

    LodestoneScreenParticleRenderType LUMITRANSPARENT = new LodestoneScreenParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            Supplier<ShaderInstance> instance = LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance();
            RenderSystem.setShader(instance);
            ((ShaderInstance) instance.get()).safeGetUniform("LumiTransparency").set(1.0F);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            Supplier<ShaderInstance> instance = LodestoneShaderRegistry.SCREEN_PARTICLE.getInstance();
            ((ShaderInstance) instance.get()).safeGetUniform("LumiTransparency").set(0.0F);
        }
    };

    void begin(BufferBuilder var1, TextureManager var2);

    void end(Tesselator var1);
}