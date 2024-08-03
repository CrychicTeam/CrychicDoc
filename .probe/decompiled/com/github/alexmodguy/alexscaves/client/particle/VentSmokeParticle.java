package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VentSmokeParticle extends TextureSheetParticle {

    private final boolean fullbright;

    protected VentSmokeParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, boolean fullbright) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.f_107215_ = xSpeed;
        this.f_107216_ = ySpeed;
        this.f_107217_ = zSpeed;
        this.m_107250_(0.5F, 0.5F);
        this.f_107663_ = 0.8F + world.f_46441_.nextFloat() * 0.3F;
        this.f_107225_ = (int) (Math.random() * 20.0) + 40;
        this.f_172258_ = 0.99F;
        this.fullbright = fullbright;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_ > this.f_107225_ / 2) {
            this.m_107271_(1.0F - ((float) this.f_107224_ - (float) (this.f_107225_ / 2)) / (float) this.f_107225_);
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

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_ * 4.0F, 0.0F, 1.0F);
    }

    @Override
    public int getLightColor(float partialTicks) {
        int i = super.m_6355_(partialTicks);
        return this.fullbright ? 240 : i;
    }

    @OnlyIn(Dist.CLIENT)
    public static class BlackFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public BlackFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            VentSmokeParticle particle = new VentSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, false);
            particle.m_108335_(this.spriteSet);
            float randCol = worldIn.f_46441_.nextFloat() * 0.05F;
            particle.m_107253_(randCol + 0.1F, randCol + 0.1F, randCol + 0.1F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class GreenFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public GreenFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            VentSmokeParticle particle = new VentSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, true);
            particle.m_108335_(this.spriteSet);
            float randCol = worldIn.f_46441_.nextFloat() * 0.05F;
            particle.m_107253_(randCol + 0.05F, randCol + 0.95F, 0.0F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class RedFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public RedFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            VentSmokeParticle particle = new VentSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, true);
            particle.m_108335_(this.spriteSet);
            float randCol = worldIn.f_46441_.nextFloat() * 0.15F;
            particle.m_107253_(randCol + 0.85F, randCol + 0.55F, randCol + 0.35F);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class WhiteFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public WhiteFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            VentSmokeParticle particle = new VentSmokeParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, false);
            particle.m_108335_(this.spriteSet);
            float randCol = worldIn.f_46441_.nextFloat() * 0.05F;
            particle.m_107253_(randCol + 0.95F, randCol + 0.95F, randCol + 0.95F);
            return particle;
        }
    }
}