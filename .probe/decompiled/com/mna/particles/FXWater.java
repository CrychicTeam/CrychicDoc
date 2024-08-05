package com.mna.particles;

import com.mna.api.particles.MAParticleType;
import com.mna.particles.base.MAParticleBase;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FXWater extends MAParticleBase {

    public FXWater(ClientLevel world, double x, double y, double z, SpriteSet sprite) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.f_107216_ *= 0.2F;
        this.f_107215_ = 0.0;
        this.f_107217_ = 0.0;
        this.m_107253_(0.8F, 0.96862745F, 1.0F);
        this.m_107271_(0.0F);
        this.f_107231_ = (float) (-45.0 + Math.random() * 90.0 / (Math.PI / 180.0));
        this.f_107204_ = this.f_107231_;
        this.f_107663_ *= 0.075F;
        this.f_107225_ = 30;
        this.f_107219_ = true;
        this.m_108339_(sprite);
    }

    @Override
    public float getQuadSize(float partialTicks) {
        return this.f_107663_ * (1.0F + (float) this.f_107224_ / (float) this.f_107225_);
    }

    @Override
    public void tick() {
        this.f_107204_ = this.f_107231_;
        this.f_107231_ = (float) ((double) this.f_107231_ + Math.random() * Math.PI / 180.0);
        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXWaterFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXWaterFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXWater particle = new FXWater(worldIn, x, y, z, this.spriteSet);
            particle.setMoveVelocity(xSpeed, ySpeed, zSpeed, true);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FXWaterLerpFactory extends MAParticleBase.FXParticleFactoryBase {

        public FXWaterLerpFactory(SpriteSet spriteSet) {
            super(spriteSet);
        }

        @Override
        public Particle createParticle(MAParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FXWater particle = new FXWater(worldIn, x, y, z, this.spriteSet);
            particle.setMoveLerp(x, y, z, xSpeed, ySpeed, zSpeed);
            this.configureParticle(particle, typeIn);
            return particle;
        }
    }
}