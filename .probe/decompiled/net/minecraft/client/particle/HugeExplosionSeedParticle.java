package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class HugeExplosionSeedParticle extends NoRenderParticle {

    private int life;

    private final int lifeTime = 8;

    HugeExplosionSeedParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
    }

    @Override
    public void tick() {
        for (int $$0 = 0; $$0 < 6; $$0++) {
            double $$1 = this.f_107212_ + (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * 4.0;
            double $$2 = this.f_107213_ + (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * 4.0;
            double $$3 = this.f_107214_ + (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * 4.0;
            this.f_107208_.addParticle(ParticleTypes.EXPLOSION, $$1, $$2, $$3, (double) ((float) this.life / (float) this.lifeTime), 0.0, 0.0);
        }
        this.life++;
        if (this.life == this.lifeTime) {
            this.m_107274_();
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new HugeExplosionSeedParticle(clientLevel1, double2, double3, double4);
        }
    }
}