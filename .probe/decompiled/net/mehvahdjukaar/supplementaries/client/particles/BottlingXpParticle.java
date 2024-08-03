package net.mehvahdjukaar.supplementaries.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class BottlingXpParticle extends TextureSheetParticle {

    private final double xStart;

    private final double yStart;

    private final double zStart;

    protected BottlingXpParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        this.xStart = this.f_107212_;
        this.yStart = this.f_107213_;
        this.zStart = this.f_107214_;
        this.f_107663_ = 0.1F * (this.f_107223_.nextFloat() * 0.2F + 0.5F);
        if (this.f_107223_.nextInt(3) != 0) {
            this.m_107253_(0.65F + this.f_107223_.nextFloat() * 0.25F, 0.85F + this.f_107223_.nextFloat() * 0.15F, this.f_107223_.nextFloat() * 0.2F);
        } else {
            this.m_107253_(0.3F + this.f_107223_.nextFloat() * 0.2F, 0.8F + this.f_107223_.nextFloat() * 0.2F, 0.1F + this.f_107223_.nextFloat() * 0.25F);
        }
        this.f_107225_ = (int) (Math.random() * 8.0) + 5;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double x, double y, double z) {
        this.m_107259_(this.m_107277_().move(x, y, z));
        this.m_107275_();
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float f = ((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        return this.f_107663_ * f;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            float f = (float) this.f_107224_ / (float) this.f_107225_;
            float f1 = -f + f * f * 2.0F;
            float f2 = 1.0F - f1;
            this.f_107212_ = this.xStart + this.f_107215_ * (double) f2;
            this.f_107213_ = this.yStart + this.f_107216_ * (double) f2 + (double) (1.0F - f);
            this.f_107214_ = this.zStart + this.f_107217_ * (double) f2;
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BottlingXpParticle particle = new BottlingXpParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_108335_(this.sprite);
            return particle;
        }
    }
}