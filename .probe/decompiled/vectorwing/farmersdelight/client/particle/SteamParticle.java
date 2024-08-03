package vectorwing.farmersdelight.client.particle;

import javax.annotation.Nonnull;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class SteamParticle extends TextureSheetParticle {

    protected SteamParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(level, x, y, z);
        this.m_6569_(2.0F);
        this.m_107250_(0.25F, 0.25F);
        this.f_107225_ = this.f_107223_.nextInt(50) + 80;
        this.f_107226_ = 3.0E-6F;
        this.f_107215_ = motionX;
        this.f_107216_ = motionY + (double) (this.f_107223_.nextFloat() / 500.0F);
        this.f_107217_ = motionZ;
    }

    @Nonnull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ < this.f_107225_ && !(this.f_107230_ <= 0.0F)) {
            this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 5000.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 5000.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107224_ >= this.f_107225_ - 60 && this.f_107230_ > 0.01F) {
                this.f_107230_ -= 0.02F;
            }
        } else {
            this.m_107274_();
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SteamParticle particle = new SteamParticle(level, x, y + 0.3, z, xSpeed, ySpeed, zSpeed);
            particle.m_107271_(0.6F);
            particle.m_108335_(this.spriteSet);
            return particle;
        }
    }
}