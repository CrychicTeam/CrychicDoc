package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class GlowParticle extends TextureSheetParticle {

    static final RandomSource RANDOM = RandomSource.create();

    private final SpriteSet sprites;

    GlowParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.sprites = spriteSet7;
        this.f_107663_ *= 0.75F;
        this.f_107219_ = false;
        this.m_108339_(spriteSet7);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public int getLightColor(float float0) {
        float $$1 = ((float) this.f_107224_ + float0) / (float) this.f_107225_;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        int $$2 = super.m_6355_(float0);
        int $$3 = $$2 & 0xFF;
        int $$4 = $$2 >> 16 & 0xFF;
        $$3 += (int) ($$1 * 15.0F * 16.0F);
        if ($$3 > 240) {
            $$3 = 240;
        }
        return $$3 | $$4 << 16;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
    }

    public static class ElectricSparkProvider implements ParticleProvider<SimpleParticleType> {

        private final double SPEED_FACTOR = 0.25;

        private final SpriteSet sprite;

        public ElectricSparkProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            GlowParticle $$8 = new GlowParticle(clientLevel1, double2, double3, double4, 0.0, 0.0, 0.0, this.sprite);
            $$8.m_107253_(1.0F, 0.9F, 1.0F);
            $$8.m_172260_(double5 * 0.25, double6 * 0.25, double7 * 0.25);
            int $$9 = 2;
            int $$10 = 4;
            $$8.m_107257_(clientLevel1.f_46441_.nextInt(2) + 2);
            return $$8;
        }
    }

    public static class GlowSquidProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public GlowSquidProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            GlowParticle $$8 = new GlowParticle(clientLevel1, double2, double3, double4, 0.5 - GlowParticle.RANDOM.nextDouble(), double6, 0.5 - GlowParticle.RANDOM.nextDouble(), this.sprite);
            if (clientLevel1.f_46441_.nextBoolean()) {
                $$8.m_107253_(0.6F, 1.0F, 0.8F);
            } else {
                $$8.m_107253_(0.08F, 0.4F, 0.4F);
            }
            $$8.f_107216_ *= 0.2F;
            if (double5 == 0.0 && double7 == 0.0) {
                $$8.f_107215_ *= 0.1F;
                $$8.f_107217_ *= 0.1F;
            }
            $$8.m_107257_((int) (8.0 / (clientLevel1.f_46441_.nextDouble() * 0.8 + 0.2)));
            return $$8;
        }
    }

    public static class ScrapeProvider implements ParticleProvider<SimpleParticleType> {

        private final double SPEED_FACTOR = 0.01;

        private final SpriteSet sprite;

        public ScrapeProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            GlowParticle $$8 = new GlowParticle(clientLevel1, double2, double3, double4, 0.0, 0.0, 0.0, this.sprite);
            if (clientLevel1.f_46441_.nextBoolean()) {
                $$8.m_107253_(0.29F, 0.58F, 0.51F);
            } else {
                $$8.m_107253_(0.43F, 0.77F, 0.62F);
            }
            $$8.m_172260_(double5 * 0.01, double6 * 0.01, double7 * 0.01);
            int $$9 = 10;
            int $$10 = 40;
            $$8.m_107257_(clientLevel1.f_46441_.nextInt(30) + 10);
            return $$8;
        }
    }

    public static class WaxOffProvider implements ParticleProvider<SimpleParticleType> {

        private final double SPEED_FACTOR = 0.01;

        private final SpriteSet sprite;

        public WaxOffProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            GlowParticle $$8 = new GlowParticle(clientLevel1, double2, double3, double4, 0.0, 0.0, 0.0, this.sprite);
            $$8.m_107253_(1.0F, 0.9F, 1.0F);
            $$8.m_172260_(double5 * 0.01 / 2.0, double6 * 0.01, double7 * 0.01 / 2.0);
            int $$9 = 10;
            int $$10 = 40;
            $$8.m_107257_(clientLevel1.f_46441_.nextInt(30) + 10);
            return $$8;
        }
    }

    public static class WaxOnProvider implements ParticleProvider<SimpleParticleType> {

        private final double SPEED_FACTOR = 0.01;

        private final SpriteSet sprite;

        public WaxOnProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            GlowParticle $$8 = new GlowParticle(clientLevel1, double2, double3, double4, 0.0, 0.0, 0.0, this.sprite);
            $$8.m_107253_(0.91F, 0.55F, 0.08F);
            $$8.m_172260_(double5 * 0.01 / 2.0, double6 * 0.01, double7 * 0.01 / 2.0);
            int $$9 = 10;
            int $$10 = 40;
            $$8.m_107257_(clientLevel1.f_46441_.nextInt(30) + 10);
            return $$8;
        }
    }
}