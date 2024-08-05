package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class SlingshotParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private SlingshotParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z);
        this.sprites = sprites;
        this.f_107215_ = motionX;
        this.f_107216_ = motionY;
        this.f_107217_ = motionZ;
        this.f_107663_ *= 2.5F;
        this.f_107225_ = (int) (10.0 / ((double) this.f_107223_.nextFloat() * 0.3 + 0.7));
        this.f_107219_ = false;
        this.m_108339_(this.sprites);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
        float x = (float) this.f_107224_ / (float) this.f_107225_;
        float a = MthUtils.PHI;
        this.f_107230_ = a + 1.0F / (x - a);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Factory(SpriteSet spriteSet) {
            this.sprite = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new SlingshotParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
        }
    }
}