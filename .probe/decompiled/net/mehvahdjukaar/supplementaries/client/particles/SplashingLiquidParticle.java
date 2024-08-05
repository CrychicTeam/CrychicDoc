package net.mehvahdjukaar.supplementaries.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.WaterDropParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class SplashingLiquidParticle extends WaterDropParticle {

    private SplashingLiquidParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z);
        this.f_107226_ = 0.04F;
        if (motionY == 0.0 && (motionX != 0.0 || motionZ != 0.0)) {
            this.f_107215_ = motionX;
            this.f_107216_ = 0.1;
            this.f_107217_ = motionZ;
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double r, double g, double b) {
            SplashingLiquidParticle splashparticle = new SplashingLiquidParticle(worldIn, x, y, z, 0.0, 0.0, 0.0);
            splashparticle.m_107253_((float) r, (float) g, (float) b);
            splashparticle.m_108335_(this.spriteSet);
            return splashparticle;
        }
    }
}