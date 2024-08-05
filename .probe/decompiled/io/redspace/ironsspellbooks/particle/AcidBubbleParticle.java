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

public class AcidBubbleParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public AcidBubbleParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.f_107663_ *= 1.0F;
        this.m_6569_(3.0F);
        this.f_107225_ = 10 + (int) (Math.random() * 5.0);
        this.sprites = spriteSet;
        this.f_107226_ = 0.35F;
        this.m_108339_(spriteSet);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new AcidBubbleParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}