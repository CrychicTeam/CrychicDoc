package yesman.epicfight.client.particle;

import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class BloodParticle extends TextureSheetParticle {

    protected BloodParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, motionX, motionY, motionZ);
        this.f_107212_ = x + (this.f_107223_.nextDouble() - 0.5) * (double) this.f_107221_;
        this.f_107213_ = y + (this.f_107223_.nextDouble() + (double) this.f_107222_) * 0.5;
        this.f_107214_ = z + (this.f_107223_.nextDouble() - 0.5) * (double) this.f_107221_;
        this.f_107215_ = motionX;
        this.f_107216_ = motionY;
        this.f_107217_ = motionZ;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            BloodParticle particle = new BloodParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            Random random = new Random();
            particle.m_6257_(0.0, 0.5, 0.0);
            float mass = random.nextFloat() + 0.2F;
            particle.f_107225_ = 10 + (int) (mass * 10.0F);
            particle.f_107226_ = mass * 4.0F;
            particle.f_107216_ = (double) ((0.9F - mass) * 0.4F);
            particle.m_107250_(mass, mass);
            particle.m_108335_(this.spriteSet);
            float green = EpicFightMod.CLIENT_CONFIGS.bloodEffects.getValue() ? 0.0F : Mth.clamp(random.nextFloat(), 0.6F, 0.4F);
            particle.m_107253_(Mth.clamp(random.nextFloat(), 0.6F, 0.4F), green, 0.0F);
            return particle;
        }
    }
}