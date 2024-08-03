package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXDrip extends MAParticleBase {

    private static final ParticleRenderType NORMAL_RENDER = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            FXDrip.beginRenderCommon(bufferBuilder, textureManager);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            FXDrip.endRenderCommon();
        }

        public String toString() {
            return "mna:drip";
        }
    };

    public FXDrip(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.m_107271_(0.0F);
        this.f_107663_ = 0.1F;
        this.f_107215_ = 0.0;
        this.f_107216_ = -0.1;
        this.f_107217_ = 0.0;
        this.f_107225_ = 30;
        this.f_107219_ = true;
        this.m_108339_(sprite);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_);
    }

    @Override
    public void tick() {
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = (float) ((double) this.f_107231_ + Math.random() * Math.PI / 180.0);
        super.tick();
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return NORMAL_RENDER;
    }

    protected static void beginRenderCommon(BufferBuilder buffer, TextureManager textureManager) {
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
        tex.setBlurMipmap(false, false);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    protected static void endRenderCommon() {
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXDripFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXDripFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXDrip particle = new FXDrip(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}