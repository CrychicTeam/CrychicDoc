package org.violetmoon.quark.addons.oddities.client.particle;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.violetmoon.quark.addons.oddities.module.MagnetsModule;
import org.violetmoon.quark.base.Quark;

@EventBusSubscriber(modid = "quark", value = { Dist.CLIENT }, bus = Bus.MOD)
public class MagnetParticleRenderType {

    private static Supplier<ShaderInstance> PARTICLE_SHADER = GameRenderer::m_172829_;

    public static final ParticleRenderType ADDITIVE_TRANSLUCENCY = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.activeTexture(33986);
            RenderSystem.activeTexture(33984);
            RenderSystem.depthMask(false);
            RenderSystem.setShader(MagnetParticleRenderType.PARTICLE_SHADER);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return "PARTICLE_SHEET_ADDITIVE_TRANSLUCENT";
        }
    };

    @SubscribeEvent
    public static void registerShader(RegisterShadersEvent event) {
        try {
            ShaderInstance translucentParticleShader = new ShaderInstance(event.getResourceProvider(), Quark.asResource("particle_no_alpha_cutoff"), DefaultVertexFormat.POSITION_TEX);
            event.registerShader(translucentParticleShader, s -> PARTICLE_SHADER = () -> s);
        } catch (Exception var2) {
            Quark.LOG.error("Failed to parse shader: " + var2);
        }
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MagnetsModule.attractorParticle, MagnetParticle.Provider::new);
        event.registerSpriteSet(MagnetsModule.repulsorParticle, MagnetParticle.Provider::new);
    }
}