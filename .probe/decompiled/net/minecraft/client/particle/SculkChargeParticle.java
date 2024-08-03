package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SculkChargeParticleOptions;

public class SculkChargeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    SculkChargeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.f_172258_ = 0.96F;
        this.sprites = spriteSet7;
        this.m_6569_(1.5F);
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

    public static record Provider(SpriteSet f_233904_) implements ParticleProvider<SculkChargeParticleOptions> {

        private final SpriteSet sprite;

        public Provider(SpriteSet f_233904_) {
            this.sprite = f_233904_;
        }

        public Particle createParticle(SculkChargeParticleOptions p_233918_, ClientLevel p_233919_, double p_233920_, double p_233921_, double p_233922_, double p_233923_, double p_233924_, double p_233925_) {
            SculkChargeParticle $$8 = new SculkChargeParticle(p_233919_, p_233920_, p_233921_, p_233922_, p_233923_, p_233924_, p_233925_, this.sprite);
            $$8.m_107271_(1.0F);
            $$8.m_172260_(p_233923_, p_233924_, p_233925_);
            $$8.f_107204_ = p_233918_.roll();
            $$8.f_107231_ = p_233918_.roll();
            $$8.m_107257_(p_233919_.f_46441_.nextInt(12) + 8);
            return $$8;
        }
    }
}