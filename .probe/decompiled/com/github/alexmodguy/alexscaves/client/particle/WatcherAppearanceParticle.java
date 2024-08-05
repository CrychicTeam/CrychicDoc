package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.model.WatcherModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.ForgeRenderTypes;

public class WatcherAppearanceParticle extends Particle {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/watcher_appearance.png");

    private final WatcherModel model = new WatcherModel();

    private WatcherAppearanceParticle(ClientLevel lvl, double x, double y, double z) {
        super(lvl, x, y, z);
        this.m_107250_(12.0F, 12.0F);
        this.f_107226_ = 0.0F;
        this.f_107225_ = 30;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        float fogBefore = RenderSystem.getShaderFogEnd();
        RenderSystem.setShaderFogEnd(40.0F);
        float age = (float) this.f_107224_ + partialTick;
        float f = (age - 5.0F) / (float) (this.f_107225_ - 5);
        float initalFlip = Math.min(f, 0.1F) / 0.1F;
        float scale = 1.0F;
        PoseStack posestack = new PoseStack();
        posestack.mulPose(camera.rotation());
        posestack.translate(0.0, 0.0, -1.2F);
        posestack.mulPose(Axis.XP.rotationDegrees(0.0F));
        posestack.scale(-scale, -scale, scale);
        posestack.translate(0.0, 0.5, (double) (2.0F + (1.0F - initalFlip)));
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(ForgeRenderTypes.getUnlitTranslucent(TEXTURE));
        this.model.positionForParticle(partialTick, age);
        this.model.m_7695_(posestack, vertexconsumer, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, Mth.clamp(1.0F - f * f, 0.0F, 1.0F));
        multibuffersource$buffersource.endBatch();
        RenderSystem.setShaderFogEnd(fogBefore);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WatcherAppearanceParticle(worldIn, x, y, z);
        }
    }
}