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

public class EmberParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public EmberParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.m_6569_(this.f_107223_.nextFloat() * 1.75F + 1.0F);
        this.f_107225_ = 4 + (int) (Math.random() * 11.0);
        this.sprites = spriteSet;
        this.m_108339_(spriteSet);
        this.f_107226_ = -0.1F;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 100.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
        this.f_107216_ = this.f_107216_ + (double) (this.f_107223_.nextFloat() / 100.0F);
        this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 100.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
        this.m_108339_(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        return 15728880;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new EmberParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}