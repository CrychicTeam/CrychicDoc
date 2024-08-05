package yesman.epicfight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FeatherParticle extends TextureSheetParticle {

    protected FeatherParticle(ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        super(level, x, y, z, dx, dy, dz);
        this.f_107225_ = 8 + this.f_107223_.nextInt(22);
        this.f_107219_ = true;
        this.f_107226_ = 0.4F;
        this.f_172258_ = 0.8F;
        float roll = this.f_107223_.nextFloat() * 180.0F;
        this.f_107204_ = roll;
        this.f_107231_ = roll;
        this.f_107215_ = dx;
        this.f_107216_ = dy;
        this.f_107217_ = dz;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            FeatherParticle featuerparticle = new FeatherParticle(level, x, y, z, dx, dy, dz);
            featuerparticle.m_108335_(this.sprite);
            return featuerparticle;
        }
    }
}