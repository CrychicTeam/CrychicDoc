package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WaterFoamParticle extends TextureSheetParticle {

    private float fadeR;

    private float fadeG;

    private float fadeB;

    protected WaterFoamParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.f_107215_ = xSpeed;
        this.f_107216_ = ySpeed;
        this.f_107217_ = zSpeed;
        this.m_107250_(0.35F, 0.35F);
        this.m_107253_(1.0F, 1.0F, 1.0F);
        this.f_107663_ = 0.3F + world.f_46441_.nextFloat() * 0.3F;
        this.f_107225_ = (int) (Math.random() * 5.0) + 4;
        this.setFadeColor(BiomeColors.getAverageWaterColor(this.f_107208_, BlockPos.containing(x, y, z)));
        this.f_172258_ = 0.9F;
    }

    public void setFadeColor(int int0) {
        this.fadeR = (float) ((int0 & 0xFF0000) >> 16) / 255.0F;
        this.fadeG = (float) ((int0 & 0xFF00) >> 8) / 255.0F;
        this.fadeB = (float) ((int0 & 0xFF) >> 0) / 255.0F;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_ > this.f_107225_ / 2) {
            this.m_107271_(1.0F - ((float) this.f_107224_ - (float) (this.f_107225_ / 2)) / (float) this.f_107225_);
        }
        this.f_107227_ = this.f_107227_ + (this.fadeR - this.f_107227_) * 0.25F;
        this.f_107228_ = this.f_107228_ + (this.fadeG - this.f_107228_) * 0.25F;
        this.f_107229_ = this.f_107229_ + (this.fadeB - this.f_107229_) * 0.25F;
        this.f_107216_ -= 0.1F;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_ * 4.0F, 0.0F, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            WaterFoamParticle particle = new WaterFoamParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_108335_(this.spriteSet);
            return particle;
        }
    }
}