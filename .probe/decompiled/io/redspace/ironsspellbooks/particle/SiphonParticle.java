package io.redspace.ironsspellbooks.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SiphonParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public SiphonParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.m_6569_(this.f_107223_.nextFloat() * 0.35F + 0.5F);
        this.f_107225_ = 1 + (int) (Math.random() * 5.0);
        this.sprites = spriteSet;
        this.m_108339_(spriteSet);
        this.f_107226_ = -0.003F;
        this.f_107227_ = 0.34F;
        this.f_107228_ = 0.0F;
        this.f_107229_ = 0.040000003F;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 500.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
        this.f_107216_ = this.f_107216_ + (double) (this.f_107223_.nextFloat() / 100.0F);
        this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 500.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
        this.m_108339_(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        return 240;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new SiphonParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}