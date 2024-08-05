package yesman.epicfight.client.particle;

import java.util.Random;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.main.EpicFightMod;

@OnlyIn(Dist.CLIENT)
public class CutParticle extends HitParticle {

    public CutParticle(ClientLevel world, double x, double y, double z, SpriteSet animatedSprite) {
        super(world, x, y, z, animatedSprite);
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
        this.f_107663_ = 1.0F;
        this.f_107225_ = 4;
        Random rand = new Random();
        float angle = (float) Math.toRadians((double) (rand.nextFloat() * 90.0F));
        this.f_107204_ = angle;
        this.f_107231_ = angle;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return !EpicFightMod.CLIENT_CONFIGS.bloodEffects.getValue() ? null : new CutParticle(worldIn, x, y, z, this.spriteSet);
        }
    }
}