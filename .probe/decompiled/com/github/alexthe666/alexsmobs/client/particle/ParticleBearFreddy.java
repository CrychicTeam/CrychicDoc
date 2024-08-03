package com.github.alexthe666.alexsmobs.client.particle;

import com.github.alexthe666.alexsmobs.client.model.ModelGrizzlyBear;
import com.github.alexthe666.alexsmobs.client.render.AMRenderTypes;
import com.github.alexthe666.alexsmobs.client.render.RenderGrizzlyBear;
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
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleBearFreddy extends Particle {

    private final ModelGrizzlyBear model = new ModelGrizzlyBear();

    ParticleBearFreddy(ClientLevel lvl, double x, double y, double z) {
        super(lvl, x, y, z);
        this.m_107250_(2.0F, 2.0F);
        this.f_107226_ = 0.0F;
        this.f_107225_ = 15;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        float fogBefore = RenderSystem.getShaderFogEnd();
        RenderSystem.setShaderFogEnd(40.0F);
        float f = ((float) this.f_107224_ + partialTick) / (float) this.f_107225_;
        float initalFlip = Math.min(f, 0.1F) / 0.1F;
        float laterFlip = Mth.clamp(f - 0.1F, 0.0F, 0.1F) / 0.1F;
        float scale = 1.0F;
        PoseStack posestack = new PoseStack();
        posestack.mulPose(camera.rotation());
        posestack.translate(0.0, -1.0, 0.0);
        posestack.mulPose(Axis.XP.rotationDegrees(10.0F - laterFlip * 35.0F));
        posestack.scale(-scale, -scale, scale);
        posestack.translate(0.0, 0.5, (double) (2.0F + (1.0F - initalFlip)));
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(AMRenderTypes.getFreddy(RenderGrizzlyBear.TEXTURE_FREDDY));
        posestack.mulPose(Axis.XP.rotationDegrees(initalFlip * 20.0F - 5.0F));
        float swing = laterFlip * (float) Math.sin((double) (((float) this.f_107224_ + partialTick) * 0.3F)) * 20.0F;
        posestack.mulPose(Axis.ZP.rotationDegrees((1.0F - initalFlip) * 45.0F + swing));
        boolean baby = this.model.f_102610_;
        this.model.f_102610_ = false;
        this.model.positionForParticle(partialTick, (float) this.f_107224_ + partialTick);
        this.model.renderToBuffer(posestack, vertexconsumer, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.model.f_102610_ = baby;
        multibuffersource$buffersource.endBatch();
        RenderSystem.setShaderFogEnd(fogBefore);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleBearFreddy(worldIn, x, y, z);
        }
    }
}