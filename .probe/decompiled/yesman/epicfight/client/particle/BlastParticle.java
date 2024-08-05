package yesman.epicfight.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlastParticle extends HitParticle {

    public BlastParticle(ClientLevel world, double x, double y, double z, SpriteSet animatedSprite) {
        super(world, x, y, z, animatedSprite);
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
        this.f_107663_ = 1.5F;
        this.f_107225_ = 2;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
    }

    @Override
    public void render(VertexConsumer buffer, Camera renderInfo, float partialTicks) {
        if (!this.f_107220_) {
            super.m_5744_(buffer, renderInfo, partialTicks);
            if (this.f_107224_++ >= this.f_107225_) {
                this.m_107274_();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Provider(SpriteSet p_i50607_1_) {
            this.spriteSet = p_i50607_1_;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new BlastParticle(worldIn, x, y, z, this.spriteSet);
        }
    }
}