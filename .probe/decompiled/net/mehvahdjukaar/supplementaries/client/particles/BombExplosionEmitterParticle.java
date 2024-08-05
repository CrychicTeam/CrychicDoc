package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;

public class BombExplosionEmitterParticle extends NoRenderParticle {

    private static final int MAXIMUM_TIME = 4;

    private final double radius;

    private BombExplosionEmitterParticle(ClientLevel world, double x, double y, double z, double radius) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.radius = radius;
    }

    @Override
    public void tick() {
        float amountMult = 0.6F;
        for (int i = 0; (double) i < this.radius * this.radius * this.radius * (double) amountMult; i++) {
            double phi = Math.acos(2.0 * this.f_107223_.nextDouble() - 1.0);
            double theta = this.f_107223_.nextDouble() * 2.0 * Math.PI;
            double r = (this.f_107223_.nextDouble() - this.f_107223_.nextDouble()) * this.radius * 1.3;
            double d0 = this.f_107212_ + r * Math.sin(phi) * Math.cos(theta);
            double d1 = this.f_107213_ + r * Math.sin(phi) * Math.sin(theta);
            double d2 = this.f_107214_ + r * Math.cos(phi);
            this.f_107208_.addParticle((ParticleOptions) ModParticles.BOMB_EXPLOSION_PARTICLE.get(), d0, d1, d2, (double) ((float) this.f_107224_ / 4.0F), 0.0, 0.0);
        }
        this.f_107224_++;
        if (this.f_107224_ >= 4) {
            this.m_107274_();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Factory(SpriteSet sprite) {
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double radius, double ySpeed, double zSpeed) {
            return new BombExplosionEmitterParticle(worldIn, x, y, z, radius);
        }
    }
}