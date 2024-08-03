package net.mehvahdjukaar.supplementaries.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class BombSmokeParticle extends TextureSheetParticle {

    private BombSmokeParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.m_6569_(3.0F);
        this.m_107250_(0.35F, 0.35F);
        this.f_107225_ = this.f_107223_.nextInt(14) + 16;
        this.f_107226_ = 3.0E-6F;
        this.f_107215_ = motionX;
        this.f_107216_ = motionY + 0.01 + (double) (this.f_107223_.nextFloat() / 2000.0F);
        this.f_107217_ = motionZ;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ < this.f_107225_ && this.f_107230_ > 0.0F) {
            this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 3500.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 3500.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107230_ = 1.0F - (float) this.f_107224_ / (float) this.f_107225_;
        } else {
            this.m_107274_();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BombSmokeParticle particle = new BombSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.9F);
            particle.m_108335_(this.spriteSet);
            return particle;
        }
    }
}