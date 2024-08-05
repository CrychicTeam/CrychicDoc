package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SoulParticle extends RisingParticle {

    private final SpriteSet sprites;

    protected boolean isGlowing;

    SoulParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.sprites = spriteSet7;
        this.m_6569_(1.5F);
        this.m_108339_(spriteSet7);
    }

    @Override
    public int getLightColor(float float0) {
        return this.isGlowing ? 240 : super.m_6355_(float0);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
    }

    public static class EmissiveProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public EmissiveProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SoulParticle $$8 = new SoulParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
            $$8.m_107271_(1.0F);
            $$8.isGlowing = true;
            return $$8;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SoulParticle $$8 = new SoulParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
            $$8.m_107271_(1.0F);
            return $$8;
        }
    }
}