package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SculkChargePopParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    SculkChargePopParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.f_172258_ = 0.96F;
        this.sprites = spriteSet7;
        this.m_6569_(1.0F);
        this.f_107219_ = false;
        this.m_108339_(spriteSet7);
    }

    @Override
    public int getLightColor(float float0) {
        return 240;
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

    public static record Provider(SpriteSet f_233944_) implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet f_233944_) {
            this.sprite = f_233944_;
        }

        public Particle createParticle(SimpleParticleType p_233958_, ClientLevel p_233959_, double p_233960_, double p_233961_, double p_233962_, double p_233963_, double p_233964_, double p_233965_) {
            SculkChargePopParticle $$8 = new SculkChargePopParticle(p_233959_, p_233960_, p_233961_, p_233962_, p_233963_, p_233964_, p_233965_, this.sprite);
            $$8.m_107271_(1.0F);
            $$8.m_172260_(p_233963_, p_233964_, p_233965_);
            $$8.m_107257_(p_233959_.f_46441_.nextInt(4) + 6);
            return $$8;
        }
    }
}