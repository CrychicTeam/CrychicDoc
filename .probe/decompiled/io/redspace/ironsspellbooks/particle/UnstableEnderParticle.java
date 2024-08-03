package io.redspace.ironsspellbooks.particle;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UnstableEnderParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public UnstableEnderParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.f_172258_ = 0.77F;
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.f_107663_ = 0.1F * (this.f_107223_.nextFloat() * 0.15F + 0.3F);
        this.m_6569_(2.25F);
        this.f_107225_ = 5 + (int) (Math.random() * 25.0);
        this.sprites = spriteSet;
        this.f_107226_ = 0.0F;
        this.randomlyAnimate();
        float f = this.f_107223_.nextFloat() * 0.6F + 0.4F;
        this.f_107227_ = f * 0.9F;
        this.f_107228_ = f * 0.3F;
        this.f_107229_ = f;
    }

    @Override
    public void tick() {
        super.m_5989_();
        float xj = this.f_107223_.nextFloat() / 50.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1);
        float yj = this.f_107223_.nextFloat() / 50.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1);
        float zj = this.f_107223_.nextFloat() / 50.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1);
        this.m_107264_(this.f_107212_ + (double) xj, this.f_107213_ + (double) yj, this.f_107214_ + (double) zj);
    }

    private void randomlyAnimate() {
        this.m_108337_(this.sprites.get(Utils.random));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        int i = super.m_6355_(float0);
        float f = (float) this.f_107224_ / (float) this.f_107225_;
        f *= f;
        f *= f;
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        k += (int) (f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }
        return j | k << 16;
    }

    @Override
    public float getQuadSize(float float0) {
        float f = ((float) this.f_107224_ + float0) / (float) this.f_107225_;
        f = 1.0F - f;
        f *= f;
        f = 1.0F - f;
        return this.f_107663_ * f;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new UnstableEnderParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}