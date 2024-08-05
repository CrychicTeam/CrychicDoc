package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import com.mna.tools.math.Vector3;
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

public class FXBlueFlame extends MAParticleBase {

    private static final ParticleRenderType NORMAL_RENDER = new ParticleRenderType() {

        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            FXBlueFlame.beginRenderCommon(bufferBuilder, textureManager);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
            FXBlueFlame.endRenderCommon();
        }

        public String toString() {
            return "mna:blue_flame";
        }
    };

    public FXBlueFlame(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107253_(1.0F, 0.0F, 1.0F);
        this.f_107663_ *= 0.45F;
        this.f_107225_ = 20;
        this.f_107219_ = false;
        this.m_108339_(sprite);
    }

    private void setMoveVelocity(double x, double y, double z) {
        this.f_107215_ = x;
        this.f_107216_ = y;
        this.f_107217_ = z;
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (float) (this.f_107225_ - this.f_107224_ + 1) / (float) this.f_107225_;
    }

    public Vector3 getPosition() {
        return new Vector3(this.f_107212_, this.f_107213_, this.f_107214_);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728640;
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return NORMAL_RENDER;
    }

    protected static void beginRenderCommon(BufferBuilder buffer, TextureManager textureManager) {
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
        tex.setBlurMipmap(true, false);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    protected static void endRenderCommon() {
        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES).restoreLastBlurMipmap();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXBlueFlameRandomFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXBlueFlameRandomFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXBlueFlame particle = new FXBlueFlame(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.85F);
            particle.m_107253_(1.0F, 1.0F, 1.0F);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}