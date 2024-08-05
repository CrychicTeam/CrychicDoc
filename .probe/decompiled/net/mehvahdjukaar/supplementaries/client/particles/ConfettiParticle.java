package net.mehvahdjukaar.supplementaries.client.particles;

import net.mehvahdjukaar.supplementaries.client.renderers.color.ColorHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class ConfettiParticle extends TextureSheetParticle {

    private ConfettiParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z);
        this.m_108335_(sprites);
        this.f_107215_ = motionX;
        this.f_107216_ = motionY;
        this.f_107217_ = motionZ;
        this.m_107250_(0.001F, 0.001F);
        this.f_107226_ = 0.375F;
        this.f_107225_ = (int) (80.0 / ((double) this.f_107223_.nextFloat() * 0.3 + 0.7));
        int col = ColorHelper.getRandomBrightColor(this.f_107223_);
        this.f_107227_ = (float) FastColor.ARGB32.red(col) / 255.0F;
        this.f_107228_ = (float) FastColor.ARGB32.green(col) / 255.0F;
        this.f_107229_ = (float) FastColor.ARGB32.blue(col) / 255.0F;
    }

    @Override
    public void tick() {
        super.m_5989_();
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
            return new ConfettiParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite);
        }
    }
}