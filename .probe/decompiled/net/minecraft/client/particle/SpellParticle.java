package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class SpellParticle extends TextureSheetParticle {

    private static final RandomSource RANDOM = RandomSource.create();

    private final SpriteSet sprites;

    SpellParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, 0.5 - RANDOM.nextDouble(), double5, 0.5 - RANDOM.nextDouble());
        this.f_172258_ = 0.96F;
        this.f_107226_ = -0.1F;
        this.f_172259_ = true;
        this.sprites = spriteSet7;
        this.f_107216_ *= 0.2F;
        if (double4 == 0.0 && double6 == 0.0) {
            this.f_107215_ *= 0.1F;
            this.f_107217_ *= 0.1F;
        }
        this.f_107663_ *= 0.75F;
        this.f_107225_ = (int) (8.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = false;
        this.m_108339_(spriteSet7);
        if (this.isCloseToScopingPlayer()) {
            this.m_107271_(0.0F);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        if (this.isCloseToScopingPlayer()) {
            this.m_107271_(0.0F);
        } else {
            this.m_107271_(Mth.lerp(0.05F, this.f_107230_, 1.0F));
        }
    }

    private boolean isCloseToScopingPlayer() {
        Minecraft $$0 = Minecraft.getInstance();
        LocalPlayer $$1 = $$0.player;
        return $$1 != null && $$1.m_146892_().distanceToSqr(this.f_107212_, this.f_107213_, this.f_107214_) <= 9.0 && $$0.options.getCameraType().isFirstPerson() && $$1.m_150108_();
    }

    public static class AmbientMobProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public AmbientMobProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            Particle $$8 = new SpellParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
            $$8.setAlpha(0.15F);
            $$8.setColor((float) double5, (float) double6, (float) double7);
            return $$8;
        }
    }

    public static class InstantProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public InstantProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new SpellParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
        }
    }

    public static class MobProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public MobProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            Particle $$8 = new SpellParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
            $$8.setColor((float) double5, (float) double6, (float) double7);
            return $$8;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new SpellParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
        }
    }

    public static class WitchProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public WitchProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SpellParticle $$8 = new SpellParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprite);
            float $$9 = clientLevel1.f_46441_.nextFloat() * 0.5F + 0.35F;
            $$8.m_107253_(1.0F * $$9, 0.0F * $$9, 1.0F * $$9);
            return $$8;
        }
    }
}