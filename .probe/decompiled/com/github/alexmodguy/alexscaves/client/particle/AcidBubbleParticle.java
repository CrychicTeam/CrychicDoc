package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AcidBubbleParticle extends TextureSheetParticle {

    private SpriteSet spriteSet;

    protected AcidBubbleParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.f_107663_ = this.f_107663_ * (0.9F + world.f_46441_.nextFloat() * 0.5F);
        this.f_107219_ = true;
        this.f_107215_ = xSpeed;
        this.f_107216_ = ySpeed;
        this.f_107217_ = zSpeed;
        this.spriteSet = spriteSet;
        this.f_172258_ = 0.95F;
        this.f_107225_ = 10 + world.f_46441_.nextInt(10);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        int ageAt = this.f_107225_ - 7;
        int sprite = this.f_107224_ >= ageAt ? Math.min(this.f_107224_ - ageAt, 7) : 0;
        this.m_108337_(this.spriteSet.get(sprite, 8));
        if (sprite > 0) {
            this.f_107215_ = 0.0;
            this.f_107216_ = 0.0;
            this.f_107217_ = 0.0;
        }
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

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AcidBubbleParticle heartparticle = new AcidBubbleParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
            heartparticle.m_108337_(this.spriteSet.get(0, 1));
            return heartparticle;
        }
    }
}