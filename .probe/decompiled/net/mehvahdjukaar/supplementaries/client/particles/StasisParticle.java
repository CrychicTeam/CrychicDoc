package net.mehvahdjukaar.supplementaries.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class StasisParticle extends SimpleAnimatedParticle {

    private StasisParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprite) {
        super(world, x, y, z, sprite, -5.0E-4F);
        this.f_107215_ = motionX;
        this.f_107216_ = motionY;
        this.f_107217_ = motionZ;
        this.f_107663_ *= 0.625F;
        this.f_107225_ = 8 + this.f_107223_.nextInt(6);
        this.m_107659_(15916745);
        this.m_108339_(sprite);
    }

    @Override
    public void move(double x, double y, double z) {
        this.m_107259_(this.m_107277_().move(x, y, z));
        this.m_107275_();
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Factory(SpriteSet sprite) {
            this.sprites = sprite;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
            return new StasisParticle(world, x, y, z, motionX, motionY, motionZ, this.sprites);
        }
    }
}