package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class SudsParticle extends BubbleBlockParticle {

    private final double additionalSize;

    SudsParticle(ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, SpriteSet pSprites) {
        super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, pSprites);
        this.f_172258_ = 0.96F;
        this.f_107226_ = -0.05F;
        this.f_172259_ = true;
        this.additionalSize = rand(0.08, 0.9) - 0.08;
        this.f_107225_ = (int) rand(32.0, 0.85);
        this.f_107219_ = true;
        this.m_107250_(0.01F, 0.01F);
    }

    @Override
    public float getQuadSize(float age) {
        float t = (float) this.f_107224_ + age;
        double a = 0.15;
        float inc = (float) (this.additionalSize * (1.0 + 1.0 / ((double) (-t) * a - 1.0)));
        return this.f_107663_ + inc;
    }

    @Override
    public void tick() {
        if (this.f_107224_ > 6) {
            this.f_107219_ = true;
        }
        super.tick();
        this.setColorForAge();
    }

    @Override
    public void updateSprite() {
        int i = this.f_107225_ - this.f_107224_;
        int s = 2;
        if (i < 3 * s) {
            int popParticleLen = 4;
            int j = Math.max(i, 0) / s;
            int popTime = 30;
            this.m_108337_(this.sprites.get((int) ((float) popTime * (3.0F - (float) j) / ((float) popParticleLen - 1.0F)), popTime));
            if (this.f_107226_ != 0.0F) {
                this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, (SoundEvent) ModSounds.BUBBLE_POP.get(), SoundSource.BLOCKS, 0.15F, 2.0F - this.f_107663_ * 0.2F, false);
                this.f_107226_ = 0.0F;
                this.f_107216_ = 0.0;
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet pSprites) {
            this.sprite = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            RandomSource r = pLevel.f_46441_;
            return new SudsParticle(pLevel, pX, pY, pZ, pXSpeed + (0.5 - (double) r.nextFloat()) * 0.04, pYSpeed + (0.5 - (double) r.nextFloat()) * 0.04, pZSpeed + (0.5 - (double) r.nextFloat()) * 0.04, this.sprite);
        }
    }
}