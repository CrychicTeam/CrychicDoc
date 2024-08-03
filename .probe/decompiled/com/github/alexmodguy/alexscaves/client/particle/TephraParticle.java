package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TephraParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private float prevAlpha = 0.0F;

    private boolean spinning;

    private float spinIncrement;

    protected TephraParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet spriteSet, boolean spinning) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = spriteSet;
        this.m_108339_(this.sprites);
        this.f_107215_ = xSpeed;
        this.f_107216_ = ySpeed;
        this.f_107217_ = zSpeed;
        this.m_107250_(0.5F, 0.5F);
        this.f_107663_ = 1.0F + world.f_46441_.nextFloat() * 0.5F;
        this.f_107225_ = 10 + world.f_46441_.nextInt(20);
        this.f_172258_ = 0.99F;
        this.spinning = spinning;
        if (spinning) {
            this.f_107231_ = (float) Math.toRadians((double) (360.0F * this.f_107223_.nextFloat()));
            this.f_107204_ = this.f_107231_;
            this.spinIncrement = (float) (this.f_107223_.nextBoolean() ? -1 : 1) * this.f_107223_.nextFloat() * 0.4F;
        }
    }

    @Override
    public void tick() {
        this.m_108339_(this.sprites);
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        float ageProgress = (float) this.f_107224_ / (float) this.f_107225_;
        float f = ageProgress - 0.5F;
        float f1 = 1.0F - f * 2.0F;
        if (ageProgress > 0.5F) {
            this.prevAlpha = this.f_107230_;
            this.m_107271_(this.prevAlpha + (f1 - this.prevAlpha) * AlexsCaves.PROXY.getPartialTicks());
        }
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
        }
        if (this.spinning) {
            this.f_107204_ = this.f_107231_;
            this.f_107231_ = this.f_107231_ + f1 * this.spinIncrement;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new TephraParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FlameFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public FlameFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TephraParticle particle = new TephraParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true);
            particle.f_107663_ = 0.5F + worldIn.f_46441_.nextFloat() * 0.25F;
            particle.f_107225_ = 20 + worldIn.f_46441_.nextInt(10);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class SmallFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public SmallFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TephraParticle particle = new TephraParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, false);
            particle.f_107663_ = 0.5F + worldIn.f_46441_.nextFloat() * 0.25F;
            particle.f_107225_ = 10 + worldIn.f_46441_.nextInt(10);
            return particle;
        }
    }
}