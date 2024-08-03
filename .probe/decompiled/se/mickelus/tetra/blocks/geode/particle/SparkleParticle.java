package se.mickelus.tetra.blocks.geode.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SparkleParticle extends SimpleAnimatedParticle {

    SparkleParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz, SpriteSet spriteSet) {
        super(level, x, y, z, spriteSet, 0.0F);
        this.f_107215_ = dx;
        this.f_107216_ = dy;
        this.f_107217_ = dz;
        this.f_107663_ = 0.125F;
        this.f_107225_ = 5 + this.f_107223_.nextInt(12);
        this.m_107659_(12565390);
        this.m_108339_(spriteSet);
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.m_107259_(this.m_107277_().move(dx, dy, dz));
        this.m_107275_();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new SparkleParticle(level, x, y, z, dx, dy, dz, this.sprites);
        }
    }
}