package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;

public class GammaroachParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected GammaroachParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107250_(0.5F, 0.5F);
        this.f_107663_ = 0.2F + world.f_46441_.nextFloat() * 0.4F;
        this.f_107225_ = 8 + world.f_46441_.nextInt(5);
        this.f_172258_ = 0.96F;
        float randCol = world.f_46441_.nextFloat() * 0.05F;
        this.sprites = sprites;
        this.m_107253_(randCol * 0.2F + 0.2F, randCol * 0.2F + 0.2F, randCol * 0.2F + 0.2F);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.m_108339_(this.sprites);
        if (this.f_107224_ > this.f_107225_ / 2) {
            this.m_107271_(1.0F - ((float) this.f_107224_ - (float) (this.f_107225_ / 4)) / (float) this.f_107225_);
        }
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            int color1 = 6092288;
            float randCol = this.f_107208_.f_46441_.nextFloat() * 0.05F;
            Vec3 targetColor = new Vec3((double) Math.min((float) FastColor.ARGB32.red(color1) / 255.0F + randCol, 1.0F), (double) ((float) FastColor.ARGB32.green(color1) / 255.0F + randCol), (double) ((float) FastColor.ARGB32.blue(color1) / 255.0F + randCol));
            this.f_107227_ = (float) ((double) this.f_107227_ + (targetColor.x - (double) this.f_107227_) * 0.1F);
            this.f_107228_ = (float) ((double) this.f_107228_ + (targetColor.y - (double) this.f_107228_) * 0.1F);
            this.f_107229_ = (float) ((double) this.f_107229_ + (targetColor.z - (double) this.f_107229_) * 0.1F);
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
        return super.m_5902_(scaleFactor);
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            GammaroachParticle particle = new GammaroachParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            particle.m_108339_(this.spriteSet);
            return particle;
        }
    }
}