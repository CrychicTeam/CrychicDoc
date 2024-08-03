package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SuspendedTownParticle extends TextureSheetParticle {

    SuspendedTownParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        float $$7 = this.f_107223_.nextFloat() * 0.1F + 0.2F;
        this.f_107227_ = $$7;
        this.f_107228_ = $$7;
        this.f_107229_ = $$7;
        this.m_107250_(0.02F, 0.02F);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 0.6F + 0.5F);
        this.f_107215_ *= 0.02F;
        this.f_107216_ *= 0.02F;
        this.f_107217_ *= 0.02F;
        this.f_107225_ = (int) (20.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double double0, double double1, double double2) {
        this.m_107259_(this.m_107277_().move(double0, double1, double2));
        this.m_107275_();
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        } else {
            this.move(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ *= 0.99;
            this.f_107216_ *= 0.99;
            this.f_107217_ *= 0.99;
        }
    }

    public static class ComposterFillProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public ComposterFillProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedTownParticle $$8 = new SuspendedTownParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            $$8.m_107253_(1.0F, 1.0F, 1.0F);
            $$8.m_107257_(3 + clientLevel1.m_213780_().nextInt(5));
            return $$8;
        }
    }

    public static class DolphinSpeedProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public DolphinSpeedProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedTownParticle $$8 = new SuspendedTownParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_107253_(0.3F, 0.5F, 1.0F);
            $$8.m_108335_(this.sprite);
            $$8.m_107271_(1.0F - clientLevel1.f_46441_.nextFloat() * 0.7F);
            $$8.m_107257_($$8.m_107273_() / 2);
            return $$8;
        }
    }

    public static class EggCrackProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public EggCrackProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedTownParticle $$8 = new SuspendedTownParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            $$8.m_107253_(1.0F, 1.0F, 1.0F);
            return $$8;
        }
    }

    public static class HappyVillagerProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public HappyVillagerProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedTownParticle $$8 = new SuspendedTownParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            $$8.m_107253_(1.0F, 1.0F, 1.0F);
            return $$8;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedTownParticle $$8 = new SuspendedTownParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}