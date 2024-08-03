package net.mehvahdjukaar.supplementaries.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class BombExplosionParticle extends TextureSheetParticle {

    private final SpriteSet spriteWithAge;

    private BombExplosionParticle(ClientLevel world, double x, double y, double z, double scale, SpriteSet spriteWithAge) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107225_ = 5 + this.f_107223_.nextInt(4);
        float f = this.f_107223_.nextFloat() * 0.6F + 0.4F;
        this.f_107227_ = f;
        this.f_107228_ = f;
        this.f_107229_ = f;
        this.f_107663_ = 1.0F * (1.0F - (float) scale * 0.8F);
        this.spriteWithAge = spriteWithAge;
        this.m_108339_(spriteWithAge);
    }

    @Override
    public int getLightColor(float partialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_108339_(this.spriteWithAge);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BombExplosionParticle(worldIn, x, y, z, xSpeed, this.spriteSet);
        }
    }
}