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

@OnlyIn(Dist.CLIENT)
public class EnderParticle extends TextureSheetParticle {

    private EnderParticle.Usage usage;

    public EnderParticle(ClientLevel worldIn, double x, double y, double z, double xd, double yd, double dz) {
        super(worldIn, x, y, z);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = dz;
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        this.f_107663_ = 0.1F * (this.f_107223_.nextFloat() * 0.2F + 0.5F);
        float f = this.f_107223_.nextFloat() * 0.6F + 0.4F;
        this.f_107227_ = f * 0.9F;
        this.f_107228_ = f * 0.3F;
        this.f_107229_ = f;
        this.f_107225_ = (int) (Math.random() * 10.0) + 40;
        this.f_107219_ = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        if (this.usage == EnderParticle.Usage.DRAGON_BREATH_FLAME) {
            int i = super.m_6355_(float0);
            int k = i >> 16 & 0xFF;
            return 240 | k << 16;
        } else {
            return super.m_6355_(float0);
        }
    }

    public void setUsage(EnderParticle.Usage usage) {
        this.usage = usage;
    }

    @OnlyIn(Dist.CLIENT)
    public static class BreathFlameProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public BreathFlameProvider(SpriteSet p_i50607_1_) {
            this.spriteSet = p_i50607_1_;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EnderParticle particle = new EnderParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_108335_(this.spriteSet);
            particle.f_107663_ *= 2.0F;
            particle.setUsage(EnderParticle.Usage.DRAGON_BREATH_FLAME);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class EndermanDeathEmitProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public EndermanDeathEmitProvider(SpriteSet p_i50607_1_) {
            this.spriteSet = p_i50607_1_;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            EnderParticle particle = new EnderParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.m_108335_(this.spriteSet);
            particle.setUsage(EnderParticle.Usage.ENDERMAN_DEATH);
            return particle;
        }
    }

    private static enum Usage {

        DRAGON_BREATH_FLAME, ENDERMAN_DEATH
    }
}